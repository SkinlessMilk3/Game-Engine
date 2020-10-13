package Renderer;

import Utils.GL_LOG;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL45.glTextureStorage2D;
import static org.lwjgl.opengl.GL45.glTextureSubImage2D;
import static org.lwjgl.stb.STBImage.*;

/*Used to load textures into a the opengl state space.*/
public class Texture {
    private int render_id;
    private int[] width = {0}, height = {0}, bitsPerPixel = {0};
    private ByteBuffer localBuffer = null;
    private String filePath;
    private int internalFormat, dataFormat;

    public Texture(int width, int height){

        render_id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, render_id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        internalFormat = GL_RGBA8;
        dataFormat = GL_RGBA;

        this.width[0] = width;
        this.height[0] = height;
        glEnable(GL_TEXTURE_2D);
        glTextureStorage2D(render_id, 1, this.internalFormat, this.width[0], this.height[0]);
    }
    //Create a brand new texture. And set several options and parameters.
    public Texture(String fp){
        filePath = fp;

        internalFormat = GL_RGBA8;
        dataFormat = GL_RGBA;

        render_id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, render_id);

        stbi_set_flip_vertically_on_load(true);
        localBuffer = stbi_load(filePath, width, height, bitsPerPixel, 4);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, localBuffer);

        glBindTexture(GL_TEXTURE_2D, 0);
        glEnable(GL_TEXTURE_2D);

        if(localBuffer != null) {
            stbi_image_free(localBuffer);
        }
    }

    public int getRender_id(){return render_id;}

    //assign the texture a sample slot and bind the texture
    public void bind(int slot){
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, render_id);
    }
    //unbind the texture, but shouldn't be done often. Poor for performance.
    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    //Deletes the texture from the GPU. A new object would need to be instantiated after this
    //function call.
    public void delete(){
        glDeleteTextures(render_id);
    }

    public void setData(int size, FloatBuffer data){
        int bpp = 0;
        if(dataFormat == GL_RGBA)
            bpp = 4;
        else//GL_RGB
            bpp = 3;
        if(size != width[0] * height[0] * bpp)
            GL_LOG.Log_Data("Error creating SubImmage");
        else
            glTextureSubImage2D(render_id, 0,0,0, this.width[0], this.height[0], dataFormat, GL_FLOAT, data);
    }
}
