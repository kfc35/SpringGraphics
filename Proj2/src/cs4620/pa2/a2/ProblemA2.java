package cs4620.pa2.a2;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cs4620.framework.CameraController;
import cs4620.framework.GLSceneDrawer;
import cs4620.pa2.material.FlowerMaterial;
import cs4620.pa2.material.GLPhongMaterial;
import cs4620.pa2.scene.MeshNode;
import cs4620.pa2.shape.CustomTriangleMesh;
import cs4620.pa2.shape.Mesh;
import cs4620.pa2.shape.Sphere;
import cs4620.pa2.ui.OneFourViewPanel;
import cs4620.pa2.ui.ToleranceSliderPanel;

public class ProblemA2 extends JFrame implements GLSceneDrawer, ChangeListener {
	private static final long serialVersionUID = 1L;

	ArrayList<Mesh> meshes;

	OneFourViewPanel oneFourViewPanel;
	ToleranceSliderPanel sliderPanel;

	boolean showFourView = true;
	boolean showOneView = false;

	float[] lightAmbient = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
	float[] lightDiffuse = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
	float[] lightSpecular = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
	float[] lightPosition = new float[] { 5, 5, 0, 1.0f};

	float[] ambient = new float[] {0.05f, 0.05f, 0.05f, 0.05f};
	float[] diffuse = new float[] {1.0f, 0.0f, 0.0f, 0.0f};
	float[] specular = new float[] {1.0f, 1.0f, 1.0f, 1.0f};
	float   shininess = 50.0f;
	
	//Scene scene;
	FlowerMaterial flowerMaterial;
	GLPhongMaterial lightMaterial;
	MeshNode flowerGreenNode;
	MeshNode flowerCenterNode;
	MeshNode flowerPetalsNode;
	MeshNode lightSourceNode; 

	public ProblemA2() {
		super("CS 4620/5620 Programming Assignment 2A / Problem 2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent windowevent ) {
            	terminate();
            }
        });
		
		flowerMaterial = new FlowerMaterial();
		flowerMaterial.setAmbient(0.05f, 0.05f, 0.05f);
		flowerMaterial.setDiffuse(0.1f, 0.6f, 0.0f);
		flowerMaterial.setSpecular(0.1f, 0.2f, 0.05f);
		flowerMaterial.setShininess(10);
		
		lightMaterial = new GLPhongMaterial();
		lightMaterial.setAmbient(1, 1, 1);
		lightMaterial.setDiffuse(0,0,0);
		lightMaterial.setSpecular(0,0,0);
		
		// create flower components and light source ball
		try{
			flowerGreenNode = new MeshNode("Flower Green", new CustomTriangleMesh(new File("data/meshes/flower_green.msh")), flowerMaterial);
			flowerCenterNode = new MeshNode("Flower Center", new CustomTriangleMesh(new File("data/meshes/flower_center.msh")), flowerMaterial);
			flowerPetalsNode = new MeshNode("Flower Petals", new CustomTriangleMesh(new File("data/meshes/flower_petals.msh")), flowerMaterial);
			
			lightSourceNode = new MeshNode("Light Ball", new Sphere(), lightMaterial);
			lightSourceNode.getMesh().buildMesh(0.133f);
		}
		catch (Exception e) {
			showExceptionDialog(e);
		}

		oneFourViewPanel = new OneFourViewPanel(this);
		getContentPane().add(oneFourViewPanel, BorderLayout.CENTER);

		sliderPanel = new ToleranceSliderPanel(this);
		getContentPane().add(sliderPanel, BorderLayout.EAST);
		
		// set light source position and dependent state
		setLightFromSlider();
	}
		
	/**
	 * Displays an exception in a window
	 * @param e
	 */
	protected void showExceptionDialog(Exception e)
	{
		String str = "The following exception was thrown: " + e.toString() + ".\n\n" + "Would you like to see the stack trace?";
		int choice = JOptionPane.showConfirmDialog(this, str, "Exception Thrown", JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION) {
			e.printStackTrace();
		}
	}

	public void run()
	{
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		oneFourViewPanel.startAnimation();
	}

	public static void main(String[] args)
	{
		new ProblemA2().run();
	}

	public void init(GLAutoDrawable drawable, CameraController cameraController) {
		final GL2 gl = drawable.getGL().getGL2();

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Set depth buffer.
		gl.glClearDepth(1.0f);
		gl.glDepthFunc(GL2.GL_LESS);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		// Set blending mode.
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL2.GL_BLEND);

		// Forces OpenGL to normalize transformed normals to be of
		// unit length before using the normals in OpenGL's lighting equations.
		gl.glEnable(GL2.GL_NORMALIZE);

		// Cull back faces.
		gl.glDisable(GL2.GL_CULL_FACE);

		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

		oneFourViewPanel.startAnimation();
	}


	public void draw(GLAutoDrawable drawable, CameraController cameraController) {
		final GL2 gl = drawable.getGL().getGL2();

		gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		if (oneFourViewPanel.isLightingMode())
		{
			gl.glEnable(GL2.GL_LIGHTING);
			gl.glEnable(GL2.GL_LIGHT0);

			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0 );
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0 );
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpecular, 0 );
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
		}
		else
		{
			gl.glDisable(GL2.GL_LIGHTING);
			gl.glDisable(GL2.GL_LIGHT0);
		}

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
		gl.glColor4f(diffuse[0], diffuse[1], diffuse[2], diffuse[3]);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
		gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);

		if (oneFourViewPanel.isWireframeMode())
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		else
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		// light source
		gl.glPushMatrix();
		gl.glTranslated(lightPosition[0], lightPosition[1], lightPosition[2]);
		lightSourceNode.draw(gl);
		gl.glPopMatrix();
		
		// manually draw the three parts of the flower, changing the color between
		// green parts (stem and leaves):
		flowerMaterial.setAmbient(0.1f, 0.1f, 0.1f);
		flowerMaterial.setDiffuse(0.1f, 0.6f, 0.0f);
		flowerMaterial.setSpecular(0.1f, 0.2f, 0.05f);
		flowerMaterial.setShininess(10);
		flowerGreenNode.draw(gl);
		
		// center of flower:
		flowerMaterial.setAmbient(0.05f, 0.05f, 0.05f);
		flowerMaterial.setDiffuse(0.4f, 0.2f, 0.1f);
		flowerMaterial.setSpecular(0.0f, 0.0f, 0.0f);
		flowerMaterial.setShininess(10);
		flowerCenterNode.draw(gl);
		
		// petals of flower:
		flowerMaterial.setAmbient(0.05f, 0.05f, 0.05f);
		flowerMaterial.setDiffuse(0.7f, 0.7f, 0.05f);
		flowerMaterial.setSpecular(0.3f, 0.2f, 0.005f);
		flowerMaterial.setShininess(10);
		flowerPetalsNode.draw(gl);
	}
	
	public void setLightPosition(float x, float y, float z)
	{
		lightPosition[0] = x;
		lightPosition[1] = y;
		lightPosition[2] = z;
		
		flowerMaterial.setUniforms(lightPosition[0], lightPosition[1], lightPosition[2]);
	}
	
	private void setLightFromSlider()
	{
		// move the light source around in a sinusoidal pattern
		float coeff = sliderPanel.getRawValue();
		setLightPosition(10 * (float) Math.sin(10 * Math.PI * coeff), 7 + 2 * (float) Math.cos(5 * Math.PI * coeff), 6 * (float) Math.cos(5 * Math.PI * coeff));
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == sliderPanel.getSlider())
		{
			setLightFromSlider();
		}
	}

	protected void updateMeshTolerance(float tolerance)
	{
		for(Mesh mesh : meshes)
			mesh.buildMesh(tolerance);
	}

	public void terminate()
	{
		oneFourViewPanel.stopAnimation();
		dispose();
		System.exit(0);
	}

	@Override
	public void mousePressed(MouseEvent e, CameraController controller) {
		// NOP

	}

	@Override
	public void mouseReleased(MouseEvent e, CameraController controller) {
		// NOP

	}

	@Override
	public void mouseDragged(MouseEvent e, CameraController controller) {
		// NOP

	}
}
