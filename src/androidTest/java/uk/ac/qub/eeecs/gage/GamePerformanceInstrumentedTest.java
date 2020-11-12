package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.FloatCyclicQueue;
import uk.ac.qub.eeecs.game.FramesPerSecondCounter;
import uk.ac.qub.eeecs.game.GamePerformanceScreen;
import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;
import uk.ac.qub.eeecs.game.cardDemo.AI;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Deck;
import uk.ac.qub.eeecs.game.cardDemo.GameLogic;
import uk.ac.qub.eeecs.game.cardDemo.GameTurnSystem;
import uk.ac.qub.eeecs.game.cardDemo.Player;
import uk.ac.qub.eeecs.game.optionsScreen.OptionsScreen;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class GamePerformanceInstrumentedTest {

    /**
     * Game Performance instrumented testing approach
     *
     * @Author Robert Hawkes <40232279>
     */
    
    private Context context;
    private DemoGame game;
    private GamePerformanceScreen gamePerformanceScreen;
    AudioManager audioManager;
    AssetManager assetManager;
    ScreenManager screenManager;


    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        setupGame();
    }

    private void setupGame() {

        // set up a game instance to perform tests on
        game = new DemoGame();

        game.mFileIO = new FileIO(context);

        //Set up local Asset and Audio Manager
        assetManager = new AssetManager(game);
        audioManager = new AudioManager(game);
        screenManager = new ScreenManager(game);


        game.mAssetManager = assetManager;

        // Create the audio manager
        game.mAudioManager = audioManager;

        // set up a screen manager to perform addition/removal of screens
        game.mScreenManager = screenManager;

        //Initialise the Game Performance Screen
        gamePerformanceScreen = new GamePerformanceScreen(game, context);
    }

    @Test
    public void testGamePerformance_Add_Screen() {

        game.getScreenManager().addScreen(gamePerformanceScreen);

        assertEquals(GamePerformanceScreen.SCREEN_NAME, game.getScreenManager().getCurrentScreen().getName());
    }

    @Test
    public void testGamePerformance_ObjectInitialization() {
        GameObject boardBackground = gamePerformanceScreen.getmBoardBackground();
        Deck player1Deck = gamePerformanceScreen.getmPlayer1Deck();
        Deck player2Deck = gamePerformanceScreen.getmPlayer2Deck();
        Board board = gamePerformanceScreen.getmBoard();
        Player player1 = gamePerformanceScreen.getmPlayer1();
        Player player2 = gamePerformanceScreen.getmPlayer2();
        AI AIPlayer1 = gamePerformanceScreen.getmAIPlayer1();
        AI AIPlayer2 = gamePerformanceScreen.getmAIPlayer2();
        GameLogic gameLogicPlayer1 = gamePerformanceScreen.getmGameLogicPlayer1();
        GameLogic gameLogicPlayer2 = gamePerformanceScreen.getmGameLogicPlayer2();
        PushButton backButton = gamePerformanceScreen.getBackButton();
        GameTurnSystem gameTurnSystem = gamePerformanceScreen.getGameTurnSystem();
        SharedPreferences sharedPreferences = gamePerformanceScreen.getSharedPreferences();
        FramesPerSecondCounter framesPerSecondCounter = gamePerformanceScreen.getFramesPerSecondCounter();
        FloatCyclicQueue fpsHistory = gamePerformanceScreen.getFpsHistory();
        AudioManager audioManager = gamePerformanceScreen.getAudioManager();

        assertNotNull(boardBackground);
        assertNotNull(player1Deck);
        assertNotNull(player2Deck);
        assertNotNull(board);
        assertNotNull(player1);
        assertNotNull(audioManager);
        assertNotNull(player2);
        assertNotNull(AIPlayer1);
        assertNotNull(AIPlayer2);
        assertNotNull(gameLogicPlayer1);
        assertNotNull(gameLogicPlayer2);
        assertNotNull(backButton);
        assertNotNull(gameTurnSystem);
        assertNotNull(sharedPreferences);
        assertNotNull(framesPerSecondCounter);
        assertNotNull(fpsHistory);
        assertNotNull(audioManager);
    }

    @Test
    public void testGamePerformance_EndCondition_Player1DeckCards_Empty() {
        gamePerformanceScreen.getmPlayer1Deck().getDeckCards().clear();

        gamePerformanceScreen.checkEndCondition();

        assertFalse(gamePerformanceScreen.getAudioManager().isMusicPlaying());
        assertNotEquals(gamePerformanceScreen, screenManager.getCurrentScreen());
    }

    @Test
    public void testGamePerformance_EndCondition_Player2DeckCards_Empty() {
        gamePerformanceScreen.getmPlayer2Deck().getDeckCards().clear();

        gamePerformanceScreen.checkEndCondition();

        assertFalse(gamePerformanceScreen.getAudioManager().isMusicPlaying());
        assertNotEquals(gamePerformanceScreen, screenManager.getCurrentScreen());
    }

    @Test
    public void testGamePerformance_EndCondition_TimeExceeded() {
        gamePerformanceScreen.setCurrentElapsedTime(61);
        gamePerformanceScreen.setStartPerformanceTime(1);

        gamePerformanceScreen.checkEndCondition();

        assertNotEquals(gamePerformanceScreen, screenManager.getCurrentScreen());
    }
}
