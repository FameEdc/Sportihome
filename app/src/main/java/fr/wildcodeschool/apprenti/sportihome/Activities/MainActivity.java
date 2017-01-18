package fr.wildcodeschool.apprenti.sportihome.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.net.HttpURLConnection;
import java.net.URL;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.wildcodeschool.apprenti.sportihome.Fragments.ProfilFragment;
import fr.wildcodeschool.apprenti.sportihome.Fragments.SearchFragment;
import fr.wildcodeschool.apprenti.sportihome.Fragments.SpotsFragment;
import fr.wildcodeschool.apprenti.sportihome.HttpHandler;
import fr.wildcodeschool.apprenti.sportihome.Model.OwnerModel;
import fr.wildcodeschool.apprenti.sportihome.ParserJSON;
import fr.wildcodeschool.apprenti.sportihome.R;

/**
 * Created by edwin on 28/12/16.
 */

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    public static int navItemIndex = 0;
    private static final String TAG_HOST = "Hébergements";
    private static final String TAG_SPOTS = "Spots";
    private static final String TAG_PROFIL = "Profil";
    public static String CURRENT_TAG = TAG_HOST;
    public final static int HOME_TAB = 0;
    public final static int SPOT_TAB = 1;
    private String[] activityTitles;
    private boolean shouldLoadHostFragOnBackPress = true;
    private Handler mHandler;
    Bundle sIS;
    OwnerModel mOwner;
    String log_token,log_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sIS = savedInstanceState;

        //Check network and user logged in...
        if (isOnline()){
            //If user not logged, start LogIn Activity and finish() this Activity
            SendPostLoggedIn logged = new SendPostLoggedIn();
            logged.execute();
        }else{
            Toast.makeText(getApplicationContext(), "Aucun acces à internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadHostFragment() {

        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            mDrawer.closeDrawers();

        }

        Runnable mPendingRunnable = new Runnable() {

            @Override
            public void run() {

                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        mDrawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        Bundle bundle = new Bundle();
        SearchFragment searchFragment = new SearchFragment();
        switch (navItemIndex) {
            case 0:
                bundle.putInt("tab",HOME_TAB);
                searchFragment.setArguments(bundle);
                return searchFragment;
            case 1:
                bundle.putInt("tab",SPOT_TAB);
                searchFragment.setArguments(bundle);
                return searchFragment;
            case 2:
                ProfilFragment profilFragment= new ProfilFragment();
                profilFragment.setArguments(bundle);
                return profilFragment;

            default:
                return new SearchFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {

        //Set Header
        if (mOwner != null){

            Context context = getBaseContext();

            //Avatar
            CircleImageView imgAvatar = (CircleImageView) navigationView.findViewById(R.id.profile_image);
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

            TextView pseudo = (TextView) navigationView.findViewById(R.id.pseudo);
            pseudo.setText(mOwner.getIdentity().getFirstName()+" "+mOwner.getIdentity().getLastName());

        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.nav_hébergements:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOST;
                        break;
                    case R.id.nav_spots:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_SPOTS;
                        break;
                    case R.id.nav_profil:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PROFIL;
                        break;

                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.nav_cgu:
                        startActivity(new Intent(MainActivity.this, CguActivity.class));
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.nav_legal:
                        startActivity(new Intent(MainActivity.this, LegalDisclaimerActivity.class));
                        mDrawer.closeDrawers();
                        return true;

                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHostFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawer.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
            return;
        }

        if (shouldLoadHostFragOnBackPress) {

            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOST;
                loadHostFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        Context mContext = getBaseContext();
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean isLogged() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        log_token = preferences.getString("token",null);
        log_id = preferences.getString("id",null);

        if(log_token !=null){
            return true;
        }else{
            return false;
        }
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
                        Toast.makeText(getApplicationContext(), result[1], Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,LogInActivity.class);
                        startActivity(intent);
                        finish();
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

                        mToolbar= (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(mToolbar);

                        mHandler = new Handler();

                        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        navigationView = (NavigationView) findViewById(R.id.nav_view);

                        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

                        setUpNavigationView();

                        if (sIS == null) {
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_HOST;
                            loadHostFragment();
                        }

                        break;
                    case "401":
                        Toast.makeText(getApplicationContext(), result[1], Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,LogInActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        }

    }
}
