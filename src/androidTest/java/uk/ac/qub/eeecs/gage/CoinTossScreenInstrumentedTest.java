package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.TextHandler;
import uk.ac.qub.eeecs.game.cardDemo.Champion;
import uk.ac.qub.eeecs.game.cardDemo.CoinState;
import uk.ac.qub.eeecs.game.cardDemo.CoinTossScreen;
import uk.ac.qub.eeecs.game.cardDemo.PlayerTurn;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CoinTossScreenInstrumentedTest {

    /**
     * Coin Toss Instrumented testing approach
     *
     * @Author Robert Hawkes <40232279>
     */
    private Context context;
    private DemoGame game;
    private CoinTossScreen coinTossScreen;
    private AudioManager audioManager;
    private AssetManager assetManager;
    private ScreenManager screenManager;
    private Champion champion;

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

        // set up asset manager to allow game objects to load assets
        game.mAssetManager = assetManager;

        // Create the audio manager
        game.mAudioManager = audioManager;

        // set up a screen manager to perform addition/removal of screens
        game.mScreenManager = screenManager;

        //Create the champion that we pass from the previous screen
        champion = new Champion("Oak King","txt/decks/OakKingDeck.JSON", 30, 8, null);

        //Initialise the Coin Toss Screen
        coinTossScreen = new CoinTossScreen(game, champion);
    }

    @Test
    public void testCoinTossScreen_Add_Screen() {

        game.getScreenManager().addScreen(coinTossScreen);

        assertEquals(CoinTossScreen.SCREEN_NAME, game.getScreenManager().getCurrentScreen().getName());
    }

    @Test
    public void testCoinTossScreen_ObjectInitialization() {
        GameObject coin = coinTossScreen.getCoin();
        GameObject coinHeadsUserSelection = coinTossScreen.getCoinHeadsUserSelection();
        GameObject coinTailsUserSelection = coinTossScreen.getCoinTailsUserSelection();
        Champion selectedChampion = coinTossScreen.getSelectedChampion();
        AudioManager audioManager = coinTossScreen.getAudioManager();
        TextHandler textHandler = coinTossScreen.getTextHandler();
        AssetManager assetManager = coinTossScreen.getAssetManager();

        assertNotNull(coin);
        assertNotNull(coinHeadsUserSelection);
        assertNotNull(coinTailsUserSelection);
        assertNotNull(selectedChampion);
        assertNotNull(audioManager);
        assertNotNull(textHandler);
        assertNotNull(assetManager);
    }

    @Test
    public void testCoinTossScreen_ScreenSize() {
        float screenHeight = coinTossScreen.getScreenHeight();
        float screenWidth = coinTossScreen.getScreenWidth();

        assertNotNull(screenHeight);
        assertNotNull(screenWidth);
        assertEquals(screenHeight, game.getScreenHeight(), 0.0f);
        assertEquals(screenWidth, game.getScreenWidth(), 0.0f);
    }

    @Test
    public void testCoinTossScreen_Load_Bitmap_CoinHead() {
        String assetName = "CoinHead";

        Bitmap coinHeadBitmap = game.getAssetManager().getBitmap(assetName);

        assertNotNull(coinHeadBitmap);
    }

    @Test
    public void testCoinTossScreen_Load_Bitmap_CoinTail() {
        String assetName = "CoinTail";

        Bitmap coinTailBitmap = game.getAssetManager().getBitmap(assetName);

        assertNotNull(coinTailBitmap);
    }

    @Test
    public void testCoinTossScreen_Coin_StartsOnMaxWidths() {
        assertTrue(coinTossScreen.getCoin().getWidth() == CoinTossScreen.COIN_DIAMETER);
    }

    @Test
    public void testCoinTossScreen_CoinTailSelection_StartsOnMaxWidth() {
        assertTrue(coinTossScreen.getCoinTailsUserSelection().getWidth() == CoinTossScreen.COIN_DIAMETER);
    }

    @Test
    public void testCoinTossScreen_CoinHeadSelection_StartsOnMaxWidth() {
        assertTrue(coinTossScreen.getCoinHeadsUserSelection().getWidth() == CoinTossScreen.COIN_DIAMETER);
    }

    @Test
    public void testCoinTossScreen_UserChoiceHead() {
        //User Choice must start null so we know it changes to a valid state
        coinTossScreen.setUserChoice(null);

        coinTossScreen.setCoinHeadTouched(1);

        coinTossScreen.DetermineUserChoice();

        assertEquals(CoinState.HEAD, coinTossScreen.getUserChoice());
    }

    @Test
    public void testCoinTossScreen_UserChoiceTail() {
        //User Choice must start null so we know it changes to a valid state
        coinTossScreen.setUserChoice(null);

        coinTossScreen.setCoinTailTouched(1);

        coinTossScreen.DetermineUserChoice();

        assertEquals(CoinState.TAIL, coinTossScreen.getUserChoice());
    }

    @Test
    public void testCoinTossScreen_UserChoice_NotOnEither_CoinHead() {
        //User Choice must start null so we know it changes to a valid state
        coinTossScreen.setUserChoice(null);

        coinTossScreen.setCoinHeadTouched(2);

        coinTossScreen.DetermineUserChoice();

        assertNull(coinTossScreen.getUserChoice());
    }

    @Test
    public void testCoinTossScreen_UserChoice_NotOnEither_CoinTail() {
        //User Choice must start null so we know it changes to a valid state
        coinTossScreen.setUserChoice(null);

        coinTossScreen.setCoinTailTouched(2);

        coinTossScreen.DetermineUserChoice();

        assertNull(coinTossScreen.getUserChoice());
    }

    @Test
    public void testCoinTossScreen_UserChoice_NotOnEither_CoinBoth() {
        //User Choice must start null so we know it changes to a valid state
        coinTossScreen.setUserChoice(null);

        coinTossScreen.setCoinHeadTouched(2);

        coinTossScreen.DetermineUserChoice();

        assertNull(coinTossScreen.getUserChoice());
    }

    @Test
    public void testCoinTossScreen_CoinStartFlipping_TouchValid() {
        coinTossScreen.setCoinTouched(1);
        coinTossScreen.setCoinFlipping(false);

        coinTossScreen.StartCoinFlip();

        assertEquals(true, coinTossScreen.isCoinFlipping());
    }

    @Test
    public void testCoinTossScreen_CoinStartFlipping_TouchInvalid() {
        coinTossScreen.setCoinTouched(2);
        coinTossScreen.setCoinFlipping(false);

        coinTossScreen.StartCoinFlip();

        assertEquals(false, coinTossScreen.isCoinFlipping());
    }

    @Test
    public void testCoinTossScreen_CoinDecreasesPerStepWidth() {
        coinTossScreen.setNumberOfSpins(1);
        float coinWidth = 100;
        coinTossScreen.getCoin().setWidth(coinWidth);
        coinTossScreen.setCoinIncreasingWidth(false);

        coinTossScreen.ProcessCoinFlipping();

        assertTrue(coinTossScreen.getCoin().getWidth() == (coinWidth - CoinTossScreen.WIDTH_PER_STEP));
    }

    @Test
    public void testCoinTossScreen_CoinIncreasesPerStepWidth() {
        coinTossScreen.setNumberOfSpins(1);
        float coinWidth = 100;
        coinTossScreen.getCoin().setWidth(coinWidth);
        coinTossScreen.setCoinIncreasingWidth(true);

        coinTossScreen.ProcessCoinFlipping();

        assertTrue(coinTossScreen.getCoin().getWidth() == (coinWidth + CoinTossScreen.WIDTH_PER_STEP));
    }

    @Test
    public void testCoinTossScreen_CoinCompleteFlip_BitmapHead() {
        coinTossScreen.setNumberOfSpins(1);
        float coinWidth = 0 + CoinTossScreen.WIDTH_PER_STEP;
        coinTossScreen.getCoin().setWidth(coinWidth);
        coinTossScreen.setCoinIncreasingWidth(false);
        coinTossScreen.getCoin().setBitmap(coinTossScreen.getCoinHead());

        coinTossScreen.ProcessCoinFlipping();

        assertTrue(coinTossScreen.isCoinIncreasingWidth());
        assertTrue(coinTossScreen.isCoinIncreasingWidth());
        assertEquals(coinTossScreen.getCoin().getBitmap(), coinTossScreen.getCoinTail());
    }

    @Test
    public void testCoinTossScreen_CoinCompleteMinimalFlip_BitmapTail() {
        coinTossScreen.setNumberOfSpins(1);
        float coinWidth = 0 + CoinTossScreen.WIDTH_PER_STEP;
        coinTossScreen.getCoin().setWidth(coinWidth);
        coinTossScreen.setCoinIncreasingWidth(false);
        coinTossScreen.getCoin().setBitmap(coinTossScreen.getCoinTail());

        coinTossScreen.ProcessCoinFlipping();

        assertTrue(coinTossScreen.isCoinIncreasingWidth());
        assertTrue(coinTossScreen.isCoinIncreasingWidth());
        assertEquals(coinTossScreen.getCoin().getBitmap(), coinTossScreen.getCoinHead());
    }

    @Test
    public void testCoinTossScreen_CoinCompleteMaximumFlip() {
        int numberOfSpins = 1;
        coinTossScreen.setNumberOfSpins(numberOfSpins);
        float coinWidth = CoinTossScreen.COIN_DIAMETER + CoinTossScreen.WIDTH_PER_STEP;
        coinTossScreen.getCoin().setWidth(coinWidth);
        coinTossScreen.setCoinIncreasingWidth(false);

        coinTossScreen.ProcessCoinFlipping();

        assertFalse(coinTossScreen.isCoinIncreasingWidth());
        assertEquals(numberOfSpins-1,coinTossScreen.getNumberOfSpins());
    }

    @Test
    public void testCoinTossScreen_DetermineCoinChoiceHead() {
        coinTossScreen.setNumberOfSpins(0);
        coinTossScreen.getCoin().setBitmap(coinTossScreen.getCoinHead());

        coinTossScreen.DetermineCoinChoice();

        assertEquals(CoinState.HEAD, coinTossScreen.getCoinChoice());
    }

    @Test
    public void testCoinTossScreen_DetermineCoinChoiceTail() {
        coinTossScreen.setNumberOfSpins(0);
        coinTossScreen.getCoin().setBitmap(coinTossScreen.getCoinTail());

        coinTossScreen.DetermineCoinChoice();

        assertEquals(CoinState.TAIL, coinTossScreen.getCoinChoice());
    }

    @Test
    public void testCoinTossScreen_DetermineWin_PlayerWin() {
        coinTossScreen.setUserChoice(CoinState.HEAD);
        coinTossScreen.setCoinChoice(CoinState.HEAD);

        coinTossScreen.DetermineWin();

        assertEquals(PlayerTurn.PLAYER, coinTossScreen.getPlayerTurn());
    }

    @Test
    public void testCoinTossScreen_DetermineWin_PlayerLoss() {
        coinTossScreen.setUserChoice(CoinState.HEAD);
        coinTossScreen.setCoinChoice(CoinState.TAIL);

        coinTossScreen.DetermineWin();

        assertEquals(PlayerTurn.OPPONENT, coinTossScreen.getPlayerTurn());
    }
}
