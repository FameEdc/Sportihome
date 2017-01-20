package fr.wildcodeschool.apprenti.sportihome.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import fr.wildcodeschool.apprenti.sportihome.Adapters.PlacesAdapter;
import fr.wildcodeschool.apprenti.sportihome.HttpHandler;
import fr.wildcodeschool.apprenti.sportihome.Model.PlaceModel;
import fr.wildcodeschool.apprenti.sportihome.ParserJSON;
import fr.wildcodeschool.apprenti.sportihome.R;

public class PlacesFragment extends Fragment {

    private ProgressDialog ppDialog;
    private final String urlPlaces = "https://sportihome.com/api/places/";
    private ArrayList<PlaceModel> placesList;
    private Button btn_profil;
    ArrayList<PlaceModel> placesData;
    int minItems = 10;
    private ListView places;
    private Handler mPlaceHandler;
    private View ftView;
    private boolean isLoadingPlace = false;
    int currentId=0;
    boolean placeEnd = false;
    private Toolbar toolbar;
    PlacesAdapter placesAdapter;

    public PlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ftView= inflater.inflate(R.layout.fragment_places, container, false);

        return ftView;
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        if (isOnline()) {
            Log.i("YOLO", "Connecté");

            new GetPlaces().execute();
        } else {
            Log.i("YOLO", "Non Connecté");
        }

    }


    private class GetPlaces extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ppDialog = new ProgressDialog(getActivity());
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

            LayoutInflater pli = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            places = (ListView) ftView.findViewById(R.id.places_list);
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

            placesAdapter = new PlacesAdapter(getActivity(), placesData);
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

    private class MyPlaceHandler extends Handler {
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
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
