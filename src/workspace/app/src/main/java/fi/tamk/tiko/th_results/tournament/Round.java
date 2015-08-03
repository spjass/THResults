package fi.tamk.tiko.th_results.tournament;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;

import fi.tamk.tiko.th_results.database.DataSource;

/**
 * This class is used to create Round objects.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class Round {
	Game[] games;
	Player restingPlayer;
	ArrayList<Player> playersList;
    long tournamentID;
    int roundNumber;

    public boolean isRoundSaved() {
        return roundSaved;
    }

    public void setRoundSaved(boolean roundSaved) {
        this.roundSaved = roundSaved;
    }

    boolean roundSaved;
    Context context;
    /**
     * Empty constructor
     */
    public Round() {
        this.roundSaved = false;

    }

    /**
     * Constructor for creating a new round associated with a tournament
     *
     * @param players ArrayList of Player objects
     * @param context Activity context
     * @param tournamentID Associated tournament ID
     * @param roundNumber Current round number
     */
	public Round(ArrayList<Player> players, Context context, long tournamentID, int roundNumber) {
		playersList = players;
		this.tournamentID = tournamentID;
		this.roundNumber = roundNumber;
        games = initGames(context);
        this.roundSaved = false;
        this.context = context;

		
	}

    /**
     * Generates games according to the playerlist, this method is ran only when a new
     * Round object is initialized.
     *
     * @return Array of games
     */
	public Game[] initGames(Context context) {
		games = new Game[(int) Math.floor( playersList.size()/2)];
		restingPlayer = playersList.get(0);
        DataSource ds = new DataSource(context);
        try {
            ds.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < games.length; i++) {
            Game tempGame = new Game(playersList.get(i+1), playersList.get(playersList.size() -1 -i));
            tempGame.setGameNumber(i + 1);
            tempGame.setRound(this.roundNumber);
            tempGame = ds.createMatch(tempGame, tournamentID);
            games[i] = tempGame;
        }

		return games;
	}

    /**
     * @return Array of games
     */
    public Game[] getGames() {

        return this.games;
    }

    /**
     * @param games Array of games to be set for the round
     */
    public void setGames(Game[] games) {
        this.games = games;
    }

    public void setGames(ArrayList<Game> gamesArrayList) {
        Game[] tempGames = new Game[gamesArrayList.size()];
        for (int i = 0; i < gamesArrayList.size(); i++) {
            tempGames[i] = gamesArrayList.get(i);
        }

        this.games = tempGames;
    }


}
