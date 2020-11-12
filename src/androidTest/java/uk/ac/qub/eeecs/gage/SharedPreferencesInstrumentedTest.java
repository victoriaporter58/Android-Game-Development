package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import uk.ac.qub.eeecs.game.SharedPreference.SharedPreferences;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SharedPreferencesInstrumentedTest {
    /**
     * Shared Preferences testing approach.
     *
     * @Author Victoria Porter <40232938>
     * @version 1.0
     */

    private Context context;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        sharedPreferences = new SharedPreferences(context);
    }

    @Test
    public void testObjectCreation_Constructor_Test() {
        assertNotNull(sharedPreferences.getSharedPreferences());
        assertNotNull(sharedPreferences.getSharedPreferencesEditor());
    }

    @Test
    public void testConstantVariableAssignment_Constructor_Test(){
        assertEquals(sharedPreferences.getSharedPreferencesStorageFileName(), "SharedPreferencesValues.txt");
        assertEquals(sharedPreferences.getMainGameActiveLabel(), "mainGameActive");
        assertEquals(sharedPreferences.getFpsCounterOnLabel(), "fpsCounterOn");
        assertEquals(sharedPreferences.getMasterVolumeOnLabel(), "masterVolumeOn");
        assertEquals(sharedPreferences.getMusicVolumeLabel(), "musicVolume");
        assertEquals(sharedPreferences.getSliderXCoordinateLabel(), "sliderXcoordinate");
    }

    @Test
    public void testSettingAndGettingBooleanValue_SetTrue(){
        sharedPreferences.setBooleanValue("testBooleanValue", true);

        boolean savedValue = sharedPreferences.getBooleanValue("testBooleanValue", false);

        assertEquals(savedValue, true);
    }

    @Test
    public void testSettingAndGettingBooleanValue_SetFalse(){
        sharedPreferences.setBooleanValue("testBooleanValue", false);

        boolean savedValue = sharedPreferences.getBooleanValue("testBooleanValue", true);

        assertEquals(savedValue, false);
    }

    @Test
    public void testSettingAndGettingFloatValue(){
        sharedPreferences.setFloatValue("testFloatValue", 5.0f);

        float savedValue = sharedPreferences.getFloatValue("testFloatValue", 0.0f);

        assertEquals(savedValue, 5.0f, 0f);
    }
}