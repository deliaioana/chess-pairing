package eu.chessdata.chesspairing.model;

import java.util.ArrayList;
import java.util.List;

public class ChesspairingRound {
	private int roundNumber;
	private List<ChesspairingPlayer> presentPlayers = new ArrayList<>();
	private List<ChesspairingPlayer> upfloaters = new ArrayList<>();
	private List<ChesspairingPlayer> downfloaters = new ArrayList<>();
	private List<ChesspairingGame> games = new ArrayList<>();

	public int getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}

	public List<ChesspairingGame> getGames() {
		return games;
	}

	public void setGames(List<ChesspairingGame> games) {
		this.games = games;
	}

	public List<ChesspairingPlayer> getUpfloaters() {
		return upfloaters;
	}

	public void setUpfloaters(List<ChesspairingPlayer> upfloaters) {
		this.upfloaters = upfloaters;
	}

	public List<ChesspairingPlayer> getPresentPlayers() {
		return presentPlayers;
	}

	public void setPresentPlayers(List<ChesspairingPlayer> presentPlayers) {
		this.presentPlayers = presentPlayers;
	}

	public List<ChesspairingPlayer> getDownfloaters() {
		return downfloaters;
	}

	public void setDownfloaters(List<ChesspairingPlayer> downfloaters) {
		this.downfloaters = downfloaters;
	}

	/**
	 * It finds the game for a specific player and returns the game
	 * 
	 * @param player
	 *            that will be located
	 * @return a game
	 */
	public ChesspairingGame getGame(ChesspairingPlayer player) {
		for (ChesspairingGame game : games) {
			if (game.getWhitePlayer().equals(player)) {
				return game;
			}
			if (game.getResult() != ChesspairingResult.BYE) {
				if (game.getBlackPlayer().equals(player)) {
					return game;
				}
			}
		}
		throw new IllegalStateException("No game found");
	}

	/**
	 * It builds a round
	 * 
	 * @param nextRoundNumber
	 *            is the next round number
	 * @param games
	 *            is the list with the games
	 * @return a round
	 */
	public static ChesspairingRound buildRound(int nextRoundNumber, List<ChesspairingGame> games) {
		ChesspairingRound round = new ChesspairingRound();
		round.roundNumber = nextRoundNumber;

		// compute present players
		round.presentPlayers = new ArrayList<>();
		for (ChesspairingGame game : games) {
			round.presentPlayers.add(game.getWhitePlayer());
			if (game.getResult() != ChesspairingResult.BYE) {
				round.presentPlayers.add(game.getBlackPlayer());
			}
		}

		round.games = games;

		// just set empty downfloaters and upfloaters
		round.downfloaters = new ArrayList<>();
		round.upfloaters = new ArrayList<>();
		
		return round;
	}
}
