package cs4620.framework;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;

import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class Texture2DMipmapped extends TextureTwoDim {
	private static GLUgl2 glu = new GLUgl2();
		
	public Texture2DMipmapped(GL2 gl)
	{
		super(gl, GL2.GL_TEXTURE_2D, GL2.GL_RGBA);
	}
	
	public Texture2DMipmapped(GL2 gl, int internalFormat)
	{
		super(gl, GL2.GL_TEXTURE_2D, internalFormat);
	}
	
	public Texture2DMipmapped(GL2 gl, String filename) throws IOException
	{
		super(gl, GL2.GL_TEXTURE_2D, GL2.GL_RGBA);
		File file = new File(filename);
		TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), file, false, null);
		if (data.getMustFlipVertically())
		{
			BufferedImage image = ImageIO.read(file);
			ImageUtil.flipImageVertically(image);
			data = AWTTextureIO.newTextureData(GLProfile.getDefault(), image, false);			
		}
		setImage(data);
	}
	
	public Texture2DMipmapped(GL2 gl, String filename, int internalFormat) throws IOException
	{
		super(gl, GL2.GL_TEXTURE_2D, internalFormat);
		File file = new File(filename);		
		TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), file, false, null);
		if (data.getMustFlipVertically())
		{
			BufferedImage image = ImageIO.read(file);
			ImageUtil.flipImageVertically(image);
			data = AWTTextureIO.newTextureData(GLProfile.getDefault(), image, false);			
		}
		setImage(data);
	}
	
	public Texture2DMipmapped(GL2 gl, File file) throws IOException
	{
		super(gl, GL2.GL_TEXTURE_2D, GL2.GL_RGBA);
		TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), file, false, null);
		if (data.getMustFlipVertically())
		{
			BufferedImage image = ImageIO.read(file);
			ImageUtil.flipImageVertically(image);
			data = AWTTextureIO.newTextureData(GLProfile.getDefault(), image, false);			
		}
		setImage(data);
	}
	
	public Texture2DMipmapped(GL2 gl, File file, int internalFormat) throws IOException
	{
		super(gl, GL2.GL_TEXTURE_2D, internalFormat);
		TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), file, false, null);
		if (data.getMustFlipVertically())
		{
			BufferedImage image = ImageIO.read(file);
			ImageUtil.flipImageVertically(image);
			data = AWTTextureIO.newTextureData(GLProfile.getDefault(), image, false);			
		}
		setImage(data);
	}
	
	@Override
	public void setImage(int width, int height, int format, int type, Buffer buffer)
	{
		this.width = width;
		this.height = height;
		
		Texture oldTexture = TextureUnit.getActiveTextureUnit(gl).getBoundTexture();
		if (oldTexture != this)
			bind();
		
		// Shouldn't be using this. 
		// glu.gluBuild2DMipmaps(target, internalFormat, width, height, format, type, buffer);
		setTextureParameters();
		gl.glTexImage2D(target, 0, internalFormat, width, height, 0, format, type, buffer);
		
		if (oldTexture == null)
			unbind();
		else if (oldTexture != this)
			oldTexture.bind();
		
		allocated = true;
	}
	
	@Override
	public void setTextureParameters() {
		// TODO PA2B: (Problem 4) Fill in code here to set the texture parameters to generate a mipmap
		
		/* Texture repeats itself in the x, y, and z directions*/
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_R, GL2.GL_REPEAT);
		
		/* gl knows to generate mipmaps*/
		gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
		
		/* gl knows to use linear interpolation for tex values and mipmap level*/
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
		//gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
	}
}
