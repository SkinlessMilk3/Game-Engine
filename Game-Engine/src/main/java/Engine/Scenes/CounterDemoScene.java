package Engine.Scenes;

import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventDispatcher;
import Components.TempCounter;
import Engine.Camera;
import Engine.GameObject;
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



    private int objNum = 0;

    private GameObject counterObj;

    public CounterDemoScene()
    {
        System.out.println("Inside demo counter!");

    }

    @Override
    public void init() {
        //Initializes camera
        this.camera = new Camera(new Vector2f());

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
