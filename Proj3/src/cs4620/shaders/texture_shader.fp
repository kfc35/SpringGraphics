#version 120

uniform vec4 ambient;
uniform vec4 diffuse;
uniform vec4 specular;
uniform float shininess;

uniform sampler2D texture;

varying vec3 normal;
varying vec4 position;

void main() {
	// TODO: (Problem 2) Write this Fragment Shader
	
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
    	vec4 texColor = texture2D(texture, gl_TexCoord[0].st);
    	vec4 diffuseLightBeforeTex = diffuse * gl_LightSource[0].diffuse * nDotL;
    	vec4 diffuseLight = texColor * diffuseLightBeforeTex;
    	
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