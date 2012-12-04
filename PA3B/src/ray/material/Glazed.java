
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
		substrate.evaluate(value, record, incoming, outgoing);
		
		// TODO scale the value by Fresnel Coeff... which I have to compute again?
		//value.scale();
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
		Vector3 outgoingNorm = new Vector3(outgoing);
		outgoingNorm.normalize();
		
		Vector3 normalNorm = record.normal;
		normalNorm.normalize();
		
		//Project outgoing ray to normal
		double projection = outgoingNorm.dot(normalNorm);
		
		//Reflected Ray = 2 * N * (N dot outgoingNorm) - outgoing
		Vector3 reflectedRay = new Vector3(normalNorm);
		reflectedRay.scale(2.0);
		reflectedRay.scale(projection);
		reflectedRay.sub(outgoingNorm);
		
		RayRecord[] toReturn = new RayRecord[1];
		toReturn[0] = new RayRecord();
		toReturn[0].ray.set(record.location, reflectedRay);
		// don't set start and end...
		
		//Fresnel Coefficient
		//toReturn[0].factor = 
		
		return toReturn;
	}
}