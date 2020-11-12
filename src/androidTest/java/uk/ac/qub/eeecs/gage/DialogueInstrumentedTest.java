package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.Dialogue;
import uk.ac.qub.eeecs.game.TextHandler;

import static junit.framework.Assert.assertNotNull;

public class DialogueInstrumentedTest {

    /**
     * Abstract Dialogue intrumented testing approach
     *
     * @Author Robert Hawkes <40232279>
     */

    private Context context;
    private DemoGame game;
    private Dialogue dialogue;
    private AssetManager assetManager;
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

        title = "";
        messageLine1 = "";
        messageLine2 = "";

        //Initialise the Dialogue
        dialogue = new Dialogue(game, title, messageLine1, messageLine2) {
            @Override
            public int getResponse() {
                return 0;
            }
        };
    }

    @Test
    public void testDialogue_ObjectInitialization() {
        String title = dialogue.getTitle();
        String messageLine1 = dialogue.getMessageLine1();
        String messageLine2 = dialogue.getMessageLine2();
        TextHandler textHandler = dialogue.getTextHandler();
        int screenWidth = dialogue.getScreenWidth();
        int screenHeight = dialogue.getScreenHeight();
        GameObject background = dialogue.getBackground();

        assertNotNull(title);
        assertNotNull(messageLine1);
        assertNotNull(messageLine2);
        assertNotNull(textHandler);
        assertNotNull(screenWidth);
        assertNotNull(screenHeight);
        assertNotNull(background);
    }
}
