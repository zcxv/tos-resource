package ru.rage.tos.resource.ies;

import java.nio.ByteBuffer;
import java.util.Arrays;

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
public class IesRow implements IIesElement {
	private final IesTable table;
	public IesRow(IesTable table) {
		this.table = table;
	}
	
	@Getter @Setter private int id;
	@Getter @Setter private String key;
	@Getter @Setter private IesData[] data;
	
	@Override
	public void read(ByteBuffer buffer) {
		setId(buffer.getInt());
		setKey(IesUtil.crypt(IesUtil.readSL(buffer)));
		setData(new IesData[table.getHeader().getColumnCount()]);
		readData(IesDataType.Int, table.getHeader().getIntColumns(), buffer);
		readData(IesDataType.String, table.getHeader().getStringColumns(), buffer);
		for(IesData iesData : getData()) {
			if(!iesData.isStringType())
				continue;
			iesData.setFlag(buffer.get());
		}
	}
	
	private void readData(IesDataType type, int max, ByteBuffer buffer) {
		for(int i = 0, column = 0; i < max; i++) {
			for(int j = 0; j < data.length; j++) {
				if(data[j] == null && table.getColumns()[j].getType() == type) {
					column = j;
					break;
				}
			}
			getData()[column] = new IesData(table.getColumns()[column]);
			getData()[column].read(type, buffer);
		}
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		buffer.putInt(getId());
		IesUtil.writeSL(IesUtil.crypt(getKey()), buffer);
		writeData(IesDataType.Int, buffer);
		writeData(IesDataType.String, buffer);
		for(IesData iesData : getData()) {
			if(!iesData.isStringType())
				continue;
			buffer.put(iesData.getFlag());
		}
	}
	
	private void writeData(IesDataType type, ByteBuffer buffer) {
		IesData[] data = Arrays.copyOf(this.data, this.data.length);
		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j < data.length; j++) {
				if(data[j] != null && data[j].isType(type)) {
					data[j].write(buffer);
					data[j] = null;
					break;
				}
			}
		}
	}
	
	@Override
	public int getElementSize() {
		int dataSize = 0;
		for(IesData data : getData())
			dataSize += data.getElementSize();
		return 4 +
				2 + getKey().length() +
				dataSize +
				table.getTypeDataCount(IesDataType.String);
	}
}
