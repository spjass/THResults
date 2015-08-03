package fi.tamk.tiko.th_results;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * MainActivity of the application.
 *
 * This activity is used to hold a view pager which holds all the fragments.
 * MainActivity also communicates with other fragments.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class MainActivity extends FragmentActivity implements
	ActionBar.TabListener, Communicator {
	  	private ViewPager viewPager;
	    private TabsPagerAdapter mAdapter;
	    private ActionBar actionBar;
	    private String[] tabs = { "Tournament", "Scores", "Standings" };
        ShareActionProvider mShareActionProvider;

    Tournament tournament;

    /**
     * onCreate() method of MainActivity
     *
     * Sets the ViewPager that holds fragments.
     *
     * @param savedInstanceState Saved bundle
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fm = getSupportFragmentManager();
		 viewPager = (ViewPager) findViewById(R.id.pager);
	     actionBar = getActionBar();
	     mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
	     int index = viewPager.getCurrentItem();
	     viewPager.setAdapter(mAdapter);
	     viewPager.setOffscreenPageLimit(2);
	     actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

	     // Adding Tabs
	     for (String tab_name : tabs) {
	         actionBar.addTab(actionBar.newTab().setText(tab_name)
	                 .setTabListener(this));
	     }
		
	     viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	    	 
	    	    @Override
	    	    public void onPageSelected(int position) {
	    	        actionBar.setSelectedNavigationItem(position);
	    	    }
	    	 
	    	    @Override
	    	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    	    }
	    	 
	    	    @Override
	    	    public void onPageScrollStateChanged(int arg0) {
	    	    }
	    	});
	}

    /**
     * Gets a desired fragment using its position in the ViewPager
     *
     * @param position Tab position
     * @return Wanted fragment
     */
    public Fragment findFragmentByPosition(int position) {
        TabsPagerAdapter fragmentPagerAdapter = mAdapter;
        return getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + viewPager.getId() + ":"
                        + fragmentPagerAdapter.getItemId(position));
    }


    @Override
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		viewPager.setCurrentItem(tab.getPosition());	// TODO Auto-generated method stub
		
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

    /**
     * Communicator interfaces method for communicating between MainActivity and fragments.
     *
     * @param tournament Tournament to send to fragment
     * @param fragmentPosition position of fragment to communicate with
     * @param isNew True if tournament is new.
     * @return Tournament object
     */
    @Override
    public void respond(Tournament tournament, int fragmentPosition, boolean isNew) {
        Fragment tempFragment = findFragmentByPosition(fragmentPosition);

        if (fragmentPosition == 1) {
            ScoresFragment scoresFragment;
            scoresFragment = (ScoresFragment) tempFragment;
            scoresFragment.updateUI(tournament, isNew);
        } else if (fragmentPosition == 2) {
            Log.d("MainActivity", "fragment pos 2");
            StandingsFragment standingsFragment;
            standingsFragment = (StandingsFragment) tempFragment;
            standingsFragment.sendTournament(tournament);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dropbox:
                Intent intent = new Intent(this, DropboxActivity.class);
                startActivity(intent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }






}
