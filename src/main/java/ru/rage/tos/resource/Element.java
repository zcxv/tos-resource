package ru.rage.tos.resource;

import java.io.File;

/**
 * @author PointerRage
 *
 */
public class Element {
	private final static byte[] tail = {
		0x50, 0x4B, 0x05, 0x06, //zip magic
		0x00, 0x00, 0x00, 0x00, 0x01, 0x09, 0x00, 0x00 //game magic
	};
	public static byte[] getTail() {
		return tail;
	}
	
	private File file;
	private String name;
	private String archive; //'extra' in zip
	private int crc;
	private int compressedSize;
	private int originalSize;
	private int fileOffset;
	private byte[] data;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArchive() {
		return archive;
	}
	public void setArchive(String archive) {
		this.archive = archive;
	}
	public int getCrc() {
		return crc;
	}
	public void setCrc(int crc) {
		this.crc = crc;
	}
	public int getCompressedSize() {
		return compressedSize;
	}
	public void setCompressedSize(int compressedSize) {
		this.compressedSize = compressedSize;
	}
	public int getOriginalSize() {
		return originalSize;
	}
	public void setOriginalSize(int originalSize) {
		this.originalSize = originalSize;
	}
	public int getFileOffset() {
		return fileOffset;
	}
	public void setFileOffset(int fileOffset) {
		this.fileOffset = fileOffset;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
