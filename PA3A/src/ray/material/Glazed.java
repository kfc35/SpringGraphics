
package ray.material;

import ray.IntersectionRecord;
import ray.RayRecord;
import ray.math.Color;
import ray.math.Vector3;

public class Glazed extends Material {

	protected double refractiveIndex;
	public void setRefractiveIndex(double refractiveIndex) { this.refractiveIndex = refractiveIndex; }

	protected Material substrate;
	public void setSubstrate(Material substrate) {
		if (substrate.hasPerfectlySpecularComponent())
			throw new RuntimeException("Substrate to a glazed material cannot have perfectly specular component!");
		this.substrate = substrate; 
	}

	/** The exponent controlling the sharpness of the specular reflection. */
	protected double exponent = 1.0;
	public void setExponent(double exponent) { this.exponent = exponent; }

	public Glazed() { }

	/**
	 * Whether the glazed material can interact with light or not depends
	 * on whether the substrate can do so or not, so just return the substrate's
	 * result of the same method.
	 */
	@Override
	public boolean canInteractWithLight() {
		return substrate.canInteractWithLight();
	}
	
	@Override
	public void evaluate(Color value, IntersectionRecord record, Vector3 incoming,
			Vector3 outgoing) {
		// TODO(B): fill in this function.
	}

	/**
	 * The glazed material has a perfectly specular component, so return true.
	 */
	@Override
	public boolean hasPerfectlySpecularComponent() {
		return true;
	}

	/**
	 * Generate one ray record, which should contains the ray pointing
	 * in the perfect-mirror reflection direction, and the factor should
	 * be the Fresnel coefficient value.
	 */
	@Override
	public RayRecord[] getIncomingSpecularRays(IntersectionRecord record, Vector3 outgoing) {
		// TODO(B): Fill in this method.		
		
		return null;
	}
}