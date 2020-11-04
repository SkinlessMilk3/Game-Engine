package Renderer;

import Components.SpriteRenderer;
import Engine.Camera;
import Engine.GameObject;
import Engine.Transform;
import Engine.Window;
import Utils.AssetPool;
import Utils.GL_LOG;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.CallbackI;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

/**
 * This is a renderer class specifically designed for 2D graphics. It will take various data and use
 * it do draw things to the screen.
 * How To Use - anything you want to submit to be drawn to the screen must be surrounded by
 * beginScene() and endScene().
 * Ex.
 *      beginScene()
 *      submit()
 *      submit()
 *      submit()
 *      endScene()
 *
 * Once endScene() is called the items submitted will be drawn.
 */
public class Renderer2D {

    private static int maxSquares = 10000;
    private static int maxIndices = 6 * maxSquares;
    private static int maxVertexCount = 4 * maxSquares;
    private static int indexCount = 0;
    private static int vertexCount = 0;

    private static Shader currentShader;
    private static VAO vao;
    private static Matrix4f transform;
    private static IBO ibo;
    private static VBO vbo;
    private static Texture whiteTexture;

    private Vector3d positions;
    private Vector4d color;
    private Vector2d texCoords;
    public static List<Texture> textures = new ArrayList<>();
    private static int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8};

    private static SpriteRenderer[] sprites = new SpriteRenderer[maxSquares];
    private static int spriteNum = 0;
    public static boolean hasRoom;

    private static float[] vertexBuffer = new float[maxVertexCount];

    private static boolean rebuferData = false;

    /**
     * Must be called before submitting data for render. If not then it could cause unexpected
     * behavior. It only needs to be called once it shouldn't be inside a loop.
     */
    public static void Init() {

        int sizeOfFloat = 4;
        final int posSize = 3;
        final int colorSize = 4;
        final int textureSize = 2;
        final int textureIdSize = 1;
        final int entityIdSize = 1;

        final int posOffset = 0;
        final int colorOffset = posOffset + posSize * sizeOfFloat;
        final int textureOffset = colorOffset + colorSize * sizeOfFloat;
        final int textureIdOffest = textureOffset + textureSize * sizeOfFloat;
        final int stride = (posSize + colorSize + textureSize + textureIdSize + entityIdSize) * sizeOfFloat;
        final int vertexSize = posSize + colorSize + textureSize + textureIdSize + entityIdSize;
        final int entityIdOffest = textureOffset + textureSize * sizeOfFloat;
        final int vertexSizeBytes = vertexSize * sizeOfFloat;

        sprites = new SpriteRenderer[maxSquares];
        vertexBuffer = new float[maxVertexCount];
        textures = new ArrayList<>();
        vertexCount = 0;
        indexCount = 0;
        spriteNum = 0;



        hasRoom = true;

        //currentShader = AssetPool.getShader("Assets/testing.glsl");
        vao = new VAO();
        vbo = new VBO(maxVertexCount * sizeOfFloat);

        glVertexAttribPointer(0, posSize, GL_FLOAT, false, stride, posOffset);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, stride, colorOffset);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, textureSize, GL_FLOAT, false, stride, textureOffset);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, textureIdSize, GL_FLOAT, false, stride, textureIdOffest);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, entityIdSize, GL_FLOAT, false, stride, entityIdOffest);
        glEnableVertexAttribArray(4);

        int[] indices = new int[maxIndices];
        int offset = 0;
        for (int i = 0; i < maxIndices; i += 6) {

            indices[i + 0] = offset + 0;
            indices[i + 1] = offset + 1;
            indices[i + 2] = offset + 2;
            indices[i + 3] = offset + 2;
            indices[i + 4] = offset + 3;
            indices[i + 5] = offset + 0;
            offset += 4;
        }

        ibo = new IBO(indices);


        /*whiteTexture = new Texture(1, 1);


        FloatBuffer whiteTextureData = BufferUtils.createFloatBuffer(4);
        float[] tmp = {1.0f, 1.0f, 1.0f, 1.0f};
        whiteTextureData.put(tmp);
        whiteTextureData.flip();

        whiteTexture.setData(4, whiteTextureData);*/
    }

    /**
     * Releases the resources allocated when we are done. It is best practice to do this whenever
     * you are done rendering things to the screen. If this happens before then it will definitely
     * break the porgram.
     */
    public static void shutdown() {
        vao.delete();
        currentShader.delete();
        ibo.delete();
        //whiteTexture.delete();
        for (int i=0; i < textures.size(); i++) {
            textures.get(i).delete();
        }
    }

    /**
     * Sets the clear color and clears the screen.
     */
    public static void Clear(Vector4f vec) {
        glClearColor(vec.x, vec.y, vec.z, vec.w);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void beginScene(final Camera camera) {

        //indexCount = 0;
        // vertexCount = 0;
        rebuferData = false;
        for (int i = 0; i < spriteNum; i++)
        {
            SpriteRenderer spr = sprites[i];
            if (spr.isDirty()) {
                CreateSquare(i);
                spr.setClean();
                rebuferData = true;
            }
        }

        currentShader = getBoundShader();
        currentShader.bind();
        currentShader.setUniformMat4("u_view", camera.getViewMatrix());
        currentShader.setUniformMat4("u_projection", camera.getProjectionMatrix());
        //whiteTexture.bind(0);
        for (int i=0; i < textures.size(); i++) {
            textures.get(i).bind(i + 1);
        }
        currentShader.uploadIntArray("u_texture", texSlots);
        //currentShader.setUniform1i("u_texture", 0);

        int check;
        if ((check = glGetError()) != 0)
            GL_LOG.Log_Data("begin scene error: " + check);
    }

    /**
     * renders data to the screen.
     */
    public static void endScene() {
        vao.bind();

        vbo.pushData(vertexBuffer);

        ibo.bind();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        Draw(indexCount);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i=0; i < textures.size(); i++) {
            textures.get(i).unbind();
        }
        currentShader.unbind();
    }

    /**
     * This function creates vertex data for a square including a square color and submits it to an
     * array for drawing later. There are two options to choose from. A 2D option and a 3D option.
     * The 2D option will be submitted to the 3D option, so it really is for convenience.
     *
     */
    /*public static void submit(final Vector2f position, final Vector2f size, final Vector4f color){
        submit(new Vector3f(position.x, position.y, 1.0f), size , color);
    }*/

    public static void submit(int spriteIndex) {

        //sprites[spriteIndex].getTexture().bind(0);
        //currentShader.setUniform1i("u_texture", 0);
        CreateSquare(spriteIndex);
        indexCount += 6;
        vertexCount += 11 * 4;
    }

    /**
     * These function is similar to the one's before except these include a texture option.
     *
     * @param position - Contains position data of a single point on the screen.
     * @param size     - Contains data about the width and height of the square.
     * @param texture  - This is just a texture that should be loaded into memory before the function
     *                 call.
     */
    public static void submit(final Vector2f position, final Vector2f size, final Texture texture) {
        submit(new Vector3f(position.x, position.y, 1.0f), size, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), texture);
    }

    public static void submit(final Vector3f position, final Vector2f size, final Vector4f color, final Texture texture) {

        texture.bind(0);
        currentShader.setUniform1i("u_texture", 0);

        //CreateSquare(position, size, color);
    }

    /**
     * Helper function draws a square to the screen.
     *
     * @param count - the amount of indices in the index buffer to draw. If it is zero it will draw
     *              all the indices.
     */
    private static void Draw(int count) {
        if (count == 0) {
            count = ibo.getCount();
        }
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
    }

    public static void addSprite(SpriteRenderer spr) {
        int index = spriteNum;
        sprites[index] = spr;
        spriteNum++;

        if (spr.getTexture() != null) {
            if (!textures.contains(spr.getTexture())) {
                textures.add(spr.getTexture());
            }
        }

        submit(index);

        if (spriteNum >= maxSquares)
        {
            hasRoom = false;
        }
    }

    public static void removeFromRenderer(GameObject go) {
        int indexToDel = 0;
        int verCount = 11;
        int spriteSize = verCount * 4;
        int spriteNumAtDelete = spriteNum;

        //Get rid of 4 verticies
        if (spriteNum <= 1) {
            spriteNum--;
            for (int i = 0; i < vertexCount; i++) {
                vertexBuffer[i] = -1;
            }
        } else {
            spriteNum--;
            for (int i = (spriteSize - 1); i < maxVertexCount; i += 43) {
                if (i != (spriteSize - 1)) {
                    i++;
                }
                if (go.getUid() + 1 == vertexBuffer[i]) {
                    indexToDel = i;
                    for (int j = spriteSize; j > 0; j--) {
                        vertexBuffer[i] = -1;
                        i--;
                    }
                    break;
                }
            }
            //Add last in place of deleted
            if ((spriteNumAtDelete * (spriteSize - 1) + spriteNum) > indexToDel) {
                int lastSpriteIndex = spriteNumAtDelete * (spriteSize - 1) + spriteNum;
                for (int i = indexToDel; i > (indexToDel - spriteSize); i--) {
                    vertexBuffer[i] = vertexBuffer[lastSpriteIndex];
                    vertexBuffer[lastSpriteIndex] = 0.0f;
                    lastSpriteIndex--;
                }
            }
            sprites[((indexToDel + 1) / spriteSize) - 1] = sprites[spriteNumAtDelete - 1];
            sprites[spriteNumAtDelete - 1] = null;
        }
    }

    /**
     * A helper function that does all the math and calculation for square vertex positions and
     * puts them into the vertex buffer. It also does this for color and texture data.
     */
    private static void CreateSquare(int index) {
        SpriteRenderer sprite = sprites[index];

        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        int offset = index * 4 * 11;

        int texId = 0;
        if (sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i).equals(sprite.getTexture())) {
                    texId = i + 1;
                    break;
                }
            }
        }

        float xAdd = 1.0f;
        float yAdd = 1.0f;

        int count = 0;
        for (int i = 0; i < 4; i++) {
            float tx = 0.0f;
            float ty = 0.0f;
            if (i == 1) {
                yAdd = 0.0f;
                tx = 1.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
                tx = 1.0f;
                ty = 1.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
                ty = 1.0f;
            }
            vertexBuffer[offset + 0] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            //System.out.println(vertexBuffer[offset + 0]);
            count++;
            vertexBuffer[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);
            //System.out.println(vertexBuffer[offset + 1]);
            count++;
            vertexBuffer[offset + 2] = 0.0f;
            //System.out.println(vertexBuffer[offset + 2]);
            count++;
            vertexBuffer[offset + 3] = color.x;
            //System.out.println(vertexBuffer[offset + 3]);
            count++;
            vertexBuffer[offset + 4] = color.y;
            //System.out.println(vertexBuffer[offset + 4]);
            count++;
            vertexBuffer[offset + 5] = color.z;
            //System.out.println(vertexBuffer[offset + 5]);
            count++;
            vertexBuffer[offset + 6] = color.w;
            //System.out.println(vertexBuffer[offset + 6]);
            count++;
            vertexBuffer[offset + 7] = texCoords[i].x;
            //System.out.println(vertexBuffer[offset + 7]);
            count++;
            vertexBuffer[offset + 8] = texCoords[i].y;
            //System.out.println(vertexBuffer[offset + 8]);
            count++;
            vertexBuffer[offset + 9] = texId;

            //load entity id
            vertexBuffer[offset + 10] = sprite.gameObject.getUid() + 1;
            //System.out.println("Offset + 10: " + (offset + 10));

            offset += 11;
        }
    }

    public static void bindShader(Shader shader)
    {
        currentShader = shader;
        shader.bind();
    }

    public static Shader getBoundShader() {
        return currentShader;
    }

}
