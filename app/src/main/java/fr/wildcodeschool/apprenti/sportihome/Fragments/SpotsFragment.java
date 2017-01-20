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
import android.widget.ListView;
import java.util.ArrayList;

import fr.wildcodeschool.apprenti.sportihome.Adapters.SpotsAdapter;
import fr.wildcodeschool.apprenti.sportihome.HttpHandler;
import fr.wildcodeschool.apprenti.sportihome.Model.SpotModel;
import fr.wildcodeschool.apprenti.sportihome.ParserJSON;
import fr.wildcodeschool.apprenti.sportihome.R;


public class SpotsFragment extends Fragment {

    private ProgressDialog  psDialog;
    private final String urlSpots = "https://sportihome.com/api/spots/";
    private ArrayList<SpotModel> spotsList;
    ArrayList<SpotModel> spotsData;
    int minItems = 10;
    private ListView spots;
    private Handler mSpotHandler;
    private View ftView;
    private boolean isLoadingSpot = false;
    int currentId = 0;
    boolean spotEnd = false;
    private Toolbar toolbar;
    SpotsAdapter spotsAdapter;


    public SpotsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ftView =  inflater.inflate(R.layout.fragment_spots, container, false);
        return ftView;
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

         if(isOnline()){
            Log.i("YOLO","Connecté");

            new GetSpots().execute();
        }else{
            Log.i("YOLO","Non Connecté");
        }


    }




    private class GetSpots extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            psDialog = new ProgressDialog(getActivity());
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


            LayoutInflater sli = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            spots = (ListView) ftView.findViewById(R.id.spots_list);
            ftView = sli.inflate(R.layout.footer_view, null);
            mSpotHandler = new MySpotHandler();

            spotsData = new ArrayList<SpotModel>(spotsList.size());

            //add 10 first items
            int limit;
            if (minItems > spotsList.size()) {
                limit = spotsList.size() - 1;
            } else {
                limit = minItems;
            }

            while (currentId < limit) {
                spotsData.add(spotsList.get(currentId));
                currentId++;
            }


            spotsAdapter = new SpotsAdapter(getActivity(), spotsData);
            spots.setAdapter(spotsAdapter);

            spots.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (view.getLastVisiblePosition() == totalItemCount - 1 && spots.getCount() >= minItems && isLoadingSpot == false && spotEnd == false) {
                        isLoadingSpot = true;
                        Thread thread = new ThreadGetMoreSpots();
                        thread.start();
                    }
                }
            });


        }
    }

    private class MySpotHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    spots.addFooterView(ftView);
                    break;
                case 1:
                    spotsAdapter.addListItemsToAdapter((ArrayList<SpotModel>) msg.obj);
                    spots.removeFooterView(ftView);
                    isLoadingSpot = false;
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
