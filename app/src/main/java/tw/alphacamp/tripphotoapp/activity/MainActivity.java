package tw.alphacamp.tripphotoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.astuetz.PagerSlidingTabStrip;
import tw.alphacamp.tripphotoapp.R;
import tw.alphacamp.tripphotoapp.fragment.AlbumFragment;
import tw.alphacamp.tripphotoapp.fragment.MapFragment;
import tw.alphacamp.tripphotoapp.fragment.PlaceholderFragment;
import tw.alphacamp.tripphotoapp.fragment.SpotFragment;


public class MainActivity extends ActionBarActivity {

    private ViewPager viewPager;

    private MainPagerAdapter mainPagerAdapter;

    private PagerSlidingTabStrip tabs;

    private static final int INTENT_POST_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_action_map);
        }

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(mainPagerAdapter);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setBackgroundResource(R.color.colorPrimary);
        tabs.setIndicatorHeight(15);
        tabs.setIndicatorColorResource(R.color.white);
        tabs.setDividerColorResource(android.R.color.transparent);
        tabs.setDividerWidth(2);
        tabs.setDividerPadding(20);
        tabs.setShouldExpand(true);

        tabs.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_camera) {
            Intent cameraIntent = new Intent(MainActivity.this, PostActivity.class);
            startActivityForResult(cameraIntent, INTENT_POST_PICTURE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_POST_PICTURE && resultCode == RESULT_OK) {

        }
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {
                getString(R.string.title_grid),
                getString(R.string.title_list),
                getString(R.string.title_map),
                getString(R.string.tab_blank_section)
        };

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new PlaceholderFragment();
            switch (position) {
                case 0:
                    fragment = new AlbumFragment();
                    break;
                case 1:
                    fragment = new SpotFragment();
                    break;
                case 2:
                    fragment = new MapFragment();
                    break;
            }
            return fragment;
        }

    }




}
