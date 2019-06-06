package ru.rage.tos.resource;

import java.io.File;

/**
 * @author PointerRage
 *
 */
public class Element {
	private final static byte[] tail = {
		0x50, 0x4B, 0x05, 0x06, //zip magic
		0x00, 0x00, 0x00, 0x00, //see IPFFile
		0x00, 0x00, 0x00, 0x00 //see IPFFile
	};
	public static byte[] getTail() {
		return tail;
	}
	
	private final static byte[] zipMagic = {
			0x50, 0x4B, 0x05, 0x06	
	};
	public static byte[] getZipMagic() {
		return zipMagic;
	}
	
	private File archiveFile;
	
	private File file;
	private String name;
	private String archive; //'extra' in zip
	private int crc;
	private int compressedSize;
	private int originalSize;
	private int fileOffset;
	private byte[] data;
	
	public File getArchiveFile() {
		return archiveFile;
	}
	public void setArchiveFile(File archiveFile) {
		this.archiveFile = archiveFile;
	}
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
