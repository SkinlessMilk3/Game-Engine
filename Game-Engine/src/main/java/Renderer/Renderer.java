package Renderer;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    int renderId;

    public Renderer(){

    }

    public void Clear(){
        glClearColor(0.8f, 1.8f, 0.8f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
    }
    public void Draw(final VAO vao, final IBO ibo, final Shader shader){

        shader.bind();
        vao.bind();
        ibo.bind();

        glDrawElements(GL_TRIANGLES, ibo.getCount(), GL_UNSIGNED_INT, 0);
    }

    public void Draw(final VAO vao, final Shader shader, final int count){

        shader.bind();
        vao.bind();

        glDrawArrays(GL_TRIANGLES, 0, count);
    }
}
