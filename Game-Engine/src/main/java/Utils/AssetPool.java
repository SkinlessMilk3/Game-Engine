package Utils;

import Renderer.Shader;
import Renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/*
    Class that offsets loading of shaders and textures from inside of classes
    and allows them to be abstracted.

    It also allows a single reference to files(only textures and shaders atm) so that a function that might get a
    file path every loop now is now getting a reference to the same file through
    a hash map.

    (Overall it's an optimization class that is especially useful since Java is a
    "garbage collecting" language)
 */
public class AssetPool {
    public static Map<String, Shader> shaders = new HashMap<>();
    public static Map<String, Texture> textures = new HashMap<>();


    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);

        if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        }
        else
        {
            Shader shader = new Shader(resourceName);
            shader.compileShader();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);

        if (AssetPool.textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        }
        else
        {
            Texture texture = new Texture(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }
}
