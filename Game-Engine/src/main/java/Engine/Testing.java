package Engine;

import Renderer.*;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;

public class Testing {
    private Shader shader;
    private VBO vbo;
    private VAO vao;
    private IBO ibo;
    private Renderer renderer;
    private Camera camera;

    float[] points = {
      0.0f, 250.5f, 0.0f,
       0.0f, 0.0f, 0.0f,
       500.5f, 0.0f, 0.0f,

       500.5f, 250.5f, 0.0f,
    };

    int[] indices = {
            0, 1, 2,
            2, 0, 3
    };

    public Testing(){

        camera = new Camera(new Vector2f());
        renderer = new Renderer();
        shader = new Shader("Assets/testing.glsl");
        vbo = new VBO(GL_ARRAY_BUFFER, points);
        ibo = new IBO(indices);
        vao = new VAO();

        vbo.bind();
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3*4, 0);
        glEnableVertexAttribArray(0);
camera.adjustProjection();
    }

    public void onUpdate(){
        shader.bind();GL_LOG.Log_Data("error: "+glGetError());
vao.bind();

//vbo.bind();
glEnableVertexAttribArray(0);
        shader.setUniformMat4("u_projection", camera.getProjectionMatrix());
        renderer.Draw(vao, ibo, shader);
    }
}
