package io.github.erfz.chemtool;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        ChemUtilsFragment.OnFragmentInteractionListener,
        ConstantsEquationsFragment.OnFragmentInteractionListener,
        ConstantsListFragment.OnFragmentInteractionListener {
    private static final int NUM_ITEMS = 2;
    MainFragmentPagerAdapter pagerAdapter;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && viewPager.getCurrentItem() != 0) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    private class MainFragmentPagerAdapter extends FragmentPagerAdapter {
        MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ChemUtilsFragment();
                case 1:
                    return new ConstantsListFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.balance_page_title);
                case 1:
                    return getResources().getString(R.string.constants_equations_page_title);
                default:
                    return null;
            }
        }
    }
}