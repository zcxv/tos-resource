package ru.rage.tos.resource;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import lombok.Data;

/**
 *
 * @author zcxv
 * @date 06.06.2019
 */
@Data
public class IPFFile {
	public final static byte[] password = {
			0x6f, 0x66, 0x4f, 0x31, 0x61, 0x30, 0x75, 0x65, 0x58, 0x41, 
			0x3f, 0x20, 0x5b, (byte)0xff, 0x73, 0x20, 0x68, 0x20, 0x25, 0x3f
	};
	
	// Open BETA:
	//  0x00, 0x00, 0x00, 0x00 subversion 
	//  0x01, 0x09, 0x00, 0x00 version
	// Some new files:
	//  0x00, 0x00, 0x00, 0x00 subversion 
	//  0x1b, 0x2b, 0x00, 0x00 version
	
	private int version;
	private int subversion;
	
	private File archiveFile;
	
	public void save(File dir) {
		try(ByteChannel channel = Files.newByteChannel(new File(dir, "version").toPath(), 
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
			ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
			buffer.putInt(version);
			buffer.putInt(subversion);
			channel.write(buffer);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	public void restore(File dir) {
		try(ByteChannel channel = Files.newByteChannel(new File(dir, "version").toPath(), StandardOpenOption.READ)) {
			ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
			channel.read(buffer);
			
			version = buffer.getInt();
			subversion = buffer.getInt();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
}
