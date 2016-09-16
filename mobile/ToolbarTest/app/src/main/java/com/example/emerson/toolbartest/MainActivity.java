package com.example.emerson.toolbartest;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
//        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
//                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] { "Home", "Events", "Social" };
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 0: return HomeFragment.newInstance("Home");
                case 1: return EventsFragment.newInstance("Events");
                case 2: return SocialFragment.newInstance("Social");
                default: return SocialFragment.newInstance("Default");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

//    private List createList(int size) {
//        List result = new ArrayList();
//        for (int i=1; i <= size; i++) {
//            ContactInfo ci = new ContactInfo();
//            ci.name = ContactInfo.NAME_PREFIX + i;
//            ci.surname = ContactInfo.SURNAME_PREFIX + i;
//            ci.email = ContactInfo.EMAIL_PREFIX + i + "@test.com";
//
//            result.add(ci);
//
//        }
//
//        return result;
//
//    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    static class Adapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragments = new ArrayList<>();
//        private final List<String> mFragmentTitles = new ArrayList<>();
//
//        public Adapter(FragmentManager fm) { super(fm); }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragments.add(fragment);
//            mFragmentTitles.add(title);
//        }
//
//        @Override
//        public Fragment getItem(int position) { return mFragments.get(position); }
//
//        @Override
//        public int getCount() {
//            return mFragments.size();
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitles.get(position);
//        }
//    }

}
