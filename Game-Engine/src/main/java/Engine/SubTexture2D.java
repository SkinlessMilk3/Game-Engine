package Engine;

import Renderer.Texture;
import org.joml.Vector2f;

public class SubTexture2D {
    Vector2f texCoords[] = new Vector2f[4];

    public SubTexture2D(Texture tx, Vector2f coords, Vector2f spriteDimensions){

        Vector2f min = new Vector2f((coords.x * spriteDimensions.x) / tx.getWidth(),
                (coords.y * spriteDimensions.y) / tx.getHeight());
        Vector2f max = new Vector2f(((coords.x + 1) * spriteDimensions.x) / tx.getWidth(),
                ((coords.y + 1) * spriteDimensions.y) / tx.getHeight());

        texCoords[0] = min;                         //bottom left corner
        texCoords[1] = new Vector2f(min.x, max.x);  //bottom right corner
        texCoords[2] = new Vector2f(max.x, max.y);  //top right corner
        texCoords[3] = new Vector2f(min.x, max.y);  //top left corner
    }
}
