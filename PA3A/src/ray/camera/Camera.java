package ray.camera;

import ray.Ray;

public interface Camera {
	  void getRay(Ray outRay, double s, double t);
}
