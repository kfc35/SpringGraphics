
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

void main()
{
    // TODO: (Problem 1.2) Implement the fragment shader for
    // per-pixel Blinn-Phong here.
    
    gl_FragColor = vec4(1.0, 0.0, 1.0, 1.0); // for now, just drawing purple
}
