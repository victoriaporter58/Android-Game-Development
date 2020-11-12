package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;

/**
 * A transparent Game Screen which can overlay the current frames per second
 * that the main game thread is achieving.
 *
 * @Author Robert Hawkes <40232279>
 */

public class FramesPerSecondCounter extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private float framesPerSecond;
    private Paint screenText;
    private Context activityContext = mGame.getActivity();
    private SharedPreferences sharedPreferences;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public FramesPerSecondCounter(Game game, Context context) {
        super("FPSCounterScreen", game);

        screenText = new Paint();
        screenText.setTextSize(50.0f);
        screenText.setTextAlign(Paint.Align.LEFT);
        screenText.setColor(Color.GREEN);
        framesPerSecond = 0;
        sharedPreferences = new SharedPreferences(context);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void update(ElapsedTime elapsedTime) {
        framesPerSecond = mGame.getAverageFramesPerSecond();
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (sharedPreferences.getBooleanValue("fpsCounterOn",false)){
            graphics2D.drawText("FPS: " + String.format("%.2f", framesPerSecond), 50.0f, 50.0f, screenText);
        }
    }
}
