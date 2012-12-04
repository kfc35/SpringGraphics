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
import ray.accel.Bvh;
import ray.camera.Camera;
import ray.math.Color;
import ray.surface.Surface;

public class WhittedRayTracer extends RayTracer {	  

	/**
	 * The main method takes all the parameters and assumes they are input files
	 * for the ray tracer. It tries to render each one and write it out to a PNG
	 * file named <input_file>.png.
	 *
	 * @param args
	 */
	public static final void main(String[] args) {
		WhittedRayTracer rayTracer = new WhittedRayTracer();
		rayTracer.run("data/scenes/whitted_ray_tracer", args);
	}

	/**
	 * The renderImage method renders the entire scene.
	 *
	 * @param scene The scene to be rendered
	 */
	public void renderImage(Scene scene) {

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
		int samples = scene.getSamples();
		double sInv = 1.0/samples;
		double sInvD2 = sInv / 2;
		double sInvSqr = sInv * sInv;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				pixelColor.set(0, 0, 0);

				// TODO(B): Support Anti-Aliasing
				for(int i = 0; i < samples; i++) {
					for(int j = 0; j < samples; j++) {
						// TODO(B): Compute the "ray" and call shadeRay on it.
						
						
						
						pixelColor.add(rayColor);
					}
				}
				pixelColor.scale(sInvSqr);

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
	public void shadeRay(Color outColor, Scene scene, Ray ray, Workspace workspace, Color absorption, int depth) {

		// Reset the output color
		outColor.set(0, 0, 0);
		
		// TODO(B): Return immediately if depth is greater than 12.



		// Rename all the workspace entries to avoid field accesses
		// and alot of typing "workspace."
		IntersectionRecord intersectionRecord = workspace.intersectionRecord;			

		// TODO(B): Fill in the part of BasicRayTracer.shadeRay
		//          from getting the first intersection onward.
		
		
		
		// TODO(B): Recursively trace rays due to perfectly specular components.
		// 1) Check whether the material has perfectly specular components.
		// 2) If so, call material.getIncomingSpecularRays to get the set of rays.
		// 3) For each ray, see if the scaling factor is greater than zero.
		//    If not, ignore the ray.
		// 4) If the factor is greater than zero, check whether the ray is going
		//    inside or outside the surface by dotting it with the normal.
		//    The ray goes inside if the dot product is less than zero.
		// 5) Based on whether the ray goes inside or outside, compute the absorption
		//    coefficient of the region the next ray will travel through.
		// 6) Call shadeRay recursively, increasing the depth by 1 and using
		//    the absorption coefficient you just computed.
		// 7) Scale the resulting ray color with the scaling factor of the specular ray,
		//    and add it to the output color.
		
		
		
		// TODO(B): Compute the distance that the ray travels and attenuate
		//          the output color by the absorption according to Beer's law.
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

		AccelStruct accelStruct = new Bvh(surfaceArray, 0, surfaceArray.length);
		accelStruct.build();
		return accelStruct;
	}
}
