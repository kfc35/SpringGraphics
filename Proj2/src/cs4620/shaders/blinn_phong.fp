
#version 120

// These values are set by the ShaderPhongMaterial class to
// contain the object's material properties from the
// left pane of the ProblemA2.java window.
uniform vec4 ambient;
uniform vec4 diffuse;
uniform vec4 specular;
uniform float shininess;

// TODO: (Problem 1.2) Add the same varying variables
// that are being set by the vertex shader

/* Interpolated Normal from the vertex shader*/
varying vec3 normal;
/* Interpolated Position, which should be equal to the
 * position you are calculating*/
varying vec4 position;

void main()
{
    // TODO: (Problem 1.2) Implement the fragment shader for
    // per-pixel Blinn-Phong here.
    
    vec4 totalLight;
    
    //ambientLight = k_a * I_a
    vec4 ambientLight = ambient * gl_LightSource[0].ambient;
    
    /* Calculate n dot l*/
    // for vectors in homogeneous coordinates, the last entry is a 0.
    vec3 interpolatedNormalizedNormal = normalize(normal);
    vec3 vectorPosition = position.xyz;
    vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vectorPosition);
    float nDotL = dot(interpolatedNormalizedNormal, lightDirection);
    
    if (nDotL > 0.0) {
    	/** Need to calculate diffuse and specular lighting **/
    	vec4 diffuseLight = diffuse * gl_LightSource[0].diffuse * nDotL;
    	
    	/* The eye is at the origin in eye space, so must multiply
    	 * by the inverse of the ModelViewProjectionMatrix to get where the eye is*/
    	vec4 eyePosition = gl_ModelViewProjectionMatrixInverse * vec4(0, 0, 0, 1);
    	vec3 eyeDirection = normalize(eyePosition.xyz - vectorPosition);
    	vec3 halfVector = normalize(lightDirection + eyeDirection);
    	float nDotH = dot(interpolatedNormalizedNormal, halfVector);
    	vec4 specularLight = specular * gl_LightSource[0].specular *
    		pow((max(nDotH, 0.0)), shininess);
    	
    	totalLight = ambientLight + diffuseLight + specularLight;
    }
    else {
    	// Only ambient light
    	totalLight = ambientLight;
    }
    
    gl_FragColor = totalLight;
}
