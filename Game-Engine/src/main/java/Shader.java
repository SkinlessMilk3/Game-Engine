import static org.lwjgl.opengl.GL20.*;

/*
* Does things like compiles shaders and checks for errors
 */
public class Shader {
    public Shader(){}
    public static int CompileShader(int shaderType, String shaderCode){

        int shader = glCreateShader(shaderType);
        glShaderSource(shader, shaderCode);
        glCompileShader(shader);

        int[] tmp = new int[1];
        glGetShaderiv(shader,GL_COMPILE_STATUS, tmp);

        if(GL_TRUE != tmp[0]){
            GL_LOG.Log_Data("SHADER COMPILE ERROR: "+glGetShaderInfoLog(shader));
            return -1;
        }
        return shader;
    }
}
