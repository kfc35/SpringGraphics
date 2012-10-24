package cs4620.pa2.material;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;

import cs4620.framework.GlslException;
import cs4620.framework.Program;

public class ShaderToonMaterial extends GLPhongMaterial {
	private static Program displaceProgram = null;
	private static Program quantizeProgram = null;
	
	private float displaceScale = 0.05f;

	public ShaderToonMaterial()
	{
		super();
		setAmbient(0.0f, 0.0f, 0.0f);
		setSpecular(0.0f, 0.0f, 0.0f);
		setShininess(0.0f);
	}
	
	@Override
	public void setAmbient(float r, float g, float b)
	{
		setArray(ambient, r, g, b);
	}

	@Override
	public void setSpecular(float r, float g, float b)
	{
		setArray(specular, r, g, b);
	}

	@Override
	public void setShininess(float shininess)
	{
		this.shininess = shininess;
	}
	
	@Override
	public void use(GL2 gl) {
		if(quantizeProgram == null)
		{
			try
			{
				quantizeProgram = new Program(gl, "toon_quantize.vp", "toon_quantize.fp");
			}
			catch(GlslException e)
			{
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		
		quantizeProgram.use();
		
		// Set uniforms. The if statements are needed because GLSL ignores uniforms declared
		// in the shader if they're not being used.
		
		if(quantizeProgram.getUniform("diffuse") != null)
			quantizeProgram.getUniform("diffuse").set4Float(diffuse[0], diffuse[1], diffuse[2], diffuse[3]);
		else
			System.out.println("Toon quantize shader is ignoring uniform \"diffuse\"");
	}

	@Override
	public void unuse(GL2 gl) {
		quantizeProgram.unuse();

	}
	
	public void useDisplace(GL2 gl) {
		if(displaceProgram == null)
		{
			try
			{
				displaceProgram = new Program(gl, "toon_displace.vp", "toon_displace.fp");
			}
			catch(GlslException e)
			{
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		
		displaceProgram.use();
		
		if(displaceProgram.getUniform("displaceScale") != null)
			displaceProgram.getUniform("displaceScale").set1Float(displaceScale);
		else
			System.out.println("Toon displace shader is ignoring uniform \"displaceScale\"");
	}
	
	public void unuseDisplace(GL2 gl) {
		displaceProgram.unuse();
	}

	@Override
	public void dispose(GL2 gl) {
		// NOP

	}

	@Override
	public Object getYamlObjectRepresentation() {
		Map<Object, Object> result = new HashMap<Object,Object>();
		result.put("type", "ShaderToonMaterial");

		result.put("ambient", convertFloatArrayToList(ambient));
		result.put("diffuse", convertFloatArrayToList(diffuse));
		result.put("specular", convertFloatArrayToList(specular));
		result.put("shininess", shininess);

		return result;
	}

}
