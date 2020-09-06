#version 410
layout(location = 0) in vec3 vertex_point;

//specifies the location in the vao where certain
//data comes from. I.e. where the vertex points come from, index 0, and where the
//colour data comes from, index 1.

layout(location = 1) in vec3 colour_data;

uniform mat4 uProjection;
uniform mat4 uView;

out vec3 colour;
void main(){
    colour = colour_data;
    gl_Position = uProjection * uView * vec4(vertex_point, 1.0);

    //Cast to a vec4/vector 4 with 4 vector directions
    //so need to add that extra 1.0 direction. Not sure why 1.0 though.
}
