package Engine.Scenes;

import Components.Component;
import Components.ComponentDeserializer;
import Components.SpriteRenderer;
import Engine.*;

import Renderer.Renderer2D;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    public List<GameObject> gameObjects = new ArrayList<>();
    public ArrayList<List<GameObjectData>> objectDataCatagories = new ArrayList<List<GameObjectData>>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;
    //Temp for demo game
    public boolean shoot;
    public boolean isGame = false;

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
            addGOtoRenderer(go);
        }
        isRunning = true;
    }

    public Camera camera() {
        return this.camera;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            addGOtoRenderer(go);
        }
    }

    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }

    public abstract void update(float dt);
    public abstract void render();

    //Gets current "selected" object in gui

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
            //Save current objects in scene
            if (!isGame){
                FileWriter writer = new FileWriter("instances.txt");
                writer.write(gson.toJson(this.gameObjects));
                writer.close();
                //Save data for objects that can be added
                writer = new FileWriter("objectData.txt");
                writer.write(gson.toJson(this.objectDataCatagories));
                writer.close();
            } else {
                FileWriter writer = new FileWriter("gameInstances.txt");
                writer.write(gson.toJson(this.gameObjects));
                writer.close();
                //Save data for objects that can be added
                writer = new FileWriter("gameObjectData.txt");
                writer.write(gson.toJson(this.objectDataCatagories));
                writer.close();
            }
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
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++)
            {
                addGameObjectToScene(objs[i]);

                for (Component c : objs[i].getAllComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }
                if (objs[i].getUid() > maxGoId) {
                    maxGoId = objs[i].getUid();
                }
            }
            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
            this.levelLoaded = true;
        }

        try {
            inFile = new String(Files.readAllBytes(Paths.get("objectData.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            GameObjectData[][] objData = gson.fromJson(inFile, GameObjectData[][].class);

           deserializeObjectData(objData, objectDataCatagories, 0);
           deserializeObjectData(objData, objectDataCatagories, 1);
           deserializeObjectData(objData, objectDataCatagories, 2);
           deserializeObjectData(objData, objectDataCatagories, 3);
           deserializeObjectData(objData, objectDataCatagories, 4);

            this.levelLoaded = true;
        }
    }

    public void deserializeObjectData(GameObjectData[][] gson, ArrayList<List<GameObjectData>> sceneData, int listNum)
    {
        for (int i = 0; i < gson[listNum].length; i++)
        {
            sceneData.get(listNum).add(gson[listNum][i]);
        }
    }

    public void addGOtoRenderer(GameObject go)
    {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null)
        {
            addSprRentoRenderer(spr);
        }
    }

    public void addSprRentoRenderer(SpriteRenderer sprite)
    {
        //If we already have maxSquare sprites, then we catch the error
        try {

            Renderer2D.addSprite(sprite);
        } catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
        catch (Error e)
        {
            System.out.println("Unknown Error!");
            e.printStackTrace();
        }
    }

    public GameObject getActiveGameObject()
    {
        return activeGameObject;
    }

    public void setActiveGameObject(GameObject go) { this.activeGameObject = go; }
}