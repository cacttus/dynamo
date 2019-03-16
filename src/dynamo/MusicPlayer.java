package dynamo;


import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Avicenna
 */
public class MusicPlayer implements Runnable {

    ArrayList   _trackFileNames  = new ArrayList();
    ArrayList   _tracks = new ArrayList(); // MediaPlayer class files.
    ArrayList   _trackLevelMap = new ArrayList(); // - corresponds to _trackFilenames and maps accordingly.
    int         _playingId; // - Current Id in "trackLevelMap" that is playing
    Thread      _playingThread;  // - Thread that is currently playing.
    
    public MusicPlayer()
    {
        addMusicFiles();
        createMusicFiles();
    }
    
    public void musicPlayLoopForId(int Id) 
    {
        try
        { 
            _playingId = Id;
            _playingThread = new Thread(this);
            _playingThread.start();
        }
        catch (Exception e) 
        { 
             System.out.println(" [MP3] Failed to strart thread. " + Id);
        }
    }
    public void stopMusic()
    {
        _playingThread.stop();//Whateva
    }

    
    private String getDataFolderBase()
    {
        return "file:///C:/Users/Avicenna/Documents/NetBeansProjects/Dynamo_Src/Data/Music/";
    }
    private void addMusicFiles()
    {
        // - for some reason we started level at 3
        addMusicForLevelId("level_1.mp3", 3);
        //...
    }
    private void addMusicForLevelId(String name, int levelId)
    {
        _trackFileNames.add(name);
        _trackLevelMap.add(levelId);
    }
    private void createMusicFiles()
    {
        ;
        
        // - Create the mp3 files
        for(int i=0; i<_trackFileNames.size(); ++i)
        {
            //String fn = getDataFolderBase() + (String)_trackFileNames.get(i);
            MediaPlayer mediaPlayer;
            String fn = "";
            String trackFn = (String)_trackFileNames.get(i);
            try 
            {
                fn = dynamo.data.Loader.getResource(trackFn);
                 mediaPlayer = new MediaPlayer(new Media(fn));
                 _tracks.add(mediaPlayer);
            }
            catch (Exception e) 
            {
                System.out.println(" [MP3] Failed to find file " + trackFn + " from " + fn);
            }
        }
    }

    @Override
    public void run() 
    {
        try 
        { 
            MediaPlayer mp;
            
            for(int i=0; i<_trackLevelMap.size(); ++i)
            {
                if((int)_trackLevelMap.get(i) == _playingId)
                {
                    mp = ((MediaPlayer)_tracks.get(i));
                    // play file
                    mp.play();
                    // - once done playing we then call this same run() method and re-play the file.
                    mp.setOnEndOfMedia(this);// - Assume that this will re call this media.
                    // - Set the beginning of the media.
                    mp.seek(Duration.ZERO);
                    return;
                }
            }
        }
        catch (Exception e) 
        { 
             System.out.println(" [MP3] Failed to play file " + _playingId);
        }
    }
}
