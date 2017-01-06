package fr.wildcodeschool.apprenti.sportihome.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fr.wildcodeschool.apprenti.sportihome.Fragments.ProfilFragment;
import fr.wildcodeschool.apprenti.sportihome.Fragments.SearchFragment;
import fr.wildcodeschool.apprenti.sportihome.Fragments.SpotsFragment;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mHandler = new Handler();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOST;
            loadHostFragment();
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
}
