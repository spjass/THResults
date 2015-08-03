package fi.tamk.tiko.th_results;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

import fi.tamk.tiko.th_results.database.DataSource;
import fi.tamk.tiko.th_results.database.Loader;
import fi.tamk.tiko.th_results.tournament.Game;
import fi.tamk.tiko.th_results.tournament.Player;
import fi.tamk.tiko.th_results.tournament.Round;
import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * ScoresFragment inflates layout defined in fragment_scores.xml
 *
 * This fragment holds the second fragment layout in the view pager. Match tables
 * are generated in this fragment.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class ScoresFragment extends Fragment implements View.OnClickListener {
    LinearLayout layout;
    LinearLayout rootLayout;
    int activeRound;
    ImageButton plusButton;
    ImageButton minusButton;
    Button saveRoundButton;
    TextView roundTextView;
    Round[] rounds;
    Tournament tournament;
    View rootView;
    ArrayList<Integer> containerIDs;
    ArrayList<Integer> awayScoreIDs;
    ArrayList<Integer> homeScoreIDs;
    int multiplier = 5;
    int containerId;
    int homeScoreId;
    int awayScoreId;
    public static final int TEMP_ID = 10000;
    Loader loader;
    Communicator comm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.fragment_scores, container, false);
        rootLayout = (LinearLayout) rootView.findViewById(R.id.scoresLinearLayoutContainer);
        layout = (LinearLayout) rootView.findViewById(R.id.scoresLinearLayout);
        /*
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity= Gravity.CENTER_HORIZONTAL;

        layout.setLayoutParams(params);
        */
        multiplier = 10;
        containerId = 7;
        homeScoreId=4;
        awayScoreId=5;
        plusButton = (ImageButton) rootView.findViewById(R.id.plusButton);
        minusButton= (ImageButton) rootView.findViewById(R.id.minusButton);
        saveRoundButton = (Button) rootView.findViewById(R.id.saveRoundButton);
        roundTextView = (TextView) rootView.findViewById(R.id.roundTextView);
        containerIDs = new ArrayList<Integer>();
        homeScoreIDs = new ArrayList<Integer>();
        awayScoreIDs = new ArrayList<Integer>();
        this.tournament = new Tournament();
        tournament.setId(TEMP_ID);
        loader = new Loader(getActivity().getBaseContext());
        plusButton.setOnClickListener(this);
        minusButton.setOnClickListener(this);
        saveRoundButton.setOnClickListener(this);

        activeRound = 1;
        //textView = (TextView) rootView.findViewById(R.id.textViewScores);
        return rootLayout;
    }
    /**
     * Method communicates with MainActivity and is used to initialize and create
     * a match table for the tournament
     *
     * @param newTournament Tournament to use for match table generation.
     * @param isNew True if a tournament is new, false if not
     */
    public void updateUI(Tournament newTournament, boolean isNew) {


        this.tournament = newTournament;
        activeRound=1;
        rounds = tournament.getRounds();

        if (!isNew) {
            clearUI();
            containerIDs.clear();
            awayScoreIDs.clear();
            homeScoreIDs.clear();
            saveAndInitAllRounds();
        }

        clearUI();
        containerIDs.clear();
        awayScoreIDs.clear();
        homeScoreIDs.clear();

        createMatchTable(activeRound);

        roundTextView.setText("Round: " + activeRound);

    }
    /**
     * Saves status of all rounds to be saved
     */
    public void saveAndInitAllRounds() {

        for (int i = 0; i < tournament.getRounds().length; i++) {
            tournament.getRounds()[i].setRoundSaved(true);
        }
    }



    /**
     * Generates match tables of the currently active tournament
     *
     * @param roundNumber Current round number
     */
    public void createMatchTable(int roundNumber) {

        rounds = tournament.getRounds();
        Log.d("ScoresFragment", rounds[0].getGames()[0].getAwayPlayer().getName());
        Game[] games = rounds[roundNumber-1].getGames();
        LinearLayout linearContainerTop = new LinearLayout(getActivity().getBaseContext());
        LinearLayout.LayoutParams paramsContainerTop = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearContainerTop.setLayoutParams(paramsContainerTop);
        containerId = containerId + multiplier;
        containerIDs.add(containerId);
        linearContainerTop.setId(containerId);
        linearContainerTop.setBackgroundColor(getResources().getColor(R.color.darkgray));
        linearContainerTop.setOrientation(LinearLayout.VERTICAL);


        for (int i = 0; i < games.length; i++) {

            int id = i;
            Player homePlayer = games[i].getHomePlayer();
            Player awayPlayer = games[i].getAwayPlayer();



            LinearLayout linearContainer = new LinearLayout(getActivity().getBaseContext());
            LinearLayout.LayoutParams paramsContainer = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            linearContainer.setLayoutParams(paramsContainer);
            linearContainer.setId(6);
            linearContainer.setBackgroundColor(getResources().getColor(R.color.darkgray));
            linearContainer.setOrientation(LinearLayout.HORIZONTAL);

            TextView homeText = new TextView(getActivity().getBaseContext());
            homeText.setId(1);
            LinearLayout.LayoutParams paramsHomeText = new LinearLayout.LayoutParams(0, 130, 0.5f);
            homeText.setGravity(Gravity.CENTER);
            paramsHomeText.setMargins(10, 10, 10, 0);
            homeText.setLayoutParams(paramsHomeText);
            homeText.setText(homePlayer.getName());
            homeText.setTextSize(18);


            TextView midText = new TextView(getActivity().getBaseContext());
            LinearLayout.LayoutParams paramsMidText = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.1f);
            paramsMidText.setMargins(10, 10, 10, 0);

            midText.setGravity(Gravity.CENTER);
            midText.setLayoutParams(paramsMidText);
            midText.setId(3);
            midText.setText("-");


            LinearLayout.LayoutParams paramsAwayText = new LinearLayout.LayoutParams(0, 130, 0.5f);
            TextView awayText = new TextView(getActivity().getBaseContext());
            awayText.setId(2);
            awayText.setText(awayPlayer.getName());
            awayText.setTextSize(18);
            paramsAwayText.setMargins(10, 10, 10, 0);

            awayText.setLayoutParams(paramsAwayText);
            awayText.setGravity(Gravity.CENTER);


            EditText homeScore = new EditText(getActivity().getBaseContext());
            homeScoreId = homeScoreId + multiplier;
            homeScoreIDs.add(homeScoreId);
            LinearLayout.LayoutParams paramsHomeScore = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.2f);
            homeScore.setGravity(Gravity.CENTER);
            paramsHomeScore.setMargins(10, 10, 10, 0);
            homeScore.setLayoutParams(paramsHomeScore);
            homeScore.setText(Integer.toString(games[i].getHomeScore()));
            homeScore.setInputType(InputType.TYPE_CLASS_NUMBER);
            homeScore.setId(homeScoreId);


            EditText awayScore = new EditText(getActivity().getBaseContext());
            awayScoreId = awayScoreId + multiplier;
            awayScoreIDs.add(awayScoreId);
            LinearLayout.LayoutParams paramsAwayScore = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.2f);
            awayScore.setGravity(Gravity.CENTER);
            paramsAwayScore.setMargins(10, 10, 10, 0);
            awayScore.setLayoutParams(paramsAwayScore);
            awayScore.setInputType(InputType.TYPE_CLASS_NUMBER);
            awayScore.setText(Integer.toString(games[i].getAwayScore()));
            awayScore.setId(awayScoreId);



            linearContainer.addView(homeText);
            linearContainer.addView(homeScore);
            linearContainer.addView(midText);

            linearContainer.addView(awayScore);

            linearContainer.addView(awayText);
            linearContainerTop.addView(linearContainer);

        }
        layout.addView(linearContainerTop);
    }

    /**
     * Saves the current active round to the database.
     *
     * @param activeRound Current active round
     * @return List of Game objects
     */
    public Game[] saveRound(int activeRound) {
        Game[] games = tournament.getRounds()[(activeRound) - 1].getGames();
        Round round = tournament.getRounds()[activeRound-1];
        round.setRoundSaved(true);
        DataSource ds = new DataSource(getActivity());
        for (int i = 0; i < games.length; i++) {
            EditText homeScore = (EditText) rootView.findViewById(homeScoreIDs.get(i));
            EditText awayScore = (EditText) rootView.findViewById(awayScoreIDs.get(i));

            games[i].setHomeScore(Integer.parseInt(homeScore.getText().toString()));
            games[i].setAwayScore(Integer.parseInt(awayScore.getText().toString()));
            try {
                ds.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Log.d("ScoresFragment", games[i].getId() + " GAME ID");
            ds.updateMatch(games[i]);
            ds.close();


        }

        return games;
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
            //activeRound = savedInstanceState.getInt("activeRound");
            long tournamentId = savedInstanceState.getLong("tournamentId");
            Log.d("ScoresFragment", "onActivityCreated " + tournamentId+"");
            if (tournamentId != TEMP_ID) {

                Loader loader = new Loader(getActivity().getApplicationContext());
                this.tournament = loader.getTournamentAndRounds(tournamentId);

                if (tournament.getRounds().length > 0) {
                    createMatchTable(1);
                    roundTextView.setText("Round: " + 1);

                } else {
                    Log.d("ScoresFragment", "getRounds = 0");
                }
            } else {
                Log.d("ScoresFragment", "temp id");
            }


        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        comm = (Communicator) activity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("tournamentId", tournament.getId());
        Log.d("ScoresFragment", "onSaveInstanceState " + tournament.getId());
        outState.putInt("activeRound", activeRound);
    }
    /**
     * Clears all generated content from the UI.
     */
    public void clearUI() {
        for (int i = 0; i < containerIDs.size(); i++) {
            LinearLayout linearContainer = (LinearLayout) rootView.findViewById(containerIDs.get(i));
            linearContainer.setVisibility(LinearLayout.GONE);
        }
        //layout.removeAllViews();
    }


    /**
     * Handles button clicks
     *
     * @return v Clicked view
     */
    @Override
    public void onClick(View v) {

        if (tournament != null) {
            switch (v.getId()) {
                case R.id.plusButton:
                    if (activeRound < tournament.getMaxRounds()) {
                        activeRound++;
                        clearUI();
                        containerIDs.clear();
                        awayScoreIDs.clear();
                        homeScoreIDs.clear();
                        createMatchTable(activeRound);
                        roundTextView.setText("Round: " + activeRound);
                    }
                    break;
                case R.id.minusButton:
                    if (activeRound > 1) {
                        activeRound--;
                        clearUI();
                        awayScoreIDs.clear();
                        homeScoreIDs.clear();
                        containerIDs.clear();
                        createMatchTable(activeRound);
                        roundTextView.setText("Round: " + activeRound);
                    }
                    break;
                case R.id.saveRoundButton:
                    tournament.getRounds()[activeRound-1].setGames(saveRound(activeRound));
                    comm.respond(tournament, 2, false);

                    break;
            }
        }
    }


}