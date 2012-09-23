package cs4620.pa1.shape;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3f;

public class Sphere extends TriangleMesh 
{
	@Override
	public void buildMesh(float tolerance) {
		// TODO: (Problem 2) Fill in the code to create a sphere mesh.
		int numLongSteps = (int) (360/(180*(tolerance/5)));
		int numLatSteps = (int) (180/(180*(tolerance/5)));
		float step_t = 360f/numLongSteps;
		float step_p = 180f/numLatSteps;
		int numVertex = 4*numLongSteps*numLatSteps;
		
		float[] vertices = new float[numVertex*3];
		float[] normals = new float[numVertex*3];
		int[] triangles = new int[numLongSteps*numLatSteps*2*3];
		
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
				int vlt = counter;
				vertices[3*vlt] = (float) (Math.cos(theta)*Math.sin(phi));
				vertices[3*vlt+1] = (float) (Math.sin(theta)*Math.sin(phi));
				vertices[3*vlt+2] = (float) (Math.cos(phi));
				
				normal.set(vertices[3*vlt], vertices[3*vlt+1], vertices[3*vlt+2]);
				normal.normalize();
				normals[3*vlt] = normal.x;
				normals[3*vlt+1] = normal.y;
				normals[3*vlt+2] = normal.z;
				
				counter++;
				
				// right bottom vertex
				int vrt = counter;
				vertices[3*vrt] = (float) (Math.cos(theta_step)*Math.sin(phi));
				vertices[3*vrt+1] = (float) (Math.sin(theta_step)*Math.sin(phi));
				vertices[3*vrt+2] = (float) (Math.cos(phi));
				
				normal.set(vertices[3*vrt], vertices[3*vrt+1], vertices[3*vrt+2]);
				normal.normalize();
				normals[3*vrt] = normal.x;
				normals[3*vrt+1] = normal.y;
				normals[3*vrt+2] = normal.z;
				
				counter++;
				
				// left top vertex
				int vlb = counter;
				vertices[3*vlb] = (float) (Math.cos(theta)*Math.sin(phi_step));
				vertices[3*vlb+1] = (float) (Math.sin(theta)*Math.sin(phi_step));
				vertices[3*vlb+2] = (float) (Math.cos(phi_step));
				
				normal.set(vertices[3*vlb], vertices[3*vlb+1], vertices[3*vlb+2]);
				normal.normalize();
				normals[3*vlb] = normal.x;
				normals[3*vlb+1] = normal.y;
				normals[3*vlb+2] = normal.z;
				
				counter++;
				
				// right top vertex
				int vrb = counter;
				vertices[3*vrb] = (float) (Math.cos(theta_step)*Math.sin(phi_step));
				vertices[3*vrb+1] = (float) (Math.sin(theta_step)*Math.sin(phi_step));
				vertices[3*vrb+2] = (float) (Math.cos(phi_step));
				
				normal.set(vertices[3*vrb], vertices[3*vrb+1], vertices[3*vrb+2]);
				normal.normalize();
				normals[3*vrb] = normal.x;
				normals[3*vrb+1] = normal.y;
				normals[3*vrb+2] = normal.z;
				
				counter++;
				
				triangles[triCounter++] = vlb;
				triangles[triCounter++] = vrb;
				triangles[triCounter++] = vlt;
				
				triangles[triCounter++] = vrb;
				triangles[triCounter++] = vrt;
				triangles[triCounter++] = vlt;
			}
		}
		
		setMeshData(vertices, normals, triangles);
	}

	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Sphere");
		return result;
	}
}
