package ray.material;

import ray.IntersectionRecord;
import ray.RayRecord;
import ray.math.Color;
import ray.math.Vector3;

/**
 * A Glass material. 
 *
 */
public class Glass extends Material {

	protected double refractiveIndex;
	public void setRefractiveIndex(double refractiveIndex) { this.refractiveIndex = refractiveIndex; }


	public Glass() { }

	/**
	 * The glass material cannot directly interact with light, so
	 * return false.
	 */
	@Override
	public boolean canInteractWithLight() {
		return false;
	}

	/**
	 * As the glass material cannot interact directly with light,
	 * we set the value to zero.
	 */
	@Override
	public void evaluate(Color value, IntersectionRecord record,
			Vector3 incoming, Vector3 outgoing) {
		value.set(0.0);
	}


	/**
	 * The glass material has at least one perfectly specular component,
	 * so return true.
	 */
	@Override
	public boolean hasPerfectlySpecularComponent() {
		return true;
	}


	@Override
	public RayRecord[] getIncomingSpecularRays(IntersectionRecord record, Vector3 outgoing) {
		// TODO(B): Fill in this method.

		return null;
	}
}