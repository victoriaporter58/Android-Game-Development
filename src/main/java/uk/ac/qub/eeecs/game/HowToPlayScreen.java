package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;

public class HowToPlayScreen extends GameScreen {
    /**
     * An informative screen which explains how the user can play the game.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    private int screenWidth;
    private int screenHeight;
    private int pageNumber;
    private int buttonLengthAndWidth;

    private static final int MAX_PAGE_NUMBER = 4;

    private String subtitleText;
    private String bodyText;
    private String assetName;

    private PushButton nextPageButton;
    private PushButton previousPageButton;
    private PushButton returnButton;

    private GameObject cardGameScreenshot;

    private LayerViewport mHowToPlayScreenViewport;

    private TextHandler textHandler;

    private ScreenManager screenManager;

    private AssetManager assetManager;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor:
    // /////////////////////////////////////////////////////////////////////////

    public HowToPlayScreen(Game game) {
        super("HowToPlayScreen", game);
        textHandler = new TextHandler(mGame);
        screenManager = new ScreenManager(mGame);

        assetManager = mGame.getAssetManager();
        screenWidth = mGame.getScreenWidth();
        screenHeight = mGame.getScreenHeight();
        pageNumber = 0;

        loadAssets();
        setUpViewports();
        setUpObjects();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters:
    // /////////////////////////////////////////////////////////////////////////

    public int getButtonLengthAndWidth() {return buttonLengthAndWidth;}

    public String getSubtitleText() {
        return subtitleText;
    }

    public String getBodyText() {
        return bodyText;
    }

    public String getAssetName() {
        return assetName;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getMAX_PAGE_NUMBER() {
        return MAX_PAGE_NUMBER;
    }

    public PushButton getNextPageButton() {
        return nextPageButton;
    }

    public PushButton getPreviousPageButton() {
        return previousPageButton;
    }

    public PushButton getReturnButton() {
        return returnButton;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextHandler getTextHandler() {
        return textHandler;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods:
    // /////////////////////////////////////////////////////////////////////////

    private void setUpViewports() {
        mDefaultScreenViewport.set(0, 0, screenWidth, screenHeight);

        float layerHeight = screenHeight * (480.0f / screenWidth);
        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mHowToPlayScreenViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    /**
     * This method loads the relevant asset file for this screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    private void loadAssets() {
        assetManager.loadAssets("txt/assets/HowToPlayAssets.JSON");
    }

    /**
     * This method dynamically sizes and positions buttons on the screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setUpObjects() {
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 8;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 2;

        // Define dynamic size of square buttons
        buttonLengthAndWidth = screenHeight/20;

        // Dynamically position the buttons on the screen
        nextPageButton = new PushButton(
                spacingX*7.0f, spacingY, buttonLengthAndWidth, buttonLengthAndWidth,
                "NextPageButton", "NextPageButton", this);

        previousPageButton = new PushButton(
                spacingX, spacingY, buttonLengthAndWidth, buttonLengthAndWidth,
                "PreviousPageButton", "PreviousPageButton", this);

        returnButton = new PushButton(
                spacingX*7.5f, spacingY*1.75f, buttonLengthAndWidth, buttonLengthAndWidth,
                "BackButton", "BackButtonSelected", this);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        returnButton.update(elapsedTime);

        // Update next and previous buttons only if required (i.e. if they're currently visible)
        if(pageNumber < MAX_PAGE_NUMBER){
            nextPageButton.update(elapsedTime);
        }
        if(pageNumber > 0){
            previousPageButton.update(elapsedTime);
        }

        // Handle output based on user interaction
        if (returnButton.isPushTriggered()) {
            backToOptions();
        }
        if(nextPageButton.isPushTriggered()){
            incrementPageNumber();
        }
        if(previousPageButton.isPushTriggered()){
            decrementPageNumber();
        }
    }

    /**
     * Increment page number by 1
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void incrementPageNumber(){pageNumber++;}

    /**
     * Decrement pageNumber by 1
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void decrementPageNumber(){pageNumber--;}

    /**
     * This method closes the How to Play screen leaving the previous screen visible i.e. Options
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void backToOptions() {
        mGame.getScreenManager().removeScreen(this);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        textHandler.drawText(TextType.TITLE,"How To Play", screenWidth/2.9f, screenHeight/7.0f, Color.BLACK, graphics2D);

        returnButton.draw(elapsedTime, graphics2D, mHowToPlayScreenViewport, mDefaultScreenViewport);

        if(pageNumber < MAX_PAGE_NUMBER){
            nextPageButton.draw(elapsedTime, graphics2D, mHowToPlayScreenViewport, mDefaultScreenViewport);
        }
        if(pageNumber > 0){
            previousPageButton.draw(elapsedTime, graphics2D, mHowToPlayScreenViewport, mDefaultScreenViewport);
        }

        drawPage(elapsedTime, graphics2D);
    }

    /**
     * This method draws all text information to the screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void drawPage(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        setInformationTextForPage();

        drawSpecificPageInformation(subtitleText, bodyText, assetName, elapsedTime, graphics2D);
    }

    /**
     * This method makes use of the pageNumber constant and draws the relevant page to the screen via a switch statement.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setInformationTextForPage(){
        switch(pageNumber){
            case 0:
                subtitleText = "Viewing Cards";
                bodyText = "Long press any card to view specific information.";
                assetName = "EnlargedCardScreenshot";
                break;
            case 1:
                subtitleText = "Playing Cards";
                bodyText = "Drag your card(s) onto the board.";
                assetName = "DragCardScreenshot";
                break;
            case 2:
                subtitleText = "Attacking";
                bodyText = "Drag a card onto opponents avatar to reduce their health.";
                assetName = "DragCardToAvatarScreenshot";
                break;
            case 3:
                subtitleText = "Attacking";
                bodyText = "Drag a card onto opponents card(s) to reduce its health.";
                assetName = "DragCardToCardScreenshot";
                break;
            case 4:
                subtitleText = "Ending Your Turn";
                bodyText = "Press End Turn when you're finished moving cards.";
                assetName = "EndTurnScreenshot";
                break;
            default:
                subtitleText = "Page Doesn't Exist!";
                bodyText = "This page does not exist yet.";
                assetName = "Error";
                break;
        }
    }

    /**
     * This method draws specific information to the screen based on the page number.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void drawSpecificPageInformation(String subtitleText, String bodyText, String assetName, ElapsedTime elapsedTime, IGraphics2D graphics2D){
        textHandler.drawText(TextType.SUBTITLE,subtitleText, screenWidth/20.0f, screenHeight/4.5f, Color.BLACK, graphics2D);
        textHandler.drawText(TextType.BODY,bodyText, screenWidth/20.0f, screenHeight/3.5f, Color.BLACK, graphics2D);

        cardGameScreenshot = new GameObject(screenWidth/2,screenHeight/1.5f,screenWidth/2,screenHeight/1.75f, getGame()
                .getAssetManager().getBitmap(assetName),this);

        cardGameScreenshot.draw(elapsedTime, graphics2D);
    }
}