package API.EventListeners;
//Need to add lwjgl to project

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseEventListener{
    private static MouseEventListener ls;
    private boolean isDragging;
    private double scrollX, scrollY;
    private double x, y, prevX, prevY;
    private boolean isPressed[] = new boolean[3];

    private MouseEventListener(){
        this.ls = null;
        this.isDragging = false;
        this.scrollX = 0;
        this.scrollY = 0;
        this.x = 0;
        this.y = 0;
        this.prevX = 0;
        this.prevY = 0;

    }

    public static MouseEventListener getListener(){
        if(ls == null){
            ls = new MouseEventListener();
        }
        return ls;
    }

    public static void isMovedCallback(long window, double xpos, double ypos){

        getListener().prevX = getListener().x;
        getListener().prevY = getListener().y;
        getListener().x = xpos;
        getListener().y = ypos;
        getListener().isDragging = getListener().isPressed[0] || getListener().isPressed[1] || getListener().isPressed[2];
    }

    public static void isPressedCallback(long window, int button, int action, int mods){

        if(GLFW_PRESS == action){
            if(button < getListener().isPressed.length) {
                getListener().isDragging = true;
                getListener().isPressed[button] = true;
            }
        }
        else if(GLFW_RELEASE == action){
            if(button < getListener().isPressed.length) {
                getListener().isDragging = false;
                getListener().isPressed[button] = true;
            }
        }
    }

    public static void isScrolledCallback(long window, double xoffset, double yoffset){

        getListener().scrollY = xoffset;
        getListener().scrollY = yoffset;
    }

    public static void endFrame(){
        getListener().scrollY = 0;
        getListener().scrollX = 0;
        getListener().prevX = 0;
        getListener().prevY = 0;
        getListener().x = getListener().prevX;
        getListener().y = getListener().prevY;
    }

    public static float getX(){
        return (float)getListener().x;
    }

    public static float getY(){
        return (float)getListener().y;
    }

    public static float getDeltaY(){
        return (float)(getListener().prevY - getListener().y);
    }

    public static float getDeltaX(){
        return (float)(getListener().prevX - getListener().x);
    }

    public static float getScrollY(){
        return (float)getListener().scrollY;
    }

    public static float getScrollX(){
        return (float)getListener().scrollX;
    }

    public static boolean isDragging(){
        return getListener().isDragging;
    }

    public static boolean isPressed(int button){
        if(button < getListener().isPressed.length){
            return getListener().isPressed[button];
        }
        return false;
    }
}
