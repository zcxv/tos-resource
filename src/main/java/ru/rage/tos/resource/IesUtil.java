package ru.rage.tos.resource;

import java.nio.ByteBuffer;

/**
 * @author PointerRage
 *
 */
public class IesUtil {
	public static String readS(ByteBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while((b = buffer.get()) != 0x00)
			sb.append((char)b);
		return sb.toString();
	}
	
	public static String readSL(ByteBuffer buffer) {
		int len = buffer.getShort();
		byte[] bytes = new byte[len];
		buffer.get(bytes);
		return new String(bytes);
	}
	
	public static void writeSL(String val, ByteBuffer buffer) {
		int len = val.length();
		buffer.putShort((short)len);
		byte[] bytes = val.getBytes();
		buffer.put(bytes);
	}
	
	public static String crypt(String str) {
		byte[] bytes = str.getBytes();
		for(int i = 0; i < bytes.length; i++)
			bytes[i] ^= 0x1;
		return new String(bytes);
	}
	
	public static int crypt(int val) {
		/*
		_put(a + 3, int3(x));
        _put(a + 2, int2(x));
        _put(a + 1, int1(x));
        _put(a    , int0(x));
        */
		byte[] bytes = {int0(val), int1(val), int2(val), int3(val)};
		for(int i = 0; i < bytes.length; i++)
			bytes[i] ^= 0x1;
		return makeInt(bytes[3], bytes[2], bytes[1], bytes[0]);
	}
	
    private static byte int3(int x) { return (byte)(x >> 24); }
    private static byte int2(int x) { return (byte)(x >> 16); }
    private static byte int1(int x) { return (byte)(x >>  8); }
    private static byte int0(int x) { return (byte)(x      ); }
    
    static private int makeInt(byte b3, byte b2, byte b1, byte b0) {
        return (((b3       ) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) <<  8) |
                ((b0 & 0xff)      ));
    }
}
