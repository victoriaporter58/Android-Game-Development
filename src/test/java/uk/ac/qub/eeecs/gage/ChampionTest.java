package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.game.cardDemo.Champion;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ChampionTest {

    String championName = "Oak King";
    String changedChampionName = "Gaia";

    String deckJsonLocation = "txt/decks/OakKingDeck.JSON";
    String changedDeckJsonLocation = "txt/decks/GaiaDeck.JSON";

    int health = 30;
    int changedHealth = 20;

    Bitmap portrait;

/*    @Mock
    Game game;

    @Mock
    AssetManager assetManager;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager));
        when(assetManager.getBitmap()).thenReturn(portrait);
    }*/

//    @Test
//    public void setChampionName_ValidData_TestSuccess() {
//        Champion aChampion = new Champion(championName, deckJsonLocation, 30, portrait);
//        aChampion.setChampionName(changedChampionName);
//        assertEquals(changedChampionName, aChampion.getChampionName());
//    }
//
//    @Test
//    public void getChampionName_ValidData_TestSuccess() {
//        Champion aChampion = new Champion(championName, deckJsonLocation, 30, portrait);
//        assertEquals(championName, aChampion.getChampionName());
//    }
//
//    @Test
//    public void setDeckJsonLocation_ValidData_TestSuccess() {
//        Champion aChampion = new Champion(championName, deckJsonLocation, 30, portrait);
//        aChampion.setDeckJsonLocation(changedDeckJsonLocation);
//        assertEquals(changedDeckJsonLocation, aChampion.getDeckJsonLocation());
//    }
//
//    @Test
//    public void getDeckJsonLocation_ValidData_TestSuccess() {
//        Champion aChampion = new Champion(championName, deckJsonLocation, 30, portrait);
//        assertEquals(deckJsonLocation, aChampion.getDeckJsonLocation());
//    }
//
//    @Test
//    public void setHealth_ValidData_TestSuccess() {
//        Champion aChampion = new Champion(championName, deckJsonLocation, 30, portrait);
//        aChampion.setHealth(changedHealth);
//        assertEquals(changedHealth, aChampion.getHealth());
//    }
//
//    @Test
//    public void getHealth_ValidData_TestSuccess() {
//        Champion aChampion = new Champion(championName, deckJsonLocation, 30, portrait);
//        assertEquals(health, aChampion.getHealth());
//    }
}