package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
import ray.math.Vector3;

public class Cylinder extends Surface {

	/** The center of the bottom of the cylinder x , y ,z components. */
	protected final Point3 center = new Point3();

	public void setCenter(Point3 center) {
		this.center.set(center);
	}

	/** The radius of the cylinder. */
	protected double radius = 1.0;

	public void setRadius(double radius) {
		this.radius = radius;
	}

	/** The height of the cylinder. */
	protected double height = 1.0;

	public void setHeight(double height) {
		this.height = height;
	}

	public Cylinder() {
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
		
		Vector3 twoDDir = new Vector3(ray.direction.x, ray.direction.y, 0);
		
		// calculating circle intersections (x and y)
		
		double qx = px - center.x;
		double qy = py - center.y;
		double dd = twoDDir.lengthsquared();
		double qd = qx * dx + qy * dy;
		double qq = qx * qx + qy * qy;
		
		double discriminantSqr = (qd * qd - dd * (qq - radius * radius));
		
		if (discriminantSqr < 0) {
			return false;
		}
		
		double discriminant = Math.sqrt(discriminantSqr);
		double txymin = (-qd - discriminant) / dd;
		double txymax = (-qd + discriminant) / dd;
		
		// calculating cap intersection (z)
		double minz = center.z - 0.5*height;
		double maxz = center.z + 0.5*height;
		
		double tminz = (minz-pz)/dz;
		double tmaxz = (maxz-pz)/dz;
		double tzmin = Math.min(tminz, tmaxz);
		double tzmax = Math.max(tminz, tmaxz);
		
		// similar to box
		double tmin = Math.max(txymin, tzmin);
		double tmax = Math.min(txymax, tzmax);
		
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
			if (t == tminz) {
				n.set(0, 0, -1);
			} else if (t == tmaxz) {
				n.set(0, 0, 1);
			} else {
				Point3 l = new Point3(outRecord.location.x, outRecord.location.y, 0);
				Point3 c = new Point3(center.x, center.y, 0);
				n.sub(l, c);
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
		// Hint: The bounding box may be transformed by a transformation matrix.
		averagePosition = new Point3(center);
		tMat.rightMultiply(averagePosition);
		
		minBound = new Point3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		maxBound = new Point3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
		
		Point3[] v = new Point3[8];
		int count = 0;
		for (int i = -1; i < 2; i += 2) {
			for (int j = -1; j < 2; j += 2) {
				for (int k = -1; k < 2; k += 2) {
					v[count] = new Point3(center);
					v[count].x += radius * i;
					v[count].y += radius * j;
					v[count].z += (height/2) * k;
					count++;
				}
			}
		}
		
		for (int i = 0; i < 8; i++) {
			tMat.rightMultiply(v[i]);
			for (int j = 0; j < 3; j++) {
				if(v[i].getE(j) < minBound.getE(j)) {
					minBound.setE(j, v[i].getE(j));
				}
				if (v[i].getE(j) > maxBound.getE(j)) {
					maxBound.setE(j, v[i].getE(j));
				}
			}
		}
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "Cylinder " + center + " " + radius + " " + height + " "
				+ material + " end";
	}
}
