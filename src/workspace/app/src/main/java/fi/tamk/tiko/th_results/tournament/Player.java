package fi.tamk.tiko.th_results.tournament;
/**
 * This class is used to create Player objects.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class Player {
	private int id;
	private String name;
	private int goalsDone;
	private int goalsAgainst;
	private int points;
	private int wins;
	private int losses;
	private int draws;
    private int matches;
    private boolean ghostPlayer;



    public Player(String name) {
		this.name = name;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGoalsDone() {
		return goalsDone;
	}
	public void setGoalsDone(int goalsDone) {
		this.goalsDone = goalsDone;
	}
	public int getGoalsAgainst() {
		return goalsAgainst;
	}
	public void setGoalsAgainst(int goalsAgainst) {
		this.goalsAgainst = goalsAgainst;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public int getDraws() {
		return draws;
	}
	public void setDraws(int draws) {
		this.draws = draws;
	}

    public int getMatches() {
        return matches;
    }

    public void setMatches() {
        this.matches = getWins() + getLosses() + getDraws();
    }
    public boolean isGhostPlayer() {
        return ghostPlayer;
    }

    public void setGhostPlayer(boolean ghostPlayer) {
        this.ghostPlayer = ghostPlayer;
    }
}
