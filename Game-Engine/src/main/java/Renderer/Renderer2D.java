package Renderer;

import Components.SpriteRenderer;
import Engine.Camera;
import Engine.GL_LOG;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

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
        /*
       transform = new Matrix4f();

        VBO vbo = new VBO(GL_ARRAY_BUFFER, points);

       ibo = new IBO(indices);

       vao = new VAO();


*/
    }

    public static void shutdown(){
        vao.delete();
        shader.delete();
        ibo.delete();
        //
        //whiteTexture.delete();
    }
    public static void Clear(){
        glClearColor(0.8f, 1.8f, 0.8f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT| GL_DEPTH_BUFFER_BIT);
    }
    public static void beginScene(final Camera camera){
//TODO figure out camera system
        //Matrix4f projmatrix = new Matrix4f().identity().ortho(-1.0f, 1.0f/*Window.getWidth()*/, -1.0f, 1.0f/*Window.getHeight()*/, -1.0f, 1.0f);

        indexCount = 0;
        vertexCount = 0;
        shader.bind();
        shader.setUniformMat4("u_view", camera.getViewMatrix());
        shader.setUniformMat4("u_projection", camera.getProjectionMatrix());
        whiteTexture.bind(0);
        shader.setUniform1i("u_texture", 0);
        //
        //shader.setUniform4f("u_color", 1.0f, 1.0f, 0.0f, 1.0f);
        GL_LOG.Log_Data("begin scene error: " + glGetError());
    }
    public static void endScene(){

        vao.bind();

        vbo.pushData(vertexBuffer);
        ibo.bind();
        Draw(indexCount);
    }
    public static void submit(final Vector2f position, final Vector2f size, final Vector4f color){
        submit(new Vector3f(position.x, position.y, 1.0f), size , color);
    }
    public static void submit(final Vector3f position, final Vector2f size, final Vector4f color) {

        CreateSquare(position, size, color);

        indexCount += 6;
        vertexCount += 9 * 4;
        //
        //whiteTexture.bind(0);
        //
        //shader.setUniform1i("u_texture", 0);
        //
        //shader.setUniform4f("u_color", (float)color.x, (float)color.y, (float)color.z, (float)color.w);
        //GL_LOG.Log_Data("submission error: " + glGetError());

        /*


       transform.identity();

       transform.translate((float)position.x, (float)position.y, (float)position.z)
                .scale((float)size.x, (float)size.y, 1.0f);


       ibo.bind();


       shader.setUniformMat4("u_transform",
       transform);

        Draw();*/
    }
    public static void submit(final Vector2f position, final Vector2f size, final Texture texture){
        submit(new Vector3f(position.x, position.y, 1.0f), size, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f) , texture);
    }
    public static void submit(final Vector3f position, final Vector2f size, final Vector4f color, final Texture texture){

        texture.bind(0);
        shader.setUniform1i("u_texture", 0);

        CreateSquare(position, size, color);
    }

    private static void Draw(int count){
        if(count == 0){
            count = ibo.getCount();
        }
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
    }
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
