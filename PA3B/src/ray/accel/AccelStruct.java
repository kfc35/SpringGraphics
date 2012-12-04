package ray.accel;

import ray.IntersectionRecord;
import ray.Ray;
import ray.surface.Surface;

public interface AccelStruct {
	void setSurfaces(Surface[] surfaces);
	void build();
	public boolean intersect(IntersectionRecord outRecord, Ray rayIn, boolean anyIntersection);
}
