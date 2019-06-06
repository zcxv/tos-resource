package ru.rage.tos.resource.ies;

import java.nio.ByteBuffer;

import ru.rage.tos.resource.IIesElement;
import ru.rage.tos.resource.IesUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author PointerRage
 *
 */
@ToString
public class IesColumn implements IIesElement {
	private final IesTable table;
	public IesColumn(IesTable table) {
		this.table = table;
	}
	
	@Getter @Setter private String name;
	@Getter @Setter private String name2;
	@Getter @Setter private IesDataType type;
	@Getter @Setter private short unk1;
	@Getter @Setter private short unk2;
	@Getter @Setter private short unk3;
	
	@Override
	public void read(ByteBuffer buffer) {
		setName(IesUtil.crypt(IesUtil.readS(buffer)));
		buffer.position(buffer.position() + 64 - getName().length() - 1);
		setName2(IesUtil.crypt(IesUtil.readS(buffer)));
		buffer.position(buffer.position() + 64 - getName2().length() - 1);
		setType(IesDataType.values()[buffer.getShort()]);
		setUnk1(buffer.getShort());
		setUnk2(buffer.getShort());
		setUnk3(buffer.getShort());
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		buffer.put(IesUtil.crypt(getName()).getBytes());
		buffer.position(buffer.position() + 64 - getName().length());
		buffer.put(IesUtil.crypt(getName2()).getBytes());
		buffer.position(buffer.position() + 64 - getName2().length());
		buffer.putShort((short) getType().ordinal());
		buffer.putShort(getUnk1());
		buffer.putShort(getUnk2());
		buffer.putShort(getUnk3());
	}
	
	@Override
	public int getElementSize() {
		return 64 +
				64 +
				2 +
				2 +
				2 +
				2;
	}
}
