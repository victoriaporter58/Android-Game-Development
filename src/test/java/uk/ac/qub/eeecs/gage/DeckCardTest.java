package uk.ac.qub.eeecs.gage;


import uk.ac.qub.eeecs.game.cardDemo.AbilityType;
import uk.ac.qub.eeecs.game.cardDemo.Card;
import uk.ac.qub.eeecs.game.cardDemo.Deck;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.List;
import uk.ac.qub.eeecs.gage.world.GameScreen;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DeckCardTest {
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

    class testCard extends Card {
        public testCard(float x, float y, GameScreen gameScreen, int attack, int health, int cost, String name, String text, String portraitImage) {
            super(x, y, gameScreen, health, attack, cost, name, text, portraitImage, AbilityType.NONE);
        }
    }

    @Test
    public void cardCreate() {
        testCard card = new testCard(100, 100, gameScreen, 2, 2, 1, "Test Card",
                "This card is for testing", "Tree");

        assertNotNull(card);
        assertEquals(card.getHeight(), card.CARD_HEIGHT, 0);
        assertEquals(card.getWidth(), card.CARD_WIDTH, 0);
        assertEquals(card.position.x, 100, 0);
        assertEquals(card.position.y, 100, 0);
        assertEquals(card.getHealth(), 2, 0);
        assertEquals(card.getAttack(), 2, 0);
        assertEquals(card.getCost(), 1, 0);
        assertEquals(card.getCardName(), "Test Card");
        assertEquals(card.getCardText(), "This card is for testing");
    }

    @Test
    public void cardFlip() {
        testCard card = new testCard(100, 100, gameScreen, 2, 2, 1, "Test Card",
                "This card is for testing", "Tree");
        card.flipCard();
        assertTrue(card.getFaceUp());
        card.flipCard();
        assertFalse(card.getFaceUp());
    }

    @Test
    public void card1Damage() {
        testCard card = new testCard(100, 100, gameScreen, 2, 2, 1, "Test Card",
                "This card is for testing", "Tree");
        card.damageCard(1);
        assertTrue(card.getHealth()==1);
    }

    @Test
    public void cardDamageKillCard() {
        testCard card = new testCard(100, 100, gameScreen, 2, 2, 1, "Test Card",
                "This card is for testing", "Tree");
        card.damageCard(300);
        assertTrue(card.getHealth()==0);
    }

    @Test
    public void cardMove() {
        testCard card = new testCard(100, 100, gameScreen, 2, 2, 1, "Test Card", "This card is for testing", "Tree");
        assertNotNull(card);
        assertEquals(card.position.x, 100, 0);
        assertEquals(card.position.y, 100, 0);
        card.setPosition(card.position.x + 100, card.position.y + 100);
        assertEquals(card.position.x, 200, 0);
        assertEquals(card.position.y, 200, 0);
    }

    @Test
    public void deckCreate() {
        Deck deck = new Deck(15, gameScreen);
        assertNotNull(deck);
        //check deck has 15 cards
        assertEquals(deck.getTopOfDeck(), 14);
        //draw card
        Card card = deck.getTopOfDeck(100, 100);
        assertNotNull(card);
        //check we got expected card
        assertEquals(card.getCardName(), "Test 14");
        //check card has been removed  and top of deck is now 13
        assertEquals(deck.getTopOfDeck(), 13);
        //add new card to bottom and check deck is now 15 cards again
        deck.addCard(card);
        assertEquals(deck.getTopOfDeck(), 14);
        //draw from top and make sure top card is now as expected
        card = deck.getTopOfDeck(100, 100);
        assertNotNull(card);
        assertEquals(card.getCardName(), "Test 13");

    }

    @Test
    public void drawfromEmptyDeck() {
        Deck deck = new Deck(2, gameScreen);
        assertNotNull(deck);
        assertEquals(deck.getTopOfDeck(), 1);
        Card card = deck.getTopOfDeck(100, 100);
        assertNotNull(card);
        card = deck.getTopOfDeck(100, 100);
        assertNotNull(card);
        card = deck.getTopOfDeck(100, 100);
        assertNull(card);
    }

    @Test
    public void drawCard() {
        Deck deck = new Deck(2, gameScreen);
        assertTrue(deck.drawCard(100,100));
    }

    @Test
    public void drawCardWithFullHand() {
        Deck deck = new Deck(2, gameScreen);
        deck.setCardsInHand(9);
        assertFalse(deck.drawCard(100,100));
    }

    @Test
    public void shuffleCardTest() {
        //create new deck
        Deck deck = new Deck(3, gameScreen);
        Deck deck2 = deck;
        assertNotNull(deck);
        //create a list of cards and place the entire decks contents in it
        List<Card> mCards;
        mCards = deck.getDeckCards();
        Assert.assertTrue(mCards.containsAll(deck.getDeckCards()));//check it contains the complete collection as expected
        // shuffle deck and confirm the contents are still the same
        deck.shuffleDeck();
        Assert.assertTrue(mCards.containsAll(deck2.getDeckCards()));
    }

    @Test
    public void DiscardPileTest() {
        //create new deck, creating the discard pile
        Deck deck = new Deck(15, gameScreen);
        assertNotNull(deck);
        //draw card from the deck
        Card card = deck.getTopOfDeck(100, 100);
        //check we got expected card
        assertEquals(card.getCardName(), "Test 14");
        //check card has been removed  and top of deck is now 13
        assertEquals(deck.getTopOfDeck(), 13);
        deck.addToDiscardPile(card);
        assertTrue(deck.getDiscardPile().size()== 1);
        card = deck.removeFromDiscardPile(100, 100);
        assertEquals(card.getCardName(), "Test 14");
    }

    @Test
    public void drawFromEmptyDiscardPile() {

        Deck deck = new Deck(2, gameScreen);
        assertNotNull(deck);
        //check deck has 15 cards
        assertEquals(deck.getTopOfDiscardPile(), 0);
        Card card = deck.removeFromDiscardPile(100, 100);
        assertNull(card);
    }

    @Test
    public void createHandTest() {
        //create new deck, creating a new hand
        Deck deck = new Deck(2, gameScreen);
        assertNotNull(deck);
        //check hand has zero cards
        assertEquals(deck.getCardsInHand(), 0);
        //add card from deck to hand
        deck.drawCard(100, 100);
        assertTrue(deck.getCardsInHand() == 1);
        Card card = deck.getActiveCards().get(0);
        //check that it is the card we expect
        assertEquals(card.getCardName(), "Test 1");
    }

    @Test
    public void RemoveFromHandTest() {
        Deck deck = new Deck(2, gameScreen);
        deck.drawCard(0,0);
        assertTrue(deck.getCardsInHand() == 1);
        Card card = deck.getActiveCards().get(0);
        deck.removeCardFromHand(card);
        assertTrue(deck.getCardsInHand() == 0);
    }

    @Test
    public void AddDefenderTest() {
        Deck deck = new Deck(2, gameScreen);
        deck.drawCard(0,0);
        Card card = deck.getActiveCards().get(0);
        deck.addCardtoDefenderList(card);
        assertTrue(deck.getActiveDefenderCards().size()==1);
    }

    @Test
    public void PlaceDeckOnBoardTest() {
        Deck deck = new Deck(2, gameScreen);
        deck.placeDeckOnBoard(100,100);
        assertTrue(deck.getDeckCards().get(0).position.x== 100);
        assertTrue(deck.getDeckCards().get(0).position.y== 100);
    }

    @Test
    public void addCardTest() {
        Deck deck = new Deck(2, gameScreen);
        testCard card = new testCard(100, 100, gameScreen, 2, 2, 1, "Test Card",
                "This card is for testing", "Tree");
        deck.addCard(card);
        assertTrue(deck.getDeckCards().size() ==3);
    }

    @Test
    public void addCardToDiscardPileTest() {
        Deck deck = new Deck(2, gameScreen);
        deck.drawCard(0,0);
        Card card = deck.getActiveCards().get(0);
        deck.addToDiscardPile(card);
        assertTrue(deck.getTopOfDiscardPile() ==1);
    }

    @Test
    public void RemoveFromDiscardPileTest() {
        Deck deck = new Deck(2, gameScreen);
        deck.drawCard(0,0);
        testCard card = new testCard(100, 100, gameScreen, 2, 2, 1, "Discarded Card",
                "This card is for testing", "Tree");
        deck.addToDiscardPile(card);
        assertTrue(deck.removeFromDiscardPile(0,0).getCardName()=="Discarded Card");
    }

    @Test
    public void RemoveFromEmptyDiscardPileTest() {
        Deck deck = new Deck(2, gameScreen);
        assertTrue(deck.removeFromDiscardPile(0,0) ==null);
    }
}
