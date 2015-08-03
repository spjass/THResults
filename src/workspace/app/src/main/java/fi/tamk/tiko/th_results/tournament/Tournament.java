package fi.tamk.tiko.th_results.tournament;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;

/**
 * This class is used to create Tournament objects.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class Tournament {
	private long id;
	private String name;
	private String date;
	private int playerCount;
	private ArrayList<Player> players;
    private int mData;
    private int maxRounds;
    private int currentRound;
    private Game[] games;
    Round[] roundList;
    int size;



    public Tournament() {
		players = new ArrayList<Player>();

	}
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate() {
        Calendar cal = Calendar.getInstance();
        String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String month = Integer.toString(cal.get(Calendar.MONTH));
        String year = Integer.toString(cal.get(Calendar.YEAR));
        this.date = year+"/"+month+"/"+day;
	}

    public void setDate(String date) {
       this.date = date;

    }

	public int getPlayerCount() {
		return this.getPlayersToStringArray().size();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void setPlayers(ArrayList<String> players) {
		this.players.clear();

        for (int i = 0; i < players.size(); i++) {
            this.players.add(new Player(players.get(i)));
        }
	}

    public void setPlayers(String playersStr) {
        String[] playersArray = playersStr.split(";");
        this.players.clear();
        for (int i = 0; i < playersArray.length; i++) {

            if (!playersArray[i].equals("X")) {
                this.players.add(new Player(playersArray[i]));
            } else {
                Player player = new Player("X");
                player.setGhostPlayer(true);
                this.players.add(player);
            }
        }
    }

    public void setPlayers(ArrayList<Player> players, int temp) {
        this.players = players;
    }
	
	public void removePlayer(int index) {
		players.remove(index);
	}

    public void clearPlayers() {
        players.clear();
    }
    /**
     * @return String ArrayList of player names.
     */
	public ArrayList<String> getPlayersToStringArray() {
		ArrayList<String> strList = new ArrayList<String>();

        for (int i = 0; i < players.size(); i++) {

            if (!players.get(i).isGhostPlayer()) {
                strList.add(players.get(i).getName());
            }
        }
		return strList;
	}
    /**
     * @return String of player names, separated with ";".
     */
    public String getPlayersToString() {
        String str = "";
        for (int i = 0; i < players.size(); i++) {

            str = str+players.get(i).getName()+";";
        }
        return str;
    }

    /**
     * Adds a new player to the Tournament
     *
     * @param player Player to add
     * @return Tournament object
     */
	public Tournament addPlayer(Player player) {
		this.players.add(player);
		setMaxRounds();
		return this;
	}
    /**
     * Finds and removes ghost players from the tournament.
     */
    public void removeGhosts() {

        for (int i = 0; i < players.size(); i++) {

            if (players.get(i).isGhostPlayer()) {
                players.remove(i);
            }
        }
    }


	public int getMaxRounds() {
		return maxRounds;
	}

    /**
     * Sets max amount of rounds according to number of participants
     */
	public void setMaxRounds() {
		if (players.size() % 2 != 0) {
			this.maxRounds = (int) Math.floor(players.size());
		} else {
			this.maxRounds = (int) Math.floor(players.size() + 1);
		}
	}
    /**
     * Generates rounds for a new tournament
     *
     * @param context Activity context
     * @param tournamentID Tournament ID
     */
	public void generateRounds(Context context, long tournamentID) {
		ArrayList<Player> tempList = this.getPlayers();

		setMaxRounds();
		Round[] roundList = new Round[maxRounds];


        for (int i = 0; i < maxRounds; i++) {
            if (i == 0) {
                Round round = new Round(tempList, context, tournamentID, i+1);
                roundList[i] = round;
            } else {
                tempList.add(0, tempList.get(tempList.size() - 1));
                tempList.remove(tempList.size() - 1);
                Round round = new Round(tempList, context, tournamentID, i+1);
                roundList[i] = round;
            }
        }

        if (tempList.size() % 2 != 0) {
            tempList.add(0, tempList.get(tempList.size() - 1));
            tempList.remove(tempList.size() - 1);
        }

        this.setRounds(roundList);

    }

    /**
     * Reset scores of all players in the tournament.
     */
    public void resetScores() {
        for (int i = 0; i < players.size(); i++) {

            Player player = players.get(i);
            player.setGoalsAgainst(0);
            player.setGoalsDone(0);
            player.setWins(0);
            player.setLosses(0);
            player.setDraws(0);
            player.setPoints(0);
            players.set(i, player);
        }

    }
    /**
     * Updates the selected player
     *
     * @param player Player object to update.
     */
    public void updatePlayerStatistics(Player player) {

        for (int i = 0; i < players.size(); i++) {

            if (player.getName().equals(players.get(i).getName())) {
                players.set(i, player);
            }
        }
    }
    /**
     * Finds players by name.
     *
     * @return Found player, null if not found
     */
    public Player findPlayerByName(String name) {

        for (int i = 0; i < players.size(); i++) {

            if (name.equals(players.get(i).getName())) {
                return players.get(i);
            }
        }

        return null;
    }
    /**
     * Checks if a player already exists in the tournament
     * @return True if duplicate found.
     */
    public boolean checkForDuplicatePlayer(String playerName) {
        for (int i = 0; i < players.size(); i++) {

            if (playerName.equals(players.get(i).getName())) {
                return true;
            }
        }
        return false;
    }



    public int getSize() {
        return this.players.size();
    }

    public void setRounds(Round[] roundList) {
        this.roundList = roundList;
    }

    public Round[] getRounds() {
        return this.roundList;
    }
	
	public int getCurrentRound() {
		return currentRound;
	}
	
	public void setCurrentRound(int round) {
		this.currentRound = round;
	}

}
