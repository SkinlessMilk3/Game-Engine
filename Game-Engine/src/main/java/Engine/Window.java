package Engine;

import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventDispatcher;

import API.EventListeners.WindowResizeDispatcher;
import Engine.Scenes.*;

import Renderer.PickingTexture;
import Renderer.Renderer2D;
import Renderer.Shader;
import Utils.AssetPool;
import Utils.GL_LOG;

import org.joml.Vector4f;
import org.lwjgl.Version;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.CallbackI;

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
    private PickingTexture pickingTexture;

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

        //glfwSetMouseButtonCallback(wnd, MouseEventDispatcher::isPressedCallback);
        glfwSetScrollCallback(wnd, MouseEventDispatcher::isScrolledCallback);
        glfwSetCursorPosCallback(wnd, MouseEventDispatcher::isMovedCallback);
        glfwSetWindowSizeCallback(wnd, WindowResizeDispatcher::WindowReizeCallback);

        //glfwSetMouseButtonCallback(wnd, MouseEventDispatcher::isPressedCallback);
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
        Renderer2D.Init();

        this.pickingTexture = new PickingTexture(getWidth(), getHeight());

        this.imGuiLayer = new ImGuiLayer(wnd, pickingTexture);
        this.imGuiLayer.initImGui();

        Window.ChangeScene(3);


    }

    private void loop() {

        //currentScene.load();

      float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("Assets/testing.glsl");
        Shader pickingShader = AssetPool.getShader("Assets/pickingShader.glsl");

        GL_LOG.Log_Data("Run loop" + glGetError());

        /*Note for the future. Draw calls need to happen after the glClear function in the loop
         * or nothing will be drawn.
         */
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //currentScene = new LevelEditorScene();
        Vector4f clearColor = new Vector4f(0.15f, 0.15f, 0.15f, 1.0f);


        while (!glfwWindowShouldClose(wnd)) {

            Frame_Rate.Update_Frame_Rate_Counter();

            glfwPollEvents();

            //Render pass 1. Render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0,0, getWidth(), getHeight());
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer2D.bindShader(pickingShader);
            currentScene.update(dt);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            //Render pass 2. Render to actual game*/

            Renderer2D.Clear(clearColor);
            Renderer2D.bindShader(defaultShader);

            //Draws/updates current scene
            if (dt >= 0) {
                Renderer2D.bindShader(defaultShader);
                currentScene.update(dt);
                currentScene.render();
            }

            //System.out.println("Mouse is at x: "  + MouseEventDispatcher.getX() + " y: " + MouseEventDispatcher.getY());

            this.imGuiLayer.update(dt, currentScene);

            if (saving)
            {
                currentScene.saveExit();
                saving = false;
            }


            glfwSwapBuffers(wnd);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        Renderer2D.shutdown();
        System.exit(0);

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
            case 3:
                currentScene = new ImguiTestScene();
                Renderer2D.Init();
                currentScene.init();
                currentScene.load();
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

    public static ImGuiLayer getImGuiLayer() {
        return getWindow().imGuiLayer;
    }

    public static PickingTexture getPickingTexture() { return  getWindow().pickingTexture; }
}