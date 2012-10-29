package cs4620.pa2.shape;

import javax.media.opengl.GL2;

public abstract class TriangleMesh extends Mesh {
	protected float[] vertices = null;
	protected float[] normals = null;
	protected float[] texCoords = null;
	protected int[] triangles = null;

	public TriangleMesh()
	{
		super();
	}

	protected void setVertex(int i, float x, float y, float z)
	{
		vertices[3*i]   = x;
		vertices[3*i+1] = y;
		vertices[3*i+2] = z;
	}

	protected void setNormal(int i, float x, float y, float z)
	{
		normals[3*i]   = x;
		normals[3*i+1] = y;
		normals[3*i+2] = z;
	}
	
	protected void setTexCoord(int i, float x, float y) 
	{
		texCoords[2*i] = x;
		texCoords[2*i+1] = y;
	}

	protected void setTriangle(int i, int i0, int i1, int i2)
	{
		triangles[3*i]   = i0;
		triangles[3*i+1] = i1;
		triangles[3*i+2] = i2;
	}

	public final void render(GL2 gl)
	{
		// TODO PA1: (Problem 1) Fill in the code to render the mesh.
		
		gl.glBegin(GL2.GL_TRIANGLES);

		// drawing normals and vertices of triangles
		for (int i=0; i < triangles.length; i=i+3) {
			int v1 = triangles[i];
			int v2 = triangles[i+1];
			int v3 = triangles[i+2];
			gl.glNormal3f(normals[3*v1], normals[3*v1+1], normals[3*v1+2]);
			if (texCoords != null) {
				gl.glTexCoord2f(texCoords[2*v1], texCoords[2*v1+1]);
			}
			gl.glVertex3f(vertices[3*v1], vertices[3*v1+1], vertices[3*v1+2]);
			gl.glNormal3f(normals[3*v2], normals[3*v2+1], normals[3*v2+2]);
			if (texCoords != null) {
				gl.glTexCoord2f(texCoords[2*v2], texCoords[2*v2+1]);
			}
			gl.glVertex3f(vertices[3*v2], vertices[3*v2+1], vertices[3*v2+2]);
			gl.glNormal3f(normals[3*v3], normals[3*v3+1], normals[3*v3+2]);
			if (texCoords != null) {
				gl.glTexCoord2f(texCoords[2*v3], texCoords[2*v3+1]);
			}
			gl.glVertex3f(vertices[3*v3], vertices[3*v3+1], vertices[3*v3+2]);
		}

		gl.glEnd();
	}

	public final void setMeshData(float[] vertices, float[] normals, int[] triangles)
	{
		if (vertices.length % 3 != 0)
			throw new Error("Vertex array's length is not a multiple of 3.");
		if (normals.length % 3 != 0)
			throw new Error("Normal array's length is not a multiple of 3");
		if (vertices.length != normals.length)
			throw new Error("Vertex and normal array are not equal in size.");
	    if (triangles.length % 3 != 0)
	        throw new Error("Triangle array's length is not a multiple of 3.");

	    this.vertices = vertices;
	    this.normals = normals;
	    this.triangles = triangles;
	}
	
	public final void setMeshData(float[] vertices, float[] normals, float[] texCoords, int[] triangles)
	{
		if (vertices.length % 3 != 0)
			throw new Error("Vertex array's length is not a multiple of 3.");
		if (normals.length % 3 != 0)
			throw new Error("Normal array's length is not a multiple of 3");
		if (texCoords.length % 2 != 0)
			throw new Error("TexCoord array's length is not a multiple of 2");
		if (vertices.length != normals.length)
			throw new Error("Vertex and normal array are not equal in size.");
		if (vertices.length/3 != texCoords.length/2)
			throw new Error("Number of Verticies and number of Texture Coordinates are different");
	    if (triangles.length % 3 != 0)
	        throw new Error("Triangle array's length is not a multiple of 3.");

	    this.vertices = vertices;
	    this.normals = normals;
	    this.texCoords = texCoords;
	    this.triangles = triangles;
	}
}
