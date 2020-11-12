package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.graphics.Color;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;
import uk.ac.qub.eeecs.game.cardDemo.CharacterSelectScreen;
import uk.ac.qub.eeecs.game.optionsScreen.OptionsScreen;

import android.graphics.Paint;

/**
 * A Class for creating the game's main menu
 *
 * @Author Blaine McKeever <40237118>
 * @Version 1.0
 */

public class SaveTheWorldMainMenu extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    // Audio
    private SharedPreferences sharedPreferences;
    private Boolean sfxVolumeOn;
    private Boolean masterVolumeOn;
    private float masterVolume;
    //private AudioManager audioManager;
    AssetManager assetManager;

    // Animated buttons
    private AnimatedPlayButton mAnimatedPlayButton;
    private AnimatedOptionsButton mAnimatedOptionsButton;

    // Text
    private Paint textPaint = new Paint();

    // Background Image
    private GameObject background;

    // Viewport parameters
    // World size within which game objects will be created and the viewport will be confined in
    // These are set to the default viewport widths currently. The viewport will match the size of the
    // Game world
    private static float WORLD_WIDTH = 480;
    private static float WORLD_HEIGHT = 320;

    // Set up two viewports, one for the game layer and one for the screen
    // Layer viewport will provide a window into the large game world
    private LayerViewport mGameLayerViewport;

    // Screen viewport will display and game objects within it to te screen of the device
    private ScreenViewport mGameScreenViewport;

    // Define layer viewport width
    private final static float FOCUSED_VIEWPORT_WIDTH = 480.0f;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    // Create the SaveTheWorldMainMenu
    public SaveTheWorldMainMenu(Game game, Context context) {
        super("SaveTheWorldMainMenu", game);

        // initialise shared preferences object with options game screen
        sharedPreferences = new SharedPreferences(context);

        setUpViewports();
        setUpAssets();
        setUpAudio();
        setUpAnimatedButtons();

        sharedPreferences.setBooleanValue("mainGameActive", false);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    private void setUpViewports() {
        // Create new viewports instead of using defaults
        float screenWidth = mGame.getScreenWidth();
        float screenHeight = mGame.getScreenHeight();
        mDefaultScreenViewport.set(0,0,mGame.getScreenWidth(),mGame.getScreenHeight());

        // Build the layer viewport. The game viewport is sized based on a specified width value
        mGameLayerViewport = new LayerViewport(
                WORLD_WIDTH / 2.0f, WORLD_HEIGHT / 2.0f,
                WORLD_WIDTH / 2.0f, WORLD_HEIGHT / 2.0f);

        float aspectRatio = screenHeight / screenWidth;

        // Build game screen viewport (Take over all drawable space on screen)
        mGameScreenViewport = new ScreenViewport(0, 0, (int) screenWidth, (int) screenHeight);
    }

    private void setUpAudio() {
        //audioManager = getGame().getAudioManager();




        masterVolumeOn = sharedPreferences.getBooleanValue("masterVolumeOn", false);
        masterVolume = sharedPreferences.getFloatValue("musicVolume", 5.0f);
    }

    private void setUpAssets() {
        // Load in the assets used using the asset manager
        assetManager = mGame.getAssetManager();

        // Load in assets specified in JSON file
        assetManager.loadAssets("txt/assets/MainMenuAssets.JSON");
        background = new GameObject(mDefaultScreenViewport.centerX(),mDefaultScreenViewport.centerY(),mDefaultScreenViewport.width,mDefaultScreenViewport.height,getGame()
                .getAssetManager().getBitmap("MainMenuBackground"),this);
    }

    private void setUpAnimatedButtons() {
        // Create and position the animated play button and enable sound feedback
        mAnimatedPlayButton = new AnimatedPlayButton(
                mDefaultLayerViewport.getWidth() * 0.25f,mDefaultLayerViewport.getHeight() * 0.5f,
                mDefaultLayerViewport.getWidth() * 0.3f,mDefaultLayerViewport.getHeight() * 0.3f,
                "PlayButton", "PlayButtonSelected", this);
        mAnimatedPlayButton.setPlaySounds(masterVolumeOn, masterVolumeOn);

        //Create and position the animated options button and enable sound feedback
        mAnimatedOptionsButton = new AnimatedOptionsButton(
                mDefaultLayerViewport.getWidth() * 0.75f, mDefaultLayerViewport.getHeight() * 0.5f,
                mDefaultLayerViewport.getWidth() * 0.3f,mDefaultLayerViewport.getHeight() * 0.3f,
                "OptionsButton", "OptionsButtonSelected", this);
        mAnimatedOptionsButton.setPlaySounds(masterVolumeOn, masterVolumeOn);
    }

    public void animatedLaunchGame() {
        if (mAnimatedPlayButton.isPushTriggered()) {
            mGame.getScreenManager().addScreen(new CharacterSelectScreen(mGame));
        }
    }

    public void animatedLaunchOptions() {
        if (mAnimatedOptionsButton.isPushTriggered()) {
            mGame.getScreenManager().addScreen(new OptionsScreen(mGame,getGame().getActivity().getApplicationContext()));
        }
    }

    // Update the SaveTheWorldMainMenu Screen
    @Override
    public void update(ElapsedTime elaspsedTime) {
        mAnimatedPlayButton.update(elaspsedTime);
        mAnimatedPlayButton.updateAnimation(elaspsedTime);
        animatedLaunchGame();

        mAnimatedOptionsButton.update(elaspsedTime);
        mAnimatedOptionsButton.updateAnimation(elaspsedTime);
        animatedLaunchOptions();
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Clear what is currently on the screen and draw the new buttons
        graphics2D.clear(Color.WHITE);
        background.draw(elapsedTime, graphics2D);
        //Set up for how title text should appear
        float textSize = ViewportHelper.convertXDistanceFromLayerToScreen(
                            mDefaultLayerViewport.getHeight() * 0.05f,
                            mDefaultLayerViewport, mDefaultScreenViewport);

        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLACK);

        // Draw text displaying the name of the game
        //graphics2D.drawText("QUB Save Our Planet!", mDefaultScreenViewport.centerX(), mDefaultScreenViewport.top + 2.0f * textSize, textPaint);

        // Draw the animated buttons
        mAnimatedPlayButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mAnimatedOptionsButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }
}
