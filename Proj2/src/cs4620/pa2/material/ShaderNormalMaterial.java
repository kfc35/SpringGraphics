package cs4620.pa2.material;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;

import cs4620.framework.GlslException;
import cs4620.framework.Program;

public class ShaderNormalMaterial extends Material {
	private static Program program = null;

	public ShaderNormalMaterial()
	{
		super();
	}
	
	@Override
	public void use(GL2 gl) {
		if(program == null)
		{
			try
			{
				program = new Program(gl, "normal_shader.vp", "normal_shader.fp");
			}
			catch(GlslException e)
			{
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		
		program.use();
	}

	@Override
	public void unuse(GL2 gl) {
		program.unuse();
	}

	@Override
	public void dispose(GL2 gl) {
		// NOP

	}

	@Override
	public Object getYamlObjectRepresentation() {
		Map<Object, Object> result = new HashMap<Object,Object>();
		result.put("type", "ShaderNormalMaterial");

		return result;
	}

}