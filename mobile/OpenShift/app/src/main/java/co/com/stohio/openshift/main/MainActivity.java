package co.com.stohio.openshift.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import co.com.stohio.openshift.R;

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
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_event_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_people_white_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_pets_white_24dp);

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
//        private String tabTitles[] = new String[] {"donger", "Events", "Social", "Showcase" };
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 0: return HomeFragment.newInstance("Home");
                case 1: return EventsFragment.newInstance("Events");
                case 2: return SocialFragment.newInstance("Social");
                case 3: return DemoFragment.newInstance("Showcase");
                default: return SocialFragment.newInstance("Default");
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
//        @Override
//        public CharSequence getPageTitle(int position) {
//            // Generate title based on item position
//            return tabTitles[position];
//        }
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}

