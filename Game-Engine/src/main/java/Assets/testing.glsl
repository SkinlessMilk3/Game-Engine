#shader vertex
#version 330 core


layout(location = 0) in vec3 points;
//layout(location = 1) in vec2 texCoords;
layout(location = 1) in vec4 color;

uniform mat4 u_projection;
uniform mat4 u_view;
uniform mat4 u_transform;

out vec2 v_texCoords;
out vec4 v_color;
void main(){
    v_color = color;
    //v_texCoords = texCoords;
    gl_Position = u_projection * u_view * /*u_transform */ vec4(points, 1.0);
}

#shader fragment
#version 330 core

//uniform sampler2D u_texture;
uniform vec4 u_color;

out vec4 fragColor;
//in vec2 v_texCoords;
in vec4 v_color;
void main(){
    //vec4 tex = texture(u_texture, v_texCoords);
    fragColor = /*tex */ v_color;//u_color;///vec4(1.0,1.0,0.0,1.0);//
}