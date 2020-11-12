package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.graphics.Color;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class SplashScreen extends GameScreen {
    /**
     * This is the initial screen in our system. It displays for 5 seconds or it will disappear immediately if the user
     * touches the screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    private static final int DELAY = 5;

    private AssetManager assetManager;

    private TextHandler textHandler;

    private GameObject mSplashScreenBackground;

    private Context context;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor:
    // /////////////////////////////////////////////////////////////////////////

    public SplashScreen(Game game, Context context) {
        super("SplashScreen", game);
        assetManager = mGame.getAssetManager();
        textHandler = new TextHandler(game);

        this.context = context;

        setUpAssets();
        setUpBackground();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters:
    // /////////////////////////////////////////////////////////////////////////

    public TextHandler getTextHandler() {
        return textHandler;
    }

    public static int getDelay() { return DELAY; }

    public AssetManager getAssetManager() { return assetManager; }

    public GameObject getSplashScreenBackground() {
        return mSplashScreenBackground;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods:
    // /////////////////////////////////////////////////////////////////////////

    public void setUpAssets(){
        assetManager.loadAssets("txt/assets/SplashScreenAssets.JSON");
    }

    /**
     * Set up the background image.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    private void setUpBackground() {
        int screenWidth = mGame.getScreenWidth();
        int screenHeight = mGame.getScreenHeight();

        mSplashScreenBackground = new GameObject(mDefaultScreenViewport.centerX(), mDefaultScreenViewport.centerY(), screenWidth, screenHeight, assetManager.getBitmap("SplashScreenImage"), this);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        boolean delayTimeElapsed = checkIfDelayTimeHasElapsed(elapsedTime);
        boolean touchEventHappened = checkIfTouchEventHappened(touchEvents);

        launchMainMenuScreen(delayTimeElapsed, touchEventHappened);
    }

    /**
     * Check if a touch event occurred.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public boolean checkIfTouchEventHappened(List<TouchEvent> touchEvents){
        if(touchEvents.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * Check if the delay time has elapsed or if a touch event has happened and launch the main menu
     * screen accordingly.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void launchMainMenuScreen(boolean delayTimeElapsed, boolean touchEventHappened){
        if(delayTimeElapsed || touchEventHappened ) {
            mGame.getScreenManager().removeScreen(this.getName());
            mGame.getScreenManager().addScreen(new SaveTheWorldMainMenu(mGame, context));
        }
    }

    /**
     * Compare the total time elapsed to the DELAY (5 seconds) during which the splash screen is visible, assuming
     * there has been no user interaction with the screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public boolean checkIfDelayTimeHasElapsed(ElapsedTime elapsedTime){
        if(elapsedTime.totalTime > DELAY){
            return true;
        }
        return false;
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        mSplashScreenBackground.draw(elapsedTime,graphics2D);
        textHandler.drawText(TextType.SUBTITLE, "2020", mGame.getScreenWidth()/2.18f, mDefaultScreenViewport.height ,Color.WHITE, graphics2D);
    }
}