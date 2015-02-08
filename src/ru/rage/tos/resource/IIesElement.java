package ru.rage.tos.resource;

import java.nio.ByteBuffer;

/**
 * @author PointerRage
 *
 */
public interface IIesElement {
	void read(ByteBuffer buffer);
	void write(ByteBuffer buffer);
	int getElementSize();
}
