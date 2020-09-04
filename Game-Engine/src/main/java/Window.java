import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventListener;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static long wnd;
    private static Window window;
    private int width, height;
    String title;

    private Window(){
        this.title = "Game Engine";
        this.width = 1920;
        this.height = 1080;
    }

    public void run(){

        init();
        loop();

        glfwFreeCallbacks(wnd);
        glfwDestroyWindow(wnd);

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

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        wnd = GLFW.glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        glfwSetKeyCallback(wnd, KeyEventListener::isKeyPressed);
        glfwSetMouseButtonCallback(wnd, MouseEventListener::isPressedCallback);
        glfwSetScrollCallback(wnd, MouseEventListener::isScrolledCallback);
        glfwSetCursorPosCallback(wnd, MouseEventListener::isMovedCallback);

        if(wnd == NULL)
            throw new RuntimeException("Failed to create window");

        glfwMakeContextCurrent(wnd);
        glfwSwapInterval(1);

        glfwShowWindow(wnd);
    }

    private void loop(){
        GL.createCapabilities();

        glClearColor(1.0f, 0.0f, 0.0f,0.0f);

        while(!glfwWindowShouldClose(wnd)){

            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            System.out.println("Mouse is at x: "  + MouseEventListener.getX() + " y: " + MouseEventListener.getY());

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
