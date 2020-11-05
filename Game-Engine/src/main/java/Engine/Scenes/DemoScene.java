package Engine.Scenes;

import Components.SpriteRenderer;
import Engine.*;
import Renderer.Renderer2D;
import Renderer.Texture;
import Utils.GL_LOG;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class DemoScene extends Scene{
    private static Vector3f position = new Vector3f(0.0f, -1.0f, 0.0f);
    private static Vector2f size = new Vector2f(0.25f, 0.25f);
    private static Vector4f color = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
    private static CameraController control = new CameraController((float) Window.getWidth()/(float)Window.getHeight());
    private List<SubTexture2D> stxs;
    private GameObject go;
    private Texture spriteSheet;

    public DemoScene(){
        stxs = new ArrayList<>();

        go = new GameObject("DemoScene GameObject 1");
        spriteSheet = new Texture("Assets/Textures/RPGpack_sheet_2X.png");//("Assets/Textures/RPGpack_sheet_2X.png");
        Vector2f coords = new Vector2f(1.0f, 1.0f);
        Vector2f dimensions = new Vector2f(128.0f, 128.0f);

        float distx = 0.0f;
        float disty = 0.0f;
        for(int y = 0; y < 15; y++){
            for(int x = 0; x < 15; x++) {

                SubTexture2D stx = new SubTexture2D(spriteSheet, coords, dimensions);
                SpriteRenderer spr = new SpriteRenderer(spriteSheet, stx.getTexCoords(), go);
                Transform transform = new Transform(new Vector2f(0.0f + distx, 0.15f+disty), go);
                transform.scale.x = 1.f;
                transform.scale.y = 1.f;

                go.addComponent(spr);
                go.addComponent(transform);

                distx = distx + 0.2f + transform.scale.x;//distx +.15f*go.getComponent(Transform.class).scale.x;
                addGameObjectToScene(go);

                go = new GameObject("DemoScene GameObject" + (x+0.5));//y));
            }
            disty = disty +1.5f ;//+.15f*go.getComponent(Transform.class).scale.y;
            distx = 0.0f;
        }

    }
    @Override
    public void update(float dt) {
        control.onUpdate(dt);

        spriteSheet.bind(0);
        Renderer2D.beginScene(control.getCamera());

        for(int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).getComponent(SpriteRenderer.class).getTexture().bind(0);

        }
        Renderer2D.endScene();

    }
}
