
package ray.material;

import ray.IntersectionRecord;
import ray.RayRecord;
import ray.math.Color;
import ray.math.Vector3;

/**
 * A Phong material.
 *
 * @author ags, pramook
 */
public class Phong extends Material {

	/** The color of the diffuse reflection. */
	protected final Color diffuseColor = new Color(1, 1, 1);
	public void setDiffuseColor(Color diffuseColor) { this.diffuseColor.set(diffuseColor); }

	/** The color of the specular reflection. */
	protected final Color specularColor = new Color(1, 1, 1);
	public void setSpecularColor(Color specularColor) { this.specularColor.set(specularColor); }

	/** The exponent controlling the sharpness of the specular reflection. */
	protected double exponent = 1.0;
	public void setExponent(double exponent) { this.exponent = exponent; }

	public Phong() { }

	/**
	 * @see Object#toString()
	 */
	public String toString() {    
		return "phong " + diffuseColor + " " + specularColor + " " + exponent + " end";
	}

	/**
	 * The Phong material can interact directly with light, so return true.
	 */
	@Override
	public boolean canInteractWithLight() { 
		return true;
	}

	@Override
	public void evaluate(Color value, IntersectionRecord record, Vector3 incoming, Vector3 outgoing) {
		// TODO(A): Fill in this function.
		
		//normalize everything
		Vector3 normal = new Vector3(record.normal);
		normal.normalize();
		Vector3 incomingNormalized = new Vector3(incoming);
		incomingNormalized.normalize();
		Vector3 outgoingNormalized = new Vector3(outgoing);
		outgoingNormalized.normalize();
		
		double nDotIncoming = normal.dot(incomingNormalized);
		if (nDotIncoming < 0.0) {
			value.set(0.0);
		}
		else {
			//kd * max(ndotincoming, 0.0)
			Color diff = new Color(diffuseColor);
			diff.scale(Math.max(nDotIncoming, 0.0)); //nDotIncoming >= 0.0
			
			//ks * (max(ndothalf, 0.0)^exponent)
			Vector3 halfVector = new Vector3(incomingNormalized);
			halfVector.add(outgoingNormalized);
			halfVector.normalize();
			Color spec = new Color(specularColor);
			spec.scale((Math.pow(Math.max(normal.dot(halfVector), 0.0), exponent)));
			
			Color toSet = new Color(diff);
			toSet.add(spec);
			
			value.set(toSet);
		}
	}

	/**
	 * The Phong material does not have a perfectly specular component, so return false.
	 */
	@Override
	public boolean hasPerfectlySpecularComponent() {	
		return false;
	}

	/**
	 * Since the Phong material does not have a perfectly specular component, this method returns null.
	 */
	@Override
	public RayRecord[] getIncomingSpecularRays(IntersectionRecord record,
			Vector3 outgoing) {
		return null;
	}
}