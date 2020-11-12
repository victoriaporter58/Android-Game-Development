package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;
import android.graphics.Paint;
import java.util.List;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.SaveTheWorldMainMenu;
import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;
import uk.ac.qub.eeecs.game.TextHandler;
import uk.ac.qub.eeecs.game.TextType;

/**
 * A screen for the actor to select their champion to play in the game.
 * The choice of champion decides which deck the actor will also use
 *
 * @Author Blaine McKeever <40237118>
 * @Version 1.0
 */

public class CharacterSelectScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    // Text
    //private Paint textPaint = new Paint();
    private TextHandler textHandler;

    // Deck Selection Buttons
    private PushButton Character1;
    private PushButton Character2;
    private PushButton Character3;

    // UI Buttons and Navigations
    private PushButton mPlayButton;
    private PushButton mBackButton;

    private SharedPreferences sharedPreferences;
    private boolean masterVolumeOn;
    private int masterVolume;
    private AudioManager audioManager;
    private List<TouchEvent> touchEvents;
    private Input input;

    // Champions to store champion data
    Champion oakKing;
    Champion gaia;
    Champion fortuna;
    Champion selectedChampion;

    // Champion Portraits for Deck Selection Buttons
    private GameObject championPortrait1;
    private GameObject championPortrait2;
    private GameObject championPortrait3;

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

    public CharacterSelectScreen(Game game) {
        super("CharacterScreen",game);
        // store width and height of game screen for use throughout this class
        setupViewports();
        setupAssets();
        setUpChampions();
        setUpObjects();
        //setUpStandardTextStyle();

        textHandler = new TextHandler(mGame);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    private void setupViewports(){
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

    private void setupAssets() {
        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAssets("txt/assets/CharacterScreenAssets.JSON");
        assetManager.loadAndAddBitmap("oakKing","img/oakKing.jpg");
        assetManager.loadAndAddBitmap("gaia","img/gaia.png");
        assetManager.loadAndAddBitmap("fortuna","img/Fortuna.jpg");
        assetManager.loadAndAddBitmap("PlayButton","img/PlayButton.png");
        assetManager.loadAndAddBitmap("PlayButtonSelected","img/PlayButtonSelected.png");
        assetManager.loadAndAddBitmap("soil_bg","img/soil_bg.jpg");
    }

    private void setUpChampions() {
        oakKing = new Champion("Oak King","txt/decks/OakKingDeck.JSON", 30, 8, mGame.getAssetManager().getBitmap("oakKing"));
        gaia = new Champion("Gaia","txt/decks/GaiaDeck.JSON", 25, 10, mGame.getAssetManager().getBitmap("gaia"));
        fortuna = new Champion("Fortuna","txt/decks/FortunaDeck.JSON", 20, 12, mGame.getAssetManager().getBitmap("fortuna"));
    }

    public void setUpObjects(){
        background = new GameObject(mDefaultScreenViewport.centerX(),mDefaultScreenViewport.centerY(),mDefaultScreenViewport.width,mDefaultScreenViewport.height,getGame()
                .getAssetManager().getBitmap("soil_bg"),this);

        Character1 = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.3f,mDefaultLayerViewport.getHeight() * 0.5f,
                mDefaultLayerViewport.getWidth() * 0.15f,mDefaultLayerViewport.getHeight() * 0.35f,
                "Frame","FrameSelected",this);
        Character2 = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.5f,mDefaultLayerViewport.getHeight() * 0.5f,
                mDefaultLayerViewport.getWidth() * 0.15f,mDefaultLayerViewport.getHeight() * 0.35f,
                "Frame","FrameSelected",this);
        Character3 = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.7f,mDefaultLayerViewport.getHeight() * 0.5f,
                mDefaultLayerViewport.getWidth() * 0.15f,mDefaultLayerViewport.getHeight() * 0.35f,
                "Frame","FrameSelected",this);

        // Set default Champion
        selectedChampion = gaia;

        mPlayButton = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.15f,mDefaultLayerViewport.getHeight() * 0.15f,
                mDefaultLayerViewport.getWidth() * 0.075f,mDefaultLayerViewport.getHeight() * 0.10f,
                "PlayButton", "PlayButtonSelected",this);
        mBackButton = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.85f, mDefaultLayerViewport.getHeight() * 0.15f,
                mDefaultLayerViewport.getWidth() * 0.075f,mDefaultLayerViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected",this);

        // Create Champion portraits in the frames
        championPortrait1 = new GameObject(
                mDefaultLayerViewport.getWidth() * 0.3f,mDefaultLayerViewport.getHeight() * 0.5f,
                mDefaultLayerViewport.getWidth() * 0.12f,mDefaultLayerViewport.getHeight() * 0.3f,
                getGame().getAssetManager().getBitmap("oakKing"),this);
        championPortrait2 = new GameObject(
                mDefaultLayerViewport.getWidth() * 0.5f,mDefaultLayerViewport.getHeight() * 0.5f,
                mDefaultLayerViewport.getWidth() * 0.12f,mDefaultLayerViewport.getHeight() * 0.3f,
                getGame().getAssetManager().getBitmap("gaia"),this);
        championPortrait3 = new GameObject(
                mDefaultLayerViewport.getWidth() * 0.7f,mDefaultLayerViewport.getHeight() * 0.5f,
                mDefaultLayerViewport.getWidth() * 0.12f,mDefaultLayerViewport.getHeight() * 0.3f,
                getGame().getAssetManager().getBitmap("fortuna"),this);
    }

//    public void setUpStandardTextStyle(){
//        // set up text style for the options screen title
//        textPaint.setTextSize(mDefaultLayerViewport.getHeight() / 10.0f);
//        textPaint.setTextAlign(Paint.Align.CENTER);
//    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        input = mGame.getInput();

        mPlayButton.update(elapsedTime);
        mBackButton.update(elapsedTime);
        Character1.update(elapsedTime);
        Character2.update(elapsedTime);
        Character3.update(elapsedTime);

        if (Character1.isPushTriggered()) {
            selectedChampion = oakKing;
        }
        if (Character2.isPushTriggered()) {
            selectedChampion = gaia;
        }
        if (Character3.isPushTriggered()) {
            selectedChampion = fortuna;
        }
        if (mPlayButton.isPushTriggered()) {
            startCoinToss();
        }
        if (mBackButton.isPushTriggered()) {
            returnToMainMenu();
        }
    }

    public void startCoinToss(){
        mGame.getScreenManager().addScreen(new CoinTossScreen(mGame,selectedChampion));
    }

    public void returnToMainMenu(){
        mGame.getScreenManager().addScreen(new SaveTheWorldMainMenu(mGame, getGame().getActivity().getApplicationContext()));
    }

    public void drawObjects(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        background.draw(elapsedTime, graphics2D);
        championPortrait1.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        championPortrait2.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        championPortrait3.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        Character1.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        Character2.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        Character3.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mPlayButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //Set up for how title text should appear
        float textSize = ViewportHelper.convertXDistanceFromLayerToScreen(
                mDefaultLayerViewport.getHeight() * 0.05f,
                mDefaultLayerViewport, mDefaultScreenViewport);

//        textPaint.setTextSize(textSize);
//        textPaint.setTextAlign(Paint.Align.CENTER);
//        textPaint.setColor(Color.BLACK);


        //graphics2D.drawText("Character Select", mDefaultScreenViewport.centerX(), mDefaultScreenViewport.top + 2.0f * textSize, textPaint);
        //graphics2D.drawText("Selected Deck: " + selectedChampion.getChampionName(), mDefaultScreenViewport.centerX(), mDefaultScreenViewport.top + 3.5f * textSize, textPaint);
        //graphics2D.drawText(oakKing.getChampionName(), mDefaultScreenViewport.width * 0.3f,mDefaultScreenViewport.height * 0.3f, textPaint);
        //graphics2D.drawText(gaia.getChampionName(), mDefaultScreenViewport.width * 0.5f,mDefaultScreenViewport.height * 0.3f, textPaint);
        //graphics2D.drawText(fortuna.getChampionName(), mDefaultScreenViewport.width * 0.7f,mDefaultScreenViewport.height * 0.3f, textPaint);

        // Draw the information text
        textHandler.drawText(TextType.TITLE,"Character Select", mGame.getScreenWidth()/4, mDefaultScreenViewport.top + 2.0f * textSize, Color.BLACK, graphics2D);
        textHandler.drawText(TextType.SUBTITLE,"Selected Deck: "+ selectedChampion.getChampionName(), mGame.getScreenWidth()/2.9f, mGame.getScreenHeight()/4.5f, Color.BLACK, graphics2D);
        textHandler.drawText(TextType.BODY, oakKing.getChampionName(), mDefaultScreenViewport.width * 0.23f, mDefaultScreenViewport.height * 0.3f, Color.BLACK, graphics2D);
        textHandler.drawText(TextType.BODY, gaia.getChampionName(), mDefaultScreenViewport.width * 0.47f, mDefaultScreenViewport.height * 0.3f, Color.BLACK, graphics2D);
        textHandler.drawText(TextType.BODY, fortuna.getChampionName(), mDefaultScreenViewport.width * 0.64f, mDefaultScreenViewport.height * 0.3f, Color.BLACK, graphics2D);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        drawObjects(elapsedTime, graphics2D);
    }
}