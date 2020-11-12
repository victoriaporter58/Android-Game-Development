package uk.ac.qub.eeecs.game.cardDemo;

import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * A class modelling the player:
 * -health
 * -deck
 *
 * @author Azhar Hussain <40237295>
 */
public class Player {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final int MAX_MANA = 10;

    private int health;
    private Deck deck;
    private int mana;
    private PlayerPortrait portrait;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public Player(float x, float y, int parameterHealth, int parameterMana, String portraitImageName, GameScreen gameScreen, Boolean isPlayer){
        health = parameterHealth;
        mana = parameterMana;
        deck = new Deck(9,gameScreen);
        this.portrait = new PlayerPortrait(x,y,health,mana,portraitImageName,gameScreen, isPlayer);
    }

    public Player(GameScreen gameScreen) {
        health = 0;
        mana = 0;
        this.portrait = new PlayerPortrait(gameScreen);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public PlayerPortrait getPlayerPortrait() {
        return this.portrait;
    }

    public void reduceHealth(int amount) {
        //Ensure health can not go below 0
        if(this.health - amount  < 0) {
            this.health = 0;
        } else {
            this.health-=amount;
        }
    }

    public boolean isHealthEmpty() {
        return health <= 0;
    }

    public void reduceMana(int amount) {
        //Ensure mana can not go below 0
        if(this.mana-amount  < 0) {
            this.mana = 0;
        } else {
            this.mana-=amount;
        }
    }

    public boolean isManaEmpty() {
        return mana <= 0;
    }

    public void incrementMana(int value) {
        if(this.mana + value < MAX_MANA){
            this.mana+=value;
        } else {
            this.mana = MAX_MANA;
        }
    }

    public void updatePortrait() {
        this.portrait.updateBitmaps(this.health,this.mana);
    }
}
