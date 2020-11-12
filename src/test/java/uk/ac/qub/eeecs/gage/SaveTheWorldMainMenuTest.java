package uk.ac.qub.eeecs.gage;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.game.cardDemo.CharacterSelectScreen;
import uk.ac.qub.eeecs.game.optionsScreen.OptionsScreen;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SaveTheWorldMainMenuTest {

    String characterSelectScreenName = "CharacterSelectScreen";
    String optionsScreenScreenName = "OptionsScreen";

    @Mock
    Game game;

    @Mock
    CharacterSelectScreen characterSelectScreen = Mockito.mock(CharacterSelectScreen.class);

    @Mock
    OptionsScreen optionsScreen = Mockito.mock(OptionsScreen.class);

    @Before
    public void setUp() {
        when(characterSelectScreen.getName()).thenReturn(characterSelectScreenName);
        when(optionsScreen.getName()).thenReturn(optionsScreenScreenName);
    }

    @Test
    public void launchGame_ScreenChange_TestSuccess() {

        ScreenManager screenManager = new ScreenManager(game);
        screenManager.addScreen(characterSelectScreen);
        assertEquals(characterSelectScreen, screenManager.getCurrentScreen());
    }

    @Test
    public void launchOptions_ScreenChange_TestSuccess() {

        ScreenManager screenManager = new ScreenManager(game);
        screenManager.addScreen(optionsScreen);
        assertEquals(optionsScreen, screenManager.getCurrentScreen());
    }
}