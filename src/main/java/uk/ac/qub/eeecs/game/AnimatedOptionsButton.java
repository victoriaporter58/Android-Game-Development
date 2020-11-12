package uk.ac.qub.eeecs.game;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.animation.AnimationManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * A Class to create an options button that is animated
 *
 * @Author Blaine McKeever <40237118>
 * @Version 1.0
 */

public class AnimatedOptionsButton extends PushButton {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private AnimationManager mAnimationManager;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public AnimatedOptionsButton (
            float startX, float startY, float width, float height, String defaultBitmap, String pushBitmap, GameScreen gameScreen) {
        super(startX, startY, width, height, defaultBitmap, pushBitmap, gameScreen);

        mAnimationManager = new AnimationManager(this);
        mAnimationManager.addAnimation("txt/animation/ButtonAnimations.JSON");
        mAnimationManager.setCurrentAnimation("OptionsButtonPulse");

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public void updateAnimation(ElapsedTime elapsedTime) {

        mAnimationManager.play(elapsedTime);
        mAnimationManager.update(elapsedTime);
    }

    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        mAnimationManager.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
    }

}