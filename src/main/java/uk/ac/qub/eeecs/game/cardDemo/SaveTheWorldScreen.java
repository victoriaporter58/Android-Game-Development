package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;
import android.graphics.Paint;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.FramesPerSecondCounter;
import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;
import uk.ac.qub.eeecs.game.TextHandler;
import uk.ac.qub.eeecs.game.TextType;
import uk.ac.qub.eeecs.game.optionsScreen.OptionsScreen;

/**
 * Starter class for Card game stories
 * @version 1.0
 */
public class SaveTheWorldScreen extends GameScreen {
    /**
     * The main game screen
     *
     * @Author All group members
     */

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////
    private final int DEFAULT_HEALTH = 30;
    private final int DEFAULT_MANA = 8;
    private final int MANA_PER_TURN = 2;

    private Card activeCard = null;
    private int lastActiveCard = -1;

    //Define graphics elements
    private GameObject mBoardBackground;
    private PushButton endTurnButton;
    private Deck mPlayer1Deck;
    private Deck mPlayer2Deck;
    private Vector2 mMoveStartPosition = new Vector2(0,0);
    private Vector2 mMoveEndPosition = new Vector2(0,0);

    //HUD elements
    private GlobalMessage globalMessage;
    private PushButton optionsScreenButton;

    //Define relevant models
    private Board mBoard;
    private Player mPlayer1;
    private Player mPlayer2;
    private AI mAIOpponent;
    private GameLogic mGameLogicPlayer1;
    private GameLogic mGameLogicPlayer2;
    private boolean playerWon;

    // selectedChampions deckJsonLocation
    private String jsonLocation;
    private Champion selectedChampion;

    private Boolean masterVolumeOn;
    private float masterVolume;
    private AudioManager audioManager;
    private FramesPerSecondCounter framesPerSecondCounter;
    private SharedPreferences sharedPreferences;
    private PlayerTurn playerturn;
    private GameTurnSystem gameTurnSystem;
    private Card cardToEnlarge;
    private boolean longTouchHappened;
    private GameObject enhancedBitmapOfCard;
    private GameObject enlargedCardBackdrop;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */
    SaveTheWorldScreen(Game game, Champion selectedChampion, PlayerTurn playerTurn) {
        super("CardScreen", game);

        this.jsonLocation = selectedChampion.getDeckJsonLocation();
        this.selectedChampion = selectedChampion;
        this.playerturn = playerTurn;
        longTouchHappened = false;

        sharedPreferences = new SharedPreferences(getGame().getActivity().getApplicationContext());
        sharedPreferences.setBooleanValue(sharedPreferences.getMainGameActiveLabel(), true);

        framesPerSecondCounter = new FramesPerSecondCounter(game, getGame().getActivity().getApplicationContext());

        globalMessage = new GlobalMessage();

        // Load the various images used by the game
        mGame.getAssetManager().loadAssets("txt/assets/SaveTheWorldScreenAssets.JSON");

        setUpAudio();
        setupViewports();
        setupBackground();
        setupPlayer();
        setupCardGameObjects(jsonLocation);
        setupObjects();
        setupGameStart();

        if(playerturn == PlayerTurn.PLAYER){
            playerturn = PlayerTurn.PLAYER;
            globalMessage.addMessage("Player 1 Turn");
        }else{
            playerturn = PlayerTurn.OPPONENT;
            globalMessage.addMessage("Player 2 Turn");
            mAIOpponent.startTurn();
        }

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    private void setUpAudio(){
        audioManager = getGame().getAudioManager();

        masterVolumeOn = sharedPreferences.getBooleanValue(sharedPreferences.getMasterVolumeOnLabel(), false);
        masterVolume = sharedPreferences.getFloatValue(sharedPreferences.getMusicVolumeLabel(), 5.0f);

        if(masterVolumeOn && !audioManager.isMusicPlaying()){
            audioManager.playMusic(mGame.getAssetManager().getMusic("MainGameBackgroundMusic"));
            audioManager.setMusicVolume(masterVolume);
            audioManager.setSfxVolume(masterVolume);
        }else if(!masterVolumeOn){
            audioManager.setSfxVolume(0.0f);
        }
    }


    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set( 0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserve the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight/2.0f, 240.0f, layerHeight/2.0f);
    }


    private void setupPlayer() {
        float width = (int)mBoard.getBoardComponents().get(mBoard.PLAYER1AVATARSPACE).getWidth();
        float height = (int)mBoard.getBoardComponents().get(mBoard.PLAYER1AVATARSPACE).getHeight();

        int xPosition = (int)mBoard.getBoardComponents().get(mBoard.PLAYER1AVATARSPACE).position.x;
        int yPosition1 =(int)mBoard.getBoardComponents().get(mBoard.PLAYER1AVATARSPACE).position.y;
        int yPosition2 = (int)mBoard.getBoardComponents().get(mBoard.PLAYER2AVATARSPACE).position.y;

        if(selectedChampion != null){
            mPlayer1 = new Player(xPosition,yPosition1,selectedChampion.getHealth(),selectedChampion.getMana(),"playerPortrait",this, true);
            mPlayer2 = new Player(xPosition,yPosition2,selectedChampion.getHealth(),selectedChampion.getMana(),"enemyPortrait",this, false);
        } else {
            mPlayer1 = new Player(xPosition,yPosition1,DEFAULT_HEALTH,DEFAULT_MANA,"playerPortrait",this, true);
            mPlayer2 = new Player(xPosition,yPosition2,DEFAULT_HEALTH,DEFAULT_MANA,"enemyPortrait",this, false);
        }
        mPlayer1.getPlayerPortrait().setWidth(width);
        mPlayer1.getPlayerPortrait().setHeight(height);
        mPlayer2.getPlayerPortrait().setWidth(width);
        mPlayer2.getPlayerPortrait().setHeight(height);
    }

    private void setupCardGameObjects(String jsonLocation){
        //Initialise decks and GameLogic for player1 and player2
        mPlayer1Deck = new Deck(jsonLocation,this,mGame);
        mPlayer2Deck = new Deck(jsonLocation,this,mGame);//same Deck for testing purpose
        mGameLogicPlayer1 = new GameLogic(mBoard, mPlayer1Deck,mPlayer2Deck,mPlayer1,mPlayer2,globalMessage,audioManager,this);
        mGameLogicPlayer2 = new GameLogic(mBoard,mPlayer2Deck, mPlayer1Deck,mPlayer2,mPlayer1,globalMessage,audioManager,this);
        mAIOpponent = new AI(mBoard,mPlayer2Deck, mPlayer1Deck,mPlayer2,mPlayer1,2);
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
     * This method retrieves the information of a card that has been long pressed and enlarges its features
     * which are then drawn to the screen later in the draw method.
     *
     * @Author Victoria Porter <40232938>
     * @version 2.0
     */
    private void displayEnlargedVersionOfCard(Deck deck, int selectedCard){

        enlargedCardBackdrop = new GameObject(mDefaultScreenViewport.centerX(),mDefaultScreenViewport.centerY(),mDefaultScreenViewport.width,mDefaultScreenViewport.height,getGame()
                .getAssetManager().getBitmap("EnlargedCardBackdrop"),this);

        int cardHealth = deck.getActiveCards().get(selectedCard).getHealth();
        int cardAttack = deck.getActiveCards().get(selectedCard).getAttack();
        int cardCost = deck.getActiveCards().get(selectedCard).getCost();

        String thisCardName = deck.getActiveCards().get(selectedCard).getCardName();
        String thisCardText = deck.getActiveCards().get(selectedCard).getCardText();
        String thisCardPortrait = deck.getActiveCards().get(selectedCard).getCardPortraitString();
        AbilityType thisAbilityType = deck.getActiveCards().get(selectedCard).getAbility();

        cardToEnlarge = new Card(mDefaultLayerViewport.getWidth()*0.75f,mDefaultLayerViewport.halfHeight,this,cardHealth,cardAttack,cardCost,thisCardName,thisCardText,thisCardPortrait, thisAbilityType);

        cardToEnlarge.setHeight(Card.CARD_HEIGHT*3.5f);
        cardToEnlarge.setWidth(Card.CARD_WIDTH*3.5f);
        cardToEnlarge.setMovable(true);
    }

    /**
     * This method resets the longTouchHappened boolean so that future longTouch touch events can
     * be identified.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void resetLongTouchBoolean(){
        if(longTouchHappened){
            longTouchHappened = false;
        }
    }


    /*
    Setup the buttons and the view card functionality related bitmaps
     */
    private void setupObjects(){
        float optionsPositionx = mBoard.getBoardComponents().get(mBoard.PLAYER2BOARDDECKSPACE).position.x +mBoard.COMPONENTSIZE;
        float optionsPositiony = mBoard.getBoardComponents().get(mBoard.PLAYER2BOARDDECKSPACE).position.y;
        float endTurnPositionx = mBoard.getBoardComponents().get(mBoard.PLAYER1BOARDDECKSPACE).position.x +mBoard.COMPONENTSIZE;
        float endTurnPositiony = mBoard.getBoardComponents().get(mBoard.PLAYER1BOARDDECKSPACE).position.y;

        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        optionsScreenButton = new PushButton(
                optionsPositionx, optionsPositiony, spacingX * 0.6f, spacingY * 0.6f,
                "SettingsIcon", "SettingsIconPushed", this);

        if(playerturn == PlayerTurn.PLAYER){
            endTurnButton = new PushButton(endTurnPositionx, endTurnPositiony, spacingX * 0.57f, spacingY * 0.44f,
                    "EndTurnButton", "EndTurnButtonSelected", this);
        } else {
            endTurnButton = new PushButton(endTurnPositionx, endTurnPositiony, spacingX * 0.57f, spacingY * 0.44f,
                    "EnemyTurnButton", "EndTurnButtonSelected", this);
        }
        if(masterVolumeOn) {
            endTurnButton.setPlaySounds(true, true);
        }else{

        }

        endTurnButton.processInLayerSpace(true);

        enhancedBitmapOfCard = new GameObject(mGame.getScreenWidth()/2, mGame.getScreenHeight()/2, spacingX*0.5f, spacingY*0.5f, getGame().getAssetManager().getBitmap("CardBackground"), this);
    }

    private void setupGameStart(){
        // Define a card to be displayed
        // Define a list of cards to be displayed
        for(int i = 0; i < mBoard.INITIALHANDSIZE; i++){
            mPlayer1Deck.drawCard(mBoard.getBoardComponents().get(mBoard.PLAYER1BOARDDECKSPACE).position.x,mBoard.getBoardComponents().get(mBoard.PLAYER1BOARDDECKSPACE).position.y);
            new CardMove(mPlayer1Deck.getActiveCards().get(i).position,mBoard.getBoardComponents().get(i).position,mBoard,i, mGameLogicPlayer1,false,PlayerTurn.PLAYER);
        }

        for(int i = 0 ; i < mBoard.INITIALHANDSIZE;i++) {
            mPlayer2Deck.drawCard(mBoard.getBoardComponents().get(mBoard.PLAYER2BOARDDECKSPACE).position.x, mBoard.getBoardComponents().get(mBoard.PLAYER2BOARDDECKSPACE).position.y);
            new CardMove(mPlayer2Deck.getActiveCards().get(i).position, mBoard.getBoardComponents().get(i + mBoard.PLAYER2HANDSPACE).position, mBoard, i, mGameLogicPlayer2, false, PlayerTurn.OPPONENT);
        }
    }

    private void touchEvents(){
        Input input = mGame.getInput();
        if(input.getTouchEvents().size() > 0) {

            if(input.getTouchEvents().get(0).type == TouchEvent.TOUCH_DOWN && cardToEnlarge != null){
                cardToEnlarge = null;
                enlargedCardBackdrop = null;
            }

            for(int card = 0; card < mPlayer2Deck.getActiveCards().size(); card++){
                for(int i = 0; i < input.getTouchEvents().size(); i++){

                    //Convert the touch location from screen position to game position
                    Vector2 touchLocation = new Vector2(0, 0);
                    int touchType = input.getTouchEvents().get(i).type;
                    ViewportHelper.convertScreenPosIntoLayer(mDefaultScreenViewport, input.getTouchEvents().get(i).x,
                            input.getTouchEvents().get(i).y, mDefaultLayerViewport, touchLocation);
                    // get the current event
                    TouchEvent touchEvent = input.getTouchEvents().get(i);

                    // LongTouch has happened, therefore prepare the card for enlargement
                    if(mPlayer2Deck.getActiveCards().get(card).getBound().contains(touchLocation.x,touchLocation.y) && touchType == touchEvent.TOUCH_LONG_PRESS && mPlayer2Deck.getActiveCards().get(card).getFaceUp() == true){
                        longTouchHappened = true;
                        displayEnlargedVersionOfCard(mPlayer2Deck, card);
                    }
                }
                resetLongTouchBoolean();
            }

            //check each card
            for (int card = 0; card < mPlayer1Deck.getActiveCards().size(); card++) {
                for (int i = 0; i < input.getTouchEvents().size(); i++) {

                    //Convert the touch location from screen position to game position
                    Vector2 touchLocation = new Vector2(0, 0);
                    int touchType = input.getTouchEvents().get(i).type;
                    ViewportHelper.convertScreenPosIntoLayer(mDefaultScreenViewport, input.getTouchEvents().get(i).x,
                            input.getTouchEvents().get(i).y, mDefaultLayerViewport, touchLocation);
                    // get the current event
                    TouchEvent touchEvent = input.getTouchEvents().get(i);

                    //check if touch location falls within hand boundary box and select active card
                    if (mPlayer1Deck.getActiveCards().get(card).getBound().contains(touchLocation.x,touchLocation.y)
                            && touchType == TouchEvent.TOUCH_DOWN
                             && mPlayer1Deck.getActiveCards().get(card).isMovable()
                            && playerturn == PlayerTurn.PLAYER)
                    {
                        activeCard = mPlayer1Deck.getActiveCards().get(card);
                        lastActiveCard = card;
                        mPlayer1Deck.getActiveCards().get(card).setHeight(Card.CARD_HEIGHT*1.5f);
                        mPlayer1Deck.getActiveCards().get(card).setWidth(Card.CARD_WIDTH*1.5f);
                        mMoveStartPosition.x= activeCard.position.x;
                        mMoveStartPosition.y = activeCard.position.y;

                    }

                    if(touchType == touchEvent.TOUCH_UP){
                        if (lastActiveCard != -1){
                            mPlayer1Deck.getActiveCards().get(lastActiveCard).setHeight(Card.CARD_HEIGHT);
                            mPlayer1Deck.getActiveCards().get(lastActiveCard).setWidth(Card.CARD_WIDTH);
                            mMoveEndPosition.x= activeCard.position.x;
                            mMoveEndPosition.y = activeCard.position.y;
                            new CardMove(mMoveStartPosition,mMoveEndPosition,mBoard,lastActiveCard, mGameLogicPlayer1,false,PlayerTurn.PLAYER);
                            lastActiveCard = -1;
                        }
                        activeCard = null;
                    }

                    // LongTouch has happened, therefore prepare the card for enlargement
                    if(mPlayer1Deck.getActiveCards().get(card).getBound().contains(touchLocation.x,touchLocation.y) && touchType == touchEvent.TOUCH_LONG_PRESS){
                        longTouchHappened = true;
                        displayEnlargedVersionOfCard(mPlayer1Deck, card);
                    }

                    //check if the move is a drag and move card position to match
                    if ( touchType == touchEvent.TOUCH_DRAGGED && activeCard != null) {
                        activeCard.setPosition(touchLocation.x, touchLocation.y );
                        checkBounds();
                    }
                }

                resetLongTouchBoolean();
            }
        }
    }

    private void checkBounds(){
        if (activeCard.position.x - activeCard.CARD_WIDTH / 2 < 0)
            activeCard.setPosition(0 + activeCard.CARD_WIDTH / 2, activeCard.position.y);
        if (activeCard.position.x + activeCard.CARD_WIDTH / 2 > mDefaultLayerViewport.getWidth())
            activeCard.setPosition(mDefaultLayerViewport.getWidth() - activeCard.CARD_WIDTH / 2, activeCard.position.y);
        if (activeCard.position.y - activeCard.CARD_HEIGHT / 2 < 1)
            activeCard.setPosition(activeCard.position.x, 1 + activeCard.CARD_HEIGHT / 2);
        if (activeCard.position.y + activeCard.CARD_HEIGHT / 2 > mDefaultLayerViewport.getTop())
            activeCard.setPosition(activeCard.position.x, mDefaultLayerViewport.getTop() - activeCard.CARD_HEIGHT / 2);
    }

    private void switchTurn() {
        if(mGameLogicPlayer1.isEmpty() && mGameLogicPlayer2.isEmpty()){
            boolean cardReturned;
            if(playerturn == PlayerTurn.PLAYER){
                playerturn = PlayerTurn.OPPONENT;
                mPlayer2.incrementMana(MANA_PER_TURN);
                cardReturned = gameTurnSystem.switchTurn(mPlayer2Deck,PlayerTurn.OPPONENT,mGameLogicPlayer2);
                globalMessage.addMessage("Player 2 Turn");
                mAIOpponent.startTurn();
                if(!cardReturned){
                    globalMessage.setSubMessage("can not draw card");
                }
                endTurnButton.setBitmap(getGame().getAssetManager().getBitmap("EnemyTurnButton"));
            }else if (playerturn == PlayerTurn.OPPONENT){
                playerturn = PlayerTurn.PLAYER;
                mPlayer1.incrementMana(MANA_PER_TURN);
                cardReturned = gameTurnSystem.switchTurn(mPlayer1Deck,PlayerTurn.PLAYER, mGameLogicPlayer1);
                globalMessage.addMessage("Player 1 Turn");
                if(!cardReturned){
                    globalMessage.setSubMessage("can not draw card");
                }
                mAIOpponent.setTurnComplete(false);
                endTurnButton.setBitmap(getGame().getAssetManager().getBitmap("EndTurnButton"));
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
        // Process any touch events occurring since the last update
        this.touchEvents();

        masterVolumeOn = sharedPreferences.getBooleanValue(sharedPreferences.getMasterVolumeOnLabel(), false);
        masterVolume = sharedPreferences.getFloatValue(sharedPreferences.getMusicVolumeLabel(), 5.0f);

        if(masterVolumeOn){
            audioManager.setSfxVolume(masterVolume);
        }else{
            audioManager.setSfxVolume(0.0f);
        }

        if(playerturn == PlayerTurn.PLAYER) {
            endTurnButton.update(elapsedTime);
        }

        if(endTurnButton.isPushTriggered() && playerturn == PlayerTurn.PLAYER){
            switchTurn();
        }

        if(optionsScreenButton.isPushTriggered()){
            //open options screen
            mGame.getScreenManager().addScreen(new OptionsScreen(mGame, getGame().getActivity().getApplicationContext()));
        }

        enhancedBitmapOfCard.update(elapsedTime);
        optionsScreenButton.update(elapsedTime);
        framesPerSecondCounter.update(elapsedTime);

        // Update the players cards
        mPlayer1Deck.update(elapsedTime);
        mPlayer2Deck.update(elapsedTime);

        //Update portraits - Implementation 02
        mPlayer1.updatePortrait();
        mPlayer2.updatePortrait();

        mGameLogicPlayer1.update();
        mGameLogicPlayer1.clearCompleted();
        mGameLogicPlayer2.update();
        mGameLogicPlayer2.clearCompleted();
        gameTurnSystem.cleanBoard(mPlayer1Deck,1);
        gameTurnSystem.cleanBoard(mPlayer2Deck,2);
        aiTaketurn();
        endGame();
        globalMessage.update(elapsedTime);
    }

    private void endGame() {
        //Check if either player is dead
        if(mPlayer1.isHealthEmpty()) {
            playerWon = false;
            sharedPreferences.setBooleanValue(sharedPreferences.getMainGameActiveLabel(),false);
            mGame.getScreenManager().addScreen(new EndGameScreen("EndGameScreen",mGame, playerWon));
        } else if(mPlayer2.isHealthEmpty()) {
            playerWon = true;
            sharedPreferences.setBooleanValue(sharedPreferences.getMainGameActiveLabel(),false);
            mGame.getScreenManager().addScreen(new EndGameScreen("EndGameScreen",mGame, playerWon));
        }
    }

    private void aiTaketurn() {
        if(mGameLogicPlayer1.isEmpty() && mGameLogicPlayer2.isEmpty() && (playerturn == PlayerTurn.OPPONENT)){
            AIMoveInformation AImove = mAIOpponent.takeTurn();
            if(AImove != null){
                new CardMove(AImove.getStartPosition(),AImove.getEndPosition(), mBoard, AImove.getCardIndex(), mGameLogicPlayer2, false, PlayerTurn.OPPONENT );

            }

        }
        if(playerturn == PlayerTurn.OPPONENT && mGameLogicPlayer2.isEmpty()&& mAIOpponent.isTurnComplete()){
            if(mAIOpponent.isFirstTurn())mAIOpponent.setFirstTurn(false);
            switchTurn();
        }
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
        optionsScreenButton.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);

        // Draw HUD
        drawHud(elapsedTime,graphics2D);

        //Draw portraits
        mPlayer1.getPlayerPortrait().draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
        mPlayer2.getPlayerPortrait().draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);

        // Draw the active card and card list
        if(playerturn == PlayerTurn.PLAYER){
            mPlayer2Deck.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
            mPlayer1Deck.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
        }else{
            mPlayer1Deck.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
            mPlayer2Deck.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
        }

        globalMessage.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
        framesPerSecondCounter.draw(elapsedTime, graphics2D);

        if(cardToEnlarge !=null) {
            cardToEnlarge.setFaceUp(true);
            enlargedCardBackdrop.draw(elapsedTime, graphics2D);
            cardToEnlarge.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

            drawTextForEnlargedCard(graphics2D);
        }
    }

    /**
     * Draw the descriptive text for the card to be enlarged to the screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void drawTextForEnlargedCard(IGraphics2D graphics2D){

        Paint p = new Paint();
        p.setTextSize(mGame.getScreenHeight()/12.0f);
        p.setColor(Color.WHITE);

        Paint t = new Paint();
        t.setTextSize(mGame.getScreenHeight()/17.0f);
        t.setColor(Color.WHITE);

        float spacing = mGame.getScreenHeight()/6.2f;

        TextHandler textHandler = new TextHandler(mGame);

        textHandler.drawText(TextType.SUBTITLE, "Click anywhere to close", mGame.getScreenWidth()/3.5f, mGame.getScreenHeight()/1.1f, Color.WHITE, graphics2D);
        textHandler.drawText(TextType.TITLE, cardToEnlarge.getCardName(), mDefaultScreenViewport.width*0.01f, mGame.getScreenHeight()/11.0f, Color.WHITE, graphics2D);
        textHandler.drawText(TextType.BODY, "Ability: "+cardToEnlarge.getAbility().name()+" - "+cardToEnlarge.getAbility().toString(), mDefaultScreenViewport.width*0.01f, spacing, Color.WHITE, graphics2D);
        textHandler.drawText(TextType.BODY, "Cost:    "+cardToEnlarge.getCost(), mDefaultScreenViewport.width*0.01f, spacing*2.0f, Color.WHITE, graphics2D);
        textHandler.drawText(TextType.BODY, "Attack:  "+cardToEnlarge.getAttack(), mDefaultScreenViewport.width*0.01f, spacing*3.0f, Color.WHITE, graphics2D);
        textHandler.drawText(TextType.BODY, "Health:  "+cardToEnlarge.getHealth(), mDefaultScreenViewport.width*0.01f, spacing*4.0f, Color.WHITE, graphics2D);
    }

    private void drawHud(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //Draw the end turn / next buttons
        endTurnButton.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
        optionsScreenButton.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);
    }

    public int getPlayerWithHighestHealth() {
        //This method checks to see if player 1 or player 2 has the highest health
        int Player1Health = mPlayer1.getHealth();
        int Player2Health = mPlayer2.getHealth();
        if(Player1Health > Player2Health) {
            return 1;
        } else {
            return 2;
        }
    }
}

