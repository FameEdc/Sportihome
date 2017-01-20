package fr.wildcodeschool.apprenti.sportihome.Adapters;

import android.app.Activity;
import android.content.Intent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import fr.wildcodeschool.apprenti.sportihome.Activities.MainActivity;
import fr.wildcodeschool.apprenti.sportihome.BuildConfig;
import fr.wildcodeschool.apprenti.sportihome.Model.PlaceModel;

/**
 * Created by chantome on 19/01/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PlacesAdapterTest {

    private PlacesAdapter placesAdapter;

    @RunWith(RobolectricTestRunner.class)

    @Config(constants = BuildConfig.class)
    public class SandwichTest {
    }

    @Before
    public void setUp() throws Exception {
        placesAdapter = new PlacesAdapter(RuntimeEnvironment.application, new ArrayList<PlaceModel>());
    }

    @Test
    public void getHobbiesString() throws Exception {
        String[] stringTab = new String[]{"bmx", "alpinisme", "apnee"};
        String hobbiesString = placesAdapter.getHobbiesString(stringTab);
        Assert.assertEquals("caO", hobbiesString);
    }

}