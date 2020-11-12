package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.Player;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerTest {
    String gameScreenName = "Game Screen";
    @Mock
    private Game game;
    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);
    @Mock
    AssetManager assetManager;

    @Before
    public void setUp() {
        when(gameScreen.getName()).thenReturn(gameScreenName);
        when(game.getAssetManager()).thenReturn(assetManager);
        when(gameScreen.getGame()).thenReturn(game);
    }

    @Test
    public void test_manaGetterAndSetter(){
        //Setup
        Player player = new Player(gameScreen);
        int expected = 5;
        //Test
        player.setMana(expected);
        assert(player.getMana() == expected);
        //Expected to pass
    }

    @Test
    public void test_playerHealthGetterAndSetter(){
        //Setup
        Player player = new Player(gameScreen);
        int expected = 30;
        //Test
        player.setHealth(expected);
        assert (player.getHealth() == expected);
        //Expected to pass
    }

    @Test
    public void test_playerHealthReduction() {
        // Setup
        Player player = new Player(gameScreen);
        player.setHealth(30);
        int expected = 25;

        // Test
        player.reduceHealth(5);
        assert (player.getHealth() == expected);
    }

    @Test
    public void test_playerHealthEmptiness() {
        // Setup
        Player player = new Player(gameScreen);
        player.setHealth(0);

        // Test
        assert (player.isHealthEmpty() == true);
    }

    @Test
    public void test_playerManaReduction() {
        // Setup
        Player player = new Player(gameScreen);
        player.setMana(10);
        int expected = 5;

        // Test
        player.reduceMana(5);
        assert (player.getMana() == expected);
    }

    @Test
    public void test_playerManaEmptiness() {
        // Setup
        Player player = new Player(gameScreen);
        player.setMana(0);

        // Test
        assert (player.isManaEmpty() == true);
    }

    @Test
    public void test_playerPortraitConstructor() {

    }
}
