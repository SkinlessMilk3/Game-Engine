#version 410

layout(location = 0)out vec4 frag_colour;

in vec2 v_texCoords;
uniform sampler2D u_texture;

void main(){
    vec4 texColor = texture(u_texture, v_texCoords);
    frag_colour = texColor;

    //cast to a vec4 which means now we have opacity. Set it to 1.0
    //so that it is visible.
}