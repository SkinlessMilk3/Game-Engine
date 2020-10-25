package Engine.Scenes;

import API.EventListeners.KeyEventListener;
import Engine.*;
import Utils.AssetPool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import imgui.ImString;
import imgui.ImVec2;
import imgui.enums.ImGuiCond;
import imgui.enums.ImGuiTreeNodeFlags;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class ImguiTestScene extends Scene {

    public List<GameObjectData> objectData = new ArrayList<>();
    public List<GameObjectData> spriteData = new ArrayList<>();
    public List<GameObjectData> fontData = new ArrayList<>();
    public List<GameObjectData> scriptData = new ArrayList<>();
    public List<GameObjectData> roomData = new ArrayList<>();
    boolean firstUpdate = true;

    private GameObjectData selectedObject = null;

    @Override
    public void init() {

        objectDataCatagories.add(objectData);
        objectDataCatagories.add(spriteData);
        objectDataCatagories.add(fontData);
        objectDataCatagories.add(scriptData);
        objectDataCatagories.add(roomData);

        if (levelLoaded) {
            return;
        }

        levelLayerLabels.add("Default Layer");
        levelLayerLabels.add("Background Layer");




    }


    @Override
    public void update(float dt) {

        if (firstUpdate) {
            objectCount = objectData.size();
            spriteCount = spriteData.size();
            fontCount = fontData.size();
            scriptCount = scriptData.size();
            roomCount = roomData.size();
            firstUpdate = false;
        }

        for (GameObject go : this.gameObjects)
        {
            go.update(dt);
        }



    }

    //Not actual list of objects, but labels for imgui
    List<String> spriteLabels = new ArrayList<>();
    List<String> objectLabels = new ArrayList<>();
    List<String> scriptLabels = new ArrayList<>();
    List<String> fontLabels = new ArrayList<>();
    List<String> roomLabels = new ArrayList<>();
    //Most likely temporary counters as the ECS should handle counts of this kind.
    int spriteCount = 0;
    int objectCount = 0;
    int scriptCount = 0;
    int fontCount = 0;
    int roomCount = 0;

    boolean popupOpen = false;
    //Empty string newly created assets
    String assetName = "";
    ImString name = new ImString();

    //TEMP val to demonstrate list room/level layers
    //Will later be part of a room/level object through attached component
    List<String> levelLayerLabels = new ArrayList<>();
    int levelLayerCount = 2;
    int selectedLayer = 0;

    //Temp vals for display only to represent level/room width/height
    int tempW = 1024;
    int tempH = 768;

    @Override
    public void imgui() {

        MainMenuBarImGui();
        RoomEditorImGui();
        assetBrowserImGui();
        if (activeGameObject != null)
        {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }
    }

    private int addNewAsset(List<String> labels, int counter, String tag)
    {
        if (ImGui.beginPopupContextItem())
        {
            ImGui.inputText("", name);

            assetName = name.toString();
            if (ImGui.smallButton("Save"))
            {
                if (!assetName.isEmpty()) {
                    counter++;
                    labels.add(assetName);
                    if (tag.equals("Sprite")) { GameObjectData sprData = new GameObjectData(); sprData.setName(assetName); spriteData.add(sprData);}
                    if (tag.equals("Object")) { GameObjectData objData = new GameObjectData(); objData.setName(assetName); objectData.add(objData);}
                    if (tag.equals("Font")) { GameObjectData fntData = new GameObjectData(); fntData.setName(assetName); fontData.add(fntData);}
                    if (tag.equals("Script")) { GameObjectData scptData = new GameObjectData(); scptData.setName(assetName); scriptData.add(scptData);}
                    if (tag.equals("Room")) { GameObjectData rmData = new GameObjectData(); rmData.setName(assetName); roomData.add(rmData);}
                    assetName = "";
                    name.set("");
                }
                //ImGui.openPopupOnItemClick(" ");
                //popObjectInspector(objectData.get(objectDataCount));

                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }
        return counter;
    }

    private void deleteAsset(GameObjectData asset, String tag)
    {
        if (tag.equals("Sprite")) {
            //AssetPool.textures.remove(obj.texture);
            spriteData.remove(asset);
            spriteCount--;
        }
        if (tag.equals("Object")) {
            gameObjects.removeIf(go -> go.name.equals(asset.name));
            objectData.remove(asset);
            objectCount--;
        }
        if (tag.equals("Font")) {

            fontData.remove(asset);
            fontCount--;
        }
        if (tag.equals("Script")) {

            scriptData.remove(asset);
            scriptCount--;
        }
        if (tag.equals("Room")) {

            roomData.remove(asset);
            roomCount--;
        }
    }

    private void assetBrowserImGui()
    {
        //For accessing game assets like objects and sprites
        ImGui.begin("Asset Browser");

        //Trees to represent folders
        if (ImGui.treeNode("Sprites"))
        {

            //Button for adding new sprites
            ImGui.smallButton("Add Sprite");
            ImGui.openPopupOnItemClick("Add Sprite", 0);
            spriteCount = addNewAsset(spriteLabels, spriteCount, "Sprite");

            //Display all created sprites
            for (int i = 0; i < spriteCount; i++)
            {
                ImGui.bulletText(spriteData.get(i).name);
            }

            ImGui.treePop();
        }
        ImGui.spacing();
        if (ImGui.treeNode("Objects"))
        {
            //Button for adding new objects
            ImGui.smallButton("Add Object");
            ImGui.openPopupOnItemClick("Add Object", 0);
            objectCount = addNewAsset(objectLabels, objectCount, "Object");

            if(ImGui.beginPopupContextItem("Object Data Options"))
            {
                ImGui.text(selectedObject.name + ":");
                if (ImGui.beginPopup("Delete Confirmation"))
                {
                    ImGui.text("Are you sure?");
                    ImGui.text("(This will delete all instances of this object)");
                    if (ImGui.button("Yes"))
                    {
                        if (activeGameObject.name == selectedObject.name)
                        {
                            activeGameObject = null;
                        }
                        deleteAsset(selectedObject, "Object");
                        selectedObject = null;
                        ImGui.closeCurrentPopup();
                    }
                    ImGui.sameLine();
                    if (ImGui.button("No"))
                    {
                        ImGui.closeCurrentPopup();
                    }
                    ImGui.endPopup();
                }
                if(ImGui.button("Add to scene"))
                {
                    GameObject newObj = selectedObject.GenerateGameObject();
                    addGameObjectToScene(newObj);
                    activeGameObject = newObj;
                    ImGui.closeCurrentPopup();
                }
                if(ImGui.button("Delete"))
                {
                    ImGui.openPopup("Delete Confirmation");
                }
                if (selectedObject == null)
                {
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }

            //Display all created objects
            for (int i = 0; i < objectCount; i++)
            {
                ImGui.bulletText(objectData.get(i).name);
                if (ImGui.isItemHovered())
                {
                    selectedObject = objectData.get(i);
                    ImGui.openPopupOnItemClick("Object Data Options", 1);
                }
            }

            ImGui.treePop();
        }
        ImGui.spacing();
        if (ImGui.treeNode("Scripts"))
        {
            //Button for adding new scripts
            ImGui.smallButton("Add Script");
            ImGui.openPopupOnItemClick("Add Script", 0);
            scriptCount = addNewAsset(scriptLabels, scriptCount, "Script");

            //Display all created scripts
            for (int i = 0; i < scriptCount; i++)
            {
                ImGui.bulletText(scriptData.get(i).name);
            }

            ImGui.treePop();
        }
        ImGui.spacing();
        if (ImGui.treeNode("Fonts"))
        {
            //Button for adding new fonts
            ImGui.smallButton("Add Font");
            ImGui.openPopupOnItemClick("Add Font", 0);
            fontCount = addNewAsset(fontLabels, fontCount, "Font");

            //Display all created fonts
            for (int i = 0; i < fontCount; i++)
            {
                ImGui.bulletText(fontData.get(i).name);
            }

            ImGui.treePop();
        }
        ImGui.spacing();
        if (ImGui.treeNode("Rooms"))
        {
            //Button for adding new rooms
            ImGui.smallButton("Add Room");
            ImGui.openPopupOnItemClick("Add Room", 0);
            roomCount = addNewAsset(roomLabels, roomCount, "Room");

            //Display all created rooms
            for (int i = 0; i < roomCount; i++)
            {
                ImGui.bulletText(roomData.get(i).name);
            }

            ImGui.treePop();
        }

        ImGui.end();
    }

    private void MainMenuBarImGui()
    {

        if (ImGui.beginMainMenuBar())
        {
            if (ImGui.beginMenu("File"))
            {
                if (ImGui.menuItem("New Project")) {}
                if (ImGui.menuItem("Open Project")) {}
                ImGui.separator();
                if (ImGui.menuItem("Save Project", "CTRL+S")) { Window.setSaving(); }
                if (ImGui.menuItem("Save Project As", "CTRL+SHIFT+S")) {}
                ImGui.separator();
                if (ImGui.menuItem("Settings")) {}
                if (ImGui.menuItem("Exit", "ALT+F4")) {}
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Edit"))
            {
                if (ImGui.menuItem("Undo", "CTRL+Z")) {}
                if (ImGui.menuItem("Redo", "CTRL+Y", false, false)) {}  // Disabled item
                ImGui.separator();
                if (ImGui.menuItem("Cut", "CTRL+X")) {}
                if (ImGui.menuItem("Copy", "CTRL+C")) {}
                if (ImGui.menuItem("Paste", "CTRL+V")) {}
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Build"))
            {
                if (ImGui.menuItem("Build", "F5")) {}
                if (ImGui.menuItem("Run", "F6")) {}
                ImGui.separator();
                if (ImGui.menuItem("Create Executable")) {}
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Help"))
            {
                if (ImGui.menuItem("Getting Started")) {}
                if (ImGui.menuItem("Documentation")) {}
                ImGui.separator();
                if (ImGui.menuItem("Report Bug")) {}
                ImGui.endMenu();
            }
            ImGui.endMainMenuBar();
        }
    }

    public void RoomEditorImGui()
    {
        ImGui.begin("Room/Level Editor");

        ImGui.setNextItemOpen(true, ImGuiCond.Once);
        ImGui.spacing();
        if (ImGui.treeNodeEx("Layers - 'Room/Level Name'", 2)) {


            ImGui.button("Add Layer");
            ImGui.openPopupOnItemClick("Add Layer", 0);
            levelLayerCount = addNewAsset(levelLayerLabels, levelLayerCount, "Layer");
            ImGui.spacing(); ImGui.spacing();


            //Display all created levels/layers
            for (int i = 0; i < levelLayerCount; i++)
            {
                if (ImGui.selectable(levelLayerLabels.get(i), selectedLayer == i))
                {
                    selectedLayer = i;
                }
                ImGui.sameLine(150); ImGui.text(" Depth: " + i * 100);
            }

            ImGui.treePop();
        }
        ImGui.dummy(0.0f, 100.0f);
        ImGui.separator();

        ImGui.setNextItemOpen(true, ImGuiCond.Once);
        ImGui.spacing();
        if (ImGui.treeNodeEx("Layer Objects", 2)){

            ImGui.button(levelLayerLabels.get(selectedLayer) + " Objects", 150, 20);
            for (GameObject go : gameObjects)
            {
                ImGui.bullet();
                ImGui.selectable(go.name);
            }
            //ImGui.text("Level/room objects would be listed here");


            ImGui.treePop();
        }

        ImGui.dummy(0.0f, 100.0f);
        ImGui.separator();

        ImGui.setNextItemOpen(true, ImGuiCond.Once);
        ImGui.spacing();
        if (ImGui.treeNodeEx("Layer Properties", 2)) {

            //ImGui.text("Width");
            ImGui.sliderInt("Width", new int[]{tempW}, 0, 1980);
            //ImGui.text("Height");
             ImGui.sliderInt("Height", new int[]{tempH}, 0, 1020);
            ImGui.separator();
            ImGui.spacing(); ImGui.spacing();
            ImGui.button("Creation Code", 200, 20);


            ImGui.treePop();
        }

        ImGui.end();
    }

    public void SceneSelectorImGui()
    {
        ImGui.begin("Scene Selector");
        ImGui.textColored(44, 244, 193, 100, "Choose Scene!");
        ImGui.sameLine(); if (ImGui.button("Triangle")){
                                        Window.ChangeScene(0);
                            }
        ImGui.end();
    }

    public void ObjectInspectorImGui(GameObjectData objData)
    {



    }
}
