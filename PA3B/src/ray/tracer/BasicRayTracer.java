
package ray.tracer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ray.Image;
import ray.IntersectionRecord;
import ray.Ray;
import ray.Scene;
import ray.Workspace;
import ray.accel.AccelStruct;
import ray.accel.NaiveAccelStruct;
import ray.camera.Camera;
import ray.light.Light; //kfc35 - imported this for shadeRay
import ray.material.Material; //kfc35 - imported this for shadeRay
import ray.math.Color;
import ray.math.Point3;
import ray.math.Vector3; //kfc35 - imported this for shadeRay
import ray.surface.Surface;

/**
 * A simple ray tracer.
 *
 * @author ags, pramook
 */
public class BasicRayTracer extends RayTracer {

	/**
	 * The main method takes all the parameters and assumes they are input files
	 * for the ray tracer. It tries to render each one and write it out to a PNG
	 * file named <input_file>.png.
	 *
	 * @param args
	 */
	public static final void main(String[] args) {
		BasicRayTracer rayTracer = new BasicRayTracer();
		rayTracer.run("data/scenes/basic_ray_tracer", args);
	}		

	/**
	 * The renderImage method renders the entire scene.
	 *
	 * @param scene The scene to be rendered
	 */
	@Override
	protected void renderImage(Scene scene) {

		// Get the output image
		Image image = scene.getImage();
		Camera cam = scene.getCamera();

		// Set the camera aspect ratio to match output image
		int width = image.getWidth();
		int height = image.getHeight();

		// Timing counters
		long startTime = System.currentTimeMillis();

		// Do some basic setup
		Workspace work = new Workspace();
		Ray ray = work.eyeRay;
		Color pixelColor = work.pixelColor;
		Color rayColor = work.rayColor;

		int total = height * width;
		int counter = 0;
		int lastShownPercent = 0;

		// Loop through each pixel.
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				// TODO(A): Compute the "ray," 
				//          and call shadeRay on it to get the ray's color.
				cam.getRay(ray, (x+0.5)/width, (y+0.5)/height); // +0.5 for center of pixel
				shadeRay(rayColor, scene, ray, work);
				
				pixelColor.set(rayColor);

				//Gamma correct and clamp pixel values
				pixelColor.gammaCorrect(2.2);
				pixelColor.clamp(0, 1);
				image.setPixelColor(pixelColor, x, y);

				counter ++;
				if((int)(100.0 * counter / total) != lastShownPercent) {
					lastShownPercent = (int)(100.0*counter / total);
					System.out.println(lastShownPercent + "%");
				}
			}
		}

		// Output time
		long totalTime = (System.currentTimeMillis() - startTime);
		System.out.println("Done.  Total rendering time: "
				+ (totalTime / 1000.0) + " seconds");

	}

	/**
	 * This method returns the color along a single ray in outColor.
	 *
	 * @param outColor output space
	 * @param scene the scene
	 * @param ray the ray to shade
	 */
	public void shadeRay(Color outColor, Scene scene, Ray ray, Workspace workspace) 
	{
		// Reset the output color
		outColor.set(0, 0, 0);

		// Rename all the workspace entries to avoid field accesses
		// and a lot of typing "workspace."
		IntersectionRecord intersectionRecord = workspace.intersectionRecord;

		// TODO(A): Find the first intersection of "ray" with the scene.
		// Record intersection in intersectionRecord. If it doesn't hit anything, just return.
		if (!scene.getFirstIntersection(intersectionRecord, ray)) {
			return;
		}
		

		// TODO(A): Compute the color of the intersection point.
		// 1) Get the material from the intersection record.
		// 2) Check whether the material can interact directly with light.
		//    If not, do nothing.
		// 3) Compute the direction of outgoing light, by subtracting the
		//	  intersection point from the origin of the ray.
		// 4) Loop through each light in the scene.
		// 5) For each light, compute the incoming direction by subtracting
		//    the intersection point from the light's position.
		// 6) Compute the BRDF value by calling the evaluate method of the material. 
		// 7) If the BRDF is not zero, check whether the intersection point is
		//    shadowed.
		// 8) If the intersection point is not shadowed, scale the light intensity
		//    by the BRDF value and add it to outColor.	
		
		// 1)
		Material material = intersectionRecord.surface.getMaterial();
		// 2)
		if (!material.canInteractWithLight()) {
			return;
		}
		// 3)
		Vector3 intersectionPoint = new Vector3(intersectionRecord.location);
		Vector3 outgoing = new Vector3(ray.origin);
		outgoing.sub(intersectionPoint);
		//outgoing.normalize();
		// 4)
		for (Iterator<Light> iter = scene.getLights().iterator(); iter.hasNext();) {
			Light light = iter.next();
			// 5)
			Vector3 incoming = new Vector3(light.position);
			incoming.sub(intersectionPoint);
			//incoming.normalize();
			
			// 6)
			Color BDRF = new Color();
			material.evaluate(BDRF, intersectionRecord, incoming, outgoing);
			
			// 7)
			if (!BDRF.isZero()) {
				Ray shadowRay = new Ray();
				// 8)
				if (!isShadowed(scene, light, intersectionRecord, shadowRay)) {
					Color intensity = new Color(light.intensity);
					intensity.scale(BDRF);
					outColor.add(intensity);
				}
			}
		}
	}

	@Override
	protected AccelStruct createAccelStruct(Scene scene) {
		ArrayList<Surface> allSurfaces = new ArrayList<Surface>();
		List<Surface> surfaces = scene.getSurfaces();
		for (Iterator<Surface> iter = surfaces.iterator(); iter.hasNext();) {
			iter.next().appendRenderableSurfaces(allSurfaces);
		}
		Surface []surfaceArray = new Surface[allSurfaces.size()];
		int count = 0;
		for(Iterator<Surface> iter = allSurfaces.iterator(); iter.hasNext();)
			surfaceArray[count++] = iter.next();
		
		AccelStruct accelStruct = new NaiveAccelStruct(surfaceArray, 0, surfaceArray.length);
		accelStruct.build();
		return accelStruct;
	}
}