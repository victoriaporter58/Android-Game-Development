package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.SaveTheWorldMainMenu;
import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;

/**
* A class for the "end game" screen
* @author Azhar Hussain <40237295>
**/

public class EndGameScreen extends GameScreen{


    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public boolean playerWon;
    private ArrayList<PushButton> pushButtons;
    private PushButton mainMenuButton;
    private PushButton playAgainButton;
    public Sprite resultMessage;
    private SharedPreferences sharedPreferences;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////


    public EndGameScreen(String name, Game game, Boolean playerWon) {
        super(name, game);
        //sharedPreferencesManager.setBooleanValue("mainGameActive", false);
        this.playerWon = playerWon;
        mGame.getAssetManager().loadAssets("txt/assets/EndGameScreenAssets.JSON");
        setupViewports();
        setupButtons();
        setupBitmaps();
    }


    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set( 0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserve the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight/2.0f, 240.0f, layerHeight/2.0f);
    }

    private void setupButtons() {
        pushButtons = new ArrayList<>();
        // Define the spacing that will be used to position the buttons
        int spacingX = (int)(mDefaultLayerViewport.getWidth() / 3);
        int spacingY = (int)mDefaultLayerViewport.getHeight() / 5;

        int width = (int)(mDefaultLayerViewport.getWidth() * 0.10);
        int height = (int)(mDefaultLayerViewport.getHeight() *  0.10);
        //Create the buttons
        mainMenuButton = new PushButton(spacingX* 0.50f, spacingY*1.5f, width,height,
                "mainMenuButton","mainMenuButtonSelected",this);
        mainMenuButton.setPlaySounds(true, true);
        playAgainButton = new PushButton(spacingX* 2.50f, spacingY*1.5f, width,height,
                "playAgainButton","playAgainButtonSelected",this);
        playAgainButton.setPlaySounds(true, true);
        pushButtons.add(mainMenuButton);
        pushButtons.add(playAgainButton);
    }


    private void setupBitmaps() {

        float xPosition = mDefaultLayerViewport.halfWidth;
        float yPosition = mDefaultLayerViewport.halfHeight;
        float width = (float) (mDefaultLayerViewport.getWidth() * 0.50);
        float height = (float)(mDefaultLayerViewport.getHeight() *  0.50);

        if(playerWon){
            this.resultMessage = new Sprite(xPosition,yPosition,
                    mGame.getAssetManager().getBitmap("Victory"), this);
        } else {
            this.resultMessage = new Sprite(xPosition,yPosition,
                    mGame.getAssetManager().getBitmap("Defeat"), this);
        }
        resultMessage.setWidth(width);
        resultMessage.setHeight(height);
    }

    private void touchEvents(ElapsedTime elapsedTime, ArrayList<PushButton> pushButtons) {
        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            //Update each button
            for(PushButton button : pushButtons){
                button.update(elapsedTime);
            }

            if (mainMenuButton.isPushTriggered()) {
                goToMainMenu();
            } else if (playAgainButton.isPushTriggered()) {
                playAgain();
            }
        }
    }

    private void playAgain() {
        mGame.getScreenManager().addScreen(new CharacterSelectScreen(mGame));
    }

    private void goToMainMenu() {
        mGame.getScreenManager().addScreen(new SaveTheWorldMainMenu(mGame, getGame().getActivity().getApplicationContext()));
    }

    /**
     * Update the menu screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        touchEvents(elapsedTime, pushButtons);
    }

    /**
     * Draw the EndGameScreen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Clear the screen and draw the buttons
        graphics2D.clear(Color.WHITE);

        resultMessage.draw(elapsedTime,graphics2D,mDefaultLayerViewport,mDefaultScreenViewport);

        //Draw each button
        for(PushButton button : pushButtons){
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        }

    }
}
