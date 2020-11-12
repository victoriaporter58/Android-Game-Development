package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public abstract class Dialogue extends GameScreen {

    /**
     * This class is an abstract class which creates a basic Dialogue
     *
     * @Author Robert Hawkes <40232279>
     */

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public static final String SCREEN_NAME = "DialogueScreen";
    private String title;
    private String messageLine1;
    private String messageLine2;
    private TextHandler textHandler;
    protected int screenWidth;
    protected int screenHeight;
    private GameObject background;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param game The game in which this overlay will run
     * @param title The title of the dialogue
     * @param messageLine1 The first line of the message
     * @param messageLine2 The second line of the message
     */
    public Dialogue(Game game, String title, String messageLine1, String messageLine2) {
        super(SCREEN_NAME, game);

        //Load the Dialogue base assets
        game.getAssetManager().loadAssets("txt/assets/DialogueAssets.JSON");

        //Initialize local variable with parameter value
        this.title = title;
        this.messageLine1 = messageLine1;
        this.messageLine2 = messageLine2;
        screenWidth = game.getScreenWidth();
        screenHeight = game.getScreenHeight();
        background= new GameObject(mDefaultScreenViewport.centerX(),mDefaultScreenViewport.centerY(),screenWidth/1.25f,screenHeight/1.25f,getGame().getAssetManager().getBitmap("Background"),this);
        textHandler = new TextHandler(game);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters:
    // /////////////////////////////////////////////////////////////////////////

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageLine1() {
        return messageLine1;
    }

    public String getMessageLine2() {
        return messageLine2;
    }

    public TextHandler getTextHandler() {
        return textHandler;
    }

    public void setTextHandler(TextHandler textHandler) {
        this.textHandler = textHandler;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public GameObject getBackground() {
        return background;
    }

    public void setBackground(GameObject background) {
        this.background = background;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void update(ElapsedTime elapsedTime) {
        background.update(elapsedTime);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        background.draw(elapsedTime, graphics2D);
        textHandler.drawText(TextType.SUBTITLE,title,screenWidth/2.5f, screenHeight/3.75f, Color.BLACK, graphics2D);
        textHandler.drawText(TextType.BODY,messageLine1,screenWidth/4.0f, screenHeight/2.50f, Color.BLACK, graphics2D);
        textHandler.drawText(TextType.BODY,messageLine2,screenWidth/2.5f, screenHeight/2.0f, Color.BLACK, graphics2D);
    }

    /**
     * An abstract method that will be determine by each derived class on how to handle the response
     * @return Returns the response for the screen
     */
    public abstract int getResponse();

}