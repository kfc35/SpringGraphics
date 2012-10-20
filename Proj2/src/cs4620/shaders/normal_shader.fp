
#version 120

varying vec3 color;

void main()
{
    // TODO: (Problem 1.1) Implement the fragment shader of the
    // normal shader here.
    
    // Take the interpolated color variable created from the vertex shader
    gl_FragColor = vec4(color, 1.0);
}
