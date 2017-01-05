package fr.wildcodeschool.apprenti.sportihome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity{

    private ProgressDialog ppDialog, psDialog;
    // more efficient than HashMap for mapping integers to objects
    SparseArray<PlacesGroup> tabPlacesGroup = new SparseArray<PlacesGroup>();
    SparseArray<SpotsGroup> tabSpotsGroup = new SparseArray<SpotsGroup>();
    private final String urlPlaces = "https://sportihome.com/api/places/";
    private final String urlSpots = "https://sportihome.com/api/spots/";
    private ArrayList<PlaceModel> placesList;
    private ArrayList<SpotModel> spotsList;
    private Button btn_profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //createGroupsData();
        new GetPlaces().execute();
       // new GetSpots().execute();
        btn_profil = (Button)findViewById(R.id.my_profil);
        btn_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

    }

    private class GetPlaces extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ppDialog = new ProgressDialog(SearchActivity.this);
            ppDialog.setMessage("Please wait...");
            ppDialog.setCancelable(false);
            ppDialog.show();
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

            if (ppDialog.isShowing())
                ppDialog.dismiss();

            PlacesGroup placesGroup = new PlacesGroup("Hébergements :");
            for (int sand=0 ; sand < placesList.size() ; sand++){
                placesGroup.place.add(placesList.get(sand));
            }

            tabPlacesGroup.append(0, placesGroup);

            ExpandableListView placesList = (ExpandableListView) findViewById(R.id.places_list);
            PlacesExpandableListAdapter placesAdapter = new PlacesExpandableListAdapter(SearchActivity.this, tabPlacesGroup);
            placesList.setAdapter(placesAdapter);


        }
    }

/*    private class GetSpots extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            psDialog = new ProgressDialog(SearchActivity.this);
            psDialog.setMessage("Please wait...");
            psDialog.setCancelable(false);
            psDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(urlSpots);

            spotsList = ParserJSON.getSpots(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (psDialog.isShowing())
                psDialog.dismiss();

            SpotsGroup spotsGroup = new SpotsGroup("Spots :");
            for (int rock=0 ; rock < spotsList.size() ; rock++){
                spotsGroup.spot.add(spotsList.get(rock));
            }

            tabSpotsGroup.append(0, spotsGroup);

            ExpandableListView spotsList = (ExpandableListView) findViewById(R.id.spots_list);
            SpotsExpandableListAdapter spotsAdapter = new SpotsExpandableListAdapter(SearchActivity.this, tabSpotsGroup);
            spotsList.setAdapter(spotsAdapter);

        }
    }*/
}