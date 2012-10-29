package cs4620.pa2.shape;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Flower extends CustomTriangleMesh {
	
	public Flower() throws Exception
	{
		super(new File("data/meshes/flower.msh"));
	}
	
	/*
	public static float height()
	{
		return 3.0f;
	}
	*/

	@Override
	public void buildMesh(float tolerance) {
		/*
		int numLevels = 20; // levels of vertices
		int levelSize = 8; // num verts in level
		float radius = 0.3f;
		float totalHeight = Flower.height();
		
		vertices = new float[3 * levelSize * numLevels];
		normals = new float[3 * levelSize * numLevels];
		triangles = new int[3 * 2 * levelSize * (numLevels - 1)];
		
		// set verts and normals
		for(int l = 0; l < numLevels; l++)
		{
			float height = totalHeight * l / ((float) numLevels - 1.0f);
			for(int a = 0; a < levelSize; a++)
			{
				float angle = a * 2 * ((float) Math.PI) / levelSize;
				float cosang = (float) Math.cos(angle);
				float sinang = (float) Math.sin(angle);
				setVertex(levelSize * l + a, radius * cosang, height, radius * sinang);
				setNormal(levelSize * l + a, cosang, 0, sinang);
			}
		}
		
		// set triangles
		for(int l = 0; l < numLevels - 1; l++)
		{
			int l0 = l * levelSize;
			int l1 = (l+1) * levelSize;
			int t = 2 * levelSize * l;
			for(int i = 0; i < levelSize; i++)
			{
				int j = (i + 1) % levelSize;
				setTriangle(t + 2 * i + 0, l0 + i, l0 + j, l1 + j);
				setTriangle(t + 2 * i + 1, l0 + i, l1 + j, l1 + i);
			}
		}
		*/
	}

	@Override
	public Object getYamlObjectRepresentation() {
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Flower");
		return result;
	}

}
