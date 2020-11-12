package uk.ac.qub.eeecs.game;

import android.graphics.Paint;
import android.graphics.Typeface;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;

public class TextHandler {
    /**
     * This class handles the styling (including font styling, colour and positioning) of titles, subtitles and body text across the system.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    private Paint paintObj;

    private float titleFontSize;
    private float subtitleFontSize;
    private float bodyFontSize;
    private float currentFontSize;

    private int currentColour;

    private Typeface currentTypeface;

    private final Typeface titleTypeface = Typeface.DEFAULT_BOLD;
    private final Typeface subtitleTypeface = Typeface.SERIF;
    private final Typeface bodyTypeface = Typeface.DEFAULT;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor:
    // /////////////////////////////////////////////////////////////////////////

    public TextHandler(Game game) {
        this.paintObj = new Paint();

        // Dynamically set the font sizes according to the text type
        this.titleFontSize = game.getScreenWidth()/15.0f;
        this.subtitleFontSize = game.getScreenWidth()/25.0f;
        this.bodyFontSize = game.getScreenWidth()/30.0f;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters:
    // /////////////////////////////////////////////////////////////////////////

    public int getCurrentColour() {
        return currentColour;
    }

    public void setCurrentColour(int currentColour) {
        this.currentColour = currentColour;
    }

    public void setPaintObj(Paint paintObj) {
        this.paintObj = paintObj;
    }

    public float getTitleFontSize() {
        return titleFontSize;
    }

    public float getSubtitleFontSize() {
        return subtitleFontSize;
    }

    public float getBodyFontSize() {
        return bodyFontSize;
    }

    public float getCurrentFontSize() {
        return currentFontSize;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods:
    // /////////////////////////////////////////////////////////////////////////

    /**
     * This method draws text to the screen and styles it according to its text type (title, subtitle, body)
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void drawText(TextType textType, String text, float x, float y, int colour, IGraphics2D graphics2D) {
        setFontStyling(textType);
        setTextColour(colour);

        graphics2D.drawText(text, x, y, this.paintObj);
    }

    /**
     * This method applies the relevant font styling to text based on its text type (title, subtitle, body)
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setFontStyling(TextType textType){
        switch(textType){
            case TITLE:
                currentFontSize = titleFontSize;
                currentTypeface = titleTypeface;
                break;
            case SUBTITLE:
                currentFontSize = subtitleFontSize;
                currentTypeface = subtitleTypeface;
                break;
            case BODY:
                currentFontSize = bodyFontSize;
                currentTypeface = bodyTypeface;
                break;
            default:
                break;
        }
        stylePaintObject();
    }

    public void stylePaintObject(){
        this.paintObj.setTextSize(currentFontSize);
        this.paintObj.setTypeface(currentTypeface);
    }

    /**
     * This method sets the colour of the text
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setTextColour(int colour){
        currentColour = colour;

        setPaintObjectColour();
    }

    public void setPaintObjectColour(){
        this.paintObj.setColor(currentColour);
    }
}