package ru.rage.tos.resource.ies;

import java.nio.ByteBuffer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.rage.tos.resource.IIesElement;

/**
 * @author PointerRage
 *
 */
@ToString
public class IesData implements IIesElement {
	private final IesColumn column;
	public IesData(IesColumn column) {
		this.column = column;
	}
	
	@Getter @Setter private Object data;
	@Getter @Setter private byte flag;
	
	@Override
	public void read(ByteBuffer buffer) {
		read(column.getType(), buffer);
	}
	
	public void read(IesDataType type, ByteBuffer buffer) {
		setData(type.read(buffer));
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		column.getType().write(getData(), buffer);
	}
	
	@Override
	public int getElementSize() {
		return column.getType().getElementSize(getData());
	}
	
	public boolean isIntType() {
		return column.getType() == IesDataType.Int;
	}
	
	public boolean isStringType() {
		return column.getType() == IesDataType.String;
	}
	
	public boolean isType(IesDataType type) {
		return column.getType() == type;
	}
}
