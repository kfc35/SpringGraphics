package cs4620.pa1.shape;

import java.util.HashMap;
import java.util.Map;

public class Cylinder extends TriangleMesh
{
	@Override
	public void buildMesh(float tolerance) {
		// TODO: (Problem 2) Fill in the code to create a cylinder mesh.

		int numSteps = (int) (360/(180*tolerance/5));
		float step = 360f/numSteps;
		
		// 2*vertices for caps + vertices for sides
		int numVertex = 2*(1+numSteps) + (2*numSteps);
		vertices = new float[numVertex*3];
		normals = new float[numVertex*3];
		triangles = new int[(numSteps*2+2*numSteps)*3];
		
		int topCenter = 0;
		int bottomCenter = numSteps+1;
		// construct the center points
		setVertex(topCenter, 0f, 1f, 0f);
		setNormal(topCenter, 0f, 1f, 0f);
		setVertex(bottomCenter, 0f, -1f, 0f);
		setNormal(bottomCenter, 0f, -1f, 0f);
		
		// draw bottom and top plate
		int tCounter = topCenter+1;
		int bCounter = bottomCenter+1;

		// top=1+numSteps, bottom=1+numSteps
		int tSideStart = 2*(1+numSteps);
		// top+bottom = 2*(1+numSteps), topSide = numSteps
		int bSideStart = 2*(1+numSteps)+numSteps;
		int tSideCounter = tSideStart;
		int bSideCounter = bSideStart;
		
		int tTriCounter = 0;
		int bTriCounter = numSteps;
		int sTriCounter = numSteps*2;
		for (int i = 1; i <= numSteps; i++) {
			double theta = i*step/180*Math.PI;
			float x, z;

			if (i == 1) {
				// draw the first point
				// circle is on x-z plane
				x = (float) Math.cos(0);
				z = (float) Math.sin(0);
				// top
				setVertex(tCounter, x, 1f, z);
				setNormal(tCounter, 0f, 1f, 0f);
				// bottom
				setVertex(bCounter, x, -1f, z);
				setNormal(bCounter, 0f, -1f, 0f);
				// side
				setVertex(tSideCounter, x, 1f, z);
				setNormal(tSideCounter, x, 0f, z);
				setVertex(bSideCounter, x, -1f, z);
				setNormal(bSideCounter, x, 0f, z);
				
				tCounter++;
				bCounter++;
				tSideCounter++;
				bSideCounter++;
			}
			
			if (i == numSteps) {
				// last triangle doesn't need to construct a new vertex
				// top triangle
				setTriangle(tTriCounter, topCenter, tCounter-1, topCenter+1);
				// bottom triangle
				setTriangle(bTriCounter, bottomCenter, bCounter-1, bottomCenter+1);
				// side triangles
				setTriangle(sTriCounter++, bSideStart, bSideCounter-1, tSideCounter-1);
				setTriangle(sTriCounter, tSideCounter-1, tSideStart, bSideStart);
			} else {
				x = (float) Math.cos(theta);
				z = (float) Math.sin(theta);
				// top
				setVertex(tCounter, x, 1f, z);
				setNormal(tCounter, 0f, 1f, 0f);
				// bottom
				setVertex(bCounter, x, -1f, z);
				setNormal(bCounter, 0f, -1f, 0f);
				// side
				setVertex(tSideCounter, x, 1f, z);
				setNormal(tSideCounter, x, 0f, z);
				setVertex(bSideCounter, x, -1f, z);
				setNormal(bSideCounter, x, 0f, z);
				
				// set top triangle
				setTriangle(tTriCounter++, topCenter, tCounter-1, tCounter);
				// bottom triangle
				setTriangle(bTriCounter++, bottomCenter, bCounter-1, bCounter);
				// side triangles
				setTriangle(sTriCounter++, bSideCounter, bSideCounter-1, tSideCounter-1);
				setTriangle(sTriCounter++, tSideCounter-1, tSideCounter, bSideCounter);

				tCounter++;
				bCounter++;
				tSideCounter++;
				bSideCounter++;
			}
			
		}
	}


	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Cylinder");
		return result;
	}
}
