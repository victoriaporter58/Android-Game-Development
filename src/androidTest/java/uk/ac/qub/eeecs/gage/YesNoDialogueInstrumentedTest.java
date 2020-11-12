package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.TextHandler;
import uk.ac.qub.eeecs.game.YesNoDialogue;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class YesNoDialogueInstrumentedTest {

    /**
     * YesNoDialogue intrumented testing approach
     *
     * @Author Robert Hawkes <40232279>
     */

    private Context context;
    private DemoGame game;
    private YesNoDialogue yesNoDialogue;
    private AssetManager assetManager;
    private AudioManager audioManager;
    private String title;
    private String messageLine1;
    private String messageLine2;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        setupGame();
    }

    private void setupGame() {

        // set up a game instance to perform tests on
        game = new DemoGame();

        game.mFileIO = new FileIO(context);

        //Set up local Asset manager
        assetManager = new AssetManager(game);
        game.mAssetManager = assetManager;

        audioManager = new AudioManager(game);
        game.mAudioManager = audioManager;

        title = "";
        messageLine1 = "";
        messageLine2 = "";

        //Initialise the Dialogue
        yesNoDialogue = new YesNoDialogue(game, title, messageLine1, messageLine2);
    }

    @Test
    public void testYesNoDialogue_ObjectInitialization() {
        String title = yesNoDialogue.getTitle();
        String messageLine1 = yesNoDialogue.getMessageLine1();
        String messageLine2 = yesNoDialogue.getMessageLine2();
        TextHandler textHandler = yesNoDialogue.getTextHandler();
        int screenWidth = yesNoDialogue.getScreenWidth();
        int screenHeight = yesNoDialogue.getScreenHeight();
        GameObject background = yesNoDialogue.getBackground();
        int response = yesNoDialogue.getResponse();
        boolean yesButtonTouched = yesNoDialogue.isYesButtonTouched();
        boolean noButtonTouched = yesNoDialogue.isNoButtonTouched();
        PushButton yesButton = yesNoDialogue.getYesButton();
        PushButton noButton = yesNoDialogue.getNoButton();

        assertNotNull(title);
        assertNotNull(messageLine1);
        assertNotNull(messageLine2);
        assertNotNull(textHandler);
        assertNotNull(screenWidth);
        assertNotNull(screenHeight);
        assertNotNull(background);
        assertNotNull(response);
        assertNotNull(yesButtonTouched);
        assertNotNull(noButtonTouched);
        assertNotNull(yesButton);
        assertNotNull(noButton);
    }

    @Test
    public void testYesNoDialogue_response_Yes() {
        yesNoDialogue.setYesButtonTouched(true);

        yesNoDialogue.checkResponse();

        assertEquals(YesNoDialogue.YES_BUTTON_PRESSED,yesNoDialogue.getResponse());
    }

    @Test
    public void testYesNoDialogue_response_No() {
        yesNoDialogue.setNoButtonTouched(true);

        yesNoDialogue.checkResponse();

        assertEquals(YesNoDialogue.NO_BUTTON_PRESSED,yesNoDialogue.getResponse());
    }

    @Test
    public void testYesNoDialogue_response_NoTouch() {
        yesNoDialogue.setYesButtonTouched(false);
        yesNoDialogue.setNoButtonTouched(false);

        yesNoDialogue.checkResponse();

        assertEquals(YesNoDialogue.NO_TOUCH_ON_BUTTONS,yesNoDialogue.getResponse());
    }
}
