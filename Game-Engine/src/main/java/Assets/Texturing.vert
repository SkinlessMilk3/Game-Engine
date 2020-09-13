#shader vertex
#version 410
layout(location = 0) in vec4 vertex_point;
layout(location = 1) in vec3 texColors;
layout(location = 2) in vec2 texCoords;
//specifies the location in the vao where certain
//data comes from. I.e. where the vertex points come from, index 0, and where the
//colour data comes from, index 1.

out vec2 v_texCoords;
void main(){
    v_texCoords = texCoords;
    gl_Position = vertex_point;

}

#shader fragment
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