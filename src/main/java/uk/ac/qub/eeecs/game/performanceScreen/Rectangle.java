package uk.ac.qub.eeecs.game.performanceScreen;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

public class Rectangle extends GameObject {

    private BoundingBox tileBound = new BoundingBox();

    protected int mTileXCount = 1;
    protected int mTileYCount = 1;

    public Rectangle(float x, float y, float width, float height,
                    String bitmapName, GameScreen gameScreen) {
        super(x, y, width, height, gameScreen.getGame().getAssetManager()
                .getBitmap(bitmapName), gameScreen);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Call the getBound method to make sure we're using an up-to-date bound
        BoundingBox bound = getBound();

        // Only draw if it is visible
        if (GraphicsHelper.isVisible(bound, layerViewport)) {

            // Define the tile size
            tileBound.halfWidth = bound.halfWidth / mTileXCount;
            float tileWidth = 2.0f * tileBound.halfWidth;
            tileBound.halfHeight = bound.halfHeight / mTileYCount;
            float tileHeight = 2.0f * tileBound.halfHeight;

            // Store the bottom left corner of the platform
            float rectangleLeft = bound.getLeft();
            float rectangleBottom = bound.getBottom();

            // Consider drawing each tile
            for (int tileXIdx = 0; tileXIdx < mTileXCount; tileXIdx++)
                for (int tileYIdx = 0; tileYIdx < mTileYCount; tileYIdx++) {

                    // Build a layer bound for the tile
                    tileBound.x = rectangleLeft + (tileXIdx + 0.5f) * tileWidth;
                    tileBound.y = rectangleBottom + (tileYIdx + 0.5f) * tileHeight;

                    // If the layer tile is visible then draw tne tile
                    if (GraphicsHelper.getClippedSourceAndScreenRect(
                            tileBound, mBitmap, layerViewport, screenViewport, drawSourceRect, drawScreenRect)) {
                        graphics2D
                                .drawBitmap(mBitmap, drawSourceRect, drawScreenRect, null);
                    }
                }
        }
    }


}
