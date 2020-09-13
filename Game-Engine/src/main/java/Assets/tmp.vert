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

    //Cast to a vec4/vector 4 with 4 vector directions
    //so need to add that extra 1.0 direction. Not sure why 1.0 though.
}