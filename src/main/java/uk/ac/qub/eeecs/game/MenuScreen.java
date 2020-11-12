package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.CharacterSelectScreen;
import uk.ac.qub.eeecs.game.miscDemos.DemoMenuScreen;
import uk.ac.qub.eeecs.game.platformDemo.PlatformDemoScreen;
import uk.ac.qub.eeecs.game.optionsScreen.OptionsScreen;
import uk.ac.qub.eeecs.game.spaceDemo.SpaceshipDemoScreen;


import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * An exceedingly basic menu screen with a couple of touch buttons
 *
 * @version 1.0
 */
public class MenuScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define the buttons for playing the 'games'
     */
    private PushButton mSpaceshipDemoButton;
    private PushButton mPlatformDemoButton;
    private PushButton mCardDemoButton;
    private PushButton mDemosButton;
    private PushButton mSaveTheWorldButton;

    private int masterVolume;
    private SharedPreferences shp;
    private SharedPreferences.Editor shpEditor;

    // Add a button link to the options screen
    private PushButton mOptionsScreenButton;

    // Add a button link to the performance screen
    private PushButton mPerformanceScreenButton;
    private boolean istestRunning;

    AudioManager audioManager;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a simple menu screen
     *
     * @param game Game to which this screen belongs
     */
    public MenuScreen(Game game, Boolean testRunning) {
        super("MenuScreen", game);

        istestRunning = testRunning;

        audioManager = getGame().getAudioManager();

        shp = this.getGame().getActivity().getApplicationContext()
                .getSharedPreferences("SharedPreferencesValues.txt", MODE_PRIVATE);

        shpEditor  = shp.edit();

        masterVolume = shp.getInt("musicVolume", 5);


        if(istestRunning == false){
            setUpScreen();
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public void setUpScreen(){

        // Load in the bitmaps used on the main menu screen
        AssetManager assetManager = mGame.getAssetManager();

        assetManager.loadAssets("txt/assets/MenuScreenAssets.JSON");

        assetManager.loadAndAddBitmap("SpaceDemoIcon", "img/SpaceDemoIcon.png");
        assetManager.loadAndAddBitmap("SpaceDemoIconSelected", "img/SpaceDemoIconSelected.png");
        assetManager.loadAndAddBitmap("CardDemoIcon", "img/CardDemoIcon.png");
        assetManager.loadAndAddBitmap("CardDemoIconSelected", "img/CardDemoIconSelected.png");
        assetManager.loadAndAddBitmap("PlatformDemoIcon", "img/PlatformDemoIcon.png");
        assetManager.loadAndAddBitmap("PlatformDemoIconSelected", "img/PlatformDemoIconSelected.png");
        assetManager.loadAndAddBitmap("DemosIcon", "img/DemosIcon.png");
        assetManager.loadAndAddBitmap("DemosIconSelected", "img/DemosIconSelected.png");
        assetManager.loadAndAddBitmap("SaveTheWorldIcon", "img/SaveTheWorldIcon.png");
        assetManager.loadAndAddBitmap("SaveTheWorldIconSelected", "img/SaveTheWorldIconSelected.png");
        //Load in bitmap for Options Screen
        assetManager.loadAndAddBitmap("OptionsIcon", "img/OptionsIcon.png");
        assetManager.loadAndAddBitmap("OptionsIconSelected", "img/OptionsIconSelected.png");

        //Load in bitmap for Performance Screen
        assetManager.loadAndAddBitmap("PerformanceIcon", "img/PerformanceIcon.png");
        assetManager.loadAndAddBitmap("PerformanceIconSelected", "img/PerformanceIconSelected.png");

        // Define the spacing that will be used to position the buttons
        int spacingX = (int)mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int)mDefaultLayerViewport.getHeight() / 4;

        // Create the trigger buttons
        mSpaceshipDemoButton = new PushButton(
                spacingX * 0.50f, spacingY * 3.0f, spacingX, spacingY,
                "SpaceDemoIcon", "SpaceDemoIconSelected",this);
        mSpaceshipDemoButton.setPlaySounds(true, true);
        mPlatformDemoButton = new PushButton(
                spacingX * 1.83f, spacingY * 3.0f, spacingX, spacingY,
                "PlatformDemoIcon", "PlatformDemoIconSelected", this);
        mPlatformDemoButton.setPlaySounds(true, true);
        mCardDemoButton = new PushButton(
                spacingX * 3.17f, spacingY * 3.0f, spacingX, spacingY,
                "CardDemoIcon", "CardDemoIconSelected", this);
        mCardDemoButton.setPlaySounds(true, true);
        mDemosButton = new PushButton(
                spacingX * 4.50f, spacingY * 3.0f, spacingX, spacingY,
                "DemosIcon", "DemosIconSelected", this);
        mDemosButton.setPlaySounds(true, true);


        // Create trigger button for Options Screen
        mOptionsScreenButton = new PushButton(
                spacingX * 0.50f, spacingY * 1.0f, spacingX, spacingY,
                "OptionsIcon", "OptionsIconSelected", this);
        mOptionsScreenButton.setPlaySounds(true, true);

        // Create trigger button for Options Screen
        mPerformanceScreenButton = new PushButton(
                spacingX * 1.83f, spacingY * 1.0f, spacingX, spacingY,
                "PerformanceIcon", "PerformanceIconSelected", this);
        mPerformanceScreenButton.setPlaySounds(true, true);

        mSaveTheWorldButton = new PushButton(
                spacingX*2.90f, spacingY*1.0f, spacingX, spacingY,
                "SaveTheWorldIcon", "SaveTheWorldIconSelected",this);
        mSaveTheWorldButton.setPlaySounds(true,true);

    }


    /**
     * Update the menu screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        playBackgroundMusic();

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            // Update each button and transition if needed
            mSpaceshipDemoButton.update(elapsedTime);
            mCardDemoButton.update(elapsedTime);
            mPlatformDemoButton.update(elapsedTime);
            mDemosButton.update(elapsedTime);
            mSaveTheWorldButton.update(elapsedTime);

            // Update Options button and transition if needed
            mOptionsScreenButton.update(elapsedTime);

            // Update Performance button and transition if needed
            mPerformanceScreenButton.update(elapsedTime);

            if (mSpaceshipDemoButton.isPushTriggered()){
                stopBackgroundMusic();
                mGame.getScreenManager().addScreen(new SpaceshipDemoScreen(mGame));}
            else if (mCardDemoButton.isPushTriggered()){
                stopBackgroundMusic();
                mGame.getScreenManager().addScreen(new CharacterSelectScreen(mGame));}
            else if (mPlatformDemoButton.isPushTriggered()){
                stopBackgroundMusic();
                mGame.getScreenManager().addScreen(new PlatformDemoScreen(mGame));}
            else if (mDemosButton.isPushTriggered())
                mGame.getScreenManager().addScreen(new DemoMenuScreen(mGame));
            else if (mOptionsScreenButton.isPushTriggered())
                mGame.getScreenManager().addScreen(new OptionsScreen(mGame, getGame().getActivity().getApplicationContext()));
            else if (mSaveTheWorldButton.isPushTriggered())
                mGame.getScreenManager().addScreen(new SaveTheWorldMainMenu(mGame, getGame().getActivity().getApplicationContext()));
//            else if (mPerformanceScreenButton.isPushTriggered())
//                mGame.getScreenManager().addScreen(new PerformanceScreen(mGame));

        }
    }

    private void stopBackgroundMusic(){
        audioManager.stopMusic();
    }


    private void playBackgroundMusic() {

        if(!audioManager.isMusicPlaying()){

            audioManager.playMusic(
                    getGame().getAssetManager().getMusic("OptionsBackgroundMusic"));
            audioManager.setMusicVolume((float)masterVolume/10);
        }
    }

    /**
     * Draw the menu screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Clear the screen and draw the buttons
        graphics2D.clear(Color.WHITE);

        if(istestRunning == false){
            drawButtons(elapsedTime, graphics2D);
        }

    }

    public void drawButtons(ElapsedTime elapsedTime, IGraphics2D graphics2D){

        mSpaceshipDemoButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mPlatformDemoButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mDemosButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mCardDemoButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mOptionsScreenButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mPerformanceScreenButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mSaveTheWorldButton.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
    }

}
