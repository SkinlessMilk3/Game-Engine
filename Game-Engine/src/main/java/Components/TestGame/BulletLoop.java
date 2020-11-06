package Components.TestGame;

import API.EventListeners.KeyEventListener;
import Components.Component;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;

public class BulletLoop extends Component {

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        gameObject.transform.position.x += 0.05;
    }
}
