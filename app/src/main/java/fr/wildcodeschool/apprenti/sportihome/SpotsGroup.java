package fr.wildcodeschool.apprenti.sportihome;

import java.util.ArrayList;

/**
 * Created by chantome on 03/12/2016.
 */

public class SpotsGroup {
    public String string;
    public final ArrayList<SpotModel> spot = new ArrayList<SpotModel>();

    public SpotsGroup(String string) {
        this.string = string;
    }
}
