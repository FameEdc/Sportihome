package fr.wildcodeschool.apprenti.sportihome.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.wildcodeschool.apprenti.sportihome.Activities.LogInActivity;
import fr.wildcodeschool.apprenti.sportihome.Activities.MainActivity;
import fr.wildcodeschool.apprenti.sportihome.Font.CustomFontTextView;
import fr.wildcodeschool.apprenti.sportihome.HttpHandler;
import fr.wildcodeschool.apprenti.sportihome.Model.OwnerModel;
import fr.wildcodeschool.apprenti.sportihome.ParserJSON;
import fr.wildcodeschool.apprenti.sportihome.R;


public class ProfilFragment extends Fragment {

    private OwnerModel mOwner;
    private View view;
    String log_id,log_token;
    public ProfilFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Check network and user logged in...
        if (isOnline()){
            //If user not logged, start LogIn Activity and finish() this Activity
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_profil, container, false);

            new SendPostLoggedIn().execute();
        }else{
            Toast.makeText(getContext(), "Aucun acces à internet", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private String getStringResourceByName(String aString,Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    public boolean isLogged() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        log_token = preferences.getString("token", null);
        log_id = preferences.getString("id", null);

        if (log_token != null) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isOnline() {
        Context mContext = getContext();
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class SendPostLoggedIn extends AsyncTask<Void, Void, String[]> {

        protected void onPreExecute(){}

        protected String[] doInBackground(Void... arg0) {

            try {
                URL url = new URL("https://sportihome.com/api/users/loggedin");
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");
                if(isLogged() == true){
                    client.setRequestProperty("Authorization",log_token);
                }
                client.connect();

                // read the response
                String[] response = new String[2];
                response[0] = String.valueOf(client.getResponseCode());
                response[1] = client.getResponseMessage();

                return response;

            }catch (Exception e) {
                Log.e("ERR", "Exception: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {

            if(result != null){
                switch (result[0]){
                    case "200":
                        //CONNECTER !!!

                        new SendPostOwner().execute();

                        break;
                    case "401":
                        Toast.makeText(getContext(), result[1], Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),LogInActivity.class);
                        startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                }
            }
        }

    }

    public class SendPostOwner extends AsyncTask<Void, Void, String[]> {

        protected void onPreExecute(){}

        protected String[] doInBackground(Void... arg0) {

            try {
                URL url = new URL("https://sportihome.com/api/users/"+log_id);
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");
                client.connect();

                // read the response
                String[] response = new String[2];
                response[0] = String.valueOf(client.getResponseCode());
                HttpHandler sh = new HttpHandler();
                String jsonStr = sh.makeServiceCall("https://sportihome.com/api/users/"+log_id);
                response[1] = jsonStr;

                return response;

            }catch (Exception e) {
                Log.e("ERR", "Exception: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {

            if(result != null){
                switch (result[0]){
                    case "200":

                        mOwner = ParserJSON.getOwner(result[1]);

                        final Context context = view.getContext();

                        //Avatar
                        CircleImageView imgAvatar = (CircleImageView) view.findViewById(R.id.profile_image);
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



                        //Sports Podium
                        if (mOwner.getHobbies() != null){
                            String[] hobbies = mOwner.getHobbies();
                            for (int i=0; i<hobbies.length; i++){

                                String stringName = "img_"+hobbies[i];
                                stringName = stringName.replace("-","_");
                                String strHobby = getStringResourceByName(stringName,getActivity());

                                switch (i){
                                    case 0:
                                        CustomFontTextView txt_sport1 = (CustomFontTextView)view.findViewById(R.id.sport1);
                                        txt_sport1.setText(strHobby);
                                        break;
                                    case 1:
                                        CustomFontTextView txt_sport2 = (CustomFontTextView)view.findViewById(R.id.sport2);
                                        txt_sport2.setText(strHobby);
                                        break;
                                    case 2:
                                        CustomFontTextView txt_sport3 = (CustomFontTextView)view.findViewById(R.id.sport3);
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
                                    ImageView img_stay = (ImageView)view.findViewById(R.id.stay);
                                    img_stay.setBackgroundResource(R.drawable.border);
                                    break;
                                case "stayshare":
                                    ImageView img_stay_share = (ImageView)view.findViewById(R.id.stay_share);
                                    img_stay_share.setBackgroundResource(R.drawable.border);
                                    break;
                                case "stayshareplay":
                                    ImageView img_stay_share_play = (ImageView)view.findViewById(R.id.stay_share_play);
                                    img_stay_share_play.setBackgroundResource(R.drawable.border);
                                    break;
                            }
                        }

                        //Email
                        if (mOwner.getEmail() != null){
                            TextView txt_email = (TextView)view.findViewById(R.id.email_user);
                            String email = mOwner.getEmail();
                            txt_email.setText(email);
                        }

                        //isIdentity ?
                        if (mOwner.getIdentity() != null){

                            String name = "";
                            TextView txt_name = (TextView)view.findViewById(R.id.name_user);
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

                            //About
                            if (mOwner.getIdentity().getAbout() != null){
                                TextView txt_about_user = (TextView)view.findViewById(R.id.description_user);
                                String description = mOwner.getIdentity().getAbout();
                                txt_about_user.setText(description);
                            }

                            //Phone
                            if (mOwner.getIdentity().getPhone() != null){
                                TextView txt_phone = (TextView)view.findViewById(R.id.phone_user);
                                String phone = mOwner.getIdentity().getPhone();
                                txt_phone.setText(phone);
                            }

                            //Mobile Phone
                            if (mOwner.getIdentity().getMobilePhone() != null){
                                TextView txt_mobile = (TextView)view.findViewById(R.id.mobile_user);
                                String mobile = mOwner.getIdentity().getMobilePhone();
                                txt_mobile.setText(mobile);
                            }

                            //Address
                            // TextView txt_address = (TextView)findViewById(R.id.address_user);

                            //Birthday
                            if (mOwner.getIdentity().getBirthday() != null){
                                TextView txt_birthday = (TextView)view.findViewById(R.id.birthday_user);
                                String birthday = mOwner.getIdentity().getBirthday();
                                txt_birthday.setText(birthday);
                            }
                        }

                        Button logout = (Button) view.findViewById(R.id.logout);

                        logout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Context context = v.getContext();
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("id", null);
                                editor.putString("token", null);
                                editor.commit();

                                //getActivity().getSupportFragmentManager().popBackStack();
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        });

                        break;
                    case "401":
                        Toast.makeText(getContext(), result[1], Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),LogInActivity.class);
                        startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                }
            }
        }

    }
}

