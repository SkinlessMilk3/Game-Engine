package Engine;

import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventDispatcher;

import API.EventListeners.WindowResizeDispatcher;
import Engine.Scenes.*;

import Renderer.Renderer2D;
import Utils.GL_LOG;

import org.joml.Vector4f;
import org.lwjgl.Version;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static long wnd;
    private static Window window = null;
    private static Scene currentScene;
    private static int width, height;
    private static boolean saving;
    String title;
    private ImGuiLayer imGuiLayer;

    private static ImguiTestScene editor = new ImguiTestScene();

    //used to calculate frame rate
    static class Frame_Rate {
        private static int frame_count;
        private static double prev_time;

        static void Update_Frame_Rate_Counter() {
            double curr_time = glfwGetTime();
            double elapsed_time = curr_time - prev_time;

            if (elapsed_time > 0.25) {
                double frame_rate = frame_count / elapsed_time;
                prev_time = curr_time;
                String str = Double.toString(frame_rate);
                glfwSetWindowTitle(wnd, str);

                frame_count = 0;
            }
            frame_count += 1;
        }
    }


    private Window() {
        this.title = "Game Engine";
        this.width = 1360;//Gotta figure out how to get these values from glfw because not every display has the same resolution.
        this.height = 768;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static float getAspectRatio(){ return (float)width/(float)height; }

    public void run() {
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

    private void init() {

        saving = false;

        //print any glfw errors to a log txt
        glfwSetErrorCallback((errcode, dsc) -> {
            GL_LOG.Log_Data(errcode + " " + GLFWErrorCallback.getDescription(dsc));
        });


        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Failed to initialize GLFW");

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        //Create the window
        wnd = GLFW.glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (wnd == NULL)
            throw new RuntimeException("Failed to create window");

        //Lambda Functions
        glfwSetKeyCallback(wnd, KeyEventListener::isKeyPressed);

        glfwSetMouseButtonCallback(wnd, MouseEventDispatcher::isPressedCallback);
        glfwSetScrollCallback(wnd, MouseEventDispatcher::isScrolledCallback);
        glfwSetCursorPosCallback(wnd, MouseEventDispatcher::isMovedCallback);
        glfwSetWindowSizeCallback(wnd, WindowResizeDispatcher::WindowReizeCallback);

        glfwSetMouseButtonCallback(wnd, MouseEventDispatcher::isPressedCallback);
        glfwSetScrollCallback(wnd, MouseEventDispatcher::isScrolledCallback);
        glfwSetCursorPosCallback(wnd, MouseEventDispatcher::isMovedCallback);
        glfwSetWindowSizeCallback(wnd, WindowResizeDispatcher::WindowReizeCallback);

        //Make openGL context current
        glfwMakeContextCurrent(wnd);

        //Useful error function if it can be figured out.
        /*glEnable(GL_DEBUG_OUTPUT);
        glDebugMessageCallback((int src, int type, int id, int severity, int length, long message, long userParam)->{
            GL_LOG.Log_Data(severity + " " + getMessage(length, message));
        }, 0);
*/
        //Enable v-sync
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(wnd);

        GL.createCapabilities();

        //Sets starting scene

        Window.ChangeScene(3);

        this.imGuiLayer = new ImGuiLayer(wnd);
        this.imGuiLayer.initImGui();

        currentScene.load();

        Renderer2D.Init();
    }

    private void loop() {

        //currentScene.load();

      float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        GL_LOG.Log_Data("Run loop" + glGetError());

        /*Note for the future. Draw calls need to happen after the glClear function in the loop
         * or nothing will be drawn.
         */
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //currentScene = new LevelEditorScene();
        Vector4f clearColor = new Vector4f(0.0f, 1.0f, 0.8f, 1.0f);


        while (!glfwWindowShouldClose(wnd)) {

            Renderer2D.Clear(clearColor);

            Frame_Rate.Update_Frame_Rate_Counter();

            glfwPollEvents();

            //Draws/updates current scene
            if (dt >= 0) {
                currentScene.update(dt);
            }

            //System.out.println("Mouse is at x: "  + MouseEventDispatcher.getX() + " y: " + MouseEventDispatcher.getY());

            this.imGuiLayer.update(dt, currentScene);


            glfwSwapBuffers(wnd);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        if (saving)
        {
            currentScene.saveExit();
            saving = false;
        }

        Renderer2D.shutdown();
    }

    public static Window getWindow() {

        if (window == null) {
            window = new Window();
        }
        return window;
    }

    public static void ChangeScene(int newScene) {
        // Temporary switch cases for testing
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            case 2:
                currentScene = new CounterDemoScene();
                currentScene.init();
                currentScene.start();
                break;
            case 3:
                currentScene = editor;
                currentScene.init();
                currentScene.start();
                break;

            default:
                assert false : "Unknown scene '" + newScene + "'!";
                break;
        }

    }

    public static Scene getScene()
    {
        return getWindow().currentScene;
    }

    public static void setWidth(int newWidth) {
        width = newWidth;
    }

    public static void setHeight(int newHeight) {
        height = newHeight;
    }

    public static void setSaving() {
        saving = true;
    }
}