package uk.ac.qub.eeecs.gage;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class TestGameScreen extends GameScreen {
    public TestGameScreen(Game game) {
        super("TestGameScreen", game);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {}

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {}
}
