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

		// --- copied from Glazed -- calculate n_1, n_2, fresnel
		Vector3 outgoingNorm = new Vector3(outgoing);
		outgoingNorm.normalize();
		
		Vector3 normalNorm = record.normal;
		normalNorm.normalize();
		
		//Project outgoing ray to normal
		double projection = outgoingNorm.dot(normalNorm);
		
		//Fresnel Coefficient using Schlick's Approx
		double n_1, n_2;
		if (projection > 0) { //outgoing is outside the material
			n_1 = 1.0;
			n_2 = refractiveIndex;
		}
		else if (projection < 0) { //outgoing is inside the material
			n_1 = refractiveIndex;
			n_2 = 1.0;
		}
		else { //this should never happen
			//if the outgoing and the normal are perp., then the
			//outgoing is implied not to intersect the surface, contradiction!
			return null;
		}
		
		double r_0 = Math.pow(((n_2 - n_1)/(n_2 + n_1)), 2.0);
		//Fresnel Coefficient
		//r_0 + (1 - r_0)(1 - cos(theta))^5
		//cos(theta) = (normalNorm dot outgoingNorm) / (||normalNorm|| ||outgoingNorm||)
		double fresnel = r_0 + (1.0 - r_0) * Math.pow(1.0 - projection, 5.0);
		
		// --- end copy
		
		return null;
	}
}