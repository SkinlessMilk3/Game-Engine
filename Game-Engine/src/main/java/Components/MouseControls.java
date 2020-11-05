package Components;

import API.EventListeners.MouseEventDispatcher;
import Engine.GameObject;
import Engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class MouseControls extends Component{
    GameObject holdingObject = null;

    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        System.out.println("x when grabbed: " + holdingObject.transform.position.x);
        System.out.println("y when grabbed: " + holdingObject.transform.position.y);
    }

    public void place() {
        this.holdingObject = null;
    }

    @Override
    public void update(float dt) {
        if (holdingObject != null) {
            holdingObject.transform.position.x = MouseEventDispatcher.getOrthoX();
            holdingObject.transform.position.y = MouseEventDispatcher.getOrthoY();
            System.out.println(holdingObject.transform.position.x);
            System.out.println(holdingObject.transform.position.y);
            System.out.println(MouseEventDispatcher.getOrthoX());
            System.out.println(MouseEventDispatcher.getOrthoY());
            if (MouseEventDispatcher.isPressed(GLFW_MOUSE_BUTTON_RIGHT)) {
                //System.out.println("placed");
                place();
            }
        }
    }
}
