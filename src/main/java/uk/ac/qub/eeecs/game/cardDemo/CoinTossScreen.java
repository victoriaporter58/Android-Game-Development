package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;
import uk.ac.qub.eeecs.game.TextHandler;
import uk.ac.qub.eeecs.game.TextType;

public class CoinTossScreen extends GameScreen {

    /**
     * A Coin Flip screen, similar to Hearthstone
     * determining what player plays the first turn
     *
     * @Author Robert Hawkes <40232279>
     * @Author Victoria Porter <40232938>
     */

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public static final String SCREEN_NAME = "CoinTossScreen";
    public static final int COIN_DIAMETER = 500;
    public static final int STEPS_PER_SPIN = 5;
    public static final float WIDTH_PER_STEP = (float)COIN_DIAMETER / STEPS_PER_SPIN;
    private static final int NO_OF_FADE_STEPS = 20000;
    private static float fadeValuePerStep;
    private static Random random = new Random(System.currentTimeMillis());

    private Bitmap coinHead;
    private Bitmap coinTail;

    private List<TouchEvent> touchEvents;
    private Input input;

    private int screenWidth, screenHeight;
    private int numberOfSpins;
    private int coinTouched;
    private int coinHeadTouched;
    private int coinTailTouched;

    private boolean coinFlipping;
    private boolean coinIncreasingWidth;
    private boolean masterVolumeOn;

    private GameObject coin;
    private GameObject coinHeadsUserSelection;
    private GameObject coinTailsUserSelection;
    private GameObject background;

    private PlayerTurn playerTurn;

    private SharedPreferences sharedPreferences;

    private float masterVolume;

    private AudioManager audioManager;

    private AssetManager assetManager;

    private CoinState userChoice;
    private CoinState coinChoice;

    private Champion selectedChampion;

    private TextHandler textHandler;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Initialise the Coin Toss Screen which allows the player to select the side of the coin
     * and then flip the coin
     * @param game The game in which the screen belongs to
     * @param selectedChampion  The selected champion from the Character Select screen
     */
    public CoinTossScreen(Game game, Champion selectedChampion) {
        super(SCREEN_NAME,game);

        assetManager = game.getAssetManager();
        audioManager = game.getAudioManager();

        // store width and height of game screen for use throughout this class
        setupViewports();
        setupAssets();
        setUpObjects();

        this.selectedChampion = selectedChampion;

        screenWidth = mGame.getScreenWidth();
        screenHeight = mGame.getScreenHeight();

        //Set default value for coin flipping to false (it hasn't been touched yet)
        coinFlipping = false;
        coinIncreasingWidth = false;

        //Randomize the number of spins from 1 to 10 (9 + 1)
        numberOfSpins = random.nextInt(10) + 1;

        textHandler = new TextHandler(mGame);
    }


    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters:
    // /////////////////////////////////////////////////////////////////////////

    public Bitmap getCoinHead() {
        return coinHead;
    }

    public Bitmap getCoinTail() {
        return coinTail;
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

    public int getNumberOfSpins() {
        return this.numberOfSpins;
    }

    public void setNumberOfSpins(int numberOfSpins) {
        this.numberOfSpins = numberOfSpins;
    }

    public void setCoinTouched(int coinTouched) {
        this.coinTouched = coinTouched;
    }

    public void setCoinHeadTouched(int coinHeadTouched) {
        this.coinHeadTouched = coinHeadTouched;
    }

    public void setCoinTailTouched(int coinTailTouched) {
        this.coinTailTouched = coinTailTouched;
    }

    public boolean isCoinFlipping() {
        return coinFlipping;
    }

    public void setCoinFlipping(boolean coinFlipping) {
        this.coinFlipping = coinFlipping;
    }

    public boolean isCoinIncreasingWidth() {
        return coinIncreasingWidth;
    }

    public void setCoinIncreasingWidth(boolean coinIncreasingWidth) {
        this.coinIncreasingWidth = coinIncreasingWidth;
    }

    public GameObject getCoin() {
        return coin;
    }

    public GameObject getCoinHeadsUserSelection() {
        return coinHeadsUserSelection;
    }

    public GameObject getCoinTailsUserSelection() {
        return coinTailsUserSelection;
    }

    public PlayerTurn getPlayerTurn() {
        return playerTurn;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public CoinState getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(CoinState userChoice) {
        this.userChoice = userChoice;
    }

    public CoinState getCoinChoice() {
        return coinChoice;
    }

    public void setCoinChoice(CoinState coinChoice) {
        this.coinChoice = coinChoice;
    }

    public Champion getSelectedChampion() {
        return selectedChampion;
    }

    public TextHandler getTextHandler() {
        return textHandler;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    private void setupViewports(){
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set( 0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserve the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight/2.0f, 240.0f, layerHeight/2.0f);
    }

    private void setupAssets() {
        assetManager.loadAssets("txt/assets/CoinTossScreenAssets.JSON");
        coinHead = assetManager.getBitmap("CoinHead");
        coinTail = assetManager.getBitmap("CoinTail");
    }

    private void setUpObjects(){
        coin = new GameObject(mDefaultScreenViewport.width/2, mDefaultScreenViewport.height/2, COIN_DIAMETER, COIN_DIAMETER, coinHead, this);
        coinHeadsUserSelection = new GameObject(mDefaultScreenViewport.width/1.5f, mDefaultScreenViewport.height/2, COIN_DIAMETER, COIN_DIAMETER, coinHead, this);
        coinTailsUserSelection = new GameObject(mDefaultScreenViewport.width/3.0f, mDefaultScreenViewport.height/2, COIN_DIAMETER, COIN_DIAMETER, coinTail, this);

        background = new GameObject(mDefaultScreenViewport.centerX(),mDefaultScreenViewport.centerY(),mDefaultScreenViewport.width,mDefaultScreenViewport.height,getGame()
                .getAssetManager().getBitmap("SoilBackground"),this);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        //Get the input
        input = mGame.getInput();

        //Get the touch events on the screen
        touchEvents = input.getTouchEvents();

        CheckAndDoCoinSelection();
        CheckAndDoCoinFlip();

    }

    /**
     * This method checks if the user hasn't made their decision and if so, checks if they are making a touch that may determine it and process
     * it as a choice
     * @Author Robert Hawkes <40232279>
     */
    private void CheckAndDoCoinSelection() {
        if(userChoice == null) {
            //User has not decided, we want to check if they have clicked and then get what coin they have clicked on (if any)
            CheckSelectionTouched();
            DetermineUserChoice();
        }
    }

    /**
     * This method updates if the Head or Tail selection coins have been touched, therefore the user
     * making a choice
     * @Author Robert Hawkes <40232279>
     */
    private void CheckSelectionTouched() {
        //User has not decided, we want to check if they have clicked and then get what coin they have clicked on (if any)
        coinHeadTouched = checkIfGameObjectClicked(coinHeadsUserSelection, touchEvents);
        coinTailTouched = checkIfGameObjectClicked(coinTailsUserSelection, touchEvents);
    }

    /**
     * This method determines the user choice by testing if either side of the selection coins have been touched
     * If a coin is touched, the user choice is set
     * If a touch is given but not on either coin, it shows a toast notification prompt to the user to select a coin
     * @Author Robert Hawkes <40232279>
     */
    public void DetermineUserChoice() {

        if(coinHeadTouched == 1) {
            //Heads selection has been clicked
            userChoice = CoinState.HEAD;
        }
        else if(coinTailTouched == 1) {
            //Tails selection has been clicked
            userChoice = CoinState.TAIL;
        }
        else if(coinHeadTouched == 2 || coinTailTouched == 2){
            showToast("Please select a coin!");
        }
    }

    /**
     * This method checks if the user has made their choice and if they have,
     * determines if the coin is flipping or not. If flipping, it calls to process and continue flipping.
     * If not flipping, it checks if the user has touched and starting the coin flip
     * @Author Robert Hawkes <40232279>
     */
    private void CheckAndDoCoinFlip() {
        if(userChoice != null) {

            //Check if coin has been touched and is in the process of flipping
            if(coinFlipping) {
                ProcessCoinFlipping();
            }
            else {
                //Check if user has pressed coin, and coin is not flipping already. If not flipping, start it flipping
                CheckCoinTouched();
                StartCoinFlip();
            }
        }
    }

    /**
     * This method processes the coin flipping
     * It checks the coin is supposed to be flipping, then determines whether to increase or decrease width
     * Once the width is changed, it checks if the bitmap should be flipped to show the other side of the coin
     * @Author Robert Hawkes <40232279>
     */
    public void ProcessCoinFlipping() {
        if(numberOfSpins > 0) {

            //We need to either decrease the width of the coin image to 0 or increase it to max
            if(!coinIncreasingWidth) {
                coin.setWidth(coin.getWidth() - WIDTH_PER_STEP);
            }
            else {
                coin.setWidth(coin.getWidth() + WIDTH_PER_STEP);
            }

            if(coin.getWidth() <= 0) {
                coinIncreasingWidth = true;

                if(coin.getBitmap() == coinHead) {
                    coin.setBitmap(coinTail);
                }
                else if(coin.getBitmap() == coinTail) {
                    coin.setBitmap(coinHead);
                }
            }
            else if(coin.getWidth() == COIN_DIAMETER) {
                coinIncreasingWidth = false;
                numberOfSpins--;
            }

        }
        else {
            //Determine what the coin choice is
            DetermineCoinChoice();

            //Determine who wins the coin toss
            DetermineWin();

            //Call the start Game method
            StartGame();
        }
    }

    /**
     * This method updates the coinTouched var based on if the screen received a touch input on
     * the coin
     * @Author Robert Hawkes <40232279>
     */
    public void CheckCoinTouched() {
        coinTouched = checkIfGameObjectClicked(coin, touchEvents);
    }

    /**
     * This method checks to see if the coin has been touched and if the coin isn't already flipping
     * If satisfying both conditions, the coin the begins flipping
     * @Author Robert Hawkes <40232279>
     */
    public void StartCoinFlip() {
        if(coinTouched == 1) {

            //If coin is not flipping
            if(!coinFlipping) {

                //Set the coin as flipping (Dealt with on the next update)
                coinFlipping = true;
            }
        }
        else if(coinTouched == 2){
            showToast("Please click the coin to flip!");
        }
    }

    /**
     * This method takes in the gameObject and touchEvents and returns one of three states
     * @param gameObject
     * @param touchEvents
     * @return 0 is no touch events, 1 is touched on the GameObject, 2 is touch events present but none on the
     * @Author Robert Hawkes <40232279>
     */
    private int checkIfGameObjectClicked(GameObject gameObject, List<TouchEvent> touchEvents) {

        int valReturn = 0;

        //Check if there is touch events
        if(touchEvents.size() > 0) {

             valReturn = 2;

            //Get the bound of the GameObject
            BoundingBox gameObjectBound = gameObject.getBound();

            //Get the width and height of the GameObject
            float gameObjectWidth = gameObject.getWidth();
            float gameObjectHeight = gameObject.getHeight();

            //Get each touch event in the list
            for(TouchEvent eachTouchEvent : touchEvents) {

                //if touch event is on the game object
                if(eachTouchEvent.x < gameObjectBound.x + gameObjectWidth/2) {
                    if(eachTouchEvent.x > gameObjectBound.x - gameObjectWidth/2) {
                        if (eachTouchEvent.y < gameObjectBound.y + gameObjectHeight) {
                            if (eachTouchEvent.y > gameObjectBound.y - gameObjectHeight) {

                                //The press is on the Game Object
                                //We return 1 - No need to continue processing
                                return 1;
                            }
                        }
                    }
                }
            }
        }

        return valReturn;
    }

    /**
     * This method determines if the users choice matches the random coin choice
     * and determines if the player should take the first turn or not
     * @Author Robert Hawkes <40232279>
     */
    public void DetermineWin() {

        if(userChoice == coinChoice) {
            //User wins the coin toss!
            showToast("You won the toss! You start!");
            playerTurn = PlayerTurn.PLAYER;

        }
        else {
            //User lost the coin toss!
            showToast("You lost the toss! Opponent starts!");
            playerTurn = PlayerTurn.OPPONENT;
        }
    }

    /**
     * This method determines what the coin lands on after flipping by
     * using the current bitmap when this method is called
     * @Author Robert Hawkes <40232279>
     */
    public void DetermineCoinChoice() {
        if(coin.getBitmap() == coinHead) {
            coinChoice = CoinState.HEAD;
        }
        else {
            coinChoice = CoinState.TAIL;
        }
    }

    /**
     * This method is unused due to a bug within Audio Manager, however without the bug
     * this method would initially start the fading of the background music
     * @Author Victoria Porter <40232938>
     * @Author Robert Hawkes <40232279>
     */
    public void startFadeBackgroundMusic() {
        sharedPreferences = new SharedPreferences(this.getGame().getActivity().getApplicationContext());
        masterVolumeOn = sharedPreferences.getBooleanValue("masterVolumeOn", true);

        // we only need to fade the music if the music is playing
        if(masterVolumeOn){
            masterVolume = sharedPreferences.getFloatValue("musicVolume", 5.0f);
            audioManager = getGame().getAudioManager();

            fadeValuePerStep = audioManager.getMusicVolume() / NO_OF_FADE_STEPS;
        }
    }

    /**
     * This method is unused due to a bug within Audio Manager, however without the bug
     * this method would fade the background music by a set value each time it's called unless Master
     * Volume is off or the Music volume is less than or equal to 0.
     * @Author Victoria Porter <40232938>
     * @Author Robert Hawkes <40232279>
     */
    public void fadeBackgroundMusic(){
        if(masterVolumeOn && audioManager.getMusicVolume() > 0.00) {
            audioManager.setMusicVolume(audioManager.getMusicVolume() - fadeValuePerStep);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        background.draw(elapsedTime, graphics2D);

        if(userChoice != null) {
            textHandler.drawText(TextType.TITLE,"Coin Toss", screenWidth/2.9f, screenHeight/7.0f, Color.BLACK, graphics2D);

            coin.draw(elapsedTime, graphics2D);
        }
        else {
            textHandler.drawText(TextType.TITLE,"Pick a side", screenWidth/2.9f, screenHeight/7.0f, Color.BLACK, graphics2D);

            coinTailsUserSelection.draw(elapsedTime, graphics2D);
            coinHeadsUserSelection.draw(elapsedTime, graphics2D);
        }
    }

    /**
     * This method starts the game by creating a new object of SaveTheWorld Screen, passing through the game
     * context, the select champion from Character Select screen and which player plays the first turn. The
     * method would also begin the music fading however due to the audio bug, this is removed for simplicity
     * @Author Robert Hawkes <40232279>
     * @Author Victoria Porter <40232938>
     */
    public void StartGame() {
        //introduce fade to music
        //fadeBackgroundMusic();

//        startFadeBackgroundMusic();
//        if(masterVolumeOn) {
//            for (int i = 0; i <= NO_OF_FADE_STEPS ; i++) {
//                fadeBackgroundMusic();
//            }
//        }
//        audioManager = getGame().getAudioManager();
//        audioManager.stopMusic();

        mGame.getScreenManager().addScreen(new SaveTheWorldScreen(mGame,selectedChampion,playerTurn));
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