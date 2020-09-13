import Engine.Window;


public class Main {

    public static void main(String[] args){

        Engine.GL_LOG log = new Engine.GL_LOG();

        //start the log.txt with data and time
        log.Restart_Log();
        Window.getWindow().run();
    }
}
