package fr.wildcodeschool.apprenti.sportihome.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.wildcodeschool.apprenti.sportihome.Font.CustomFontTextView;
import fr.wildcodeschool.apprenti.sportihome.HttpHandler;
import fr.wildcodeschool.apprenti.sportihome.Model.OwnerModel;
import fr.wildcodeschool.apprenti.sportihome.ParserJSON;
import fr.wildcodeschool.apprenti.sportihome.R;


/**
 * Created by edwin on 28/12/16.
 */

public class ProfilFragment extends Fragment {

    private ProgressDialog pDialog;
    private OwnerModel mOwner;
    private String url;
    View view;

    public ProfilFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profil, container, false);

        String user_id = "5821d43134323134576bc5cb";
        url = "https://sportihome.com/api/users/"+user_id+"/";

        return view;
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        new GetPlace().execute();

    }

    private class GetPlace extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            mOwner = ParserJSON.getOwner(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            CircleImageView img_avatar = (CircleImageView)view.findViewById(R.id.profile_image);
            TextView txt_name = (TextView)view.findViewById(R.id.name_user);
            CustomFontTextView txt_sport1 = (CustomFontTextView)view.findViewById(R.id.sport1);
            CustomFontTextView txt_sport2 = (CustomFontTextView)view.findViewById(R.id.sport2);
            CustomFontTextView txt_sport3 = (CustomFontTextView)view.findViewById(R.id.sport3);
            ImageView img_stay = (ImageView)view.findViewById(R.id.stay);
            ImageView img_stay_share = (ImageView)view.findViewById(R.id.stay_share);
            ImageView img_stay_share_play = (ImageView)view.findViewById(R.id.stay_share_play);
            TextView txt_about_user = (TextView)view.findViewById(R.id.description_user);
            TextView txt_email = (TextView)view.findViewById(R.id.email_user);
            TextView txt_phone = (TextView)view.findViewById(R.id.phone_user);
            TextView txt_mobile = (TextView)view.findViewById(R.id.mobile_user);
            // TextView txt_address = (TextView)findViewById(R.id.address_user);
            TextView txt_birthday = (TextView)view.findViewById(R.id.birthday_user);

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
                Picasso.with(getActivity()).load(avatarUrl).fit().centerCrop().into(img_avatar);
            }

            for (int i=0; i<hobbies.length; i++){

                String stringName = "img_"+hobbies[i];
                stringName = stringName.replace("-","_");
                String strHobby = getStringResourceByName(stringName,getActivity());

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

