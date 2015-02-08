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
public class IesHeader implements IIesElement {
	private final IesTable table;
	public IesHeader(IesTable table) {
		this.table = table;
	}
	
	@Getter @Setter private String name;
	@Getter @Setter private int flag;
	@Getter @Setter private int columnSize;
	@Getter @Setter private int rowSize;
	@Getter @Setter private int size;
	@Getter @Setter private short flag2;
	@Getter @Setter private short rowCount;
	@Getter @Setter private short columnCount;
	@Getter @Setter private short intColumns;
	@Getter @Setter private short stringColumns;
	@Getter @Setter private short unkColumns;
	
	public int getColumnsOffset() {
		return size - columnSize - rowSize;
	}
	
	public int getRowsOffset() {
		return size - rowSize;
	}
	
	@Override
	public void read(ByteBuffer buffer) {
		setName(IesUtil.readS(buffer)); //name, 128 bytes (with null-bytes)
		buffer.position(buffer.position() + 128 - getName().length() - 1); //skip null-bytes
		setFlag(buffer.getInt());
		setColumnSize(buffer.getInt());
		setRowSize(buffer.getInt());
		setSize(buffer.getInt());
		setFlag2(buffer.getShort());
		setRowCount(buffer.getShort());
		setColumnCount(buffer.getShort());
		setIntColumns(buffer.getShort());
		setStringColumns(buffer.getShort());
		setUnkColumns(buffer.getShort());
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		update();
		
		buffer.put(getName().getBytes());
		buffer.position(buffer.position() + 128 - getName().length());
		buffer.putInt(getFlag());
		buffer.putInt(getColumnSize());
		buffer.putInt(getRowSize());
		buffer.putInt(getSize());
		buffer.putShort(getFlag2());
		buffer.putShort(getRowCount());
		buffer.putShort(getColumnCount());
		buffer.putShort(getIntColumns());
		buffer.putShort(getStringColumns());
		buffer.putShort(getUnkColumns());
	}
	
	public void update() {
		setSize(table.getElementSize());
		setColumnSize(table.getColumnsSize());
		setRowSize(table.getRowsSize());
		
		setColumnCount((short) table.getColumns().length);
		setRowCount((short) table.getRows().length);
		
		setIntColumns((short)table.getTypeDataCount(IesDataType.Int));
		setStringColumns((short)table.getTypeDataCount(IesDataType.String));
	}
	
	@Override
	public int getElementSize() {
		return 128 + 
				4 +
				4 +
				4 +
				4 +
				2 +
				2 +
				2 +
				2 +
				2 +
				2;
	}
}
