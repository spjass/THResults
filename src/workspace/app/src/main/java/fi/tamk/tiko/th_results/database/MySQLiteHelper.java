package fi.tamk.tiko.th_results.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * SQL helper class. Holds methods for creating tables and database variables.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    //  Change to create new tables
    private static final int DATABASE_VERSION = 16;

    public static final String TABLE_TOURNAMENT = "tournament";
    public static final String TABLE_MATCH = "match";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GAME_NUMBER = "game";
    public static final String COLUMN_AWAY_PLAYER = "awayPlayer";
    public static final String COLUMN_HOME_PLAYER = "homePlayer";
    public static final String COLUMN_AWAY_SCORE= "awayScore";
    public static final String COLUMN_HOME_SCORE = "homeScore";
    public static final String COLUMN_ROUND_ID = "round";
    public static final String COLUMN_TOURNAMENT_ID = "t_id";
    public static final String COLUMN_PLAYERLIST = "playerlist";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";

    private static final String DATABASE_NAME = "db";



    // Database creation sql statement
    private static final String DATABASE_CREATE_TOURNAMENT = "create table "
            + TABLE_TOURNAMENT + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null,"
            + COLUMN_DATE + " text,"
            + COLUMN_PLAYERLIST + " text);";

    private static final String DATABASE_CREATE_MATCH = "create table "
            + TABLE_MATCH + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_GAME_NUMBER + " integer not null,"
            + COLUMN_AWAY_PLAYER + " text not null,"
            + COLUMN_HOME_PLAYER + " text not null,"
            + COLUMN_AWAY_SCORE + " integer,"
            + COLUMN_HOME_SCORE + " integer,"
            + COLUMN_ROUND_ID + " integer not null,"
            + COLUMN_TOURNAMENT_ID + " integer not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    /**
     * Creates database tables for matches and tournaments.
     *
     * @param database Used database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d("MySQLiteHelper", "creating databases");
        database.execSQL(DATABASE_CREATE_TOURNAMENT);
        database.execSQL(DATABASE_CREATE_MATCH);
        Log.d("MySQLiteHelper", "creating databases2");
    }

    /**
     * Creates new database tables if version upgraded
     *
     * @param db Used database
     * @param oldVersion Old database version
     * @param newVersion New database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURNAMENT);
        onCreate(db);
    }

}