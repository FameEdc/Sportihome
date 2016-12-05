package fr.wildcodeschool.apprenti.sportihome;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.ExpandableListView;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity{

    private ProgressDialog pDialog;
    // more efficient than HashMap for mapping integers to objects
    SparseArray<PlacesGroup> placesGroup = new SparseArray<PlacesGroup>();
    SparseArray<SpotsGroup> spotsGroup = new SparseArray<SpotsGroup>();
    private final String urlPlaces = "https://sportihome.com/api/places/";
    private final String urlSpots = "https://sportihome.com/api/spots/";
    private ArrayList<PlaceModel> placesList;
    private ArrayList<SpotModel> spotsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        createGroupsData();

        ExpandableListView placesList = (ExpandableListView) findViewById(R.id.places_list);
        PlacesExpandableListAdapter placesAdapter = new PlacesExpandableListAdapter(SearchActivity.this, placesGroup);
        placesList.setAdapter(placesAdapter);

        ExpandableListView spotsList = (ExpandableListView) findViewById(R.id.spots_list);
        SpotsExpandableListAdapter spotsAdapter = new SpotsExpandableListAdapter(SearchActivity.this, spotsGroup);
        spotsList.setAdapter(spotsAdapter);

    }

    public void createGroupsData() {

        new GetPlaces().execute();
        new GetSpots().execute();

        PlacesGroup placesGroup = new PlacesGroup("Hébergements :");
        SpotsGroup spotsGroup = new SpotsGroup("Spots :");

        for (int sand=0 ; sand < placesList.size() ; sand++){
            placesGroup.place.add(placesList.get(sand));
        }
        for (int rock=0 ; rock < placesList.size() ; rock++){
            spotsGroup.spot.add(spotsList.get(rock));
        }

        //placesGroup.append(0, placesGroup);
        //spotsGroup.append(0, spotsGroup);

    }

    private class GetPlaces extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(urlPlaces);
            placesList = ParserJSON.getPlaces(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }

    private class GetSpots extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(urlSpots);
            //spotsList = ParserJSON.getSpots(jsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }
}