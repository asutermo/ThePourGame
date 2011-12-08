package org.android.pourgame;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Square {
	// Our vertices.
	protected Bitmap bitmap;
	protected int textureId = -1;
	protected boolean texturize = true;
	
	protected float vertices[] = {
		      -1.0f,  1.0f, 0.0f,  // 0, Top Left
		      -1.0f, -1.0f, 0.0f,  // 1, Bottom Left
		       1.0f, -1.0f, 0.0f,  // 2, Bottom Right
		       1.0f,  1.0f, 0.0f,  // 3, Top Right
		};

	// The order we like to connect them.
	protected short[] indices = { 0, 1, 2, 0, 2, 3 };

	protected float textureCoordinates[] = {
			0.0f, 2.0f,
			0.0f, 0.0f,
			2.0f, 0.0f,
            2.0f, 2.0f,
    };
	
	// Our vertex buffer.
	protected FloatBuffer vertexBuffer, textureBuffer;

	// Our index buffer.
	protected ShortBuffer indexBuffer;

	public Square() {
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		textureBuffer = tbb.asFloatBuffer();
		textureBuffer.put(textureCoordinates);
		textureBuffer.position(0);
	}
	
	public void loadBitmap(Context context, int id) {
		bitmap = BitmapFactory.decodeResource(context.getResources(), id);
	}
	

	/**
	 * This function draws our square on screen.
	 * @param gl
	 */
	public void draw(GL10 gl) {
		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE); 
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK);

		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
                                 vertexBuffer);

		if (texturize && bitmap != null) {
			loadGLTexture(gl);
			texturize = false;
		}
		if (textureId != -1) {
			gl.glEnable(GL10.GL_TEXTURE_2D);
			// Enable the texture state
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	 
			// Point to our buffers
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		}
	 
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				  GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		if (textureId != -1)
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); 
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE); 
	}
	
	private void loadGLTexture(GL10 gl) {
		// Generate one texture pointer...
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		textureId = textures[0];
	 
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	 
		// Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
	 
		// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);

		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	}

	public void setTextureCoordinates(float[] coords) {
		ByteBuffer tbb = ByteBuffer.allocateDirect(coords.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		textureBuffer = tbb.asFloatBuffer();
		textureBuffer.put(coords);
		textureBuffer.position(0);
	}
}