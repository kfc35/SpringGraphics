
#version 120

// These values are set by the FlowerMaterial class to
// contain the flower's Blinn-Phong material properties
uniform vec4 ambient;
uniform vec4 diffuse;
uniform vec4 specular;
uniform float shininess;

// Transform between the object's local frame and
// the "yellow" light-aligned frame in Figures 5/6/7
uniform mat4 frameToObj;
uniform mat4 objToFrame;

//TODO: (Problem 2) Put any additional uniforms for the shader here

//TODO: (Problem 2) Put any "varying" variables here (hint: these
// should be the same as your Blinn-Phong shader)
/* Interpolated Normal from the vertex shader*/
varying vec3 normal;
/* Interpolated Position, which should be equal to the
 * position you are calculating*/
varying vec4 position;

void main()
{
    // TODO: (Problem 3) Shade the bent flower using per-pixel Blinn-Phong
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

