package cs4620.pa2.material;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;

import cs4620.framework.GlslException;
import cs4620.framework.Program;
import cs4620.framework.Texture2D;
import cs4620.framework.TextureUnit;

public class ShaderTextureMaterial extends GLPhongMaterial{
	
	private static Program program = null;
	
	public Texture2D texture = null;
	
	public ShaderTextureMaterial() 
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
				program = new Program(gl, "texture_shader.vp", "texture_shader.fp");
			}
			catch(GlslException e)
			{
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		
		if (texture == null) {
			try {
				texture = new Texture2D(gl, "data/textures/earth.jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}

		program.use();
		
		program.getUniform("ambient").set4Float(ambient[0], ambient[1], ambient[2], ambient[3]);
		program.getUniform("diffuse").set4Float(diffuse[0], diffuse[1], diffuse[2], diffuse[3]);
		program.getUniform("specular").set4Float(specular[0], specular[1], specular[2], specular[3]);
		program.getUniform("shininess").set1Float(shininess);
		
		TextureUnit.getActiveTextureUnit(gl).bindToUniform(program.getUniform("texture"));
		texture.use();
	}

	@Override
	public void unuse(GL2 gl) {
		// TODO Auto-generated method stub
		texture.unuse();
		program.unuse();
	}

	@Override
	public void dispose(GL2 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getYamlObjectRepresentation() {
		// TODO Auto-generated method stub
		Map<Object, Object> result = new HashMap<Object,Object>();
		result.put("type", "ShaderTextureMaterial");

		result.put("ambient", convertFloatArrayToList(ambient));
		result.put("diffuse", convertFloatArrayToList(diffuse));
		result.put("specular", convertFloatArrayToList(specular));
		result.put("shininess", shininess);
		
		return result;
	}

}
