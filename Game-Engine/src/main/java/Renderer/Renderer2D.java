package Renderer;

import Components.SpriteRenderer;
import Engine.Camera;
import Utils.GL_LOG;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 * This is a renerer class specifically designed for 2D graphics. It will take various data and use
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

    private static Shader shader;
    private static VAO vao;
    private static Matrix4f transform;
    private static IBO ibo;
    private static VBO vbo;
    private static Texture whiteTexture;

    private Vector3d positions;
    private Vector4d color;
    private Vector2d texCoords;

    private SpriteRenderer[] sprites;
    private int spriteNum;

    private static float[] vertexBuffer = new float[maxVertexCount];

    /**
     * Must be called before submitting data for render. If not then it could cause unexpected
     * behavior. It only needs to be called once it shouldn't be inside a loop.
     */
    public static void Init(){

        int sizeOfFloat = 4;
        final int posSize = 3;
        final int colorSize = 4;
        final int textureSize = 2;

        final int posOffset = 0;
        final int colorOffset = posOffset + posSize * sizeOfFloat;
        final int textureOffset = colorOffset + colorSize * sizeOfFloat;
        final int stride = (posSize + colorSize + textureSize) * sizeOfFloat;
        final int vertexSize = posSize + colorSize + textureSize;
        final int vertexSizeBytes = vertexSize * sizeOfFloat;


        shader = new Shader("Assets/testing.glsl");
        vao = new VAO();
        vbo = new VBO(maxVertexCount * sizeOfFloat);

       glVertexAttribPointer(0, posSize, GL_FLOAT, false, stride, posOffset);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, stride, colorOffset);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, textureSize, GL_FLOAT, false, stride, textureOffset);
        glEnableVertexAttribArray(2);

        int[] indices = new int[maxIndices];
        int offset = 0;
        for(int i = 0; i < maxIndices; i+= 6){

            indices[i + 0] = offset + 0;
            indices[i + 1] = offset + 1;
            indices[i + 2] = offset + 2;
            indices[i + 3] = offset + 2;
            indices[i + 4] = offset + 3;
            indices[i + 5] = offset + 0;
             offset += 4;
        }

        ibo = new IBO(indices);


       whiteTexture = new Texture(1, 1);


        FloatBuffer whiteTextureData = BufferUtils.createFloatBuffer(4);
        float[]tmp = {1.0f, 1.0f, 1.0f, 1.0f};
        whiteTextureData.put(tmp);
        whiteTextureData.flip();

        whiteTexture.setData(4, whiteTextureData);
    }

    /**
     * Releases the resources allocated when we are done. It is best practice to do this whenever
     * you are done rendering things to the screen. If this happens before then it will definitely
     * break the porgram.
     */
    public static void shutdown(){
        vao.delete();
        shader.delete();
        ibo.delete();
        whiteTexture.delete();
    }

    /**
     * Sets the clear color and clears the screen.
     */
    public static void Clear(Vector4f vec){
        glClearColor(vec.x, vec.y, vec.z, vec.w);
        glClear(GL_COLOR_BUFFER_BIT| GL_DEPTH_BUFFER_BIT);
    }
    public static void beginScene(final Camera camera){

        indexCount = 0;
        vertexCount = 0;
        shader.bind();
        shader.setUniformMat4("u_view", camera.getViewMatrix());
        shader.setUniformMat4("u_projection", camera.getProjectionMatrix());
        whiteTexture.bind(0);
        shader.setUniform1i("u_texture", 0);

        int check;
        if((check = glGetError()) != 0)
            GL_LOG.Log_Data("begin scene error: " + check);
    }

    /**
     * renders data to the screen.
     */
    public static void endScene(){

        vao.bind();

        vbo.pushData(vertexBuffer);
        ibo.bind();
        Draw(indexCount);
    }

    /**
     * This function creates vertex data for a square including a square color and submits it to an
     * array for drawing later. There are two options to choose from. A 2D option and a 3D option.
     * The 2D option will be submitted to the 3D option, so it really is for convenience.
     *
     * @param position - a vector containing position data for the current screen.
     * @param size - just a vector containing height and width data
     * @param color - vector of data containing color values between 1.0 and 0.0
     */
    public static void submit(final Vector2f position, final Vector2f size, final Vector4f color){
        submit(new Vector3f(position.x, position.y, 1.0f), size , color);
    }
    public static void submit(final Vector3f position, final Vector2f size, final Vector4f color) {

        CreateSquare(position, size, color);

        indexCount += 6;
        vertexCount += 9 * 4;

    }

    /**
     * These function is similar to the one's before except these include a texture option.
     * @param position - Contains position data of a single point on the screen.
     * @param size - Contains data about the width and height of the square.
     * @param texture - This is just a texture that should be loaded into memory before the function
     *                call.
     */
    public static void submit(final Vector2f position, final Vector2f size, final Texture texture){
        submit(new Vector3f(position.x, position.y, 1.0f), size, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f) , texture);
    }
    public static void submit(final Vector3f position, final Vector2f size, final Vector4f color, final Texture texture){

        texture.bind(0);
        shader.setUniform1i("u_texture", 0);

        CreateSquare(position, size, color);
    }

    /**
     * Helper function draws a square to the screen.
     * @param count - the amount of indices in the index buffer to draw. If it is zero it will draw
     *              all the indices.
     */
    private static void Draw(int count){
        if(count == 0){
            count = ibo.getCount();
        }
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
    }

    /**
     * A helper function that does all the math and calculation for square vertex positions and
     * puts them into the vertex buffer. It also does this for color and texture data.
     * @param position - A single point on the screen to begin drawing a square.
     * @param size - The height and width of the square.
     * @param color - The color of the square.
     */
    private static void CreateSquare(Vector3f position, Vector2f size, Vector4f color){
        int count = 0;
        for(int i = 0; i < 4; i++){
            float x = position.x;
            float y = position.y;
            float tx = 0.0f;
            float ty = 0.0f;

            if(i == 1) {
                x += size.x;
                tx = 1.0f;
            }
            else if(i == 2){
                x += size.x;
                y += size.y;
                tx = 1.0f;
                ty = 1.0f;
            }
            else if(i == 3){
                y += size.y;
                ty = 1.0f;
            }
            vertexBuffer[count + vertexCount] = x;
            count++;
            vertexBuffer[count + vertexCount] = y;
            count++;
            vertexBuffer[count + vertexCount] = 0.0f;
            count++;
            vertexBuffer[count + vertexCount] = color.x;
            count++;
            vertexBuffer[count + vertexCount] = color.y;
            count++;
            vertexBuffer[count + vertexCount] = color.z;
            count++;
            vertexBuffer[count + vertexCount] = color.w;
            count++;
            vertexBuffer[count + vertexCount] = tx;
            count++;
            vertexBuffer[count + vertexCount] = ty;
            count++;
        }
    }
}
