package cs4620.pa1.shape;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3f;

public class Sphere extends TriangleMesh 
{
	@Override
	public void buildMesh(float tolerance) {
		// TODO: (Problem 2) Fill in the code to create a sphere mesh.
		// TODO: Right now same vertex is stored multiple times. Need to reduce them?
		int numLongSteps = (int) (360/(180*(tolerance/5)));
		int numLatSteps = (int) (180/(180*(tolerance/5)));
		float step_t = 360f/numLongSteps;
		float step_p = 180f/numLatSteps;
		int numVertex = 4*numLongSteps*numLatSteps;
		
		vertices = new float[numVertex*3];
		normals = new float[numVertex*3];
		triangles = new int[numLongSteps*numLatSteps*2*3];
		
		int counter = 0;
		int triCounter = 0;
		for (int lat = 0; lat < numLatSteps; lat++) {
			for (int longi = 0; longi < numLongSteps; longi++) {
				double theta = longi*step_t/180*Math.PI;
				double phi = lat*step_p/180*Math.PI;
				
				double theta_step = (longi+1)*step_t/180*Math.PI;
				double phi_step = (lat+1)*step_p/180*Math.PI;
				
				Vector3f normal = new Vector3f();
				
				// left bottom vertex
				int vlt = counter++;
				float x = (float) (Math.cos(theta)*Math.sin(phi));
				float y = (float) (Math.sin(theta)*Math.sin(phi));
				float z = (float) (Math.cos(phi));
				setVertex(vlt, x, y, z);
				
				normal.set(x, y, z);
				normal.normalize();
				setNormal(vlt, normal.x, normal.y, normal.z);
				
				// right bottom vertex
				int vrt = counter++;
				x = (float) (Math.cos(theta_step)*Math.sin(phi));
				y = (float) (Math.sin(theta_step)*Math.sin(phi));
				z = (float) (Math.cos(phi));
				setVertex(vrt, x, y, z);
				
				normal.set(x, y, z);
				normal.normalize();
				setNormal(vrt, normal.x, normal.y, normal.z);
				
				// left top vertex
				int vlb = counter++;
				x = (float) (Math.cos(theta)*Math.sin(phi_step));
				y = (float) (Math.sin(theta)*Math.sin(phi_step));
				z = (float) (Math.cos(phi_step));
				setVertex(vlb, x, y, z);
				
				normal.set(x, y, z);
				normal.normalize();
				setNormal(vlb, normal.x, normal.y, normal.z);
				
				// right top vertex
				int vrb = counter++;
				x = (float) (Math.cos(theta_step)*Math.sin(phi_step));
				y = (float) (Math.sin(theta_step)*Math.sin(phi_step));
				z = (float) (Math.cos(phi_step));
				setVertex(vrb, x, y, z);
				
				normal.set(x, y, z);
				normal.normalize();
				setNormal(vrb, normal.x, normal.y, normal.z);
				
				// setting the 2 triangles
				setTriangle(triCounter++, vlb, vrb, vlt);
				setTriangle(triCounter++, vrb, vrt, vlt);
			}
		}
	}

	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Sphere");
		return result;
	}
}
