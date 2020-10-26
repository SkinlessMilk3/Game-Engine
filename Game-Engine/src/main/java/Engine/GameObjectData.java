package Engine;

import Components.TempCounter;
import Renderer.Texture;

public class GameObjectData {

    public String name;
    private Texture sprite;
    //private Transform transform;

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
        GameObject obj = new GameObject(name);
        TempCounter temp = new TempCounter();

        obj.addComponent(temp);

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
