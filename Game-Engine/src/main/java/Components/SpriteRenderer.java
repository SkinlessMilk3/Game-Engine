package Components;
import Engine.Component;
import Engine.GameObject;
import Engine.SubTexture2D;
import Renderer.Texture;
import Engine.Transform;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import Renderer.Texture;

import java.util.Arrays;

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

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;

//    public SpriteRenderer(Vector4f color) {
//        this.color = color;
//        this.sprite = new Sprite(null);
//        this.isDirty = true;
//    }
//
//    public SpriteRenderer(Sprite sprite) {
//        this.sprite = sprite;
//        this.color = new Vector4f(1, 1, 1, 1);
//        this.isDirty = true;
//    }

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void imgui() {


    @Override
    public void update(float dt) {}

    public Vector4f getColor() {
        return this.color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.isDirty = true;
            this.color.set(color);
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public Vector2f[] getTextCoords() {

        return textCoords;
}
    public void setClean() {
        this.isDirty = false;
    }
}