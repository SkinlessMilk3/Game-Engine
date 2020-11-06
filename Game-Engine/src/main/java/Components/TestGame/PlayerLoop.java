package Components.TestGame;

import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventDispatcher;
import Components.Component;
import Engine.Window;

import static org.lwjgl.glfw.GLFW.*;

//The main object loop for an object. Describes what the object does every frame.
public class PlayerLoop extends Component {

    public int x = 0;
    private transient int loops = 25;

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        if (KeyEventListener.isKeyPressed(GLFW_KEY_W)) {
            gameObject.transform.position.y += 0.025;
        }

        if (KeyEventListener.isKeyPressed(GLFW_KEY_S)) {
            gameObject.transform.position.y -= 0.025;
        }


        if (loops >= 25) {
            if (KeyEventListener.isKeyPressed(GLFW_KEY_SPACE)) {
                Window.getScene().shoot = true;
                loops = 0;
            }
        }
        if (loops < 25) {
            loops++;
        }
    }
}
