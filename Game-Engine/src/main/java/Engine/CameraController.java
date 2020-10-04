package Engine;

import API.EventListeners.MouseEventDispatcher;
import API.EventListeners.MouseEventListener;
import API.EventListeners.WindowResizeDispatcher;
import API.EventListeners.WindowResizeListener;
import org.joml.Vector2f;


public class CameraController implements MouseEventListener, WindowResizeListener {

    private float zoomLevel;
    private float aspectratio;
    private Camera camera;

    public CameraController(float aspectratio){

        this.aspectratio = aspectratio;
        zoomLevel = 1.0f;

        camera = new Camera(new Vector2f().set(-zoomLevel, zoomLevel));
        camera.setProjectionMatrix(-aspectratio * zoomLevel, aspectratio * zoomLevel, -zoomLevel, zoomLevel);

        MouseEventDispatcher.addListener(this);
        WindowResizeDispatcher.addListener(this);
    }
    public void onUpdate(float dt){

        if (MouseEventDispatcher.isDragging())
        {
            if (MouseEventDispatcher.getDeltaX() != MouseEventDispatcher.getX()) {
                camera.position.x -= MouseEventDispatcher.getDeltaX() * dt;
                camera.position.y += MouseEventDispatcher.getDeltaY() * dt;
            }
        }
    }
    public void scrolledEvent(float xoffset, float yoffset){

          if(zoomLevel > 1.0f)
            zoomLevel -= yoffset;
          else if(yoffset < 0)
              zoomLevel -= yoffset;

        camera.setProjectionMatrix(-aspectratio * zoomLevel, aspectratio * zoomLevel, -zoomLevel, zoomLevel);
    }
    @Override
    public void onWindowResizeEvent(int width, int height) {
        aspectratio = (float) width / (float) height;

        camera.setProjectionMatrix(-aspectratio * zoomLevel, aspectratio * zoomLevel, -zoomLevel, zoomLevel);
    }
    public Camera getCamera(){
        return camera;
    }
}
