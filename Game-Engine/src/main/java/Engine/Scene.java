package Engine;

import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;


public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
        }
    }

    public abstract void update(float dt);

    //Gets current "selected" object in gui
    public void sceneImgui() {
        if (activeGameObject != null)
        {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    //The function that handles the layout of every imgui per scene
    public void imgui() {

    }
}