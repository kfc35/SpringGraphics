package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
import ray.math.Vector3;

public class Box extends Surface {

	/* The corner of the box with the smallest x, y, and z components. */
	protected final Point3 minPt = new Point3();

	public void setMinPt(Point3 minPt) {
		this.minPt.set(minPt);
	}

	/* The corner of the box with the largest x, y, and z components. */
	protected final Point3 maxPt = new Point3();

	public void setMaxPt(Point3 maxPt) {
		this.maxPt.set(maxPt);
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
		// TODO(A): fill in this function.
		// Hint: This object can be transformed by a transformation matrix.
		// So the rayIn needs to be processed so that it is in the same
		// coordinate as the object.
		Ray ray = untransformRay(rayIn);
		
		double px = ray.origin.x;
		double py = ray.origin.y;
		double pz = ray.origin.z;
		
		double dx = ray.direction.x;
		double dy = ray.direction.y;
		double dz = ray.direction.z;
		
		double minx = minPt.x;
		double miny = minPt.y;
		double minz = minPt.z;
		
		double maxx = maxPt.x;
		double maxy = maxPt.y;
		double maxz = maxPt.z;
		
		double tminx = (minx-px)/dx;
		double tmaxx = (maxx-px)/dx;
		double txmin = Math.min(tminx, tmaxx);
		double txmax = Math.max(tminx, tmaxx);
		
		double tminy = (miny-py)/dy;
		double tmaxy = (maxy-py)/dy;
		double tymin = Math.min(tminy, tmaxy);
		double tymax = Math.max(tminy, tmaxy);
		
		double tminz = (minz-pz)/dz;
		double tmaxz = (maxz-pz)/dz;
		double tzmin = Math.min(tminz, tmaxz);
		double tzmax = Math.max(tminz, tmaxz);
		
		double tmin = Math.max(txmin, Math.max(tymin, tzmin));
		double tmax = Math.min(txmax, Math.min(tymax, tzmax));
		
		if (tmin > tmax) {
			// no intersection
			return false;
		}
		
		double t = 0;
		if (tmin > ray.start && tmin < ray.end) {
			t = tmin;
		} else if (tmax > ray.start && tmax < ray.end) {
			t = tmax;
		} else {
			// neither intersection was in the ray's half line
			return false;
		}
		
		// there is intersection, set IntersectionRecord
		if (outRecord != null) {
			outRecord.t = t;
			ray.evaluate(outRecord.location, t);
			outRecord.surface = this;
			
			Vector3 n = new Vector3();
			if (t == tminx) {
				n.set(-1, 0, 0);
			} else if (t == tmaxx) {
				n.set(1, 0, 0);
			} else if (t == tminy) {
				n.set(0, -1, 0);
			} else if (t == tmaxy) {
				n.set(0, 1, 0);
			} else if (t == tminz) {
				n.set(0, 0, -1);
			} else {
				n.set(0, 0, 1);
			}
			outRecord.normal.set(n);
			tMat.rightMultiply(outRecord.location);
			tMatTInv.rightMultiply(outRecord.normal);
			outRecord.normal.normalize();
		}
		
		return true;
	}

	public void computeBoundingBox() {
		// TODO(B): Compute the bounding box and store the result in
		// averagePosition, minBound, and maxBound.
		// Hint: The bounding box is not the same as just minPt and maxPt,
		// because
		// this object can be transformed by a transformation matrix.
		averagePosition = new Point3((minPt.x+maxPt.x)/2, (minPt.y+maxPt.y)/2, (minPt.z+maxPt.z)/2);
		tMat.rightMultiply(averagePosition);
		
		minBound = new Point3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY); 
	    maxBound = new Point3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
	    
	    Point3[] v = new Point3[8];
	    int count = 0;
	    for (int i = 0; i < 2; i++) {
	    	for (int j = 0; j < 2; j++) {
	    		for (int k = 0; k < 2; k++) {
	    			v[count] = new Point3();
	    			v[count].x = minPt.x*i+maxPt.x*(1.0-i);
	    			v[count].y = minPt.y*j+maxPt.y*(1.0-j);
	    			v[count].z = minPt.z*k+maxPt.z*(1.0-k);
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
		return "Box ";
	}
}