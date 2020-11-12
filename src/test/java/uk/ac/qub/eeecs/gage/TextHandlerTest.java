package uk.ac.qub.eeecs.gage;

import android.graphics.Color;
import android.graphics.Paint;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.TextHandler;
import uk.ac.qub.eeecs.game.TextType;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class TextHandlerTest {
    /**
     * Unit testing approach to TextHandler.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */

    @Mock
    TextHandler textHandler;
    @Mock
    Paint paint = Mockito.mock(Paint.class);
    @Mock
    Game game = Mockito.mock(Game.class);
    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);

    @Before
    public void setup() {
        when(gameScreen.getGame()).thenReturn(game);
        when(game.getScreenHeight()).thenReturn(400);
        when(game.getScreenWidth()).thenReturn(400);
    }

    @Test
    public void testTitleFontSize_Test_Success(){
        textHandler = new TextHandler(game);

        assertEquals(textHandler.getTitleFontSize(),game.getScreenWidth()/15.0f,0f);
        assertEquals(textHandler.getSubtitleFontSize(),game.getScreenWidth()/25.0f,0f);
        assertEquals(textHandler.getBodyFontSize(),game.getScreenWidth()/30.0f,0f);
    }

    @Test
    public void styleFontStyling_Title(){
        textHandler = new TextHandler(game);
        textHandler.setPaintObj(paint);

        textHandler.setFontStyling(TextType.TITLE);

        assertEquals(textHandler.getCurrentFontSize(), textHandler.getTitleFontSize(), 0f);
    }

    @Test
    public void styleFontStyling_Subtitle(){
        textHandler = new TextHandler(game);
        textHandler.setPaintObj(paint);

        textHandler.setFontStyling(TextType.SUBTITLE);

        assertEquals(textHandler.getCurrentFontSize(), textHandler.getSubtitleFontSize(), 0f);
    }

    @Test
    public void styleFontStyling_Body(){
        textHandler = new TextHandler(game);
        textHandler.setPaintObj(paint);

        textHandler.setFontStyling(TextType.BODY);

        assertEquals(textHandler.getCurrentFontSize(), textHandler.getBodyFontSize(), 0f);
    }

    @Test
    public void styleFontColour_Test_Success(){
        textHandler = new TextHandler(game);
        textHandler.setPaintObj(paint);

        textHandler.setCurrentColour(Color.WHITE);

        assertEquals(textHandler.getCurrentColour(), Color.WHITE, 0f);
    }
}