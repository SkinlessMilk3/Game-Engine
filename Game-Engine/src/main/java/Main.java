public class Main {

    public static void main(String[] args){

        GL_LOG log = new GL_LOG();

        //start the log.txt with data and time
        log.Restart_Log();
GL_Shader_Reader sh = new GL_Shader_Reader();
        System.out.println("FILE: " + sh.getFileContent("first.vert"));
        Window.getWindow().run();
    }
}
