package com.example.riesz.chemicalequationbalancer;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by tensor on 8/17/2017.
 */

public class ChemUtilsPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public ChemUtilsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new BalanceEquationFragment();
            case 1:
                return new BalanceEquationFragment(); // TODO: return actual view
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) { // TODO: store string values
        switch(position) {
            case 0:
                return context.getResources().getString(
                        R.string.balance_page_title);
            case 1:
                return context.getResources().getString(
                        R.string.constants_and_equations_page_title);
            default:
                return null;
        }
    }
}