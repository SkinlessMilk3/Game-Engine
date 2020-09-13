package Renderer;

import static org.lwjgl.opengl.GL30.*;

/*
Helper class that abstracts opengl Vertex array objects and functions.
 */
public class VAO {

    int renderId;

    public VAO(){
        renderId = glGenVertexArrays();
        glBindVertexArray(renderId);
    }

    public void bind(){
        glBindVertexArray(renderId);
    }

    public void unbind(){
        glBindVertexArray(0);
    }

    public void delete(){
        glDeleteVertexArrays(renderId);
    }
    public int getID(){
        return renderId;
    }
}
