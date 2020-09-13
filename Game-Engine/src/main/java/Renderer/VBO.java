package Renderer;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

/*
Helper class that abstracts away the vertex buffer objects.
 */
public class VBO {
    private int renderId;
    private int target;
    private FloatBuffer localBuffer;

    public VBO(int target, final float[] buffer){
        this.target = target;
        localBuffer = BufferUtils.createFloatBuffer(buffer.length);
        localBuffer.put(buffer).flip();
        renderId = glGenBuffers();

        glBindBuffer(this.target, renderId);
        glBufferData(GL_ARRAY_BUFFER, localBuffer, GL_STATIC_DRAW);
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
