package uk.ac.qub.eeecs.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.qub.eeecs.gage.Game;

/**
 * Sample demo game that is create within the MainActivity class
 *
 * @version 1.0
 */
public class DemoGame extends Game {

    /**
     * Create a new demo game
     */
    public DemoGame() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.ac.qub.eeecs.gage.Game#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Go with a default 20 UPS/FPS
        //setTargetFramesPerSecond(20);

        //Setting target frames per second to 30 (A reasonable target for phones)
        setTargetFramesPerSecond(60);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Call the Game's onCreateView to get the view to be returned.
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Create and add a stub game screen to the screen manager. We don't
        // want to do this within the onCreate method as the menu screen
        // will layout the buttons based on the size of the view.
        //MenuScreen stubMenuScreen = new MenuScreen(this);
        //mScreenManager.addScreen(stubMenuScreen);

        addSplashScreen();

        return view;
    }

    public void addSplashScreen(){
        // Load splash screen initially
        SplashScreen splash = new SplashScreen(this, this.getActivity().getApplicationContext());
        mScreenManager.addScreen(splash);
    }

    // Blaine's new main menu screen
    public void addMainMenu(){
        // Load menu screen
        //game.getScreenManager().removeScreen(this.getName());
        SaveTheWorldMainMenu menu = new SaveTheWorldMainMenu(this, this.getActivity().getApplicationContext());
        mScreenManager.addScreen(menu);
    }

//    @Override
//    public boolean onBackPressed() {
//        // If we are already at the menu screen then exit
//        if (mScreenManager.getCurrentScreen().getName().equals("MenuScreen"))
//            return false;
//
//        // Stop any playing music
//        if(mAudioManager.isMusicPlaying())
//            mAudioManager.stopMusic();
//
//        // Go back to the menu screen
//        getScreenManager().removeAllScreens();
//        MenuScreen menuScreen = new MenuScreen(this, false);
//        getScreenManager().addScreen(menuScreen);
//        return true;
//    }

    // New back press for blaine's new main menu
    @Override
    public boolean onBackPressed() {
        // If we are already at the menu screen then exit
        if (mScreenManager.getCurrentScreen().getName().equals("SaveTheWorldMainMenu"))
            return false;

        // Stop any playing music
        if(mAudioManager.isMusicPlaying())
            mAudioManager.stopMusic();

        // Go back to the menu screen
        getScreenManager().removeAllScreens();
        SaveTheWorldMainMenu menu = new SaveTheWorldMainMenu(this, this.getActivity().getApplicationContext());
        getScreenManager().addScreen(menu);
        return true;
    }
}