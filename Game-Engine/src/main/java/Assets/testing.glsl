#shader vertex
#version 330 core

layout(location = 0) in vec3 points;
uniform mat4 u_projection;
void main(){
    gl_Position = u_projection*vec4(points, 1.0);
}

#shader fragment
#version 330 core

out vec4 fragColor;
void main(){
    fragColor = vec4(1.0, 0.0, 0, 1.0);
}