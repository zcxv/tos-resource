package ru.rage.tos.resource;

/**
 * Base {@link wgp.server.utils.CRC64}
 *
 * @author mangol
 */
public class CRC32 {
	/** CRC-32-IEEE 802.3 */
	private final static int polynom = 0xEDB88320;
	private final static int[] table = new int[256];

	static {
		for(int i = 0; i < table.length; i++) {
			int value = i;
			for(int j = 0; j < 8; j++) {
				if((value & 1) == 1) {
					value = (value >>> 1) ^ polynom;
				}
				else {
					value = value >>> 1;
				}
			}

			table[i] = value;
		}
	}

	private int hash = 0xFFFFFFFF;

	public int getValue() {
		return hash ^ 0xffffffff;
	}
	
	public int calculate(int crc, byte b) {
		return (table[((crc ^ b) & 0xFF)] ^ (crc >>> 8));
	}

	public int update(byte b) {
		hash = table[((hash ^ b) & 0xFF)] ^ (hash >>> 8);
		return getValue();
	}

	public int update(byte[] bytes) {
		return update(bytes, 0, bytes.length);
	}

	public int update(byte[] bytes, int offset, int length) {
		for(int i = offset; i < length; i++) {
			hash = table[((hash ^ bytes[i]) & 0xFF)] ^ (hash >>> 8);
		}
		return getValue();
	}

	public void reset() {
		hash = 0xFFFFFFFF;
	}
}
