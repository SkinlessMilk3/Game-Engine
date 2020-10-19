package Renderer;

import Utils.GL_LOG;
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

    public VBO(final int target, final float[] buffer){
        this.target = target;
        localBuffer = BufferUtils.createFloatBuffer(buffer.length);
        localBuffer.put(buffer).flip();
        renderId = glGenBuffers();

        glBindBuffer(this.target, renderId);
        glBufferData(GL_ARRAY_BUFFER, localBuffer, GL_STATIC_DRAW);
    }
    public VBO(final long size){
        renderId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, renderId);
        glBufferData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);
    }
    public void pushData(float[] data){
        bind();
        glBufferSubData(GL_ARRAY_BUFFER, 0, data);

        GL_LOG.Log_Data("pushData error: 37 " + glGetError());
    }
    public void bind(){
        glBindBuffer(GL_ARRAY_BUFFER, renderId);
    }

    public void unbind(){
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void delete(){
        glDeleteBuffers(renderId);
    }
}
