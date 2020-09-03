import java.io.*;

class GL_LOG{

    FileWriter out;

    public GL_LOG(){

    }

    public void Restart_Log(){
        if(out == null){
            out = new FileWriter("Log.txt");
        }
        LocalDateTime time = LocalDateTime.now();
        int seconds = Seconds(time);
        int days = Days(time);
        int hours = Hours(time);
        int minutes = Minutes(time);
        out.write();
    }
}