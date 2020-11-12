package uk.ac.qub.eeecs.game.optionsScreen;

import android.content.Context;
import android.graphics.Color;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.ui.ToggleButton;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.GamePerformanceScreen;
import uk.ac.qub.eeecs.game.HowToPlayScreen;
import uk.ac.qub.eeecs.game.SaveTheWorldMainMenu;
import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;
import uk.ac.qub.eeecs.game.TextHandler;
import uk.ac.qub.eeecs.game.TextType;
import uk.ac.qub.eeecs.game.YesNoDialogue;
import uk.ac.qub.eeecs.game.cardDemo.EndGameScreen;
import uk.ac.qub.eeecs.game.cardDemo.SaveTheWorldScreen;

public class OptionsScreen extends GameScreen {
    /**
     * This screen allows the user to adjust settings that have effects across the entire system.
     *
     * @Author Victoria Porter <40232938>
     * @Author Robert Hawkes <40232279>
     * @version 1.0
     */

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    private int screenWidth;
    private int screenHeight;
    private int spacingX;
    private int spacingY;

    private float masterVolume;
    private float sliderMinXcoordinate;
    private float sliderMaxXcoordinate;
    private float sliderBorderXCoordinate;
    private float sliderBorderWidth;
    private float distanceFromMinCoordinate;
    private float distanceFromMinToMaxCoordinate;

    private boolean masterVolumeOn;
    private boolean fpsCounterOn;
    private boolean musicSetUp;
    private boolean musicPlaying;
    private boolean mainGameActive;
    private boolean howToPlayButtonPressed;
    private boolean performanceButtonPressed;
    private boolean mainMenuButtonPressed;
    private boolean resumeGameButtonPressed;
    private boolean fpsToggledOn;
    private boolean muteToggledOn;
    private boolean showExitDialogue;

    private String masterVolumeOnLabel;
    private String musicVolumeLabel;
    private String mainGameActiveLabel;
    private String sliderXcoordinateLabel;
    private String fpsCounterOnLabel;

    private PushButton mainMenuButton;
    private PushButton resumeGameButton;
    private PushButton performanceScreenButton;
    private PushButton howToPlayButton;

    private ToggleButton muteSoundToggle;
    private ToggleButton fpsCounterToggle;

    private GameObject volumeSlider;
    private GameObject sliderBorder;
    private GameObject settingsBackground;

    private YesNoDialogue exitDialogue;

    private SharedPreferences sharedPreferences;

    private LayerViewport mOptionScreenViewport;

    private AudioManager audioManager;

    private AssetManager assetManager;

    private TextHandler textHandler;

    private Context context;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor:
    // /////////////////////////////////////////////////////////////////////////

    public OptionsScreen(Game game, Context context) {
        super("OptionsScreen", game);
        textHandler = new TextHandler(game);
        sharedPreferences = new SharedPreferences(context);
        audioManager = game.getAudioManager();
        assetManager = game.getAssetManager();

        this.context = context;

        setUpExitDialogue(game);
        getSharedPreferencesLabels();
        getScreenDimensions();
        updateSharedPreferencesValues();
        loadAssets();
        setUpViewports();
        setUpButtonsAndGameObjects();
        setToggled(masterVolumeOn, muteSoundToggle);
        setToggled(fpsCounterOn, fpsCounterToggle);
        checkIfMusicIsPlaying();
        setUpMusic();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters:
    // /////////////////////////////////////////////////////////////////////////

    public TextHandler getTextHandler() {
        return textHandler;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public boolean isMasterVolumeOn() {
        return masterVolumeOn;
    }

    public float getMasterVolume() {
        return masterVolume;
    }

    public boolean isFpsCounterOn() {
        return fpsCounterOn;
    }

    public boolean isMainGameActive() {
        return mainGameActive;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public float getSliderMinXcoordinate() {
        return sliderMinXcoordinate;
    }

    public float getSliderMaxXcoordinate() {
        return sliderMaxXcoordinate;
    }

    public GameObject getVolumeSlider() {
        return volumeSlider;
    }

    public int getSpacingX() {
        return spacingX;
    }

    public float getSliderBorderXCoordinate() {
        return sliderBorderXCoordinate;
    }

    public float getSliderBorderWidth() {
        return sliderBorderWidth;
    }

    public void setMainGameActive(boolean mainGameActive) {
        this.mainGameActive = mainGameActive;
    }

    public void setShowExitDialogue(boolean showExitDialogue) { this.showExitDialogue = showExitDialogue; }

    public PushButton getMainMenuButton() {
        return mainMenuButton;
    }

    public PushButton getResumeGameButton() {
        return resumeGameButton;
    }

    public PushButton getPerformanceScreenButton() {
        return performanceScreenButton;
    }

    public PushButton getHowToPlayButton() {
        return howToPlayButton;
    }

    public ToggleButton getMuteSoundToggle() {
        return muteSoundToggle;
    }

    public ToggleButton getFpsCounterToggle() {
        return fpsCounterToggle;
    }

    public GameObject getSliderBorder() {
        return sliderBorder;
    }

    public GameObject getSettingsBackground() {
        return settingsBackground;
    }

    public boolean isMusicPlaying() {
        return musicPlaying;
    }

    public void setMusicPlaying(boolean musicPlaying) {
        this.musicPlaying = musicPlaying;
    }

    public boolean isMusicSetUp() {
        return musicSetUp;
    }

    public void setMasterVolumeOn(boolean masterVolumeOn) {
        this.masterVolumeOn = masterVolumeOn;
    }

    public void setHowToPlayButtonPressed(boolean howToPlayButtonPressed) { this.howToPlayButtonPressed = howToPlayButtonPressed; }

    public void setFpsToggledOn(boolean fpsToggledOn) {
        this.fpsToggledOn = fpsToggledOn;
    }

    public void setMuteToggledOn(boolean muteToggledOn) {
        this.muteToggledOn = muteToggledOn;
    }

    public void setMusicSetUp(boolean musicSetUp) {
        this.musicSetUp = musicSetUp;
    }

    public float getDistanceFromMinCoordinate() {
        return distanceFromMinCoordinate;
    }

    public float getDistanceFromMinToMaxCoordinate() { return distanceFromMinToMaxCoordinate; }

    public int getSpacingY() { return spacingY; }

    public String getMasterVolumeOnLabel() {
        return masterVolumeOnLabel;
    }

    public String getMusicVolumeLabel() {
        return musicVolumeLabel;
    }

    public String getMainGameActiveLabel() {
        return mainGameActiveLabel;
    }

    public String getSliderXcoordinateLabel() {
        return sliderXcoordinateLabel;
    }

    public String getFpsCounterOnLabel() {
        return fpsCounterOnLabel;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = masterVolume;
    }

    // //////////////////////////////////////////////////////////////////////////
    // Methods:
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Set up the exit dialogue shown when the user attempts to exit the main game.
     *
     * @Author Robert Hawkes <40232279>
     * @version 1.0
     */
    public void setUpExitDialogue(Game game){
        showExitDialogue = false;
        exitDialogue = new YesNoDialogue(game, "Warning", "Are you sure to want to quit the", "current game?");
    }

    /**
     * Get the constant string values defined in the shared preferences text file.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void getSharedPreferencesLabels(){
        masterVolumeOnLabel = sharedPreferences.getMasterVolumeOnLabel();
        fpsCounterOnLabel = sharedPreferences.getFpsCounterOnLabel();
        musicVolumeLabel = sharedPreferences.getMusicVolumeLabel();
        mainGameActiveLabel = sharedPreferences.getMainGameActiveLabel();
        sliderXcoordinateLabel = sharedPreferences.getSliderXCoordinateLabel();
    }

    /**
     * Dynamically retrieve the screen dimensions of the device currently running the game.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void getScreenDimensions(){
        screenWidth = mGame.getScreenWidth();
        screenHeight = mGame.getScreenHeight();
    }

    /**
     * Update the members of this class with the values stored in shared preferences.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void updateSharedPreferencesValues(){
        masterVolumeOn = sharedPreferences.getBooleanValue(masterVolumeOnLabel, false);
        fpsCounterOn = sharedPreferences.getBooleanValue(fpsCounterOnLabel, false);
        masterVolume = sharedPreferences.getFloatValue(musicVolumeLabel, 5.0f);
        mainGameActive = sharedPreferences.getBooleanValue(mainGameActiveLabel, false);
    }

    private void loadAssets(){
        assetManager.loadAssets("txt/assets/OptionsScreenAssets.JSON");
    }

    private void setUpViewports() {
        mDefaultScreenViewport.set(0, 0, screenWidth, screenHeight);
        float layerHeight = screenHeight * (480.0f / screenWidth);
        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mOptionScreenViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    /**
     * Instantiate buttons and game objects with dynamic positioning and sizing.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setUpButtonsAndGameObjects() {

        spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        setSliderMinAndMaxXCoordinates(spacingX);
        calculateSliderBorderXCoordinate();
        String resumeIcon = setResumeGameButtonBitmap();

        volumeSlider = new GameObject(sharedPreferences.getFloatValue(sliderXcoordinateLabel, (sliderMinXcoordinate+sliderMaxXcoordinate)/2.0f), spacingY * 1.9f,spacingX * 0.4f,spacingX * 0.4f, assetManager.getBitmap("Slider"), this);

        settingsBackground= new GameObject(mDefaultScreenViewport.centerX(),mDefaultScreenViewport.centerY(),mDefaultScreenViewport.width,mDefaultScreenViewport.height,getGame()
                .getAssetManager().getBitmap("SettingsBackgroundImage"),this);

        performanceScreenButton = new PushButton(
                spacingX * 0.4f, spacingY * 2.4f, spacingX * 0.6f, spacingY * 0.6f,
                "PerformanceIcon", "PerformanceIconSelected", this);

        mainMenuButton = new PushButton(
                spacingX * 0.4f, spacingY * 1.5f, spacingX * 0.6f, spacingY * 0.6f,
                "MenuIcon", "MenuIcon", this);

        resumeGameButton = new PushButton(
                spacingX * 0.4f, spacingY * 0.6f, spacingX * 0.6f, spacingY * 0.6f,
                resumeIcon, resumeIcon, this);

        howToPlayButton = new PushButton(
                spacingX * 4.4f, spacingY * 0.6f, spacingX * 0.6f, spacingY * 0.6f,
                "HowToPlayScreenIcon", "HowToPlayScreenIcon", this);

        calculateSliderBorderWidth();
        sliderBorder= new GameObject(sliderBorderXCoordinate,spacingY * 1.9f, sliderBorderWidth,spacingX * 0.4f,getGame()
                .getAssetManager().getBitmap("SliderBorder"),this);

        muteSoundToggle = new ToggleButton(
                sliderMinXcoordinate, spacingY * 1.25f, spacingX * 0.4f, spacingY * 0.4f,
                "SoundOff", "SoundOn", this);

        fpsCounterToggle = new ToggleButton(
                sliderMinXcoordinate, spacingY * 0.6f, spacingX * 0.4f, spacingY * 0.4f,
                "ToggleOff", "ToggleOn", this);
    }

    /**
     * Set the toggle button state to match the value defined in shared preferences.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setToggled(boolean toggledOn, ToggleButton toggleButton){
        if(toggledOn) {
            toggleButton.setToggled(true);
        }
        else {
            toggleButton.setToggled(false);
        }
    }

    /**
     * Check if the audio manager is playing music thus implying that the music track has already been set up.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void checkIfMusicIsPlaying(){
        if(audioManager.isMusicPlaying()){
            musicPlaying = true;
        }
        else{
            musicPlaying = false;
        }
    }

    /**
     * Check if the audio manager has already been set up with a music file. If the requirements are correct,
     * resume the music / set up audio manager with track and play it.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setUpMusic(){
        musicSetUp = false;
        if(musicPlaying) musicSetUp = true;
        if(!musicPlaying && mainGameActive && masterVolumeOn) startMusic();
    }

    /**
     * Set up audio manager with track and play it.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void startMusic(){
        audioManager.playMusic(assetManager.getMusic("MainGameBackgroundMusic"));
        musicSetUp = true;
    }

    /**
     * Dynamically calculated the minimum and maximum x positions of the slider based on the current device.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setSliderMinAndMaxXCoordinates(int spacingX){
        sliderMaxXcoordinate = spacingX * 4.7f;
        sliderMinXcoordinate = spacingX * 2.3f;
    }

    /**
     * The leftmost edge of the bitmap should be exactly equal to the minimum x coordinate that the slider can have.
     * To achieve this, we add half of the total distance between the minimum x and maximum x onto the minimum x. This is
     * because the x coordinate is assigned to the centre of the border bitmap, thus we work with the centre value rather than
     * the leftmost value.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void calculateSliderBorderXCoordinate(){
        sliderBorderXCoordinate = sliderMinXcoordinate + (sliderMaxXcoordinate - sliderMinXcoordinate)/2.0f;
    }

    /**
     * This method uses shared preferences to set the bitmap of the resume game icon to show the user when they can
     * and cannot return to the main game.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public String setResumeGameButtonBitmap(){
        if(mainGameActive){
            return "ResumeIcon";
        }else{
            return "ResumeIconDisabled";
        }
    }

    /**
     * Dynamically calculate the slider border width for the bitmap.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void calculateSliderBorderWidth(){
        sliderBorderWidth = sliderMaxXcoordinate - sliderMinXcoordinate + volumeSlider.getWidth();
    }

    /**
     * Check if a specific push button has been triggered. This method makes testing outcomes resulting from
     * push events easier as it assigns the relevant boolean value to the relevant member of this class.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public boolean isButtonPushTriggered(PushButton pushButton){
        if(pushButton.isPushTriggered()) return true;
        return false;
    }

    /**
     * Check if a specific toggle button has been triggered. This method makes testing outcomes resulting from
     * push events easier as it assigns the relevant boolean value to the relevant member of this class.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public boolean isButtonToggledOn(ToggleButton toggleButton){
        if(toggleButton.isToggledOn()) return true;
        return false;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        exitDialogue.update(elapsedTime);
        muteSoundToggle.update(elapsedTime);
        fpsCounterToggle.update(elapsedTime);
        mainMenuButton.update(elapsedTime);
        volumeSlider.update(elapsedTime);
        performanceScreenButton.update(elapsedTime);
        resumeGameButton.update(elapsedTime);
        howToPlayButton.update(elapsedTime);

        howToPlayButtonPressed = isButtonPushTriggered(howToPlayButton);
        performanceButtonPressed = isButtonPushTriggered(performanceScreenButton);
        mainMenuButtonPressed = isButtonPushTriggered(mainMenuButton);
        resumeGameButtonPressed = isButtonPushTriggered(resumeGameButton);
        fpsToggledOn = isButtonToggledOn(fpsCounterToggle);
        muteToggledOn = isButtonToggledOn(muteSoundToggle);

        updateExitDialogue();
        checkIfMusicIsPlaying();
        saveFpsToggleState();
        saveMuteToggleState();
        updateVolumeSlider(touchEvents);
        backToMainMenu();
        launchPerformanceScreen();
        backToMainGame();
        launchHowToPlayScreen();
    }

    /**
     * Update exit dialogue - check if screen transition is necessary
     *
     * @Author Robert Hawkes <40232279>
     * @version 1.0
     */
    private void updateExitDialogue() {
        if(showExitDialogue) {
            int exitDialogueResponse = exitDialogue.getResponse();

            switch(exitDialogueResponse) {
                case YesNoDialogue.YES_BUTTON_PRESSED:
                    SaveTheWorldScreen cardScreen = (SaveTheWorldScreen)mGame.getScreenManager().getScreen("CardScreen");
                    int playerWithHighestHealth = cardScreen.getPlayerWithHighestHealth();

                    boolean playerWouldWin;
                    if(playerWithHighestHealth == 1) {
                        playerWouldWin = true;
                    }
                    else {
                        playerWouldWin = false;
                    }

                    mGame.getScreenManager().removeScreen(this);
                    mGame.getScreenManager().removeScreen(cardScreen);
                    sharedPreferences.setBooleanValue("mainGameActive", false);
                    mGame.getScreenManager().addScreen(new EndGameScreen("EndGameScreen",this.getGame(),playerWouldWin));
                    stopMusic();
                    break;
                case YesNoDialogue.NO_BUTTON_PRESSED:
                    showExitDialogue = false;
                    break;
            }
        }
    }

    /**
     * Check if the FPS Counter Toggle Button has been pushed. If pushed, set the Shared Preferences to true to reflect that
     * If toggled off, set the Shared Preferences to false.
     *
     * @Author Robert Hawkes <40232279>
     *
     * @version 1.0
     */
    public void saveFpsToggleState(){
        if(fpsToggledOn) {
            sharedPreferences.setBooleanValue(fpsCounterOnLabel, true);
        }
        else {
            sharedPreferences.setBooleanValue(fpsCounterOnLabel, false);
        }
    }

    /**
     * Check if the mute toggle button has been triggered. Update shared preferences and adjust music accordingly.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void saveMuteToggleState(){
        updateSharedPreferencesValues();
        if(muteToggledOn && mainGameActive){
            sharedPreferences.setBooleanValue(masterVolumeOnLabel, true);
            resumeMusic();
        }
        else{
            sharedPreferences.setBooleanValue(masterVolumeOnLabel, false);
            pauseMusic();
        }
    }

    /**
     * This method decides whether music can be resumed or, if music hasn't been set up, then it sets up the file and plays it.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void resumeMusic(){
        if(!musicPlaying && mainGameActive){
            if(musicSetUp){
                audioManager.resumeMusic();
            }else{
                startMusic();
            }
        }
        calculateMusicVolume();
    }

    /**
     * Pause music and stop SFX sounds.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void pauseMusic(){
        if (musicPlaying && mainGameActive) {
            audioManager.pauseMusic();
            audioManager.setSfxVolume(0.0f);
        }
    }

    /**
     * Use the touch events to update the volume slider where necessary.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void updateVolumeSlider(List<TouchEvent> touchEvents){
        Vector2 layerPosition = new Vector2();

        for (TouchEvent touchEvent : touchEvents) {
            ViewportHelper.convertScreenPosIntoLayer(mDefaultScreenViewport, touchEvent.x,
                    touchEvent.y, mDefaultLayerViewport, layerPosition);

            updateSliderPosition(layerPosition.x, layerPosition.y);
        }
    }

    /**
     * Check if the touch event is within the bounds of the slider border and update the slider's position accordingly.
     * The user can either drag the slider to a specific location, or they can tap anywhere within the bounds and the slider
     * will jump to that location. The master volume is updated accordingly.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void updateSliderPosition(float layerPosX, float layerPosY){
        if (sliderBorder.getBound().contains(layerPosX, layerPosY)) {
            if(layerPosX <= sliderMaxXcoordinate && layerPosX >= sliderMinXcoordinate){
                volumeSlider.position.x = layerPosX;
            }
            calculateMusicVolume();
        }
    }

    /**
     * Calculate the volume that the music should be set to based on the position of the volume slider.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void calculateMusicVolume(){
        distanceFromMinCoordinate = volumeSlider.position.x - sliderMinXcoordinate;
        distanceFromMinToMaxCoordinate = sliderMaxXcoordinate - sliderMinXcoordinate;
        masterVolume = (distanceFromMinCoordinate/distanceFromMinToMaxCoordinate);

        updateMuteToggleState();
        saveSliderXCoordinate();
    }

    /**
     * Update the toggle state of the Mute button based on the master volume. This method triggers when the volume
     * slider is located within a small range (0.01) of its lowest position. It will cause the mute toggle to switch states
     * based on the location of the volume slider.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void updateMuteToggleState(){
        if(masterVolume < 0.01){
            muteSoundToggle.setToggled(false);
            setMusicVolume(0.0f);
        }else{
            muteSoundToggle.setToggled(true);
            setMusicVolume(masterVolume);
        }
    }

    /**
     * Save the slider x coordinate to shared preferences so that it can be correctly loaded in the same position
     * if the screen is closed and re-opened.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void saveSliderXCoordinate(){
        sharedPreferences.setFloatValue(sliderXcoordinateLabel, volumeSlider.position.x);
    }

    /**
     * Calculate the volume that the music should be set to based on the position of the volume slider.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setMusicVolume(float volume){
        audioManager.setSfxVolume(volume);
        audioManager.setMusicVolume(volume);
        sharedPreferences.setFloatValue(musicVolumeLabel, masterVolume);
    }

    /**
     * Check if the main menu icon has been triggered and perform transition to main menu screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void backToMainMenu() {
        if (mainMenuButtonPressed) {
            if(mainGameActive) {
                setShowExitDialogue(true);
            } else {
                stopMusic();
                mGame.getScreenManager().addScreen(new SaveTheWorldMainMenu(mGame, context));
            }
        }
    }

    /**
     * Check if the how to play icon has been triggered and perform transition to how to play screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void launchHowToPlayScreen(){
        if (howToPlayButtonPressed) mGame.getScreenManager().addScreen(new HowToPlayScreen(mGame));
    }

    /**
     * Check if the performance icon has been triggered and perform transition to performance screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void launchPerformanceScreen() {
        if (performanceButtonPressed) {
            stopMusic();
            mGame.getScreenManager().addScreen(new GamePerformanceScreen(mGame, context));
        }
    }

    public void stopMusic(){
        audioManager.stopMusic();
    }

    /**
     * Check if main game is active and the resume game icon has been triggered and perform transition to main game screen.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void backToMainGame() {
        if (mainGameActive) {
            if(resumeGameButtonPressed) mGame.getScreenManager().removeScreen(this);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        settingsBackground.draw(elapsedTime,graphics2D);

        textHandler.drawText(TextType.TITLE,"Options", screenWidth/2.5f, screenHeight/7.0f, Color.WHITE, graphics2D);
        textHandler.drawText(TextType.SUBTITLE,"Volume: ", screenWidth / 6.0f, screenHeight / 2.5f, Color.WHITE, graphics2D);
        textHandler.drawText(TextType.SUBTITLE,"Mute: ", screenWidth / 6.0f, screenHeight / 1.65f, Color.WHITE, graphics2D);
        textHandler.drawText(TextType.SUBTITLE,"FPS Counter: ", screenWidth / 6.0f, screenHeight / 1.2f, Color.WHITE, graphics2D);

        sliderBorder.draw(elapsedTime,graphics2D, mOptionScreenViewport, mDefaultScreenViewport);
        volumeSlider.draw(elapsedTime, graphics2D, mOptionScreenViewport, mDefaultScreenViewport);
        mainMenuButton.draw(elapsedTime, graphics2D, mOptionScreenViewport, mDefaultScreenViewport);
        performanceScreenButton.draw(elapsedTime, graphics2D, mOptionScreenViewport, mDefaultScreenViewport);
        resumeGameButton.draw(elapsedTime, graphics2D, mOptionScreenViewport, mDefaultScreenViewport);
        muteSoundToggle.draw(elapsedTime, graphics2D, mOptionScreenViewport, mDefaultScreenViewport);
        fpsCounterToggle.draw(elapsedTime, graphics2D, mOptionScreenViewport, mDefaultScreenViewport);
        howToPlayButton.draw(elapsedTime, graphics2D, mOptionScreenViewport, mDefaultScreenViewport);
        if(showExitDialogue) { exitDialogue.draw(elapsedTime, graphics2D); }
    }
}