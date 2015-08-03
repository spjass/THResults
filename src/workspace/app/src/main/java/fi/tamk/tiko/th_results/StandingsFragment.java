package fi.tamk.tiko.th_results;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

import fi.tamk.tiko.th_results.database.DataSource;
import fi.tamk.tiko.th_results.database.Loader;
import fi.tamk.tiko.th_results.dropbox.HTMLGenerator;
import fi.tamk.tiko.th_results.tournament.Player;
import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * StandingsFragment inflates layout defined in fragment_standings.xml
 *
 * This fragment holds the third fragment layout in the view pager. Tournament standings
 * are generated in this fragment.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class StandingsFragment extends Fragment {
    TableLayout tableLayout;
    Tournament tournament;
    StandingsManager sm;
    ArrayList<Integer> containerIDs;
    View rootView;
    boolean empty;
    int containerID;
    HTMLGenerator gen;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.fragment_standings, container, false);
        tableLayout = (TableLayout) rootView.findViewById(R.id.standingsTableLayout);
        containerIDs = new ArrayList<Integer>();
        empty = true;
        containerID = 8;

        return rootView;
    }

    /**
     * Method communicates with MainActivity, used to create a new Standings Manager and update
     * standings table.
     *
     * @param tournament Tournament to create standings for
     */
    public void sendTournament(Tournament tournament) {
        this.tournament = tournament;
       // gen = new HTMLGenerator(getActivity().getApplicationContext(), "testfile.html", tournament);
        Log.d("StandingsFragment", "Tournament sent");
        sm = new StandingsManager(tournament);
        updateTable();

    }

    /**
     * Generates standings tables.
     */
    public void updateTable() {
        if (!empty) {
            clearUI();
        }
        empty = false;
        this.tournament = sm.calculatePoints();
        ArrayList<Player> players = tournament.getPlayers();
        int placement = 1;
        int IDmultiplier = 10;
        containerIDs.clear();
        int height = 75;
        for (int i = players.size() -1; i >= 0; i--) {

            Player player = players.get(i);
            if (!player.isGhostPlayer()) {
                TableRow tableRow = new TableRow(getActivity().getBaseContext());
                TableRow.LayoutParams paramsContainer = new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, height);

                tableRow.setLayoutParams(paramsContainer);
                containerID = containerID + (IDmultiplier * i);
                containerIDs.add(containerID);
                tableRow.setId(containerID);
                tableRow.setBackgroundColor(getResources().getColor(R.color.darkgray));

                final TextView nameTextView = new TextView(getActivity().getBaseContext());
                nameTextView.setId(1);
                TableRow.LayoutParams paramsNameText = new TableRow.LayoutParams(0, height, 0.6f);
                paramsNameText.gravity=Gravity.RIGHT;
                nameTextView.setGravity(Gravity.CENTER);
                nameTextView.setLayoutParams(paramsNameText);

                nameTextView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
                nameTextView.setText(player.getName());


                TextView standingTextView = new TextView(getActivity().getBaseContext());
                standingTextView.setId(1);
                TableRow.LayoutParams paramsStandingText = new TableRow.LayoutParams(0, height, 0.1f);
                standingTextView.setGravity(Gravity.CENTER);
                standingTextView.setLayoutParams(paramsStandingText);
                standingTextView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
                standingTextView.setText(placement+"");



                TextView gamesTextView = new TextView(getActivity().getBaseContext());
                gamesTextView.setId(2);
                TableRow.LayoutParams paramsGamesText = new TableRow.LayoutParams(0, height, 0.2f);
                gamesTextView.setGravity(Gravity.CENTER);
                gamesTextView.setLayoutParams(paramsGamesText);
                gamesTextView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
                gamesTextView.setText(player.getMatches() + "");

                TextView winsTextView = new TextView(getActivity().getBaseContext());
                winsTextView.setId(3);
                TableRow.LayoutParams paramsWinsText = new TableRow.LayoutParams(0, height, 0.2f);
                winsTextView.setGravity(Gravity.CENTER);
                winsTextView.setLayoutParams(paramsWinsText);
                winsTextView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
                winsTextView.setText(player.getWins() + "");

                TextView drawsTextView = new TextView(getActivity().getBaseContext());
                drawsTextView.setId(4);
                TableRow.LayoutParams paramsDrawsText = new TableRow.LayoutParams(0, height, 0.2f);
                drawsTextView.setGravity(Gravity.CENTER);
                drawsTextView.setLayoutParams(paramsDrawsText);
                drawsTextView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
                drawsTextView.setText(player.getDraws() + "");

                TextView lossesTextView = new TextView(getActivity().getBaseContext());
                lossesTextView.setId(5);
                TableRow.LayoutParams paramsLossesText = new TableRow.LayoutParams(0, height, 0.2f);
                lossesTextView.setGravity(Gravity.CENTER);
                lossesTextView.setLayoutParams(paramsLossesText);
                lossesTextView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
                lossesTextView.setText(player.getLosses() + "");

                TextView goalsTextView = new TextView(getActivity().getBaseContext());
                goalsTextView.setId(6);
                TableRow.LayoutParams paramsGoalsText = new TableRow.LayoutParams(0, height, 0.3f);
                goalsTextView.setGravity(Gravity.CENTER);
                goalsTextView.setLayoutParams(paramsGoalsText);
                goalsTextView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
                goalsTextView.setText(player.getGoalsDone() + "-" + player.getGoalsAgainst());

                TextView pointsTextView = new TextView(getActivity().getBaseContext());
                nameTextView.setId(7);
                TableRow.LayoutParams paramsPointsText = new TableRow.LayoutParams(0, height, 0.2f);
                pointsTextView.setGravity(Gravity.CENTER);
                paramsPointsText.gravity=Gravity.RIGHT;
                pointsTextView.setLayoutParams(paramsPointsText);
                pointsTextView.setBackground(getResources().getDrawable(R.drawable.cell_shape));
                pointsTextView.setText(player.getPoints() + "");

                tableRow.addView(standingTextView);
                tableRow.addView(nameTextView);
                tableRow.addView(gamesTextView);
                tableRow.addView(winsTextView);
                tableRow.addView(drawsTextView);
                tableRow.addView(lossesTextView);
                tableRow.addView(goalsTextView);
                tableRow.addView(pointsTextView);
                tableLayout.addView(tableRow);
                placement++;
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            DataSource ds = new DataSource(getActivity());
            try {
                ds.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            long tournamentId = savedInstanceState.getLong("tournamentId");
            Log.d("StandingsFragment", "onActivityCreated " + tournamentId+"");

            Loader loader = new Loader(getActivity().getApplicationContext());
            this.tournament = loader.getTournamentAndRounds(tournamentId);

            if (tournament.getRounds().length > 0) {
                sm = new StandingsManager(tournament);
                updateTable();
            } else {
                Log.d("StandingsFragment", "getRounds = 0");
            }
        } else {
            Log.d("StandingsFragment", "temp id");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tournament != null) {
            outState.putLong("tournamentId", tournament.getId());
            Log.d("ScoresFragment", "onSaveInstanceState " + tournament.getId());
        }
    }
    /**
     * Clears all generated content from the UI.
     */
    public void clearUI() {

            tableLayout.removeAllViews();

    }
 
}