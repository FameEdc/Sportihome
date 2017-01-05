package fr.wildcodeschool.apprenti.sportihome;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ProgressDialog ppDialog, psDialog;
    private final String urlPlaces = "https://sportihome.com/api/places/";
    private final String urlSpots = "https://sportihome.com/api/spots/";
    private ArrayList<PlaceModel> placesList;
    private ArrayList<SpotModel> spotsList;
    private Button btn_profil;
    ArrayList<SpotModel> spotsData;
    ArrayList<PlaceModel> placesData;
    int minItems = 10;
    private ListView spots,places;
    private Handler mSpotHandler,mPlaceHandler;
    private View ftView;
    private boolean isLoadingSpot = false;
    private boolean isLoadingPlace = false;
    int currentId=0;
    boolean spotEnd = false;
    boolean placeEnd = false;

    SpotsAdapter spotsAdapter;
    PlacesAdapter placesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if(isOnline()){
            Log.i("YOLO","Connecté");
            new GetPlaces().execute();
            //new GetSpots().execute();
        }else{
            Log.i("YOLO","Non Connecté");
        }

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

            LayoutInflater pli = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            places = (ListView) findViewById(R.id.places_list);
            ftView = pli.inflate(R.layout.footer_view,null);
            mPlaceHandler = new MyPlaceHandler();

            placesData = new ArrayList<PlaceModel>(placesList.size());

            //add 10 first items
            int limit;
            if (minItems > placesList.size()){
                limit = placesList.size()-1;
            }else{
                limit = minItems;
            }

            while(currentId<limit){
                placesData.add(placesList.get(currentId));
                currentId++;
            }

            placesAdapter = new PlacesAdapter(SearchActivity.this, placesData);
            places.setAdapter(placesAdapter);

            places.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (view.getLastVisiblePosition() == totalItemCount-1 && places.getCount() >= minItems && isLoadingPlace == false && placeEnd == false){
                        isLoadingPlace=true;
                        Thread thread = new ThreadGetMorePlaces();
                        thread.start();
                    }
                }
            });

        }
    }

    private class GetSpots extends AsyncTask<Void, Void, Void> {

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
/*
            LayoutInflater sli = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            spots = (ListView) findViewById(R.id.spots_list);
            ftView = sli.inflate(R.layout.footer_view,null);
            mSpotHandler = new MySpotHandler();

            spotsData = new ArrayList<SpotModel>(spotsList.size());

            //add 10 first items
            int limit;
            if (minItems > spotsList.size()){
                limit = spotsList.size()-1;
            }else{
                limit = minItems;
            }

            while(currentId<limit){
                spotsData.add(spotsList.get(currentId));
                currentId++;
            }



            spotsAdapter = new SpotsAdapter(SearchActivity.this, spotsData);
            spots.setAdapter(spotsAdapter);

            spots.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (view.getLastVisiblePosition() == totalItemCount-1 && spots.getCount() >= minItems && isLoadingSpot == false && spotEnd == false){
                        isLoadingSpot=true;
                        Thread thread = new ThreadGetMoreSpots();
                        thread.start();
                    }
                }
            });
*/

        }
    }

    private class MySpotHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    spots.addFooterView(ftView);
                    break;
                case 1:
                    spotsAdapter.addListItemsToAdapter((ArrayList<SpotModel>) msg.obj);
                    spots.removeFooterView(ftView);
                    isLoadingSpot=false;
                    break;
                default:
                    break;
            }
        }
    }

    private class MyPlaceHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    places.addFooterView(ftView);
                    break;
                case 1:
                    placesAdapter.addListItemsToAdapter((ArrayList<PlaceModel>) msg.obj);
                    places.removeFooterView(ftView);
                    isLoadingPlace=false;
                    break;
                default:
                    break;
            }
        }
    }

    private ArrayList<SpotModel> getMoreSpots(){
        ArrayList<SpotModel> lst= new ArrayList<>();
        //add 10 next items in lst
        int limit = currentId + minItems;
        int max = spotsList.size()-1;
        if(limit > max){
            limit = max;
            spotEnd = true;
        }

        while (currentId<limit){
            lst.add(spotsList.get(currentId));
            currentId++;
        }

        return lst;
    }

    private ArrayList<PlaceModel> getMorePlaces(){
        ArrayList<PlaceModel> lst= new ArrayList<>();
        //add 10 next items in lst

        int limit = currentId + minItems;
        int max = placesList.size()-1;
        if(limit > max){
            limit = max;
            placeEnd = true;
        }

        while (currentId<limit){
            lst.add(placesList.get(currentId));
            currentId++;
        }

        return lst;
    }

    private class ThreadGetMoreSpots extends Thread{
        @Override
        public void run() {
            super.run();
            mSpotHandler.sendEmptyMessage(0);
            ArrayList<SpotModel> lstResult = getMoreSpots();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = mSpotHandler.obtainMessage(1,lstResult);
            mSpotHandler.sendMessage(msg);
        }
    }

    private class ThreadGetMorePlaces extends Thread{
        @Override
        public void run() {
            super.run();
            mPlaceHandler.sendEmptyMessage(0);
            ArrayList<PlaceModel> lstResult = getMorePlaces();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = mPlaceHandler.obtainMessage(1,lstResult);
            mPlaceHandler.sendMessage(msg);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}