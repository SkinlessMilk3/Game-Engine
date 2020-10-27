package Components;

import API.EventListeners.MouseEventDispatcher;
import Engine.Component;

public class ObjectLoop extends Component {

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        if (MouseEventDispatcher.isPressed(0))
        {
            gameObject.transform.position.x -= 0.10f;
        }
        if (MouseEventDispatcher.isPressed(1))
        {
            gameObject.transform.position.x += 0.10f;
        }
        System.out.println("x: " + gameObject.transform.position.x);
    }
}
