#version 410
in vec3 colour;
out vec4 frag_colour;
void main(){
    frag_colour = vec4(colour, 1.0);
    //cast to a vec4 which means now we have opacity. Set it to 1.0
    //so that it is visible.
}