package fi.tamk.tiko.th_results;


import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * FragmentPageAdapter to create fragments inside ViewPager.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
	ArrayList<Fragment> mPageReferenceMap;
	
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        mPageReferenceMap = new ArrayList<Fragment>();
    }

    /**
     * Gets wanted adapter fragment
     *
     * @param index Index of fragment wanted
     * @return Selected fragment
     */
    @Override
    public Fragment getItem(int index) {

        TournamentFragment tf = new TournamentFragment();
        ScoresFragment sf = new ScoresFragment();
        StandingsFragment stf = new StandingsFragment();

        switch (index) {
        case 0:
            // Tournament fragment activity

            return tf;
        case 1:
            // Scores fragment activity

            return sf;
        case 2:
            // Standings fragment activity

            return stf;
        }
 
        return null;
    }

    /**
     * @return Count of tabs
     */
    @Override
    public int getCount() {

        return 3;
    }
 
}