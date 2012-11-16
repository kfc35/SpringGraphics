
package ray.accel;

import java.util.Arrays;
import java.util.Comparator;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
import ray.surface.Surface;

/**
 * Class for Axis-Aligned-Bounding-Box to speed up the intersection look up time.
 *
 * @author ss932, pramook
 */
public class Bvh implements AccelStruct {   
	/** A shared surfaces array that will be used across every node in the tree. */
	private Surface[] surfaces;

	/** A comparator class that can sort surfaces by x, y, or z coordinate. */
	static MyComparator cmp = new MyComparator();
	
	/** left and right are indices for []surfaces,
	 * meaning that this tree node contains a set of surfaces 
	 * from surfaces[left] to surfaces[right-1]. */
	int left, right;
	
	/** The root of the BVH tree. */
	BvhNode root;

	public Bvh(Surface[] surfaces, int left, int right) {
		this.surfaces = surfaces;
		this.left = left;
		this.right = right;
	}   

	/**
	 * Set the shared surfaces that every node in the tree will use by 
	 * using start and end indices.
	 */
	public void setSurfaces(Surface []surfaces) {
		this.surfaces = surfaces;
	}

	/**
	 * Set outRecord to the first intersection of ray with the scene. Return true
	 * if there was an intersection and false otherwise. If no intersection was
	 * found outRecord is unchanged.
	 *
	 * @param outRecord the output IntersectionRecord
	 * @param ray the ray to intersect
	 * @param anyIntersection if true, will immediately return when found an intersection
	 * @return true if and intersection is found.
	 */
	public boolean intersect(IntersectionRecord outRecord, Ray rayIn, boolean anyIntersection) {
		return intersectHelper(root, outRecord, rayIn, anyIntersection);
	}
	
	/**
	 * A helper method to the main intersect method. It finds the intersection with
	 * any of the surfaces under the given BVH node.  
	 *   
	 * @param node a BVH node that we would like to find an intersection with surfaces under it
	 * @param outRecord the output InsersectionMethod
	 * @param rayIn the ray to intersect
	 * @param anyIntersection if true, will immediately return when found an intersection
	 * @return true if an intersection is found with any surface under the given node
	 */
	private boolean intersectHelper(BvhNode node, IntersectionRecord outRecord, Ray rayIn, boolean anyIntersection)
	{
		// TODO(B): fill in this function.
		// Hint: For a leaf node, use a normal linear search. 
		// Otherwise, search in the left and right children.
		
		return false;
	}


	@Override
	public void build() {
		root = createTree(left, right);
	}
	
	/**
	 * Create a BVH [sub]tree.  This tree node will be responsible for storing
	 * and processing surfaces[left] to surfaces[right-1].
	 * @param left The left index of surfaces
	 * @param left The right index of surfaces
	 */
	private BvhNode createTree(int left, int right) {
		// TODO(B): fill in this function.
		int i, j;

		// ==== Step 1 ====
		// Find out the BIG bounding box enclosing all the surfaces in the range [left, right)
		// and store them in minB and maxB.
		// Hint: To find the bounding box for each surface, use getMinBound() and getMaxBound() */
		Point3 minB = new Point3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY); 
		Point3 maxB = new Point3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

		
		
		// ==== Step 2 ====
		// Check for the base case. 
		// If the range [left, right) is small enough, just return a new leaf node.

		

		// ==== Step 3 ====
		// Figure out the widest dimension (x or y or z).
		// If x is the widest, set widestDim = 0. If y, set widestDim = 1. If z, set widestDim = 2.
		int widestDim = 0;


		
		// ==== Step 4 (DONE) ====
		// Sort surfaces according to the widest dimension.
		// You can also implement O(n) randomized splitting algorithm.
		cmp.setIndex(widestDim);
		Arrays.sort(surfaces, left, right, cmp);


		// ==== Step 5 ====
		// Recursively create left and right children.
		
		return null;
	}

}

class MyComparator implements Comparator<Surface> {
	int index;
	public MyComparator() {  }

	public void setIndex(int index) {
		this.index = index;
	}

	public int compare(Surface o1, Surface o2) {
		double v1 = o1.getAveragePosition().getE(index);
		double v2 = o2.getAveragePosition().getE(index);
		if(v1 < v2) return 1;
		if(v1 > v2) return -1;
		return 0;
	}

}
