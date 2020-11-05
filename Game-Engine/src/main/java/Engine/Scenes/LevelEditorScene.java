package Engine.Scenes;

import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventDispatcher;
import Engine.*;
import Renderer.*;
import Utils.AssetPool;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
//import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
//import java.nio.FloatBuffer;
//import java.nio.IntBuffer;

//import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LevelEditorScene extends Scene {

    public boolean changingScene = false;
    public int sceneNum = 0;
    public float timeToChangeScene = 2.0f;

    private float points[]={

            0.0f, 500.5f, 0.0f,
            500.5f, -0.5f, 0.0f,
            -0.5f, -500.5f,0.0f
    };
    private float colours[] = {
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f
    };

    private int vao, vbo_points, vbo_colours; //shader_program;
    Shader shader;
    //Temp
    private GameObject testObj;
    private boolean firstTime = false;


    public LevelEditorScene()
    {
        System.out.println("Inside level editor!");
    }

    @Override
    public void init()
    {
        System.out.println("Creating Test object!");
        this.testObj = new GameObject("test object");
        //this.testObj.addComponent(new SpriteRenderer());

        this.addGameObjectToScene(this.testObj);

        //Initializes camera
        this.camera = new Camera(new Vector2f());

        //GL_Shader_Reader reader = new GL_Shader_Reader();

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

        shader = new Shader("Assets/first.vert");

        loadResources();
    }

    private void loadResources() {
        //AssetPool.getShader("Assets/testing.glsl");
    }


    @Override
    public void update(float dt)
    {
        if (MouseEventDispatcher.isDragging())
        {
            if (MouseEventDispatcher.getDeltaX() != MouseEventDispatcher.getX()) {
                camera.position.x -= MouseEventDispatcher.getDeltaX();
                camera.position.y += MouseEventDispatcher.getDeltaY();
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
            timeToChangeScene -= 0.1f;
        }
        else if (changingScene)
        {
            timeToChangeScene = 2.0f;
            //Window.ChangeScene(sceneNum);
        }

        shader.bind();
        Shader.uploadMat4f("uProjection", camera.getProjectionMatrix(), shader.getId());
        Shader.uploadMat4f("uView", camera.getViewMatrix(), shader.getId());

        //Bind the VAO that we're using
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        // Unbind everything
        glBindVertexArray(0);

        glUseProgram(0);

        for (GameObject go : this.gameObjects)
        {
            go.update(dt);
        }
    }

    @Override
    public void render() {

    }
}