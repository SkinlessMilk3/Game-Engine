package Editor;

import API.EventListeners.MouseEventDispatcher;
import Engine.GameObject;
import Engine.Scenes.Scene;
import Engine.Window;
import Renderer.PickingTexture;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
        //activeGameObject = Window.getScene().getActiveGameObject();
    }

    public void update(float dt, Scene currentScene) {
        //activeGameObject = currentScene.getActiveGameObject();
        if (MouseEventDispatcher.isPressed(GLFW_MOUSE_BUTTON_LEFT)) {
            int x = (int)MouseEventDispatcher.getScreenX();
            int y = (int)MouseEventDispatcher.getScreenY();
            //System.out.println(pickingTexture.readPixel(x, y));
            activeGameObject = currentScene.getGameObject(pickingTexture.readPixel(x, y));
            Window.getScene().setActiveGameObject(activeGameObject);
        }
        if (Window.getScene().getActiveGameObject() == null){
            voidActiveObject();
        }
    }

    public void imgui() {
        if (activeGameObject != null)
        {
            ImGui.begin("Inspector");
            ImGui.text("Name: " + activeGameObject.name);
            ImGui.text("ID: " + activeGameObject.getUid());
            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {
        return this.activeGameObject;
    }

    public void setActiveGameObject(GameObject go) {
        this.activeGameObject = go;
    }

    public void voidActiveObject() {
        this.activeGameObject = null;
    }
}
