package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by edwin on 02/12/2016.
 */


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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class PlaceActivity extends FragmentActivity {

    private ProgressDialog pDialog;
    private PlaceModel maPlace;
    private String url;
    //private SliderLayout sliderShow;


    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    //public static final String[] IMAGE_NAME = {"eagle", "horse", "bonobo", "wolf", "owl", "bear",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Bundle bundle = getIntent().getExtras();
        String place_id = bundle.getString("place_id");

        url = "https://sportihome.com/api/places/"+place_id+"/";

        new GetPlace().execute();

    }

    private class GetPlace extends AsyncTask<Void, Void, Void> {

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

            TextView txtPrice = (TextView) findViewById(R.id.price);
            CircleImageView imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
            TextView txtOwnerName = (TextView) findViewById(R.id.name_user);
            TextView txtName = (TextView) findViewById(R.id.title_place);
            TextView txtAddress = (TextView) findViewById(R.id.address);
            RatingBar ratePlace = (RatingBar) findViewById(R.id.ratingBar);
            TextView txtRatingCount = (TextView) findViewById(R.id.rating_count);
            ImageView imgEngagement = (ImageView) findViewById(R.id.engagement);
            CustomFontTextView txtSports = (CustomFontTextView) findViewById(R.id.sports);
            CustomFontTextView imgProperty = (CustomFontTextView) findViewById(R.id.imgProperty);
            TextView txtProperty = (TextView) findViewById(R.id.txtProperty);
            CustomFontTextView imgPrivate = (CustomFontTextView) findViewById(R.id.imgPrivate);
            TextView txtPrivate = (TextView) findViewById(R.id.txtPrivate);
            TextView txtTravellers = (TextView) findViewById(R.id.txtTravellers);
            TextView txtAbout = (TextView) findViewById(R.id.txt_about);

            //Load datas
            String[] pictures = maPlace.getPictures();
            String[] picturesUrl = new String[pictures.length];
            for (int i= 0 ; i < pictures.length ;i++){
                //picturesUrl[i] = picturesUrl[i].replace(" ","%20");
                picturesUrl[i] = "https://sportihome.com/uploads/places/"+maPlace.get_id()+"/thumb/"+pictures[i];
            }

            int price = maPlace.getHome().getPrice().getLowSeason();
            String user,avatarUrl="";
            if(!maPlace.getOwner().getAvatar().isEmpty()){
                user = maPlace.getOwner().get_id();
                avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/image.jpeg";
            }
            else{
                if (!maPlace.getOwner().getFacebook().getAvatar().isEmpty()){
                    avatarUrl = maPlace.getOwner().getFacebook().getAvatar();
                }
            }
            String userName = maPlace.getOwner().getIdentity().getFirstName();
            String name = maPlace.getName();
            String city = maPlace.getAddress().getLocality();
            String region = maPlace.getAddress().getAdministrative_area_level_1();
            String country = maPlace.getAddress().getCountry();
            int rating = maPlace.getRating().getOverallRating();
            int countRatings = maPlace.getRating().getNumberOfRatings();
            String engagement = maPlace.getOwner().getEngagement();
            String[] hobbies = maPlace.getHobbies();
            String strHobbies="";
            for (int i=0 ; i < hobbies.length ; i++){
                String stringName = "img_"+hobbies[i];
                stringName = stringName.replace("-","_");
                strHobbies += getStringResourceByName(stringName,PlaceActivity.this);
            }
            String propertyType = maPlace.getHome().getPropertyType();
            boolean mPrivate = maPlace.ismPrivate();
            int travellers = maPlace.getHome().getTravellers();
            String about = maPlace.getAbout();

            //AFFICHER
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
                            if(count<=5){
                                viewPager.setCurrentItem(count);
                                count++;
                            }else{
                                count = 0;
                                viewPager.setCurrentItem(count);
                            }
                        }
                    });
                }
            }, 500, 3000);

            txtPrice.setText(price+" €/nuit");
            if (!avatarUrl.isEmpty()){
                Picasso.with(PlaceActivity.this).load(avatarUrl).fit().centerCrop().into(imgAvatar);
            }
            txtOwnerName.setText(userName);
            txtName.setText(name);
            txtAddress.setText(city+", " + region+", "+ country);

            ratePlace.setRating(rating);
            txtRatingCount.setText("("+String.valueOf(countRatings+")"));
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
            txtSports.setText(strHobbies);

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
            if (mPrivate) {
                imgPrivate.setVisibility(View.VISIBLE);
                txtPrivate.setText("Privée");
            }
            txtTravellers.setText(travellers+"");
            txtAbout.setText(about);
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
