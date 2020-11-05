package Engine;

import org.joml.Vector2f;

public class Transform extends Component{

    public Vector2f position;
    public Vector2f scale;

    public Transform(GameObject go)
    {
        init(new Vector2f(), new Vector2f());
        gameObject = go;
    }

    public Transform(Vector2f position, GameObject go)
    {
        init(position, new Vector2f());
        gameObject = go;
    }

    public Transform(Vector2f position, Vector2f scale, GameObject go)
    {
        init(position, scale);
        gameObject = go;
    }

    public void init(Vector2f position, Vector2f scale) {
        System.out.println("x: " + position.x + " y: " + position.y);
        this.position = new Vector2f(position.x, position.y);
        this.scale = new Vector2f(0.25f, 0.25f);
    }
}
