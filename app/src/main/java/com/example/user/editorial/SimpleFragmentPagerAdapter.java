package com.example.user.editorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[]{"THE HINDU", "INDEPENDENT", "NEW YORK TIMES"};

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TheHinduFragment();
        } else if (position == 1) {
            return new TheGuardianFragment();
        } else if (position == 2){
            return new TimesOfIndiaFragment();
        }
        else
            return new TheHinduFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return 3;
    }
}
