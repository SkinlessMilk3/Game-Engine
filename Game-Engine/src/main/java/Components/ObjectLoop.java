package Components;

import API.EventListeners.MouseEventDispatcher;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

//The main object loop for an object. Describes what the object does every frame.
public class ObjectLoop extends Component {

    public int x = 0;

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

        /*
        if (MouseEventDispatcher.isPressed(GLFW_MOUSE_BUTTON_LEFT)) {
            gameObject.transform.position.x -= 1;
            System.out.println(gameObject.transform.position.x);
        }
        if (MouseEventDispatcher.isPressed(GLFW_MOUSE_BUTTON_RIGHT)) {
            gameObject.transform.position.x += 1;
            System.out.println(gameObject.transform.position.x);
        }
        */
    }
}
