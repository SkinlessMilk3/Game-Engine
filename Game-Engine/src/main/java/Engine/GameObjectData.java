package Engine;

import Components.ObjectLoop;
import Components.SpriteRenderer;
import Components.TempCounter;
import Renderer.Renderer2D;
import Renderer.Texture;
import Utils.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class GameObjectData {

    public String name;
    private Texture sprite;
    //currentShader = AssetPool.getShader("Assets/testing.glsl");
    //private Transform transform;
    private  transient Vector4f color = new Vector4f(0.0f, 0.0f, 1.0f, 1.0f);
    private  transient Vector2f position = new Vector2f(0.0f, 1.0f);
    private  transient Vector2f size = new Vector2f(0.25f, 0.25f);

    public GameObjectData()
    {
        name = "default";

    }

    public void setName(String n)
    {
        this.name = n;
    }

    public GameObject GenerateGameObject()
    {
        GameObject obj = new GameObject(name, new Transform(position, size));
        TempCounter temp = new TempCounter();
        SpriteRenderer spr = new SpriteRenderer(color);

        obj.addComponent(temp);
        obj.addComponent(spr);

        return obj;
    }

    public GameObject GenerateGameObject(Transform pos)
    {
        this.sprite = new Texture("Assets/noTexture.png");
        GameObject obj = new GameObject(name, pos);
        TempCounter temp = new TempCounter();
        SpriteRenderer spr = new SpriteRenderer(sprite);
        ObjectLoop objLoop = new ObjectLoop();

        obj.addComponent(temp);
        obj.addComponent(spr);
        obj.addComponent(objLoop);

        return obj;
    }

    public GameObject GenerateGameObject(Transform pos, Texture texture)
    {
        this.sprite = texture;
        GameObject obj = new GameObject(name, pos);
        TempCounter temp = new TempCounter();
        SpriteRenderer spr = new SpriteRenderer(sprite);
        ObjectLoop objLoop = new ObjectLoop();

        obj.addComponent(temp);
        obj.addComponent(spr);
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
