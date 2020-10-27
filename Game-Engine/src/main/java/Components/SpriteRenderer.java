package Components;

import Engine.Component;
import Engine.Transform;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Vector2f[] textCoords;
    private Texture texture;

    private Transform lastTransform;

    private boolean isDirty = false;

    //For asset with no texture
    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.texture = null;
    }

    //For asset with texture
    public SpriteRenderer(Texture texture) {
        this.texture = texture;
        //White
        this.color = new Vector4f(1,1,1,1);
    }

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    public Vector4f getColor() {
        return this.color;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTextCoords() {
        Vector2f[] texCoords = {
                new Vector2f(1,1),
                new Vector2f(1,0),
                new Vector2f(0,1),
                new Vector2f(0,0)
        };
        return textCoords;
    }

    public void setColor(Vector4f color)
    {
        if(!this.color.equals(color)) {
            this.isDirty = true;
            this.color.set(color);
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }
}
