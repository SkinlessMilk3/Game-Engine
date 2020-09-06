import API.EventListeners.KeyEventListener;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelScene extends Scene{

    public boolean changingScene = false;
    public float timeToChangeScene = 2.0f;
    private GL_Shader_Reader shader_reader = new GL_Shader_Reader();
    private String vertexShaderScr = shader_reader.getFileContent("ShaderCode/Square.vert");

    private String fragmentShaderSrc = shader_reader.getFileContent("ShaderCode/Square.frag");

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            //Making Square
            //positions             //colors
            0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, //Bottom Right
            -0.5f, 0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, //Top left
            0.5f, 0.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f, //Top Right
            -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f, //Bottom Left
    };

    //ALWAYS counter-clockwise
    private int[] elementArray = {
            /*
                x         x


                x         x
             */

            2,1,0, //Top right triangle
            0,1,3  //Bottom right triangle
    };

    private int vaoID, vboID, eboID;

    public LevelScene()
    {
        System.out.println("Inside level!");

    }

    @Override
    public void init()
    {
        //Compile and link shaders

        //load and compile vertex shader
        vertexID = Shader.CompileShader(GL_VERTEX_SHADER, vertexShaderScr);

        //load and compile fragment shader
        fragmentID = Shader.CompileShader(GL_FRAGMENT_SHADER, fragmentShaderSrc);

        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        //Check linking status
        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("Error: 'defaultShader.glsl'\nLinking shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        //Generate VAO, VBO, EBO buffer objects and send to GPU

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer of verticies
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
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
            timeToChangeScene -= 0.2f;
        }
        else if (changingScene)
        {
            timeToChangeScene = 2.0f;
            Window.ChangeScene(0);
        }

        glUseProgram(shaderProgram);
        //Bind the VAO that we're using
        glBindVertexArray(vaoID);

        //Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }

}
