package cs4620.pa2.scene;

import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import cs4620.pa2.material.GLPhongMaterial;
import cs4620.pa2.material.Material;
import cs4620.pa2.material.ShaderGreenMaterial;
import cs4620.pa2.material.ShaderNormalMaterial;
import cs4620.pa2.material.ShaderPhongMaterial;
import cs4620.pa2.material.ShaderToonMaterial;
import cs4620.pa2.shape.Mesh;
import cs4620.pa2.shape.Sphere;
import cs4620.pa2.util.Util;

public class MeshNode extends TransformationNode
{
	private static final long	serialVersionUID	= 1L;

	private Mesh mesh;
	private Material material;

	/**
	 * Required for I/O
	 */
	public MeshNode()
	{
		this("", null, null);
	}

	public MeshNode(String name)
	{
		this(name, new Sphere(), new GLPhongMaterial());
	}

	public MeshNode(String name, Mesh mesh)
	{
		this(name, mesh, new GLPhongMaterial());
	}

	public MeshNode(String name, Mesh mesh, Material material)
	{
		super(name);
		this.mesh = mesh;
		this.material = material;
	}

	public Mesh getMesh()
	{
		return mesh;
	}

	public void setMesh(Mesh mesh)
	{
		this.mesh = mesh;
	}

	public Material getMaterial()
	{
		return material;
	}
	
	public void draw(GL2 gl)
	{
		// The "Toon" material actually requires two rendering passes, so
		// drawing with it must be implemented separately
		if(getMaterial() instanceof ShaderToonMaterial)
		{
			ShaderToonMaterial mat = (ShaderToonMaterial) getMaterial();
			
			// render quantized mesh
			mat.use(gl); // enable quantize shader
			getMesh().render(gl);
			mat.unuse(gl); // disable quantize shader
			
			// TODO: (Problem 1.3) Fill in the code to add an outline to
			// your quantize-shaded object using the displace shader.
			// Use
			//   mat.useDisplace(gl);
			// and
			//   mat.unuseDisplace(gl);
			// to enable and disable the displace shader.
			
			mat.useDisplace(gl);
			/*Orient the cull face to be the front face, 
			 *which has vertices labeled counter clockwise*/
			gl.glFrontFace(GL.GL_CCW);
			gl.glCullFace(GL.GL_FRONT);
			gl.glEnable(GL.GL_CULL_FACE);
			getMesh().render(gl);
			gl.glDisable(GL.GL_CULL_FACE);
			mat.unuseDisplace(gl);
		}
		else
		{
			getMaterial().use(gl);
			getMesh().render(gl);
			getMaterial().unuse(gl);
		}
	}
	
	public void drawForPicking(GL2 gl)
	{
		gl.glLoadName(getMesh().getId());
		getMesh().render(gl);
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<String, Object> result = (Map<String, Object>)super.getYamlObjectRepresentation();
		result.put("type", "MeshNode");
		result.put("mesh", mesh.getYamlObjectRepresentation());
		result.put("material", material.getYamlObjectRepresentation());
		return result;
	}

	public void extractMeshFromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;

		mesh = Mesh.fromYamlObject(yamlMap.get("mesh"));
	}

	public void extractMaterialFromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;

		if (!(yamlMap.get("material") instanceof Map))
			throw new RuntimeException("material field not a Map");
		Map<?, ?> materialMap = (Map<?, ?>)yamlMap.get("material");
		
		String matType = (String) materialMap.get("type");

		boolean hasColors = false;
		
		if(matType.equals("GLPhongMaterial"))
		{
			material = new GLPhongMaterial();
			hasColors = true;
		}
		else if(matType.equals("ShaderPhongMaterial"))
		{
			material = new ShaderPhongMaterial();
			hasColors = true;
		}
		else if(matType.equals("ShaderToonMaterial"))
		{
			material = new ShaderToonMaterial();
			hasColors = true;
		}
		else if(matType.equals("ShaderGreenMaterial"))
		{
			material = new ShaderGreenMaterial();
		}
		else if(matType.equals("ShaderNormalMaterial"))
		{
			material = new ShaderNormalMaterial();
		}
		
		if(hasColors)
		{
			GLPhongMaterial glMaterial = (GLPhongMaterial) material;
			Util.assign4ElementArrayFromYamlObject(glMaterial.ambient, materialMap.get("ambient"));
			Util.assign4ElementArrayFromYamlObject(glMaterial.diffuse, materialMap.get("diffuse"));
			Util.assign4ElementArrayFromYamlObject(glMaterial.specular, materialMap.get("specular"));
			glMaterial.shininess = Float.valueOf(materialMap.get("shininess").toString());
		}

	}

	public static SceneNode fromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;

		MeshNode result = new MeshNode();
		result.setName((String)yamlMap.get("name"));
		result.extractTransformationFromYamlObject(yamlObject);
		result.addChildrenFromYamlObject(yamlObject);
		result.extractMeshFromYamlObject(yamlObject);
		result.extractMaterialFromYamlObject(yamlObject);

		return result;
	}
}
