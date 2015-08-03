package fi.tamk.tiko.th_results.dropbox;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import fi.tamk.tiko.th_results.StandingsManager;
import fi.tamk.tiko.th_results.tournament.Player;
import fi.tamk.tiko.th_results.tournament.Tournament;


/**
 * Generates HTML-code from tournament results.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class HTMLGenerator {
    Tournament tournament;
    File myExternalFile;
    StandingsManager sm;
    String FILEPATH;

    /**
     * Constructor
     *
     * Creates a new html-file to the external storage directory and sets the file for Dropbox sync.
     *
     * @param filename File name for html-file
     * @param tournament Tournament to export
     */
    public HTMLGenerator(String filename, Tournament tournament) {
        this.tournament = tournament;
        Log.d("HTMLGenerator", tournament.getRounds()[0].getGames()[0].getAwayScore()+"");
        FILEPATH = "/THRESULTS/";
        myExternalFile = new File(Environment.getExternalStorageDirectory(), filename);
        sm = new StandingsManager(tournament);
        try {
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(getData(filename).getBytes());
            fos.close();
            Log.d("HTMLGenerator", Environment.getExternalStorageDirectory().toString() + "success");
        } catch (IOException e) {
            Log.d("HTMLGenerator", "failed");
            e.printStackTrace();
        }



    }
    /**
     * Gets created file
     *
     * @return Created file
     */
    public File getFile() {
        return this.myExternalFile;
    }

    /**
     * Generates standings data in html format
     *
     * @param filename File name to be used as the title.
     * @return Data string
     */
    public String getData(String filename) {
        Log.d("HTMLGenerator", "testing1");
        sm.saveAllRounds();
        tournament = sm.calculatePoints();
        this.tournament.setPlayers(sm.sortByPoints(), 1);
        int placement = 1;
        Log.d("HTMLGenerator", "testing");
        //System.getProperty("line.separator");
        String data = "<html>" + System.getProperty("line.separator");
        data = data + "<head>" + System.getProperty("line.separator");
        data = data + "<title>" + filename + "</title>" + System.getProperty("line.separator");
        data = data + "<body>" + System.getProperty("line.separator");
        data = data + "<table align='center' bgcolor='c0c0c0'><tr><td><pre>" + System.getProperty("line.separator");
        data = data + "<table class='seriestable'>" + System.getProperty("line.separator");

        for (int i = tournament.getPlayerCount() -1; i >= 0; i --) {
            Player player = tournament.getPlayers().get(i);
            data = data + "<tr class='seriestablerow'>" + System.getProperty("line.separator");
            data = data + "<td class='seriesorder'><a name='0'/>"
                    + placement + "</td>" + System.getProperty("line.separator");
            data = data + "<td class='seriesname'>" + player.getName()
                    + "</td>" + System.getProperty("line.separator");
            data = data + "<td class='seriesgames'>" + player.getMatches() + "</td>"
                    + System.getProperty("line.separator");
            data = data + "<td class='serieswins'>" + player.getWins() + "</td>"
                    + System.getProperty("line.separator");
            Log.d("HTMLGenerator", player.getWins()+"");
            data = data + "<td class='seriesties'>" + player.getDraws() + "</td>"
                    + System.getProperty("line.separator");
            data = data + "<td class='serieslosses'>" + player.getLosses() + "</td>"
                    + System.getProperty("line.separator");
            data = data + "<td class='seriesscored'>" + player.getGoalsDone() + "</td>"
                    + System.getProperty("line.separator");
            data = data + "<td class='seriesminus'>-</td>" + System.getProperty("line.separator");
            data = data + "<td class='seriesyielded'>" + player.getGoalsAgainst() + "</td>"
                    + System.getProperty("line.separator");
            data = data + "<td class='seriespoints'>" + player.getPoints() + "</td></tr>"
                    + System.getProperty("line.separator");


            placement++;

        }

        data = data + "</table></body>" + System.getProperty("line.separator");
        data = data + "</html>";

        return data;
    }

}
