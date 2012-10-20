
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

void main()
{
    // TODO: (Problem 3) Shade the bent flower using per-pixel Blinn-Phong
    
    gl_FragColor = vec4(1.0, 0.0, 1.0, 1.0);
}

