package Components;

import API.EventListeners.KeyEventListener;
import Engine.Component;
import Engine.GameObject;

import java.awt.event.KeyEvent;

public class TempCounter extends Component {

    public float x = 0;
    public float y = 0;


    @Override
    public void start() {
        System.out.println("Controller added!");
    }

    @Override
    public void update(float dt) {
        float movingX = (float)Math.floor((Math.random() * 10) + 1);
        float movingY = movingX;

        x += movingX;
        y += movingY;
        if (KeyEventListener.isKeyPressed(KeyEvent.VK_2)){
            System.out.println("x: " + x);
            System.out.println("y: " + y);
        }

    }
}
