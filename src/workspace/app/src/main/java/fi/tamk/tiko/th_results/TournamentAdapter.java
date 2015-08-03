
package fi.tamk.tiko.th_results;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * Custom adapter for using Tournament-objects inside a ListView.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class TournamentAdapter extends ArrayAdapter<Tournament> {
    ArrayList<Tournament> tournaments;

    public TournamentAdapter(Context context, int textViewResourceId, ArrayList<Tournament> tournaments) {
        super(context, textViewResourceId, tournaments);
        this.tournaments = tournaments;
    }
    @Override
    public Tournament getItem(int position) {

        Tournament tournament = tournaments.get(position);
        return tournament;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Log.d("TournamentAdapter", "testing");
        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.tournamentlist_item, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        Tournament tournament = tournaments.get(position);

        if (tournament != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView tt = (TextView) v.findViewById(R.id.toptext);
            TextView ttd = (TextView) v.findViewById(R.id.toptextdata);
            TextView mt = (TextView) v.findViewById(R.id.middletext);
            TextView mtd = (TextView) v.findViewById(R.id.middletextdata);
            TextView bt = (TextView) v.findViewById(R.id.bottomtext);
            TextView btd = (TextView) v.findViewById(R.id.desctext);
            long tournamentID = tournament.getId();
            v.setTag(tournamentID);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (tt != null){
                tt.setText("Name: ");
            }
            if (ttd != null){
                ttd.setText(tournament.getName());
            }
            if (mt != null){
                mt.setText("Participants: ");
            }
            if (mtd != null){
                mtd.setText(tournament.getPlayerCount()+"");
            }
            if (bt != null){
                bt.setText("Date: ");
            }
            if (btd != null){
                btd.setText(tournament.getDate());
            }
        }

        // the view must be returned to our activity
        return v;

    }
}