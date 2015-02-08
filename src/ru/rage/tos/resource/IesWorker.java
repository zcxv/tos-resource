package ru.rage.tos.resource;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import lombok.extern.slf4j.Slf4j;
import ru.rage.tos.resource.ies.IesTable;

/**
 * @author PointerRage
 *
 */
@Slf4j
public class IesWorker {
	public static void main(String[] args) {
		IIesHandler handler = null;
		File file = null;
		boolean isPack = false;
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-file"))
				file = new File(args[++i]);
			else if(args[i].equals("-pack"))
				isPack = true;
			else if(args[i].equals("-handler"))
				try {
					handler = (IIesHandler) Class.forName(args[++i]).newInstance();
				} catch(Throwable e) {
					log.error("Failed init IesHandler!", e);
					return;
				}
		}
		
		if(file == null || !file.exists()) {
			log.error("File '{}' doesnt exists", file);
			return;
		}
		
		if(handler == null) {
			log.error("IesHandler not found!");
			return;
		}
		
		if(file.isDirectory()) {
			for(File f : file.listFiles(f -> f.isFile()))
				process(isPack, f, handler);
			return;
		}
		
		process(isPack, file, handler);
	}
	
	private static void process(boolean isPack, File f, IIesHandler handler) {
		if(isPack)
			try {
				IesTable iesTable = handler.toIes(f);
				serialize(iesTable);
			} catch(Throwable e) {
				log.error("Failed serialize file '{}' to IES", f, e);
			}
		else
			try {
				IesTable iesTable = deserialize(f);
				handler.fromIes(iesTable);
			} catch(Throwable e) {
				log.error("Failed deserialize file '{}' from IES", f, e);
			}
	}
	
	private static void serialize(IesTable iesTable) throws Throwable {
		File f = new File(iesTable.getHeader().getName() + ".ies2");
		f.createNewFile();
		try(RandomAccessFile raf = new RandomAccessFile(f, "rw"); FileChannel fc = raf.getChannel()) {
			ByteBuffer buffer = ByteBuffer.allocate(iesTable.getElementSize() + 1).order(ByteOrder.LITTLE_ENDIAN);
			iesTable.write(buffer);
			buffer.flip();
			fc.write(buffer);
		}
	}
	
	private static IesTable deserialize(File file) throws Throwable {
		IesTable iesTable;
		try(RandomAccessFile raf = new RandomAccessFile(file, "r"); FileChannel fc = raf.getChannel()) {
			ByteBuffer buffer = fc.map(MapMode.READ_ONLY, 0, raf.length()).order(ByteOrder.LITTLE_ENDIAN);
			iesTable = new IesTable();
			iesTable.read(buffer);
		}
		
		return iesTable;
	}
	
	
	

}
