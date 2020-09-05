import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventListener;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static long wnd;
    private static Window window;
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

    private Window(){
        this.title = "Game Engine";
        this.width = 1360;//Gotta figure out how to get these values from glfw because not every display has the same resolution.
        this.height = 768;
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

        GL_Shader_Reader reader = new GL_Shader_Reader();

        GL.createCapabilities();

        glClearColor(0.8f, 0.8f, 0.8f,0.0f);

        float points[]={
                0.0f, 0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                -0.5f, -0.5f,0.0f
        };

        //IntBuffer vbo = BufferUtils.createIntBuffer(1);
        //vbo.put(points)
        //IntBuffer vao = BufferUtils.createIntBuffer(1);
        int vao = 0;//Vertex Buffer Object
        int vbo = 0;//Vertex Array Object
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glBufferData(GL_ARRAY_BUFFER, points, GL_STATIC_DRAW);

        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, NULL);

        final String vertex_shader = reader.getFileContent("first.vert");
        //Job is to set the colour for each fragment
        final String fragment_shader = reader.getFileContent("first.frag");

        int vs = Shader.CompileShader(GL_VERTEX_SHADER, vertex_shader);

        int fs = Shader.CompileShader(GL_FRAGMENT_SHADER, fragment_shader);

        int shader_program = glCreateProgram();
        glAttachShader(shader_program, fs);
        glAttachShader(shader_program, vs);
        glLinkProgram(shader_program);

        int[] params = new int[1];
        glGetProgramiv(shader_program, GL_LINK_STATUS, params);
        if(GL_TRUE != params[0]){
            GL_LOG.Log_Data("PROGRAM LINKING ERROR: "+glGetProgramInfoLog(shader_program));
        }

        while(!glfwWindowShouldClose(wnd)){

            Frame_Rate.Update_Frame_Rate_Counter();
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glUseProgram(shader_program);
            glBindVertexArray(vao);
            glDrawArrays(GL_TRIANGLES, 0, 3);

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
