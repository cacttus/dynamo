package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MyApplet.java

// - Copyright 2007 Derek Page
/**
*	C - Crazy particles
*
*	turn hacks on - "i am a cheater"
*	turn debug on - "debug me"
*
*
*	*** HACKS, DEBUGGING, AND OTHER CONTROLS ***
*	d - Show debug.
*	v - When debugging, show / hide variables
*	F1 - When hacking, Set the end of level timer
*	F2 - When hacking, Destroy all enemies
*	(F1+F2) - Go to next level.
*	F3 - When hacking, Upgrade Ship to maximum.
*	F4 - When hacking, Create Moon Shield.
*	F5 - When Hacking, Randomly set new moon shiled paths to Cycloid.
*	F6 - When Hacking, Slow Down Framerate.
*	F7 - When Hacking, Speed Up Framerate.
*	F8 - When Hacking, Reset Frame rate.
* 	F9 - Invincible Enemies (not bosses)
*	F10 - Set score to 1000000.
*/


import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;
import static javafx.application.Platform.exit;
import javafx.embed.swing.JFXPanel;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static jdk.nashorn.internal.runtime.regexp.joni.Syntax.Java;

public class Dynamo extends JFXPanel implements Runnable, KeyListener
{
	
    static final float g_Version = 1.08F;

    MenuOrSubMenu _menu;
    float Version;
    int Room_Width;
    int Room_Height;
    int Screen_Width;
    int Screen_Height;  // - Separating screen from room width allows us to rescale the given image.
    Thread thread;
    Random RAND;
    VECTOR2 Backdrop_Pos;
    float Backdrop_Speed;
    int g_Score;
    int g_Level;
    int g_MaxLevels;
    int g_TimeToNextLevel;
    int g_MaxTimeToNextLevel;
    int g_TimeToBoss;
    int g_HealthUp;
    boolean Show_Score;
    boolean g_TextDisplay;
    boolean g_UsingMusic;
    boolean g_UsingSFX;
    int g_CurMessageIndex;			// - The current message that is to be displayed.
    String g_DisplayStrings[];		// - A list of messages that are shown at the end of each level.
    // boolean g_DisplayMessage;
    boolean g_EndOfLevelAnimation;	// - Set to true when the end of level messages are over, and the ship flys off.
    float g_MessageSpeed;
    int g_MessageWaitTime;			// - How many frames to wait until transitioning to the next level. (the "fly off into the distance" time)
    boolean g_Transitioning;
    float g_TransitionLinePos;
    float g_TempYSPD;
    boolean g_PlayEndSound;
    int g_BMLength;
    int g_BMMaxLength;
    FreindShip KellyMarie;
    boolean g_BossMode;
    boolean g_EndCredits;
    boolean g_MainMenu;
    int g_PlayNumber;
    ANIMATION g_cursor;

    VECTOR2 g_StoryPos;			// - Position of the story string when it is showing.
    boolean g_Story;			// - STATE: THe story is currently showed.

    boolean g_EndSequence;
    int g_MaxEndTimer;
    int g_EndTimer;
    boolean g_OptionsMenu;
    String g_OptionsStrings[];
    int g_OptionsValues[];
    int g_OptionsUpperLimits[];
    int g_OptionsLowerLimits[];
    boolean g_OptionsEnabled[];
    boolean g_EditingOption;
    VECTOR2 g_IntroPos;
    ANIMATION g_Logo;
    boolean g_Intro;
    int g_IntroTimer0;
    int g_MaxIntroTimer0;
    int g_IntroTimer1;
    int g_MaxIntroTimer1;
    int g_IntroWait;
    int g_MaxIntroWait;
    VECTOR2 g_PopMessagePos;
        String g_PopMessageString;
    int g_PopMessageTimer;
    int g_MaxPopMessageTimer;
    int g_SpecialBackgroundIndex;
    boolean g_SpecialLevel;
    int g_SpecialScore;
    boolean g_ColorfulParticles;
    boolean g_BigParticles;
    boolean g_CrazyParticles;
    //private Image Back_Buffer;
    private BufferedImage Back_Buffer;
    private BufferedImage Front_Buffer;
    
    private String _menuitem_newgame = "new_game";
    private String _menuitem_continue = "continue";
    private String _menuitem_story = "story";
    private String _menuitem_special = "special";
    private String _menuitem_options = "options";

    
    private Graphics Back_Graphics;
    int Accum_Swaps;
    int Max_Accum_Swaps;
    private Image Accum_Last;
    private Image Accum_Buffer;
    private Image Transparent_Reset;
    private Graphics Accum_Graphics;
    MP3 SoundEffects[];
    Image Backgrounds[];
    BufferedImage SpriteMap;
    boolean UP_down;
    boolean LEFT_down;
    boolean RIGHT_down;
    boolean DOWN_down;
    boolean SPACE_down;
    boolean CONTROL_down;
    PlayerShip Player;
    EnemyCreator enemyCreator;
    ANIMATION Explosions[];
    ParticleHandler particleHandler;
    PowerUp PowerUpHandler;
    DodadController dodadController;
    cIncrementalMessage MessageHandler;
    cBoss Boss;
    cCreditsHandler CreditHandler;
    String g_StoryString;
    boolean P_Down;
    boolean g_Paused;			// - If the game loop is paused
    FrameCheck g_FrameChecker;

    String upgradeStrings[];		// - A list of strings indicating what level the player's ship is at (displayed on the main screen)

    long g_ThreadSleepTime;		// - Time that we make the tread sleep to let the system do it's processing.
    
    boolean g_Debug;			// - Debug mode.
    boolean g_Media_Loaded;		// - Flag indicating if the media has been loaded or not.

    int g_ConsoleHeight;		// - The height of the console portion of the screen, at the bottom.
    float g_StoryPosSpeed;
    boolean g_CurvyMoons;		// - The curvature of the moons
    VECTOR2 Vel ;
    VECTOR2 temp_cPos;

    String g_MexicoPrintStr;		// Static string printed ** for debug

    ///#########################
    // FOCUS
    boolean lastHadFocus = true;

    //####################################
    // - LOADING
    int 	g_sfxLoadStage;
    int 	g_sfxMaxLoadStage;
    float	g_LoadProgress;			// 0.0 - 1.0 - load for the whole game.
    int         g_NumSFX;

    //#####################################
    // - CURRENT FRAME VARIABLES
    // these variables are valid only in the context of the current frame.

    //#####################################
    // - FLY TRANSITION ANIMATION
    //
    float		flyCushion_T;		// - Time value for the sky fall animation.
    float 		flyShip_T;		// - Time value for the ship to change direction.
    float               flyCloud_T;		// - The Time for the cloud animation.
    float 		flyMotion_T;		// - The T for interpolating the ship to the different points.
    float		flyShine_T;		// - Time for player shine animation.
    int			flyCur_Key;		// - The current key in the fly aniamtion.
    boolean             g_flyStaticGround;	// - If the ground is moving or not.
    VECTOR2             flyAnimKeys[];		// - Interpolation points for the ship to fly to.
    static final int    FLY_TRANSITION_LEVEL = 7;   // - The level in which we play the fly transition
    static final int    FLY_NUM_KEYS = 4;	// - The number of keys in the fly animation.		- There must be at least two keys

    //#####################################
    // - GAME STATE
    boolean 	g_Loading;
    String		currentLoadingInfo;
    int			g_LoadState;
    boolean		g_ContinueMode;		// - The player has died and the main menu will display a continue.

    //#####################################
    // - SONG CONSTANTS
    static final int 		SONG_END = 18;


    //#####################################
    // - OTHER LEVEL CONSTNATS
    static final int 		GATE_LEVEL = 8;			// - The last level before the boss.
    static final float  	GATE_BACKDROP_SPEED = 12.0F;	// - The last level before the boss.
    static final float  	BOSS_BACKDROP_SPEED = 24.0F;	// - The last level before the boss.

    static final int		SPECIAL_LEVEL_INDEX = 1000;	// - All levels under 1000 are not special levels
    static final int		SPECIAL_LEVEL_CAP = 1000;
    static final int		SPECIAL_BACKGROUND_INDEX = 10;

    //#####################################
    // - SOUND INDEX CONSTANTS
    public static final int BOSS_APPEAR_SOUND = 17;
    MusicPlayer _musicPlayer;
    
    //#####################################
    // - HACKS
    boolean Hacks;				// - Set to true to have hacks.
    boolean g_Hack_SetLevelTimer;		// - Skips to the end of the level
    boolean g_Show_Debug_Variables;		// - If the debugging variables are being shown
    boolean g_Invincible_Enemies;		// - Makes enemies invincible.
    String  hackSwitch = "i am a cheater";      // - The string that you must type to turn hacks on.
    int		hackSwitchIndex;		// - Index of the hack switch.
    String  debugSwitch = "debug me";           // - turns on debugging
    boolean g_canDebug = false;

    //####################################
    // - HEALTH BAR BLINKER
    public static final int SOUND_HEALTH_BLINK = 19;
    BaseTimer bt;
    public float g_BlinkRatio;	// - 0 to 1, describes the amount of hilight the health bar gets.

    //###############3
    // - TITLE SCREEN STARFIELD
    class Star {
            VECTOR2 pos;
            float intensity;
            Star(){ pos = new VECTOR2(); }	
    }

    VECTOR2 g_StarSpeed;
    Star[]	Stars;

    /**
    *	@fn Dynamo()
    *	@brief Constructor to initialize all the many variables.
    */
    public Dynamo(int rWidth, int rHeight, int sWidth, int sHeight)
    {
        super();
        //super(name);
        g_ConsoleHeight=32;
        Version = 1.6F;
        
        //DEFAULT 512!!!!!!!
        Room_Width = rWidth;
        Room_Height = rHeight - g_ConsoleHeight;
        Screen_Width = sWidth;
        Screen_Height = sHeight;
        //DEFAULT 512!!!!!!!

        Backdrop_Speed = 1.0F;
        g_Score = 0;
        g_Level = 3;
        g_MaxLevels = 9;
        g_TimeToNextLevel = 2500;
        g_MaxTimeToNextLevel = 2500;
        g_TimeToBoss = 100;
        g_HealthUp = 1;
        g_CurMessageIndex = 0;
        g_PlayNumber = 0;
        g_MessageSpeed = 0.01F;
        g_MessageWaitTime = 145;
        g_TransitionLinePos = 0.0F;
        g_TempYSPD = 0.0F;
        g_BMLength = 100;
        g_BMMaxLength = 100;
        KellyMarie = new FreindShip();
        g_cursor = new ANIMATION();
        g_StoryPos = new VECTOR2();
        g_MaxEndTimer = 70;
        g_EndTimer = 70;
        g_OptionsStrings = new String[2];
        g_OptionsValues = new int[g_OptionsStrings.length];
        g_OptionsUpperLimits = new int[g_OptionsStrings.length];
        g_OptionsLowerLimits = new int[g_OptionsStrings.length];
        g_OptionsEnabled = new boolean[g_OptionsStrings.length];
        g_IntroPos = new VECTOR2();
        g_Logo = new ANIMATION();
        g_IntroTimer0 = 50;
        g_MaxIntroTimer0 = 50;
        g_IntroTimer1 = 330;
        g_MaxIntroTimer1 = 330;
        g_IntroWait = 1000;
        g_MaxIntroWait = 1000;
        g_PopMessagePos = new VECTOR2();
        g_PopMessageString = "";
        g_PopMessageTimer = -1;
        g_MaxPopMessageTimer = 100;
        g_SpecialBackgroundIndex = 8;
        g_SpecialScore = 1000000;
        Accum_Swaps = 7;
        Max_Accum_Swaps = 7;
        CreditHandler = new cCreditsHandler(Room_Width, Room_Height);
        g_FrameChecker = new FrameCheck();	// - Initialize the FPS Checker
         g_StoryString = GameScript.setStory();
        Vel = new VECTOR2();
        g_ThreadSleepTime = 20L;
        temp_cPos = new VECTOR2();
        g_StoryPosSpeed=0.01F;
        g_LoadProgress = 0.0F;


        g_NumSFX=20;

        g_sfxLoadStage=0;
        g_sfxMaxLoadStage = g_NumSFX;

        //###############################
        // - FLAGS
        P_Down 			= false;
        g_Paused		= false;
        Show_Score              = false;
        g_TextDisplay 		= false;
        g_UsingMusic 		= false;
        g_UsingSFX 		= true;
        g_EndOfLevelAnimation 	= false;
        g_Transitioning 	= false;
        g_BossMode 		= false;
        g_EndCredits 		= false;
        g_MainMenu 		= true;
        g_PlayEndSound 			= false;
        g_Story 			= false;
        g_EndSequence 			= false;
        g_OptionsMenu 			= false;
        g_EditingOption 		= false;
        g_Intro 			= true;
        g_SpecialLevel 			= false;
        g_ColorfulParticles             = false;
        g_BigParticles 			= false;
        g_CrazyParticles 		= false;
        g_Debug 			= false;
        g_Show_Debug_Variables          = true;
        g_Media_Loaded 			= false;
        g_Loading 			= true;
        g_CurvyMoons			= false;	// - The moons that surround the player will follow cycloid paths.
        SPACE_down 			= false;
        CONTROL_down 			= false;
        g_Invincible_Enemies            = false;
        g_ContinueMode 			= false;
        //#################################
        // - HACKS
        g_Hack_SetLevelTimer = false;
        Hacks = false;
        hackSwitchIndex=0;

        //##################################
        // - GAME MEDIA
        Backgrounds = new Image[11];
        //SoundEffects = new AudioClip[21];
        SoundEffects = new MP3[21];
        _musicPlayer = new MusicPlayer();
        
        //#################################
        // - FLY ANIMATION
        flyAnimKeys = new VECTOR2[FLY_NUM_KEYS];	// - Interpolation points for the ship to fly to.
        addKeyListener(this);
        setFocusable(true);

        //################################
        // - CURRENT FRAME VARIABLES.
        // variables valid only in the context of the current frame.

        //################################
        // - HEALTH BAR BLINKER
        bt = new BaseTimer();
        bt.start();
        g_BlinkRatio=0.0F;


        //################################
        // - TITLE SCREEN STARFIELD
        RAND = new Random();
        Stars = new Star[200];
        for( int i=0; i<Stars.length; ++i )
        {
                Stars[i] = new Star();
                Stars[i].pos.x = RAND.nextFloat() * Room_Width;
                Stars[i].pos.y = RAND.nextFloat() * Room_Height;
                Stars[i].intensity = RAND.nextFloat();
        }
        g_StarSpeed = new VECTOR2();
        g_StarSpeed.x = 0.0f;
        g_StarSpeed.y = 1.0f;

        requestFocus();
        lastHadFocus = true;	// - Request focus and set that the last focus was true.
        
        initDisplay(rWidth,rHeight);
        initMenu();
    }


    public void ErrorMsg( String s )
    {
        System.out.println( "\nAPPLICATION ERROR Code Line: " + Thread.currentThread().getStackTrace()[2].getLineNumber() + " :: " + s + "\n" );
    }

    public void Msg( String s )
    {
        System.out.println( "\nMSG:" + s );
    }

    public void setBossVariables() 
    {
        Boss.LongLaserClip = SoundEffects[15];
        Boss.DieSound = SoundEffects[16];
        Boss.bossWasHitSound = SoundEffects[20];
    }

    JFXPanel _mainCanvas = null;
    public JFXPanel getMainCanvas()
    {
        return _mainCanvas;
    }
    private void initDisplay(int nWidth,int nHeight)
    {
       // resize(nWidth,nHeight);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = ge.getDefaultScreenDevice();
        //GraphicsDevice d = new GraphicsDevice() {};
        
        //_mainCanvas = new Canvas( device.getDefaultConfiguration() );
         _mainCanvas = this;
         
        //f = new JFrame("h");//new GraphicsConfiguration());
        _mainCanvas.setBounds(0,0,nWidth,nHeight);
        _mainCanvas.setVisible(true);
     
        
        //add(this);
        // Back_Buffer = _mainCanvas.createImage(nWidth, nHeight);
        //Back_Graphics = _mainCanvas.getGraphics();
        byte[] r = new byte[]{(byte)0,(byte)255}; // 255=black, we could set it to some other gray component as desired
        byte[] g = new byte[]{(byte)0,(byte)255};
        byte[] b = new byte[]{(byte)0,(byte)255};
        byte[] a = new byte[]{(byte)255,(byte)0};
        IndexColorModel bitmaskColorModel = new IndexColorModel(1, 2, r, g, b, a);
        //bitmaskColorModel, null,false,null);//
        Back_Buffer = new BufferedImage(nWidth,nHeight, BufferedImage.TYPE_INT_ARGB);
        Back_Graphics = Back_Buffer.createGraphics();
        
        Front_Buffer = new BufferedImage(nWidth,nHeight, BufferedImage.TYPE_INT_ARGB);
        
//        Back_Buffer = createImage(nWidth, nHeight);
//        
//        System.out.println(getSize().width + " " + getSize().height);
//        Back_Graphics = Back_Buffer.getGraphics();// Back_Buffer.getGraphics();

//        BufferedImage off_Image =
//          new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
//
//        Graphics2D g2 = off_Image.createGraphics();

    }
    // - Start the game and initialize various variables.
    public void start()
    {
        // - Set up the appropriate state to load the media.
        g_LoadState=0;
        
        // - Start the applet
        thread = new Thread(this);
        thread.start();
    }

    // - Allocate memory
    public void AllocateMem() 
    {
        Backdrop_Pos = new VECTOR2();
        Backdrop_Pos.equals(0.0F);

        Player = new PlayerShip(Room_Width,Room_Height);
        enemyCreator = new EnemyCreator(Room_Width, Room_Height);
        Explosions = new ANIMATION[100];
        particleHandler = new ParticleHandler();
        PowerUpHandler = new PowerUp(Room_Width,Room_Height);
        dodadController = new DodadController();
        MessageHandler = new cIncrementalMessage();
        Boss = new cBoss(Room_Width, Room_Height);
        for(int i = 0; i < Explosions.length; i++)
            Explosions[i] = new ANIMATION();
		
        for( int i=0; i<flyAnimKeys.length; ++i )
            flyAnimKeys[i] = new VECTOR2();

        setBossVariables();
        g_IntroPos.x = Room_Width;
        g_IntroPos.y = 0.0F;
        PowerUpHandler.Init();
        enemyCreator.Init(100, 100, g_PlayNumber);
        
        // - Main Player ship
        Player.init(0, 2, 3);
        Player.InitPlayer();
        Player.Speed.assign(4F);
        
        particleHandler.Init();
        MessageHandler.NewMessage(g_MessageSpeed);
        Boss.Create(0, g_PlayNumber);
        
        // - The scrolling logo
        g_Logo.Init(65, 5, 3);
        g_Logo.Pos.x = 224F;
        g_Logo.Pos.y = 224F;
        g_Logo.Looping = true;
		
        // - The main menu cursor
        g_cursor.Init(196, 20, 3);
        g_cursor.Looping = true;
        g_cursor.Pos.x = 200F;
        g_cursor.Pos.y = 208F;

        // - Pre-Setup the fly-off transition.
        setupFlyTransition();
        g_DisplayStrings = GameScript.setScript(g_MaxLevels);

        for(int i = 0; i < g_OptionsStrings.length; i++)
        {
            g_OptionsStrings[i] = "Void menu";
            g_OptionsValues[i] = g_OptionsUpperLimits[i] = g_OptionsLowerLimits[i] = 0;
            g_OptionsEnabled[i] = false;
        }

        g_OptionsStrings[0] = "Text Speed:";
        g_OptionsLowerLimits[0] = 2;
        g_OptionsUpperLimits[0] = 30;
        g_OptionsEnabled[0] = true;
        g_OptionsValues[0] = 15;
        g_OptionsStrings[1] = "Exit";
    }

    /**
    *	@fn LoadSFX
    *	@brief Load the sfx into the game.
    */
    public void LoadSFX() 
    {
        //URL cb = getDataDir();
        //------------Make sure to alter this value when adding new sfx.
        g_sfxMaxLoadStage = g_NumSFX = 20;
        
        String fn = "";
        
        switch( g_sfxLoadStage ) 
        {
            case 0:
                    fn = "GetItem.mp3";
            break;
            case 1:
                    fn = "Muny.mp3";
            break;
            case 2:
                    fn = "explode.mp3";
            break;
            case 3:
                    fn = "GetSomething.mp3";
            break;
            case 4:
                    fn = "Laser4.mp3";// -----------Need to make a laser4.wav file
            break;
            case 5:
                    fn = "Clicik.mp3";
            break;
            case 6:
                    fn = "Shiphit1.mp3";
            break;
            case 7:
                    fn = "ok01.mp3";
            break;
            case 8:
                    fn = "ok02.mp3";
            break;
            case 9:
                    fn = "ok03.mp3";
            break;
            case 10:
                    fn = "End.mp3";
            break;
            case 11:
                    fn = "HiPitch.mp3";
            break;
            case 12:
                    fn = "Special.mp3";
            break;
            case 13:
                    fn = "ShieldHit.mp3";
            break;
            case 14:
                    fn = "gamestart.mp3";
            break;
            case 15:
                    fn = "Long_Laser.mp3";
            break;
            case 16:
                    fn = "BossDie.mp3";
            break;
            case 17:
                    fn = "BossAppear.mp3";
            break;
            case 18:
                    fn = "Beep01.mp3";
            break;
            case 19:
                    fn = "Banging_Sound_Hit.mp3";
            break;
            case 20:
                    //SoundEffects[18] = new MP3("Data/Music/End.mp3");
            break;
        }
        
        
        SoundEffects[g_sfxLoadStage] = new MP3(fn);
        
        g_sfxLoadStage++;
    }

    // - Swap a RGBA to an ARGB image.
    public void MakeRGBA_ARGB(BufferedImage img)
    {
        for(int iY=0; iY<img.getHeight(); ++iY)
        {
            for(int iX=0; iX<img.getWidth(); ++iX)
            {
                int rgb = img.getRGB(iX,iY);
                int a = (rgb>>0)&0x000000FF;
                int b = (rgb>>8)&0x000000FF;
                int g = (rgb>>16)&0x000000FF;
                int r = (rgb>>24)&0x000000FF;
                
               // if(r==255 && b==255)
                //    a=0;
                
                //int rgb2 = ((a<<24)&0xFF000000)|((r<<16)&0x00FF0000)|((g<<8)&0x0000FF00)|((b<<0)&0x000000FF);
                int rgb2 = ((r<<24)&0xFF000000)|((g<<16)&0x00FF0000)|((b<<8)&0x0000FF00)|((a<<0)&0x000000FF);
                
                img.setRGB(iX,iY,rgb2);
              
            }
        }
        BufferedImage x = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        x.getGraphics().drawImage(img, 0, 0, null);

        SpriteMap = x;
    }
    /**
    *	@fn LoadImages()
    *	@brief Load images.
    */
    private Image loadImage(String fileName){
        Image img = null;
        try {
            URL url = dynamo.data.Loader.getResourceURL(fileName);
            if(url==null){
                System.out.println("Null URL - Failed to load image " + fileName);
            }
            else {
                img = ImageIO.read(url);
            }
        }
        catch( IOException e) {
            System.out.println("Failed to load image " + fileName + " exception:\r\n" + e.toString());
        }        

        return img;
    }
    public void LoadImages()
    {
        //URL cb = getDataDir();
        //String x = cb+"data/Images/background.gif";
        //Backgrounds[0] = ImageIO.read(new File(x));
        Backgrounds[1] = loadImage( "Atmosphere.gif");
        Backgrounds[2] = loadImage( "Gamymede.gif");
        Backgrounds[3] = loadImage( "Titan.gif");
        Backgrounds[4] = loadImage( "IO.gif");
        Backgrounds[5] = loadImage( "Earth.gif");
        Backgrounds[6] = loadImage( "Moon.gif");
        Backgrounds[7] = loadImage( "End.gif");
        Backgrounds[8] = loadImage( "BkgGradient.gif");
        Backgrounds[9] = loadImage( "TransparentClouds.gif");
        Backgrounds[10] = loadImage( "Special.gif");
        SpriteMap = (BufferedImage)loadImage("ship_Main.png");

        setBossVariables();

        setUpgradeStrings();
    }


    // - Precompute the cache
    public void RunCache() 
    { 
        Factorial.precompute(12);
    }

    /**
    *	@fn stop()	
    *	@brief Stop the applet.
    */
    public void stop()
    {
        for( int i=0; i<SoundEffects.length; ++i )
            SoundEffects[i].stop();
    }

    /**
    *	@fn destroy()
    *	@brief Destroy the applet ( teardown ).
    */
    public void destroy()
    {
        for( int i=0; i<SoundEffects.length; ++i )
            SoundEffects[i].stop();	
    }

    /**
    *	@fn run()	
    *	@brief The main loop for the applet.
    *
    */
    @Override
    public void run()
    {
        //###############################
        // 	MAIN GAME LOOP
        //###############################
        thread.setPriority(1);
        requestFocus();
        do
        {
            //###################
            // - Begin frame poll
            if( g_Debug )
                    g_FrameChecker.framePollBegin();

            // if( hasFocus() )
            // {
                    // if( lastHadFocus = false )	// - we just got focus back, unpause the game.
                            // g_Paused = false;

                    // lastHadFocus = true;
            // }
            // else
            // {
                    // lastHadFocus = false;
                    // g_Paused = true;
            // }

            if( !hasFocus() )
                    lastHadFocus = false;
            else
                    lastHadFocus = true;

            if( thread.isAlive() ) {
                    try {
                            thread.sleep(g_ThreadSleepTime);
                    }
                    catch(InterruptedException ex) { 
                            ErrorMsg("When Thread.sleep");
                    }
                    //thread.yield();
            }

            //
            //	Go through the game states one-by one and perform the update.
            //

            if( !g_Media_Loaded )
            {
                    // - Initially start at zero, so we can go through and paint the loading message on the screen.
                    if( g_LoadState==1 ){
                            AllocateMem();
                            g_LoadProgress=0.1F;
                    }
                    else if( g_LoadState==2 ){
                        RunCache();
                            g_LoadProgress=0.15F;
                    }
                    else if( g_LoadState==3 ){
                        LoadImages();
                        g_LoadProgress=0.55F;
                    }
                    else if( g_LoadState==4 ){
                        LoadSFX();	// - Loads the SFX individually
                            g_LoadProgress = 0.55f + ((float)g_sfxLoadStage*0.45f/(float)g_sfxMaxLoadStage);
                    }
            }
            else if( g_Paused )
            {				
                    evaluateHacks();
            }
            else if(!g_MainMenu && !g_Intro)
            {
                // - We're in the game loop
                Backdrop_Pos.y += Backdrop_Speed;	// - Old for the old gif background.
                updateStars();						// - Now I use a real starfield.

                if((double)Backdrop_Pos.y > Room_Height)
                    Backdrop_Pos.y = 0.0F;				
                    evaluateHacks();

                if(!g_Transitioning && !g_EndCredits && !g_EndSequence)
                {
                    // - It's the main game loop

                    // - Gather User input, and update the player ship.

                    // - The player can only go outside of the room, when the level-end message is over.
                    if(!g_EndOfLevelAnimation)
                    {
                        Vel.x=0.0F;
                        Vel.y=0.0f;

                        if( LEFT_down )
                                Vel.x = -Player.Speed.x;
                        else if( RIGHT_down )
                                Vel.x = Player.Speed.x;

                        if( UP_down )
                                Vel.y = -Player.Speed.y;
                        else if( DOWN_down )
                                Vel.y = Player.Speed.y;

                        Player.move( Vel );
                        Player.CheckIfOutside(Room_Width, Room_Height);
                        //Player.Wrap(Room_Width, Room_Height);
                        if( SPACE_down || CONTROL_down )
                        {
                                Player.AddMissile(0.0F, -18F, 0);
                                playSound(SoundEffects[4]);
                        }
                    }
					
                    // - Decrement the missile delay time (the time it takes to make another missile)
                    if( !SPACE_down && !CONTROL_down )
                        Player.MissileDelay -= 3;

                    Player.Update();
                    PowerUpHandler.Update();
					
                    if(g_CrazyParticles)
                        particleHandler.crazyparticles = true;
					
                    // --- Update Particles
                    particleHandler.Update();
                    
                    // --- Update Dodads
                    dodadController.Update(Player.Pos);

					// --- Update Explosions
                    for(int i = 0; i < Explosions.length; i++)
                        if(Explosions[i].Active)
                            Explosions[i].Update();

                    // --- Update the health blinker
                    updateHealthBlinker();
					
                    if(!g_BossMode)
                    {
                        // - Check to see if we should be transitioning to the next level
                        checkLevelTimer();

                        // - Every 500*Difficulty points, you get a health bonus.
                        if( g_Score > (500 * (g_PlayNumber + 1) * (g_HealthUp + g_Level - 1) )  )
                        {
                            g_PopMessageString = 500 * (g_PlayNumber + 1) * g_HealthUp + " Points\nHealth Bonus!";
                            g_PopMessageTimer = g_MaxPopMessageTimer;
                            Player.Health = Player.MaxHealth;
                            g_HealthUp++;
                        }

                        // - Update the enemies, returning any accumulated score in the process.
                        addScore(enemyCreator.Update(Player, particleHandler, PowerUpHandler, g_Level, g_PlayNumber, SoundEffects[2], g_UsingSFX));

                        checkShipCollisions();
                        checkMissileCollisions();
						
                    } //- if !bossmode
                    else if(Boss.Update(Player, particleHandler, SoundEffects[3], PowerUpHandler, SoundEffects[0], g_UsingSFX))
                    {
                        // - BOSS DEATH
                        hideAllEnemiesMissilesDodadsAndExplosions();
                        g_BossMode = false;
                        if(g_Level < SPECIAL_LEVEL_CAP)
                        {
                            g_EndCredits = true;
                            if( SoundEffects[SONG_END] != null )
                                    SoundEffects[SONG_END].loop();
                        }
                        else
                            showMainMenu();
                    }
                } 
                else if( g_Transitioning )
                {
                        // - End of level transition.
                        g_TransitionLinePos += 25F;
                }
                else if( g_EndSequence )
                {
                    // - PLAYER DIED DEAD
                    // - The end sequence ( player died ) is being played.
                    if(Player.Pos.y < 550F)
                    {
                        Player.Speed.y += Player.Gravity.y;
                        Player.Pos.y += Player.Speed.y;
                        Player.Pos.x += Player.Speed.x;
                    }

                    SoundEffects[15].stop();

                    Player.Pos.y += -1+Math.abs(RAND.nextInt(2));
                    g_EndTimer--;

                    VECTOR2 rPOS = new VECTOR2();

                    rPOS.x = (Player.Pos.x-32F+(float)Math.abs(RAND.nextInt(64)));
                    rPOS.y = (Player.Pos.y-32F+(float)Math.abs(RAND.nextInt(64)));
                    int n = 4+RAND.nextInt(3);

                    for(int k = 0; k < PowerUpHandler.PowerUps.length; k++)
                    {
                        if(!PowerUpHandler.PowerUps[k].Active)
                        {
                            PowerUpHandler.PowerUps[k].Active = true;
                            PowerUpHandler.PowerUps[k].Create(23);
                            PowerUpHandler.PowerUps[k].Pos.x = rPOS.x;
                            PowerUpHandler.PowerUps[k].Pos.y = rPOS.y;
                            if((--n)==0)break;
                        }
                    }
                    PowerUpHandler.Update();	// - The update loop doesn't run when player died, so this needs to be called for the explosions.

                    if(Player.Pos.y > 550F && g_EndTimer <= 0)
                    {
                        showMainMenu();
                        g_EndSequence = false;
                        g_BossMode = false;
                        KellyMarie.Enabled = false;
                        g_EndTimer = g_MaxEndTimer;
                        hideAllEnemiesMissilesDodadsAndExplosions();
                        g_ContinueMode = true;
                        g_cursor.Pos.y = 208F-15F;
                    }
                }
				
                //###########################
                // - End of level animation.	findme
                if(g_EndOfLevelAnimation)
                {
                    if( g_Level == FLY_TRANSITION_LEVEL )
                    {
                        // - Fly off animation
                        if( (flyShip_T >= 1.0F) && (flyCushion_T >= 1.0F) )
                        {
                            //224 + 5
                            Player.Tile_Current = 224 + (int)Math.floor( flyMotion_T * 6 );
                            if(!g_PlayEndSound)
                            {
                                    playSound(SoundEffects[8]);
                                    g_PlayEndSound = true;
                            }
                            if( flyMotion_T >= 1.0f )
                            {
                                Player.Tile_Current = 230 + (int)Math.floor(flyShine_T*5);
                                if( flyShine_T >=1.0f )
                                {
                                    //
                                    // - End of the animation.
                                    ///
                                    MessageHandler.Timer--;
                                    if(MessageHandler.Timer <= 0)	// - should not be -1
                                    {
                                        MessageHandler.NewMessage(g_MessageSpeed);	// reset message handler
                                        g_CurMessageIndex = 0;
                                        g_Transitioning = true;
                                        g_TransitionLinePos = 0.0F;
                                        g_EndOfLevelAnimation = false;
                                        Player.Pos.y = Room_Height;
                                        g_TempYSPD = 0.0F;
                                        g_PlayEndSound = false;
                                        hideAllEnemiesMissilesDodadsAndExplosions();
                                    }
                                }
                                else
                                {
                                    flyShine_T+=0.1f;
                                    if( flyShine_T > 1.0f )
                                        flyShine_T = 1.0f;	
                                }
                            }
                            else
                            {
                                flyMotion_T+=0.02f;
                                if( flyMotion_T > 1.0f )
                                    flyMotion_T = 1.0f;

                                // - Tween the position of the ship in the animation with a bezier curve.
                                Player.Pos = VECTOR2.bezierD4( flyAnimKeys, flyMotion_T );
                            }
                        }
                        else
                        {
                            // - Pincushion
                            Player.warpAnimation(flyShip_T);
                            KellyMarie.warpAnimation(flyShip_T);
                            flyCushion_T+=0.008F;
                            if( flyCushion_T > 1.0F )
                                    flyCushion_T = 1.0F;

                            flyShip_T+=0.006F;
                        }

                        // - Animate the clouds.
                        flyCloud_T+=0.005F;
                        if( flyCloud_T > 1.0F )
                            flyCloud_T = 0.0F;

                    }
                    else	
                    // - Basic end sequence.
                    {
                        if(!g_PlayEndSound)
                        {
                            playSound(SoundEffects[8]);
                            g_PlayEndSound = true;
                        }

                        g_TempYSPD += -0.1F;
                        Player.Pos.y += g_TempYSPD;

                        if(KellyMarie.Enabled && g_Level != 7)
                            KellyMarie.Pos.y += g_TempYSPD;

                        ///
                        MessageHandler.Timer--;

                        if(MessageHandler.Timer <= 0)	// - should not be -1
                        {
                            MessageHandler.NewMessage(g_MessageSpeed);	// reset message handler
                            g_CurMessageIndex = 0;
                            g_Transitioning = true;
                            g_TransitionLinePos = 0.0F;
                            g_EndOfLevelAnimation = false;
                            Player.Pos.y = Room_Height;
                            g_TempYSPD = 0.0F;
                            g_PlayEndSound = false;
                        }
                    }
                }
				
                //##############################
                // - POWERUP COLLISION DETECTION
                if( !g_EndSequence )
                    collideWithPowerups();
				
                //#############################
                // - Check if player has died
                if( Player.Health <= 0 && !g_EndSequence && ( Player.Pos.y < 550F)  )
                {
                    g_EndSequence = true;
                    Player.saveSpeed.equals(Player.Speed);
					
                    _saved_playerspeed = Player.Speed;
                    Player.Speed.x = -1F;
                    Player.Speed.y = -12F;
                    Player.Gravity.y = 0.5F;
                    if(!g_PlayEndSound)
                    {
                        g_PlayEndSound = true;
                        playSound(SoundEffects[10]);
                    }
                }
            }// if !g_mainMenu && ! g_intro && !g_Paused
            else
            {
                // - Main Menu or intro..
                Backdrop_Pos.y += Backdrop_Speed;	// 0ld
                updateStars();						// n3w

                if((double)Backdrop_Pos.y > Room_Height)
                    Backdrop_Pos.y = 0.0F;				
                    evaluateHacks();
            }
            //repaint(0); // callls Update()
                
            Graphics mcg = _mainCanvas.getGraphics();
            update(mcg);
            mcg.dispose();

            //###################
            // - End frame poll
            if( g_Debug )
            {
                g_FrameChecker.framePollEnd();
            }

        }while( true );
    }
	
    Graphics _saved_Main_g_from_update;
    /**
    *	@fn update()
    *	@brief Repaints the screen.
    */
    public void update(Graphics main_g)
    {
        _saved_Main_g_from_update = main_g;
        Graphics g = Back_Buffer.getGraphics();
        

        if(!g_Transitioning && !g_Paused){	// - We don't want to wipe the screen during transition.
            Back_Graphics.setColor(Color.BLACK);
            Back_Graphics.fillRect(0, 0, _mainCanvas.getSize().width, _mainCanvas.getSize().height);
        }


        if( !g_Media_Loaded )
        {
            Back_Graphics.setColor( Color.WHITE );
            renderStars(Back_Graphics);
            updateStars();
            String out="Loading: ";
            if( g_LoadState==0 ) // - Initially start at zero, so we can go through and paint the loading message on the screen.
            {
                g_LoadState++;
                out+="Allocating Memory";
            }
            else if( g_LoadState==1 )
            {
                g_LoadState++;
                out+="Precomputing Cache";
            }
            else if( g_LoadState==2 )
            {
                g_LoadState++;
                out+="Images";
            }
            else if( g_LoadState==3 )
            {
                g_LoadState++;
                out+="Sound 1";
            }
            else if( g_LoadState==4 )
            {
                if( g_sfxLoadStage == g_sfxMaxLoadStage )
                {
                    g_Media_Loaded=true;
                    g_LoadState=-1;
                    out+="Starting..";
                }
                else 
                {
                    out+="Sound "+Integer.toString(g_sfxLoadStage+1);
                }
            }
            else
                out+="Error.. (" + g_LoadState + ")";


            drawBevelString( out ,(int)Room_Width / 2 - 80, (int)Room_Height / 2, Back_Graphics );
            Back_Graphics.setColor( Color.RED );
            Back_Graphics.fillRect( (int)Room_Width / 2 - 80, (int)Room_Height/2 + 20, (int)((float)120F*g_LoadProgress), 10 );
            Back_Graphics.setColor( Color.WHITE );
            Back_Graphics.drawRect( (int)Room_Width / 2 - 80, (int)Room_Height/2 + 20, 120, 10 );
        }
        else if( g_Paused )
        {
            Back_Graphics.setColor( Color.WHITE );
            drawBevelString( "PAUSED" ,(int)Room_Width / 2 - 20, (int)Room_Height / 2, Back_Graphics );
        }	
        else 
        { 
            if(!g_Transitioning)
            {
                //####################
                // - RENDER THE SCENE
                draw(Back_Graphics);

            } 
            else if(g_TransitionLinePos >= (float)Room_Height+16)
            {
                //#######################################
                // - Reset all variables for a new level.
                g_TransitionLinePos = 0.0f;

                g_Transitioning = false;
                g_Level++;

                enemyCreator.CanUpdate = true;
                enemyCreator.CanMakeEnemies = true;
                enemyCreator.EnemyTimer = enemyCreator.MaxEnemyTimer;

                if(g_Level == 6 || g_Level == 7)
                {
                    KellyMarie.Enabled = true;
                    KellyMarie.init(12, 2, 4);
                    KellyMarie.Pos.x = 180F;
                    KellyMarie.Pos.y = 480F;
                    KellyMarie.Speed.x = 4.2F;
                } 
                else
                {
                    KellyMarie.Enabled = false;
                }

                for(int i = 0; i < PowerUpHandler.PowerUps.length; i++)
                    PowerUpHandler.PowerUps[i].Active = false;

                hideAllEnemiesMissilesDodadsAndExplosions();

            } 
            else
            {
                // - Draw the transition line
                Back_Graphics.setColor(Color.BLACK);
                //Back_Graphics.fillRect(0, 0, Room_Width, (int)g_TransitionLinePos);
                Back_Graphics.fillRect( 0, (int)(Room_Height-g_TransitionLinePos), Room_Width, (int)(Room_Height - ( Room_Height-g_TransitionLinePos )) ); 
            }
            //DebugArea			
            if( g_Debug )
            {

                Back_Graphics.setColor(Color.RED);
                for( int i=0; i<FLY_NUM_KEYS; ++i )
                {
                    Back_Graphics.drawRect( (int)flyAnimKeys[i].x, (int)flyAnimKeys[i].y, 2, 2 );
                }	
                if( !g_EndOfLevelAnimation )
                    setupFlyTransition();
                VECTOR2 p;
                VECTOR2 lastp=null;

                // - Let's see what the bezier path is
                for( float i=0.0F; i<1.0F; i+=0.01 )
                {
                    p = VECTOR2.bezierD4( flyAnimKeys, i );
                    if( lastp!=null )
                    {
                        Back_Graphics.drawLine((int)p.x,(int)p.y,(int)lastp.x,(int)lastp.y);
                    }
                    lastp=p;
                }

                Back_Graphics.setColor( Color.WHITE );
                Back_Graphics.drawString( "DEBUG" ,Room_Width-50, 15 );
                int x=-2;

                if( g_Show_Debug_Variables )
                {
                    Back_Graphics.drawString( "FPSAVG: " + Double.toString(g_FrameChecker.getAvgRate() ) 											,2, (int)Room_Height +(x));
                    Back_Graphics.drawString( "FPS: " + Double.toString(g_FrameChecker.getRate() ) 												,2, (int)Room_Height +(x-=13));
                    //Back_Graphics.drawString( "TLP: " + Float.toString(g_TransitionLinePos) 													,2, (int)Room_Height +(x-=13));	
                    Back_Graphics.drawString( "Time To Next Lv: " + Integer.toString(g_TimeToNextLevel) 										,2, (int)Room_Height +(x-=13));	
                    //Back_Graphics.drawString( "g_BossMode: " + Boolean.toString(g_BossMode) 													,2, (int)Room_Height +(x-=13));	
                    Back_Graphics.drawString( "Num_Enemies: " + Integer.toString(enemyCreator.Num_Enemies) 										,2, (int)Room_Height +(x-=13));	
                    Back_Graphics.drawString( "Player.Speed: " + Float.toString(Player.Speed.x) + "," + Float.toString(Player.Speed.y) 	  	,2, (int)Room_Height +(x-=13));	
                    Back_Graphics.drawString( "Player.saveSpeed: " + Float.toString(Player.saveSpeed.x) + "," + Float.toString(Player.saveSpeed.y) 	  	,2, (int)Room_Height +(x-=13));	
                    //Back_Graphics.drawString( "Player.lastVelocity: " + Float.toString(Player.lastVelocity.x) + "," + Float.toString(Player.lastVelocity.y) 	,2, (int)Room_Height +(x-=13));
                    //Back_Graphics.drawString( "g_BlinkRatio: " + Float.toString(g_BlinkRatio) 													,2, (int)Room_Height +(x-=13));	
                    //Back_Graphics.drawString( "Mexico Str: " + g_MexicoPrintStr 													,2, (int)Room_Height +(x-=13));	
                    Back_Graphics.drawString( "Boss_LaserHP: " + Integer.toString(Boss.Parts[Boss.PART_LASER].Health)					,2, (int)Room_Height +(x-=13));	
                    Back_Graphics.drawString( "Boss_LWingHP: " + Integer.toString(Boss.Parts[Boss.PART_LWING].Health)					,2, (int)Room_Height +(x-=13));	
                    Back_Graphics.drawString( "Boss_RWingHP: " + Integer.toString(Boss.Parts[Boss.PART_RWING].Health)					,2, (int)Room_Height +(x-=13));	
                    Back_Graphics.drawString( "Boss_BodyHP: " + Integer.toString(Boss.Parts[Boss.PART_BODY].Health)					,2, (int)Room_Height +(x-=13));	
                    Back_Graphics.drawString( "Using Sounds: " + Boolean.toString(g_UsingSFX)					,2, (int)Room_Height +(x-=13));	


                    Back_Graphics.drawString( "Version: " + Float.toString(g_Version) 															,2, (int)Room_Height +(x-=13));	

                }

                Back_Graphics.setColor( Color.GREEN );				
                // - Render all of the boundaries
                // player
                Back_Graphics.drawOval( (int)(Player.Pos.x-Player.boundCircle.getBoundRadius()), (int)(Player.Pos.y-Player.boundCircle.getBoundRadius()), (int)(Player.boundCircle.getBoundRadius()*2F), (int)(Player.boundCircle.getBoundRadius()*2F) );
                Back_Graphics.drawRect( (int) Player.Pos.x, (int)Player.Pos.y,1,1);

                //enemies
                for( int i=0 ;i<enemyCreator.Enemies.length; ++i )
                {
                    if( enemyCreator.Enemies[i].InUse )
                    {
                        Back_Graphics.drawOval( (int)(enemyCreator.Enemies[i].Pos.x-enemyCreator.Enemies[i].boundCircle.getBoundRadius()), (int)(enemyCreator.Enemies[i].Pos.y-enemyCreator.Enemies[i].boundCircle.getBoundRadius()),(int)(enemyCreator.Enemies[i].boundCircle.getBoundRadius()*2F), (int)(enemyCreator.Enemies[i].boundCircle.getBoundRadius()*2F) );
                        Back_Graphics.drawRect( (int) enemyCreator.Enemies[i].Pos.x, (int)enemyCreator.Enemies[i].Pos.y,1,1);
                    }
                }
                //powerups
                for( int i=0 ;i<PowerUpHandler.PowerUps.length; ++i )
                {
                    if( PowerUpHandler.PowerUps[i].Active )
                    {
                        Back_Graphics.drawOval( (int)(PowerUpHandler.PowerUps[i].Pos.x-PowerUpHandler.PowerUps[i].boundCircle.getBoundRadius()), (int)(PowerUpHandler.PowerUps[i].Pos.y-PowerUpHandler.PowerUps[i].boundCircle.getBoundRadius()),(int)(PowerUpHandler.PowerUps[i].boundCircle.getBoundRadius()*2F), (int)(PowerUpHandler.PowerUps[i].boundCircle.getBoundRadius()*2F) );
                        Back_Graphics.drawRect( (int) PowerUpHandler.PowerUps[i].Pos.x, (int)PowerUpHandler.PowerUps[i].Pos.y,1,1);
                    }
                }	

                //dodads
                for( int i=0; i<dodadController.Dodads.length; ++i )
                {
                    if( dodadController.Dodads[i].Active )
                    {
                        Back_Graphics.drawOval( (int)(dodadController.Dodads[i].Pos.x-dodadController.Dodads[i].boundCircle.getBoundRadius()), (int)(dodadController.Dodads[i].Pos.y-dodadController.Dodads[i].boundCircle.getBoundRadius()),(int)(dodadController.Dodads[i].boundCircle.getBoundRadius()*2F), (int)(dodadController.Dodads[i].boundCircle.getBoundRadius()*2F) );
                        Back_Graphics.drawRect( (int) dodadController.Dodads[i].Pos.x, (int)dodadController.Dodads[i].Pos.y,1,1);
                    }
                }	

                //player missiles
                for( int i=0 ;i<Player.Missiles.length; ++i )
                {
                    if( Player.Missiles[i].InUse )
                    {
                        Back_Graphics.drawOval( (int)(Player.Missiles[i].Pos.x-Player.Missiles[i].boundCircle.getBoundRadius()), (int)(Player.Missiles[i].Pos.y-Player.Missiles[i].boundCircle.getBoundRadius()),(int)(Player.Missiles[i].boundCircle.getBoundRadius()*2F), (int)(Player.Missiles[i].boundCircle.getBoundRadius()*2F) );
                        Back_Graphics.drawRect( (int) Player.Missiles[i].Pos.x, (int)Player.Missiles[i].Pos.y,1,1);
                    }
                }	

                // //dodads
                for( int i=0; i<enemyCreator.Missiles.length; ++i )
                {
                    if( enemyCreator.Missiles[i].InUse )
                    {
                        Back_Graphics.drawOval( (int)(enemyCreator.Missiles[i].Pos.x-enemyCreator.Missiles[i].boundCircle.getBoundRadius()), (int)(enemyCreator.Missiles[i].Pos.y-enemyCreator.Missiles[i].boundCircle.getBoundRadius()),(int)(enemyCreator.Missiles[i].boundCircle.getBoundRadius()*2F), (int)(enemyCreator.Missiles[i].boundCircle.getBoundRadius()*2F) );
                        Back_Graphics.drawRect( (int) enemyCreator.Missiles[i].Pos.x, (int)enemyCreator.Missiles[i].Pos.y,1,1);
                    }
                }	
            }
        }

        // - Draw to main graphics
        main_g.drawImage(Back_Buffer, 0, 0, Screen_Width, Screen_Height, null); 
    }

    /**
    *	@fn draw()
    *	@brief Renders the scene and everything in it.
    *	Most items in the scene have been split up into utility functions.
    */
    public void draw(Graphics g)
    {
        
        g.clipRect(0, -g_ConsoleHeight, Room_Width, Room_Height+g_ConsoleHeight*2);/* 32 : due to objects going off the screen */
       
        if( g_Intro || g_MainMenu )
        {
            // - RENDER THE STARFIELD
            renderStars(g);
        }
		
        if(g_Intro)
        {
            if(g_IntroTimer0 > 0)
            {
                // - Draw the beginning logo.
                g_IntroTimer0--;
                g_Logo.Update();
                g.setColor(Color.WHITE);
                DrawTile(Back_Buffer, SpriteMap, (int)g_Logo.Pos.x, (int)g_Logo.Pos.y, 1, 1, g_Logo.Tile_Cur, 14);
                drawBevelString("Copyright (C) 2007 Derek Page", 150, 280,g);
            } 
            else if(g_IntroTimer1 > 0)
            {
                g_IntroTimer1--;
                g_IntroPos.x -= 7.1F;
                for(int i = 0; i < 3; i++)
                {
                    int CurY = (102 + i) / 14;
                    int CurX = (102 + i) % 14;
                    CurX = CurX * 1 + 1 + CurX * 32;
                    CurY = CurY * 1 + 1 + CurY * 32;
                    g.drawImage(SpriteMap, 
                            (int)g_IntroPos.x + Room_Width * i, 
                            (int)g_IntroPos.y, 
                            (int)g_IntroPos.x + Room_Width + Room_Width * i,
                            (int)g_IntroPos.y + Room_Height, CurX, CurY, CurX + 32, CurY + 32, null);
                }
            } 
            else
            {
                g_Intro = false;
                showMainMenu();
            }
        }
        else if( g_MainMenu )
        {
            if( g_Story )
            {
                //#################
                // - STORY
                g.setColor(Color.WHITE);
                g_StoryPos.y -= g_StoryPosSpeed;
                String Temp = "";
                int jjj = 0;
                for(int i = 0; i < g_StoryString.length(); i++)
                {
                    if(g_StoryString.charAt(i) == '\n')
                    {
                        g.drawString(Temp, (int)g_StoryPos.x, (int)g_StoryPos.y + jjj);
                        Temp = "";
                        jjj += 20;
                    } 
                                        else
                    Temp = Temp + g_StoryString.charAt(i);
                }
            } 
            else if(g_OptionsMenu) 
            {
                //#################
                // - OPTIONS MENU
                g.setColor(Color.WHITE);
                int TempCur = 0;
            } 
            else
            {
                //#################
                // - MAIN MENU
                g_StarSpeed.y = 2.0F;
                g.setColor(new Color(180, 200,255));

                if( g_ContinueMode )
                {
                    _menu.enableItemByName(_menuitem_continue);
                }
                else
                {
                    _menu.disableItemByName(_menuitem_continue);
                }
  
                if(g_PlayNumber == 0)
                {
                    _menu.enableItemByName(_menuitem_newgame);
                    _menu.changeItemTextByName(_menuitem_newgame,"New game");
                }
                else if(g_PlayNumber == 1)
                {
                    _menu.enableItemByName(_menuitem_newgame);
                    _menu.changeItemTextByName(_menuitem_newgame,"New game - Hard");
                }
                else if(g_PlayNumber > 1)
                {
                    String rn;
                    switch( g_PlayNumber )
                    {
                        case 2: rn="II"; break;
                        case 3: rn="III"; break;
                        case 4: rn="IV"; break;
                        case 5: rn="V"; break;
                        case 6: rn="VI"; break;
                        case 7: rn="VII"; break;
                        case 8: rn="VIII"; break;
                        case 9: rn="IX"; break;
                        case 10: rn="X"; break;
                        case 11: rn="DANOS"; break;
                        case 12: rn="OGMIOS"; break;
                        case 13: rn="FORSETI"; break;
                        default: rn="YOU ROCK!"; break;
                    }
                    _menu.enableItemByName(_menuitem_newgame);
                    _menu.changeItemTextByName(_menuitem_newgame,"New game - Hard " + rn);
                }

                _menu.enableItemByName(_menuitem_story);

                for(int i = 0; i < 6; i++)
                {
                    DrawTile(Back_Buffer, SpriteMap, 74 + i * 64, 0, 1, 1, 168 + i, 14);
                }
                g_IntroWait--;
                if(g_IntroWait <= 0)
                {
                    g_Intro = true;
                    g_MainMenu = false;
                    g_IntroWait = g_MaxIntroWait;
                    g_IntroTimer0 = g_MaxIntroTimer0;
                    g_IntroTimer1 = g_MaxIntroTimer1;
                    g_IntroPos.x = Room_Width;
                }
                if(g_Score > g_SpecialScore)
                {
                    _menu.enableItemByName(_menuitem_special);
                    _menu.enableItemByName(_menuitem_options);
                }
                else
                {
                    _menu.enableItemByName(_menuitem_options);
                }
                //DrawTile(Back_Buffer, SpriteMap, (int)g_cursor.Pos.x, (int)g_cursor.Pos.y, 1, 1, g_cursor.Tile_Cur, 14);
                //g_cursor.Update();
            }
            
            //***RENDER MENU, DRAW MENU
            _menu.render(g);
            VECTOR2 cursorPos = _menu.getCursorPosition();
            DrawTile(Back_Buffer, SpriteMap, (int)cursorPos.x, (int)cursorPos.y, 1, 1, g_cursor.Tile_Cur, 14);
            
            g.setColor(Color.BLACK);
            g.fillRect(0, Room_Width, Room_Height, 32);
        } 
        else if( !g_Intro && ! g_MainMenu )
        {
            // - RENDER THE LEVEL BACKGROUND IMAGES
            if( g_EndOfLevelAnimation && (g_Level == FLY_TRANSITION_LEVEL) )
            {
                // - Render the scaled background in the ship fly off scene.

                float rh = (float)Room_Height;		
                float GroundPad = rh/10;
                // - Draw the Land.
                if( !g_flyStaticGround )
                {
                    float gPos = (rh-GroundPad) * flyCushion_T - 2F/*-2f is so there aren't any gaps.*/;	// - Top position of the ground area.
                    float myH = rh-gPos;			// - Height of the ground area rectangle.

                    int d1Y = (int) ( 
                        (float) (
                            gPos + (
                                Backdrop_Pos.y / rh * myH
                            )
                        )
                    );	
                    int d2Y = (int) ( 
                        (float) (
                            gPos + (
                                (Backdrop_Pos.y-rh) / rh * myH
                            )
                        )
                    );	

                    g.drawImage(Backgrounds[g_Level - 1], 
                            0, 
                            d1Y, 
                            Room_Width,//Backgrounds[g_Level-1].getWidth(null), 
                            (int)(myH+2F), 
                            null);
                    g.drawImage(Backgrounds[g_Level - 1], 
                            0, 
                            d2Y, 
                            Room_Width,//Backgrounds[g_Level-1].getWidth(null), 
                            (int)(myH+2F), 
                            null);

                    if( g_Debug )
                    {
                        g.setColor( Color.RED );
                        g.drawLine(	0, d1Y, Room_Width, d1Y );
                        g.drawLine( 0, d2Y, Room_Width, d2Y );
                    }

                }
                else
                {	
                        g.drawImage( Backgrounds[g_Level - 1], 0, (int)((float)Room_Height*flyCushion_T), Backgrounds[g_Level-1].getWidth(_mainCanvas),
                        (int)((float)Room_Height-(float)Room_Height*flyCushion_T), null);
                }
                int h = (int)(float)(flyCushion_T*(float)(rh - GroundPad));
                // - Draw the background gradient
                g.drawImage( Backgrounds[8], 0, 0, Backgrounds[8].getWidth(null), h, null );

                // - Draw the moving clouds
                g.drawImage( Backgrounds[9], (int)( Room_Width * (flyCloud_T) ), 0, Backgrounds[9].getWidth(null), h, null );
                g.drawImage( Backgrounds[9], (int)( Room_Width * (flyCloud_T) )-Room_Width, 0, Backgrounds[9].getWidth(null), h, null );

                blitGradient(g);

            }
            // - RENDER THE LEVEL BACKGROUND IMAGE
            else if(g_Level < SPECIAL_LEVEL_CAP)
            {
                if( g_Level <=1 )
                    renderStarsPlain(g);
                else 
                {
                    g.drawImage(Backgrounds[g_Level - 1], 
                            0, 
                            (int)Backdrop_Pos.y - Room_Height,
                            Room_Width,
                            Room_Height,
                            null);
                    g.drawImage(Backgrounds[g_Level - 1], 
                            0, 
                            (int)Backdrop_Pos.y, 
                            Room_Width,
                            Room_Height,
                            null);
                }
            }
            else
            {
                g.drawImage(Backgrounds[SPECIAL_BACKGROUND_INDEX], 0, (int)Backdrop_Pos.y - Room_Height, null);
                g.drawImage(Backgrounds[SPECIAL_BACKGROUND_INDEX], 0, (int)Backdrop_Pos.y, null);
            } 

            // - RENDER PLAYER SHIP
            DrawTile(Back_Buffer, SpriteMap, (int)Player.Pos.x-16, (int)Player.Pos.y-16, 1, 1, Player.Tile_Current, 14);

            if(!g_EndCredits)
            {
                if( !( g_EndOfLevelAnimation && g_Level==FLY_TRANSITION_LEVEL ) )
                    // - RENDER ENEMIES
                    for(int i = 0; i < enemyCreator.Enemies.length; i++)
                        if(enemyCreator.Enemies[i].InUse)
                            DrawTile(Back_Buffer, SpriteMap, (int)enemyCreator.Enemies[i].Pos.x-16, (int)enemyCreator.Enemies[i].Pos.y-16, 1, 1, enemyCreator.Enemies[i].Tile_Current, 14);
				
				// - RENDER ENEMY MISSILES
                for(int i = 0; i < enemyCreator.Missiles.length; i++)
                    if(enemyCreator.Missiles[i].InUse)
                        DrawTile(Back_Buffer, SpriteMap, (int)enemyCreator.Missiles[i].Pos.x-16, (int)enemyCreator.Missiles[i].Pos.y-16, 1, 1, enemyCreator.Missiles[i].Img_Cur, 14);
				
				// - RENDER PLAYER MISSILES
                for(int i = 0; i < Player.Missiles.length; i++)
                {
                    if(Player.Missiles[i].InUse)
                    {
                        if( Player.Missiles[i].Type==0 )
                            DrawTile(Back_Buffer, SpriteMap, (int)Player.Missiles[i].Pos.x-16, (int)Player.Missiles[i].Pos.y-16, 1, 1, Player.Missiles[i].Img_Cur, 14);
                        else if( Player.Missiles[i].Type==1 )	// heat sink missiles (for kelly only)
                            DrawTile(Back_Buffer, SpriteMap, (int)Player.Missiles[i].Pos.x-16, (int)Player.Missiles[i].Pos.y-16, 1, 1, 232, 14);	// shiny
                    }
                }
				
		// - RENDER BOSS
                if(g_BossMode)
                {
                    for(int i = 0; i < Boss.Num_Parts; i++)
                        DrawTile(Back_Buffer, SpriteMap, (int)Boss.Parts[i].Pos.x-16, (int)Boss.Parts[i].Pos.y-16, 1, 1, Boss.Parts[i].Tile_Offset, 14);
                    
                    for(int i = 0; i < Boss.Beam.length; i++)
                    {
                        if(!Boss.Beam[i].InUse)
                            continue;
                        if(Player.CollideBox(Boss.Beam[i].BoundBox, Boss.Beam[i].Pos))
                            Player.Health -= Boss.Beam[i].Damage;// - This is flat damage, and always exists, it does not rely on Player.damage()
                        DrawTile(Back_Buffer, SpriteMap, (int)Boss.Beam[i].Pos.x, (int)Boss.Beam[i].Pos.y, 1, 1, Boss.Beam[i].Img_Off, 14);
                    }
					
                    for(int i = 0; i < Boss.Missiles.length; i++)
                    {
						if( !Boss.Missiles[i].InUse )
							continue;
						
                        if(Collideable.collide(
						Boss.Missiles[i].boundCircle, 
						Boss.Missiles[i].Pos, 
						Boss.Missiles[i].Speed,
						Player.boundCircle, 
						Player.Pos,
						Player.lastVelocity,
						temp_cPos ))
                        {
                            Player.damage(Boss.Missiles[i].Damage, g_Level);
                            Boss.Missiles[i].InUse = false;
                            particleHandler.Particles(10, Boss.Missiles[i].Pos, 0);
                        }
                        if(Boss.Missiles[i].InUse)
                            DrawTile(Back_Buffer, SpriteMap, (int)Boss.Missiles[i].Pos.x-16, (int)Boss.Missiles[i].Pos.y-16, 1, 1, Boss.Missiles[i].Img_Off, 14);
                    }
                }
            }	// if !g_EndCredits
			
            if( !( g_EndOfLevelAnimation && g_Level==FLY_TRANSITION_LEVEL ) )
            {
                // - RENDER POWERUPS 
                for(int i = 0; i < PowerUpHandler.PowerUps.length; i++)
                        if(PowerUpHandler.PowerUps[i].Active)
                                DrawTile(Back_Buffer, SpriteMap, (int)PowerUpHandler.PowerUps[i].Pos.x-16, (int)PowerUpHandler.PowerUps[i].Pos.y-16, 1, 1, PowerUpHandler.PowerUps[i].Cur_Img, 14);

                // - RENDER DODADS
                for(int i = 0; i < dodadController.Dodads.length; i++)
                        if(dodadController.Dodads[i].Active)
                                DrawTile(Back_Buffer, SpriteMap, (int)dodadController.Dodads[i].Pos.x-16, (int)dodadController.Dodads[i].Pos.y-16, 1, 1, dodadController.Dodads[i].Cur_Img, 14);
            }
            
            // - RENDER FRIEND SHIP
            if(KellyMarie.Enabled)
            {
                KellyMarie.Update(enemyCreator, Player);
                if( ! g_EndOfLevelAnimation ) 
					KellyMarie.UpdateAnimation();
                DrawTile(Back_Buffer, SpriteMap, (int)KellyMarie.Pos.x-16, (int)KellyMarie.Pos.y-16, 1, 1, KellyMarie.Tile_Current, 14);
            }
			
			// - REDNER PARTICLES
            renderParticles( g );
			
			// - RENDER EXPLOSIONS
            for(int i = 0; i < Explosions.length; i++)
                if(Explosions[i].Active)
                    DrawTile(Back_Buffer, SpriteMap, (int)Explosions[i].Pos.x, (int)Explosions[i].Pos.y, 1, 1, Explosions[i].Tile_Cur, 14);
			
			// - Render credits last.
            if(g_EndCredits)
            {
                // - Draw an alpha-blended square over the region, to signify the importance of the credits
                g.setColor( new Color(0.0F, 0.0F, 0.0F, 0.5F) );
                g.fillRect(0,0,Room_Width,Room_Height);
                g.setColor( Color.WHITE );
                boolean creditsDone = CreditHandler.RollCredits(g);
                
		if(!creditsDone)
                {
                    Player.Speed.y = 6F;
                    Player.Gravity.y = -0.3F;
                }
                else
                {
                    Player.Speed.y += Player.Gravity.y;
                    Player.Pos.y += Player.Speed.y;
                    if(KellyMarie.Enabled)
                        KellyMarie.Pos.y += Player.Speed.y;
                    if(Player.Pos.y < -60F)
                    {
                        for(int i = 0; i < enemyCreator.Enemies.length; i++)
                            enemyCreator.Enemies[i].InUse = false;

                        for(int i = 0; i < enemyCreator.Missiles.length; i++)
                            enemyCreator.Missiles[i].InUse = false;

                        for(int i = 0; i < Player.Missiles.length; i++)
                            Player.Missiles[i].InUse = false;

                        for(int i = 0; i < particleHandler.Active.length; i++)
                            particleHandler.Active[i] = false;

                        for(int i = 0; i < PowerUpHandler.PowerUps.length; i++)
                            PowerUpHandler.PowerUps[i].Active = false;

                        showMainMenu();
                        g_EndCredits = false;
						SoundEffects[SONG_END].stop();
                        g_PlayNumber++;
                        addScore(1000);
                    }
                }
				
                for(int i = CreditHandler.St_Num; i < CreditHandler.Cur_Num; i++)
                {
                    DrawTile(Back_Buffer, SpriteMap, 80, (int)CreditHandler.Positions[i], 1, 1, CreditHandler.Images[i], 14);
                    drawBevelString(CreditHandler.Strings[i], 100, (int)CreditHandler.Positions[i],g);
                }
            }
            // - Render console bar Very-Lasth
            renderConsoleBar( g );
        } 

        
//!@#!$@#!%###################################################################################################
		
		
        if( g_CurMessageIndex != 0 && !g_EndOfLevelAnimation )
        {
            g.setColor(Color.WHITE);
            if( g_CurMessageIndex >= SPECIAL_LEVEL_CAP )
            {
                g_EndOfLevelAnimation = true;
				
                hideAllEnemiesMissilesDodadsAndExplosions();
                MessageHandler.Timer = g_MessageWaitTime;
            }
            else if(MessageHandler.Message(150F, 522F, g, g_DisplayStrings[g_CurMessageIndex - 1]))
            {
                g_EndOfLevelAnimation = true;

                hideAllEnemiesMissilesDodadsAndExplosions();

                if( g_Level == FLY_TRANSITION_LEVEL )
                {
                        setupFlyTransition();
                }						
                MessageHandler.Timer = g_MessageWaitTime;
            }
        } 
	else if( g_PopMessageTimer > 0 )
        {
            g_PopMessageTimer--;
            String Temp = "";
            int jjj = 0;
            for(int i = 0; i < g_PopMessageString.length(); i++)
            {
                if(g_PopMessageString.charAt(i) == '\n')
                {
                    g.drawString(Temp, 2, 522 + jjj);
                    Temp = "";
                    jjj += 15;
                } 
		else
                {
                    Temp = Temp + g_PopMessageString.charAt(i);
                }
            }
            drawBevelString(Temp, 2, 522 + jjj,g);
        }
		
		
        Back_Graphics.setColor( Color.WHITE );
        if( lastHadFocus == false )
                drawBevelString( "PLEASE CLICK ME TO PLAY", (int)Room_Width/2-70, (int)Room_Height / 2 - 90, Back_Graphics ); 
        
        
    } // END OF public void draw()

    /**
    *	@fn accum()
    *	@brief Some sort of accumulation buffer thing? Doesn't work anyhow.
    *
    */
    public void accum(Graphics g)
    {
        for(int i = 0; i < particleHandler.Pos.length; i++)
        {
            if(particleHandler.Active[i])
            {
                g.setColor(Color.BLUE);
                g.drawLine((int)particleHandler.Pos[i].x, (int)particleHandler.Pos[i].y, (int)particleHandler.Pos[i].x, (int)particleHandler.Pos[i].y);
            }
        }
    }

    /**
    *	@fn DrawTile()
    *	@brief Draws a on the sprite sheet.
    */
    public void DrawTile(BufferedImage imgDst, BufferedImage imgSrc, int XPOS, int YPOS, int xpad, int ypad, int TileOffset, int HTile)
    {
        int CurY = TileOffset / HTile;
        int CurX = TileOffset % HTile;
        CurX = CurX * xpad + 1 + CurX * 32;
        CurY = CurY * ypad + 1 + CurY * 32;
        
        int type = AlphaComposite.SRC_OVER; 
        java.awt.AlphaComposite ac = java.awt.AlphaComposite.getInstance(AlphaComposite.DST_OVER, 0.50f);

        //Graphics2D g2d = (Graphics2D)g;
        //g2d.setPaint( new Color(0,0,0,0));
       // g2d.setComposite(ac);
        //g2d.set
        int clipYMax = 32;
        int clipXMax = 32;
        int clipXMin = 0;
        int clipYMin = 0;
        
        // - make sure quad is within bounds
        if(XPOS+clipXMax < 0) return;
        if(YPOS+clipYMax < 0) return;
        if(XPOS+clipXMax >= imgDst.getWidth()) clipXMax -= (XPOS+clipXMax)-imgDst.getWidth();
        if(YPOS+clipYMax >= imgDst.getHeight()) clipYMax -= (YPOS+clipYMax)-imgDst.getHeight();
        if(XPOS+clipXMin < 0) return;
        if(YPOS+clipYMin < 0) return;
            
        for(int iY = clipYMin; iY<clipYMax; ++iY)
        {
            for(int iX = clipXMin; iX<clipXMax; ++iX)
            {
                int rgb = imgSrc.getRGB(CurX+iX,CurY+iY);
                if( (rgb&0x000000FF)!=255) {
                    if( (rgb|0x000000FF)!=0xFF00FFFF) { // Filter Pink out
                        imgDst.setRGB(XPOS+iX,YPOS+iY,rgb);
                    }
                }
            }
        }
        
        
        
        //g2d.drawImage( image, XPOS, YPOS, XPOS + 32, YPOS + 32, CurX, CurY, CurX + 32, CurY + 32, null );
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }
	
	
    /**
    *	@fn keyPressed()	
    *	@brief Checks if keys are pressed and updates menus and variables accordingly.
    *
    */
    public void keyPressed(KeyEvent e)
    {
        int cd = e.getKeyCode();
        if(cd== KeyEvent.VK_ESCAPE)
           System.exit(0);
        else if(cd == KeyEvent.VK_LEFT)
            LEFT_down = true;
        else if(cd == KeyEvent.VK_RIGHT)
            RIGHT_down = true;
       
        if( cd == KeyEvent.VK_P && !P_Down )
        {
            if( !g_Paused && g_Media_Loaded )
                    g_Paused = true;
            else
                    g_Paused = false;
            P_Down = true;
        }
		
        if(cd == KeyEvent.VK_UP)
        {
            UP_down = true;
            if( g_Story )
            {
                g_StoryPos.y-=12F;
                g_StoryPosSpeed=0F;
            }
            else if(g_OptionsMenu || g_MainMenu)
            {

                playSound(SoundEffects[5]);
                _menu.advanceCursorUp();
//                if(!g_EditingOption)
//                {
//                    g_cursor.Pos.y -= 15F;
//                    if(g_cursor.Pos.y < (float)(253 - (g_OptionsStrings.length / 2) * 15))
//                        g_cursor.Pos.y = 253 + (g_OptionsStrings.length / 2) * 15;
//                } 
//                else
//                {
//                    int Ind = (int)(g_cursor.Pos.y - (float)(253 - (g_OptionsStrings.length / 2) * 15)) / 15;
//                    g_OptionsValues[Ind]++;
//                    if(g_OptionsValues[Ind] > g_OptionsUpperLimits[Ind])
//                        g_OptionsValues[Ind] = g_OptionsUpperLimits[Ind];
//                }
//            } 
//            else if(g_MainMenu)
//            {
//                g_cursor.Pos.y -= 15F;
//                evaluateMenuCursorPosition();
            }
        } 
        else if(cd == KeyEvent.VK_DOWN)
        {
            DOWN_down = true;
            if( g_Story )
            {
                g_StoryPos.y+=12F;
                g_StoryPosSpeed=0F;
            }
            else if(g_OptionsMenu || g_MainMenu)
            {

                playSound(SoundEffects[5]);
                _menu.advanceCursorDown();
//                if(!g_EditingOption)
//                {
//                    g_cursor.Pos.y += 15F;
//                    if(g_cursor.Pos.y > (float)(253 + (g_OptionsStrings.length / 2) * 15))
//                        g_cursor.Pos.y = 253 - (g_OptionsStrings.length / 2) * 15;
//                } else
//                {
//                    int Ind = (int)(g_cursor.Pos.y - (float)(253 - (g_OptionsStrings.length / 2) * 15)) / 15;
//                    g_OptionsValues[Ind]--;
//                    if(g_OptionsValues[Ind] < g_OptionsLowerLimits[Ind])
//                        g_OptionsValues[Ind] = g_OptionsLowerLimits[Ind];
//                }
//            } 
//            else if(g_MainMenu)
//            {
//                g_cursor.Pos.y += 15F;
//                evaluateMenuCursorPosition();
            }
        } 
        else if(cd == KeyEvent.VK_SPACE)
            SPACE_down = true;
        else if(cd == KeyEvent.VK_CONTROL)
            CONTROL_down = true;
        else if(cd == KeyEvent.VK_ALT)
            Show_Score = true;
        e.consume();
    }
    VECTOR2 _saved_playerspeed; //after the death sequence the player's speed is jacked.

    /**
    *	@fn keyReleased()
    *	@brief Checks the released key to see if input variables need to be set.
    */
    public void keyReleased(KeyEvent e)
    {
        int cd = e.getKeyCode();
        if(cd == KeyEvent.VK_LEFT)
            LEFT_down = false;
        else if(cd == KeyEvent.VK_RIGHT)
            RIGHT_down = false;
        if(cd == KeyEvent.VK_UP)
            UP_down = false;
        else if(cd == KeyEvent.VK_DOWN)
            DOWN_down = false;
        
        if( cd== KeyEvent.VK_P )
            P_Down = false;
		
        // - Toggle SFX on / off
        if( cd==KeyEvent.VK_S )
        {
            if( g_UsingSFX )
            {
                g_UsingSFX=false;
            }	
            else
            {
                g_UsingSFX=true;
            }
        }

        if( g_canDebug ) 
        {
            if( cd == KeyEvent.VK_D )
            {
                if( !g_Debug )
                    g_Debug = true;
                else
                    g_Debug = false;
            }
        }
		
        if( Hacks == true )
        {
            if( cd== KeyEvent.VK_F1 )			// - Skip LEvel
            {
                g_TimeToNextLevel=1;
            }
            else if( cd == KeyEvent.VK_F2 )		// - Destroy all enemies
            {
                for(int i = 0; i < enemyCreator.Enemies.length; i++)
                {
                    enemyCreator.Enemies[i].Health=0;
                }
            }
            else if ( cd == KeyEvent.VK_F3 )	// - All powerups and weapons
            {
                Player.Damage = 6000;
                Player.Weapon_Upgrade = 5;
                Player.Speed.equals(12F);
                Player.Booster_Upgrade = 5;
                Player.ShieldAmt = 7000;
                Player.Shield_Upgrade = 5;
                if(Player.Upgrade < 4)
                {
                    Player.Upgrade++;
                    Player.Tile_Offset += 2;
                    Player.Tile_Current = Player.Tile_Offset;
                }
                Player.MaxHealth += 5000;
                Player.Health+=5000;
                particleHandler.Particles(particleHandler.Num_Particles, Player.Pos, 10);
                addScore(1000);
                playSound(SoundEffects[12]);
            }
            else if( cd == KeyEvent.VK_F4 )
            {
                createNewShield();
            }
            else if( cd == KeyEvent.VK_F5 )
            {
                if( g_CurvyMoons )
                    g_CurvyMoons=false;
                else
                    g_CurvyMoons=true;
            }
            else if( cd == KeyEvent.VK_F6 )
            {
                if( g_ThreadSleepTime < 2000 )
                    g_ThreadSleepTime+=20L;
            }
            else if( cd == KeyEvent.VK_F7 )
            {
                g_ThreadSleepTime -=20L;
                if( g_ThreadSleepTime<2 )
                    g_ThreadSleepTime=2;
            }
            else if( cd == KeyEvent.VK_F8 )
            {
                g_ThreadSleepTime=20L;
            }
            else if( cd == KeyEvent.VK_F9 )
            {
                if( g_Invincible_Enemies )
                    enemyCreator.Invincible = g_Invincible_Enemies = g_Invincible_Enemies = false;
                else
                    enemyCreator.Invincible = g_Invincible_Enemies = g_Invincible_Enemies = true;
            }
            else if( cd == KeyEvent.VK_F10 )
            {
                g_Score = 10000000;
            }
        }
        else
        {
            if( e.getKeyChar() == hackSwitch.charAt(hackSwitchIndex) )
            {
                hackSwitchIndex++;
                if( hackSwitchIndex == hackSwitch.length()-1 )
                {
                    playSound(SoundEffects[3]);
                    Hacks = true;
                }
            }
            else if( e.getKeyChar() == debugSwitch.charAt(hackSwitchIndex) )
            {
                hackSwitchIndex++;
                if( hackSwitchIndex == debugSwitch.length() - 1 )
                {
                    playSound(SoundEffects[3]);
                    g_canDebug = true;
                }
            }
            else
            {
                hackSwitchIndex=0;
            }
        }
		
        if( cd == KeyEvent.VK_C )
        {
            g_CrazyParticles = true;
        }
		
        if( g_Debug )
        {
            if( cd==KeyEvent.VK_V )
            {
                if( g_Show_Debug_Variables )
                    g_Show_Debug_Variables = false;
                else
                    g_Show_Debug_Variables=true;
            }
        }
		
        if( (cd == KeyEvent.VK_ALT) )
        {
            Show_Score = false;
            if(g_TextDisplay)
                g_TextDisplay = false;
            else
                g_TextDisplay = true;
        } 
        else if( (cd == KeyEvent.VK_SPACE) || (cd == KeyEvent.VK_ENTER) || (cd==KeyEvent.VK_CONTROL) )
        {
            SPACE_down = false;
            CONTROL_down = false;
            if(g_CurMessageIndex != 0 && cd == KeyEvent.VK_ENTER)
                g_EndOfLevelAnimation = true;
				
            if(g_OptionsMenu)
            {
//                if(!g_EditingOption)
//                {
//                    if(g_OptionsStrings[(int)(g_cursor.Pos.y - (float)(253 - (g_OptionsStrings.length / 2) * 15)) / 15] == "Exit")
//                    {
//                        g_OptionsMenu = false;
//                        g_cursor.Pos.y = 208F;
//                    } 
//                    else
//                    {
//                        g_EditingOption = true;
//                    }
//                } 
//                else
//                {
//                    for(int i = 0; i < g_OptionsStrings.length; i++)
//                        if(g_OptionsStrings[i] == "Text Speed:")
//                            g_MessageSpeed = (float)g_OptionsValues[i] * 0.001F;
//
//                    g_EditingOption = false;
//                }
            } 
            else if(g_MainMenu && !g_Story && !g_Intro)
            {
                g_IntroWait = g_MaxIntroWait;
		MenuOrSubMenu menuItem = _menu.getMenuItemAtCursorPosition();		
                
                
                if( menuItem._name.equals(_menuitem_continue) ) //g_cursor.Pos.y == 208F-15F)
                {
                    // - The user selected to continue the game.
                    playSound(SoundEffects[14]);
                    
                    _musicPlayer.musicPlayLoopForId(g_Level);
                    Player.Speed = _saved_playerspeed;
                    
                    CreditHandler = new cCreditsHandler(Room_Width, Room_Height);
                    //Player = new PlayerShip();
                    Boss = new cBoss(Room_Width, Room_Height);
                    enemyCreator = new EnemyCreator(Room_Width, Room_Height);
                    MessageHandler = new cIncrementalMessage();
					
                    particleHandler.Init();
                    //Player.init(0, 2, 3);
                    Player.Missiles = new Missile[100];
                    for(int i = 0; i < Player.Missiles.length; i++)
                    {
                        Player.Missiles[i] = new Missile(Room_Width,Room_Height);
                        Player.Missiles[i].Init();
                    }
                    Player.Pos.x = 256F;
                    Player.Pos.y = 470F;
                    Player.Health = Player.MaxHealth;
					
                    Boss.Create(0, g_PlayNumber);
                    PowerUpHandler.Init();
                    enemyCreator.Init(40, 100, g_PlayNumber);
                    particleHandler.Init();
                    Player.Gravity.assign(0.0f);

                    g_PlayEndSound = false;
                    g_MainMenu = false;

                    // g_Level = 1;
                    Backdrop_Speed = 6F;
                    g_StarSpeed.y=6F;

                    Player.Gravity.y = 0.0F;
                    Player.Gravity.x = 0.0F;
                    Player.Speed.x = Player.saveSpeed.x;
                    Player.Speed.y = Player.saveSpeed.y;

                    enemyCreator.Num_Enemies = 0;
                    enemyCreator.Num_Missiles = 0;
                    enemyCreator.EnemyTimer = enemyCreator.MaxEnemyTimer = 5 + (82 - g_Level * 2);

                    MessageHandler.NewMessage(g_MessageSpeed);
                    g_TimeToNextLevel = g_MaxTimeToNextLevel;

                    setBossVariables(); 

                    hideAllEnemiesMissilesAndExplosions();

                    for(int i = 0; i < particleHandler.Active.length; i++)
                        particleHandler.Active[i] = false;

                    for(int i = 0; i < PowerUpHandler.PowerUps.length; i++)
                        PowerUpHandler.PowerUps[i].Active = false;

                    g_EndSequence = false;
                    g_HealthUp = 1;	
                    g_Score=0;

                    g_ContinueMode = false;
					
                }
                else if( menuItem._name.equals(_menuitem_newgame) )
                {
                    // - The user selected a new game.
                    // - START A NEW GAME 
                    playSound(SoundEffects[14]);
                    CreditHandler = new cCreditsHandler(Room_Width, Room_Height);
                    Player = new PlayerShip(Room_Width,Room_Height);
                    Boss = new cBoss(Room_Width, Room_Height);
                    enemyCreator = new EnemyCreator(Room_Width, Room_Height);
                    MessageHandler = new cIncrementalMessage();
		 
                    _musicPlayer.musicPlayLoopForId(g_Level);
                    	
                    particleHandler.Init();
                    Player.init(0, 2, 3);
                    Player.InitPlayer();
                    Boss.Create(0, g_PlayNumber);
                    PowerUpHandler.Init();
                    enemyCreator.Init(40, 100, g_PlayNumber);
                    particleHandler.Init();
					
                    g_PlayEndSound = false;
                    g_MainMenu = false;
					
                    g_Level = 1;
                    Backdrop_Speed = 6F;
                    g_StarSpeed.y=6F;

                    Player.Gravity.y = 0.0F;

                    enemyCreator.Num_Enemies = 0;
                    enemyCreator.Num_Missiles = 0;
                    enemyCreator.EnemyTimer = enemyCreator.MaxEnemyTimer = 5 + (82 - g_Level * 2);
					
                    MessageHandler.NewMessage(g_MessageSpeed);
                    g_TimeToNextLevel = g_MaxTimeToNextLevel;
					
                    setBossVariables(); 

                    hideAllEnemiesMissilesDodadsAndExplosions();
                    
                    for(int i = 0; i < particleHandler.Active.length; i++)
                        particleHandler.Active[i] = false;

                    for(int i = 0; i < PowerUpHandler.PowerUps.length; i++)
                        PowerUpHandler.PowerUps[i].Active = false;

                    g_EndSequence = false;
                    g_HealthUp = 1;
                    g_ContinueMode=false;
                }
                else if( menuItem._name.equals(_menuitem_story))
                {
                    // - The user selected to see the story
                    g_Story = true;
                    g_StoryPos.x = 100F;
                    g_StoryPos.y = Room_Height;
                }
                else if( menuItem._name.equals(_menuitem_special))
                {
                    if(g_Score > g_SpecialScore)
                    {
                        // - The User selected a special level
                        Backdrop_Speed = 3F;
                        PowerUpHandler.Init();
                        enemyCreator.Init(100, 100, g_PlayNumber);
                        Player.init(0, 2, 3);
                        Player.InitPlayer();
                        particleHandler.Init();
                        MessageHandler.NewMessage(g_MessageSpeed);
                        Boss.Create(1, g_PlayNumber);
                        enemyCreator.Num_Enemies = 0;
                        enemyCreator.Num_Missiles = 0;
                        enemyCreator.EnemyTimer = enemyCreator.MaxEnemyTimer = 5;
                        g_TimeToNextLevel = 100;
                        g_MainMenu = false;
                        g_SpecialLevel = true;
                        g_Level = SPECIAL_LEVEL_INDEX;
                        Player.ShieldAmt = 10000;
                        setBossVariables(); 
                        hideAllEnemiesMissilesDodadsAndExplosions();
                    } 
                    else
                    {
                        g_OptionsMenu = true;
                        g_cursor.Pos.y = 253 - (g_OptionsStrings.length / 2) * 15;
                    }
                }
                else if( menuItem._name.equals(_menuitem_options))
                {
                    g_OptionsMenu = true;
                    g_cursor.Pos.y = 253 - (g_OptionsStrings.length / 2) * 15;
                }
            }
            else if( g_Story )
            {
                // - We're in the story, exit out.
                g_Story = false;
            }
            else if(g_Intro)
            {
                if(g_IntroTimer0 > 0)
                    g_IntroTimer0 = 0;
                else if(g_IntroTimer1 > 0)
                    g_IntroTimer1 = 0;
                else
                    showMainMenu();
            }
        }
        e.consume();
    }
	
	/**
	*	@fn blitGradient()
	*	@brief Blits a gradient to the floor of the fly transition to make the ground seem more realistic.
	*/
	public void blitGradient(Graphics g) {
		// Color3b bc;
		// bc.r = 255;
		// bc.g = 255;
		// bc.b = 255;
	}
	/**
	*	@fn evaluateMenuCursorPosition()
	*	@brief Called when the player moves his or her cursor.
	*/
	public void evaluateMenuCursorPosition() {	
		
			playSound(SoundEffects[5]);
		
		g_IntroWait = g_MaxIntroWait;

		
		float minx = 208; // Min and max yvals without options.
		float maxx = 238;
		
		if( g_ContinueMode )
			minx -= 15F;
		
		if( g_Score>g_SpecialScore )
			maxx += 15F;
		
		 if(g_cursor.Pos.y < minx)
			g_cursor.Pos.y = maxx;
		
		else if(g_cursor.Pos.y > maxx)
			g_cursor.Pos.y = minx;
	}
	int mx_w(int x)
        {
            return (int)(((float)x)/512F * Room_Width);
        }
	int mx_h(int y)
        {
            return (int)(((float)y)/512F * Room_Height);
        }
        /**
	*	@fn renderConsoleBar()
        * render menu
	*	@brief renders the bar at the bottom of the screen, witht the text, weapons etc.
	*/
	public void renderConsoleBar( Graphics g )
	{
            g.setColor(Color.BLACK);
            g.fillRect(0, Room_Height, Room_Width,  g_ConsoleHeight);
            g.finalize();
            if(g_CurMessageIndex == 0)
            {
                // - I assume this means we display health and status as text rather than health bar.
                if(!g_TextDisplay)
                {
                    g.setColor(new Color(255,(int)cap(255F*g_BlinkRatio,0,255),(int)cap(255F*g_BlinkRatio,0,255)));
                    g.fillRect(
                            mx_w(207), 
                            mx_h(522-6), 
                            (int)((double)getPlayerHealthRatio()*(double)100.0), 
                            8);
                    g.setColor(new Color(255,255-(int)cap(255F*g_BlinkRatio,0,255),255-(int)cap(255F*g_BlinkRatio,0,255)));
                    g.drawRect(
                            mx_w(207), 
                            mx_h(522-6), 
                            100, 
                            8);
                } 
                else
                {
                    g.setColor(new Color(255,255-(int)cap(255F*g_BlinkRatio,0,255),255-(int)cap(255F*g_BlinkRatio,0,255)));
                    drawBevelString(
                            Integer.toString(Player.Health) + " / " + Integer.toString(Player.MaxHealth), 
                            mx_w(207), 
                            mx_h(522),g);
                }

                drawBevelString("Life: ", mx_w(190), mx_h(522),g);
                drawBevelString("Score: " + Integer.toString(g_Score), mx_w(370), mx_h(522),g);
                drawBevelString("Sound: " + (g_UsingSFX?("ON"):("OFF")), mx_w(10), mx_h(522),g);

                //if(g_TextDisplay)
                g.setColor(new Color( 128, 128, 128 ) );
                String rn="";
                if( g_PlayNumber>0 )
                {
                        rn+=" Hard";
                }
                if(g_PlayNumber > 1)
                {
                    rn+="...";
                    switch( g_PlayNumber )
                    {
                        case 2: rn+="II"; break;
                        case 3: rn+="III"; break;
                        case 4: rn+="IV"; break;
                        case 5: rn+="V"; break;
                        case 6: rn+="VI"; break;
                        case 7: rn+="VII"; break;
                        case 8: rn+="VIII"; break;
                        case 9: rn+="IX"; break;
                        case 10: rn+="X"; break;
                        case 11: rn+="DANOS"; break;
                        case 12: rn+="OGMIOS"; break;
                        case 13: rn+="FORSETI"; break;
                        default: rn+="YOU ROCK!"; break;
                    }
                }

                drawBevelString("Level: " + g_Level + rn, mx_w(10), mx_h(20), g);

                if( g_TextDisplay )
                    drawBevelString("Class: " + getUpgradeClass(Player.Upgrade), mx_w(10), mx_h(34), g);

                //################
                // - RENDER SHIELD
                if(Player.Shield_Upgrade == 1)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-94, Room_Height+1, 1, 1, 129, 14);
                else if(Player.Shield_Upgrade == 2)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-94, Room_Height+1, 1, 1, 130, 14);
                else if(Player.Shield_Upgrade == 3)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-94, Room_Height+1, 1, 1, 131, 14);
                else if(Player.Shield_Upgrade == 4)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-94, Room_Height+1, 1, 1, 132, 14);
                else if(Player.Shield_Upgrade == 5)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-94, Room_Height+1, 1, 1, 111, 14);

                //#################
                // - RENDER BOOSTER
                if(Player.Booster_Upgrade == 1)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-(512-448), Room_Height+1, 1, 1, 115, 14);
                else if(Player.Booster_Upgrade == 2)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-(512-448), Room_Height+1, 1, 1, 126, 14);
                else if(Player.Booster_Upgrade == 3)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-(512-448), Room_Height+1, 1, 1, 137, 14);
                else if(Player.Booster_Upgrade == 4)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-(512-448), Room_Height+1, 1, 1, 127, 14);
                else if(Player.Booster_Upgrade == 5)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-(512-448), Room_Height+1, 1, 1, 124, 14);

                //################
                // - RENDER WEAPON
                if(Player.Weapon_Upgrade == 1)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-(512-480), Room_Height+1, 1, 1, 133, 14);
                else if(Player.Weapon_Upgrade == 2)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-(512-480), Room_Height+1, 1, 1, 134, 14);
                else if(Player.Weapon_Upgrade == 3)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-(512-480), Room_Height+1, 1, 1, 135, 14);
                else if(Player.Weapon_Upgrade == 4)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-(512-480), Room_Height+1, 1, 1, 136, 14);
                else if(Player.Weapon_Upgrade == 5)
                        DrawTile(Back_Buffer, SpriteMap, Room_Width-(512-480), Room_Height+1, 1, 1, 110, 14);
            }
	}
	/**
	*	@fn renderParticles
	*	@brief	Renders particles.
	*
	*/
	public void renderParticles( Graphics g )
	{
		for(int i = 0; i < particleHandler.Pos.length; i++)
		{
			if(!particleHandler.Active[i])
				continue;
			
			if(particleHandler.Type[i] == 0)
			{
				if(g_ColorfulParticles)
				{
					if(particleHandler.Colors[i] == 0)
						g.setColor(Color.BLUE);
					else if(particleHandler.Colors[i] == 1)
						g.setColor(Color.RED);
					else if(particleHandler.Colors[i] == 2)
						g.setColor(Color.WHITE);
					else if(particleHandler.Colors[i] == 3)
						g.setColor(Color.PINK);
					else if(particleHandler.Colors[i] == 4)
						g.setColor(Color.YELLOW);
					else if(particleHandler.Colors[i] == 5)
						g.setColor(Color.GREEN);
					else if(particleHandler.Colors[i] == 6)
						g.setColor(Color.CYAN);
					else
						g.setColor(Color.ORANGE);
				} 
				else
				{
					g.setColor(Color.RED);
				}
				if(!g_BigParticles)
					g.drawLine((int)particleHandler.Pos[i].x, (int)particleHandler.Pos[i].y, (int)particleHandler.Pos[i].x, (int)particleHandler.Pos[i].y);
				else
					g.fillOval((int)particleHandler.Pos[i].x, (int)particleHandler.Pos[i].y, 10, 10);
				continue;
			}
			
			int ddd = 0;
			if(particleHandler.Type[i] == 1)
				ddd = 140;
			if(particleHandler.Type[i] == 2)
				ddd = 141;
			if(particleHandler.Type[i] == 3)
				ddd = 142;
			if(particleHandler.Type[i] == 4)
				ddd = 143;
			if(particleHandler.Type[i] == 5)
				ddd = 144;
			if(particleHandler.Type[i] == 6)
				ddd = 145;
			if(particleHandler.Type[i] == 7)
				ddd = 146;
			if(particleHandler.Type[i] == 8)
				ddd = 149;
			if(particleHandler.Type[i] == 9)
				ddd = 150;
			if(particleHandler.Type[i] == 10)
				ddd = 151;
			if(particleHandler.Type[i] == 11)
				ddd = 152;
			if(particleHandler.Type[i] == 12)
				ddd = 153;
				
			DrawTile(Back_Buffer, SpriteMap, (int)particleHandler.Pos[i].x-16, (int)particleHandler.Pos[i].y-16, 1, 1, ddd, 14);
		}
	}
	/**
	*	@fn checkShipCollisions()
	*	@brief Checks for collisions between player ship and other ships.
	*
	*/
	public void checkShipCollisions()
	{
            int i;
            for(i = 0; i < enemyCreator.Enemies.length; i++)
            {
                if( !enemyCreator.Enemies[i].InUse )
                    continue;

                if( !Collideable.collide(
                enemyCreator.Enemies[i].boundCircle, 
                enemyCreator.Enemies[i].Pos, 
                enemyCreator.Enemies[i].Speed,
                Player.boundCircle, 
                Player.Pos,
                Player.lastVelocity,
                temp_cPos ) )
                        continue;

                playSound(SoundEffects[6]);
                enemyCreator.Missiles[i].InUse = false;

                Player.damage( (int)(enemyCreator.Enemies[i].Health/100), g_Level );
                if( !g_Invincible_Enemies )
                        enemyCreator.Enemies[i].Health-=( enemyCreator.Enemies[i].MaxHealth/10 );
                // - Respond to the collision
                //Player.move( Player.lastVelocity.negative() );
                //enemyCreator.Enemies[i].move( enemyCreator.Enemies[i].lastVelocity.negative() );
                //particleHandler.Particles( 4, enemyCreator.Enemies[i].Pos.plus(temp_cPos), 1 );
                for(int j = 0; j < Explosions.length; j++)
                {
                    if(!Explosions[j].Active)
                    {
                        Explosions[j].Active = true;
                        Explosions[j].Init(84, 1, 7);
                        Explosions[j].Pos.x = Player.Pos.x-16;
                        Explosions[j].Pos.y = Player.Pos.y-16;
                    }
                }		
            }
            if( g_BossMode )
            {
                for( i=0; i<Boss.Parts.length; ++i )
                {
                    if( !Boss.Parts[i].Active )
                        continue;
                    if( !Boss.Parts[i].Collide( Player.BoundBox, Player.Pos ) )
                        continue;

                    if( i==6 && Boss.Type==0 )
                        Boss.stopLasering();

                    playSound(SoundEffects[6]);
                    Player.damage( (Boss.Parts[i].Health/10), g_Level );

                    // - Respond to the collision
                    //Player.move( Player.lastVelocity.negative() );
                    for(int j = 0; j < Explosions.length; j++)
                    {
                        if(!Explosions[j].Active)
                        {
                            Explosions[j].Active = true;
                            Explosions[j].Init(84, 1, 7);
                            Explosions[j].Pos.x = Player.Pos.x-16;
                            Explosions[j].Pos.y = Player.Pos.y-16;
                        }
                    }	
                }
            }
	}
	
	/**
	*	@fn checkMissileCollisions()
	*	@brief Checks for collisions
	*
	*/
	public void checkMissileCollisions()
	{
            int i,j,k;
            for( i=0; i<enemyCreator.Enemies.length; ++i )
            {
                if( !enemyCreator.Enemies[i].InUse )
                        continue;

                //##########################################
                // - CHECK FOR ENEMY - ENEMY			
                for( j=0; j< enemyCreator.Enemies.length; ++j ) 
                {
                    if(i==j)
                            continue;
                    if(!enemyCreator.Enemies[j].InUse )
                            continue;
                    if( Collideable.collide(
                    enemyCreator.Enemies[i].boundCircle, 
                    enemyCreator.Enemies[i].Pos, 
                    enemyCreator.Enemies[i].Speed,
                    enemyCreator.Enemies[j].boundCircle, 
                    enemyCreator.Enemies[j].Pos,
                    enemyCreator.Enemies[j].Speed,
                    temp_cPos))
                    {
                        playSound(SoundEffects[13]);
                        particleHandler.Particles(15, temp_cPos, 0);
                        if( !g_Invincible_Enemies ){
                            enemyCreator.Enemies[i].Health-=( enemyCreator.Enemies[j].MaxHealth/10 );
                            enemyCreator.Enemies[j].Health-=( enemyCreator.Enemies[i].MaxHealth/10 );
                        }

                    }

                }						

                //############################################
                // - CHECK ENEMY - ENEMY MISSILES
                for( j=0; j< enemyCreator.Missiles.length; ++j ) 
                {
                    if( !enemyCreator.Missiles[j].InUse )
                        continue;

                    // - don't want to collide the missile with it's creator.
                    if( enemyCreator.Missiles[j].creator == i )
                        continue;

                    if( Collideable.collide(
                    enemyCreator.Enemies[i].boundCircle, 
                    enemyCreator.Enemies[i].Pos, 
                    enemyCreator.Enemies[i].Speed,
                    enemyCreator.Missiles[j].boundCircle, 
                    enemyCreator.Missiles[j].Pos,
                    enemyCreator.Missiles[j].Speed,
                    temp_cPos))
                    {
                        playSound(SoundEffects[13]);
                        enemyCreator.Missiles[j].InUse = false;
                        particleHandler.Particles(15, enemyCreator.Missiles[j].Pos, 0);
                        enemyCreator.Enemies[i].Health -= enemyCreator.Missiles[j].Damage;
                    }
                }

                //#################################3
                // - CHECK ENEMY - DODAD
                for( j=0; j<dodadController.Dodads.length; ++j )
                {
                    if( !dodadController.Dodads[j].Active )
                            continue;			

                    if( Collideable.collide(
                    dodadController.Dodads[j].boundCircle, 
                    dodadController.Dodads[j].Pos,
                    dodadController.Dodads[j].lastVelocity,
                    enemyCreator.Enemies[i].boundCircle, 
                    enemyCreator.Enemies[i].Pos,
                    enemyCreator.Enemies[i].Speed,
                    temp_cPos))
                    {
                        playSound(SoundEffects[13]);
                        if( !g_Invincible_Enemies )
                                enemyCreator.Enemies[i].Health -= dodadController.Damage;
                        particleHandler.Particles(4, dodadController.Dodads[j].Pos, 1);
                        dodadController.Dodads[j].Life -= enemyCreator.Enemies[i].Damage;
                    }
                }
            }

            //############################################
            // - CHECK ENEMY MISSILES VS VARIOUS
            for( i=0; i< enemyCreator.Missiles.length; ++i ) 
            {
                if( !enemyCreator.Missiles[i].InUse )
                        continue;

                //########################################
                // - CHECK FOR PLAYER - ENEMYMISSILE 
                if( Collideable.collide(
                enemyCreator.Missiles[i].boundCircle, 
                enemyCreator.Missiles[i].Pos, 
                enemyCreator.Missiles[i].Speed,
                Player.boundCircle, 
                Player.Pos,
                Player.lastVelocity,
                temp_cPos) )
                {

                    playSound(SoundEffects[6]);
                    enemyCreator.Missiles[i].InUse = false;

                    Player.damage(enemyCreator.Missiles[i].Damage,g_Level);

                    VECTOR2 v = enemyCreator.Missiles[i].Pos;
                    for(j = 0; j < Explosions.length; j++)
                    {
                        if(!Explosions[j].Active)
                        {
                            Explosions[j].Active = true;
                            Explosions[j].Init(84, 1, 7);
                            Explosions[j].Pos.x = enemyCreator.Missiles[i].Pos.x-16;
                            Explosions[j].Pos.y = enemyCreator.Missiles[i].Pos.y-16;
                        }
                    }		
                }
                //##########################################
                // - CHECK FOR DODAD - ENEMYMISSILE
                for( j=0; j<dodadController.Dodads.length; ++j )
                {
                    if( !dodadController.Dodads[j].Active )
                            continue;
                    if( Collideable.collide(
                    dodadController.Dodads[j].boundCircle, 
                    dodadController.Dodads[j].Pos, 
                    dodadController.Dodads[j].lastVelocity,
                    enemyCreator.Missiles[i].boundCircle, 
                    enemyCreator.Missiles[i].Pos,
                    enemyCreator.Missiles[i].Speed,
                    temp_cPos))
                    {
                        playSound(SoundEffects[13]);
                        enemyCreator.Missiles[i].InUse = false;
                        particleHandler.Particles(4, dodadController.Dodads[j].Pos, 1);
                        dodadController.Dodads[j].Life -= enemyCreator.Missiles[i].Damage;
                    }
                    if(dodadController.Dodads[j].Life <= 0)
                        dodadController.Dodads[j].Active = false;			
                }		
            }
	}
	
	/**
	*	@fn createNewShield()
	*	@brief Creates a new moon shield for the player.
	*/
	public void createNewShield() 
        {
            for(int b = 0; b < dodadController.Dodads.length; b++)
            {
                if(dodadController.Dodads[b].Active)
                        continue;
                if(g_Level > 3)
                        dodadController.Dodads[b].Life = 100;
                else
                if(g_Level > 2)
                        dodadController.Dodads[b].Life = 70;
                else
                        dodadController.Dodads[b].Life = 40;
                dodadController.Dodads[b].Pos.x = Player.Pos.x;
                dodadController.Dodads[b].Pos.y = Player.Pos.y - 16F;
                if( g_CurvyMoons )
                        dodadController.Dodads[b].curveType = RAND.nextInt(DodadController.Dodad.MAX_CURVES);
                else
                        dodadController.Dodads[b].curveType = 0;
                dodadController.Dodads[b].Angle = (double)RAND.nextFloat() * 6.28; 
                dodadController.Dodads[b].AngleAdder = (double)RAND.nextInt(100) * (double)0.001; 
                dodadController.Dodads[b].Radius = RAND.nextFloat() * (30F + ( (float)g_Level*2F ) );	// - maximum radius of 30 pixels to 46 pixels.
                dodadController.Dodads[b].Active = true;
                b = dodadController.Dodads.length;
            }
	}
	/**
	*	@fn collideWithPowerups()	
	*	@brief Check and respond to collisions with powerups.
	*/
	public void collideWithPowerups()
	{
            for(int i = 0; i < PowerUpHandler.PowerUps.length; i++)
            {
                    if(!PowerUpHandler.PowerUps[i].Active)
                            continue;

                    if(!Collideable.collide( 
                    Player.boundCircle, 
                    Player.Pos, 
                    Player.lastVelocity,
                    PowerUpHandler.PowerUps[i].boundCircle, 
                    PowerUpHandler.PowerUps[i].Pos,
                    PowerUpHandler.PowerUps[i].Speed,
                    temp_cPos) )
                            continue;

                    PowerUpHandler.PowerUps[i].Active = false;

                if(PowerUpHandler.PowerUps[i].Type == 1) 				// - Missile Increaser
                {
                        if(Player.MaxMissileDelay > 3F)
                                Player.MaxMissileDelay -= 0.3F;
                        if(Player.MaxRocketDelay > 3F)
                                Player.MaxRocketDelay -= 0.2F;
                        Player.MaxHealth++;
                        Player.Health++;
                        particleHandler.Particles(15, Player.Pos, 10);
                        addScore(5);
                        playSound(SoundEffects[12]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 2)			// - Ship Upgrade
                {
                        if(Player.Upgrade < 4 && Player.Upgrade < g_Level)
                        {
                                Player.Health += 50;
                                Player.Upgrade++;
                                Player.Tile_Offset += 2;
                                Player.Tile_Current = Player.Tile_Offset;
                        }
                        else if( Player.Upgrade>=4 )
                        {
                                Player.Health+=10;
                                if( Player.Upgrade < 8 )
                                        Player.Upgrade++;
                                else if( Player.Upgrade >= 8 && g_PlayNumber > 0 )
                                        Player.Upgrade++;
                        }
                        particleHandler.Particles(15, Player.Pos, 10);
                        addScore(10);
                        playSound(SoundEffects[12]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 3)
                {

                        playSound(SoundEffects[3]);

                        createNewShield();

                        particleHandler.Particles(15, Player.Pos, 9);
                        addScore( 5 );
                }			
                else if(PowerUpHandler.PowerUps[i].Type == 6)
                {
                        Player.Health += 10;
                        particleHandler.Particles(10, Player.Pos, 5);
                        addScore( 5 );
                        playSound(SoundEffects[11]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 10)
                {
                        Player.Health += 50;
                        particleHandler.Particles(10, Player.Pos, 6);
                        addScore( 10 );
                        playSound(SoundEffects[11]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 14)
                {
                        Player.Health += 100;
                        particleHandler.Particles(10, Player.Pos, 4);
                        addScore( 15 );
                        playSound(SoundEffects[11]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 18)
                {
                        Player.Health = Player.MaxHealth;
                        particleHandler.Particles(10, Player.Pos, 3);
                        addScore( 20 );
                        playSound(SoundEffects[11]);
                }
                //########################3
                // SPEED UPGRADES
                else if(PowerUpHandler.PowerUps[i].Type == 7)
                {
                        if(Player.Booster_Upgrade < 1)
                        {
                                Player.Speed.equals(6F);
                                Player.Booster_Upgrade = 1;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 5 );

                                playSound(SoundEffects[3]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 11)
                {
                        if(Player.Booster_Upgrade < 2)
                        {
                                Player.Speed.equals(8F);
                                Player.Booster_Upgrade = 2;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 10 );

                                playSound(SoundEffects[3]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 15)
                {
                        if(Player.Booster_Upgrade < 3)
                        {
                                Player.Speed.equals(10F);
                                Player.Booster_Upgrade = 3;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 15 );

                                playSound(SoundEffects[3]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 19)
                {
                        if(Player.Booster_Upgrade < 4)
                        {
                                Player.Speed.equals(12F);
                                Player.Booster_Upgrade = 4;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 20 );

                                playSound(SoundEffects[3]);
                }
                //#####################
                // SHIELD UPGRADES
                else if(PowerUpHandler.PowerUps[i].Type == 8)
                {
                        if(Player.Shield_Upgrade < 1)
                        {
                                Player.ShieldAmt = 10;
                                Player.Shield_Upgrade = 1;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 5 );

                                playSound(SoundEffects[3]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 12)
                {
                        if(Player.Shield_Upgrade < 2)
                        {
                                Player.ShieldAmt = 30;
                                Player.Shield_Upgrade = 2;
                        }
                        particleHandler.Particles(10,Player.Pos, 8);
                        addScore( 10 );

                                playSound(SoundEffects[3]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 16)
                {
                        if(Player.Shield_Upgrade < 3)
                        {
                                Player.ShieldAmt = 50;
                                Player.Shield_Upgrade = 3;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 15 );

                                playSound(SoundEffects[3]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 20)
                {
                        if(Player.Shield_Upgrade < 4)
                        {
                                Player.ShieldAmt = 70;
                                Player.Shield_Upgrade = 4;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 20 );

                                playSound(SoundEffects[3]);
                }
                //##########################
                // WEAPON UPGRADES
                else if(PowerUpHandler.PowerUps[i].Type == 9)
                {
                        if(Player.Weapon_Upgrade < 1)
                        {
                                Player.Damage = 20;
                                Player.Weapon_Upgrade = 1;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 5 );

                                playSound(SoundEffects[3]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 13)
                {
                        if(Player.Weapon_Upgrade < 2)
                        {
                                Player.Damage = 30;
                                Player.Weapon_Upgrade = 2;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 10 );

                                playSound(SoundEffects[3]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 17)
                {
                        if(Player.Weapon_Upgrade < 3)
                        {
                                Player.Damage = 40;
                                Player.Weapon_Upgrade = 3;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 15 );

                                playSound(SoundEffects[3]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 21)		// - Best Weapon
                {
                        if(Player.Weapon_Upgrade < 4)
                        {
                                Player.Damage = 60;
                                Player.Weapon_Upgrade = 4;
                        }
                        particleHandler.Particles(10, Player.Pos, 8);
                        addScore( 20 );

                                playSound(SoundEffects[3]);
                }
                //##########################3
                // SHIP UPGRADE
                else if(PowerUpHandler.PowerUps[i].Type == 22)
                {
                    Player.Damage++;
                    Player.ShieldAmt++;
                    particleHandler.Particles(10, Player.Pos, 8);
                    addScore( 100 );
                    playSound(SoundEffects[1]);
                }
                //###########################
                //  SPECIAL UPGRADES
                else if(PowerUpHandler.PowerUps[i].Type == 24)
                {
                    if( Player.Damage <120 )
                        Player.Damage = 120;
                    Player.Weapon_Upgrade = 5;
                    particleHandler.Particles(10, Player.Pos, 8);
                    addScore( 500 );
                    playSound(SoundEffects[12]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 25)
                {
                    if( Player.ShieldAmt <120 )
                        Player.ShieldAmt = 120;
                    Player.Shield_Upgrade = 5;
                    particleHandler.Particles(10, Player.Pos, 8);
                    addScore( 500 );
                    playSound(SoundEffects[12]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 26)
                {
                    if( Player.Speed.isLessThan(15F) )
                        Player.Speed.equals(15F);
                    Player.Booster_Upgrade = 5;
                    particleHandler.Particles(10, Player.Pos, 8);
                    addScore( 500 );
                    playSound(SoundEffects[12]);
                }
                else if(PowerUpHandler.PowerUps[i].Type == 27)
                {
                    Player.Upgrade = 5;
                    Player.Tile_Offset = Player.Tile_Current = 10;
                    playSound(SoundEffects[12]);
                    Player.Health += 200;
                    Player.MaxMissileDelay -= 5F;
                    Player.MaxRocketDelay -= 5F;
                    if(Player.MaxMissileDelay <= 3F)
                        Player.MaxMissileDelay = 3F;

                    if(Player.MaxRocketDelay <= 3F)
                        Player.MaxRocketDelay = 3F;
                }

                if(Player.Health > Player.MaxHealth)
                    Player.Health = Player.MaxHealth;

            }
	}
	
	
	/**
	*	@fn evaluateHacks()
	*	@brief Evaulate any hacks that are present.
	*/
	public void evaluateHacks()
        {
            if( g_Hack_SetLevelTimer ) 
            {
                // - Skip this level
                g_Hack_SetLevelTimer = false;
            }
	}
	
	
	/**
	*	@fn checkLevelTimer()	
	*	@brief Checks to see if we should be advancing to the next level
	*/
	public void checkLevelTimer() {
	
		// - Update the level timer.
		if(g_TimeToNextLevel != -1 && g_TimeToNextLevel!=0)
			g_TimeToNextLevel--;
		else if(g_TimeToNextLevel == -1)
			g_TimeToNextLevel = -2;

		if(g_TimeToNextLevel == 0)
		{
                    if( g_Level == FLY_TRANSITION_LEVEL )
                    {
                        // - Second to last level, do pincushion animation.
                        enemyCreator.CanMakeEnemies = false;
                        g_TimeToNextLevel = g_MaxTimeToNextLevel;
                        g_CurMessageIndex = g_Level;	// - Setting this will cause the message handler to display the story text.
                    }
                    else if(g_Level + 1 == GATE_LEVEL)
                    {
                        Backdrop_Speed = GATE_BACKDROP_SPEED;
                    }
                    else if(g_Level + 1 == g_MaxLevels) // - Time to make the boss level
                    {
                        enemyCreator.CanMakeEnemies = false;
                        if(enemyCreator.Num_Enemies <= 0)
                        {
                            hideAllEnemiesMissilesAndExplosions();
                            g_CurMessageIndex = 0;
                            g_BossMode = true;
                            // - Play the boss intro sound.
                            playSound(SoundEffects[BOSS_APPEAR_SOUND]);
                            g_TimeToNextLevel = -1;
                            Backdrop_Speed = BOSS_BACKDROP_SPEED;
                            particleHandler.Particles(particleHandler.Num_Particles, Boss.Pos, 10);
                        }
                    }
                    else if( g_Level == SPECIAL_LEVEL_CAP+1 )
                    {
                        enemyCreator.CanMakeEnemies = false;
                        if(enemyCreator.Num_Enemies <= 0)
                        {
                            hideAllEnemiesMissilesDodadsAndExplosions();
                            g_CurMessageIndex = 0;
                            g_BossMode = true;
                            // - Play the boss intro sound.
                            playSound(SoundEffects[BOSS_APPEAR_SOUND]);
                            g_TimeToNextLevel = -1;
                            Backdrop_Speed = BOSS_BACKDROP_SPEED;
                        }
                    }
                    else
                    {
                        enemyCreator.CanMakeEnemies = false;
                        if(enemyCreator.Num_Enemies <= 0)
                        {
                            g_TimeToNextLevel = g_MaxTimeToNextLevel;
                            g_CurMessageIndex = g_Level;	// - Setting this will cause the message handler to display the story text.
                        }
                    } 
		}
	}
	/**
	*	@fn hideAllEnemiesMissilesDodadsAndExplosions()
	*/
	public void hideAllEnemiesMissilesDodadsAndExplosions(){
            int i;
            for(i=0;i<enemyCreator.Enemies.length;++i)
                enemyCreator.Enemies[i].InUse=false;
            for(i=0;i<particleHandler.Active.length;++i)
                particleHandler.Active[i]=false;
            for(i=0;i<enemyCreator.Missiles.length;++i)
                enemyCreator.Missiles[i].InUse=false;
            enemyCreator.Num_Enemies=0;
            for(i=0;i<Player.Missiles.length;++i)
                Player.Missiles[i].InUse=false;
	}
	/**
	*	@fn hideAllEnemiesMissilesAndExplosions()
	*/
	public void hideAllEnemiesMissilesAndExplosions(){
            int i;
            for(i=0;i<enemyCreator.Enemies.length;++i)
                enemyCreator.Enemies[i].InUse=false;
            for(i=0;i<particleHandler.Active.length;++i)
                particleHandler.Active[i]=false;
            for(i=0;i<enemyCreator.Missiles.length;++i)
                enemyCreator.Missiles[i].InUse=false;
                enemyCreator.Num_Enemies=0;
            for(i=0;i<Player.Missiles.length;++i)
                Player.Missiles[i].InUse=false;

	}
	/**
	*	@fn setupFlyTransition()
	*	@brief Set up the variables and proponents to the fly transition animation.
	*
	*/
	public void setupFlyTransition()
	{
            flyCushion_T=0.0F;
            g_flyStaticGround = false;// (boolean)(RAND.nextBoolean());
            flyCloud_T=0.0F;
            flyShip_T=0.0F;
            flyMotion_T=0.0F;						
            flyShine_T=0.0F;
            flyCur_Key=0;	
            // - Set up the bezier control points
            flyAnimKeys[0] = Player.Pos;
            flyAnimKeys[1].x = 10;
            flyAnimKeys[1].y = Player.Pos.y;	
            flyAnimKeys[2].x = 10;
            flyAnimKeys[2].y = Room_Height/3;
            flyAnimKeys[3].x = Room_Width/2;
            flyAnimKeys[3].y = Room_Height/3;
	}
	
	/**
	*	@fn addScore();
	*
	*/
	public void addScore(int s){
            g_Score+=s*g_Level*(g_PlayNumber+1);
	}
	
	/** 
	- Just sets main menu to true, and moves the cursor to the desired position.
	*/
	public void showMainMenu() {
            g_MainMenu= true;
            if( g_ContinueMode )
                g_cursor.Pos.y = (208-15);
            else
                g_cursor.Pos.y = 208;
	}
	
	/**
	*	@fn getUpgradeClass
	*	@brief Returns the upgrade level of the player's ship (just a string indicating how many upgrades there are for the player.
	*/
	public String getUpgradeClass( int upgrade ) {
            if( upgrade < upgradeStrings.length )
                return upgradeStrings[upgrade];

            return upgradeStrings[upgradeStrings.length-1];
	}

	/**
	*	@fn setUpgradeStrings();
	*	@brief Sets the upgrade strins upon initialization of the game.
	*/
	public void setUpgradeStrings() {
            // - You can only get the first eight in the first level.
            upgradeStrings = new String[]{ "NEWBIE", "BEGINNER", "GRUNT", "SHIP MAINTAINER",
            "GUN OPERATOR", "BOMBADEER", "MECHANIC", "NOVICE PILOT", "FLIGHT COORDINATOR", "FLIGHT SPECIALIST", "PROFESSIONAL", 
            "EXPERT WINGMAN", "SPACE CADET", "ASTRO-FIGHTER", "SHARP EYE", "ACE FIGHTER", "FABLED FIGHTER", "LEGEND" };
	}
	
	/**
	*	@fn drawBevelString()
	*	@brief Draws a string that looks beveled pertruding from the window.
	*/
	public void drawBevelString( String s, int x, int y, Graphics g ) {
		bevelString(s,x,y,g,2);
	}

        void initMenu()
        {
            _menu = new MenuOrSubMenu(Room_Width/2-32,Room_Height/2-5*8);
            _menu.addMenuItem(_menuitem_newgame,"New Game");            
            _menu.addMenuItem(_menuitem_continue,"Continue");
            _menu.addMenuItem(_menuitem_story,"Story");
            _menu.addMenuItem(_menuitem_special,"Special"); 
            _menu.addMenuItem(_menuitem_options,"Options"); 
            
            _menu.disableItemByName(_menuitem_continue);            
            _menu.disableItemByName(_menuitem_special);

        }
	/**
	*	@fn bevelString()
	*	@brief Draws a string that looks beveled pertruding from the window.
	*/
	public void bevelString( String s, int x, int y, Graphics g, int level ) {
            Color c = g.getColor();
            for( int i=0; i<=level; ++i ) {
                g.setColor(
                    new Color( 
                        (int)((float)c.getRed()*((float)i/(float)level)),
                        (int)((float)c.getGreen()*((float)i/(float)level)),
                        (int)((float)c.getBlue()*((float)i/(float)level))
                    )
                );
                g.drawString(s,(x+(i-level)),(y+(i-level)));
            }
            g.setColor(c);
	}
	
	/**
	*	@fn borderString()
	*	@brief Draws a a string with a border *level pixels around the string.
	*/
	public void borderString( String s, int x, int y, Graphics g, int level ) {
            int c = 190;
            int i;

            for( i=level; i>=0; --i )
            {
                g.setColor( new Color( (int)((float)c*((float)i/(float)level)), (int)((float)c*((float)i/(float)level)), (int)((float)c*((float)i/(float)level)) ) );
                g.drawString( s, x-i, y );
                g.drawString( s, x, y-i );
                g.drawString( s, x+i, y );
                g.drawString( s, x, y+i );
                g.drawString( s, x-i, y-i );
                g.drawString( s, x+i, y-i );
                g.drawString( s, x+i, y+i );
                g.drawString( s, x-i, y+i );
            }

            g.setColor(Color.WHITE);
            g.drawString( s, x, y );
	}
	
	/**
	*	@fn updateHealthblinker()
	*	@brief Updates the health bar blinker with a timer, and player the blink sound.
	*
	*/
	public void updateHealthBlinker() 
        {
            if( (double)getPlayerHealthRatio() <= (double)0.25 )
            {
                if( bt.getState().get()==TimerState.STOPPED )
                {
                    bt.setActionTime( 2000 );	// - Blink every 2 seconds.
                    bt.start();

                    // - Play the stuff on the initial
                    if( !g_EndSequence && !g_EndOfLevelAnimation ) {  
                        playSound(SoundEffects[SOUND_HEALTH_BLINK]);
                    }
                    g_BlinkRatio=1.0F;
                }
                else
                {
                    if( bt.update() > 0 )
                    {
                        // - Play the blinky sound.
                        if( !g_EndSequence && !g_EndOfLevelAnimation ) {
                                playSound(SoundEffects[SOUND_HEALTH_BLINK]);
                        }
                        // - Hilight the health bar.
                        g_BlinkRatio=1.0F;
                    }
                    else
                    {
                        if(g_BlinkRatio>0.0F)
                            g_BlinkRatio-=0.02F;
                        else
                            g_BlinkRatio=0.0F;
                    }
                }
            }
            else 
            {
                if( bt.getState().get()!=TimerState.STOPPED ) {
                if(SoundEffects[SOUND_HEALTH_BLINK] != null )
                    SoundEffects[SOUND_HEALTH_BLINK].stop();	// - Stop the blinky if it was playing.
                if(bt!= null )	
                    bt.stop();
                    g_BlinkRatio=0.0F;
                }
            }
	}
	public double getPlayerHealthRatio(){ return (double)((double)Player.Health/(double)Player.MaxHealth); }
	float cap( float f, float min, float max ){ if(f>max)return max; else if(f<min)return min; return f; }

	/**
	*	@fn updateStars()
	*	@brief Update the starfield in the background.
	*/
	public void updateStars() 
        {
            for( int i=0; i<Stars.length; ++i )
            {
                Stars[i].pos.plusEquals(g_StarSpeed);
                if( Stars[i].pos.x > Room_Width ) Stars[i].pos.x=0;
                if( Stars[i].pos.y > Room_Height ) Stars[i].pos.y=0;
                if( Stars[i].pos.x < 0 ) Stars[i].pos.x = Room_Width;
                if( Stars[i].pos.y < 0 ) Stars[i].pos.y = Room_Height;
            }
	}
	
	/**
	*	@fn renderStars()
	*	@brief Render the starfield in the background.
	*/
	public void renderStars(Graphics myg) 
        {
            int n = 7;
            VECTOR2 ns = new VECTOR2();
            ns.x = g_StarSpeed.x;
            ns.y = g_StarSpeed.y;

            ns.normalize();
            ns.x = -ns.x;
            ns.y = -ns.y;

            for( int i=0; i<Stars.length; ++i )
            {
                for( int j=1; j<n+1; ++j )
                {
                    myg.setColor( new Color(Stars[i].intensity*((float)j/(float)n), Stars[i].intensity*((float)j/(float)n), Stars[i].intensity*((float)j/(float)n)) );
                    myg.fillRect((int)(Stars[i].pos.x- (ns.x*(float)j)),(int)(Stars[i].pos.y-(ns.y*(float)j)),1,1);
                }
            }
            myg.setColor(Color.WHITE);
	}	
	/**
	*	@fn renderStarsPlain()
	*	@brief A second version of the starfield for the game level.
	*/
	public void renderStarsPlain(Graphics myg)
        {
            myg.setColor(Color.WHITE);
            for( int i=0; i<Stars.length; ++i )
            {
                myg.setColor( new Color(Stars[i].intensity, Stars[i].intensity, Stars[i].intensity) );
                myg.fillRect((int)Stars[i].pos.x,(int)Stars[i].pos.y,1,1);
            }
	}
	/**
	*	@fn playSound()
	*	@brief Encapsulation to play a sound effect.
	*/
//	public void playSound( AudioClip a ) { 
//            if(a==null)
//            {
//                System.out.println("Null audio clip.");
//                return;
//            }
//		if( g_UsingSFX ){
//			//Msg("Playing clip");
//			a.play();
//		}
//	}
        public void playSound( MP3 a ) 
        { 
            if(a==null)
            {
                System.out.println("Null audio clip.");
                return;
            }
            if( g_UsingSFX )
            {
                //Msg("Playing clip");
                a.play();
            }
	}
}


