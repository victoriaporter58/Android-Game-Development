package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.game.HowToPlayScreen;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class HowToPlayScreenTest {
    /**
     * Testing approach to the How To Play screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */

    @Mock
    AssetManager assetManager = Mockito.mock(AssetManager.class);
    @Mock
    Game game = Mockito.mock(Game.class);
    @Mock
    HowToPlayScreen howToPlayScreen = Mockito.mock(HowToPlayScreen.class);

    @Before
    public void setup() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(game.getScreenHeight()).thenReturn(400);
        when(game.getScreenWidth()).thenReturn(600);

        howToPlayScreen = new HowToPlayScreen(game);
    }

    @Test
    public void testConstructorSetUp_Test_Success(){
        assertEquals(howToPlayScreen.getScreenHeight(), game.getScreenHeight());
        assertEquals(howToPlayScreen.getScreenWidth(), game.getScreenWidth());
        assertEquals(howToPlayScreen.getPageNumber(), 0);
        assertEquals(howToPlayScreen.getMAX_PAGE_NUMBER(), 4);
    }

    @Test
    public void testInformationDrawnOnPageZero_Test_Success(){
        // Page number should = 0 as defined in the constructor, thus we don't need to set it
        assertEquals(howToPlayScreen.getPageNumber(), 0, 0f);
        howToPlayScreen.setInformationTextForPage();

        // Check that the correct information is assigned
        assertEquals(howToPlayScreen.getSubtitleText(), "Viewing Cards");
        assertEquals(howToPlayScreen.getBodyText(), "Long press any card to view specific information.");
        assertEquals(howToPlayScreen.getAssetName(), "EnlargedCardScreenshot");
    }

    @Test
    public void testInformationDrawnOnPageOne_Test_Success(){
        howToPlayScreen.setPageNumber(1);
        howToPlayScreen.setInformationTextForPage();

        // Check that the correct information is assigned
        assertEquals(howToPlayScreen.getPageNumber(), 1, 0f);
        assertEquals(howToPlayScreen.getSubtitleText(), "Playing Cards");
        assertEquals(howToPlayScreen.getBodyText(), "Drag your card(s) onto the board.");
        assertEquals(howToPlayScreen.getAssetName(), "DragCardScreenshot");
    }

    @Test
    public void testInformationDrawnOnPageTwo_Test_Success(){
        howToPlayScreen.setPageNumber(2);
        howToPlayScreen.setInformationTextForPage();

        // Check that the correct information is assigned
        assertEquals(howToPlayScreen.getPageNumber(), 2, 0f);
        assertEquals(howToPlayScreen.getSubtitleText(), "Attacking");
        assertEquals(howToPlayScreen.getBodyText(), "Drag a card onto opponents avatar to reduce their health.");
        assertEquals(howToPlayScreen.getAssetName(), "DragCardToAvatarScreenshot");
    }

    @Test
    public void testInformationDrawnOnPageThree_Test_Success(){
        howToPlayScreen.setPageNumber(3);
        howToPlayScreen.setInformationTextForPage();

        // Check that the correct information is assigned
        assertEquals(howToPlayScreen.getPageNumber(), 3, 0f);
        assertEquals(howToPlayScreen.getSubtitleText(), "Attacking");
        assertEquals(howToPlayScreen.getBodyText(), "Drag a card onto opponents card(s) to reduce its health.");
        assertEquals(howToPlayScreen.getAssetName(), "DragCardToCardScreenshot");
    }

    @Test
    public void testInformationDrawnOnFinalPage_Test_Success(){
        howToPlayScreen.setPageNumber(howToPlayScreen.getMAX_PAGE_NUMBER());
        howToPlayScreen.setInformationTextForPage();

        // Check that the correct information is assigned
        assertEquals(howToPlayScreen.getPageNumber(), howToPlayScreen.getMAX_PAGE_NUMBER(), 0f);
        assertEquals(howToPlayScreen.getSubtitleText(), "Ending Your Turn");
        assertEquals(howToPlayScreen.getBodyText(), "Press End Turn when you're finished moving cards.");
        assertEquals(howToPlayScreen.getAssetName(), "EndTurnScreenshot");
    }
    @Test
    public void testInformationDrawnOnInvalidPage_Test_Success(){
        // Testing drawing to a page that doesn't exist
        howToPlayScreen.setPageNumber(howToPlayScreen.getMAX_PAGE_NUMBER() + 5);
        howToPlayScreen.setInformationTextForPage();

        // Check that the correct information is assigned
        assertEquals(howToPlayScreen.getPageNumber(), howToPlayScreen.getMAX_PAGE_NUMBER() + 5, 0f);
        assertEquals(howToPlayScreen.getSubtitleText(), "Page Doesn't Exist!");
        assertEquals(howToPlayScreen.getBodyText(), "This page does not exist yet.");
        assertEquals(howToPlayScreen.getAssetName(), "Error");
    }

    @Test
    public void testDynamicButtonDimensions_Test_Success(){
        assertEquals(howToPlayScreen.getNextPageButton().getHeight(), howToPlayScreen.getButtonLengthAndWidth(), 0f);
        assertEquals(howToPlayScreen.getPreviousPageButton().getHeight(), howToPlayScreen.getButtonLengthAndWidth(), 0f);
        assertEquals(howToPlayScreen.getReturnButton().getHeight(), howToPlayScreen.getButtonLengthAndWidth(), 0f);

        assertEquals(howToPlayScreen.getNextPageButton().getWidth(), howToPlayScreen.getButtonLengthAndWidth(), 0f);
        assertEquals(howToPlayScreen.getPreviousPageButton().getWidth(), howToPlayScreen.getButtonLengthAndWidth(), 0f);
        assertEquals(howToPlayScreen.getReturnButton().getWidth(), howToPlayScreen.getButtonLengthAndWidth(), 0f);
    }

    @Test
    public void testPageIncrementToValidPage_Test_Success(){
        // Page number is initially 0
        assertEquals(howToPlayScreen.getPageNumber(), 0, 0f);
        howToPlayScreen.setInformationTextForPage();

        // Check that the correct information is assigned
        assertEquals(howToPlayScreen.getSubtitleText(), "Viewing Cards");
        assertEquals(howToPlayScreen.getBodyText(), "Long press any card to view specific information.");
        assertEquals(howToPlayScreen.getAssetName(), "EnlargedCardScreenshot");

        // Increment page number (pageNumber = 1)
        howToPlayScreen.incrementPageNumber();
        howToPlayScreen.setInformationTextForPage();
        assertEquals(howToPlayScreen.getPageNumber(), 1, 0f);

        // Check that the text is for page 1
        assertEquals(howToPlayScreen.getSubtitleText(), "Playing Cards");
        assertEquals(howToPlayScreen.getBodyText(), "Drag your card(s) onto the board.");
        assertEquals(howToPlayScreen.getAssetName(), "DragCardScreenshot");
    }

    @Test
    public void testPageDecrementToValidPage_Test_Success(){
        // Set page number = 3 initially
        howToPlayScreen.setPageNumber(3);
        howToPlayScreen.setInformationTextForPage();

        // Check that the correct information is assigned
        assertEquals(howToPlayScreen.getPageNumber(), 3, 0f);
        assertEquals(howToPlayScreen.getSubtitleText(), "Attacking");
        assertEquals(howToPlayScreen.getBodyText(), "Drag a card onto opponents card(s) to reduce its health.");
        assertEquals(howToPlayScreen.getAssetName(), "DragCardToCardScreenshot");

        // Decrement page number by 1 (pageNumber = 2)
        howToPlayScreen.decrementPageNumber();
        howToPlayScreen.setInformationTextForPage();
        assertEquals(howToPlayScreen.getPageNumber(), 2, 0f);

        // Check that text is equal to page 2 text
        assertEquals(howToPlayScreen.getSubtitleText(), "Attacking");
        assertEquals(howToPlayScreen.getBodyText(), "Drag a card onto opponents avatar to reduce their health.");
        assertEquals(howToPlayScreen.getAssetName(), "DragCardToAvatarScreenshot");
    }

    @Test
    public void testPageDecrementToInvalidPage_Test_Success(){
        // Page number is 0 initially
        assertEquals(howToPlayScreen.getPageNumber(), 0, 0f);
        howToPlayScreen.setInformationTextForPage();

        // Check that the correct information is assigned
        assertEquals(howToPlayScreen.getSubtitleText(), "Viewing Cards");
        assertEquals(howToPlayScreen.getBodyText(), "Long press any card to view specific information.");
        assertEquals(howToPlayScreen.getAssetName(), "EnlargedCardScreenshot");

        // Decrement page number, i.e. page number will = -1
        howToPlayScreen.decrementPageNumber();
        howToPlayScreen.setInformationTextForPage();
        assertEquals(howToPlayScreen.getPageNumber(), -1, 0f);

        // Check that text is equal to error page text
        assertEquals(howToPlayScreen.getSubtitleText(), "Page Doesn't Exist!");
        assertEquals(howToPlayScreen.getBodyText(), "This page does not exist yet.");
        assertEquals(howToPlayScreen.getAssetName(), "Error");
    }

    @Test
    public void testPageIncrementToInvalidPage_Test_Success(){
        // Set page number to max page number initially
        howToPlayScreen.setPageNumber(howToPlayScreen.getMAX_PAGE_NUMBER());
        howToPlayScreen.setInformationTextForPage();

        // Check that the correct information is assigned
        assertEquals(howToPlayScreen.getPageNumber(), howToPlayScreen.getMAX_PAGE_NUMBER(), 0f);
        assertEquals(howToPlayScreen.getSubtitleText(), "Ending Your Turn");
        assertEquals(howToPlayScreen.getBodyText(), "Press End Turn when you're finished moving cards.");
        assertEquals(howToPlayScreen.getAssetName(), "EndTurnScreenshot");

        // Increment page number, i.e. page number will = maxPageNumber + 1
        howToPlayScreen.incrementPageNumber();
        assertEquals(howToPlayScreen.getPageNumber(), howToPlayScreen.getMAX_PAGE_NUMBER() + 1, 0f);
        howToPlayScreen.setInformationTextForPage();

        // Check that text is equal to error page text
        assertEquals(howToPlayScreen.getSubtitleText(), "Page Doesn't Exist!");
        assertEquals(howToPlayScreen.getBodyText(), "This page does not exist yet.");
        assertEquals(howToPlayScreen.getAssetName(), "Error");
    }
}