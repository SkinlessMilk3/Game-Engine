package Engine.Scenes;

import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventListener;
import Components.TempCounter;
import Engine.Camera;
import Engine.GameObject;
import Engine.Scene;
import Engine.Window;
import Renderer.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
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

public class CounterDemoScene extends Scene {

    public boolean changingScene = false;
    public int sceneNum = 2;
    public float timeToChangeScene = 2.0f;

    //private GL_Shader_Reader shader_reader = new GL_Shader_Reader();
    //private String //vertexShaderScr = new Shader("Assets/first.vert").CompileShader(GL_VERTEX_SHADER);

    //private String fragmentShaderSrc = shader_reader.getFileContent("Assets/first.frag");

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            // position               // color
            100.5f, 0.5f, 0.0f,       0.50f, 0.0f, 1.0f, 1.0f, // Bottom right 0
            0.5f,  100.5f, 0.0f,       0.50f, 0.0f, 1.0f, 1.0f, // Top left     1
            100.5f,  100.5f, 0.0f ,      0.50f, 0.0f, 1.0f, 1.0f, // Top right    2
            0.5f, 0.5f, 0.0f,              0.50f, 0.0f, 1.0f, 1.0f, // Bottom left  3
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

    private int objNum = 0;

    private GameObject counterObj;

    public void uploadMat4f(String varName, Matrix4f mat4, int shaderProgram) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public CounterDemoScene()
    {
        System.out.println("Inside demo controller!");

    }

    @Override
    public void init()
    {
        //Initializes camera
        this.camera = new Camera(new Vector2f());

        //Compile and link shaders

        //load and compile vertex shader
        vertexID = new Shader("Assets/first.vert").getId();

        //load and compile fragment shader
        fragmentID = new Shader("Assets/first.frag").getId();

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
    public void update(float dt)
    {
        if (MouseEventListener.isDragging())
        {
            if (MouseEventListener.getDeltaX() != MouseEventListener.getX()) {
                camera.position.x -= MouseEventListener.getDeltaX();
                camera.position.y += MouseEventListener.getDeltaY();
            }
        }

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
            //Window.ChangeScene(0);
        }

        glUseProgram(shaderProgram);

        uploadMat4f("uProjection", camera.getProjectionMatrix(), shaderProgram);
        uploadMat4f("uView", camera.getViewMatrix(), shaderProgram);

        //Bind the VAO that we're using
        glBindVertexArray(vaoID);

        //Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);

        if (KeyEventListener.isKeyPressed(KeyEvent.VK_1) && this.objNum < 1) {
            //System.out.println("Creating mover object " + objNum + "!");
            this.objNum++;
            this.counterObj = new GameObject("Counter  object created!");
            //System.out.println("Obj: " + objNum + ": ");
            this.counterObj.addComponent(new TempCounter());
            this.addGameObjectToScene(this.counterObj);
        }

        for (GameObject go : this.gameObjects)
        {
            go.update(dt);
        }
    }

}
