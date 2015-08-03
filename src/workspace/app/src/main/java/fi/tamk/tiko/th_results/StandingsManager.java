package fi.tamk.tiko.th_results;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fi.tamk.tiko.th_results.tournament.Game;
import fi.tamk.tiko.th_results.tournament.Player;
import fi.tamk.tiko.th_results.tournament.Round;
import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * Class calculates points and statistics for players and sorts them in final standings order.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class StandingsManager implements Comparator<Player> {
    private ArrayList<Player> players;
    private Tournament tournament;

    public StandingsManager(Tournament tournament) {
        this.tournament = tournament;
    }

    /**
     * Calculates points for players.
     *
     * @return Tournament object
     */
    public Tournament calculatePoints() {
        tournament.resetScores();
        Round[] rounds = this.tournament.getRounds();

        for (int i = 0; i < rounds.length; i++) {

            Game[] games = rounds[i].getGames();

            for (int j = 0; j < games.length; j++) {

                Game tempGame = games[j];
                Player homePlayer = tempGame.getHomePlayer();
                Player awayPlayer = tempGame.getAwayPlayer();

                if (rounds[i].isRoundSaved() && !homePlayer.isGhostPlayer() && !awayPlayer.isGhostPlayer()) {
                    if (tempGame.getHomeScore() > tempGame.getAwayScore()) {
                        homePlayer.setPoints(homePlayer.getPoints() + 2);
                        homePlayer.setWins(homePlayer.getWins() + 1);
                        awayPlayer.setLosses(awayPlayer.getLosses() + 1);

                        Log.d("StandingsManager", "home win " + homePlayer.getName() + " " + homePlayer.getPoints());
                    } else if (tempGame.getHomeScore() < tempGame.getAwayScore()) {
                        awayPlayer.setPoints(awayPlayer.getPoints() + 2);
                        awayPlayer.setWins(awayPlayer.getWins() + 1);
                        homePlayer.setLosses(homePlayer.getLosses() + 1);
                        Log.d("StandingsManager", "away win " + awayPlayer.getName());
                    } else {
                        homePlayer.setPoints(homePlayer.getPoints() + 1);
                        awayPlayer.setPoints(awayPlayer.getPoints() + 1);
                        homePlayer.setDraws(homePlayer.getDraws() + 1);
                        awayPlayer.setDraws(awayPlayer.getDraws() + 1);
                        Log.d("StandingsManager", "draw " + homePlayer.getName() + " " + awayPlayer.getName());
                    }

                    homePlayer.setGoalsDone(homePlayer.getGoalsDone() + games[j].getHomeScore());
                    homePlayer.setGoalsAgainst(homePlayer.getGoalsAgainst() + games[j].getAwayScore());

                    awayPlayer.setGoalsDone(awayPlayer.getGoalsDone() + games[j].getAwayScore());
                    awayPlayer.setGoalsAgainst(awayPlayer.getGoalsAgainst() + games[j].getHomeScore());
                }
                homePlayer.setMatches();
                awayPlayer.setMatches();
                tournament.updatePlayerStatistics(homePlayer);
                tournament.updatePlayerStatistics(awayPlayer);

                tempGame.setHomePlayer(homePlayer);
                tempGame.setAwayPlayer(awayPlayer);
                games[j] = tempGame;

            }
            rounds[i].setGames(games);
        }
        tournament.setRounds(rounds);
        tournament.setPlayers(sortByPoints(), 1);

        return tournament;
    }
    /**
     * Sorts players by points.
     *
     * @return Sorted ArrayList of player objects
     */
    public ArrayList<Player> sortByPoints() {
        ArrayList<Player> playersList = tournament.getPlayers();

        Comparator<Player> comparator = this;
        Collections.sort(playersList, comparator);

        return playersList;

    }

    /**
     * Compares player points, mutual matches and goal differences
     *
     * @param homePlayer Home player to be compared
     * @param awayPlayer Away player to be compared
     * @return Integer for player sorting.
     */
    @Override
    public int compare(Player homePlayer, Player awayPlayer) {
        int c = 1;
        if(homePlayer.getPoints() <  awayPlayer.getPoints()) c = -1;
        if(homePlayer.getPoints() == awayPlayer.getPoints()) c = 0;


        if (c == 0) {
            Game mutualMatch = findMutualMatch(homePlayer, awayPlayer);
            if(mutualMatch.getHomeScore() < mutualMatch.getAwayScore()) c = -1;
            if(mutualMatch.getHomeScore() == mutualMatch.getAwayScore()) c = -0;
        }

        if (c == 0) {
            if(homePlayer.getGoalsDone() - homePlayer.getGoalsAgainst()
                    < awayPlayer.getGoalsDone() - awayPlayer.getGoalsAgainst()) c = -1;
            if(homePlayer.getGoalsDone() - homePlayer.getGoalsAgainst()
                    == awayPlayer.getGoalsDone() - awayPlayer.getGoalsAgainst()) c = 0;
        }

        if (c == 0) {
            if(homePlayer.getGoalsDone() < awayPlayer.getGoalsDone()) c = -1;
            if(homePlayer.getGoalsDone() == awayPlayer.getGoalsDone()) c = 0;
        }
        return c;

    }

    /**
     * Finds a mutual match of two players
     *
     * @param homePlayer Home player to find
     * @param awayPlayer Away player to find
     * @return Mutual match
     */
    public Game findMutualMatch(Player homePlayer, Player awayPlayer) {
        Round[] rounds = tournament.getRounds();
        Log.d("StandingsManager", rounds.length+"");
        for (int i = 0; i < rounds.length; i++) {
            Round round = rounds[i];
            for (int j = 0; j < round.getGames().length; j++) {
                Game tempGame = round.getGames()[j];

                if (homePlayer.getName().equals(tempGame.getHomePlayer().getName())
                        && awayPlayer.getName().equals(tempGame.getAwayPlayer().getName())) {
                    return tempGame;
                } else if (homePlayer.getName().equals(tempGame.getAwayPlayer().getName())
                        && awayPlayer.getName().equals(tempGame.getHomePlayer().getName())) {
                    tempGame.setHomePlayer(awayPlayer);
                    tempGame.setHomeScore(tempGame.getAwayScore());
                    tempGame.setAwayPlayer(homePlayer);
                    tempGame.setAwayScore(tempGame.getHomeScore());
                    return tempGame;
                }
            }
        }
        Log.d("StandingsManager", "mutual match not found");
        return null;
    }
    /**
     * Sets status of all rounds to "saved".
     */
    public void saveAllRounds() {

        for (int i = 0; i < tournament.getRounds().length; i++) {
            tournament.getRounds()[i].setRoundSaved(true);
        }
    }


}
