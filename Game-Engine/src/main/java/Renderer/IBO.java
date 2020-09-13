package Renderer;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public class IBO {
    int renderId;
    IntBuffer localBuffer;
    private int sizeOfInt = 4;
    private int bufferCount;

    public IBO(int target, final int[] buffer){
        localBuffer = BufferUtils.createIntBuffer(buffer.length);

        bufferCount = buffer.length;
        localBuffer.put(buffer);
        localBuffer.flip();

        renderId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, renderId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeOfInt * localBuffer.capacity(), GL_STATIC_DRAW);
    }

    public void bind(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, renderId);
    }

    public void unbind(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void delete(){
        glDeleteBuffers(renderId);
    }

    public int getCount(){
        return bufferCount;
    }
}
