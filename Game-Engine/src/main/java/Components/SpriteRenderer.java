/*
*
*
*
* CURRENTLY DOES NOTHING BUT PRINTS WHEN ATTACHED TO GAME OBJECT
*
*
*
*
 */

package Components;

import Engine.Component;
import Engine.GameObject;
import Engine.SubTexture2D;
import Renderer.Texture;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private boolean firstTime = false;
    private Texture tx;
    private SubTexture2D stx;
    private Vector4f color = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
    public SpriteRenderer(GameObject go, Texture texture){
        gameObject = go;
        tx = texture;
    }

    public SpriteRenderer(GameObject go, SubTexture2D texture2D){
        gameObject = go;
        stx = texture2D;
    }

    public SpriteRenderer(GameObject go, Vector4f colour){
        color = colour;
        gameObject = go;
    }

    public Texture getTexture(){
        return tx;
    }

    public SubTexture2D getSubTexture2D(){
        return stx;
    }

    public Vector4f getColor(){
        return color;
    }

    @Override
    public void start() {
        System.out.println("I am starting");
    }

    @Override
    public void update(float dt) {
        if (!firstTime) {
            System.out.println("I am updating");
            firstTime = true;
            
        }
    }
}