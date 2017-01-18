package fr.wildcodeschool.apprenti.sportihome.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.net.ssl.HttpsURLConnection;
import fr.wildcodeschool.apprenti.sportihome.Model.LogInModel;
import fr.wildcodeschool.apprenti.sportihome.ParserJSON;
import fr.wildcodeschool.apprenti.sportihome.R;

/**
 * Created by chantome on 09/01/2017.
 */

public class LogInActivity extends FragmentActivity {

    private TextView content;
    private EditText editEmail, editPassword;
    private String email, password,sport;
    private Button connect,signup,forgotPassword;
    private HttpURLConnection client;
    private LogInActivity.ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    private ViewPager viewPager;
    private boolean autoScroll=true;
    private int nbrPictures=3;
    private int count = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int[] pictures = new int[3];
        pictures[0] = getBaseContext().getResources().getIdentifier("biking", "mipmap", getBaseContext().getPackageName());
        pictures[1] = getBaseContext().getResources().getIdentifier("equitation", "mipmap", getBaseContext().getPackageName());
        pictures[2] = getBaseContext().getResources().getIdentifier("rando", "mipmap", getBaseContext().getPackageName());

        //Pictures
        if (pictures != null){

            imageFragmentPagerAdapter = new LogInActivity.ImageFragmentPagerAdapter(getSupportFragmentManager(),pictures);
            viewPager = (ViewPager) findViewById(R.id.home_pager);
            viewPager.setPageTransformer(false, new FadePageTransformer());
            viewPager.setAdapter(imageFragmentPagerAdapter);
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
            }, 500, 5000);
        }

        editEmail      =    (EditText)findViewById(R.id.login);
        editPassword       =   (EditText)findViewById(R.id.pass);

        connect = (Button)findViewById(R.id.connection);
        signup = (Button)findViewById(R.id.btn_signup);
        forgotPassword = (Button)findViewById(R.id.btn_forgot);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editEmail.getText().toString().isEmpty() && !editPassword.getText().toString().isEmpty()){
                    new SendPostLogin().execute();
                }
                if(editEmail.getText().toString().isEmpty()){
                    editEmail.setError("email vide !");
                }
                if(editPassword.getText().toString().isEmpty()){
                    editPassword.setError("mot de passe vide !");
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS=0;
        private int[] pictures;

        public ImageFragmentPagerAdapter(FragmentManager fm, int[] pictures) {
            super(fm);
            this.NUM_ITEMS = pictures.length;
            this.pictures = pictures;
        }

        @Override
        public int getCount() {
            return this.NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            LogInActivity.SwipeFragment fragment = new LogInActivity.SwipeFragment();
            return LogInActivity.SwipeFragment.newInstance(position, pictures[position]);
        }
    }

    public static class SwipeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            Log.i("YOLO","onCreatView");

            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            Context context = swipeView.getContext();

            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);

            Bundle bundle = getArguments();

            int position = bundle.getInt("position");
            int imageFile = bundle.getInt("id");

            //Picasso.with(context).load(imageFile).fit().centerCrop().into(imageView);
            imageView.setImageResource(imageFile);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //code
                }
            });

            return swipeView;

        }

        static LogInActivity.SwipeFragment newInstance(int position, int picture) {

            LogInActivity.SwipeFragment swipeFragment = new LogInActivity.SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putInt("id", picture);
            swipeFragment.setArguments(bundle);

            return swipeFragment;

        }
    }

    public class FadePageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {
            view.setTranslationX(view.getWidth() * -position);

            if(position <= -1.0F || position >= 1.0F) {
                view.setAlpha(0.0F);
            } else if( position == 0.0F ) {
                view.setAlpha(1.0F);
            } else {
                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                view.setAlpha(1.0F - Math.abs(position));
            }
        }
    }

    public class SendPostLogin extends AsyncTask<String, Void, String[]> {

        protected void onPreExecute(){
            email = editEmail.getText().toString();
            password = editPassword.getText().toString();
        }

        protected String[] doInBackground(String... arg0) {

            try {

                URL url = new URL("https://sportihome.com/api/users/login");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", email);
                postDataParams.put("password", password);

                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setDoOutput(true);
                client.setDoInput(true);

                OutputStream os = client.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=client.getResponseCode();
                String res[] = new String[2];
                InputStream input;

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    //RESPONSE OK
                    input = client.getInputStream();

                }
                else {
                    //RESPONSE ERROR
                    input = client.getErrorStream();

                }

                BufferedReader in=new BufferedReader(new InputStreamReader(input));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();

                res[0] = String.valueOf(responseCode);
                res[1] = sb.toString();

                return res;
            }
            catch(Exception e){
                Log.e("ERR : ",new String("Exception: " + e.getMessage()));
                return null;
            }

        }

        @Override
        protected void onPostExecute(String[] result) {
            if(result != null){
                switch (result[0]){
                    case "200":

                        final LogInModel monLog = ParserJSON.getLogIn(result[1]);
                        
                        if (monLog.isSuccess()){
                            //Connexion Ok - Go page Search
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LogInActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("id", monLog.getUser().get_id());
                            editor.putString("token", monLog.getToken());
                            editor.commit();

                            Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        break;
                    case "401":
                        Toast.makeText(getApplicationContext(), result[1], Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
