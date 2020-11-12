package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.widget.Switch;

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
 * Card class that can be drawn using a number of overlapping images.
 * <p>
 * Note: See the course documentation for extension/refactoring stories
 * for this class.
 *
 * @version 1.0
 */
public class Card extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    // Define the default card width and height
    public static final int CARD_WIDTH = Board.COMPONENTSIZE /2;//90
    public static final int CARD_HEIGHT = (int)(Board.COMPONENTSIZE/1.5);//120

    // Define the common card base
    private Bitmap mCardBase;
    private Bitmap mCardBack;
    private Bitmap mAbilityImage;
    private Bitmap mCardPortrait;
    private Bitmap mCardGrey;
    private Bitmap mTextBitmap;
    private Bitmap mDescriptionBitmap;
    private Bitmap[] mCardDigits = new Bitmap[10];


    // Define the offset locations and scaling for the card portrait
    // card attack and card health values - all measured relative
    // to the centre of the object as a percentage of object size
    private Vector2 mAbilityOffset = new Vector2(0.77f, 0.84f);
    private Vector2 mAbilityScale = new Vector2(0.2f, 0.2f);

    private Vector2 mAttackOffset = new Vector2(-0.68f, -0.82f);
    private Vector2 mAttackScale = new Vector2(0.2f, 0.2f);

    private Vector2 mCostOffset = new Vector2(-0.77f, 0.84f);
    private Vector2 mCostScale = new Vector2(0.2f, 0.2f);

    private Vector2 mHealthOffset = new Vector2(0.72f, -0.82f);
    private Vector2 mHealthScale = new Vector2(0.2f, 0.2f);

    private Vector2 mPortraitOffset = new Vector2(0.0f, 0.3f);
    private Vector2 mPortraitScale = new Vector2(0.55f, 0.55f);

    private Vector2 mNameOffset = new Vector2(-0.0f, -0.5f);
    private Vector2 mNameScale = new Vector2(2f, 2f);

    private Vector2 mTextOffset = new Vector2(0.0f, -.9f);
    private Vector2 mTextScale = new Vector2(1.7f, 1.7f);


    // Define the health and attack values
    private int mAttack;
    private int mHealth;

    private boolean mFaceUp;
    private boolean mMovable;

    // Define the energy cost
    private int mCost;

    private String mCardName;
    private String mCardText;
    private String mCardPortraitString;
    private int[] mOriginalValues;
    private boolean mInHand;

    private AbilityType mAbility;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new platform.
     *
     * @param x          Centre y location of the platform
     * @param y          Centre x location of the platform
     * @param gameScreen Gamescreen to which this platform belongs
     */
    public Card(float x, float y, GameScreen gameScreen, int health, int attack, int cost, String cardName, String cardText, String portraitImage,AbilityType abilityType) {
        super(x, y, CARD_WIDTH, CARD_HEIGHT, null, gameScreen);

        AssetManager assetManager = gameScreen.getGame().getAssetManager();

        // Store the common card base image
        mCardBase = assetManager.getBitmap("CardBackground");
        mCardBack = assetManager.getBitmap("CardBack");
        mCardGrey = assetManager.getBitmap("CardGrey");

        mCardPortraitString = portraitImage;
        // Store the card portrait image
        mCardPortrait = assetManager.getBitmap(portraitImage);

        // Store each of the damage/health digits
        for (int digit = 0; digit <= 9; digit++)
            mCardDigits[digit] = assetManager.getBitmap(String.valueOf(digit));
        mFaceUp = false;
        mMovable = false;
        mInHand = false;
        // Set attack and health values
        mAttack = attack;
        mHealth = health;
        // Set cost
        mCost = cost;
        //store original values
        mOriginalValues = new int[3];
        mOriginalValues[0] = mAttack;
        mOriginalValues[1] = mHealth;
        mOriginalValues[2] = mCost;
        //set name and text
        mCardName = cardName;
        mCardText = cardText;
        mAbility = abilityType;

        switch (mAbility){
            case RUSH: mAbilityImage = assetManager.getBitmap("Rush");
            break;
            case MANA: mAbilityImage = assetManager.getBitmap("Mana");
            break;
            case DEFEND: mAbilityImage = assetManager.getBitmap("Defend");
            break;
            default:mAbilityImage = null;
        }


        //null check added for testing purposes
        if (mCardBase != null) {
            //create bitmap of card name
            mTextBitmap = Bitmap.createBitmap(mCardBase.getWidth(), 400, mCardBase.getConfig());
            Canvas canvas = new Canvas(mTextBitmap);
            TextPaint paint = new TextPaint();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setFakeBoldText(true);
            paint.setTextAlign(Paint.Align.CENTER);
            //paint.setTypeface(Typeface.MONOSPACE);
            paint.setTextSize(canvas.getWidth() / 25.f);
            canvas.drawText(getCardName(), mCardBase.getWidth() / 2, 200, paint);


            //create bitmap of card description
            mDescriptionBitmap = Bitmap.createBitmap(mCardBase.getWidth(), 450, mCardBase.getConfig());
            Canvas canvas2 = new Canvas(mDescriptionBitmap);
            paint.setTextSize(canvas2.getWidth() / 25.f);
            canvas2.drawText(getCardText(), mCardBase.getWidth() / 2, 200, paint);

        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters/setters for Testing
    // /////////////////////////////////////////////////////////////////////////
    public int getAttack() {
        return mAttack;
    }

    public int getHealth() {
        return mHealth;
    }

    public int getCost() {
        return mCost;
    }

    public boolean getFaceUp() {
        return mFaceUp;
    }

    public String getCardName() {
        return mCardName;
    }

    public String getCardText() {
        return mCardText;
    }

    public String getCardPortraitString() {
        return mCardPortraitString;
    }

    public GameScreen getGameScreen() {
        return mGameScreen;
    }



    public void setMovable(boolean mMovable) {
        this.mMovable = mMovable;
    }

    public boolean isMovable() {
        return mMovable;
    }

    public boolean isInHand() {
        return mInHand;
    }

    public void setInHand(boolean mInHand) {
        this.mInHand = mInHand;
    }

    public void setFaceUp(boolean mfaceUp) {
        this.mFaceUp = mfaceUp;
    }

    public AbilityType getAbility() {
        return mAbility;
    }


    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////
    /**
     * a method to flip the card over
     * Created by &Justin johnston <40237507>
     *
     * @version 1.0
     */
    public void flipCard() {
        if (mFaceUp) {
            mFaceUp = false;
        } else {
            mFaceUp = true;
        }
    }
    /**
     * a method that reduces the cards health
     * down to a minimum of 0
     * Created by &Justin johnston <40237507>
     *
     * @version 1.0
     */
    public void damageCard(int damage) {
        if((mHealth  - damage) <= 0){
            mHealth = 0;
            mFaceUp = false;
        }else{
            mHealth = mHealth - damage;
        }
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
        if (mFaceUp) {
            // Draw the portrait
            drawBitmap(mCardPortrait, mPortraitOffset, mPortraitScale,
                    graphics2D, layerViewport, screenViewport);

            // Draw the card base background
            if(mMovable){
                mBitmap = mCardBase;
                super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
            }else{
                mBitmap = mCardGrey;
                super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
            }


            // Draw the attack value
            drawBitmap(mCardDigits[mAttack], mAttackOffset, mAttackScale,
                    graphics2D, layerViewport, screenViewport);

            // Draw the attack value
            drawBitmap(mCardDigits[mHealth], mHealthOffset, mHealthScale,
                    graphics2D, layerViewport, screenViewport);

            // Draw the cost value
            drawBitmap(mCardDigits[mCost], mCostOffset, mCostScale,
                    graphics2D, layerViewport, screenViewport);

            // Draw the ability image
            if(mAbilityImage != null){
                drawBitmap(mAbilityImage, mAbilityOffset, mAbilityScale,
                        graphics2D, layerViewport, screenViewport);
            }

            // Draw the name
            drawBitmap(mTextBitmap, mNameOffset, mNameScale, graphics2D, layerViewport, screenViewport);

            // Draw the description
            drawBitmap(mDescriptionBitmap, mTextOffset, mTextScale, graphics2D, layerViewport, screenViewport);


        } else {

            // Draw the card base background
            mBitmap = mCardBack;
            super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        }

    }

    private BoundingBox bound = new BoundingBox();

    public void resetCard() {
        mAttack = mOriginalValues[0];
        mHealth = mOriginalValues[1];
        mCost = mOriginalValues[2];
        mFaceUp = false;
        mMovable = false;
        mInHand = false;
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
