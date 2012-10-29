
#version 120

varying vec3 interpolatedNormal;
varying vec3 interpolatedColor;

void main()
{
    // TODO: (Problem 1.1) Implement the fragment shader of the
    // normal shader here.
    
    // STUBBED: this is here because the problem spec does not specify
    // whether to interpolate the normal or the color.
    bool perPixelShading = false;
    
    vec3 interpolatedNormalizedNormal = normalize(interpolatedNormal);
   
    if (perPixelShading) {
    	// Create per-pixel color based on interpolated normal
    	vec3 perPixelColor = vec3((interpolatedNormalizedNormal.x + 1)/2, 
    		(interpolatedNormalizedNormal.y + 1)/2, 
    		(interpolatedNormalizedNormal.z + 1)/2);
    	gl_FragColor = vec4(perPixelColor, 1.0);
    }
    else { //perVertexShading
    	// Merely use the interpolated color
    	gl_FragColor = vec4(interpolatedColor, 1.0);
    }
}
