package cs4620.pa2.shape;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3f;

public class Sphere extends TriangleMesh {

	@Override
	public void buildMesh(float tolerance)
	{
		// TODO PA1: (Problem 2) Fill in the code to create a sphere mesh.
		int numLongSteps = (int) (360/(180*(tolerance/5)));
		int numLatSteps = (int) (180/(180*(tolerance/5)));
		double step_t = 360./numLongSteps;
		double step_p = 180./numLatSteps;
		int numVertex = (1+numLatSteps)*(numLongSteps+1);
		
		vertices = new float[numVertex*3];
		normals = new float[numVertex*3];
		triangles = new int[numLongSteps*numLatSteps*2*3];
		texCoords = new float[numVertex*2];
		
		int counter = 0;
		int triCounter = 0;
		for (int lat = 0; lat <= numLatSteps; lat++) {
			double phi = lat*step_p/180.*Math.PI;
			float texCoordY = lat*1.0f/numLatSteps;
			for (int longi = 0; longi <= numLongSteps; longi++) {
				double theta = longi*step_t/180.*Math.PI;
				float texCoordX = longi*1.0f/numLongSteps;
				float x, y, z;
				
				Vector3f normal = new Vector3f();
				
//				if (longi == 1) {
//					// first vertex of each row
//					x = (float) (Math.cos(0)*Math.sin(phi));
//					y = (float) (Math.sin(0)*Math.sin(phi));
//					z = (float) (Math.cos(phi));
//					setVertex(counter, x, y, z);
//					normal.set(x, y, z);
//					normal.normalize();
//					setNormal(counter, normal.x, normal.y, normal.z);
//					// TODO: set texCoords
//					counter++;
//				}
				
//				if (longi == numLongSteps) {
//					// last iteration of the current latitude, doesn't need vertex
//					if (lat != 0) {
//						int tlv = counter - 1 - numLongSteps;
//						int trv = counter - 1 - numLongSteps - (numLongSteps - 1);
//						int blv = counter - 1;
//						int brv = counter - 1 - (numLongSteps - 1);
//						setTriangle(triCounter++, tlv, trv, blv);
//						setTriangle(triCounter++, blv, brv, trv);
//					}
//				} else {
					x = (float) (Math.cos(theta)*Math.sin(phi));
					y = (float) (Math.sin(theta)*Math.sin(phi));
					z = (float) (Math.cos(phi));
					setVertex(counter, x, y, z);
					normal.set(x, y, z);
					normal.normalize();
					setNormal(counter, normal.x, normal.y, normal.z);
					setTexCoord(counter, texCoordX, texCoordY);
					if (lat != 0 && longi != 0) {
						int tlv = counter - numLongSteps - 2;
						int trv = counter - numLongSteps - 1;
						int blv = counter - 1;
						int brv = counter;
						setTriangle(triCounter++, tlv, trv, blv);
						setTriangle(triCounter++, blv, brv, trv);
					}
					counter++;
//				}
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
