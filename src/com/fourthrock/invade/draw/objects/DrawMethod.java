package com.fourthrock.invade.draw.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * Specifies how the DrawObject will interpret its vertices.
 * 
 * @author Joseph
 */
public class DrawMethod {
	public final int drawMode;
	public final boolean drawsInOrder;
	public final int vertexCount;
	public final ShortBuffer drawList;
	
	
	private DrawMethod(final int drawMode, final boolean drawsInOrder, final int vertexCount,
						final ShortBuffer drawList) {
		this.drawMode = drawMode;
		this.drawsInOrder = drawsInOrder;
		this.vertexCount = vertexCount;
		this.drawList = drawList;
	}
	
	/**
	 * Draw the vertices in order.
	 */
	public DrawMethod(final int drawMode, final int vertexCount) {
		this(drawMode, true, vertexCount, null);
	}
	
	/**
	 * Draw the vertices according to a draw list buffer.
	 */
	public DrawMethod(final int drawMode, final short[] drawList) {
		this(drawMode, false, drawList.length, shortBufferFromArray(drawList));
	}
	

	/**
	 * Creates a properly formatted ShortBuffer to hold draw orders.
	 */
	private static ShortBuffer shortBufferFromArray(final short[] drawOrder) {
		final ShortBuffer drawBuffer = ByteBuffer
				.allocateDirect(drawOrder.length * 2)
				.order(ByteOrder.nativeOrder()).asShortBuffer().put(drawOrder);
		drawBuffer.position(0);
		return drawBuffer;
	}
}
