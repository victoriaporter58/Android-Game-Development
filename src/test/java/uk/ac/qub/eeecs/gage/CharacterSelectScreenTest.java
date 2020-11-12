package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.game.SaveTheWorldMainMenu;
import uk.ac.qub.eeecs.game.cardDemo.SaveTheWorldScreen;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CharacterSelectScreenTest {

    private String saveTheWorldScreenName = "SaveTheWorldScreen";
    private String mainMenuScreenName = "MainMenu";

    @Mock
    Game game;

    @Mock
    SaveTheWorldScreen saveTheWorldScreen = Mockito.mock(SaveTheWorldScreen.class);

    @Mock
    SaveTheWorldMainMenu saveTheWorldMainMenu = Mockito.mock(SaveTheWorldMainMenu.class);

    @Before
    public void setUp() {
        when(saveTheWorldScreen.getName()).thenReturn(saveTheWorldScreenName);
        when(saveTheWorldMainMenu.getName()).thenReturn(mainMenuScreenName);
    }

    @Test
    public void launchGame_ScreenChange_TestSuccess() {

        ScreenManager screenManager = new ScreenManager(game);
        screenManager.addScreen(saveTheWorldScreen);
        assertEquals(saveTheWorldScreen, screenManager.getCurrentScreen());
    }

    @Test
    public void return_ScreenChange_TestSuccess() {

        ScreenManager screenManager = new ScreenManager(game);
        screenManager.addScreen(saveTheWorldMainMenu);
        assertEquals(saveTheWorldMainMenu, screenManager.getCurrentScreen());
    }
}
