package cs4620.pa2.shape;

import java.util.HashMap;
import java.util.Map;

public class Cylinder extends TriangleMesh
{
	@Override
	public void buildMesh(float tolerance)
	{
		// TODO PA1: (Problem 2) Fill in the code to create a cylinder mesh.
	}

	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Cylinder");
		return result;
	}
}