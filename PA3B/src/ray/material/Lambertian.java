
package ray.material;

import ray.IntersectionRecord;
import ray.RayRecord;
import ray.math.Color;
import ray.math.Vector3;

/**
 * A Lambertian material scatters light equally in all directions. BRDF value is
 * a constant
 *
 * @author ags
 */
public class Lambertian extends Material {

	/** The color of the surface. */
	protected final Color diffuseColor = new Color(1, 1, 1);
	public void setDiffuseColor(Color inDiffuseColor) { diffuseColor.set(inDiffuseColor); }

	public Lambertian() { }
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "lambertian: " + diffuseColor;
	}

	/**
	 * The Lambertian material can interact with light, so return true.
	 */
	@Override
	public boolean canInteractWithLight() { 
		return true;
	}

	/**
	 * Evaluate the BRDF. This should be very simple.
	 */
	@Override
	public void evaluate(Color value, IntersectionRecord record, Vector3 incoming, Vector3 outgoing) {
		// TODO(A): Fill in this function.
		Vector3 normal = new Vector3(record.normal);
		normal.normalize();
		Vector3 incomingNormalized = new Vector3(incoming);
		incomingNormalized.normalize();
		// kd * max(n dot incoming, 0)
		Color toSet = new Color(diffuseColor);
		toSet.scale(Math.max(normal.dot(incomingNormalized), 0.0));
		value.set(toSet);
	}

	/**
	 * The Lambertian material does not have any perfectly specular component,
	 * so return false.
	 */
	@Override
	public boolean hasPerfectlySpecularComponent() {		
		return false;
	}

	/**
	 * Since the Lambertian material does not have a perfectly specular component, this method returns null.
	 */
	@Override
	public RayRecord[] getIncomingSpecularRays(IntersectionRecord record,
			Vector3 outgoing) {
		return null;
	}

}