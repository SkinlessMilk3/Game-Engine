package Engine.Scenes;

import API.EventListeners.KeyEventListener;
import Components.Sprite;
import Components.SpriteRenderer;
import Components.TestGame.BulletLoop;
import Components.TestGame.EnemyLoop;
import Components.TestGame.PlayerLoop;
import Engine.*;
import Renderer.Renderer2D;
import Renderer.Texture;
import Utils.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class GameDemoScene extends Scene{

    int objectsInScene = gameObjects.size();
    private boolean firstLoad = true;
    private static CameraController control = new CameraController((float) Window.getWidth()/(float)Window.getHeight());

    private int loops = 0;

    public void init() {
        isGame = true;
        this.camera = new Camera(new Vector2f(-250, 0));


        objectsInScene = gameObjects.size();

        if (levelLoaded) {
            return;
        }
        //Create Background
        Transform backgroundTransform = new Transform(new Vector2f(-3.0f, 0f), new Vector2f(4f, 2f));
        GameObject background = new GameObject("background", backgroundTransform);
        Sprite backgroundSpr = new Sprite();
        backgroundSpr.setTexture(AssetPool.getTexture("Assets/GameDemo/spaceBack.png"));
        Texture backgroundTex = AssetPool.getTexture("Assets/GameDemo/spaceBack.png");
        SpriteRenderer backgroundSprRen = new SpriteRenderer(backgroundTex, background);
        backgroundSprRen.setSprite(backgroundSpr);
        background.addComponent(backgroundSprRen);
        addGameObjectToScene(background);

        //Create player object
        Transform playerTransform = new Transform(new Vector2f(-2.75f, 1.0f), new Vector2f(0.25f, 0.25f));
        GameObject player = new GameObject("player", playerTransform);
        Sprite playerSpr = new Sprite();
        playerSpr.setTexture(AssetPool.getTexture("Assets/GameDemo/ship.png"));
        Texture playerTex = AssetPool.getTexture("Assets/GameDemo/ship.png");
        SpriteRenderer playerSprRen = new SpriteRenderer(playerTex, player);
        playerSprRen.setSprite(playerSpr);
        PlayerLoop playerLoop = new PlayerLoop();
        player.addComponent(playerSprRen);
        player.addComponent(playerLoop);
        addGameObjectToScene(player);

        spawnEnemies(0.6f, 1.55f);
        spawnEnemies(0.50f, 1.0f);
        spawnEnemies(0.6f, 0.30f);


    }

    @Override
    public void update(float dt) {

        control.onUpdate(dt);

        if (firstLoad) {
            loadResources();
            firstLoad = false;
        }

        for (GameObject go : this.gameObjects)
        {
            go.update(dt);

        }
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i).transform.position.x > 0.80f && gameObjects.get(i).name.equals("bullet")) { Renderer2D.removeFromRenderer(gameObjects.get(i)); gameObjects.remove(gameObjects.get(i)); }
        }
        shoot();

        if (loops >= 100) {
            spawnEnemies(0.6f, 1.55f);
            spawnEnemies(0.50f, 1.0f);
            spawnEnemies(0.6f, 0.30f);
            loops = 0;
        }
        if (loops < 100) {
            loops++;
        }

    }

    @Override
    public void render() {
        Renderer2D.beginScene(control.getCamera());

        Renderer2D.endScene();
    }

    private void loadResources() {
        AssetPool.getShader("Assets/testing.glsl");
        AssetPool.getTexture("Assets/GameDemo/ship.png");
        AssetPool.getTexture("Assets/GameDemo/ship.png");
        AssetPool.getTexture("Assets/GameDemo/laser.png");
        AssetPool.getTexture("Assets/GameDemo/enemy.png");

    }

    private void shoot() {
        if (shoot) {
            Texture tex = AssetPool.getTexture("Assets/GameDemo/laser.png");
            GameObject bullet = new GameObject("bullet", new Transform(new Vector2f((gameObjects.get(1).transform.position.x + 0.1f), gameObjects.get(1).transform.position.y + 0.075f), new Vector2f(0.25f,0.10f)));
            Sprite spr = new Sprite();
            SpriteRenderer sprRen = new SpriteRenderer(tex, bullet);
            spr.setTexture(tex);
            sprRen.setSprite(spr);
            BulletLoop bLoop = new BulletLoop();

            bullet.addComponent(sprRen);
            bullet.addComponent(bLoop);

            Window.getScene().addGameObjectToScene(bullet);
            shoot = false;
        }
    }

    private void spawnEnemies(float x, float y) {

        //Create Enemy Object
        Transform enemyTransform = new Transform(new Vector2f(x, y), new Vector2f(0.0652f, 0.125f));
        GameObject enemy = new GameObject("enemy", enemyTransform);
        Sprite enemySpr = new Sprite();
        enemySpr.setTexture(AssetPool.getTexture("Assets/GameDemo/enemy.png"));
        Texture enemyTex = AssetPool.getTexture("Assets/GameDemo/enemy.png");
        SpriteRenderer enemySprRen = new SpriteRenderer(enemyTex, enemy);
        enemySprRen.setSprite(enemySpr);
        EnemyLoop enemyLoop = new EnemyLoop();
        enemy.addComponent(enemySprRen);
        enemy.addComponent(enemyLoop);
        addGameObjectToScene(enemy);
    }
}
