package fi.tamk.tiko.th_results.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

import fi.tamk.tiko.th_results.tournament.Game;
import fi.tamk.tiko.th_results.tournament.Player;
import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * All SQL-queries are defined in this class.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class DataSource{

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumnsTournament = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_DATE,
            MySQLiteHelper.COLUMN_PLAYERLIST};



    private String[] allColumnsMatch = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_AWAY_PLAYER,
            MySQLiteHelper.COLUMN_HOME_PLAYER,
            MySQLiteHelper.COLUMN_AWAY_SCORE,
            MySQLiteHelper.COLUMN_HOME_SCORE,
            MySQLiteHelper.COLUMN_ROUND_ID,
            MySQLiteHelper.COLUMN_TOURNAMENT_ID,
            MySQLiteHelper.COLUMN_GAME_NUMBER
    };
    /**
     * Constructor for a DataSource object.
     *
     * @param context Activity context
     */
    public DataSource(Context context) {

        dbHelper = new MySQLiteHelper(context);
        Log.d("DataSource", "open");
    }

    /**
     * Opens database connection.
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes database connection.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Adds a new tournament to the database
     *
     * @param tournament Tournament object
     * @return Tournament ID
     */
    public long createTournament(Tournament tournament) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_NAME, tournament.getName());
        values.put(MySQLiteHelper.COLUMN_DATE, tournament.getDate().toString());
        values.put(MySQLiteHelper.COLUMN_PLAYERLIST, tournament.getPlayersToString());

        long insertId = database.insert("tournament", null,
                values);
        return insertId;
    }

    /**
     * Updates Tournament table.
     *
     * @param tournament Tournament object
     * @return Tournament object
     */
    public Tournament updateTournament(Tournament tournament) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, tournament.getName());
        values.put(MySQLiteHelper.COLUMN_DATE, tournament.getDate().toString());

        String strFilter = MySQLiteHelper.COLUMN_TOURNAMENT_ID+"=" + tournament.getId();
        database.update(MySQLiteHelper.TABLE_TOURNAMENT, values, strFilter, null);

        return tournament;
    }

    /**
     * Adds a new game to the database.
     *
     * @param game Game to add
     * @param tournamentId Tournament ID for tournament to add to
     * @return Game object
     */
    public Game createMatch(Game game, long tournamentId) {
        Log.d("DataSource", "TOURNAMENT ID " + tournamentId);
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_AWAY_PLAYER, game.getAwayPlayer().getName());
        values.put(MySQLiteHelper.COLUMN_HOME_PLAYER, game.getHomePlayer().getName());

        values.put(MySQLiteHelper.COLUMN_AWAY_SCORE, game.getAwayScore());
        values.put(MySQLiteHelper.COLUMN_HOME_SCORE, game.getHomeScore());
        values.put(MySQLiteHelper.COLUMN_GAME_NUMBER, game.getGameNumber());
        Log.d("DataSource", "test " + game.getGameNumber());
        values.put(MySQLiteHelper.COLUMN_ROUND_ID, game.getRound());
        values.put(MySQLiteHelper.COLUMN_TOURNAMENT_ID, tournamentId);

        long insertId = database.insert(MySQLiteHelper.TABLE_MATCH, null,
                values);
        game.setId(insertId);
        Log.d("DataSource", game.getAwayPlayer().getName() + " saving");
        return game;

    }
    /**
     * Updates game table.
     *
     * @param game Game to update
     */
    public void updateMatch(Game game) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_AWAY_PLAYER, game.getAwayPlayer().getName());
        values.put(MySQLiteHelper.COLUMN_HOME_PLAYER, game.getHomePlayer().getName());
        values.put(MySQLiteHelper.COLUMN_AWAY_SCORE, game.getAwayScore());
        values.put(MySQLiteHelper.COLUMN_HOME_SCORE, game.getHomeScore());
        Log.d("DataSource", "test " + game.getAwayScore());
        Log.d("DataSource", "test " + game.getHomeScore());
        values.put(MySQLiteHelper.COLUMN_ROUND_ID, game.getRound());
        String strFilter = MySQLiteHelper.COLUMN_ID + "=" + game.getId();
        database.update(MySQLiteHelper.TABLE_MATCH, values, strFilter, null);

    }
    /**
     * Deletes a tournament and its matches from the database.
     *
     * @param tournamentID Tournament ID for tournament to be removed
     */
    public void deleteTournamentandMatches(long tournamentID) {

        database.delete(MySQLiteHelper.TABLE_TOURNAMENT, MySQLiteHelper.COLUMN_ID
                + " = " + tournamentID, null);
        database.delete(MySQLiteHelper.TABLE_MATCH, MySQLiteHelper.COLUMN_TOURNAMENT_ID
                + " = " + tournamentID, null);
    }

    /**
     * Fetches all tournaments from the database
     *
     * @return ArrayList of Tournament objects
     */
    public ArrayList<Tournament> getTournaments() {
        ArrayList<Tournament> tournaments = new ArrayList<Tournament>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TOURNAMENT,
                allColumnsTournament, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tournament tournament = cursorToTournament(cursor);
            tournaments.add(tournament);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tournaments;
    }

    /**
     * Fetches a desired tournament from the database
     *
     * @param tournamentId Tournament ID for fetching tournament
     * @return Tournament object
     */
    public Tournament getTournament(long tournamentId) {
        Tournament tournament = new Tournament();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TOURNAMENT,
                allColumnsTournament, MySQLiteHelper.COLUMN_ID + " = " + tournamentId, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
             tournament = cursorToTournament(cursor);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tournament;
    }
    /**
     * Fetches all matches belonging to a desired tournament
     *
     * @param tournamentId Tournament ID for fetching matches
     * @return ArrayList of Game objects
     */
    public ArrayList<Game> getMatches(long tournamentId) {
        ArrayList<Game> games = new ArrayList<Game>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_MATCH,
                allColumnsMatch, MySQLiteHelper.COLUMN_TOURNAMENT_ID+" = "+tournamentId, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Game game = cursorToMatch(cursor);
            games.add(game);
            cursor.moveToNext();

        }
        // make sure to close the cursor
        cursor.close();
        return games;
    }
    /**
     * Initializes fetched tournament data to a Tournament object
     *
     * @param cursor Database cursor
     * @return Tournament object
     */
    private Tournament cursorToTournament(Cursor cursor) {
        Tournament tournament = new Tournament();
        tournament.setId(cursor.getLong(0));
        tournament.setName(cursor.getString(1));
        tournament.setDate(cursor.getString(2));
        tournament.setPlayers(cursor.getString(3));


        return tournament;
    }
    /**
     * Initializes fetched game data to a Game object
     *
     * @param cursor Database cursor
     * @return Game object
     */
    private Game cursorToMatch(Cursor cursor) {
        Game game = new Game();
        game.setId(cursor.getLong(0));

        game.setAwayPlayer(new Player(cursor.getString(1)));
        Log.d("DataSource", "Away " + game.getAwayPlayer().getName());
        game.setHomePlayer(new Player(cursor.getString(2)));
        Log.d("DataSource", "Home " + game.getHomePlayer().getName());
        game.setAwayScore(cursor.getInt(3));
        Log.d("DataSource", "Awaysc " + game.getAwayScore());
        game.setHomeScore(cursor.getInt(4));
        Log.d("DataSource", "Homesc " + game.getHomeScore());

        game.setRound(cursor.getInt(5));
        Log.d("DataSource", "Round " + game.getRound());
        game.setTournamentId(cursor.getInt(6));
        Log.d("DataSource", "TID " + game.getTournamentId());
        game.setGameNumber(cursor.getInt(7));
        Log.d("DataSource", "GN " + game.getGameNumber());
        Log.d("DataSource", cursor.getLong(0) + " " + cursor.getString(1) + " " +cursor.getString(2) + cursor.getInt(3) + " " + cursor.getInt(4) + " " + cursor.getInt(5) + " " +  cursor.getInt(6) + " " +  cursor.getInt(7));


        return game;

    }
}