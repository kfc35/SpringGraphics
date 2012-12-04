
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
  int []index;
  
  double a, b, c, d, e, f;
  public Triangle(Mesh owner, int index0, int index1, int index2, Material material) {
    this.owner = owner;
    index = new int[3];
    index[0] = index0;
    index[1] = index1;
    index[2] = index2;
    
    Point3 v0 = owner.getVertex(index0);
    Point3 v1 = owner.getVertex(index1);
    Point3 v2 = owner.getVertex(index2);
    
    if(!owner.existsNormals()) {
      Vector3 e0 = new Vector3(), e1 = new Vector3();
      e0.sub(v1, v0);
      e1.sub(v2, v0);
      norm = new Vector3();
      norm.cross(e0, e1);
    }
    a = v0.x-v1.x;
    b = v0.y-v1.y;
    c = v0.z-v1.z;
    
    d = v0.x-v2.x;
    e = v0.y-v2.y;
    f = v0.z-v2.z;
    
    this.setMaterial(material);
  }

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
    // TODO(B): fill in this function.
    // Hint: This object can be transformed by a transformation matrix.
    // So the rayIn needs to be processed so that it is in the same coordinate as the object.
    
    return false;
  }

  public void computeBoundingBox() {
    // TODO(B): Compute the bounding box and store the result in
    // averagePosition, minBound, and maxBound.
  }

  /**
   * @see Object#toString()
   */
  public String toString() {
    return "Triangle ";
  }
}