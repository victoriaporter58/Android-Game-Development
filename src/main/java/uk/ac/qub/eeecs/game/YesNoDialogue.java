package uk.ac.qub.eeecs.game;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;

public class YesNoDialogue extends Dialogue {

    /**
     * An extension of Dialogue which presents the user with Yes No options
     *
     * @Author Robert Hawkes <40232279>
     */

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public static final int NO_TOUCH_ON_BUTTONS = 0;
    public static final int YES_BUTTON_PRESSED = 1;
    public static final int NO_BUTTON_PRESSED = 2;

    private PushButton yesButton;
    private float yesButtonWidth = 300.0f;
    private float yesButtonHeight = 200.0f;

    private PushButton noButton;
    private float noButtonWidth = 300.0f;
    private float noButtonHeight = 200.0f;

    private int response;
    private boolean yesButtonTouched;
    private boolean noButtonTouched;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param game The game in which this overlay will run
     * @param title The title of the dialogue
     * @param messageLine1 The first line of the message
     * @param messageLine2 The second line of the message
     */
    public YesNoDialogue(Game game, String title, String messageLine1, String messageLine2) {
        super(game, title, messageLine1, messageLine2);

        game.getAssetManager().loadAssets("txt/assets/YesNoDialogueAssets.JSON");

        yesButton = new PushButton(screenWidth/3.25f, screenHeight/1.5f, yesButtonWidth, yesButtonHeight, "YesButton", this);

        noButton = new PushButton(screenWidth/1.5f, screenHeight/1.5f, noButtonWidth, noButtonHeight, "NoButton", this);
        response = NO_TOUCH_ON_BUTTONS;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters:
    // /////////////////////////////////////////////////////////////////////////

    public PushButton getYesButton() {
        return yesButton;
    }

    public PushButton getNoButton() {
        return noButton;
    }

    public boolean isYesButtonTouched() {
        return yesButtonTouched;
    }

    public void setYesButtonTouched(boolean yesButtonTouched) {
        this.yesButtonTouched = yesButtonTouched;
    }

    public boolean isNoButtonTouched() {
        return noButtonTouched;
    }

    public void setNoButtonTouched(boolean noButtonTouched) {
        this.noButtonTouched = noButtonTouched;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void update(ElapsedTime elapsedTime) {
        super.update(elapsedTime);

        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        processTouch(touchEvents);
        checkResponse();

    }

    /**
     * This method processes what the response code should be from the screen
     *
     * @Author Robert Hawkes <40232279>
     */
    public void checkResponse() {
        if(yesButtonTouched) {
            //Yes button has been pressed, change the response
            response = YES_BUTTON_PRESSED;
        } else if(noButtonTouched) {
            //No button has been pressed, change the response
            response = NO_BUTTON_PRESSED;
        } else {
            //No touch
            response = NO_TOUCH_ON_BUTTONS;
        }
    }

    /**
     * This method checks if either button has been pressed and stores the return to be processed later
     * @param touchEvents The touch events for the screen
     *
     * @Author Robert Hawkes <40232279>
     */
    private void processTouch(List<TouchEvent> touchEvents) {
        yesButtonTouched = checkIfButtonClicked(yesButton,touchEvents);
        noButtonTouched = checkIfButtonClicked(noButton,touchEvents);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        super.draw(elapsedTime, graphics2D);
        yesButton.draw(elapsedTime, graphics2D);
        noButton.draw(elapsedTime, graphics2D);
    }

    /**
     * An override from the abstract method
     * @return Returns the response code
     * @Author Robert Hawkes <40232279>
     */
    @Override
    public int getResponse() {
        return response;
    }

    /**
     * A method which determines if any of the touch events are within the buttons bounds
     * @param button The button on the screen
     * @param touchEvents The touch events for the screen
     * @return True, a touch occurred on the button. False, no touch occurred on the button.
     */
    private boolean checkIfButtonClicked(PushButton button, List<TouchEvent> touchEvents) {

        //Get the bound of the GameObject
        BoundingBox buttonBound = button.getBound();

        //Get the width and height of the GameObject
        float buttonWidth = button.getWidth();
        float buttonHeight = button.getHeight();

        //Get each touch event in the list
        for(TouchEvent eachTouchEvent : touchEvents) {

            //if touch event is on the game object
            if(eachTouchEvent.x < buttonBound.x + buttonWidth/2) {
                if(eachTouchEvent.x > buttonBound.x - buttonWidth/2) {
                    if (eachTouchEvent.y < buttonBound.y + buttonHeight) {
                        if (eachTouchEvent.y > buttonBound.y - buttonHeight) {

                            //The press is on the button!
                            //Return true, don't need to continue processing
                            return true;
                        }
                    }
                }
            }
        }

        //We've tested every touch event
        //and none of it was in the bounds of the button
        //We return false
        return false;
    }
}
