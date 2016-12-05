package fr.wildcodeschool.apprenti.sportihome;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaceActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private PlaceModel maPlace;
    private String url;

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

            ImageView imgPlace = (ImageView) findViewById(R.id.place_img);
            TextView txtPrice = (TextView) findViewById(R.id.price);
            CircleImageView imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
            TextView txtOwnerName = (TextView) findViewById(R.id.name_user);
            TextView txtName = (TextView) findViewById(R.id.title_place);
            TextView txtCity = (TextView) findViewById(R.id.city);
            TextView txtRegion = (TextView) findViewById(R.id.region);
            TextView txtCountry = (TextView) findViewById(R.id.country);
            RatingBar ratePlace = (RatingBar) findViewById(R.id.ratingBar);
            TextView txtRatingCount = (TextView) findViewById(R.id.rating_count);
            ImageView imgEngagement = (ImageView) findViewById(R.id.engagement);
            CustomFontTextView txtSports = (CustomFontTextView) findViewById(R.id.sports);
            CustomFontTextView imgProperty = (CustomFontTextView) findViewById(R.id.imgProperty);
            TextView txtProperty = (TextView) findViewById(R.id.txtProperty);
            TextView txtTravellers = (TextView) findViewById(R.id.txtTravellers);
            TextView txtAbout =(TextView) findViewById(R.id.txt_about);

            //Load datas
            String picture = maPlace.getFirstPicture();
            String pictureUrl = "https://sportihome.com/uploads/places/"+maPlace.get_id()+"/thumb/"+picture;

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

            int travellers = maPlace.getHome().getTravellers();
            String about = maPlace.getAbout();

            //AFFICHER
            Picasso.with(PlaceActivity.this).load(pictureUrl).fit().centerCrop().into(imgPlace);

            txtPrice.setText(price+" €/nuit");
            if (!avatarUrl.isEmpty()){
                Picasso.with(PlaceActivity.this).load(avatarUrl).fit().centerCrop().into(imgAvatar);
            }
            txtOwnerName.setText(userName);
            txtName.setText(name);
            txtCity.setText(city+",");
            txtRegion.setText(region+",");
            txtCountry.setText(country);
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
                    imgProperty.setText("");
                    txtProperty.setText(propertyType);
                    break;
                case "Appartement":
                    imgProperty.setText("");
                    txtProperty.setText(propertyType);
                    break;
            }
            txtTravellers.setText(travellers+"");
            txtAbout.setText(about);
        }
    }

    private String getStringResourceByName(String aString,Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }
}
