package dynamo;



import java.io.BufferedInputStream;
import java.io.FileInputStream;

//import javazoom.jl.player.Player;
import java.net.*;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.swing.JOptionPane;


public class MP3 implements Runnable {
    private String filename;
    //private Player player; 
    private MediaPlayer mediaPlayer;

    public MP3( String file ) 
    {		
        //Application.launch();
        try {
            //cinve=rt to uri
            filename = dynamo.data.Loader.getResource(file);
            //filename = "file:///C:/Users/Avicenna/Documents/NetBeansProjects/Dynamo_Src/" + file;
            mediaPlayer = new MediaPlayer(new Media(filename));
        }
        catch (Exception e) {
            //JOptionPane.showMessageDialog(null,"Exception when creating a new file.\n" + e );
            System.out.println(" [MP3] Failed to find file " + filename);
        }

    }
    public void loop()
    {
        
    }
    public void play() 
    {
        try { 
            Thread thread = new Thread(this);
            thread.start();
        }
         catch (Exception e) { 
             System.out.println(" [MP3] Failed to strart thread" + filename);
        }
    }

    public void stop()
    {
        if (mediaPlayer != null)
                mediaPlayer.stop();
    }

    @Override
    public void run() {
        try { 
            mediaPlayer.play();
            mediaPlayer.seek(Duration.ZERO);
        }
         catch (Exception e) { 
             System.out.println(" [MP3] Failed to play file " + filename);
        }
    }

}