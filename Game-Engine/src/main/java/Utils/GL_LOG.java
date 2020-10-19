package Utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/*
 *Desc: Opens a txt file called Log.txt to write information such as opengl errors. Stored in the build
 * output file.
 */
public class GL_LOG{

    private static FileWriter out;

    public GL_LOG(){}

    public void Restart_Log(){

        try {
            out = new FileWriter("../../../build/Log.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to open file Log.txt");
        }

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter obj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH;mm:ss");
        String dateAndTime = time.format(obj);

        try {
            out.write(dateAndTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to close log file.");
            }
        }
    }

    //Write error to log.txt
    static public void Log_Data(String message){

        try {
            out = new FileWriter("../../../build/Log.txt",true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.write("\n"+ message);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to write data to Log.txt");
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void Log_Data(char message){

        try {
            out = new FileWriter("../../../build/Log.txt",true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.write("\n"+ message);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to write data to Log.txt");
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}