package Engine;

import Components.ObjectLoop;
import Components.Sprite;
import Components.SpriteRenderer;
import Components.TempCounter;
import Renderer.Renderer2D;
import Renderer.Texture;
import Utils.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class GameObjectData {

    public String name;
    private String spritePath = "";
    //currentShader = AssetPool.getShader("Assets/testing.glsl");
    //private Transform transform;
    private  transient Vector4f color = new Vector4f(0.0f, 0.0f, 1.0f, 1.0f);
    private  transient Vector2f position = new Vector2f(0.0f, 0.0f);
    private  transient Vector2f size = new Vector2f(0.25f, 0.25f);

    public GameObjectData()
    {
        name = "default";

    }

    public void setName(String n)
    {
        this.name = n;
    }

    public void setTexture(String tex) { this.spritePath = tex;}

    public String getSpritePath() { return this.spritePath;}

    public GameObject GenerateGameObject()
    {
        GameObject obj = new GameObject(name, new Transform(position, size));
        TempCounter temp = new TempCounter();
        SpriteRenderer spr = new SpriteRenderer();

        obj.addComponent(temp);
        obj.addComponent(spr);

        return obj;
    }

    public GameObject GenerateGameObject(Transform pos)
    {
        Texture tex = AssetPool.getTexture("Assets/noTexture.png");
        GameObject obj = new GameObject(name, pos);
        TempCounter temp = new TempCounter();
        SpriteRenderer sprRen = new SpriteRenderer();
        Sprite spr = new Sprite();
        spr.setTexture(tex);
        sprRen.setSprite(spr);
        ObjectLoop objLoop = new ObjectLoop();

        obj.addComponent(temp);
        obj.addComponent(sprRen);
        obj.addComponent(objLoop);

        return obj;
    }

    public GameObject GenerateGameObject(Transform pos, Texture texture)
    {
        GameObject obj = new GameObject(name, pos);
        TempCounter temp = new TempCounter();
        Sprite spr = new Sprite();
        SpriteRenderer sprRen = new SpriteRenderer();
        spr.setTexture(texture);
        sprRen.setSprite(spr);
        ObjectLoop objLoop = new ObjectLoop();

        obj.addComponent(temp);
        obj.addComponent(sprRen);
        obj.addComponent(objLoop);

        return obj;
    }

    public void UpdateGameObjects()
    {
        for (GameObject go : Window.getScene().gameObjects)
        {
            if (go.name.equals(this.name))
            {
                updateTestVal(go);
            }
        }
    }

    private void updateTestVal(GameObject go)
    {
        if (go.getComponent(TempCounter.class) != null)
        {
            go.getComponent(TempCounter.class).x += 5;
        }
    }

}
