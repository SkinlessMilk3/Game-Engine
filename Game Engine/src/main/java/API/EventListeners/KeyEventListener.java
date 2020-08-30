package API.EventListeners;
//lwjgl needs to be added to project

import java.awt.*;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyEventListener {

    private static KeyEventListener ls;
    private int key;
    private boolean pressed;
    private boolean keyPressed[] = new boolean[350];

    private KeyEventListener(){
        key = 0;
        pressed = false;
    }

    private static KeyEventListener getKeyListener(){

        if(ls == null)
            ls = new KeyEventListener();
        return ls;
    }
    public static void isKeyPressed(long window, int key, int scancode, int action, int mods){

        if(GLFW_PRESS == action){
            getKeyListener().keyPressed[key] = true;
        }
        else if(GLFW_RELEASE == action){
            getKeyListener().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int key){
        return getKeyListener().keyPressed[key];
    }
}
