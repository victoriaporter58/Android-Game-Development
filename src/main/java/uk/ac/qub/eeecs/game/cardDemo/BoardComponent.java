package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Board Components are placed on the screen in a grid to form the board
 *  Created by &Justin johnston <40237507>
 * @version 1.0
 */
public class BoardComponent extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    // Define the common card base
    protected Bitmap mBoardComponentBackground;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create an image for use with the board system
     *Created by &Justin johnston <40237507>
     * @param x          Centre y location of the platform
     * @param y          Centre x location of the platform
     * @param gameScreen Gamescreen to which this platform belongs
     */
    public BoardComponent(int x,int y, GameScreen gameScreen,int componentWidth,int componentHeight, String boardComponentBackground) {
        super(x, y, componentWidth, componentHeight, null, gameScreen);
        AssetManager assetManager = gameScreen.getGame().getAssetManager();
        mBoardComponentBackground = assetManager.getBitmap(boardComponentBackground);
        super.setBitmap(mBoardComponentBackground);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters for Testing
    // /////////////////////////////////////////////////////////////////////////

    public GameScreen getGameScreen() {
        return mGameScreen;
    }


    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Draw method for an image used the board system
     *Created by &Justin johnston <40237507>
     * @param elapsedTime    Elapsed time information
     * @param graphics2D     Graphics instance
     * @param layerViewport  Game layer viewport
     * @param screenViewport Screen viewport
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {
        // Draw the portrait
        //drawBitmap(mBoardComponentBackground,mBoardComponentOffset,mBoardComponentScale,graphics2D, layerViewport, screenViewport);
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

    }

    private BoundingBox bound = new BoundingBox();

}
