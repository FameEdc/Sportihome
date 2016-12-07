package fr.wildcodeschool.apprenti.sportihome;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by apprenti on 02/12/16.
 */

public class SpotActivity extends AppCompatActivity {
    private String url;
    private ProgressDialog pDialog;
    private SpotModel mSpot;
    //final Context context = SpotActivity.this.getBaseContext();

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
            ImageView imgSlider = (ImageView)findViewById(R.id.img_slide);
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

            String picture = mSpot.getFirstPicture();
            picture = picture.replace(" ","%20");
            String pictureUrl = "https://sportihome.com/uploads/spots/"+mSpot.get_id()+"/thumb/"+picture;


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
            Picasso.with(SpotActivity.this).load(pictureUrl).fit().centerCrop().into(imgSlider);
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
}
