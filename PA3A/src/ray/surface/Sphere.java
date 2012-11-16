
package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
import ray.math.Vector3;

/**
 * Represents a sphere as a center and a radius.
 *
 * @author ags
 */
public class Sphere extends Surface {
  
  /** The center of the sphere. */
  protected final Point3 center = new Point3();
  public void setCenter(Point3 center) { this.center.set(center); }
  
  /** The radius of the sphere. */
  protected double radius = 1.0;
  public void setRadius(double radius) { this.radius = radius; }
  
  public Sphere() { }
  
  /**
   * Tests this surface for intersection with ray. If an intersection is found
   * record is filled out with the information about the intersection and the
   * method returns true. It returns false otherwise and the information in
   * outRecord is not modified.
   *
   * @param outRecord the output IntersectionRecord
   * @param ray the ray to intersect
   * @return true if the surface intersects the ray
   */
  public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {
    // This object can be transformed by a transformation matrix.
    // So the rayIn needs to be processed so that it is in the same coordinate as the object.
    
    Ray ray = untransformRay(rayIn);
    
    // Rename the common vectors so I don't have to type so much
    Vector3 d = ray.direction;
    Point3 c = center;
    Point3 o = ray.origin;
    
    // Compute some factors used in computation
    double qx = o.x - c.x;
    double qy = o.y - c.y;
    double qz = o.z - c.z;
    double dd = d.lengthsquared();
    double qd = qx * d.x + qy * d.y + qz * d.z;
    double qq = qx * qx + qy * qy + qz * qz;
    
    // solving the quadratic equation for t at the pts of intersection
    // dd*t^2 + (2*qd)*t + (qq-r^2) = 0
    double discriminantsqr = (qd * qd - dd * (qq - radius * radius));
    
    // If the discriminant is less than zero, there is no intersection
    if (discriminantsqr < 0) {
      return false;
    }
    
    // Otherwise check and make sure that the intersections occur on the ray (t
    // > 0) and return the closer one
    double discriminant = Math.sqrt(discriminantsqr);
    double t1 = (-qd - discriminant) / dd;
    double t2 = (-qd + discriminant) / dd;
    double t = 0;
    if (t1 > ray.start && t1 < ray.end) {
      t = t1;
    }
    else if (t2 > ray.start && t2 < ray.end) {
      t = t2;
    }
    else {
      return false; // Neither intersection was in the ray's half line.
    }
    
    // There was an intersection, fill out the intersection record
    if (outRecord != null) {
      outRecord.t = t;
      ray.evaluate(outRecord.location, t);
      outRecord.surface = this;
      
      outRecord.normal.sub(outRecord.location, center);
      tMat.rightMultiply(outRecord.location);
      tMatTInv.rightMultiply(outRecord.normal);
      outRecord.normal.normalize();

    }
    
    return true;

  }
  
  public void computeBoundingBox() {
    // Compute the bounding box and store the result in
    // averagePosition, minBound, and maxBound.
    
    averagePosition = new Point3(center);
    tMat.rightMultiply(averagePosition);
    
    minBound = new Point3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY); 
    maxBound = new Point3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    
    Point3 []v = new Point3[8];
    int count = 0;
    for(int i=-1;i<2;i+=2) {
      for(int j=-1;j<2;j+=2) {
        for(int k=-1;k<2;k+=2) {
          v[count] = new Point3(center);
          v[count].x += radius * i;
          v[count].y += radius * j;
          v[count].z += radius * k;
          count++;
        }
      }
    }

    for (int i=0;i<8;i++) {
      tMat.rightMultiply(v[i]);
      for(int j=0;j<3;j++) {
        if(v[i].getE(j) < minBound.getE(j))
          minBound.setE(j, v[i].getE(j));
        if(v[i].getE(j) > maxBound.getE(j))
          maxBound.setE(j, v[i].getE(j));
      }
    }

  }
  
  /**
   * @see Object#toString()
   */
  public String toString() {
    return "sphere " + center + " " + radius + " " + material + " end";
  }

}