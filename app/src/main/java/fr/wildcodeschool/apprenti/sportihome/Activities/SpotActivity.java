package fr.wildcodeschool.apprenti.sportihome.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.Timer;
import java.util.TimerTask;

import fr.wildcodeschool.apprenti.sportihome.Font.CustomFontTextView;
import fr.wildcodeschool.apprenti.sportihome.HttpHandler;
import fr.wildcodeschool.apprenti.sportihome.Model.SpotModel;
import fr.wildcodeschool.apprenti.sportihome.ParserJSON;
import fr.wildcodeschool.apprenti.sportihome.R;
import fr.wildcodeschool.apprenti.sportihome.Adapters.SpotCommentsAdapter;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by apprenti on 02/12/16.
 */

public class SpotActivity extends FragmentActivity {
    private String url;
    private ProgressDialog pDialog;
    private SpotModel mSpot;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    private boolean autoScroll=true;
    private int nbrPictures=0;

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

            //CHECK & LOAD DATAS

            //Avatar
            ImageView avatar = (ImageView)findViewById(R.id.img_avatar);
            String user,avatarUrl;
            //on verifie d'abord si c'est un avatar du site
            user = mSpot.getOwner().get_id();
            if (mSpot.getOwner().getAvatar() != null){
                avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/"+mSpot.getOwner().getAvatar();
                avatarUrl = avatarUrl.replace(" ","%20");
                Log.i("YOLO",avatarUrl);
                Picasso.with(context).load(avatarUrl).fit().centerCrop().into(avatar);
            }else{
                //sinon c'est que l'avatar viens soit de facebook soit de google

                if (mSpot.getOwner().getGoogle() != null){
                    if (mSpot.getOwner().getGoogle().getAvatar() != null){
                        //avatar google
                        avatarUrl = mSpot.getOwner().getGoogle().getAvatar();
                        Log.i("YOLO",avatarUrl);
                        Picasso.with(context).load(avatarUrl).fit().centerCrop().into(avatar);
                    }
                }

                if (mSpot.getOwner().getFacebook() != null){
                    if (mSpot.getOwner().getFacebook().getAvatar() != null){
                        //avatar facebook
                        avatarUrl = mSpot.getOwner().getFacebook().getAvatar();
                        Log.i("YOLO",avatarUrl);
                        Picasso.with(context).load(avatarUrl).fit().centerCrop().into(avatar);
                    }
                }
            }

            //Pictures
            if(mSpot.getPictures() != null){
                String[] pictures = mSpot.getPictures();
                String[] picturesUrl = new String[pictures.length];
                nbrPictures = pictures.length;
                for (int i= 0 ; i < pictures.length ;i++){
                    //picturesUrl[i] = picturesUrl[i].replace(" ","%20");
                    picturesUrl[i] = "https://sportihome.com/uploads/spots/"+mSpot.get_id()+"/thumb/"+pictures[i];
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
                                    if(counti < nbrPictures){
                                        viewPager.setCurrentItem(counti);
                                        counti++;
                                    }else{
                                        Log.i("YOLO","change state : "+counti);
                                        counti = 0;
                                        viewPager.setCurrentItem(counti);
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

            //User Name
            if(mSpot.getOwner().getIdentity().getFirstName() != null){
                TextView txtName = (TextView)findViewById(R.id.name_user);
                String userName = mSpot.getOwner().getIdentity().getFirstName();
                txtName.setText(userName);
            }

            //Spot Name
            if(mSpot.getName() != null){
                TextView txtSpotName = (TextView)findViewById(R.id.title_spot);
                String name = mSpot.getName();
                txtSpotName.setText(name);
            }

            //Hobby
            if(mSpot.getHobby() != null){
                CustomFontTextView imgSport = (CustomFontTextView) findViewById(R.id.img_hobby);
                TextView txtSport = (TextView)findViewById(R.id.txt_hobby);
                String sport = mSpot.getHobby();
                String strName = "img_"+sport;
                strName = strName.replace("-","_");
                String strHobby = getStringResourceByName(strName, context);
                imgSport.setText(strHobby);
                txtSport.setText(sport);
            }

            //Address
            if(mSpot.getAddress().getLocality() != null && mSpot.getAddress().getAdministrative_area_level_1() != null && mSpot.getAddress().getCountry() != null){
                TextView txtAddress = (TextView)findViewById(R.id.address);
                String city = mSpot.getAddress().getLocality();
                String region = mSpot.getAddress().getAdministrative_area_level_1();
                String country = mSpot.getAddress().getCountry();
                txtAddress.setText(city+", "+region+", "+country);
            }

            //Rating
            if(mSpot.getRating().getOverallRating() != 0){
                int rating = mSpot.getRating().getOverallRating();
                RatingBar rateSpot = (RatingBar)findViewById(R.id.ratingBar);
                rateSpot.setRating(rating);
            }

            //Count Rating
            if(mSpot.getRating().getNumberOfRatings() != 0){
                TextView txtCountRate = (TextView)findViewById(R.id.rating_count);
                TextView txtOpinions = (TextView)findViewById(R.id.title_opinions);
                int count = mSpot.getRating().getNumberOfRatings();
                txtCountRate.setText("("+String.valueOf(count+")"));
                txtOpinions.setText(count+" Avis");
            }

            //Quality
            if(mSpot.getRating().getQuality() != 0){
                RatingBar rateQuality = (RatingBar)findViewById(R.id.ratingBar_quality);
                int ratingsQuality = mSpot.getRating().getQuality();
                rateQuality.setRating(ratingsQuality);
            }

            //Beauty
            if(mSpot.getRating().getBeauty() != 0){
                RatingBar rateBeauty = (RatingBar)findViewById(R.id.ratingBar_beauty);
                int ratingsBeauty = mSpot.getRating().getBeauty();
                rateBeauty.setRating(ratingsBeauty);
            }

            //Difficulty
            if(mSpot.getRating().getDifficulty() != 0){
                RatingBar rateDifficulty = (RatingBar)findViewById(R.id.ratingBar_difficulty);
                int ratingsDifficulty = mSpot.getRating().getDifficulty();
                rateDifficulty.setRating(ratingsDifficulty);
            }

            //About
            if(mSpot.getAbout() != null){
                TextView txtAbout = (TextView)findViewById(R.id.txt_about);
                String about = mSpot.getAbout();
                txtAbout.setText(about);
            }

            //Comments
            if(mSpot.getComments() != null){

                ListView listComments = (ListView) findViewById(R.id.spot_comments);
                SpotCommentsAdapter spotCommentsAdapter = new SpotCommentsAdapter(SpotActivity.this, mSpot.getComments());
                listComments.setAdapter(spotCommentsAdapter);

            }

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

}
