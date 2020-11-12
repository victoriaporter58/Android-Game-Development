package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.SplashScreen;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SplashScreenInstrumentedTest {
    /**
     * Splash Screen testing approach.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */

    private Context context;
    private DemoGame game;
    private SplashScreen splash;
    private AssetManager assetManager;
    private AudioManager audioManager;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        setupGame();
    }

    private void setupGame() {
        game = new DemoGame();
        game.mFileIO = new FileIO(context);

        assetManager = new AssetManager(game);
        audioManager = new AudioManager(game);
        game.mAssetManager = assetManager;
        game.mAudioManager = audioManager;
        game.mScreenManager = new ScreenManager(game);

        splash = new SplashScreen(game, context);
    }

    @Test
    public void testInitialSetUp(){
        assertNotNull(splash.getTextHandler());
        assertNotNull(splash.getAssetManager());
        assertNotNull(splash.getSplashScreenBackground());
        assertEquals(splash.getDelay(), 5, 0.0f);
    }

    @Test
    public void testSplashScreenDuration_GreaterThan(){
        ElapsedTime et = new ElapsedTime();
        et.totalTime = 6;

        boolean delayTimeHasElapsed = splash.checkIfDelayTimeHasElapsed(et);

        assertEquals(delayTimeHasElapsed, true);
    }

    @Test
    public void testSplashScreenDuration_LessThan(){
        ElapsedTime et = new ElapsedTime();
        et.totalTime = 4;

        boolean delayTimeHasElapsed = splash.checkIfDelayTimeHasElapsed(et);

        assertEquals(delayTimeHasElapsed, false);
    }
    @Test
    public void testSplashScreenDuration_EqualTo(){
        ElapsedTime et = new ElapsedTime();
        et.totalTime = 5;

        boolean delayTimeHasElapsed = splash.checkIfDelayTimeHasElapsed(et);

        assertEquals(delayTimeHasElapsed, false);
    }

    @Test
    public void testLaunchMainMenu_DelayNotElapsed_TouchEventNotHappened(){
        game.getScreenManager().addScreen(splash);

        splash.launchMainMenuScreen(false, false);

        assertEquals(game.getScreenManager().getCurrentScreen().getName(), "SplashScreen");
    }

    @Test
    public void testLaunchMainMenu_DelayElapsed_NoTouchEvent(){
        game.getScreenManager().addScreen(splash);

        splash.launchMainMenuScreen(true, false);

        assertEquals(game.getScreenManager().getCurrentScreen().getName(), "SaveTheWorldMainMenu");
    }

    @Test
    public void testLaunchMainMenu_DelayElapsed_TouchEventHappened(){
        game.getScreenManager().addScreen(splash);

        splash.launchMainMenuScreen(true, true);

        assertEquals(game.getScreenManager().getCurrentScreen().getName(), "SaveTheWorldMainMenu");
    }

    @Test
    public void testLaunchMainMenu_DelayNotElapsed_TouchEventHappened(){
        game.getScreenManager().addScreen(splash);

        splash.launchMainMenuScreen(false, true);

        assertEquals(game.getScreenManager().getCurrentScreen().getName(), "SaveTheWorldMainMenu");
    }
}
