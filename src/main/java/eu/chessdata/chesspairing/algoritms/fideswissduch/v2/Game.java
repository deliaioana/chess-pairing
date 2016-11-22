package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.comparators.ComparatorChain;

public class Game {
	private boolean valid;
	private boolean buyGame;
	private Player white;
	private Player black;
	private String initalPlayers;
	// private List<Player> players;

	protected static final Comparator<Game> byPoints = new Comparator<Game>() {

		/**
		 * i have to return the opposite of normal compare in this case
		 * 
		 * negative is o1 > o2 zero if equal positive if o1 < 1
		 */
		@Override
		public int compare(Game o1, Game o2) {
			if (null == o1 || null == o2){
				throw new IllegalStateException("some games are null");
			}

			// if a and b is buy
			if (o1.isBuyGame() || o2.isBuyGame()) {
				throw new IllegalStateException("the 2 games are buy games");
			}
			if (o2.isBuyGame()) {
				return -1;
			}
			if (o1.isBuyGame()) {
				return 1;
			}

			Double high1 = o1.getHigherPlayer().getPairingPoints();
			Double high2 = o2.getHigherPlayer().getPairingPoints();
			if (!high1.equals(high2)) {
				return -1 * high1.compareTo(high2);
			}
			
			Double low1 = o1.getLowerPlayer().getPairingPoints();
			Double low2 = o2.getLowerPlayer().getPairingPoints();
			if (null == low1 || null == low2){
				throw new IllegalStateException("null black players not treated corectly. I should never reach this state");
			}
			return -1 * low1.compareTo(low2);

		}
	};

	protected boolean conatains(Player player) {
		if (white.equals(player)) {
			return true;
		}
		if (black.equals(player)) {
			return true;
		}
		return false;
	}

	protected boolean isValid() {
		if (this.buyGame){
			if (null != white){
				return true;
			}
		}
		
		if (valid) {
			if (null == white){
				return false;
			}
			if (null == black){
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * returns the the player that he is ranked higher
	 * 
	 * @return
	 */
	protected Player getHigherPlayer() {
		if (null == white) {
			throw new IllegalStateException("white player is null");
		}
		if (null == black) {
			return white;
		}
		// just sort the list
		List<Player> players = getPlayers();
		
		ComparatorChain<Player> comparatorChain = new ComparatorChain<>();
		comparatorChain.addComparator(Player.byPoints);
		comparatorChain.addComparator(Player.byElo);
		comparatorChain.addComparator(Player.byInitialRanking);
		
		Collections.sort(players, comparatorChain);
		return players.get(0);
	}

	/**
	 * if the game does not have the second player it returns null
	 * 
	 * @return
	 */
	protected Player getLowerPlayer() {
		if (null == white) {
			throw new IllegalStateException("game does not contain white player");
		}
		if (null == black) {
			return null;
		}
		// just sor the list
		List<Player> players = getPlayers();
		
		ComparatorChain<Player> comparatorChain = new ComparatorChain<>();
		comparatorChain.addComparator(Player.byPoints);
		comparatorChain.addComparator(Player.byElo);
		comparatorChain.addComparator(Player.byInitialRanking);
		
		Collections.sort(players, comparatorChain);
		return players.get(1);
	}

	private Game() {
		this.buyGame = false;
	}

	protected List<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<>();
		if (null != white) {
			players.add(white);
		}
		if (null != black) {
			players.add(black);
		}
		return players;
	}

	/**
	 * it creates a game from player playerA and playerB
	 * 
	 * @param playerA
	 * @param playerB
	 * @return
	 */
	public static Game createGame(Player playerA, Player playerB) {

		Game game = new Game();
		game.initalPlayers = playerA.toString() + ", " + playerB.toString();
		if (!historyOK(playerA, playerB)) {
			game.valid = false;
			return game;
		}

		if (playerA.preferesWhite() && playerB.canPlayBlack()) {
			game.valid = true;
			game.white = playerA;
			game.black = playerB;
			return game;
		}

		if (playerB.preferesWhite() && playerA.canPlayBlack()) {
			game.valid = true;
			game.white = playerB;
			game.black = playerA;
			return game;
		}

		if (playerA.canPlayWhite() && playerB.canPlayBlack()) {
			game.valid = true;
			game.white = playerA;
			game.black = playerB;
			return game;
		}

		if (playerB.canPlayWhite() && playerA.canPlayBlack()) {
			game.valid = true;
			game.white = playerB;
			game.black = playerA;
			return game;
		}

		/**
		 * if i have reached this point than due to the color preferences
		 * players can not play each other.
		 */
		game.valid = false;
		return game;
	}

	public static Game createBuyGame(Player player) {
		if (player.wasBuy) {
			throw new IllegalStateException("This player was allready buy");
		}
		Game game = new Game();
		game.valid = true;
		game.white = player;
		game.buyGame = true;
		return null;
	}

	private static boolean historyOK(Player playerA, Player playerB) {
		String aKey = playerA.getPlayerKey();
		String bKey = playerB.getPlayerKey();
		List<String> aHistory = playerA.getPlayersHistory();
		for (String key : aHistory) {
			if (bKey.equals(key)) {
				return false;
			}
		}
		List<String> bHistory = playerB.getPlayersHistory();
		for (String key : bHistory) {
			if (aKey.equals(key)) {
				throw new IllegalStateException("Detected bug in compute tournament state. "
						+ "This players do not find each other in the their oun history");
			}
		}
		// history is OK
		return true;
	}

	public Player getWhite() {
		return white;
	}

	public Player getBlack() {
		return black;
	}

	public boolean isBuyGame() {
		return buyGame;
	}

	@Override
	public String toString() {
		if (isValid()) {
			return this.white.toString() + ", " + this.black.toString();
		} else {
			return "bad_game: " + this.initalPlayers;
		}
	}

	/**
	 * it computes and returns the b3 factor for this game
	 * 
	 * @return
	 */
	public Double getPointsDiff() {
		if (this.buyGame) {
			return 0.0;
		}
		if (null == white || null == black) {
			throw new IllegalStateException("Not a valid game");
		}
		Double val = white.getPairingPoints() - black.getPairingPoints();
		// return only positive value
		if (val >= 0.0) {
			return val;
		} else {
			return (-1.0) * val;
		}
	}
}
