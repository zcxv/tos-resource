package ru.rage.tos.resource.test;

import java.io.File;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;

import ru.rage.tos.resource.IIesHandler;
import ru.rage.tos.resource.ies.IesTable;

/**
 * @author PointerRage
 *
 */
public class IesTestHandler implements IIesHandler {

	@Override
	public void fromIes(IesTable iesTable) throws Throwable {
		File f = new File(iesTable.getHeader().getName() + ".xml");
		f.createNewFile();
		XStream xstream = new XStream();
		try(FileOutputStream fos = new FileOutputStream(f)) {
			xstream.toXML(iesTable, fos);
		}
	}

	@Override
	public IesTable toIes(File file) {
		XStream xstream = new XStream();
		return (IesTable) xstream.fromXML(file);
	}

}
