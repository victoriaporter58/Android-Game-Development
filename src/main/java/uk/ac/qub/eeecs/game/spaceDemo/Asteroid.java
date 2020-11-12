package uk.ac.qub.eeecs.game.spaceDemo;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Simple asteroid
 *
 * Note: See the course documentation for extension/refactoring stories
 * for this class.
 *
 * @version 1.0
 */
public class Asteroid extends SpaceEntity {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Default size for the asteroid
     */

    private static final int DEFAULT_RADIUS = 20;
    private static final int MAX_RADIUS = 40;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create an asteroid
     *
     * @param startX     x location of the asteroid
     * @param startY     y location of the asteroid
     * @param gameScreen Gamescreen to which asteroid belongs
     */
    public Asteroid(float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, DEFAULT_RADIUS * 2.0f, DEFAULT_RADIUS * 2.0f, null, gameScreen);

        Random random = new Random();
        this.setHeight(random.nextInt(MAX_RADIUS - DEFAULT_RADIUS) + 20 );
        this.setWidth(this.getHeight());


        // Array List to hold all 4 images of asteroids
        ArrayList<String> asteriod_images = new ArrayList<>();

        // Add all 4 asteroid images to array list
        asteriod_images.add("Asteroid1");
        asteriod_images.add("Asteroid2");
        asteriod_images.add("Asteroid3");
        asteriod_images.add("Asteroid4");

        // Retrieve the size of the array
        int asteroid_list_size = asteriod_images.size();

        mBitmap = gameScreen.getGame().getAssetManager()
                .getBitmap(asteriod_images.get(random.nextInt(asteroid_list_size)));

        mRadius = this.getHeight() / 2;
        mMass = 1000.0f;

        angularVelocity = random.nextFloat() * 240.0f - 20.0f;

    }
}
