package Engine.Scenes;

import Components.SpriteRenderer;
import Engine.*;
import Renderer.Renderer2D;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class GameObjectsScene extends Scene{
    List<GameObject> gameObjects;
    CameraController controller;
    Vector3f pos = new Vector3f(0.2f, 0.1f, 1.0f);
    Vector2f size = new Vector2f(1.0f, 1.0f);
    Vector4f color;

    public GameObjectsScene(){
        controller = new CameraController(Window.getAspectRatio());

        Texture tx = new Texture("/Assets/Textures/RPGpack_sheet_2X.png");
        SubTexture2D stx2d = new SubTexture2D(tx, new Vector2f(0.0f, 0.0f), new Vector2f(128.0f, 128.0f));
        gameObjects = new ArrayList<>();
        GameObject obj = new GameObject("obj 1");

        obj.addComponent(new SpriteRenderer(obj, stx2d));
        gameObjects.add(obj);

        obj = new GameObject("obj 2");
        obj.addComponent(new SpriteRenderer(obj, tx));
        gameObjects.add(obj);

        color = gameObjects.get(1).getComponent(SpriteRenderer.class).getColor();
    }
    @Override
    public void update(float dt) {
        controller.onUpdate(dt);
        Renderer2D.beginScene(controller.getCamera());

        Renderer2D.submit(pos, size, color);

        Renderer2D.endScene();
    }
}
