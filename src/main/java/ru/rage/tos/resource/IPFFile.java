/*
 * Copyright (c) 2010-2019 fork2
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
	
	// Open BETA:
	//  0x00, 0x00, 0x00, 0x00 subversion 
	//  0x01, 0x09, 0x00, 0x00 version
	// Some new files:
	//  0x00, 0x00, 0x00, 0x00 subversion 
	//  0x1b, 0x2b, 0x00, 0x00 version
	
	private int version;
	private int subversion;
	
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
