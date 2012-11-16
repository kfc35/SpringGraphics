package ray.material;

import ray.IntersectionRecord;
import ray.RayRecord;
import ray.math.Color;
import ray.math.Vector3;

/**
 * This interface specifies what is necessary for an object to be a material.
 * The shade method is pretty obvious - a material should know how to "color"
 * itself.  The copy method is needed so that a deep copy may be performed by
 * Surface objects, which all have a generic reference to a material.
 * @author ags, pramook
 */
public abstract class Material {
	
	/**
	 * The material given to all surfaces unless another is specified.
	 */
	public static final Material DEFAULT_MATERIAL = new Lambertian();
	
	/**
	 * Returns true if this material can interact directly with a light source.
	 * Any material that is not a pure mirror should return true when this method
	 * is called.
	 */
	public abstract boolean canInteractWithLight();
	
	/**
	 * Calculate the BRDF value for this material at the intersection described in record.
	 * @param value Space for the output value
	 * @param record The intersection record, which hold the location, normal, etc. 
	 * @param incoming the direction of incoming light
	 * @param outgoing the direction of outgoing light 
	 */
	public abstract void evaluate(Color value, IntersectionRecord record, Vector3 incoming, Vector3 outgoing);
	
	/**
	 * Return true if this material has perfectly specular components.
	 */
	public abstract boolean hasPerfectlySpecularComponent();
	
	/**
	 * Create a list of incoming rays to be traced from a specular surface to compute perfectly specular
	 * reflection. While the rays are "incoming", they must always point from the surface. 
	 * @param record the intersect record of the hit point
	 * @param outgoing the direction that the light from the generated ray(s) strike the surface
	 * @return a list of incoming specular rays 
	 */
	public abstract RayRecord[] getIncomingSpecularRays(IntersectionRecord record, Vector3 outgoing);		
}