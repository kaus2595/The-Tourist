package com.dexter.tourist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dexter.tourist.MainTab.BotFragment;
import com.dexter.tourist.MainTab.GuideFragment;
import com.dexter.tourist.MainTab.ReviewFragment;


/**
 * Created by Honey Sharma.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                GuideFragment guideFragment=new GuideFragment();
                return  guideFragment;

            case 1:
                //MyPostFragment myPostFragment = new MyPostFragment();
                BotFragment botFragment=new BotFragment();
                return botFragment;

            case 2:
                //NewsFragment newsFragment = new NewsFragment();
                ReviewFragment reviewFragment=new ReviewFragment();
                return  reviewFragment;

            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "Guides";

            case 1:
                return "Bot";

            case 2:
                return "Review";

            default:
                return null;
        }

    }

}
