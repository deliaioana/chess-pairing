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
	 * if the players have played against each other
	 * 
	 * @param playerA
	 * @param playerB
	 */
	public void addGame(Player playerA, Player playerB) {
		//
		
		
		Game game = Game.createGame(playerA,playerB);
		

		throw new IllegalStateException("Please implement thisAddGame");
	}
	
	
}