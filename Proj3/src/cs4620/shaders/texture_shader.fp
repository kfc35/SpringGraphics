#version 120

uniform vec4 ambient;
uniform vec4 diffuse;
uniform vec4 specular;
uniform float shininess;

uniform sampler2D texture;

void main() {
	// TODO: (Problem 2) Write this Fragment Shader
	
	gl_FragColor = vec4(1.0,1.0,0.0,1.0);
}