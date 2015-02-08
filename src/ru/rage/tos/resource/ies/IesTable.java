package ru.rage.tos.resource.ies;

import java.nio.ByteBuffer;

import ru.rage.tos.resource.IIesElement;
import lombok.Getter;
import lombok.Setter;

/**
 * @author PointerRage
 *
 */
public class IesTable implements IIesElement {
	@Getter @Setter private IesHeader header;
	@Getter @Setter private IesColumn[] columns;
	@Getter @Setter private IesRow[] rows;
	
	@Override
	public void read(ByteBuffer buffer) {
		header = new IesHeader(this);
		header.read(buffer);
		
		buffer.position(header.getColumnsOffset());
		columns = new IesColumn[header.getColumnCount()];
		for(int i = 0; i < columns.length; i++) {
			columns[i] = new IesColumn(this);
			columns[i].read(buffer);
		}
		
		rows = new IesRow[header.getRowCount()];
		for(int i = 0; i < rows.length; i++) {
			rows[i] = new IesRow(this);
			rows[i].read(buffer);
		}
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		header.write(buffer);
		
		for(IesColumn column : columns)
			column.write(buffer);
		
		for(IesRow row : rows)
			row.write(buffer);
	}
	
	@Override
	public int getElementSize() {
		return header.getElementSize() +
				getColumnsSize() +
				getRowsSize();
	}
	
	public int getColumnsSize() {
		int columnsSize = 0;
		for(IesColumn column : columns)
			columnsSize += column.getElementSize();
		return columnsSize;
	}
	
	public int getRowsSize() {
		int rowsSize = 0;
		for(IesRow row : rows)
			rowsSize += row.getElementSize();
		return rowsSize;
	}
	
	public int getTypeDataCount(IesDataType type) {
		int count = 0;
		for(IesColumn column : getColumns())
			if(column.getType() == type)
				count++;
		return count;
	}
}
