package Engine.Scenes;

import Engine.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    public List<GameObject> gameObjects = new ArrayList<>();
    ArrayList<List<GameObjectData>> objectDataCatagories = new ArrayList<List<GameObjectData>>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;

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
            //activeGameObject.imgui();
            ImGui.text(activeGameObject.name);
            ImGui.end();
        }

        imgui();
    }

    //The function that handles the layout of every imgui per scene
    public void imgui() {

    }

    public void saveExit() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            //Save current objects in scenee
            FileWriter writer = new FileWriter("instances.txt");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
            //Save data for objects that can be added
            writer = new FileWriter("objectData.txt");
            writer.write(gson.toJson(this.objectDataCatagories));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("instances.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++)
            {
                addGameObjectToScene(objs[i]);
            }
            this.levelLoaded = true;
        }

        try {
            inFile = new String(Files.readAllBytes(Paths.get("objectData.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            Type listOfMyClassObject = new TypeToken<List<List<GameObjectData>>>() {}.getType();
            ArrayList<List<GameObjectData>> objData = gson.fromJson(inFile, listOfMyClassObject);

            //List<GameObjectData> test = objData.get(0);

           // test = objectDataCatagories.get(0);
            //objectDataCatagories.get(0) = test;

            this.levelLoaded = true;
        }
    }
}