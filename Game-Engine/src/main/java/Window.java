import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventListener;

import org.lwjgl.BufferUtils;
import org.lwjgl.Version;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static long wnd;
    private static Window window = null;
    private static Scene currentScene;
    private int width, height;
    String title;

    //used to calculate frame rate
    static class Frame_Rate{
        private static int frame_count;
        private static double prev_time;

        static void Update_Frame_Rate_Counter() {
            double curr_time = glfwGetTime();
            double elapsed_time = curr_time - prev_time;

            if(elapsed_time > 0.25){
                double frame_rate = frame_count/elapsed_time;
                prev_time = curr_time;
                String str = Double.toString(frame_rate);
                glfwSetWindowTitle(wnd, str);

                frame_count = 0;
            }
            frame_count += 1;
        }
    }

    public static void ChangeScene(int newScene)
    {
        // Temporary switch case
        switch (newScene)
        {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'!";
                break;
        }
    }

    private Window(){
        this.title = "Game Engine";
        this.width = 1360;//Gotta figure out how to get these values from glfw because not every display has the same resolution.
        this.height = 768;
    }

    public void run(){
        System.out.println("Started LWJGL" + Version.getVersion() + "!");

        init();
        loop();

        //Free memory
        glfwFreeCallbacks(wnd);
        glfwDestroyWindow(wnd);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init(){

        //print any glfw errors to a log txt
        glfwSetErrorCallback((errcode, dsc)->{
            GL_LOG.Log_Data(errcode + " " + GLFWErrorCallback.getDescription(dsc));
        });
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Failed to initialize GLFW");

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        //Create the window
        wnd = GLFW.glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if(wnd == NULL)
            throw new RuntimeException("Failed to create window");

        //Lambda Functions
        glfwSetKeyCallback(wnd, KeyEventListener::isKeyPressed);
        glfwSetMouseButtonCallback(wnd, MouseEventListener::isPressedCallback);
        glfwSetScrollCallback(wnd, MouseEventListener::isScrolledCallback);
        glfwSetCursorPosCallback(wnd, MouseEventListener::isMovedCallback);

        //Make openGL context current
        glfwMakeContextCurrent(wnd);
        //Enable v-sync
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(wnd);
    }
    private void loop(){

        GL.createCapabilities();

        //Sets starting scene
        Window.ChangeScene(0);

        while(!glfwWindowShouldClose(wnd)){

            Frame_Rate.Update_Frame_Rate_Counter();

            glfwPollEvents();

            glClearColor(0.8f, 0.8f, 0.8f,1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //Draws/updates current scene
            currentScene.update();

            //System.out.println("Mouse is at x: "  + MouseEventListener.getX() + " y: " + MouseEventListener.getY());

            glfwSwapBuffers(wnd);
            
        }
    }

    public static Window getWindow(){

        if(window == null){
            window = new Window();
        }
        return window;
    }
}