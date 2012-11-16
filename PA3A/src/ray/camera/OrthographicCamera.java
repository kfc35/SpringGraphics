package ray.camera;

import ray.Ray;
import ray.math.Point3;
import ray.math.Vector3;

public class OrthographicCamera implements Camera {
	
    /*
     * Fields that are read in from the input file to describe the camera.
     * You'll probably want to store some derived values to make ray generation easy.
     */

	protected final Point3 viewPoint = new Point3();
	public void setViewPoint(Point3 viewPoint) { this.viewPoint.set(viewPoint); }
	  
	protected final Vector3 viewDir = new Vector3(0, 0, -1);
	public void setViewDir(Vector3 viewDir) { this.viewDir.set(viewDir); }
	  
	protected final Vector3 viewUp = new Vector3(0, 1, 0);
	public void setViewUp(Vector3 viewUp) { this.viewUp.set(viewUp); }
	   
	protected double viewWidth = 1.0;
	public void setViewWidth(double viewWidth) { this.viewWidth = viewWidth; }
	  
	protected double viewHeight = 1.0;
	public void setViewHeight(double viewHeight) { this.viewHeight = viewHeight; }
	
	/*
	 * Derived values that are computed before ray generation.
	 * basisU, basisV, and basisW form an orthonormal basis.
	 * 
	 * basisW is parallel to projNormal.
	 */
	protected final Vector3 basisU = new Vector3();
	protected final Vector3 basisV = new Vector3();
	protected final Vector3 basisW = new Vector3();
	protected final Vector3 centerDir = new Vector3();

	// Has the view been initialized?
	protected boolean initialized = false;
	
	/**
	 * Initialize the derived view variables to prepare for using the camera.
	 */
    public void initView() {
	    // TODO(A): fill in this function. 
	    // Hint:
	    //   1. set basisW to be parallel to projection normal but pointing to the opposite direction.
	    //   2. set basisU to be parallel to the image's X-axis.
	    //   3. set basisV to be parallel to the image's Y-axis.
	    
	    initialized = true;
    }


    /**
     * Set outRay to be a ray from the camera through a point in the image.
     *
     * @param outRay The output ray (not normalized)
     * @param inS The s coord of the image point (range [0,1])
     * @param inT The t coord of the image point (range [0,1])
     */
	@Override
	public void getRay(Ray outRay, double inS, double inT) {
		if (!initialized) initView();
	    
	    // TODO(A): fill in this function.
		
		
	}
}
