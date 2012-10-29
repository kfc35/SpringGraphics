package cs4620.pa2.material;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL2;
import cs4620.framework.*;

public class ShaderFireMaterial extends Material{
	
	private static Program program = null;
	
	private Texture2D noiseTexture = null;
	private Texture2D fireTexture = null;
	private float time = 0f;
	
	public final float[] scrollSpeeds = new float[] {1f, 2f, 4f};
	
	
	public ShaderFireMaterial() {
		super();
	}

	@Override
	public void use(GL2 gl) {
		if (program == null) {
			try {
				program = new Program(gl, "fire_shader.vp", "fire_shader.fp");
			}
			catch (GlslException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		
		if (noiseTexture == null) {
			try {
				noiseTexture = new Texture2D(gl, "data/textures/noise.jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		if (fireTexture == null) {
			try {
				fireTexture = new Texture2D(gl, "data/textures/fire.jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		program.use();
		
		program.getUniform("time").set1Float(time);
		program.getUniform("scroll_speeds").set3Float(scrollSpeeds[0], scrollSpeeds[1], scrollSpeeds[2]);
		
		TextureUnit.getActiveTextureUnit(gl).bindToUniform(program.getUniform("noise_texture"));
		noiseTexture.use();
		
		TextureUnit.getActiveTextureUnit(gl).bindToUniform(program.getUniform("fire_texture"));
		fireTexture.use();
		
		time+= 0.001f;
		
	}

	@Override
	public void unuse(GL2 gl) {
		noiseTexture.unuse();
		fireTexture.unuse();
		program.unuse();
	}

	@Override
	public void dispose(GL2 gl) {

	}
	
	protected static void setArray(float[] x, float x0, float x1, float x2)
	{
		x[0] = x0;
		x[1] = x1;
		x[2] = x2;
	}
	
	public void setSpeeds(float r, float g, float b) {
		setArray(scrollSpeeds,r,g,b);
	}

	protected List<Object> convertFloatArrayToList(float[] a)
	{
		List<Object> result = new ArrayList<Object>();
		for(int i=0;i<a.length;i++)
			result.add(a[i]);
		return result;
	}
	
	@Override
	public Object getYamlObjectRepresentation() {
		Map<Object, Object> result = new HashMap<Object,Object>();
		result.put("type", "ShaderFireMaterial");
		
		result.put("scroll_speeds", convertFloatArrayToList(scrollSpeeds));

		return result;
	}

}
