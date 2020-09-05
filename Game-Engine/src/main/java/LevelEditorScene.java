import API.EventListeners.KeyEventListener;
//import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
//import java.nio.FloatBuffer;
//import java.nio.IntBuffer;

//import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LevelEditorScene extends Scene {

    public boolean changingScene = false;
    public float timeToChangeScene = 2.0f;

    private float points[]={

            0.0f, 0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            -0.5f, -0.5f,0.0f
    };

    private int vao, vbo, shader_program;

    public LevelEditorScene()
    {
        System.out.println("Inside level editor!");
    }

    @Override
    public void init()
    {
        //IntBuffer vbo = BufferUtils.createIntBuffer(1);
        //vbo.put(points)
        //IntBuffer vao = BufferUtils.createIntBuffer(1);
        vao = 0; //Vertex Buffer Object
        vbo = 0; //Vertex Array Object
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glBufferData(GL_ARRAY_BUFFER, points, GL_STATIC_DRAW);

        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, NULL);

        final String vertex_shader = "" +
                "#version 410\n" +
                "in vec3 vp;" +
                "void main(){" +
                "   gl_Position = vec4 (vp, 1.0);" +
                "}";
        //Job is to set the colour for each fragment
        final String fragment_shader = "" +
                "#version 410\n" +
                "out vec4 frag_colour;" +
                "void main(){" +
                "   frag_color = vec4 (0.5, 0.0, 0.5, 1.0);" +
                "}";

        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, vertex_shader);
        glCompileShader(vs);

        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, fragment_shader);
        glCompileShader(fs);

        shader_program = glCreateProgram();
        glAttachShader(shader_program, fs);
        glAttachShader(shader_program, vs);
        glLinkProgram(shader_program);

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
