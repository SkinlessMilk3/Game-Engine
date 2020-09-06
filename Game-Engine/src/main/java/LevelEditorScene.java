import API.EventListeners.KeyEventListener;
//import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
//import java.nio.FloatBuffer;
//import java.nio.IntBuffer;

//import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LevelEditorScene extends Scene {

    public boolean changingScene = false;
    public float timeToChangeScene = 2.0f;

    private float points[]={

            0.0f, 0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            -0.5f, -0.5f,0.0f
    };
    private float colours[] = {
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f
    };

    private int vao, vbo_points, vbo_colours, shader_program;

    public LevelEditorScene()
    {
        System.out.println("Inside level editor!");
    }

    @Override
    public void init()
    {
        GL_Shader_Reader reader = new GL_Shader_Reader();

        vao = 0; //Vertex Buffer Object
        vbo_points = 0; //Vertex Array Object
        vbo_colours = 0;

        vbo_points = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_points);
        glBufferData(GL_ARRAY_BUFFER, points, GL_STATIC_DRAW);

        vbo_colours = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_colours);
        glBufferData(GL_ARRAY_BUFFER, colours, GL_STATIC_DRAW);

        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo_points);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, NULL);

        glBindBuffer(GL_ARRAY_BUFFER, vbo_colours);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, NULL);

        /*
         *We have two attributes, points and colours, one assigned to index 0 and index 1 of our
         * vao. We have to enable the vao currently binded so that we may use these attributes
         * in the shaders. I believe this only needs to happen once for this vao where as in
         * previous versions it had to happen every time a vao was swapped for another.
         */

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        final String vertex_shader = reader.getFileContent("ShaderCode/first.vert");
        //Job is to set the colour for each fragment
        final String fragment_shader = reader.getFileContent("ShaderCode/first.frag");

        int vs = Shader.CompileShader(GL_VERTEX_SHADER, vertex_shader);

        int fs = Shader.CompileShader(GL_FRAGMENT_SHADER, fragment_shader);

        shader_program = glCreateProgram();
        glAttachShader(shader_program, fs);
        glAttachShader(shader_program, vs);
        glLinkProgram(shader_program);

        int[] params = new int[1];
        glGetProgramiv(shader_program, GL_LINK_STATUS, params);
        if(GL_TRUE != params[0]) {
            GL_LOG.Log_Data("PROGRAM LINKING ERROR: " + glGetProgramInfoLog(shader_program));
        }
    }

    @Override
    public void update()
    {
        // Temp to show scene change
        if (!changingScene && KeyEventListener.isKeyPressed(KeyEvent.VK_SPACE))
        {
            changingScene = true;
        }
        if (changingScene && timeToChangeScene > 0)
        {
            timeToChangeScene -= 0.1f;
        }
        else if (changingScene)
        {
            timeToChangeScene = 2.0f;
            Window.ChangeScene(1);
        }

        glUseProgram(shader_program);
        //Bind the VAO that we're using
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        // Unbind everything
        glBindVertexArray(0);

        glUseProgram(0);


    }


}
