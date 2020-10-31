package Components;

import API.EventListeners.MouseEventDispatcher;

import static org.lwjgl.glfw.GLFW.*;

public class TempCounter extends Component {

    public float x = 0;
    public float y = 0;


    @Override
    public void start() {
        System.out.println("Object added!");
    }

    @Override
    public void update(float dt) {

        if (MouseEventDispatcher.isPressed(GLFW_MOUSE_BUTTON_2))
        {
            x += 1;
        }
        if (MouseEventDispatcher.isPressed(GLFW_MOUSE_BUTTON_1))
        {
            x -= 1;
        }
        //System.out.println("object x: " + x);
    }
}
