package Engine.Scenes;

import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventListener;
import Engine.Camera;
import Engine.GL_LOG;
import Engine.Scene;
import Engine.Window;
import Renderer.*;
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

public class LevelScene extends Scene {

    public boolean changingScene = false;
    public int sceneNum = 1;
    public float timeToChangeScene = 2.0f;

    private Shader shader;
    private VAO vao;
    private VBO vbo;
    private IBO ibo;

    private float[] vertexArray = {
            // position               // color
            300.5f, 500.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            500.5f,  100.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            300.5f,  100.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            500.5f, 500.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
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

    public void uploadMat4f(String varName, Matrix4f mat4, int shaderProgram) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public LevelScene()
    {
        System.out.println("Inside level!");

    }

    @Override
    public void init()
    {

        //Initializes camera
        this.camera = new Camera(new Vector2f());

        //load and compile vertex shader

        shader = new Shader("Assets/first.vert");
        //Generate VAO, VBO, EBO buffer objects and send to GPU
        vao = new VAO();
        //Create VBO upload the vertex buffer
        vbo = new VBO(GL_ARRAY_BUFFER, vertexArray);
        //Create the indices and upload
        ibo = new IBO(elementArray);

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
            sceneNum++;
            changingScene = true;
        }
        if (changingScene && timeToChangeScene > 0)
        {
            timeToChangeScene -= 0.2f;
        }
        else if (changingScene)
        {
            timeToChangeScene = 2.0f;
            //Window.ChangeScene(sceneNum);
        }



        glUseProgram(shader.getId());

        uploadMat4f("uProjection", camera.getProjectionMatrix(), shader.getId());
        uploadMat4f("uView", camera.getViewMatrix(), shader.getId());

        //Bind the VAO that we're using
        vao.bind();
        //Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, ibo.getCount(), GL_UNSIGNED_INT, 0);
    }

}
