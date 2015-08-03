package fi.tamk.tiko.th_results.database;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import fi.tamk.tiko.th_results.tournament.Game;
import fi.tamk.tiko.th_results.tournament.Round;
import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * Helper class to easily load tournaments and matches from database.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class Loader {
    Button loadTournamentButton;
    ArrayList<String> players;
    ListView tournamentsListView;
    ArrayAdapter<String> adapter;
    ArrayList<Tournament> tournaments;
    Context context;

    /**
     * Constructor for the loader, sets context
     *
     * @param context Activity context
     */
    public Loader(Context context) {
        this.context = context;

    }

    /**
     * Loads tournaments from the database.
     *
     * @return ArrayList of all tournaments
     */
    public ArrayList<Tournament> loadTournaments(){
        DataSource ds = new DataSource(context);
        try {
            ds.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Tournament> tournamentList = ds.getTournaments();
        ds.close();


        return tournamentList;
    }

    /**
     * Creates an ArrayList<String> of an ArrayList<Tournament>, using tournament names.
     *
     * @param tournaments ArrayList of Tournament objects
     * @return String ArrayList of tournament names
     */
    public ArrayList<String> tournamentsToStringArray(ArrayList<Tournament> tournaments) {
        ArrayList<String> tempList = new ArrayList<String>();

        for (int i = 0; i < tournaments.size(); i++) {
            String str = tournaments.get(i).getName() + ", " + tournaments.get(i).getSize() + " players" + tournaments.get(i).getId();
            tempList.add(str);
        }
        return tempList;
    }



    /**
     * Gets all rounds and matches of a tournament
     *
     * @param tournamentId Tournament ID
     * @return Tournament object
     */
    public Tournament getTournamentAndRounds(long tournamentId) {
        DataSource ds = new DataSource(context);
        try {
            ds.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Tournament tournament = ds.getTournament(tournamentId);
        tournament.setMaxRounds();
        Round[] rounds = new Round[tournament.getMaxRounds()];
        ArrayList<Game> games = ds.getMatches(tournamentId);
        Log.d("Loader", "maxrounds " + tournament.getMaxRounds());
        Log.d("Loader", games.size()+"");

        for (int j = 0; j < rounds.length; j++) {
            Round round = new Round();

            ArrayList<Game> tempGames = new ArrayList<Game>();
            for (int i = 0; i < games.size(); i++) {

                if (games.get(i).getRound() == j + 1) {
                    games.get(i).setHomePlayer(tournament.findPlayerByName(games.get(i).getHomePlayer().getName()));
                    games.get(i).setAwayPlayer(tournament.findPlayerByName(games.get(i).getAwayPlayer().getName()));
                    tempGames.add(games.get(i));

                }
            }
            round.setGames(tempGames);

            rounds[j] = round;

        }

        tournament.setRounds(rounds);
        return tournament;
    }


}
