package uk.ac.qub.eeecs.game.cardDemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class DeckManager {
    /**
     * Initialised list of cards created which will be filled and returned to the deck class
     * also initialised the File IO and Game classes
     */
    private List<Card> mDeckCards;
    private FileIO mFileIO;
    private Game mGame;
    private GameScreen mGameScreen;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new deckManager
     * this opens a file IO for use with a JSON file, which will be used to create a deck
     * adapted from the AssetManager Class
     * Created by &Justin johnston <40237507>
     * @see uk.ac.qub.eeecs.gage.engine.AssetManager
     * @param game       Game instance to which this manager belongs
     * @param gameScreen GameScreen instance to which this manager belongs
     */
    public DeckManager(Game game, GameScreen gameScreen) {
        mGame = game;
        mGameScreen = gameScreen;
        mFileIO = mGame.getFileIO();
    }

    /**
     * this creates a deck from a JSON File and returns a List of cards to the Deck Class
     * Created by &Justin johnston <40237507>
     */
    public List<Card> loadDeckFromJSON(String assetsToLoadJSONFile) {
        // Attempt to load in the JSON asset details
        String loadedJSON;
        try {
            loadedJSON = mFileIO.loadJSON(assetsToLoadJSONFile);
        } catch (IOException e) {
            throw new RuntimeException(
                    "DeckManager.constructor: Cannot load JSON [" + assetsToLoadJSONFile + "]");
        }

        // Attempt to extract the JSON information
        try {
            JSONObject settings = new JSONObject(loadedJSON);
            JSONArray assets = settings.getJSONArray("cards");
            mDeckCards = new ArrayList<>(assets.length());

            // Load in each asset
            for (int idx = 0; idx < assets.length(); idx++) {
                String cardName = assets.getJSONObject(idx).getString("cardName");
                String cardText = assets.getJSONObject(idx).getString("cardText");
                String cardCost = assets.getJSONObject(idx).getString("cost");
                String cardAttack = assets.getJSONObject(idx).getString("attack");
                String cardHealth = assets.getJSONObject(idx).getString("health");
                String cardPortrait = assets.getJSONObject(idx).getString("portrait");
                String cardAmount = assets.getJSONObject(idx).getString("amount");
                String cardAbility = assets.getJSONObject(idx).getString("ability");
                for(int i = 0;i<Integer.parseInt(cardAmount);i++){
                    Card card = new Card(0, 0, mGameScreen, Integer.parseInt(cardHealth), Integer.parseInt(cardAttack), Integer.parseInt(cardCost), cardName, cardText, cardPortrait,getAbilityType(cardAbility));

                    mDeckCards.add(card);
                }

            }

        } catch (JSONException | IllegalArgumentException e) {
            throw new RuntimeException(
                    "DeckManager.constructor: JSON parsing error [" + e.getMessage() + "]");
        }
        return mDeckCards;
    }

    /**
     * converts a string from the JSON file to its respective Enum
     * Created by &Justin johnston <40237507>
     */
    public AbilityType getAbilityType(String cardAbility){
        AbilityType ability = AbilityType.NONE;
        if(cardAbility.equals("Rush")){
           ability = AbilityType.RUSH;
        }else if( cardAbility.equals("Mana")){
            ability = AbilityType.MANA;
        }else if(cardAbility.equals("Defend")){
            ability = AbilityType.DEFEND;
        }
        return ability;
    }
}
