package fr.wildcodeschool.apprenti.sportihome.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.wildcodeschool.apprenti.sportihome.Font.CustomFontTextView;
import fr.wildcodeschool.apprenti.sportihome.HttpHandler;
import fr.wildcodeschool.apprenti.sportihome.Model.LogInModel;
import fr.wildcodeschool.apprenti.sportihome.Model.OwnerModel;
import fr.wildcodeschool.apprenti.sportihome.R;

public class ProfilActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private OwnerModel mOwner;
    private String url;
    private LogInModel mLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        final Context context = ProfilActivity.this.getBaseContext();

        mLogIn = (LogInModel) getIntent().getSerializableExtra("login");
        //String user_id = mLogIn.getUser().get_id();
        //url = "https://sportihome.com/api/users/"+user_id+"/";

        //new GetPlace().execute();

        mOwner = mLogIn.getUser();

        //Avatar
        CircleImageView imgAvatar = (CircleImageView) findViewById(R.id.profile_image);
        String user,avatarUrl;
        //on verifie d'abord si c'est un avatar du site
        user = mOwner.get_id();
        if (mOwner.getAvatar() != null){
            avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/"+mOwner.getAvatar();
            avatarUrl = avatarUrl.replace(" ","%20");
            Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
        }else{
            //sinon c'est que l'avatar viens soit de facebook soit de google

            if (mOwner.getGoogle() != null){
                if (mOwner.getGoogle().getAvatar() != null){
                    //avatar google
                    avatarUrl = mOwner.getGoogle().getAvatar();
                    Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
                }
            }

            if (mOwner.getFacebook() != null){
                if (mOwner.getFacebook().getAvatar() != null){
                    //avatar facebook
                    avatarUrl = mOwner.getFacebook().getAvatar();
                    Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
                }
            }
        }

        String name = "";
        TextView txt_name = (TextView)findViewById(R.id.name_user);
        //User FirstName
        if (mOwner.getIdentity().getFirstName() != null){
            name += mOwner.getIdentity().getFirstName();
            txt_name.setText(name);
        }
        //User LastName
        if (mOwner.getIdentity().getLastName() != null){
            name += " "+mOwner.getIdentity().getLastName();
            txt_name.setText(name);
        }

        //Sports Podium
        if (mOwner.getHobbies() != null){
            String[] hobbies = mOwner.getHobbies();
            for (int i=0; i<hobbies.length; i++){

                String stringName = "img_"+hobbies[i];
                stringName = stringName.replace("-","_");
                String strHobby = getStringResourceByName(stringName,ProfilActivity.this);

                switch (i){
                    case 0:
                        CustomFontTextView txt_sport1 = (CustomFontTextView)findViewById(R.id.sport1);
                        txt_sport1.setText(strHobby);
                        break;
                    case 1:
                        CustomFontTextView txt_sport2 = (CustomFontTextView)findViewById(R.id.sport2);
                        txt_sport2.setText(strHobby);
                        break;
                    case 2:
                        CustomFontTextView txt_sport3 = (CustomFontTextView)findViewById(R.id.sport3);
                        txt_sport3.setText(strHobby);
                        break;
                }
            }
        }

        //Engagement
        if (mOwner.getEngagement() != null){
            String engagement = mOwner.getEngagement();
            switch (engagement){
                case "stay":
                    ImageView img_stay = (ImageView)findViewById(R.id.stay);
                    img_stay.setBackgroundResource(R.drawable.border);
                    break;
                case "stayshare":
                    ImageView img_stay_share = (ImageView)findViewById(R.id.stay_share);
                    img_stay_share.setBackgroundResource(R.drawable.border);
                    break;
                case "stayshareplay":
                    ImageView img_stay_share_play = (ImageView)findViewById(R.id.stay_share_play);
                    img_stay_share_play.setBackgroundResource(R.drawable.border);
                    break;
            }
        }

        //Email
        if (mOwner.getEmail() != null){
            TextView txt_email = (TextView)findViewById(R.id.email_user);
            String email = mOwner.getEmail();
            txt_email.setText(email);
        }

        //isIdentity ?
        if (mOwner.getIdentity() != null){

            //About
            if (mOwner.getIdentity().getAbout() != null){
                TextView txt_about_user = (TextView)findViewById(R.id.description_user);
                String description = mOwner.getIdentity().getAbout();
                txt_about_user.setText(description);
            }

            //Phone
            if (mOwner.getIdentity().getPhone() != null){
                TextView txt_phone = (TextView)findViewById(R.id.phone_user);
                String phone = mOwner.getIdentity().getPhone();
                txt_phone.setText(phone);
            }

            //Mobile Phone
            if (mOwner.getIdentity().getMobilePhone() != null){
                TextView txt_mobile = (TextView)findViewById(R.id.mobile_user);
                String mobile = mOwner.getIdentity().getMobilePhone();
                txt_mobile.setText(mobile);
            }

            //Address
            // TextView txt_address = (TextView)findViewById(R.id.address_user);

            //Birthday
            if (mOwner.getIdentity().getBirthday() != null){
                TextView txt_birthday = (TextView)findViewById(R.id.birthday_user);
                String birthday = mOwner.getIdentity().getBirthday();
                txt_birthday.setText(birthday);
            }
        }
    }

    private class GetPlace extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ProfilActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            //String jsonStr = sh.makeServiceCall(url);
            //mOwner = ParserJSON.getOwner(jsonStr);
            mOwner = mLogIn.getUser();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            CircleImageView img_avatar = (CircleImageView)findViewById(R.id.profile_image);
            TextView txt_name = (TextView)findViewById(R.id.name_user);
            CustomFontTextView txt_sport1 = (CustomFontTextView)findViewById(R.id.sport1);
            CustomFontTextView txt_sport2 = (CustomFontTextView)findViewById(R.id.sport2);
            CustomFontTextView txt_sport3 = (CustomFontTextView)findViewById(R.id.sport3);
            ImageView img_stay = (ImageView)findViewById(R.id.stay);
            ImageView img_stay_share = (ImageView)findViewById(R.id.stay_share);
            ImageView img_stay_share_play = (ImageView)findViewById(R.id.stay_share_play);
            TextView txt_about_user = (TextView)findViewById(R.id.description_user);
            TextView txt_email = (TextView)findViewById(R.id.email_user);
            TextView txt_phone = (TextView)findViewById(R.id.phone_user);
            TextView txt_mobile = (TextView)findViewById(R.id.mobile_user);
           // TextView txt_address = (TextView)findViewById(R.id.address_user);
            TextView txt_birthday = (TextView)findViewById(R.id.birthday_user);

            String user,avatarUrl="";
            if(!mOwner.getAvatar().isEmpty()){
                user = mOwner.get_id();
                avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/"+mOwner.getAvatar();
            }
            else{
                if (!mOwner.getFacebook().getAvatar().isEmpty()){
                    avatarUrl = mOwner.getFacebook().getAvatar();
                }
            }

            String name = mOwner.getIdentity().getFirstName() + " " +mOwner.getIdentity().getLastName();
            String engagement = mOwner.getEngagement();
            String description = mOwner.getIdentity().getAbout();
            String email = mOwner.getEmail();
            String phone = mOwner.getIdentity().getPhone();
            String mobile = mOwner.getIdentity().getMobilePhone();
            String birthday = mOwner.getIdentity().getBirthday();


            String[] hobbies = mOwner.getHobbies();

            txt_name.setText(name);
            switch (engagement){
                case "stay":
                    img_stay.setBackgroundResource(R.drawable.border);
                    break;
                case "stayshare":
                    img_stay_share.setBackgroundResource(R.drawable.border);
                    break;
                case "stayshareplay":
                    img_stay_share_play.setBackgroundResource(R.drawable.border);
                    break;
            }
            txt_about_user.setText(description);
            txt_email.setText(email);
            txt_phone.setText(phone);
            txt_mobile.setText(mobile);
            txt_birthday.setText(birthday);


            if (!avatarUrl.isEmpty()){
                Picasso.with(ProfilActivity.this).load(avatarUrl).fit().centerCrop().into(img_avatar);
            }

            for (int i=0; i<hobbies.length; i++){

                String stringName = "img_"+hobbies[i];
                stringName = stringName.replace("-","_");
                String strHobby = getStringResourceByName(stringName,ProfilActivity.this);

                switch (i){
                    case 0:
                        txt_sport1.setText(strHobby);
                        break;
                    case 1:
                        txt_sport2.setText(strHobby);
                        break;
                    case 2:
                        txt_sport3.setText(strHobby);
                        break;
                }

            }
        }
    }

    private String getStringResourceByName(String aString,Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

}
