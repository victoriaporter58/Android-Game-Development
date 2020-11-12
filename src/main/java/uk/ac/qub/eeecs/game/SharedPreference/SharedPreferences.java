package uk.ac.qub.eeecs.game.SharedPreference;

import android.content.Context;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferences {
    /**
     * This class handles getting and setting shared preferences across the system. It stores
     * the values in a text file called "SharedPreferencesValues.txt".
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    private android.content.SharedPreferences sharedPreferences;

    private android.content.SharedPreferences.Editor sharedPreferencesEditor;

    private static final String SHARED_PREFERENCES_STORAGE_FILE_NAME = "SharedPreferencesValues.txt";
    private static final String MASTER_VOLUME_ON_LABEL = "masterVolumeOn";
    private static final String FPS_COUNTER_ON_LABEL = "fpsCounterOn";
    private static final String MAIN_GAME_ACTIVE_LABEL = "mainGameActive";
    private static final String MUSIC_VOLUME_LABEL = "musicVolume";
    private static final String SLIDER_X_COORDINATE_LABEL = "sliderXcoordinate";

    // /////////////////////////////////////////////////////////////////////////
    // Constructor:
    // /////////////////////////////////////////////////////////////////////////

    public SharedPreferences(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_STORAGE_FILE_NAME, MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters:
    // /////////////////////////////////////////////////////////////////////////

    public android.content.SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public android.content.SharedPreferences.Editor getSharedPreferencesEditor() { return sharedPreferencesEditor; }

    public String getSharedPreferencesStorageFileName() {
        return SHARED_PREFERENCES_STORAGE_FILE_NAME;
    }

    public static String getMasterVolumeOnLabel() {
        return MASTER_VOLUME_ON_LABEL;
    }

    public static String getMainGameActiveLabel() {
        return MAIN_GAME_ACTIVE_LABEL;
    }

    public static String getMusicVolumeLabel() {
        return MUSIC_VOLUME_LABEL;
    }

    public static String getSliderXCoordinateLabel() {
        return SLIDER_X_COORDINATE_LABEL;
    }

    public static String getFpsCounterOnLabel() {
        return FPS_COUNTER_ON_LABEL;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods:
    // /////////////////////////////////////////////////////////////////////////

    /**
     * This method saves a given boolean value to the shared preferences text file.
     *
     * Argument 1: sharedPreferencesString - the name of the variable in the text file that will be
     * used to store the given value.
     *
     * Argument 2: value - the value to be stored (in this case true or false.)
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setBooleanValue(String sharedPreferencesLabel, boolean value){
        sharedPreferencesEditor.putBoolean(sharedPreferencesLabel, value);
        sharedPreferencesEditor.commit();
    }

    /**
     * This method returns a given boolean value from the shared preferences text file.
     *
     * Argument 1: sharedPreferencesString - the name of the variable in the text file.
     *
     * Argument 2: defaultValue - the default value that will be returned in the event that the
     * actual value is null (in this case true or false.)
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public boolean getBooleanValue(String sharedPreferencesLabel, boolean defaultValue){
        return sharedPreferences.getBoolean(sharedPreferencesLabel, defaultValue);
    }

    /**
     * This method saves a given float value to the shared preferences text file.
     *
     * Argument 1: sharedPreferencesString - the name of the variable in the text file that will be
     * used to store the given value.
     *
     * Argument 2: value - the value to be stored.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public void setFloatValue(String sharedPreferencesLabel, float value){
        sharedPreferencesEditor.putFloat(sharedPreferencesLabel, value);
        sharedPreferencesEditor.commit();
    }

    /**
     * This method returns a given float value from the shared preferences text file.
     *
     * Argument 1: sharedPreferencesString - the name of the variable in the text file.
     *
     * Argument 2: defaultValue - the default value that will be returned in the event that the
     * actual value is null.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */
    public float getFloatValue(String sharedPreferencesLabel, float defaultValue){
        return sharedPreferences.getFloat(sharedPreferencesLabel, defaultValue);
    }
}