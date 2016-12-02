package fr.wildcodeschool.apprenti.sportihome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity{

    private ProgressDialog pDialog;
    private ListView lvp;
    private ListView lvs;
    private PlacesAdapter adapter;
    private SpotsAdapter spotsAdapter;
    private final String url = "https://sportihome.com/api/places/";
    private ArrayList<PlaceModel> placesList;
    private ArrayList<Integer> placesSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        placesList = new ArrayList<PlaceModel>();
/*        placesSpot = new ArrayList<Integer>();

        placesSpot.add(1);
        placesSpot.add(2);*/


        lvp = (ListView) findViewById(R.id.places_list);
 /*       lvs = (ListView) findViewById(R.id.spots_list);

        spotsAdapter = new SpotsAdapter(SearchActivity.this, placesSpot);

        lvs.setAdapter(spotsAdapter);

        lvs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this,SpotActivity.class);
                startActivity(intent);
                finish();
            }
        });*/

        new GetPlaces().execute();
//        new GetSpots().execute();
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
            String jsonStr = sh.makeServiceCall(url);
            placesList = ParserJSON.getPlaces(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            adapter = new PlacesAdapter(SearchActivity.this, placesList);

            lvp.setAdapter(adapter);

            lvp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(SearchActivity.this,PlaceActivity.class);
                    intent.putExtra("place_id",placesList.get(i).get_id());
                    startActivity(intent);
                    finish();
                }
            });
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

            placesSpot.add(1);
            placesSpot.add(2);

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


