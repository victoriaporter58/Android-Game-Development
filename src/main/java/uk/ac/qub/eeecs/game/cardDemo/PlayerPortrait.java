package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Bitmap;

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
* Class encapsulating the Player portrait board component
*
* @author Azhar Hussain <40237295>
**/

public class PlayerPortrait extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    // Define the default card width and height
    public static final int PORTRAIT_WIDTH = 30;//90
    public static final int PORTRAIT_HEIGHT = 40;//120

    private String portraitImageName;
    private Bitmap portraitImage;
    private Bitmap portraitFrame;
    private Bitmap[] digits = new Bitmap[31];

    private int health;
    private int mana;

    private BoundingBox bound = new BoundingBox();

    // Define the offset locations and scaling for the portrait
    // mana and health values - all measured relative
    // to the centre of the object as a percentage of object size
    private Vector2 healthOffset = new Vector2(0.75f, -0.84f);
    private Vector2 healthScale = new Vector2(0.2f, 0.2f);

    private Vector2 manaOffset = new Vector2(-0.75f, -0.84f);
    private Vector2 manaScale = new Vector2(0.2f, 0.2f);

    private Vector2 portraitOffset = new Vector2(0.0f, 0.0f);
    private Vector2 portraitScale = new Vector2(0.60f, 0.60f);

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public PlayerPortrait(float x, float y, int health, int mana, String portraitImageName, GameScreen gameScreen, Boolean isPlayer) {
        super(x,y,PORTRAIT_WIDTH,PORTRAIT_HEIGHT,null,gameScreen);

        AssetManager assetManager = gameScreen.getGame().getAssetManager();

        this.portraitImageName = portraitImageName;
        portraitImage = assetManager.getBitmap(this.portraitImageName);

        if(isPlayer){
            portraitFrame = assetManager.getBitmap("playerPortraitFrame");
        } else {
            portraitFrame = assetManager.getBitmap("enemyPortraitFrame");
        }
        super.setBitmap(portraitFrame);

        // Store each of the mana/health digits
        for (int digit = 0; digit <= 30; digit++)
            digits[digit] = assetManager.getBitmap(String.valueOf(digit));

        this.health = health;
        this.mana = mana;
    }

    public PlayerPortrait(GameScreen gameScreen) {
        super(gameScreen);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    //Ensures bitmaps for health and mana are up to date
    public void updateBitmaps(int health,int mana){
        this.health = health;
        this.mana = mana;
    }

    /**
     * Draw the game platform
     *
     * @param elapsedTime    Elapsed time information
     * @param graphics2D     Graphics instance
     * @param layerViewport  Game layer viewport
     * @param screenViewport Screen viewport
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

            drawBitmap(portraitImage, portraitOffset, portraitScale,graphics2D,layerViewport,screenViewport);

            // Draw the background
            super.draw(elapsedTime,graphics2D,layerViewport,screenViewport);

            // Draw the attack value
            drawBitmap(digits[mana], manaOffset, manaScale, graphics2D, layerViewport, screenViewport);

            // Draw the attack value
            drawBitmap(digits[health], healthOffset, healthScale, graphics2D, layerViewport, screenViewport);
    }

    /**
     * Method to draw out a specified bitmap using a specific offset (relative to the
     * position of this game object) and scaling (relative to the size of this game
     * object).
     *
     * @param bitmap         Bitmap to draw
     * @param offset         Offset vector
     * @param scale          Scaling vector
     * @param graphics2D     Graphics instance
     * @param layerViewport  Game layer viewport
     * @param screenViewport Screen viewport
     */
    private void drawBitmap(Bitmap bitmap, Vector2 offset, Vector2 scale, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Calculate a game layer bound for the bitmap to be drawn
        bound.set(position.x + mBound.halfWidth * offset.x,
                position.y + mBound.halfHeight * offset.y,
                mBound.halfWidth * scale.x,
                mBound.halfHeight * scale.y);

        // Calculate the center position of the rotated offset point.
        double rotation = Math.toRadians(-this.orientation);
        float diffX = mBound.halfWidth * offset.x;
        float diffY = mBound.halfHeight * offset.y;
        float rotatedX = (float) (Math.cos(rotation) * diffX - Math.sin(rotation) * diffY + position.x);
        float rotatedY = (float) (Math.sin(rotation) * diffX + Math.cos(rotation) * diffY + position.y);

        // Calculate a game layer bound for the bitmap to be drawn
        bound.set(rotatedX, rotatedY,
                mBound.halfWidth * scale.x, mBound.halfHeight * scale.y);

        // Draw out the specified bitmap using the calculated bound.
        // The following code is based on the Sprite's draw method.
        if (GraphicsHelper.getSourceAndScreenRect(
                bound, bitmap, layerViewport, screenViewport, drawSourceRect, drawScreenRect)) {

            // Build an appropriate transformation matrix
            drawMatrix.reset();

            float scaleX = (float) drawScreenRect.width() / (float) drawSourceRect.width();
            float scaleY = (float) drawScreenRect.height() / (float) drawSourceRect.height();
            drawMatrix.postScale(scaleX, scaleY);

            drawMatrix.postRotate(orientation, scaleX * bitmap.getWidth()
                    / 2.0f, scaleY * bitmap.getHeight() / 2.0f);

            drawMatrix.postTranslate(drawScreenRect.left, drawScreenRect.top);

            // Draw the bitmap
            graphics2D.drawBitmap(bitmap, drawMatrix, null);
        }
    }
}
