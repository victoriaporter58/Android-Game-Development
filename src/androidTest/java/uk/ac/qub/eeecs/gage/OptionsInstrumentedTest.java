package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.ui.ToggleButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;
import uk.ac.qub.eeecs.game.TextHandler;
import uk.ac.qub.eeecs.game.optionsScreen.OptionsScreen;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OptionsInstrumentedTest {
    /**
     * Options screen testing approach.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */

    private Context context;
    private DemoGame game;
    private OptionsScreen optionsScreen;
    private AudioManager audioManager;
    private AssetManager assetManager;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        setupGame();
    }

    private void setupGame() {
        game = new DemoGame();
        game.mFileIO = new FileIO(context);

        assetManager = new AssetManager(game);
        audioManager = new AudioManager(game);
        game.mAssetManager = assetManager;
        game.mAudioManager = audioManager;
        game.mScreenManager = new ScreenManager(game);

        optionsScreen = new OptionsScreen(game, context);
    }

    @Test
    public void testObjectCreation_Constructor_Test(){
        TextHandler textHandler = optionsScreen.getTextHandler();
        SharedPreferences sharedPreferences = optionsScreen.getSharedPreferences();
        AudioManager audioManager = optionsScreen.getAudioManager();
        AssetManager assetManager = optionsScreen.getAssetManager();

        assertNotNull(textHandler);
        assertNotNull(sharedPreferences);
        assertNotNull(audioManager);
        assertNotNull(assetManager);
    }

    @Test
    public void testVariableAssignment_Constructor_Test(){
        int screenHeight = optionsScreen.getScreenHeight();
        int screenWidth = optionsScreen.getScreenWidth();

        boolean masterVolumeOn = optionsScreen.isMasterVolumeOn();
        boolean fpsCounterOn = optionsScreen.isFpsCounterOn();
        boolean mainGameActive = optionsScreen.isMainGameActive();

        float masterVolume = optionsScreen.getMasterVolume();
        float sliderMinXcoordinate = optionsScreen.getSliderMinXcoordinate();
        float sliderMaxXcoordinate = optionsScreen.getSliderMaxXcoordinate();
        float volumeSliderHeight = optionsScreen.getVolumeSlider().getHeight();
        float volumeSliderWidth = optionsScreen.getVolumeSlider().getWidth();
        float sliderBorderWidth = optionsScreen.getSliderBorderWidth();
        float sliderBorderXcoordinate = optionsScreen.getSliderBorderXCoordinate();

        String masterVolumeOnLabel = optionsScreen.getMasterVolumeOnLabel();
        String fpsCounterOnLabel = optionsScreen.getFpsCounterOnLabel();
        String mainGameActiveLabel = optionsScreen.getMainGameActiveLabel();
        String sliderXcoordinateLabel = optionsScreen.getSliderXcoordinateLabel();
        String musicVolumeLabel = optionsScreen.getMusicVolumeLabel();

        SharedPreferences shp = new SharedPreferences(context);
        assertEquals(masterVolumeOnLabel, shp.getMasterVolumeOnLabel());
        assertEquals(fpsCounterOnLabel, shp.getFpsCounterOnLabel());
        assertEquals(mainGameActiveLabel, shp.getMainGameActiveLabel());
        assertEquals(sliderXcoordinateLabel, shp.getSliderXCoordinateLabel());
        assertEquals(musicVolumeLabel, shp.getMusicVolumeLabel());

        // Compare the values returned above with the actual values that we know each variable should have been set to in the class
        assertEquals(screenHeight, game.getScreenHeight(), 0f);
        assertEquals(screenWidth, game.getScreenWidth(), 0f);

        assertEquals(masterVolumeOn, optionsScreen.getSharedPreferences().getBooleanValue("masterVolumeOn", false));
        assertEquals(fpsCounterOn, optionsScreen.getSharedPreferences().getBooleanValue("fpsCounterOn", false));
        assertEquals(mainGameActive, optionsScreen.getSharedPreferences().getBooleanValue("mainGameActive", false));
        assertEquals(masterVolume, optionsScreen.getSharedPreferences().getFloatValue("musicVolume", 5.0f),0.0f);

        assertEquals(sliderMinXcoordinate, optionsScreen.getSpacingX()*2.3f,0.0f);
        assertEquals(sliderMaxXcoordinate, optionsScreen.getSpacingX()*4.7f, 0.0f);
        assertEquals(volumeSliderHeight, optionsScreen.getSpacingX()*0.4f,0.0f);
        assertEquals(volumeSliderWidth, optionsScreen.getSpacingX()*0.4f,0.0f);
        assertEquals(sliderBorderWidth, optionsScreen.getSliderMaxXcoordinate() - optionsScreen.getSliderMinXcoordinate() + volumeSliderWidth, 0.0f);
        assertEquals(sliderBorderXcoordinate, optionsScreen.getSliderMinXcoordinate() + (optionsScreen.getSliderMaxXcoordinate() - optionsScreen.getSliderMinXcoordinate())/2.0f, 0.0f);
    }

    @Test
    public void testObjectCreation_ButtonsAndGameObjects(){
        GameObject volumeSlider = optionsScreen.getVolumeSlider();
        GameObject sliderBorder = optionsScreen.getSliderBorder();
        GameObject background = optionsScreen.getSettingsBackground();

        PushButton performanceButton = optionsScreen.getPerformanceScreenButton();
        PushButton mainMenuButton = optionsScreen.getMainMenuButton();
        PushButton resumeGameButton = optionsScreen.getResumeGameButton();
        PushButton howToPlayButton = optionsScreen.getHowToPlayButton();

        ToggleButton fpsToggle = optionsScreen.getFpsCounterToggle();
        ToggleButton muteToggle = optionsScreen.getMuteSoundToggle();

        assertNotNull(volumeSlider);
        assertNotNull(sliderBorder);
        assertNotNull(background);

        assertNotNull(performanceButton);
        assertNotNull(mainMenuButton);
        assertNotNull(resumeGameButton);
        assertNotNull(howToPlayButton);

        assertNotNull(fpsToggle);
        assertNotNull(muteToggle);
    }

    @Test
    public void testBitmapAssignment_MainGameNotActive(){
        optionsScreen.setMainGameActive(false);

        String resumeIcon = optionsScreen.setResumeGameButtonBitmap();

        assertEquals(resumeIcon, "ResumeIconDisabled");
    }

    @Test
    public void testBitmapAssignment_MainGameIsActive(){
        optionsScreen.setMainGameActive(true);

        String resumeIcon = optionsScreen.setResumeGameButtonBitmap();

        assertEquals(resumeIcon, "ResumeIcon");
    }

    @Test
    public void testSettingToggleState_SetToFalse(){
        boolean isToggled = false;
        ToggleButton toggleButton = new ToggleButton(100, 100, 50, 50, "ToggleOff", "ToggleOn", optionsScreen);

        optionsScreen.setToggled(isToggled, toggleButton);

        assertEquals(toggleButton.isToggledOn(), false);
    }

    @Test
    public void testSettingToggleState_SetToTrue(){
        boolean isToggled = true;
        ToggleButton toggleButton = new ToggleButton(100, 100, 50, 50, "ToggleOff", "ToggleOn", optionsScreen);

        optionsScreen.setToggled(isToggled, toggleButton);

        assertEquals(toggleButton.isToggledOn(), true);
    }

    @Test
    public void testReturningToggleState_ToggledOff(){
        ToggleButton toggleButton = new ToggleButton(100, 100, 50, 50, "ToggleOff", "ToggleOn", optionsScreen);

        optionsScreen.setToggled(false, toggleButton);

        boolean isButtonToggledOn = optionsScreen.isButtonToggledOn(toggleButton);

        assertEquals(isButtonToggledOn, false);
    }

    @Test
    public void testReturningToggleState_ToggledOn(){
        ToggleButton toggleButton = new ToggleButton(100, 100, 50, 50, "ToggleOff", "ToggleOn", optionsScreen);

        optionsScreen.setToggled(true, toggleButton);

        boolean isButtonToggledOn = optionsScreen.isButtonToggledOn(toggleButton);

        assertEquals(isButtonToggledOn, true);
    }



    @Test
    public void testMusicPlaying_NotPlayingInitially(){
        audioManager.stopMusic();
        optionsScreen.checkIfMusicIsPlaying();

        assertEquals(optionsScreen.isMusicPlaying(), false);
    }

    @Test
    public void testMusicPlaying_IsPlayingInitially(){
        if(!audioManager.isMusicPlaying()) audioManager.resumeMusic();

        optionsScreen.checkIfMusicIsPlaying();

        assertEquals(optionsScreen.isMusicPlaying(), true);
    }

    @Test
    public void testMusicSetUp_IsSetUpInitially(){
        optionsScreen.setMusicPlaying(true);

        optionsScreen.setUpMusic();

        assertEquals(optionsScreen.isMusicSetUp(), true);
        assertEquals(audioManager.isMusicPlaying(), true);
    }

    @Test
    public void testMusicSetUp_NotSetUpInitially_VolumeOn_MainGameNotActive(){
        audioManager.stopMusic();
        optionsScreen.setMusicPlaying(false);
        optionsScreen.setMasterVolumeOn(true);
        optionsScreen.setMainGameActive(false);

        optionsScreen.setUpMusic();

        assertEquals(optionsScreen.isMusicSetUp(), false);
        assertEquals(audioManager.isMusicPlaying(), false);
    }

    @Test
    public void testMusicSetUp_NotSetUpInitially_VolumeOn_MainGameIsActive(){
        audioManager.stopMusic();
        optionsScreen.setMusicPlaying(false);
        optionsScreen.setMasterVolumeOn(true);
        optionsScreen.setMainGameActive(true);

        optionsScreen.setUpMusic();

        assertEquals(optionsScreen.isMusicSetUp(), true);
        assertEquals(audioManager.isMusicPlaying(), true);
    }

    @Test
    public void testMusicSetUp_NotSetUpInitially_VolumeOff_MainGameIsActive(){
        audioManager.stopMusic();
        optionsScreen.setMusicPlaying(false);
        optionsScreen.setMasterVolumeOn(false);
        optionsScreen.setMainGameActive(true);

        optionsScreen.setUpMusic();

        assertEquals(optionsScreen.isMusicSetUp(), false);
        assertEquals(audioManager.isMusicPlaying(), false);
    }

    @Test
    public void testLaunchHowToPlayScreen(){
        optionsScreen.setHowToPlayButtonPressed(true);

        optionsScreen.launchHowToPlayScreen();

        assertEquals(game.getScreenManager().getCurrentScreen().getName(), "HowToPlayScreen");
    }

    @Test
    public void testUpdateFPSToggleState_ToggledOn(){
        optionsScreen.setFpsToggledOn(true);

        optionsScreen.saveFpsToggleState();

        assertEquals(optionsScreen.getSharedPreferences().getBooleanValue("fpsCounterOn", false), true);
    }

    @Test
    public void testUpdateFPSToggleState_ToggledOff(){
        optionsScreen.setFpsToggledOn(false);

        optionsScreen.saveFpsToggleState();

        assertEquals(optionsScreen.getSharedPreferences().getBooleanValue("fpsCounterOn", true), false);
    }

    @Test
    public void testUpdateMuteToggleState_ToggledOn(){
        if(optionsScreen.isMusicPlaying()) optionsScreen.getAudioManager().pauseMusic();
        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), false);

        optionsScreen.setMainGameActive(true);
        optionsScreen.getSharedPreferences().setBooleanValue(optionsScreen.getSharedPreferences().getMainGameActiveLabel(), true);
        optionsScreen.setMuteToggledOn(true);
        optionsScreen.saveMuteToggleState();

        assertEquals(optionsScreen.getSharedPreferences().getBooleanValue("masterVolumeOn", false), true);
        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), true);
    }

    @Test
    public void testUpdateMuteToggleState_ToggledOff(){
        optionsScreen.setMainGameActive(true);
        optionsScreen.getSharedPreferences().setBooleanValue(optionsScreen.getSharedPreferences().getMainGameActiveLabel(), true);
        if(!optionsScreen.isMusicPlaying()) optionsScreen.resumeMusic();
        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), true);


        optionsScreen.setMusicPlaying(true);
        optionsScreen.setMuteToggledOn(false);
        optionsScreen.saveMuteToggleState();

        assertEquals(optionsScreen.getSharedPreferences().getBooleanValue("masterVolumeOn", true), false);
        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), false);
    }

    @Test
    public void testPauseMusic(){
        if(!optionsScreen.isMusicPlaying()) optionsScreen.resumeMusic();
        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), true);

        optionsScreen.setMainGameActive(true);
        optionsScreen.setMusicPlaying(true);

        optionsScreen.pauseMusic();

        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), false);
    }

    @Test
    public void testResumeMusic_MusicIsSetUp(){
        if(optionsScreen.getAudioManager().isMusicPlaying()) optionsScreen.getAudioManager().pauseMusic();
        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), false);

        optionsScreen.setMusicSetUp(true);
        optionsScreen.resumeMusic();

        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), true);
    }

    @Test
    public void testResumeMusic_MusicNotSetUp(){
        if(optionsScreen.getAudioManager().isMusicPlaying()) optionsScreen.getAudioManager().stopMusic();
        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), false);

        optionsScreen.setMusicSetUp(false);
        optionsScreen.resumeMusic();

        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), true);
    }

    @Test
    public void testCalculateMusicVolume_ValidSliderPosition(){
        // Set location of slider to be directly in the middle of the minimum x coordinate and the maximum x coordinate
        optionsScreen.getVolumeSlider().position.x = (optionsScreen.getSliderMaxXcoordinate() - optionsScreen.getSliderMinXcoordinate())/2.0f;

        optionsScreen.calculateMusicVolume();

        assertEquals(optionsScreen.getMasterVolume(), (optionsScreen.getDistanceFromMinCoordinate()/optionsScreen.getDistanceFromMinToMaxCoordinate()), 0.0f);
        assertEquals(optionsScreen.getVolumeSlider().position.x, optionsScreen.getSharedPreferences().getFloatValue("sliderXcoordinate", 5.0f), 0.0f);
    }

    @Test
    public void testCalculateMusicVolume_LowestSliderPosition(){
        optionsScreen.getVolumeSlider().position.x = optionsScreen.getSliderMinXcoordinate();
        optionsScreen.calculateMusicVolume();

        assertEquals(optionsScreen.getSharedPreferences().getFloatValue("musicVolume", 5.0f), 0.0f, 0f);
        assertEquals(optionsScreen.getMuteSoundToggle().isToggledOn(), false);
        assertEquals(optionsScreen.getVolumeSlider().position.x, optionsScreen.getSharedPreferences().getFloatValue("sliderXcoordinate", 5.0f), 0.0f);
    }

    @Test
    public void testCalculateMusicVolume_HighestSliderPosition(){
        optionsScreen.getVolumeSlider().position.x = optionsScreen.getSliderMaxXcoordinate();
        optionsScreen.calculateMusicVolume();

        assertEquals(optionsScreen.getMuteSoundToggle().isToggledOn(), true);
        assertEquals(optionsScreen.getMasterVolume(), (optionsScreen.getDistanceFromMinCoordinate()/optionsScreen.getDistanceFromMinToMaxCoordinate()), 0.0f);
        assertEquals(optionsScreen.getVolumeSlider().position.x, optionsScreen.getSharedPreferences().getFloatValue("sliderXcoordinate", 5.0f), 0.0f);
    }

    @Test
    public void testUpdateSliderPosition_ValidLocation(){
        // We know that the y location of the slider is fixed at spacingY*1.9f, thus we use this value
       optionsScreen.updateSliderPosition(optionsScreen.getSliderMinXcoordinate() + 100,optionsScreen.getSpacingY() * 1.9f);

       assertEquals(optionsScreen.getVolumeSlider().position.x,optionsScreen.getSliderMinXcoordinate() + 100,0.0f);
    }

    @Test
    public void testUpdateSliderPosition_InvalidLocation(){
        // We know that the y location of the slider is fixed at spacingY*1.9f, thus we use this value
        optionsScreen.updateSliderPosition(optionsScreen.getSliderMinXcoordinate() - 100,optionsScreen.getSpacingY() * 1.9f);

        assertNotEquals(optionsScreen.getVolumeSlider().position.x,optionsScreen.getSliderMinXcoordinate() - 100,0.0f);
        assertEquals(optionsScreen.getVolumeSlider().position.x, optionsScreen.getSharedPreferences().getFloatValue("sliderXcoordinate", 5.0f), 0.0f);
    }

    @Test
    public void testUpdateSliderPosition_LowestLocation(){
        // We know that the y location of the slider is fixed at spacingY*1.9f, thus we use this value
        optionsScreen.updateSliderPosition(optionsScreen.getSliderMinXcoordinate(),optionsScreen.getSpacingY() * 1.9f);

        assertEquals(optionsScreen.getVolumeSlider().position.x,optionsScreen.getSliderMinXcoordinate(),0.0f);
    }

    @Test
    public void testUpdateSliderPosition_HighestLocation(){
        // We know that the y location of the slider is fixed at spacingY*1.9f, thus we use this value
        optionsScreen.updateSliderPosition(optionsScreen.getSliderMaxXcoordinate(),optionsScreen.getSpacingY() * 1.9f);

        assertEquals(optionsScreen.getVolumeSlider().position.x,optionsScreen.getSliderMaxXcoordinate(),0.0f);
    }

    @Test
    public void testStartMusic(){
        if(optionsScreen.isMusicPlaying()) optionsScreen.getAudioManager().stopMusic();

        optionsScreen.startMusic();

        assertEquals(optionsScreen.getAudioManager().isMusicPlaying(), true);
        assertEquals(optionsScreen.isMusicSetUp(), true);
    }

    @Test
    public void testUpdatingMuteToggleState_MasterVolumeOff(){
        optionsScreen.setMasterVolume(0.0f);

        optionsScreen.updateMuteToggleState();

        assertEquals(optionsScreen.getMuteSoundToggle().isToggledOn(), false);
        assertEquals(optionsScreen.getAudioManager().getMusicVolume(), 0.0f,0.0f);
    }

    @Test
    public void testUpdatingMuteToggleState_MasterVolumeOn(){
        optionsScreen.setMasterVolume(5.0f);

        optionsScreen.updateMuteToggleState();

        assertEquals(optionsScreen.getMuteSoundToggle().isToggledOn(), true);
        assertEquals(optionsScreen.getSharedPreferences().getFloatValue("musicVolume", 0.0f), 5.0f,0.0f);
    }
}