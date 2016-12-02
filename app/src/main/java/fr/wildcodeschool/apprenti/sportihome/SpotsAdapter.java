package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by Alban on 24/11/2016.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class SpotsAdapter extends ArrayAdapter<Integer> {

    private final Activity context;
    public ArrayList<Integer> mesSpots;

    public SpotsAdapter(Activity context, ArrayList<Integer> mesSpots) {
        super(context, R.layout.activity_search, mesSpots);

        this.context=context;
        this.mesSpots = mesSpots;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.spots_list_item, null,true);


        return rowView;
    }

}
