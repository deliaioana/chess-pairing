package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.List;

public class PairingResult {
	private boolean ok;
	private List<Game> games;

	/**
	 * it attempts to create the games and if it does not succeed it invalidates
	 * the result
	 * 
	 * @param players
	 * @param indexA
	 * @param indexB
	 */
	public PairingResult(final List<Player> players, Integer[] indexA, Integer[] indexB) {
		this.ok = true;
		this.games = new ArrayList<>();

		for (int i = 0; i < indexA.length; i++) {
			Player a = players.get(indexA[i]);
			Player b = players.get(indexB[i]);
			Game game = Game.createGame(a, b);
			if (!game.isValid()) {
				// invalidate the result and break the loop
				resultIsNoGood();
				break;
			}
			this.games.add(game);
		}
	}

	/**
	 * this creates games 2 by 2 in the order specified by the index
	 * 
	 * @param players
	 * @param index
	 */
	public PairingResult(final List<Player> players, Integer[] index) {
		this.ok = true;
		this.games = new ArrayList<>();
		for (int i = 0; i < index.length; i++) {
			if ((i % 2) == 0) {
				if (i == index.length - 1) {
					// if last game
					Player player = players.get(i);
					if (player.wasBuy()) {
						// if player was buy then result no good
						resultIsNoGood();
						break;
					} else {
						Game game = Game.createBuyGame(player);
						if (game.isValid()) {
							this.games.add(game);
						} else {
							resultIsNoGood();
							break;
						}
					}
				}
			} else {
				// there is always i and i-1;
				Player a = players.get(i);
				Player b = players.get(i - 1);
				Game game = Game.createGame(a, b);
				if (game.isValid()) {
					this.games.add(game);
				} else {
					resultIsNoGood();
					break;
				}

			}
		}
	}

	public boolean isOk() {
		return ok;
	}

	public List<Game> getGames() {
		return games;
	}

	public void addGame(Game game) {
		this.games.add(game);
	}

	/**
	 * it makes sure too release the resources and to set the inner boolean
	 * value to false;
	 */
	private void resultIsNoGood() {
		this.games = null;
		this.ok = false;
	}
}
