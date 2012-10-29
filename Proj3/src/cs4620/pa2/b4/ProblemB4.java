package cs4620.pa2.b4;

import java.awt.event.*;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import cs4620.framework.GLFrame;
import cs4620.framework.Texture2D;
import cs4620.framework.Texture2DMipmapped;

public class ProblemB4 extends GLFrame implements KeyListener{
	private static final long serialVersionUID = 1L;
	private Texture2DMipmapped mTexture;
	private Texture2D texture;
	
	private float dist = 5f;
	private boolean useMipmap = false;
	
	public ProblemB4() {
		super("CS 4620/5620 Programming Assignment 2B / Problem 4", 640, 480, 60);		
	}	

	public static void main(String args[]) {
		new ProblemB4().run();
	}
	
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		addKeyListener(this);
		final GL2 gl = drawable.getGL().getGL2();
		
		// We want to show the back of the plane as well
		gl.glDisable(GL2.GL_CULL_FACE);			
				
		try {
			mTexture = new Texture2DMipmapped(gl, "data/textures/checkerboard.jpg");
			texture = new Texture2D(gl, "data/textures/checkerboard.jpg");
		}
		catch (IOException e) {
			System.out.print("Cannot load texture: ");
			System.out.println(e.getMessage());
			terminate();
		}
				
	}
	
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		final GLU glu = new GLU();
			
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
					
		gl.glLoadIdentity();
		glu.gluLookAt(20, 5, 0, 0, 0, 0, 0, 1, 0);
		
		drawTexturedQuad(gl);
	}
		
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		final GLU glu = new GLU();

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(
				45.0f, 
				(double) width / (double) height, 
				0.1f,
				1000.0f);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	protected void drawTexturedQuad(GL2 gl) {
		if (useMipmap) 
			mTexture.use();
		else
			texture.useB4();
		
		gl.glBegin(GL2.GL_QUADS);
		{			
			gl.glTexCoord2f(10, 10);
			gl.glVertex3f( 100.0f, 0.0f, 100.0f);
			gl.glTexCoord2f(0, 10);
			gl.glVertex3f(-100.0f, 0.0f, 100.0f);
			gl.glTexCoord2f(0, 0);
			gl.glVertex3f(-100.0f, 0.0f,-100.0f);
			gl.glTexCoord2f(10, 0);
			gl.glVertex3f( 100.0f, 0.0f,-100.0f);
		}
		gl.glEnd();
		if (useMipmap)
			mTexture.unuse();
		else
			texture.unuse();
	}
	
	public void dispose(GLAutoDrawable drawable) {
		texture.dispose();
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP && dist > 1f)
			dist-=0.1;
		if (e.getKeyCode() == KeyEvent.VK_DOWN && dist < 21f)
			dist+=0.1;
	}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) 
			useMipmap = !useMipmap;
	}
}
