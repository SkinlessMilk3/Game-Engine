import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public class Main {

    public static void main(String[] args){

        GL_LOG log = new GL_LOG();
        log.Restart_Log();

        Window.getWindow().run();
    }
}
