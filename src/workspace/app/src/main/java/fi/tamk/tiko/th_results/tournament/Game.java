package fi.tamk.tiko.th_results.tournament;

/**
 * This class is used to create Game objects.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class Game {
	Player homePlayer;
	Player awayPlayer;
	int homeScore;
	int awayScore;
    private int gameNumber;
    private int round;
    private long id;
    private long tournamentId;

    /**
     * Empty constructor
     */
    public Game() {

    }
    /**
     * Constructor for setting players on construct
     *
     * @param homePlayer Home Player object
     * @param awayPlayer Away Player object
     */
	public Game(Player homePlayer, Player awayPlayer) {
		this.homePlayer = homePlayer;
		this.awayPlayer = awayPlayer;
	}

    /**
     * Gets game id
     *
     * @return Game id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets game id
     *
     * @param id Game id to be set
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * Gets home player
     *
     * @return Home Player object
     */
    public Player getHomePlayer() {
		return homePlayer;
	}

    /**
     * Sets game id
     *
     * @param homePlayer Player to be set
     */
	public void setHomePlayer(Player homePlayer) {
		this.homePlayer = homePlayer;
	}

    /**
     * Gets away player
     *
     * @return Away Player object
     */
	public Player getAwayPlayer() {
		return awayPlayer;
	}

    /**
     * Sets away player
     *
     * @param awayPlayer Player to be set
     */
	public void setAwayPlayer(Player awayPlayer) {
		this.awayPlayer = awayPlayer;
	}

    /**
     * Gets home score
     *
     * @return Home score
     */
	public int getHomeScore() {
		return homeScore;
	}

    /**
     * Sets home score
     *
     * @param homeScore Score to be set
     */
	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

    /**
     * Gets away score
     *
     * @return Away score
     */
	public int getAwayScore() {
		return awayScore;
	}

    /**
     * Sets away score
     *
     * @param awayScore Score to be set
     */
	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}
    /**
     * Gets game number
     *
     * @return Game number
     */
    public int getGameNumber() {
        return gameNumber;
    }

    /**
     * Sets game number
     *
     * @param gameNumber Game number to be set
     */
    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    /**
     * Gets round number
     *
     * @return Round number
     */
    public int getRound() {
        return round;
    }

    /**
     * Sets round number
     *
     * @param round Round number to be set
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * Gets associated tournament ID
     *
     * @return Tournament ID
     */
    public long getTournamentId() {
        return tournamentId;
    }

    /**
     * Sets associated tournament ID
     *
     * @param tournamentId Tournament ID to be set
     */
    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

}
