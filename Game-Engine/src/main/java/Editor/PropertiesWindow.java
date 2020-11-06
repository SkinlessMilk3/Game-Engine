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
            float xval = activeGameObject.transform.position.x;
            float[] imFloatx = {xval};
            if (ImGui.dragFloat("X Pos: ", imFloatx)) {
                activeGameObject.transform.position.x = imFloatx[0];
            }
            float yval = activeGameObject.transform.position.y;
            float[] imFloaty = {yval};
            if (ImGui.dragFloat("Y Pos: ", imFloaty)) {
                activeGameObject.transform.position.y = imFloaty[0];
            }
            float sclaexval = activeGameObject.transform.scale.x;
            float[] imFloatScalex = {sclaexval};
            if (ImGui.dragFloat("X Scale: ", imFloatScalex)) {
                activeGameObject.transform.scale.x = imFloatScalex[0];
            }
            float scaleyval = activeGameObject.transform.scale.y;
            float[] imFloatScaley = {scaleyval};
            if (ImGui.dragFloat("Y Scale: ", imFloatScaley)) {
                activeGameObject.transform.scale.y = imFloatScaley[0];
            }
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
