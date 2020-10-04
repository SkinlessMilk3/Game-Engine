package Renderer;

import Engine.Camera;
import Engine.GL_LOG;
import org.joml.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Renderer2D {

    private static class renderData{
        public static int maxSquares = 10000;
        public static int maxIndices = 6 * maxSquares;
        public static int maxVertexCount = 4 * maxSquares;
        public static int indexCount = 0;
        public static int vertexCount = 0;

        public static Shader shader;
        public static VAO vao;
        public static Matrix4f transform;
        public static IBO ibo;
        public static VBO vbo;
        public static Texture whiteTexture;
    }

    private static class vertexData{
        public Vector3d positions;
        public Vector4d color;
        public Vector2d texCoords;
        public static float[] vertexBuffer = new float[renderData.maxVertexCount];
    }
    public static void Init(){

        /*float points[] = {
                -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,//bottom left
                -0.5f, 0.5f, 0.0f, 0.0f, 1.0f,//top left
                0.5f, -0.5f, 0.0f, 1.0f, 0.0f,//bottom right
                0.5f, 0.5f, 0.0f, 1.0f, 1.0f//top right
        };*/

        /*int indices[] = {
                0, 1, 2,
                1, 2, 3
        };*/
        int sizeOfFloat = 4;
        final int posSize = 3;
        final int posOffset = 0;
        final int colorSize = 4;
        final int colorOffset = posOffset + posSize * sizeOfFloat;
        final int stride = (posSize + colorSize) * sizeOfFloat;
        final int vertexSize = 7;
        final int vertexSizeBytes = vertexSize * sizeOfFloat;

        renderData.shader = new Shader("Assets/testing.glsl");
        renderData.vao = new VAO();
        renderData.vbo = new VBO(renderData.maxVertexCount * sizeOfFloat);

       glVertexAttribPointer(0, posSize, GL_FLOAT, false, stride, posOffset);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, stride, colorOffset);
        glEnableVertexAttribArray(1);

        int[] indices = new int[renderData.maxIndices];
        int offset = 0;
        for(int i = 0; i < renderData.maxIndices; i+= 6){

            indices[i + 0] = offset + 0;
            indices[i + 1] = offset + 1;
            indices[i + 2] = offset + 2;
            indices[i + 3] = offset + 2;
            indices[i + 4] = offset + 3;
            indices[i + 5] = offset + 0;
             offset += 4;
        }

        renderData.ibo = new IBO(indices);

        /*renderData.whiteTexture = new Texture(1, 1);

        FloatBuffer whiteTextureData = BufferUtils.createFloatBuffer(4);
        float[]tmp = {1.0f, 1.0f, 1.0f, 1.0f};
        whiteTextureData.put(tmp);
        whiteTextureData.flip();

        renderData.whiteTexture.setData(4, whiteTextureData);
        /*renderData.transform = new Matrix4f();

        VBO vbo = new VBO(GL_ARRAY_BUFFER, points);
        renderData.ibo = new IBO(indices);
        renderData.vao = new VAO();


*/
    }

    public static void shutdown(){
        renderData.vao.delete();
        renderData.shader.delete();
        renderData.ibo.delete();
        //renderData.whiteTexture.delete();
    }
    public static void Clear(){
        glClearColor(0.8f, 1.8f, 0.8f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT| GL_DEPTH_BUFFER_BIT);
    }
    public static void beginScene(final Camera camera){
//TODO figure out camera system
        //Matrix4f projmatrix = new Matrix4f().identity().ortho(-1.0f, 1.0f/*Window.getWidth()*/, -1.0f, 1.0f/*Window.getHeight()*/, -1.0f, 1.0f);

        renderData.indexCount = 0;
        renderData.vertexCount = 0;
        renderData.shader.bind();
        renderData.shader.setUniformMat4("u_view", camera.getViewMatrix());
        renderData.shader.setUniformMat4("u_projection", camera.getProjectionMatrix());
        //renderData.shader.setUniform4f("u_color", 1.0f, 1.0f, 0.0f, 1.0f);
        GL_LOG.Log_Data("begin scene error: " + glGetError());
    }
    public static void endScene(){

        renderData.vao.bind();

        renderData.vbo.pushData(vertexData.vertexBuffer);
        renderData.ibo.bind();
        Draw(renderData.indexCount);
    }
    public static void submit(final Vector2d position, final Vector2d size, final Vector4d color){
        submit(new Vector3d((float)position.x, (float)position.y, 1.0f), size , color);
    }
    public static void submit(final Vector3d position, final Vector2d size, final Vector4d color) {

        /*for(int y = 0; y < 4; y++) {
            for (int x = 0; x < y + 8; x++) {
                if (x == 0) {
                    vertexData.vertexBuffer[0 + renderData.vertexCount] = (float) position.x;
                    vertexData.vertexBuffer[1 + renderData.vertexCount] = (float) position.y;
                    vertexData.vertexBuffer[2 + renderData.vertexCount] = 0.0f;
                }
                else if(x == 1){
                    vertexData.vertexBuffer[7 + renderData.vertexCount] = (float)position.x + (float)size.x;
                    vertexData.vertexBuffer[8 + renderData.vertexCount] = (float)position.y;
                    vertexData.vertexBuffer[9 + renderData.vertexCount] = 0.0f;
                }


                vertexData.vertexBuffer[3 + renderData.vertexCount] = (float) color.x;
                vertexData.vertexBuffer[4 + renderData.vertexCount] = (float) color.y;
                vertexData.vertexBuffer[5 + renderData.vertexCount] = (float) color.z;
                vertexData.vertexBuffer[6 + renderData.vertexCount] = (float) color.w;
            }
        }*/
        vertexData.vertexBuffer[0 + renderData.vertexCount] = (float)position.x;
        vertexData.vertexBuffer[1 + renderData.vertexCount] = (float)position.y;
        vertexData.vertexBuffer[2 + renderData.vertexCount] = 0.0f;
        vertexData.vertexBuffer[3 + renderData.vertexCount] = (float)color.x;
        vertexData.vertexBuffer[4 + renderData.vertexCount] = (float)color.y;
        vertexData.vertexBuffer[5 + renderData.vertexCount] = (float)color.z;
        vertexData.vertexBuffer[6 + renderData.vertexCount] = (float)color.w;
//GL_LOG.Log_Data("vert1 " + position.x + " " + position.y);
        vertexData.vertexBuffer[7 + renderData.vertexCount] = (float)position.x + (float)size.x;
        vertexData.vertexBuffer[8 + renderData.vertexCount] = (float)position.y;
        vertexData.vertexBuffer[9 + renderData.vertexCount] = 0.0f;
        vertexData.vertexBuffer[10 + renderData.vertexCount] = (float)color.x;
        vertexData.vertexBuffer[11 + renderData.vertexCount] = (float)color.y;
        vertexData.vertexBuffer[12 + renderData.vertexCount] = (float)color.z;
        vertexData.vertexBuffer[13 + renderData.vertexCount] = (float)color.w;
        //GL_LOG.Log_Data("vert2 " + ((float)position.x + (float)size.x )+ " " + position.y);

        vertexData.vertexBuffer[14 + renderData.vertexCount] = (float)position.x + (float)size.x;
        vertexData.vertexBuffer[15 + renderData.vertexCount] = (float)position.y + (float)size.y;
        vertexData.vertexBuffer[16 + renderData.vertexCount] = 0.0f;
        vertexData.vertexBuffer[17 + renderData.vertexCount] = (float)color.x;
        vertexData.vertexBuffer[18 + renderData.vertexCount] = (float)color.y;
        vertexData.vertexBuffer[19 + renderData.vertexCount] = (float)color.z;
        vertexData.vertexBuffer[20 + renderData.vertexCount] = (float)color.w;
        //GL_LOG.Log_Data("vert3 " + ((float)position.x + (float)size.x) + " " + ((float)position.y + (float)size.y));
        vertexData.vertexBuffer[21 + renderData.vertexCount] = (float)position.x;
        vertexData.vertexBuffer[22 + renderData.vertexCount] = (float)position.y + (float)size.y;
        vertexData.vertexBuffer[23 + renderData.vertexCount] = 0.0f;
        vertexData.vertexBuffer[24 + renderData.vertexCount] = (float)color.x;
        vertexData.vertexBuffer[25 + renderData.vertexCount] = (float)color.y;
        vertexData.vertexBuffer[26 + renderData.vertexCount] = (float)color.z;
        vertexData.vertexBuffer[27 + renderData.vertexCount] = (float)color.w;
        //GL_LOG.Log_Data("vert4 " + position.x + " " + ((float)position.y + (float)size.y));
        renderData.indexCount += 6;
        renderData.vertexCount += 28;

        //renderData.whiteTexture.bind(0);
        //renderData.shader.setUniform1i("u_texture", 0);
        //renderData.shader.setUniform4f("u_color", (float)color.x, (float)color.y, (float)color.z, (float)color.w);
        //GL_LOG.Log_Data("submission error: " + glGetError());

        /*

        renderData.transform.identity();
        renderData.transform.translate((float)position.x, (float)position.y, (float)position.z)
                .scale((float)size.x, (float)size.y, 1.0f);

        renderData.ibo.bind();

        renderData.shader.setUniformMat4("u_transform", renderData.transform);

        Draw();*/
    }
    public static void submit(final Vector2d position, final Vector2d size, final Texture texture){
        submit(new Vector3d((float)position.x, (float)position.y, 1.0f), size ,texture);
    }
    public static void submit(final Vector3d position, final Vector2d size, final Texture texture){
        renderData.transform.identity();
        renderData.transform.translate((float)position.x, (float)position.y, (float)position.z)
                .scale((float)size.x, (float)size.y, 1.0f);

        renderData.shader.bind();
        renderData.vao.bind();
        renderData.ibo.bind();
        texture.bind(0);
        renderData.shader.setUniform1i("u_texture", 0);
        renderData.shader.setUniformMat4("u_transform", renderData.transform);
        renderData.shader.setUniform4f("u_color", 1.0f, 1.0f, 1.0f, 1.0f);

        Draw(0);
    }

    private static void Draw(int count){
        if(count == 0){
            count = renderData.ibo.getCount();
        }
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
    }
}
