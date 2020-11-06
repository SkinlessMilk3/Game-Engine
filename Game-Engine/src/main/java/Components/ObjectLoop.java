package Components;

import API.EventListeners.KeyEventListener;
import API.EventListeners.MouseEventDispatcher;
import Components.TestGame.BulletLoop;
import Engine.GameObject;
import Engine.Transform;
import Engine.Window;
import Renderer.Texture;
import Utils.AssetPool;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

//The main object loop for an object. Describes what the object does every frame.
public class ObjectLoop extends Component {

    public transient int x = 0;


    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {


    }
}
