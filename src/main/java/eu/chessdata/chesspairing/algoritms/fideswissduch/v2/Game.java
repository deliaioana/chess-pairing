package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.List;

public class Game {
	private boolean valid;
	private Player white;
	private Player black;
	
	private Game(){
		
	}

	public static Game createGame(Player playerA, Player playerB) {
		Game game = new Game();
		if (!historyOK(playerA,playerB)){
			game.valid = false;
			return game;
		}
		
		//
		
		throw new IllegalStateException("Please implement this");
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
		//history is OK
		return true;
	}
}
