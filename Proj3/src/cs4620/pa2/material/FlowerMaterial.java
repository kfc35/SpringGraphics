package cs4620.pa2.material;

import javax.media.opengl.GL2;
import javax.vecmath.Matrix4f;

import cs4620.framework.GlslException;
import cs4620.framework.Program;


public class FlowerMaterial extends GLPhongMaterial {
	
	private Program flowerProgram;
	
	// When light is close enough to directly above flower, the math
	// for bending the flower becomes unstable -- use the Blinn-Phong
	// shader material in this case instead
	private ShaderPhongMaterial unbentMaterial;
	private boolean useFlowerShader = true;
	
	// the height of the flower in object space
	private float flowerHeight = 3.0f;
	
	// matrices passed to the vertex shader as uniforms
	private Matrix4f frameToObj;
	private Matrix4f objToFrame;
	
	// TODO: (Problem 2) Declare any other variables you want to pass as
	// uniforms to your vertex shader here
	private float R, phi;
	
	public FlowerMaterial()
	{
		super();
		unbentMaterial = new ShaderPhongMaterial();
	}
	
	/**
	 * Update the values that will be assigned to the flower shader's
	 * uniforms based on the new position of the light source (expressed
	 * in the coordinate frame of the flower mesh vertices)
	 * @param lightx the light x coordinate in the object frame
	 * @param lighty the light y coordinate in the object frame
	 * @param lightz the light z coordinate in the object frame
	 */
	
	public void setUniforms(float lightx, float lighty, float lightz)
	{
		// the height of the flower mesh in object-space
		float height = flowerHeight;
		
		// find the values L_x and L_y from the figure in pa2a.pdf
		float L_x = (float) Math.sqrt(lightx * lightx + lightz * lightz);
		float L_y = lighty;
		
		if(L_x < 0.00001)
		{
			// If the light is too close to lying directly above the flower, the math
			// for the bending of the flower becomes unstable, so just render
			// the unbent flower
			useFlowerShader = false;
		}
		else
		{
			useFlowerShader = true;
			
			// These matrices map between the frame of the flower mesh's vertices and a frame in which
			// the light lies on the z>0 part of the x-y plane
			frameToObj = new Matrix4f(lightx / L_x, 0, -lightz / L_x, 0,
					                  0,            1,  0,            0,
					                  lightz / L_x, 0,  lightx / L_x, 0,
					                  0,            0,  0,            1);
			
			// Find inverse of frameToObj
			objToFrame = new Matrix4f();
			objToFrame.transpose(frameToObj);
			
			// TODO: (Problem 2) Use L_x and L_y to calculate any other uniforms you
			// want to send to the vertex shader
			double theta = Math.atan(L_y/L_x);
			phi = (float) ((Math.PI/2.0) - theta);
			R = height/phi;
			
		}
	}

	@Override
	public void use(GL2 gl) {
		
		if(flowerProgram == null)
		{
			try
			{
				flowerProgram = new Program(gl, "flower.vp", "flower.fp");
			}
			catch(GlslException e)
			{
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		
		if(useFlowerShader)
		{
			flowerProgram.use();
			
			// set frame transformation matrices
			if(flowerProgram.getUniform("frameToObj") != null)
				flowerProgram.getUniform("frameToObj").setMatrix4(frameToObj);
			else
				System.out.println("Flower shader is ignoring uniform \"frameToObj\"");
			
			if(flowerProgram.getUniform("objToFrame") != null)
				flowerProgram.getUniform("objToFrame").setMatrix4(objToFrame);
			else
				System.out.println("Flower shader is ignoring uniform \"objToFrame\"");
			
			// and lighting parameters
			if(flowerProgram.getUniform("ambient") != null)
				flowerProgram.getUniform("ambient").set4Float(ambient[0], ambient[1], ambient[2], ambient[3]);
			else
				System.out.println("Flower shader is ignoring uniform \"ambient\"");
			
			if(flowerProgram.getUniform("diffuse") != null)
				flowerProgram.getUniform("diffuse").set4Float(diffuse[0], diffuse[1], diffuse[2], diffuse[3]);
			else
				System.out.println("Flower shader is ignoring uniform \"diffuse\"");
			
			if(flowerProgram.getUniform("specular") != null)
				flowerProgram.getUniform("specular").set4Float(specular[0], specular[1], specular[2], specular[3]);
			else
				System.out.println("Flower shader is ignoring uniform \"specular\"");
			
			if(flowerProgram.getUniform("shininess") != null)
				flowerProgram.getUniform("shininess").set1Float(shininess);
			else
				System.out.println("Flower shader is ignoring uniform \"shininess\"");
			
			// TODO: (Problem 2) Send any additional uniforms you calculated in setUniforms() to the shader
			if (flowerProgram.getUniform("R") != null)
				flowerProgram.getUniform("R").set1Float(R);
			else
				System.out.println("Flower shader is ignoring uniform \"R\"");
			
			if (flowerProgram.getUniform("phi") != null)
				flowerProgram.getUniform("phi").set1Float(phi);
			else
				System.out.println("Flower shader is ignoring uniform \"phi\"");
			
			if (flowerProgram.getUniform("height") != null)
				flowerProgram.getUniform("height").set1Float(flowerHeight);
			else
				System.out.println("Flower shader is ignoring uniform \"height\"");
		}
		else
		{
			// render the unbent flower using per-pixel Blinn-Phong
			unbentMaterial.use(gl);
		}
	}

	@Override
	public void unuse(GL2 gl) {
		if(useFlowerShader)
		{
			flowerProgram.unuse();
		}
		else
		{
			unbentMaterial.unuse(gl);
		}

	}

	@Override
	public void dispose(GL2 gl) {
		// NOP

	}
	
	public void setAmbient(float r, float g, float b)
	{
		super.setAmbient(r, g, b);
		unbentMaterial.setAmbient(r, g, b);
	}

	public void setDiffuse(float r, float g, float b)
	{
		super.setDiffuse(r, g, b);
		unbentMaterial.setDiffuse(r, g, b);
	}

	public void setSpecular(float r, float g, float b)
	{
		super.setSpecular(r, g, b);
		unbentMaterial.setSpecular(r, g, b);
	}

	public void setShininess(float shininess)
	{
		super.setShininess(shininess);
		unbentMaterial.setShininess(shininess);
	}

	@Override
	public Object getYamlObjectRepresentation() {
		// irrelevant, since flower is not used with editable scene graph
		return null;
	}

}
