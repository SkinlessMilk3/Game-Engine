package Components;

import Engine.Component;
import Engine.GameObject;
import Engine.SubTexture2D;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Vector2f[] textCoords = {
            new Vector2f(0,0),
            new Vector2f(1,0),
            new Vector2f(1,1),
            new Vector2f(0,1)
    };
    private Texture texture;

    //For asset with no texture
    public SpriteRenderer(Vector4f color, GameObject go) {
        this.color = color;
        this.texture = null;
        gameObject = go;
    }

    //For asset with texture
    public SpriteRenderer(Texture texture, GameObject go) {
        this.texture = texture;
        //White
        this.color = new Vector4f(1,1,1,1);
        gameObject = go;
    }

    public SpriteRenderer(Texture texture, Vector2f[] texCoords, GameObject go){
        this.texture = texture;
        textCoords = texCoords;
        gameObject = go;
        color = new Vector4f(1.f, 1.f, 1.f, 1.f);
    }

    @Override
    public void start() {}

    @Override
    public void update(float dt) {}

    public Vector4f getColor() {
        return this.color;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTextCoords() {

        return textCoords;
    }
}
