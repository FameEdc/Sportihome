package fr.wildcodeschool.apprenti.sportihome.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import fr.wildcodeschool.apprenti.sportihome.Activities.LogInActivity;
import fr.wildcodeschool.apprenti.sportihome.Model.LogInModel;
import fr.wildcodeschool.apprenti.sportihome.R;

/**
 * Created by edwin on 05/12/16.
 */

public class SearchFragment extends Fragment {

    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    public static int int_items = 2;
    private View mView;
    private LogInModel mLogIn;
    private int[] tabIcons = {
            R.drawable.ic_tabhost,
            R.drawable.ic_tabspot
    };

    public SearchFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLogIn = (LogInModel) getArguments().getSerializable("login");

        mView = inflater.inflate(R.layout.fragment_search, null);

        tabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        mViewPager = (ViewPager) mView.findViewById(R.id.viewpager);

        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(mViewPager);
                setupTabIcons();

            }
        });

        return mView;

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(getArguments().getInt("tab")).select();
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PlacesFragment();
                case 1:
                    return new SpotsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Hébergements";
                case 1:
                    return "Spots";

            }
            return null;
        }
    }
}
