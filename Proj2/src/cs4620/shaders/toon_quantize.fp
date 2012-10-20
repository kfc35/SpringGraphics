
#version 120

// This value is set by ShaderToonMaterial to hold the
// object's diffuse color
uniform vec4 diffuse;

// TODO: (Problem 1.3) Add the same "varying" being sent by the
// vertex shader
varying vec3 normal;
varying vec4 position;

void main()
{
    // set color based on quantization
    // TODO: (Problem 1.3) Implement the fragment shader of the
    // toon quantize shader.
    
    // Only calculate diffuse color.
    
    vec3 interpolatedNormalizedNormal = normalize(normal);
    vec3 vectorPosition = position.xyz;
    vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vectorPosition);
    float nDotL = dot(interpolatedNormalizedNormal, lightDirection);
    
    // Quantize sin value
    if (nDotL <= 0) {
    	nDotL = 0;
    }
    else if (nDotL <= 0.5) {
    	nDotL = 0.25;
    }
    else if (nDotL <= 0.75) {
    	nDotL = 0.5;
    }
    else {
    	nDotL = 0.75;
    }
    
    gl_FragColor = vec4(diffuse * gl_LightSource[0].diffuse * nDotL); // for now, just setting to teal
}
