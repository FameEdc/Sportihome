package fr.wildcodeschool.apprenti.sportihome.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.wildcodeschool.apprenti.sportihome.Font.CustomFontTextView;
import fr.wildcodeschool.apprenti.sportihome.HttpHandler;
import fr.wildcodeschool.apprenti.sportihome.Model.LocModel;
import fr.wildcodeschool.apprenti.sportihome.ParserJSON;
import fr.wildcodeschool.apprenti.sportihome.Adapters.PlaceCommentsAdapter;
import fr.wildcodeschool.apprenti.sportihome.Model.PlaceModel;
import fr.wildcodeschool.apprenti.sportihome.R;
import me.relex.circleindicator.CircleIndicator;

public class PlaceActivity extends FragmentActivity {

    private ProgressDialog pDialog;
    private PlaceModel maPlace;
    private LocModel mLoc;
    private String url;
    private boolean autoScroll=true;
    private int nbrPictures=0;
    private SupportMapFragment map;
    private float hue = 199;



    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Bundle bundle = getIntent().getExtras();
        String place_id = bundle.getString("place_id");

        url = "https://sportihome.com/api/places/"+place_id+"/";

        new GetPlace().execute();

    }

    private class GetPlace extends AsyncTask<Void, Void, Void> implements OnMapReadyCallback {

        private int count = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PlaceActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            maPlace = ParserJSON.getPlace(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            final Context context = PlaceActivity.this.getBaseContext();

            //CHECK & LOAD DATAS

            map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            map.getMapAsync(this);

            //Avatar
            CircleImageView imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
            String user,avatarUrl;
            //on verifie d'abord si c'est un avatar du site
            user = maPlace.getOwner().get_id();
            if (maPlace.getOwner().getAvatar() != null){
                avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/"+maPlace.getOwner().getAvatar();
                avatarUrl = avatarUrl.replace(" ","%20");
                Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
            }else{
                //sinon c'est que l'avatar viens soit de facebook soit de google

                if (maPlace.getOwner().getGoogle() != null){
                    if (maPlace.getOwner().getGoogle().getAvatar() != null){
                        //avatar google
                        avatarUrl = maPlace.getOwner().getGoogle().getAvatar();
                        Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
                    }
                }

                if (maPlace.getOwner().getFacebook() != null){
                    if (maPlace.getOwner().getFacebook().getAvatar() != null){
                        //avatar facebook
                        avatarUrl = maPlace.getOwner().getFacebook().getAvatar();
                        Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
                    }
                }
            }

            //Pictures
            if (maPlace.getPictures() != null){
                String[] pictures = maPlace.getPictures();
                nbrPictures = pictures.length;
                String[] picturesUrl = new String[nbrPictures];
                for (int i= 0 ; i < pictures.length ;i++){
                    //picturesUrl[i] = picturesUrl[i].replace(" ","%20");
                    picturesUrl[i] = "https://sportihome.com/uploads/places/"+maPlace.get_id()+"/thumb/"+pictures[i];
                }

                imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager(),picturesUrl);
                viewPager = (ViewPager) findViewById(R.id.pager);
                CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                viewPager.setAdapter(imageFragmentPagerAdapter);
                indicator.setViewPager(viewPager);
                viewPager.setCurrentItem(0);

                // Timer for auto sliding
                Timer timer  = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(autoScroll){
                                    if(count < nbrPictures){
                                        viewPager.setCurrentItem(count);
                                        count++;
                                    }else{
                                        count = 0;
                                        viewPager.setCurrentItem(count);
                                    }
                                }
                            }
                        });
                    }
                }, 500, 3000);

                viewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        autoScroll=false;
                        return false;
                    }
                });
            }

            //Price
            if (maPlace.getHome().getPrice().getLowSeason() != 0){
                TextView txtPrice = (TextView) findViewById(R.id.price);
                int price = maPlace.getHome().getPrice().getLowSeason();
                txtPrice.setText(price+" €/nuit");
            }

            //User name
            if (maPlace.getOwner().getIdentity().getFirstName() != null){
                TextView txtOwnerName = (TextView) findViewById(R.id.name_user);
                String userName = maPlace.getOwner().getIdentity().getFirstName();
                txtOwnerName.setText(userName);
            }

            //Place name
            if (maPlace.getName() != null){
                TextView txtName = (TextView) findViewById(R.id.title_place);
                String name = maPlace.getName();
                txtName.setText(name);
            }

            //Address
            if (maPlace.getAddress().getLocality() != null && maPlace.getAddress().getAdministrative_area_level_1() != null && maPlace.getAddress().getCountry() != null){
                TextView txtAddress = (TextView) findViewById(R.id.address);
                String city = maPlace.getAddress().getLocality();
                String region = maPlace.getAddress().getAdministrative_area_level_1();
                String country = maPlace.getAddress().getCountry();
                txtAddress.setText(city+", " + region+", "+ country);
            }

            //Rating
            if (maPlace.getRating().getOverallRating() != 0){
                RatingBar ratePlace = (RatingBar) findViewById(R.id.ratingBar);
                int rating = maPlace.getRating().getOverallRating();
                ratePlace.setRating(rating);
            }

            //Count rating
            if (maPlace.getRating().getNumberOfRatings() != 0){
                TextView txtRatingCount = (TextView) findViewById(R.id.rating_count);
                TextView txtOpinions = (TextView)findViewById(R.id.title_opinions);
                int countRatings = maPlace.getRating().getNumberOfRatings();
                txtRatingCount.setText("("+String.valueOf(countRatings+")"));
                txtOpinions.setText(countRatings+" Avis");
            }

            //Engagement
            if (maPlace.getOwner().getEngagement() != null){
                ImageView imgEngagement = (ImageView) findViewById(R.id.engagement);
                String engagement = maPlace.getOwner().getEngagement();
                switch (engagement){
                    case "stay":
                        imgEngagement.setImageResource(R.drawable.stay);
                        break;
                    case "stayshare":
                        imgEngagement.setImageResource(R.drawable.stayshare);
                        break;
                    case "stayshareplay":
                        imgEngagement.setImageResource(R.drawable.stayshareplay);
                        break;
                }
            }

            //Hobbies
            if (maPlace.getHobbies() != null){
                CustomFontTextView txtSports = (CustomFontTextView) findViewById(R.id.sports);
                String[] hobbies = maPlace.getHobbies();
                String strHobbies="";
                for (int i=0 ; i < hobbies.length ; i++){
                    String stringName = "img_"+hobbies[i];
                    stringName = stringName.replace("-","_");
                    strHobbies += getStringResourceByName(stringName,PlaceActivity.this);
                }
                txtSports.setText(strHobbies);
            }

            //Proper type
            if (maPlace.getHome().getPropertyType() != null){
                TextView txtProperty = (TextView) findViewById(R.id.txtProperty);
                CustomFontTextView imgProperty = (CustomFontTextView) findViewById(R.id.imgProperty);
                String propertyType = maPlace.getHome().getPropertyType();
                switch (propertyType){
                    case "Maison":
                        imgProperty.setText("f");
                        txtProperty.setText(propertyType);
                        break;
                    case "Appartement":
                        imgProperty.setText("m");
                        txtProperty.setText(propertyType);
                        break;
                }
            }

            //Private
            if (maPlace.ismPrivate() == true){
                TextView txtPrivate = (TextView) findViewById(R.id.txtPrivate);
                CustomFontTextView imgPrivate = (CustomFontTextView) findViewById(R.id.imgPrivate);
                imgPrivate.setText("5");
                txtPrivate.setText("Privée");
            }

            //Travellers
            if (maPlace.getHome().getTravellers() != 0){
                TextView txtTravellers = (TextView) findViewById(R.id.txtTravellers);
                int travellers = maPlace.getHome().getTravellers();
                txtTravellers.setText(String.valueOf(travellers)+" voyageur(s)");
            }

            //About
            if (maPlace.getAbout() != null){
                TextView txtAbout = (TextView) findViewById(R.id.txt_about);
                String about = maPlace.getAbout();
                txtAbout.setText(about);
            }

            //Value for money
            if (maPlace.getRating().getValueForMoney() != 0){
                RatingBar rateValueForMoney = (RatingBar) findViewById(R.id.ratingBar_valueformoney);
                int ratingVFM = maPlace.getRating().getValueForMoney();
                rateValueForMoney.setRating(ratingVFM);
            }

            //Location
            if (maPlace.getRating().getLocation() != 0){
                RatingBar rateLocation = (RatingBar) findViewById(R.id.ratingBar_location);
                int ratingLocation = maPlace.getRating().getLocation();
                rateLocation.setRating(ratingLocation);
            }

            //Cleanness
            if (maPlace.getRating().getCleanness() != 0){
                RatingBar rateCleanness = (RatingBar) findViewById(R.id.ratingBar_cleanness);
                int ratingCleannness = maPlace.getRating().getCleanness();
                rateCleanness.setRating(ratingCleannness);
            }

            //Comments
            if(maPlace.getComments() != null && maPlace.getRating().getNumberOfRatings() != 0){

                ListView listComments = (ListView) findViewById(R.id.place_comments);
                PlaceCommentsAdapter placeCommentsAdapter = new PlaceCommentsAdapter(PlaceActivity.this, maPlace.getComments());
                listComments.setAdapter(placeCommentsAdapter);

            }

        }

        @Override
        public void onMapReady(GoogleMap map) {

            double[] latitudeLongitude = maPlace.getLoc().getCoordinates();
            double lat = latitudeLongitude[1];
            double lng = latitudeLongitude[0];
            LatLng latLng = new LatLng(lat, lng);

            BitmapDescriptor marker_place = BitmapDescriptorFactory.defaultMarker(hue);

            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(maPlace.getName()).icon(BitmapDescriptorFactory.defaultMarker(hue)));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            noScrollMap();

        }

        public void noScrollMap() {
            final ScrollView mScrollView = (ScrollView) findViewById(R.id.activity_place);
            ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

            transparentImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                            return false;
                        case MotionEvent.ACTION_UP:
                            mScrollView.requestDisallowInterceptTouchEvent(false);
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                            return false;
                        default:
                            return true;
                    }
                }
            });
        }
    }

    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS=0;
        private String[] picturesUrl;

        public ImageFragmentPagerAdapter(FragmentManager fm, String[] picturesUrl) {
            super(fm);
            this.NUM_ITEMS = picturesUrl.length;
            this.picturesUrl = picturesUrl;
        }

        @Override
        public int getCount() {
            return this.NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position, picturesUrl[position]);
        }
    }

    public static class SwipeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            Context context = swipeView.getContext();

            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);

            Bundle bundle = getArguments();

            int position = bundle.getInt("position");
            String imageFileUrl = bundle.getString("url");

            Picasso.with(context).load(imageFileUrl).fit().centerCrop().into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //code
                }
            });

            return swipeView;

        }

        static SwipeFragment newInstance(int position, String pictureUrl) {

            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putString("url", pictureUrl);
            swipeFragment.setArguments(bundle);

            return swipeFragment;

        }
    }

    private String getStringResourceByName(String aString,Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }



}
