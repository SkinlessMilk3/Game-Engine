package Renderer;

import static org.lwjgl.opengl.GL20.*;
import Engine.GL_LOG;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
* Does things like compiles shaders and checks for errors
* update to contain programs too.
 */
public class Shader {
    private String shaderCode;
    private int renderid;
    String filepath;

    public Shader(int shaderType, String filename){

        filepath = filename;

        FileReader in = null;
        char[] buffer = new char[2048];

        try {
            in = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("failed to open file to read");
        }

        if(in != null){

            try {
                in.read(buffer, 0, 2048);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to close shader file");
        }
        shaderCode =  new String(buffer);
        renderid = CompileShader(shaderType);
    }

    private int CompileShader(int shaderType){

        int shader = glCreateShader(shaderType);
        glShaderSource(shader, shaderCode);
        glCompileShader(shader);

        int[] tmp = new int[1];
        glGetShaderiv(shader,GL_COMPILE_STATUS, tmp);

        if(GL_TRUE != tmp[0]){
            GL_LOG.Log_Data("SHADER COMPILE ERROR " + filepath+":" + glGetShaderInfoLog(shader));
            return -1;
        }
        return shader;
    }

    public int getId(){
        return renderid;
    }

    //find the uniform and give it 4 values of type float.
    public void setGLUniform4f(int program, String u_name, float v1, float v2, float v3, float v4){
        glUniform4f(glGetUniformLocation(program, u_name), v1, v2, v3, v4);
    }

    //Find the uniform and give it a value of type float.
    public void setGLUniform1i(int program, String u_name, int value){
        int location = glGetUniformLocation(program, u_name);
        if(location == -1)
            GL_LOG.Log_Data("Uniform not found: " + u_name);
        else
            glUniform1i(location, value);
    }
}
