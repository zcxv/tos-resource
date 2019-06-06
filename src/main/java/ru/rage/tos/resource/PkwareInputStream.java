package ru.rage.tos.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zcxv
 * @date 06.06.2019
 */
public class PkwareInputStream extends InputStream {

	private final int[] key = { 0x12345678, 0x23456789, 0x34567890 };
	private final CRC32 crc32 = new CRC32();

	private final InputStream is;

	private int offset;

	public PkwareInputStream(String password, InputStream is) {
		generateKey(password);
		this.is = is;
	}

	public PkwareInputStream(byte[] password, InputStream is) {
		generateKey(password);
		this.is = is;
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
	public int read() throws IOException {
		try {
			if ((offset % 2) != 0) {
				return is.read();
			}

			int uByte = is.read() & 0xff;
			int magicByte = (this.key[2] & 0xffff) | 0x02;
			uByte ^= ((magicByte * (magicByte ^ 1)) >> 8) & 0xff;
			updateKey((byte)uByte);
			return uByte;
		} finally {
			offset++;
		}
	}

	@Override
	public int available() throws IOException {
		return is.available();
	}

	@Override
	public void close() throws IOException {
		is.close();
	}

}
