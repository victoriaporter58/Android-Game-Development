package uk.ac.qub.eeecs.gage;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

public class TestGame extends Game {

    public Context context;

    public TestGame(int screenWidth, int screenHeight){
        // Store the testing context
        context = InstrumentationRegistry.getTargetContext();

        // Create the file IO service
        mFileIO = new FileIO(context);

        // Create the asset manager
        // Note: Check that the last bit of the AudioManager constructor
        // contains the following code:
        //  // Request control of the volume
        //  if(mGame.getActivity() != null)
        //      mGame.getActivity().setVolumeControlStream(
        //           android.media.AudioManager.STREAM_MUSIC);
        mAssetManager = new AssetManager(this);

        // Create the audio manager
        mAudioManager = new AudioManager(this);

        // Create the screen manager
        mScreenManager = new ScreenManager(this);

        // Store the size of the window we're using
        // Note: If you see an error here remember to update the Game class to
        // change both mScreenWidth and mScreenHeight to protected access from
        // private access.
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;

        // Create a new game loop <-- LOOP NOT CREATED
        // mLoop = new GameLoop();

        // Create the output view and associated renderer <-- RENDERER NOT CREATED
        //mRenderSurface = new CanvasRenderSurface(this, getActivity());
        //View view = mRenderSurface.getAsView();

        // Get our input from the created view <-- INPUT MANAGER NOT CREATED
        //mInput = new Input(getActivity(), view);
    }
}
