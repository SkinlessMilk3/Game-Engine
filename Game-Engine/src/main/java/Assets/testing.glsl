#shader vertex
#version 330 core


layout(location = 0) in vec3 points;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texCoords;
layout(location = 3) in float texId;
layout(location = 4) in float entityId;

uniform mat4 u_projection;
uniform mat4 u_view;
uniform mat4 u_transform;

out vec2 v_texCoords;
out vec4 v_color;
out float v_texId;
void main(){
    v_color = color;
    v_texCoords = texCoords;
    v_texId = texId;
    gl_Position = u_projection * u_view * /*u_transform */ vec4(points, 1.0);
}

#shader fragment
#version 330 core

uniform vec4 u_color;
uniform sampler2D u_texture[511];

in vec2 v_texCoords;
in vec4 v_color;
in float v_texId;

out vec4 fragColor;

void main(){

        if (v_texId > 0) {
            int id = int(v_texId);
            fragColor = texture(u_texture[id], v_texCoords) * v_color;
        } else {
            fragColor = v_color;
        }

}