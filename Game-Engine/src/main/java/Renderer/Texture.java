package Renderer;

import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;

/*Used to load textures into a the opengl state space.*/
public class Texture {
    private int render_id;
    private int[] width = {0}, height = {0}, bitsPerPixel = {0};
    private ByteBuffer localBuffer = null;
    private String filePath;

    //Create a brand new texture. And set several options and parameters.
    public Texture(String fp){
        filePath = fp;

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
}
