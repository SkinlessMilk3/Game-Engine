package Engine.Scenes;

import Engine.CameraController;
import Engine.Window;
import Renderer.Renderer2D;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4d;

public class BatchRendererScene {
    private static Vector3d position = new Vector3d().set(0.0f, -1.0f, 0.0f);
    private static Vector2d size = new Vector2d().set(0.25f, 0.25f);
    private static Vector4d color = new Vector4d(1.0f, 0.0f, 0.0f, 1.0f);
    private static CameraController control = new CameraController((float) Window.getWidth()/(float)Window.getHeight());

    public static void onUpdate(float dt){

        control.onUpdate(dt);
        Renderer2D.beginScene(control.getCamera());
        for(int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x+= 1) {
                Renderer2D.submit(position, size, color);
                position.x += 0.26f;
            }
            position.y += 0.26f;
            position.x = 0.0f;
        }
        Renderer2D.endScene();
        position.y = -1.0f;
        position.x = 0.0f;
    }
}
