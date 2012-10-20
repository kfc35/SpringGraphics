package cs4620.pa2.material;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;

import cs4620.framework.GlslException;
import cs4620.framework.Program;

public class ShaderPhongMaterial extends GLPhongMaterial {
	
	private static Program program = null;

	public ShaderPhongMaterial()
	{
		super();
	}

	@Override
	public void use(GL2 gl)
	{
		if(program == null)
		{
			try
			{
				program = new Program(gl, "blinn_phong.vp", "blinn_phong.fp");
			}
			catch(GlslException e)
			{
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		
		program.use();
		
		// Set uniforms. The if statements are needed because GLSL ignores uniforms declared
		// in the shader if they're not being used.
		if(program.getUniform("ambient") != null)
			program.getUniform("ambient").set4Float(ambient[0], ambient[1], ambient[2], ambient[3]);
		else
			System.out.println("Phong shader is ignoring uniform \"ambient\"");
		
		if(program.getUniform("diffuse") != null)
			program.getUniform("diffuse").set4Float(diffuse[0], diffuse[1], diffuse[2], diffuse[3]);
		else
			System.out.println("Phong shader is ignoring uniform \"diffuse\"");
		
		if(program.getUniform("specular") != null)
			program.getUniform("specular").set4Float(specular[0], specular[1], specular[2], specular[3]);
		else
			System.out.println("Phong shader is ignoring uniform \"specular\"");
		
		if(program.getUniform("shininess") != null)
			program.getUniform("shininess").set1Float(shininess);
		else
			System.out.println("Phong shader is ignoring uniform \"shininess\"");
	}

	@Override
	public void unuse(GL2 gl)
	{
		// NOP
		program.unuse();
	}

	@Override
	public void dispose(GL2 gl)
	{
		// NOP
	}

	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<Object, Object> result = new HashMap<Object,Object>();
		result.put("type", "ShaderPhongMaterial");

		result.put("ambient", convertFloatArrayToList(ambient));
		result.put("diffuse", convertFloatArrayToList(diffuse));
		result.put("specular", convertFloatArrayToList(specular));
		result.put("shininess", shininess);

		return result;
	}

}
