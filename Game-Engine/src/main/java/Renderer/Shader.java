package Renderer;

import static org.lwjgl.opengl.GL20.*;
import Utils.GL_LOG;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

/*
* Does things like compiles shaders and checks for errors
* updated to contain programs and compile them using shaders.
*
* Things to know:
*   This now takes only one file that contains shaders. ONLY vertex and fragment shaders.
*   You must define your shaders in your shader files using #shader <shaderType>
    i.e. #shader vertex. This must be at the top of the file with one shader and at the top
    * of the second shader. It does not matter what order the shaders are defined. You can
    * define your fragment shader first or your vertex shader.
    *
* Use of this class will let you do texture and program bindings, compiling, and linking just by
* calling the constructor.
* It has the functions bind, unbind, and set uniform functions the user will find of use.
* This class should define any opengl uniform functionality. This way we don't repeat code.
 */
public class Shader {
    //Used for parsing
    private enum ShaderType{
        NONE(-1, "NONE"),
        Vertex(0, "vertex"),
        Fragment(1, "fragment");

        private int typeVal;
        private String type;
        ShaderType(int num, String type){
            typeVal = num;
            this.type = type;
        }
        public int getTypeVal(){
            return typeVal;
        }
        public String toString(){
            return type;
        }
    }
    //shader code gets stored here
    private String[] shaders;
    //this is ultimately the programid we receive from opengl when glCreateProgram() is called
    private int renderid;
    String filepath;

    public Shader(String filename){

        filepath = filename;
        shaders = new String[2];

        parseShader();
        compileShader();
    }

    public int getId(){
        return renderid;
    }

    public void bind(){
        glUseProgram(renderid);
    }

    public void unbind(){
        glUseProgram(0);
    }
    public void delete(){ glDeleteShader(renderid); }
    //find the uniform and give it 4 values of type float.
    public void setUniform4f(String u_name, float v1, float v2, float v3, float v4){
        glUniform4f(glGetUniformLocation(renderid, u_name), v1, v2, v3, v4);
    }

    //Find the uniform and give it a value of type float.
    public void setUniform1i(String u_name, int value){
        int location = glGetUniformLocation(this.renderid, u_name);
        if(location == -1)
            GL_LOG.Log_Data("Uniform not found: " + u_name);

        glUniform1i(location, value);
    }
    public void setUniformMat4(String u_name, Matrix4f matrix){
        int location = glGetUniformLocation(this.renderid, u_name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);

        if(location == -1)
            GL_LOG.Log_Data("Uniform not found: " + u_name);
        glUniformMatrix4fv(location, false, buffer);
    }

    //A helper function for compiling opengl shaders as well as error checking.
    //Will call compileProgram for program compilation.
    public void compileShader(){

        int v_shader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(v_shader ,shaders[0]);
        glCompileShader(v_shader);

        int[] tmp = new int[1];
        glGetShaderiv(v_shader,GL_COMPILE_STATUS, tmp);

        if(GL_TRUE != tmp[0]){
            GL_LOG.Log_Data("SHADER COMPILE ERROR " + filepath+":" + glGetShaderInfoLog(v_shader));
            return;
        }

        int f_shader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(f_shader, shaders[1]);
        glCompileShader(f_shader);


        glGetShaderiv(f_shader,GL_COMPILE_STATUS, tmp);

        if(GL_TRUE != tmp[0]){
            GL_LOG.Log_Data("SHADER COMPILE ERROR " + filepath+":" + glGetShaderInfoLog(f_shader));
            return;
        }


        compileProgram(v_shader, f_shader);
    }

    //Helper function for compiling and error checking of an opengl program.
    private void compileProgram(int v_shader, int f_shader){

        int program = glCreateProgram();
        glAttachShader(program, v_shader);
        glAttachShader(program, f_shader);
        glLinkProgram(program);

        int success = glGetProgrami(program, GL_LINK_STATUS);
        if(success == GL_FALSE)
            GL_LOG.Log_Data("Failed to link");

        renderid = program;
    }

    private void parseShader(){
        ShaderType type = ShaderType.NONE;

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            GL_LOG.Log_Data("Failed to create a reader for a shader file");
        }

        if(in != null){
            String tmp = "";

            try {
                shaders[0] = "";
                shaders[1] = "";

                while ((tmp = in.readLine()) != null) {
                    if(tmp.contains("#shader")){
                        if(tmp.contains("vertex")){
                            type = ShaderType.Vertex;
                        }
                        else if(tmp.contains("fragment")){
                            type = ShaderType.Fragment;
                        }
                    }
                    else{

                        shaders[type.getTypeVal()] += tmp + "\n";
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to close shader file");
        }
    }

    public static void uploadMat4f(String varName, Matrix4f mat4, int shaderProgram) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadIntArray(String varName, int[] array)
    {
        int varLocation = glGetUniformLocation(renderid, varName);
        bind();
        glUniform1iv(varLocation, array);
    }
}
