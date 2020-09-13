package Renderer;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

/*
Helper class that abstracts away the vertex buffer objects.
 */
public class VBO {
    int renderId;
    int target;

    public VBO(int target, float[] buffer){
        this.target = target;

        renderId = glGenBuffers();
        glBindBuffer(this.target, renderId);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }
    public void bind(){
        glBindBuffer(target, renderId);
    }

    public void unbind(){
        glBindBuffer(target, 0);
    }

    public void delete(){
        glDeleteBuffers(renderId);
    }
}
