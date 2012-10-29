#version 120

uniform sampler2D noise_texture;
uniform sampler2D fire_texture;
uniform float time;

uniform vec3 scroll_speeds;

varying vec3 pos;

void main() {
	// TODO: (Problem 2) Write this fragment shader;
	
	gl_FragColor = vec4(1.0,1.0,0.0,1.0);
}