package Engine;

import Renderer.Texture;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class SubTexture2D {
    Vector2f texCoords[] = new Vector2f[4];
    //int render_id;

    public SubTexture2D(Texture tx, Vector2f coords, Vector2f spriteDimensions){
        //render_id = glGenTextures();

        Vector2f min = new Vector2f((coords.x * spriteDimensions.x) / tx.getWidth(),
                (coords.y * spriteDimensions.y) / tx.getHeight());
        Vector2f max = new Vector2f(((coords.x + 1) * spriteDimensions.x) / tx.getWidth(),
                ((coords.y + 1) * spriteDimensions.y) / tx.getHeight());

        texCoords[0] = min;                         //bottom left corner
        texCoords[1] = new Vector2f(max.x, min.y);  //bottom right corner
        texCoords[2] = max;                         //top right corner
        texCoords[3] = new Vector2f(min.x, max.y);  //top left corner
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    /*public void bind(int slot){
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, render_id);
    }*/
}
