package fr.wildcodeschool.apprenti.sportihome;

import java.util.ArrayList;

/**
 * Created by chantome on 03/12/2016.
 */

public class PlacesGroup {
    public String string;
    public final ArrayList<PlaceModel> place = new ArrayList<PlaceModel>();

    public PlacesGroup(String string) {
        this.string = string;
    }
}
