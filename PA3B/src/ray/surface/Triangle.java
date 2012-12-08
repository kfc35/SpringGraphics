package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.material.Material;
import ray.math.Point3;
import ray.math.Vector3;

/**
 * Represents a single triangle, part of a triangle mesh
 * 
 * @author ags
 */
public class Triangle extends Surface {
	/** The normal vector of this triangle mesh */
	Vector3 norm;

	/** The mesh that contains this triangle mesh */
	Mesh owner;

	/** 3 indices to the vertices of this triangle. */
	int[] index;

	double a, b, c, d, e, f;

	public Triangle(Mesh owner, int index0, int index1, int index2,
			Material material) {
		this.owner = owner;
		index = new int[3];
		index[0] = index0;
		index[1] = index1;
		index[2] = index2;

		Point3 v0 = owner.getVertex(index0);
		Point3 v1 = owner.getVertex(index1);
		Point3 v2 = owner.getVertex(index2);

		if (!owner.existsNormals()) {
			Vector3 e0 = new Vector3(), e1 = new Vector3();
			e0.sub(v1, v0);
			e1.sub(v2, v0);
			norm = new Vector3();
			norm.cross(e0, e1);
		}
		a = v0.x - v1.x;
		b = v0.y - v1.y;
		c = v0.z - v1.z;

		d = v0.x - v2.x;
		e = v0.y - v2.y;
		f = v0.z - v2.z;

		this.setMaterial(material);
	}

	/**
	 * Tests this surface for intersection with ray. If an intersection is found
	 * record is filled out with the information about the intersection and the
	 * method returns true. It returns false otherwise and the information in
	 * outRecord is not modified.
	 * 
	 * @param outRecord
	 *            the output IntersectionRecord
	 * @param ray
	 *            the ray to intersect
	 * @return true if the surface intersects the ray
	 */
	public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {
		// TODO(B): fill in this function.
		// Hint: This object can be transformed by a transformation matrix.
		// So the rayIn needs to be processed so that it is in the same
		// coordinate as the object.
		Ray ray = untransformRay(rayIn);
		
		double ax = owner.getVertex(index[0]).x;
		double ay = owner.getVertex(index[0]).y;
		double az = owner.getVertex(index[0]).z;
		double px = ray.origin.x;
		double py = ray.origin.y;
		double pz = ray.origin.z;
		double dx = ray.direction.x;
		double dy = ray.direction.y;
		double dz = ray.direction.z;
		
		// calculating t, gamma, and beta based on linear system presented in 
		// Shirley and Marschner
		double ei_hf = e*dz - dy*f;
		double gf_di = dx*f - d*dz;
		double dh_eg = d*dy - e*dx;
		
		double ak_jb = a*(ay-py) - (ax-px)*b;
		double jc_al = (ax-px)*c - a*(az-pz);
		double bl_kc = b*(az-pz) - (ay-py)*c;
		
		double m = a*ei_hf + b*gf_di + c*dh_eg;
		
		double t = -(f*ak_jb + e*jc_al + d*bl_kc)/m;
		if (t < ray.start || t > ray.end) {
			return false;
		}
		
		double gamma = (dz*ak_jb + dy*jc_al + dx*bl_kc)/m;
		if (gamma < 0 || gamma > 1) {
			return false;
		}
		
		double beta = ((ax-px)*ei_hf + (ay-py)*gf_di + (az-pz)*dh_eg)/m;
		if (beta < 0 || beta > 1-gamma) {
			return false;
		}
		
		if (outRecord != null) {
			outRecord.t = t;
			ray.evaluate(outRecord.location, t);
			outRecord.surface = this;
			
			if (norm == null) {
				// interpolate normal based on barycentric coords
				double alpha = 1.0 - gamma - beta;
				Vector3 an = owner.getNormal(index[0]);
				an.scale(alpha);
				Vector3 bn = owner.getNormal(index[1]);
				bn.scale(beta);
				Vector3 cn = owner.getNormal(index[2]);
				cn.scale(gamma);
				Vector3 n = new Vector3(an.x+bn.x+cn.x, an.y+bn.y+cn.y, an.z+bn.z+cn.z);
				outRecord.normal.set(n);
			} else {
				outRecord.normal.set(norm);
			}
			tMat.rightMultiply(outRecord.location);
			tMatTInv.rightMultiply(outRecord.normal);
			outRecord.normal.normalize();
		}
		return true;
	}

	public void computeBoundingBox() {
		// TODO(B): Compute the bounding box and store the result in
		// averagePosition, minBound, and maxBound.
		Point3 a = owner.getVertex(index[0]);
		Point3 b = owner.getVertex(index[1]);
		Point3 c = owner.getVertex(index[2]);
		
		averagePosition = new Point3((a.x+b.x+c.x)/3.0, (a.y+b.y+c.y)/3.0, (a.z+b.z+c.z)/3.0);
		tMat.rightMultiply(averagePosition);
		
		minBound = new Point3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY); 
	    maxBound = new Point3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
	    
	    double min_x = Math.min(a.x, Math.min(b.x, c.x));
		double min_y = Math.min(a.y, Math.min(b.y, c.y));
		double min_z = Math.min(a.z, Math.min(b.z, c.z));
		double max_x = Math.max(a.x, Math.max(b.x, c.x));
		double max_y = Math.max(a.y, Math.max(b.y, c.y));
		double max_z = Math.max(a.z, Math.max(b.z, c.z));
		
		Point3[] v = new Point3[8];
		int count = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					v[count] = new Point3();
					v[count].x = min_x*i+max_x*(1.0-i);
					v[count].y = min_y*j+max_y*(1.0-j);
					v[count].z = min_z*k+max_z*(1.0-k);
					count++;
				}
			}
		}
		
		for (int i = 0; i < 8; i++) {
			tMat.rightMultiply(v[i]);
			for (int j = 0; j < 3; j++) {
				if (v[i].getE(j) < minBound.getE(j))
					minBound.setE(j, v[i].getE(j));
				if (v[i].getE(j) > maxBound.getE(j))
					maxBound.setE(j, v[i].getE(j));
			}
		}
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "Triangle ";
	}
}