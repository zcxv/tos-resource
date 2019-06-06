package ru.rage.tos.resource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * @author PointerRage
 *
 */
public class Packer {
	private final static Map<String, List<Element>> map = new HashMap<>();
	private static File in = new File("./pack");
	private static int level = 8;
	private static ThreadPoolExecutor exec;
	public static void main(String...args) {
		{
			int cpu = Math.max(Runtime.getRuntime().availableProcessors() / 3, 1);
			exec = (ThreadPoolExecutor) Executors.newFixedThreadPool(cpu);
			System.out.printf("Selected %d threads\r\n", cpu);
		}
		
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-input") || args[i].equals("-in"))
				in = new File(args[++i]);
			else if(args[i].equals("-level") || args[i].equals("-lvl"))
				level = Integer.parseInt(args[++i]);
		}
		
		if(!in.exists()) {
			System.out.println("input folder doesnt exists");
			return;
		}
		
		for(File f : in.listFiles(file -> file.isDirectory()))
			scan(f, "", f.getName());
		
		for(Entry<String, List<Element>> entry : map.entrySet())
			exec.execute(() -> process(entry.getKey(), entry.getValue()));
		exec.shutdown();
	}
	
	private static void scan(File file, String name, String archive) {
		if(file.isFile()) {
			if(file.getName().equals("version")) {
				return;
			}
			
			Element e = new Element();
			e.setFile(file);
			e.setName(name.substring(1, name.length()));
			List<Element> elements = map.get(archive);
			if(elements == null)
				map.put(archive, elements = new ArrayList<>());
			elements.add(e);
			return;
		}
		
		for(File f : file.listFiles()) {
			scan(f, "/" + f.getName(), archive);
		}
	}
	
	private static void process(String archive, List<Element> elements) {
		long processStartPack = System.currentTimeMillis();
		
		IPFFile ipfFile = new IPFFile();
		ipfFile.restore(new File(in, archive));
		
		File out = new File("./", archive);
		try {
			out.createNewFile();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		ipfFile.setArchiveFile(out);
		
		
		try(RandomAccessFile raf = new RandomAccessFile(out, "rw"); FileChannel ch = raf.getChannel()) {
			int offset = 0;
			CRC32 crc = new CRC32();
			for(Element element : elements) { //write compressed files
				byte[] buffer = Files.readAllBytes(element.getFile().toPath());
				
				final OutputStream os;
				final Deflater deflater = new Deflater(level, true);
				final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				if(ipfFile.getVersion() >= 11035) {
					os = new DeflaterOutputStream(new PkwareOutputStream(IPFFile.password, baos), deflater);
				} else {
					os = new DeflaterOutputStream(baos, deflater);
				}
				
				try {
					os.write(buffer, 0, buffer.length);
					os.flush();
				} finally {
					os.close();
					deflater.end();
				}
				
				crc.reset();
				crc.update(buffer);
				element.setCrc((int)crc.getValue());
				
				element.setOriginalSize(buffer.length);
					
				buffer = baos.toByteArray();
				element.setCompressedSize(buffer.length);
				element.setFileOffset(offset);
				offset += buffer.length;
				
				ch.write(ByteBuffer.wrap(buffer));
			}
			
			int doffset = offset;
			byte[] barchive = archive.getBytes();
			ByteBuffer buffer = ByteBuffer.allocate(20 + 2*512).order(ByteOrder.LITTLE_ENDIAN);
			for(Element element : elements) { //write file table
				byte[] fname = element.getName().getBytes();
				buffer.putShort((short)fname.length); //2
				buffer.putInt(element.getCrc()); //6
				buffer.putInt(element.getCompressedSize()); //10
				buffer.putInt(element.getOriginalSize()); //14
				buffer.putInt(element.getFileOffset()); //18
				
				buffer.putShort((short)barchive.length); //20
				buffer.put(barchive); //
				buffer.put(fname);
				
				buffer.flip();
				doffset += buffer.limit();
				ch.write(buffer);
				buffer.clear();
			}
			
			{ //write data
				buffer.putShort((short)elements.size());
				buffer.putInt(offset);
				buffer.putShort((short)0x00); //general purpose bit flag
				buffer.putInt(doffset);
			}
			
			{ //write tail
				buffer.put(Element.getZipMagic());
				buffer.putInt(ipfFile.getSubversion());
				buffer.putInt(ipfFile.getVersion());
				buffer.flip();
				ch.write(buffer);
			}
		} catch(IOException e) {
			System.out.println("FLUSH FLUSH!!!");
			return;
		}
		System.out.printf("%s done! %d/%d [%d -> %d ms]\r\n", 
				archive, 
				map.size() - exec.getQueue().size() - exec.getActiveCount() + 1, map.size(), 
				elements.size(), 
				System.currentTimeMillis() - processStartPack);
	}
}
