package com.fourthrock.invade.draw.objects;

import java.nio.FloatBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureDrawObject extends Square {
	private final int textureDataHandle;
	private final FloatBuffer textureCoords;
	
	public TextureDrawObject(final int program, final String textureFile) {
		super(program);
		this.textureDataHandle = loadTexture(textureFile);
		textureCoords = vertexBuffer.duplicate();
	}
	
	@Override
	public void draw(final float[] mvpMat, final float[] color) {
		final int positionHandle = super.setup(mvpMat, color);

		final HandleObject h = new HandleObject(program);
		textureCoords.position(0);
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);
	    GLES20.glUniform1i(h.texture, 0); 
	    GLES20.glVertexAttribPointer(h.textureCoord, 3, GLES20.GL_FLOAT, false, 0, textureCoords);
	    GLES20.glEnableVertexAttribArray(h.textureCoord);

	    super.disablingDraw(positionHandle);
	}
	
	private static class HandleObject {
		public final int texture, textureCoord;
		
		public HandleObject(final int program) {
			texture      = GLES20.glGetAttribLocation(program, "u_Texture");
			textureCoord = GLES20.glGetAttribLocation(program, "a_TexCoordinate");
		}
	}
	
	private static int loadTexture(final String fileName) {
	    final int[] textureHandle = new int[1];
	    GLES20.glGenTextures(1, textureHandle, 0);

	    if (textureHandle[0] != 0) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inScaled = false;
	        final Bitmap bitmap = BitmapFactory.decodeFile(fileName);

	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
	        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	        bitmap.recycle();
	    }

	    if (textureHandle[0] == 0) {
	        throw new RuntimeException("Error loading texture.");
	    }
	    return textureHandle[0];
	}
	
}
