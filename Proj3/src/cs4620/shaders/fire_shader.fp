#version 120

uniform sampler2D noise_texture;
uniform sampler2D fire_texture;
uniform float time;

uniform vec3 scroll_speeds;

//varying vec3 pos;

void main() {
	// TODO: (Problem 2) Write this fragment shader;
	
	/* Calculate three sets of texture coordinates and sample from
	 * the noise texture*/
	vec2 texCoordOne = vec2(gl_TexCoord[0].s, 
	mod((gl_TexCoord[0].t + time * scroll_speeds.x), 1.0));
	vec4 sampleOne = texture2D(noise_texture, texCoordOne);
	
	vec2 texCoordTwo = vec2(gl_TexCoord[0].s, 
	mod((gl_TexCoord[0].t + time * scroll_speeds.y), 1.0));
	vec4 sampleTwo = texture2D(noise_texture, texCoordTwo);
	
	vec2 texCoordThree = vec2(gl_TexCoord[0].s, 
	mod((gl_TexCoord[0].t + time * scroll_speeds.z), 1.0));
	vec4 sampleThree = texture2D(noise_texture, texCoordThree);
	
	/* Average all the values and sample from fireTexture*/
	vec4 avgSample = ((sampleOne + sampleTwo + sampleThree))/3.0;
	gl_FragColor = texture2D(fire_texture, avgSample.st);
}