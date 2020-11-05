package API.EventListeners;
//Need to add lwjgl to project

import Engine.Window;
import Utils.GL_LOG;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class MouseEventDispatcher{
    private static MouseEventDispatcher la;
    private boolean isDragging;
    private double scrollX, scrollY, prevScrollX, prevScrollY;
    private double x, y, prevX, prevY;
    private boolean isPressed[] = new boolean[3];
    private static List<MouseEventListener> scrollEventListeners;

    private MouseEventDispatcher(){
        la = null;
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
    public static void addListener(MouseEventListener tmp){
        if(scrollEventListeners == null)
            scrollEventListeners = new ArrayList<>();
        scrollEventListeners.add(tmp);
    }
    public static MouseEventDispatcher getDispatcher(){
        if(la == null){
            la = new MouseEventDispatcher();
        }
        return la;
    }

    public static void isMovedCallback(long window, double xpos, double ypos){

        getDispatcher().prevX = getDispatcher().x;
        getDispatcher().prevY = getDispatcher().y;
        getDispatcher().x = xpos;
        getDispatcher().y = ypos;
        getDispatcher().isDragging = getDispatcher().isPressed[0] || getDispatcher().isPressed[1] || getDispatcher().isPressed[2];
    }

    public static void isPressedCallback(long window, int button, int action, int mods){

        if(GLFW_PRESS == action){
            if(button < getDispatcher().isPressed.length) {
                getDispatcher().isDragging = true;
                getDispatcher().isPressed[button] = true;
            }
        }
        if(GLFW_RELEASE == action){
            if(button < getDispatcher().isPressed.length) {
                getDispatcher().isDragging = false;
                getDispatcher().isPressed[button] = false;
            }
        }
    }

    public static void isScrolledCallback(long window, double xoffset, double yoffset){

        getDispatcher().prevScrollX = getDispatcher().scrollX;
        getDispatcher().prevScrollY = getDispatcher().scrollY;
        getDispatcher().scrollY = xoffset;
        getDispatcher().scrollY = yoffset;

        for(MouseEventListener tmp : scrollEventListeners){
            tmp.onScrolledEvent((float) getDispatcher().scrollX , (float) getDispatcher().scrollY);
        }
    }

    public static void endFrame(){
        getDispatcher().scrollY = 0;
        getDispatcher().scrollX = 0;
        getDispatcher().prevX = 0;
        getDispatcher().prevY = 0;
        getDispatcher().x = getDispatcher().prevX;
        getDispatcher().y = getDispatcher().prevY;
    }

    public static float getX(){
        return (float) getDispatcher().x;
    }

    public static float getY(){
        return (float) getDispatcher().y;
    }

    public static float getScreenX() {
        float currentX = getX();
        currentX = (currentX / (float) Window.getWidth()) * Window.getWidth();

        return currentX;
    }

    public static float getScreenY() {
        float currentY = getY();
        currentY = Window.getHeight() - ((currentY / (float)Window.getHeight()) * Window.getHeight() );


        return currentY;
    }

    public static float getOrthoX() {
        float currentX = getX();
        currentX = (currentX / (float) Window.getWidth()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
        tmp.mul(Window.getScene().camera().getInverseProjection()).mul(Window.getScene().camera().getInverseView());
        currentX = tmp.x;

        return currentX;
    }

    public static float getOrthoY() {
        float currentY = getY();
        currentY = (currentY / (float)Window.getHeight()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);
        tmp.mul(Window.getScene().camera().getInverseProjection()).mul(Window.getScene().camera().getInverseView());
        currentY = tmp.y;

        return currentY;
    }

    public static boolean isDragging(){
        return getDispatcher().isDragging;
    }

    public static boolean isPressed(int button){
        if(button < getDispatcher().isPressed.length){
            return getDispatcher().isPressed[button];
        }
        return false;
    }
    public static float getDeltaX(){ return (float) getDispatcher().x - (float)getDispatcher().prevX; }
    public static float getDeltaY(){ return (float) getDispatcher().y - (float)getDispatcher().prevY; }

}
