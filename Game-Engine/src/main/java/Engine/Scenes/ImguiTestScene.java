package Engine.Scenes;

import Engine.Window;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import imgui.ImString;
import imgui.ImVec2;
import imgui.enums.ImGuiCond;
import imgui.enums.ImGuiTreeNodeFlags;

import java.util.ArrayList;
import java.util.List;

public class ImguiTestScene extends Scene {

    @Override
    public void init() {

        levelLayerLabels.add("Default Layer");
        levelLayerLabels.add("Background Layer");
    }


    @Override
    public void update(float dt) {

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
    }

    private int addNewAsset(List<String> labels, int counter)
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
                    assetName = "";
                    name.set("");
                }
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }
        return counter;
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
            spriteCount = addNewAsset(spriteLabels, spriteCount);

            //Display all created sprites
            for (int i = 0; i < spriteCount; i++)
            {
                ImGui.bulletText(spriteLabels.get(i));
            }

            ImGui.treePop();
        }
        ImGui.spacing();
        if (ImGui.treeNode("Objects"))
        {
            //Button for adding new objects
            ImGui.smallButton("Add Object");
            ImGui.openPopupOnItemClick("Add Object", 0);
            objectCount = addNewAsset(objectLabels, objectCount);

            //Display all created objects
            for (int i = 0; i < objectCount; i++)
            {
                ImGui.bulletText(objectLabels.get(i));
            }

            ImGui.treePop();
        }
        ImGui.spacing();
        if (ImGui.treeNode("Scripts"))
        {
            //Button for adding new scripts
            ImGui.smallButton("Add Script");
            ImGui.openPopupOnItemClick("Add Script", 0);
            scriptCount = addNewAsset(scriptLabels, scriptCount);

            //Display all created scripts
            for (int i = 0; i < scriptCount; i++)
            {
                ImGui.bulletText(scriptLabels.get(i));
            }

            ImGui.treePop();
        }
        ImGui.spacing();
        if (ImGui.treeNode("Fonts"))
        {
            //Button for adding new fonts
            ImGui.smallButton("Add Font");
            ImGui.openPopupOnItemClick("Add Font", 0);
            fontCount = addNewAsset(fontLabels, fontCount);

            //Display all created fonts
            for (int i = 0; i < fontCount; i++)
            {
                ImGui.bulletText(fontLabels.get(i));
            }

            ImGui.treePop();
        }
        ImGui.spacing();
        if (ImGui.treeNode("Rooms"))
        {
            //Button for adding new rooms
            ImGui.smallButton("Add Room");
            ImGui.openPopupOnItemClick("Add Room", 0);
            roomCount = addNewAsset(roomLabels, roomCount);

            //Display all created rooms
            for (int i = 0; i < roomCount; i++)
            {
                ImGui.bulletText(roomLabels.get(i));
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
                if (ImGui.menuItem("Save Project", "CTRL+S")) {}
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
            levelLayerCount = addNewAsset(levelLayerLabels, levelLayerCount);
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
            ImGui.text("Level/room objects would be listed here");


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
        ImGui.sameLine();
        if (ImGui.button("Triangle")){
            Window.ChangeScene(0);
        }
        ImGui.end();
    }
}
