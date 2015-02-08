package ru.rage.tos.resource;

import java.io.File;

import ru.rage.tos.resource.ies.IesTable;

/**
 * @author PointerRage
 *
 */
public interface IIesHandler {
	void fromIes(IesTable iesTable) throws Throwable;
	IesTable toIes(File file) throws Throwable;
}
