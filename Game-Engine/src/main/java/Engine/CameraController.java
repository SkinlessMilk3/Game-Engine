package Engine;

import API.EventListeners.MouseEventDispatcher;
import API.EventListeners.MouseEventListener;
import API.EventListeners.WindowResizeDispatcher;
import API.EventListeners.WindowResizeListener;
import org.joml.Vector2f;

/**
 * This class controls camera movement. This is how the camera zooms in and out as well as move
 * move in every direction.
 */
public class CameraController implements MouseEventListener, WindowResizeListener {

    private float zoomLevel;
    private float aspectratio;
    private float speed;
    private Camera camera;

    /**
     * Sets up event listening and creates a new camera. It also sets a camera movement speed.
     * @param aspectratio - The aspect ratio of the screen (width/height)
     */
    public CameraController(float aspectratio){

        this.aspectratio = aspectratio;
        zoomLevel = 2.0f;
        speed = 0.25f;
        camera = new Camera(new Vector2f().set(-zoomLevel, zoomLevel));
        camera.setProjectionMatrix(-aspectratio * zoomLevel, aspectratio * zoomLevel, -zoomLevel, zoomLevel);

        MouseEventDispatcher.addListener(this);
        WindowResizeDispatcher.addListener(this);
    }

    /**
     * Every iteration of the loop this checks for camera dragging and responds with movin the
     * camera.
     * @param dt - Delta time
     */
    public void onUpdate(float dt){

        if (MouseEventDispatcher.isDragging())
        {
            if (MouseEventDispatcher.getDeltaX() != 0 && MouseEventDispatcher.getDeltaY() != 0){
                camera.position.x -= MouseEventDispatcher.getDeltaX() * dt * zoomLevel * speed;
                camera.position.y += MouseEventDispatcher.getDeltaY() * dt * zoomLevel * speed;
            }
        }
    }


    /**
     * Zooms the screen in and out.
     * @param xoffset
     * @param yoffset
     */
    public void onScrolledEvent(float xoffset, float yoffset){

          if(zoomLevel > 1.0f)
            zoomLevel -= yoffset;
          else if(yoffset < 0)
              zoomLevel -= yoffset;

        camera.setProjectionMatrix(-aspectratio * zoomLevel, aspectratio * zoomLevel, -zoomLevel, zoomLevel);
    }

    /**
     * Updates the camera to take into account the window resize.
     * @param width - New screen width
     * @param height - New screen height
     */
    @Override
    public void onWindowResizeEvent(int width, int height) {
        aspectratio = (float) width / (float) height;

        camera.setProjectionMatrix(-aspectratio * zoomLevel, aspectratio * zoomLevel, -zoomLevel, zoomLevel);
    }
    public Camera getCamera(){
        return camera;
    }
}
