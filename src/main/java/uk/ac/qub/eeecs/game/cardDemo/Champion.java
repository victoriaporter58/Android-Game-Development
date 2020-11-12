package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Bitmap;

/**
 * A Class used to create champions for the game
 *
 * @Author Blaine McKeever <40237118>
 * @Version 1.0
 */

public class Champion {

    private String mChampionName;
    private String mDeckJsonLocation;
    private int mHealth;
    private int mMana;
    private Bitmap mPortrait;

    public Champion(String championName, String deckJsonLocation, int health, int mana, Bitmap portrait) {

        mChampionName = championName;
        mDeckJsonLocation = deckJsonLocation;
        mHealth = health;
        mMana = mana;
        mPortrait = portrait;
    }

    public void setChampionName(String championName) { this.mChampionName = championName; }

    public String getChampionName() { return mChampionName; }

    public void setDeckJsonLocation(String deckJsonLocation) { this.mDeckJsonLocation = deckJsonLocation; }

    public String getDeckJsonLocation() { return mDeckJsonLocation; }

    public void setHealth(int health) { this.mHealth = health; }

    public int getHealth() { return mHealth; }

    public void setPortrait(Bitmap portrait) { this.mPortrait = portrait; }

    public Bitmap getPortrait() { return mPortrait; }

    public void setMana(int mana) { this.mMana = mana; }

    public int getMana() { return mMana; }
}