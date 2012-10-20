
#version 120

// This value is set by ShaderToonMaterial to hold the
// object's diffuse color
uniform vec4 diffuse;

// TODO: (Problem 1.3) Add the same "varying" being sent by the
// vertex shader

void main()
{
    // set color based on quantization
    // TODO: (Problem 1.3) Implement the fragment shader of the
    // toon quantize shader.
    
    gl_FragColor = vec4(0.0, 1.0, 1.0, 1.0); // for now, just setting to teal
}
