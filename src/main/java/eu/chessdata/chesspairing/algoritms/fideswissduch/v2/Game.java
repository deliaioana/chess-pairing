package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.List;

public class Game {
	private boolean valid;
	private Player white;
	private Player black;
	private String initalPlayers;

	protected boolean isValid() {
		if (valid) {
			return true;
		} else {
			return false;
		}
	}

	private Game() {

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

	@Override
	public String toString() {
		if (isValid()) {
			return this.white.toString() + ", " + this.black.toString();
		} else {
			return "bad_game: " + this.initalPlayers;
		}
	}
}
