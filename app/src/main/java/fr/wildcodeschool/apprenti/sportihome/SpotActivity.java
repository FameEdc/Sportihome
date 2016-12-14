package fr.wildcodeschool.apprenti.sportihome;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by apprenti on 02/12/16.
 */

public class SpotActivity extends FragmentActivity {
    private String url;
    private ProgressDialog pDialog;
    private SpotModel mSpot;
    SpotActivity.ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);


        Bundle bundle = getIntent().getExtras();
        String spot_id = bundle.getString("spot_id");
        url = "https://www.sportihome.com/api/spots/"+spot_id+"/";
        new GetSpot().execute();
    }

    private class GetSpot extends AsyncTask<Void, Void, Void> {

        private int counti = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SpotActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            mSpot = ParserJSON.getSpot(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            final Context context = SpotActivity.this.getBaseContext();

            ImageView avatar = (ImageView)findViewById(R.id.profile_image);
            //ImageView imgSlider = (ImageView)findViewById(R.id.img_slide);
            TextView txtCountRate = (TextView)findViewById(R.id.rating_count);
            TextView txtName = (TextView)findViewById(R.id.name_user);
            TextView txtSpotName = (TextView)findViewById(R.id.title_place);

            TextView txtAddress = (TextView)findViewById(R.id.address);

            RatingBar rateSpot = (RatingBar)findViewById(R.id.ratingBar_beauty);
            RatingBar rateQuality = (RatingBar)findViewById(R.id.ratingBar_dificulty);
            RatingBar rateBeauty = (RatingBar)findViewById(R.id.ratingBar_quality);
            RatingBar rateDificulty = (RatingBar)findViewById(R.id.ratingBar);
            TextView txtSport = (TextView)findViewById(R.id.sport);
            TextView txtAbout = (TextView)findViewById(R.id.text_about);
            CustomFontTextView imgSport = (CustomFontTextView) findViewById(R.id.sport_affichage);

            String[] pictures = mSpot.getPictures();
            String[] picturesUrl = new String[pictures.length];
            for (int i= 0 ; i < pictures.length ;i++){
                //picturesUrl[i] = picturesUrl[i].replace(" ","%20");
                picturesUrl[i] = "https://sportihome.com/uploads/spots/"+mSpot.get_id()+"/thumb/"+pictures[i];
            }

            String user,avatarUrl="";
            if(!mSpot.getOwner().getAvatar().isEmpty()){
                user = mSpot.getOwner().get_id();
                avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/"+mSpot.getOwner().getAvatar();
            }
            else{
                if (!mSpot.getOwner().getFacebook().getAvatar().isEmpty()){
                    avatarUrl = mSpot.getOwner().getFacebook().getAvatar();
                }
            }
            int count = mSpot.getRating().getNumberOfRatings();
            String sport = mSpot.getHobby();
            String userName = mSpot.getOwner().getIdentity().getFirstName();
            String name = mSpot.getName();
            String city = mSpot.getAddress().getLocality();
            String region = mSpot.getAddress().getAdministrative_area_level_1();
            String country = mSpot.getAddress().getCountry();
            String about = mSpot.getAbout();
            int rating = mSpot.getRating().getOverallRating();
            int countRatingsQuality = mSpot.getRating().getNumberOfRatings();
            int countRatingsDificulty = mSpot.getRating().getNumberOfRatings();
            int countRatingsBeauty = mSpot.getRating().getNumberOfRatings();

            String strName = "img_"+sport;
            strName = strName.replace("-","_");
            String strHobby = getStringResourceByName(strName, context);

            //AFFICHER
            imageFragmentPagerAdapter = new SpotActivity.ImageFragmentPagerAdapter(getSupportFragmentManager(),picturesUrl);
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
                            if(counti<=5){
                                viewPager.setCurrentItem(counti);
                                counti++;
                            }else{
                                counti = 0;
                                viewPager.setCurrentItem(counti);
                            }
                        }
                    });
                }
            }, 500, 3000);

            //Picasso.with(SpotActivity.this).load(pictureUrl).fit().centerCrop().into(imgSlider);
            if (!avatarUrl.isEmpty()){
                Picasso.with(SpotActivity.this).load(avatarUrl).fit().centerCrop().into(avatar);
            }

            txtCountRate.setText("("+String.valueOf(count+")"));
            imgSport.setText(strHobby);
            txtAbout.setText(about);
            txtSport.setText(sport);
            txtName.setText(userName);
            txtSpotName.setText(name);

            txtAddress.setText(city+", "+region+", "+country);

            rateSpot.setRating(rating);
            rateQuality.setRating(countRatingsQuality);
            rateDificulty.setRating(countRatingsDificulty);
            rateBeauty.setRating(countRatingsBeauty);



        }

    }
    private String getStringResourceByName(String aString,Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
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
            PlaceActivity.SwipeFragment fragment = new PlaceActivity.SwipeFragment();
            return PlaceActivity.SwipeFragment.newInstance(position, picturesUrl[position]);
        }
    }

    public static class SwipeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            Context context = swipeView.getContext();

            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
            //ImageButton imageBtn = (ImageButton) swipeView.findViewById(R.id.imageBtn);

            Bundle bundle = getArguments();

            int position = bundle.getInt("position");
            String imageFileUrl = bundle.getString("url");

            Picasso.with(context).load(imageFileUrl).fit().centerCrop().into(imageView);
            //Picasso.with(context).load(imageFileUrl).fit().centerCrop().into(imageBtn);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //code
                }
            });

            return swipeView;

        }

        static PlaceActivity.SwipeFragment newInstance(int position, String pictureUrl) {

            PlaceActivity.SwipeFragment swipeFragment = new PlaceActivity.SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putString("url", pictureUrl);
            swipeFragment.setArguments(bundle);

            return swipeFragment;

        }
    }

}
