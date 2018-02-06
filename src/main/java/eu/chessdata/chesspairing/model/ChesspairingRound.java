package eu.chessdata.chesspairing.model;

import java.util.ArrayList;
import java.util.List;

public class ChesspairingRound {
	private int roundNumber;
	private List<ChesspairingPlayer> presentPlayers = new ArrayList<>();
	private List<ChesspairingPlayer> absentPlayers = new ArrayList<>();
	private List<ChesspairingPlayer> upfloaters = new ArrayList<>();
	private List<ChesspairingPlayer> downfloaters = new ArrayList<>();
	private List<ChesspairingGame> games = new ArrayList<>();

	/**
	 * A round has games if the games list is not null or is size is grater then
	 * zero
	 * 
	 * @return boolean value
	 */
	public boolean hasGames() {
		if (this.games == null) {
			return false;
		}
		if (this.games.size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * It checks if the player is part of the absentPlayers list
	 * 
	 * @return true when the player is part of the list and false when the
	 *         player is not part of the absent list
	 */
	public boolean playerAbsent(ChesspairingPlayer player) {
		if (absentPlayers == null) {
			throw new IllegalStateException("Absent plaeyrs ware not initialized");
		}
		if (absentPlayers.contains(player)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * It ads a player to the list of absent players. If the player is also in
	 * the list with present players It will remove it from there.
	 * 
	 * @param player
	 */
	public void addAbsentPlayer(ChesspairingPlayer player) {

		if (presentPlayers == null) {
			presentPlayers = new ArrayList<>();
		}
		if (absentPlayers == null) {
			absentPlayers = new ArrayList<>();
		}

		if (this.presentPlayers.contains(player)) {
			int index = this.presentPlayers.indexOf(player);
			this.presentPlayers.remove(index);
		}

		if (this.absentPlayers.contains(player)) {
			return;
		} else {
			this.absentPlayers.add(player);
		}
	}

	/**
	 * It adds a player to the list of present players. If the player is also in
	 * the list with absent players It will remove it from there
	 * 
	 * @param player
	 */
	public void addPresentPlayer(ChesspairingPlayer player) {

		if (presentPlayers == null) {
			presentPlayers = new ArrayList<>();
		}
		if (absentPlayers == null) {
			absentPlayers = new ArrayList<>();
		}

		if (this.absentPlayers.contains(player)) {
			int index = this.absentPlayers.indexOf(player);
			this.absentPlayers.remove(index);
		}

		if (this.presentPlayers.contains(player)) {
			return;
		} else {
			this.absentPlayers.add(player);
		}
	}

	public int getRoundNumber() {
		return roundNumber;
	}

	public List<ChesspairingPlayer> getAbsentPlayers() {
		return absentPlayers;
	}

	public void setAbsentPlayers(List<ChesspairingPlayer> absentPlayers) {
		this.absentPlayers = absentPlayers;
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
