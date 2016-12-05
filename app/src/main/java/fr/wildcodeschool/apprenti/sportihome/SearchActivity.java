package fr.wildcodeschool.apprenti.sportihome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity{

    private ProgressDialog pDialog;
    // more efficient than HashMap for mapping integers to objects
    SparseArray<PlacesGroup> groups = new SparseArray<PlacesGroup>();
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

        //placesList = new ArrayList<PlaceModel>();

        //createGroupsData();
        new GetPlaces().execute();


/*        placesSpot = new ArrayList<Integer>();

        placesSpot.add(1);
        placesSpot.add(2);*/


        //lvp = (ListView) findViewById(R.id.places_list);
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
            createGroupsData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            ExpandableListView searchList = (ExpandableListView) findViewById(R.id.search_list);
            SearchExpandableListAdapter searchAdapter = new SearchExpandableListAdapter(SearchActivity.this, groups);
            searchList.setAdapter(searchAdapter);


            /*
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
            });*/
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

    public void createGroupsData() {

        PlacesGroup placesGroup = new PlacesGroup("Hébergements :");
        for (int sand=0 ; sand < placesList.size() ; sand++){
            placesGroup.place.add(placesList.get(sand));
        }
        groups.append(0, placesGroup);
        /*
        for (int j = 0; j < 5; j++) {
            Group group = new Group("Test " + j);
            for (int i = 0; i < 5; i++) {
                group.children.add("Sub Item" + i);
            }
            //groups.append(j, group);
        }
        */
    }

}


