
#version 120

// How far the object's surface should be displaced (in eye space)
uniform float displaceScale;

void main()
{
    // TODO: (Problem 1.3) Implement the fragment shader of the
    // toon displace shader. This one should be straightforward :)
    gl_FragColor = vec4(0.0,0.0,0.0,1); //outlining is black, so black!
}
