package ru.rage.tos.resource;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author zcxv
 * @date 06.06.2019
 */
public class PkwareOutputStream extends OutputStream {

	private final int[] key = { 0x12345678, 0x23456789, 0x34567890 };
	private final CRC32 crc32 = new CRC32();

	private final OutputStream os;

	private int offset;

	public PkwareOutputStream(String password, OutputStream os) {
		generateKey(password);
		this.os = os;
	}

	public PkwareOutputStream(byte[] password, OutputStream os) {
		generateKey(password);
		this.os = os;
	}

	private void generateKey(String password) {
		generateKey(password.getBytes());
	}

	private void generateKey(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			updateKey(bytes[i]);
		}
	}

	private void updateKey(byte data) {
		key[0] = crc32.calculate(key[0], data);
		key[1] += key[0] & 0xff;
		key[1] = key[1] * 0x08088405 + 1;
		key[2] = crc32.calculate(key[2], (byte) (key[1] >> 24));
	}

	@Override
	public void write(int uByte) throws IOException {
		try {
			if ((offset % 2) != 0) {
				os.write(uByte);
				return;
			}

			int magicByte = (this.key[2] & 0xffff) | 0x02;
			int encrypted = uByte ^ (((magicByte * (magicByte ^ 1)) >> 8) & 0xff);
			updateKey((byte)uByte);
			os.write(encrypted);
		} finally {
			offset++;
		}
	}
	
	@Override
	public void flush() throws IOException {
		os.flush();
	}

	@Override
	public void close() throws IOException {
		os.close();
	}

}
