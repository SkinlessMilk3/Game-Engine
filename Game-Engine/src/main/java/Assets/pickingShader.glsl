#shader vertex
#version 410 core
layout(location = 0) in vec3 points;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texCoords;
layout(location = 3) in float texId;
layout(location = 4) in float entityId;

uniform mat4 u_projection;
uniform mat4 u_view;

out vec2 v_texCoords;
out vec4 v_color;
out float v_texId;
out float v_entityId;

void main()
{
    v_color = color;
    v_texCoords = texCoords;
    v_texId = texId;
    v_entityId = entityId;

    gl_Position = u_projection * u_view * vec4(points, 1.0);
}

#shader fragment
#version 410 core

in vec2 v_texCoords;
in vec4 v_color;
in float v_texId;
in float v_entityId;

uniform sampler2D u_textures[511];

out vec3 fragColor;

void main()
{
    vec4 texColor = vec4(1, 1, 1, 1);

    if (v_texId > 0) {
        int id = int(v_texId);
        texColor = v_color * texture(u_textures[id], v_texCoords);
    }

    if (texColor.a < 0.5) {
        discard;
    }
    fragColor = vec3(v_entityId, v_entityId, v_entityId);
}