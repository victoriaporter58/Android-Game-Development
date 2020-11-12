package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;
import uk.ac.qub.eeecs.game.cardDemo.AI;
import uk.ac.qub.eeecs.game.cardDemo.AIMoveInformation;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Card;
import uk.ac.qub.eeecs.game.cardDemo.CardMove;
import uk.ac.qub.eeecs.game.cardDemo.Deck;
import uk.ac.qub.eeecs.game.cardDemo.GameLogic;
import uk.ac.qub.eeecs.game.cardDemo.GameTurnSystem;
import uk.ac.qub.eeecs.game.cardDemo.GlobalMessage;
import uk.ac.qub.eeecs.game.cardDemo.Player;
import uk.ac.qub.eeecs.game.cardDemo.PlayerTurn;
import uk.ac.qub.eeecs.game.optionsScreen.OptionsScreen;

/**
 * This class represents the Game Performance screen which primarily uses code
 * from Justin Johnston's <40237507> codebase extended upon with two AI players
 * who play against one another in order to determine the best performance
 * from the game.
 *
 * A further extension of the screen I would love to do but didn't have time
 * to complete would be to change the resolution and graphics to different
 * qualities depending on what run best on the phone as well as ensuring
 * each player who players the game on their phone
 * has good performance which doesn't cause screen stutering or any visual glitches.
 *
 * @Author Robert Hawkes <40232279>
 * @Author Justin Johnston <40237507>
 */
public class GamePerformanceScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public static final String SCREEN_NAME = "GamePerformanceScreen";
    private final int DEFAULT_HEALTH = 30;
    private final int DEFAULT_MANA = 8;
    private final int MANA_PER_TURN = 2;
    private final int MAX_RUN_TIME = 60;

    private Card activeCard = null;

    //Define graphics elements
    private GameObject mBoardBackground;

    private Deck mPlayer1Deck;
    private Deck mPlayer2Deck;

    //HUD elements
    private GlobalMessage globalMessage;

    //Define relevant models
    private Board mBoard;
    private Player mPlayer1;
    private Player mPlayer2;
    private AI mAIPlayer1;
    private AI mAIPlayer2;
    private AudioManager audioManager;
    private GameLogic mGameLogicPlayer1;
    private GameLogic mGameLogicPlayer2;

    private PushButton backButton;

    private GameTurnSystem gameTurnSystem;
    private PlayerTurn playerturn;

    private Boolean masterVolumeOn;
    private float masterVolume;
    private SharedPreferences sharedPreferences;

    private AssetManager assetManager;
    private ScreenManager screenManager;

    //Performance Metric
    private FramesPerSecondCounter framesPerSecondCounter;
    private FloatCyclicQueue fpsHistory;
    private double startPerformanceTime;
    private double currentElapsedTime;

    private Context context;



    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public GamePerformanceScreen(Game game, Context context) {
        super(SCREEN_NAME, game);
        this.context = context;
        assetManager = new AssetManager(game);
        this.screenManager = game.getScreenManager();
        sharedPreferences = new SharedPreferences(context);
        framesPerSecondCounter = new FramesPerSecondCounter(game, context);
        fpsHistory = new FloatCyclicQueue(7200); // 120 updates per second x max run time seconds

        globalMessage = new GlobalMessage();

        audioManager = new AudioManager(this.getGame());

        //Load the assets needed
        mGame.getAssetManager().loadAssets("txt/assets/SaveTheWorldScreenAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/GamePerformanceAssets.JSON");

        setUpAudio();
        setupPlayer();
        setupViewports();
        setupBackground();
        //We are using the Gaia Deck as a performance tester
        setupCardGameObjects("txt/decks/GaiaDeck.JSON");
        setupObjects();
        setupGameStart();

        //Set the playerTurn
        playerturn = PlayerTurn.OPPONENT;
        globalMessage.addMessage("Player 1 Turn");
        mAIPlayer1.startTurn();

    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters:
    // /////////////////////////////////////////////////////////////////////////

    public GameObject getmBoardBackground() {
        return mBoardBackground;
    }

    public Deck getmPlayer1Deck() {
        return mPlayer1Deck;
    }

    public Deck getmPlayer2Deck() {
        return mPlayer2Deck;
    }

    public Board getmBoard() {
        return mBoard;
    }

    public Player getmPlayer1() {
        return mPlayer1;
    }

    public Player getmPlayer2() {
        return mPlayer2;
    }

    public AI getmAIPlayer1() {
        return mAIPlayer1;
    }

    public AI getmAIPlayer2() {
        return mAIPlayer2;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public GameLogic getmGameLogicPlayer1() {
        return mGameLogicPlayer1;
    }

    public GameLogic getmGameLogicPlayer2() {
        return mGameLogicPlayer2;
    }

    public PushButton getBackButton() {
        return backButton;
    }

    public GameTurnSystem getGameTurnSystem() {
        return gameTurnSystem;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public FramesPerSecondCounter getFramesPerSecondCounter() {
        return framesPerSecondCounter;
    }

    public FloatCyclicQueue getFpsHistory() {
        return fpsHistory;
    }

    public void setStartPerformanceTime(double startPerformanceTime) {
        this.startPerformanceTime = startPerformanceTime;
    }

    public void setCurrentElapsedTime(double currentElapsedTime) {
        this.currentElapsedTime = currentElapsedTime;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * This method sets up the audio and gets master volume from shared preferences
     * This code has been taken from Save The World Screen originally created by
     * @Author Justin Johnston <40237507>
     */
    private void setUpAudio(){
        audioManager = getGame().getAudioManager();
        masterVolumeOn = sharedPreferences.getBooleanValue(sharedPreferences.getMasterVolumeOnLabel(), false);
        masterVolume = sharedPreferences.getFloatValue(sharedPreferences.getMusicVolumeLabel(), 5.0f);
        playBackgroundMusic();
    }

    /**
     * This method sets up the player portaits
     * This code has been taken from Save The World Screen originally created by
     * @Author Justin Johnston <40237507>
     */
    private void setupPlayer() {
        float width = mDefaultLayerViewport.getWidth() * 0.35f;
        float height = mDefaultLayerViewport.getHeight() * 0.72f;

        float xPosition = mDefaultLayerViewport.getWidth() * 3.35f;
        float yPosition1 = mDefaultLayerViewport.getHeight() * 2.10f;
        float yPosition2 = mDefaultLayerViewport.getHeight() * 1.30f;

        mPlayer1 = new Player(xPosition,yPosition1,DEFAULT_HEALTH,DEFAULT_MANA,"playerPortrait",this, false);
        mPlayer1.getPlayerPortrait().setWidth(width);
        mPlayer1.getPlayerPortrait().setHeight(height);

        mPlayer2 = new Player(xPosition,yPosition2,DEFAULT_HEALTH,DEFAULT_MANA,"enemyPortrait",this, false);
        mPlayer2.getPlayerPortrait().setWidth(width);
        mPlayer2.getPlayerPortrait().setHeight(height);
    }

    /**
     * This method sets the objects used for both AI to play the card game,
     * @Author Justin Johnston <40237507>
     * @Author Robert Hawkes <40232279> (Worked with Justin to make sure that both AI can have their own objects independently)
     */
    private void setupCardGameObjects(String jsonLocation){

        //Initialise decks and GameLogic for player1 and player2
        mPlayer1Deck = new Deck(jsonLocation,this,mGame);
        mPlayer2Deck = new Deck("txt/decks/GaiaDeck.JSON",this,mGame);//same Deck for testing purpose

        mGameLogicPlayer1 = new GameLogic(mBoard, mPlayer1Deck,mPlayer2Deck,mPlayer1,mPlayer2,globalMessage, audioManager, this);
        mGameLogicPlayer2 = new GameLogic(mBoard,mPlayer2Deck, mPlayer1Deck,mPlayer2,mPlayer1,globalMessage, audioManager, this);

        mAIPlayer1 = new AI(mBoard,mPlayer1Deck, mPlayer2Deck,mPlayer1,mPlayer2,1);
        mAIPlayer2 = new AI(mBoard,mPlayer2Deck, mPlayer1Deck,mPlayer2,mPlayer1,2);

        mPlayer1.setDeck(mPlayer1Deck);
        mPlayer2.setDeck(mPlayer2Deck);

        mPlayer1Deck.placeDeckOnBoard(
                mBoard.getBoardComponents().get(mBoard.PLAYER1BOARDDECKSPACE).position.x,
                mBoard.getBoardComponents().get(mBoard.PLAYER1BOARDDECKSPACE).position.y);

        mPlayer2Deck.placeDeckOnBoard(
                mBoard.getBoardComponents().get(mBoard.PLAYER2BOARDDECKSPACE).position.x,
                mBoard.getBoardComponents().get(mBoard.PLAYER2BOARDDECKSPACE).position.y);

        gameTurnSystem = new GameTurnSystem(mBoard);
    }

    /**
     * This method sets up the viewports
     * This code has been taken from Save The World Screen originally created by
     * @Author Justin Johnston <40237507>
     */
    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set( 0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserve the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight/2.0f, 240.0f, layerHeight/2.0f);
    }

    /**
     * This method sets up the background and the board.
     * This code has been taken from Save The World Screen originally created by
     * @Author Justin Johnston <40237507>
     */
    private void setupBackground() {
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());
        // Create the space background
        mBoardBackground = new GameObject(240.0f, layerHeight/2.0f, 480.0f, layerHeight
                , getGame()
                .getAssetManager().getBitmap("BoardBackground"), this);
        mBoard = new Board((int)(mDefaultLayerViewport.getWidth()*0.50f),(int)(mDefaultLayerViewport.getHeight()*0.50f),(int)(mDefaultLayerViewport.getWidth()*1.0f),
                (int)(mDefaultLayerViewport.getHeight()*0.9f),this);
    }

    /**
     * This method sets up the buttons seen on the screen
     * @Author Robert Hawkes <40232279>
     */
    private void setupObjects(){
        float backPositionX = mBoard.getBoardComponents().get(mBoard.PLAYER2BOARDDECKSPACE).position.x +mBoard.COMPONENTSIZE;
        float backPositionY = mBoard.getBoardComponents().get(mBoard.PLAYER2BOARDDECKSPACE).position.y;
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        backButton = new PushButton(
                backPositionX, backPositionY, spacingX * 0.6f, spacingY * 0.6f,
                "BackArrow", "BackArrowSelected", this);
    }

    /**
     * This method sets up the board dynamically using the Board constants.
     * This code has been taken from Save The World Screen originally created by
     * @Author Justin Johnston <40237507>
     */
    private void setupGameStart(){

        // Define a card to be displayed
        // Define a list of cards to be displayed
        //private List<Card> mCards;
        for(int i = 0; i < mBoard.INITIALHANDSIZE; i++){
            mPlayer1Deck.drawCard(mBoard.getBoardComponents().get(mBoard.PLAYER1BOARDDECKSPACE).position.x,mBoard.getBoardComponents().get(mBoard.PLAYER1BOARDDECKSPACE).position.y);
            new CardMove(mPlayer1Deck.getActiveCards().get(i).position,mBoard.getBoardComponents().get(i).position,mBoard,i, mGameLogicPlayer1,false,PlayerTurn.PLAYER);
        }

        for(int i = 0 ; i < mBoard.INITIALHANDSIZE;i++) {
            mPlayer2Deck.drawCard(mBoard.getBoardComponents().get(mBoard.PLAYER2BOARDDECKSPACE).position.x, mBoard.getBoardComponents().get(mBoard.PLAYER2BOARDDECKSPACE).position.y);
            new CardMove(mPlayer2Deck.getActiveCards().get(i).position, mBoard.getBoardComponents().get(i + mBoard.PLAYER2HANDSPACE).position, mBoard, i, mGameLogicPlayer2, false, PlayerTurn.OPPONENT);
        }
    }

    /**
     * This method plays the background music.
     * This code has been taken from Save The World Screen originally created by
     * @Author Justin Johnston <40237507>
     */
    private void playBackgroundMusic() {
        //AssetManager audioAssetManager = mGame.getAssetManager();
        //mGame.getAssetManager().loadAssets("txt/assets/SaveTheWorldScreenAssets.JSON");
        masterVolumeOn = sharedPreferences.getBooleanValue(sharedPreferences.getMasterVolumeOnLabel(), false);
        if(masterVolumeOn && !audioManager.isMusicPlaying()){
            audioManager.playMusic(mGame.getAssetManager().getMusic("MainGameBackgroundMusic"));
            audioManager.setMusicVolume(masterVolume);
            audioManager.setSfxVolume(masterVolume);
        }else if(!masterVolumeOn){
            audioManager.setSfxVolume(0.0f);
        }
    }

    /**
     * This method ensures that the turn is switched between each both AI
     * @Author Justin Johnston <40237507>
     * @Author Robert Hawkes <40232279> (Worked with Justin so that a turn could be an AI or a player)
     */
    private void switchTurn() {
        if(mGameLogicPlayer1.isEmpty() && mGameLogicPlayer2.isEmpty()){
            boolean cardReturned;
            if(playerturn == PlayerTurn.PLAYER){
                playerturn = PlayerTurn.OPPONENT;
                mPlayer2.incrementMana(MANA_PER_TURN);
                cardReturned = gameTurnSystem.switchTurn(mPlayer2Deck,PlayerTurn.OPPONENT,mGameLogicPlayer2);
                globalMessage.addMessage("Player 2 Turn");
                mAIPlayer2.startTurn();
                if(!cardReturned){
                    globalMessage.setSubMessage("can not draw card");
                }
            }else if (playerturn == PlayerTurn.OPPONENT){
                playerturn = PlayerTurn.PLAYER;
                mPlayer1.incrementMana(MANA_PER_TURN);
                cardReturned = gameTurnSystem.switchTurn(mPlayer1Deck,PlayerTurn.PLAYER, mGameLogicPlayer1);
                globalMessage.addMessage("Player 1 Turn");
                mAIPlayer1.startTurn();
                if(!cardReturned){
                    globalMessage.setSubMessage("can not draw card");
                }
            }
        }
    }

    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        currentElapsedTime = elapsedTime.totalTime;
        checkAndAssignStartTime(elapsedTime);

        backButton.update(elapsedTime);
        framesPerSecondCounter.update(elapsedTime);

        if(backButton.isPushTriggered()){
            exitGamePerformance();
        }

        // Update the players cards
        for (Card card : mPlayer1Deck.getActiveCards()) {
            card.update(elapsedTime);
        }
        for (Card card : mPlayer2Deck.getActiveCards()) {
            card.update(elapsedTime);
        }

        //Update portraits - Implementation 02
        mPlayer1.updatePortrait();
        mPlayer2.updatePortrait();
        checkEndCondition();

        globalMessage.update(elapsedTime);
    }

    /**
     * This method checks if the start time for the screen is null (Therefore it hasn't been checked/assigned before)
     * and sets it to the current Elapsed Time of the update loop
     * @param elapsedTime
     * @Author Robert Hawkes <40232279>
     */
    public void checkAndAssignStartTime(ElapsedTime elapsedTime) {
        if(startPerformanceTime == 0.0f) {
            startPerformanceTime = elapsedTime.totalTime;
        }
    }

    /**
     * This method checks to see if any of the end conditions are reached
     * and if so, exits this screen
     * @Author Robert Hawkes <40232279>
     */
    public void checkEndCondition() {

        //Check if either player can play more cards or if we have exceeded the max test time
        if(mPlayer1Deck.getDeckCards().isEmpty() || mPlayer2Deck.getDeckCards().isEmpty() || checkTime()) {
            //Performance test finished
            //We want to calculate the average FPS
            //then give the user the value and return to Settings screen
            exitGamePerformance();
        }
    }

    /**
     * This method checks if the time this screen has ran for exceeds the maximum run time
     * @return Returns a boolean to signify passing the maximum time (True) or not (False)
     * @Author Robert Hawkes <40232279>
     */
    private boolean checkTime() {
        return ((int)(currentElapsedTime - startPerformanceTime)) >= MAX_RUN_TIME;
    }

    /**
     * This method exits the game performance screen.
     * It stops the music playing, displays the average FPS in a toast notification using a helper method
     * and then create a new options screen and destroys this one
     * @Author Robert Hawkes <40232279>
     */
    private void exitGamePerformance() {
        audioManager.stopMusic();

        //remove game screen and open options screen

        showToast("Average FPS: " + fpsHistory.getAverage());

        screenManager.addScreen(new OptionsScreen(mGame, context));
        screenManager.removeScreen(this);
    }

    /**
     * Draw the card demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        graphics2D.clear(Color.WHITE);

        //Draw the board background
        mBoardBackground.draw(elapsedTime,graphics2D,mDefaultLayerViewport, mDefaultScreenViewport);
        mBoard.draw(elapsedTime,graphics2D,mDefaultLayerViewport, mDefaultScreenViewport);
        mGameLogicPlayer1.update();
        mGameLogicPlayer1.clearCompleted();
        mGameLogicPlayer2.update();
        mGameLogicPlayer2.clearCompleted();
        gameTurnSystem.cleanBoard(mPlayer1Deck,1);
        gameTurnSystem.cleanBoard(mPlayer2Deck,2);

        determineAndMakeMove();

        backButton.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);

        //Draw portraits
        mPlayer1.getPlayerPortrait().draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
        mPlayer2.getPlayerPortrait().draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);

        drawCards(elapsedTime, graphics2D);

        globalMessage.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
        framesPerSecondCounter.draw(elapsedTime, graphics2D);
        fpsHistory.add(mGame.getAverageFramesPerSecond());
    }

    /**
     * This method draws the cards of both players 1 and player 2
     * @param elapsedTime
     * @param graphics2D
     * @Author Justin Johnston <40237507>
     */
    private void drawCards(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Draw the active card and card list
        for (Card card : mPlayer1Deck.getActiveCards()) {
            card.draw(elapsedTime, graphics2D,
                    mDefaultLayerViewport, mDefaultScreenViewport);
        }
        for (Card card : mPlayer1Deck.getDeckCards()) {
            card.draw(elapsedTime, graphics2D,
                    mDefaultLayerViewport, mDefaultScreenViewport);
        }

        for (Card card : mPlayer2Deck.getActiveCards()) {
            card.draw(elapsedTime, graphics2D,
                    mDefaultLayerViewport, mDefaultScreenViewport);
        }
        for (Card card : mPlayer2Deck.getDeckCards()) {
            card.draw(elapsedTime, graphics2D,
                    mDefaultLayerViewport, mDefaultScreenViewport);
        }
        for (Card card : mPlayer2Deck.getDiscardPile()) {
            card.draw(elapsedTime, graphics2D,
                    mDefaultLayerViewport, mDefaultScreenViewport);
        }
        for (Card card : mPlayer1Deck.getDiscardPile()) {
            card.draw(elapsedTime, graphics2D,
                    mDefaultLayerViewport, mDefaultScreenViewport);
        }
    }

    /**
     * This method determines if a move can be made from either player and then makes the move or switches the turn
     * @Author Justin Johnston <40237507>
     * @Author Robert Hawkes <40232279> (Edited and worked with Justin to ensure two AI's could manipulate cards)
     */
    private void determineAndMakeMove() {
        if(mGameLogicPlayer1.isEmpty() && mGameLogicPlayer2.isEmpty() && (playerturn == PlayerTurn.OPPONENT)){
            AIMoveInformation AImove = mAIPlayer2.takeTurn();
            if(AImove != null){
                new CardMove(AImove.getStartPosition(),AImove.getEndPosition(), mBoard, AImove.getCardIndex(), mGameLogicPlayer2, false, PlayerTurn.OPPONENT);

            }

        }
        if(playerturn == PlayerTurn.OPPONENT && mGameLogicPlayer2.isEmpty()&& mAIPlayer2.isTurnComplete()){
            switchTurn();
        }

        if(mGameLogicPlayer1.isEmpty() && mGameLogicPlayer2.isEmpty() && (playerturn == PlayerTurn.PLAYER)){
            AIMoveInformation AImove = mAIPlayer1.takeTurn();
            if(AImove != null){
                new CardMove(AImove.getStartPosition(),AImove.getEndPosition(), mBoard, AImove.getCardIndex(), mGameLogicPlayer1, false, PlayerTurn.PLAYER);

            }

        }
        if(playerturn == PlayerTurn.PLAYER && mGameLogicPlayer1.isEmpty()&& mAIPlayer1.isTurnComplete()){
            switchTurn();
        }
    }

    /**
     * This method shows an Android toast notification to the user by getting the Ui Thread and using
     * the Android SDK Toast class.
     * Surrounded by a try catch to ensure, if failing, doesn't impact crucial tasks or tests
     * Robert Hawkes <40232279>
     * @param message The message which you want to show to the user
     */
    private void showToast(final String message) {
        try {
            mGame.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    android.widget.Toast.makeText(mGame.getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            Log.e("GAGE", "Unable to show toast:" + message);
        }
    }
}
