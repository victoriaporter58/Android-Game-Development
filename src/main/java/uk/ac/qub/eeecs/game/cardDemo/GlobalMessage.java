package uk.ac.qub.eeecs.game.cardDemo;

import android.graphics.Paint;
import android.graphics.Typeface;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

import static java.lang.StrictMath.abs;


public class GlobalMessage  {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    private String mMessage;
    private String mSubMessage;
    private Paint mMessagePaint = new Paint();
    private Paint mMessageOutlinePaint = new Paint();
    private Paint mSubMessagePaint = new Paint();
    private Paint mSubMessageOutlinePaint = new Paint();
    private double mTimeMessageCreated;
    private Boolean mNewMessageCreated;
    private Boolean mFirstRun;
    private static double MESSAGEDURATION = 1.6;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * system designed to display text feedback to the user
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public GlobalMessage(){
        mMessage = "";
        mSubMessage = "";
        mTimeMessageCreated = 0.0;
        mNewMessageCreated = false;
        mFirstRun = true;
    }


    // /////////////////////////////////////////////////////////////////////////
    // Getters // Setters
    // /////////////////////////////////////////////////////////////////////////

    public String getMessage() {
        return mMessage;
    }

    public void setSubMessage(String mSubMessage) {
        this.mSubMessage = mSubMessage;
    }

    public String getSubMessage() {
        return mSubMessage;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////
    /**
     * adds a message to the screen
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void addMessage(String message){
        mMessage = message;
        mNewMessageCreated = true;

    }

    /**
     * sets up the paint style for the text to be displayed
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private void setupPaintStyles(LayerViewport layerViewport, ScreenViewport screenViewport) {
        mMessagePaint.setTextSize(layerViewport.getHeight()*0.8f);
        mMessagePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mMessagePaint.setARGB(255,255,255,255);

        mMessageOutlinePaint.setTextSize(layerViewport.getHeight()*0.8f);
        mMessageOutlinePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mMessageOutlinePaint.setStyle(Paint.Style.STROKE);
        mMessageOutlinePaint.setARGB(255,0,0,0);
        mMessageOutlinePaint.setStrokeWidth(25);
        mMessageOutlinePaint.setStrokeCap(Paint.Cap.SQUARE);

        mSubMessagePaint.setTextSize(layerViewport.getHeight()*0.3f);
        mSubMessagePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mSubMessagePaint.setARGB(200,255,100,100);

        mSubMessageOutlinePaint.setTextSize(layerViewport.getHeight()*0.3f);
        mSubMessageOutlinePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mSubMessageOutlinePaint.setStyle(Paint.Style.STROKE);
        mSubMessageOutlinePaint.setARGB(255,0,0,0);
        mSubMessageOutlinePaint.setStrokeWidth(20);
        mSubMessageOutlinePaint.setStrokeCap(Paint.Cap.SQUARE);
    }

    /**
     * checks if a message has been displayed for long enough
     * removes the message if the correct amount of time has passed
     * Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void update(ElapsedTime elapsedTime){
        if((abs(elapsedTime.totalTime - mTimeMessageCreated))>MESSAGEDURATION){
            mMessage = "";
            mSubMessage = "";
            mTimeMessageCreated = elapsedTime.totalTime;
        }

        if(mNewMessageCreated){
            mTimeMessageCreated = elapsedTime.totalTime;
            mNewMessageCreated = false;
        }
    }

    /**
     * draws the text to the screen, sets up paint style if first run of the class
     * Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {
        if(mFirstRun){
            setupPaintStyles( layerViewport, screenViewport);
            mFirstRun = false;
        }
        graphics2D.drawText(mMessage, layerViewport.getWidth()/2, layerViewport.getHeight()*1.4f,  mMessageOutlinePaint);
        graphics2D.drawText(mMessage, layerViewport.getWidth()/2, layerViewport.getHeight()*1.4f,  mMessagePaint);
        graphics2D.drawText(mSubMessage, layerViewport.getWidth()/2, layerViewport.getHeight()*2.0f,  mSubMessageOutlinePaint);
        graphics2D.drawText(mSubMessage, layerViewport.getWidth()/2, layerViewport.getHeight()*2.0f,  mSubMessagePaint);
    }

}
