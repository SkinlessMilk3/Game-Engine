import java.io.*;

/*
* Reads shaders from files. Preferably they will have the appropriate file extensions like *.vert *.frag
 */
public class GL_Shader_Reader {

    public GL_Shader_Reader(){}

    public String getFileContent(String filename){

        FileReader in = null;
        char[] buffer = new char[2048];

        try {
            in = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("failed to open file to read");
        }

        if(in != null){

            try {
                in.read(buffer, 0, 2048);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to close shader file");
        }
        return new String(buffer);
    }
}
