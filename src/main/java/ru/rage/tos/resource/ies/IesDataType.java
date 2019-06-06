package ru.rage.tos.resource.ies;

import java.nio.ByteBuffer;

import ru.rage.tos.resource.IesUtil;

/**
 * @author PointerRage
 *
 */
public enum IesDataType {
	Int {
		@Override
		public Object read(ByteBuffer buffer) {
			return buffer.getInt();
		}
		
		@Override
		public void write(Object object, ByteBuffer buffer) {
			buffer.putInt((int)object);
		}
		
		@Override
		public int getElementSize(Object object) {
			return 4;
		}
	},
	String {
		@Override
		public Object read(ByteBuffer buffer) {
			return IesUtil.crypt(IesUtil.readSL(buffer));
		}
		
		@Override
		public void write(Object object, ByteBuffer buffer) {
			IesUtil.writeSL(IesUtil.crypt((String)object), buffer);
		}
		
		@Override
		public int getElementSize(Object object) {
			return 2 + ((String)object).length();
		}
	};
	
	public abstract Object read(ByteBuffer buffer);
	public abstract void write(Object object, ByteBuffer buffer);
	public abstract int getElementSize(Object object);
}
