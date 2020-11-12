package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.HowToPlayScreen;
import uk.ac.qub.eeecs.game.TextHandler;

import static junit.framework.Assert.assertNotNull;

public class HowToPlayInstrumentedTest {
    /**
     * How to play screen testing approach.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */

    private Context context;
    private DemoGame game;
    private HowToPlayScreen howToPlayScreen;
    private AudioManager audioManager;
    private AssetManager assetManager;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        setupGame();
    }

    private void setupGame() {
        game = new DemoGame();

        game.mFileIO = new FileIO(context);
        assetManager = new AssetManager(game);
        game.mAssetManager = assetManager;
        audioManager = new AudioManager(game);
        game.mAudioManager = audioManager;
        game.mScreenManager = new ScreenManager(game);

        howToPlayScreen = new HowToPlayScreen(game);
    }

    @Test
    public void testObjectCreation_Constructor_Test(){
        AssetManager assetManager = howToPlayScreen.getAssetManager();
        ScreenManager screenManager = howToPlayScreen.getScreenManager();
        TextHandler textHandler = howToPlayScreen.getTextHandler();
        PushButton nextPageButton = howToPlayScreen.getNextPageButton();
        PushButton previousPageButton = howToPlayScreen.getPreviousPageButton();
        PushButton returnButton = howToPlayScreen.getReturnButton();

        assertNotNull(assetManager);
        assertNotNull(nextPageButton);
        assertNotNull(previousPageButton);
        assertNotNull(returnButton);
        assertNotNull(screenManager);
        assertNotNull(textHandler);
    }
}
