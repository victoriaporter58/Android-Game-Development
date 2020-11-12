package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.SaveTheWorldMainMenu;
import uk.ac.qub.eeecs.game.cardDemo.CharacterSelectScreen;
import uk.ac.qub.eeecs.game.cardDemo.EndGameScreen;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EndGameScreenTest {

    private String endGameScreenName = "EndGameScreen";
    private String mainMenuScreenName = "MainMenu";
    private String characterSelectScreenName = "CharacterSelectScreen";

    @Mock
    Game game = new DemoGame();

    @Mock
    EndGameScreen endGameScreen = Mockito.mock(EndGameScreen.class);

    @Mock
    SaveTheWorldMainMenu saveTheWorldMainMenu = Mockito.mock(SaveTheWorldMainMenu.class);

    @Mock
    CharacterSelectScreen characterSelectScreen = Mockito.mock(CharacterSelectScreen.class);

    @Mock
    AssetManager assetManager;

    @Mock
    Bitmap victorybitmap;
    @Mock
    Bitmap defeatbitmap;

    @Mock
    Input input;

    @Before
    public void setUp() {
        when(endGameScreen.getName()).thenReturn(endGameScreenName);
        when(saveTheWorldMainMenu.getName()).thenReturn(mainMenuScreenName);
        when(characterSelectScreen.getName()).thenReturn(characterSelectScreenName);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getAssetManager().getBitmap("Victory")).thenReturn(victorybitmap);
        when(game.getAssetManager().getBitmap("Defeat")).thenReturn(defeatbitmap);
        when(game.getInput()).thenReturn(input);
    }

    @Test
    public void test_endGameScreenPlayerWonVar() {
        EndGameScreen screen = new EndGameScreen(endGameScreenName,game,true);
        assertEquals(screen.playerWon,true);
    }

    @Test
    public void test_resultMessage() {
        EndGameScreen screen;
        screen = new EndGameScreen(endGameScreenName,game,true);
        assertEquals(screen.resultMessage.getBitmap(),victorybitmap);
        screen = new EndGameScreen(endGameScreenName,game,false);
        assertEquals(screen.resultMessage.getBitmap(),defeatbitmap);
    }
}
