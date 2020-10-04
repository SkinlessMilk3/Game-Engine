package API.EventListeners;
//Need to add lwjgl to project

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class MouseEventDispatcher{
    private static MouseEventDispatcher ls;
    private boolean isDragging;
    private double scrollX, scrollY, prevScrollX, prevScrollY;
    private double x, y, prevX, prevY;
    private boolean isPressed[] = new boolean[3];
    private static List<MouseEventListener> scrollEventListeners;

    public static void addListener(MouseEventListener e){

        if(scrollEventListeners == null){
            scrollEventListeners = new ArrayList<>();
        }
        scrollEventListeners.add(e);
    }

    private MouseEventDispatcher(){
        ls = null;
        isDragging = false;
        scrollX = 0;
        scrollY = 0;
        x = 0;
        y = 0;
        prevX = 0;
        prevY = 0;
        prevScrollX = 0;
        prevScrollY = 0;

    }

    public static MouseEventDispatcher getListener(){
        if(ls == null){
            ls = new MouseEventDispatcher();
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
                getListener().isPressed[button] = false;
            }
        }
    }

    public static void isScrolledCallback(long window, double xoffset, double yoffset){

        getListener().prevScrollX = getListener().scrollX;
        getListener().prevScrollY = getListener().scrollY;
        getListener().scrollY = xoffset;
        getListener().scrollY = yoffset;

        for(MouseEventListener tmp : scrollEventListeners){
            tmp.scrolledEvent((float)getListener().scrollX /*- (float)getListener().prevScrollX*/, (float)getListener().scrollY /*- (float)getListener().prevScrollY*/);
        }
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

    public static double getDeltaScrollX(){ return getListener().scrollX - getListener().prevScrollX; }

    public static double getDeltaScrollY(){ return getListener().scrollY - getListener().prevScrollY; }
    public static boolean isPressed(int button){
        if(button < getListener().isPressed.length){
            return getListener().isPressed[button];
        }
        return false;
    }
}
