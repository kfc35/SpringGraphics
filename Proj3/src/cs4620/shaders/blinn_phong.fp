
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
    vec3 interpolatedNormalizedNormal = normalize(normal);
    // light source position is already in EYE COORDINATES
    vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - position.xyz);
    float nDotL = dot(interpolatedNormalizedNormal, lightDirection);
    
    if (nDotL > 0.0) {
    	/** Need to calculate diffuse and specular lighting **/
    	vec4 diffuseLight = diffuse * gl_LightSource[0].diffuse * nDotL;
    	
    	/* The eye is at the origin in eye space*/
    	vec3 eyeDirection = normalize(vec3(0, 0, 0) - position.xyz);
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
