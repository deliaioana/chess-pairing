package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.List;

public class GameList {
	private List<Game> list = new ArrayList<>();
	private List<Player> notPared = new ArrayList<>();

	public void setNoPartner(Player playerA) {

		for (Player player : notPared) {
			if (playerA.equals(player)) {
				throw new IllegalStateException("Same player");
			}
		}
		this.notPared.add(playerA);
	}

	/**
	 * if the players can play against add the game to the list
	 * else add the players to the notPared list
	 * 
	 * @param playerA
	 * @param playerB
	 */
	public void addGame(Player playerA, Player playerB) {
		Game game = Game.createGame(playerA, playerB);
		if (game.isValid()) {
			this.list.add(game);
		} else {
			this.notPared.add(playerA);
			this.notPared.add(playerB);
		}
	}

	/**
	 * it returns the size of the iner list of games
	 * @return
	 */
	public int size() {
		return list.size();
	}

}