package com.example.riesz.chemicalequationbalancer;

import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BalanceEquationFragment.OnFragmentInteractionListener{
    ChemUtilsPagerAdapter mChemUtilsPagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;
    private int defaultTabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChemUtilsPagerAdapter = new ChemUtilsPagerAdapter(
                        getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mChemUtilsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); ++i){
            if (i != defaultTabPosition){
                LinearLayout tabViewGroup = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
                TextView tabTextView = (TextView) tabViewGroup.getChildAt(1);
                tabTextView.setAlpha(0.5f);
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LinearLayout tabViewGroup = (LinearLayout)
                        ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabViewGroup.getChildAt(1);
                tabTextView.setAlpha(1.0f);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout tabViewGroup = (LinearLayout)
                        ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabViewGroup.getChildAt(1);
                tabTextView.setAlpha(0.5f);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }
}