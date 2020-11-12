package uk.ac.qub.eeecs.game.cardDemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

public class Deck {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////
    //declare deck
    private List<Card> mDeckCards;
    private int mTopOfDeck;
    private List<Card> mDiscardPile;
    private int mTopOfDiscardPile;
    private List<Card> mActiveCards;
    private int mCardsInHand;
    private List<Card> mActiveDefenderCards;
    private float mDeckOffset;
    private float mCardOffset;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Deck Class which holds a copy of the actors hand, deck of cards and discard pile.
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     * @param JSONlocation the file location of the JSON file which holds the card details
     */

    public Deck(String JSONlocation, GameScreen gameScreen, Game mGame) {
        mGame.getAssetManager().loadAssets("txt/assets/SaveTheWorldScreenAssets.JSON");
        DeckManager deckManager = new DeckManager(mGame, gameScreen);
        mDeckCards = deckManager.loadDeckFromJSON(JSONlocation);
        mTopOfDeck = mDeckCards.size() - 1;
        mDiscardPile = new ArrayList<>(mDeckCards.size());//same size as this is the maximum amount of cards which can be discarded
        mTopOfDiscardPile = 0;
        shuffleDeck();
        mActiveCards = new ArrayList<>(5);
        mCardsInHand = 0;
        mActiveDefenderCards = new ArrayList<>(5);
        mDeckOffset = 5f;
        mCardOffset = 0.5f;
    }

    /**
     * an overloaded constructor for use with testing, this does not require a JSON Location
     * it creates a set of cards of known values
     *  Created by &Justin johnston <40237507>
     * @param size The size of the deck to be created
     * @version 1.0
     */
    public Deck(int size, GameScreen gameScreen) {
        mDiscardPile = new ArrayList<>(size);//same size as this is the maximum amount of cards which can be discarded
        mDeckCards = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            mDeckCards.add(new Card(0, 0, gameScreen, 2, 2, 2, "Test " + i, "test card " + i, "Tree",AbilityType.NONE));
        }
        mTopOfDeck = size - 1;
        mTopOfDiscardPile = 0;
        mActiveCards = new ArrayList<>(9);
        mCardsInHand = 0;
        mActiveDefenderCards = new ArrayList<>(5);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters for Testing
    // /////////////////////////////////////////////////////////////////////////

    public int getTopOfDeck() {
        return mTopOfDeck;
    }

    public int getTopOfDiscardPile() {
        return mTopOfDiscardPile;
    }

    public List<Card> getDeckCards() {
        return mDeckCards;
    }

    public List<Card> getActiveCards() {
        return mActiveCards;
    }

    public int getCardsInHand() {
        return mCardsInHand;
    }

    public void setCardsInHand(int mCardsInHand) {
        this.mCardsInHand = mCardsInHand;
    }

    public List<Card> getDiscardPile() {
        return mDiscardPile;
    }

    public List<Card> getActiveDefenderCards() {
        return mActiveDefenderCards;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * checks that the actor has enough space in their hand to receive a card
     * returns a boolean to indicate if a card was returned
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public boolean drawCard(float xPosition, float yPosition) {
        if (mCardsInHand < Board.HANDCAPACITY) {
            Card card = getTopOfDeck(xPosition, yPosition);
            if(card != null){
                card.setInHand(true);
                mActiveCards.add(card);
                mCardsInHand++;
                return true;
            }
        }
        return false;
    }

    /**
     * set a card as not in the players hand and decrease the counter for cards in hand
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void removeCardFromHand(Card card) {
        card.setInHand(false);
        mCardsInHand--;
    }

    /**
     * add a card to the list of defenders, this is used by the card ability system
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void addCardtoDefenderList(Card card) {
        mActiveDefenderCards.add(card);
    }

    /**
     * checks that the deck is not empty and then returns a card
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public Card getTopOfDeck(float xPosition, float yPosition) {
        if (mTopOfDeck < 0) return null;
        Card card = mDeckCards.get(mTopOfDeck);
        card.setPosition(xPosition, yPosition);
        mDeckCards.remove(mTopOfDeck);
        mTopOfDeck--;
        card.resetCard();
        return card;
    }

    /**position deck on the game board, slightly moves the cards to give a 3d deck appearance
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void placeDeckOnBoard(float xPosition, float yPosition) {
        float xPositionDeck = xPosition - mDeckOffset;
        for(Card card:mDeckCards){
            card.setPosition(xPositionDeck, yPosition);
            card.resetCard();
            xPositionDeck+=mCardOffset;
        }
    }

    /**
     * adds card to the bottom of the deck
     *  Created by &Justin johnston <40237507>
     * @param card The Card to be added to the deck
     */
    public void addCard(Card card) {
        mDeckCards.add(0, card);
        mTopOfDeck++;
    }

    /**
     * Shuffles the deck
     *  Created by &Justin johnston <40237507>
     */
    public void shuffleDeck() {
        Collections.shuffle(mDeckCards);
    }

    /**
     * adds card to the top of the discard pile
     *  Created by &Justin johnston <40237507>
     * @param card The Card to be added to the discard pile
     */
    public void addToDiscardPile(Card card) {
        mDiscardPile.add(mTopOfDiscardPile, card);
        mTopOfDiscardPile++;
    }

    /**
     * adds card to the top of the deck
     * used for testing only
     *  Created by &Justin johnston <40237507>
     * @param card The Card to be added to the discard pile
     */
    public void addToTopOfDeck(Card card) {
        mDeckCards.add(card);
        mTopOfDeck++;
    }

    /**
     * removes a card from the top of the discard pile
     *  Created by &Justin johnston <40237507>
     * @param xPosition Position of card on X axis
     * @param yPosition Position of card on Y axis
     */
    public Card removeFromDiscardPile(float xPosition, float yPosition) {
        if (mTopOfDiscardPile <= 0) return null;
        Card card = mDiscardPile.get(--mTopOfDiscardPile);
        card.resetCard();
        card.setPosition(xPosition, yPosition);
        return card;
    }

    /**
     * this method is called by the main game and this ensures all decks are drawn in screen
     *  Created by &Justin johnston <40237507>
     */
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport){
        drawCards(elapsedTime,graphics2D,layerViewport, screenViewport, getActiveCards());
        drawCards(elapsedTime,graphics2D,layerViewport, screenViewport, getDeckCards());
        drawCards(elapsedTime,graphics2D,layerViewport, screenViewport, getDiscardPile());
    }

    /**
     * this method calls each card in the individual decks and calls their draw method
     *  Created by &Justin johnston <40237507>
     */
    public void drawCards(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport,List<Card> deck){

        for(int i = deck.size()-1;i>=0;i--){
            deck.get(i).draw(elapsedTime, graphics2D,
                    layerViewport, screenViewport);
        }
    }

    /**
     * update the active cards on the field
     * this changes the image based on whats happening in game
     *  Created by &Justin johnston <40237507>
     */
    public void update(ElapsedTime elapsedTime){
        for (Card card : getActiveCards()) {
            card.update(elapsedTime);
        }
    }
}
