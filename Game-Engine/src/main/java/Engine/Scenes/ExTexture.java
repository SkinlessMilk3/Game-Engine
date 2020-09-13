package Engine.Scenes;

import Engine.GL_LOG;
import Renderer.Shader;
import Renderer.Texture;
import Renderer.VAO;
import Renderer.VBO;

import static org.lwjgl.opengl.GL20.*;

public class ExTexture {
    float [] points = {
            -0.5f, -0.5f,   0.0f,   1.0f, 0.0f, 0.0f,   0.0f, 0.0f,//bottom left
            0.5f,   -0.5f,   0.0f,  0.0f, 1.0f, 0.0f,   1.0f, 0.0f,//bottom right
            0.5f,   0.5f,   0.0f,   0.0f, 0.0f, 1.0f,   1.0f, 1.0f,//top right

            0.5f,  0.5f,  0.0f,   0.0f, 0.0f, 1.0f,     1.0f, 1.0f,//top right
            -0.5f, 0.5f,  0.0f,   0.0f, 1.0f, 0.0f,     0.0f, 1.0f,
            -0.5f, -0.5f, 0.0f,   1.0f, 0.0f, 0.0f,      0.0f, 0.0f
    };
    Texture texture;
    Shader shader;
    VAO vao;

    public ExTexture(){
        texture = new Texture("Assets/Textures/Fantasy-My-Hero-Academia.jpg");
        texture.bind(0);

        shader = new Shader("Assets/Texturing.vert");

        GL_LOG.Log_Data("Linking check "+Integer.toString(glGetError()));


        VBO vbo = new VBO(GL_ARRAY_BUFFER, points);
        vao = new VAO();

        GL_LOG.Log_Data("New VAO and VBO "+Integer.toString(glGetError()));

        int sizeFloat = 4;
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * sizeFloat, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * sizeFloat, 3 * sizeFloat);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * sizeFloat, 6 * sizeFloat);
        GL_LOG.Log_Data("set buffer layouts "+Integer.toString(glGetError()));

        texture.bind(0);
    }
    public void onUpdate(){

        shader.setGLUniform1i("u_texture", 0);

        glUseProgram(shader.getId());
        vao.bind();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawArrays(GL_TRIANGLES, 0, 6);


        //GL_LOG.Log_Data("Drawing triangles "+Integer.toString(glGetError()));

    }
}
