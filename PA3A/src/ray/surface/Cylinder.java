
package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;

public class Cylinder extends Surface {

  /** The center of the bottom of the cylinder  x , y ,z components. */
  protected final Point3 center = new Point3();
  public void setCenter(Point3 center) { this.center.set(center); }

  /** The radius of the cylinder. */
  protected double radius = 1.0;
  public void setRadius(double radius) { this.radius = radius; }

  /** The height of the cylinder. */
  protected double height = 1.0;
  public void setHeight(double height) { this.height = height; }

  public Cylinder() { }

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
    // TODO(A): fill in this function.
    // Hint: This object can be transformed by a transformation matrix.
    // So the rayIn needs to be processed so that it is in the same coordinate as the object.

    return false;
  }

  public void computeBoundingBox() {
    // TODO(B): Compute the bounding box and store the result in
    // averagePosition, minBound, and maxBound.
    // Hint: The bounding box may be transformed by a transformation matrix.
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    return "Cylinder " + center + " " + radius + " " + height + " "+ material + " end";
  }
}
