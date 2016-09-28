package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.chessdata.chesspairing.algoritms.fideswissduch.Algorithm;
import eu.chessdata.chesspairing.model.ChesspairingByeValue;
import eu.chessdata.chesspairing.model.ChesspairingGame;
import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingResult;
import eu.chessdata.chesspairing.model.ChesspairingRound;
import eu.chessdata.chesspairing.model.ChesspairingTournament;

public class FideSwissDutch implements Algorithm {
	private ChesspairingTournament tournament;
	private Map<String, ChesspairingPlayer> allPlayersMap;
	private Set<String> presentPlayers;
	private Map<Integer, ChesspairingRound> roundsMap;
	private Map<String, List<Integer>> colourHistory;
	private Map<String, List<String>> opponentsHistory;

	/**
	 * if player got point buy buy or by the adversary not present then for
	 * paring purposes only disregard that point.
	 */

	private Map<String, Double> pairingPoints;

	/**
	 * this is the round that wee will generate
	 */
	private Integer generationRoundId;

	@Override
	public ChesspairingTournament generateNextRound(ChesspairingTournament tournament) {
		initializeAlgorithm(tournament);
		return null;
	}

	// meant to calculate current tournament state.
	private void initializeAlgorithm(ChesspairingTournament tournament) {
		this.tournament = tournament;
		computeAllPlayersMap();
		computeRoundsMap();
		computeGenerationRoundId();
		computePresentPlayers();
		computeColourHistory();
		computeOpponentsHistory();

	}

	/**
	 * it recursively computes the round points
	 * 
	 * @param roundNumber
	 * @param playerKey
	 */
	protected Double getRoundPoints(Integer roundNumber, String playerKey) {

		Double previousPoints = 0.0;
		if (roundNumber > 1) {
			previousPoints = getRoundPoints(roundNumber - 1, playerKey);
		}
		ChesspairingRound round = getRound(roundNumber);
		for (ChesspairingGame game : round.getGames()) {

			String whiteKey = game.getWhitePlayer().getPlayerKey();
			if (playerKey.equals(whiteKey)) {
				double currentPoints = previousPoints + getPointsFromGame(game, playerKey);
				return currentPoints;
			}
			ChesspairingResult result = game.getResult();
			if (result != ChesspairingResult.BYE) {
				// wee have a black player
				String blackKey = game.getBlackPlayer().getPlayerKey();
				if (playerKey.equals(blackKey)) {
					double currentPoints = previousPoints + getPointsFromGame(game, playerKey);
					return currentPoints;
				}
			}
		}
		// if wee reach this place then the player is not in the round
		return previousPoints;
	}

	/**
	 * return the player points from the game
	 * 
	 * if player not in the game throw error if player wins return 1 if player
	 * buy return 1 if draw return 0.5 if loses return 0
	 * 
	 * @param game
	 * @param playerKey
	 * @return
	 */
	private Double getPointsFromGame(ChesspairingGame game, String playerKey) {
		Double bye = 0.0;
		ChesspairingByeValue byeValue = this.tournament.getChesspairingByeValue();
		switch (byeValue) {
		case HALF_A_POINT:
			bye = 0.5;
			break;
		case ONE_POINT:
			bye = 1.0;
			break;
		default:
			throw new IllegalStateException("one more value for bye points? This does not sound write");
		}
		Double whin = 1.0;
		Double lost = 0.0;

		final ChesspairingResult result = game.getResult();

		// buy case
		if (result == ChesspairingResult.BYE) {
			// if key is not white the throw error
			if (!playerKey.equals(game.getWhitePlayer().getPlayerKey())) {
				throw new IllegalStateException("player key not in the game");
			}
			return bye;
		}

		String whiteKey = game.getWhitePlayer().getPlayerKey();
		if (playerKey.equals(whiteKey)) {
			if ((result == ChesspairingResult.WHITE_WINS) || (result == ChesspairingResult.WHITE_WINS_OPONENT_ABSENT)) {
				return whin;
			} else if ((result == ChesspairingResult.BLACK_WINS)
					|| (result == ChesspairingResult.BLACK_WINS_OPONENT_ABSENT)) {
				return lost;
			}
		}

		String blackKey = game.getBlackPlayer().getPlayerKey();
		if (blackKey.equals(playerKey)) {
			if ((result == ChesspairingResult.BLACK_WINS) || (result == ChesspairingResult.BLACK_WINS_OPONENT_ABSENT)) {
				return whin;
			} else if ((result == ChesspairingResult.WHITE_WINS)
					|| (result == ChesspairingResult.WHITE_WINS_OPONENT_ABSENT)) {
				return lost;
			}
		}

		throw new IllegalStateException(
				"Have no idea what I have missed. Pleas investigate. Posible also no result yet. Still please investigate");
	}

	/**
	 * it recursively computs the pairing points consider the buy only if it was
	 * given more then 2 rounds ago consider a win buy absence only if it was
	 * given more then 2 rounds ago
	 */
	protected Double getPairingPoints(Integer roundNumber, String playerKey) {

		int difference = this.generationRoundId - 2;
		if (roundNumber < difference) {
			// normal computing strategy
			return getRoundPoints(roundNumber, playerKey);
		}

		Double previousPoints = 0.0;
		if (roundNumber > 1) {
			previousPoints = getPairingPoints(roundNumber - 1, playerKey);
		}

		ChesspairingRound round = getRound(roundNumber);
		// if difference smaller than 2
		for (ChesspairingGame game : round.getGames()) {

			String whiteKey = game.getWhitePlayer().getPlayerKey();
			if (whiteKey.equals(playerKey)) {
				double currentPoints = previousPoints
						+ getPairingPointsFromGame(game, playerKey, round.getRoundNumber());
				return currentPoints;
			}

			ChesspairingResult result = game.getResult();
			if (result != ChesspairingResult.BYE) {
				// wee have a black player
				String blackKey = game.getBlackPlayer().getPlayerKey();
				if (blackKey.equals(playerKey)) {
					double currentPoints = previousPoints
							+ getPairingPointsFromGame(game, playerKey, round.getRoundNumber());
					return currentPoints;
				}
			}
		}

		// if wee reach this place then the player is not in the round
		return previousPoints;
	}

	/**
	 * returns the player points from the game. If this game is part of the last
	 * 2 rounds and the player wan by buy or by absence then return 0;
	 * 
	 * @param game
	 * @param playerKey
	 * @param roundId
	 * @return
	 */
	private Double getPairingPointsFromGame(ChesspairingGame game, String playerKey, int roundId) {
		int difference = this.generationRoundId - 2;
		if (roundId <= difference) {
			// standard evaluation applies;
			return getPointsFromGame(game, playerKey);
		}

		Double whin = 1.0;
		Double lost = 0.0;

		final ChesspairingResult result = game.getResult();

		// buy case
		if (result == ChesspairingResult.BYE) {
			// if key is not white the throw error
			if (!playerKey.equals(game.getWhitePlayer().getPlayerKey())) {
				throw new IllegalStateException("player key not in the game");
			}
			return lost;
		}

		String whiteKey = game.getWhitePlayer().getPlayerKey();
		if (playerKey.equals(whiteKey)) {
			if (result == ChesspairingResult.WHITE_WINS) {
				return whin;
			} else if ((result == ChesspairingResult.BLACK_WINS)
					|| (result == ChesspairingResult.BLACK_WINS_OPONENT_ABSENT)
					|| (result == ChesspairingResult.WHITE_WINS_OPONENT_ABSENT)) {
				return lost;
			}
		}

		String blackKey = game.getBlackPlayer().getPlayerKey();
		if (blackKey.equals(playerKey)) {
			if (result == ChesspairingResult.BLACK_WINS) {
				return whin;
			} else if ((result == ChesspairingResult.WHITE_WINS)
					|| (result == ChesspairingResult.WHITE_WINS_OPONENT_ABSENT)
					|| (result == ChesspairingResult.BLACK_WINS_OPONENT_ABSENT)) {
				return lost;
			}
		}

		throw new IllegalStateException(
				"Have no idea what I have missed. Pleas investigate. Posible also no result yet. Still please investigate");

	}

	private void computeOpponentsHistory() {
		this.opponentsHistory = new HashMap<>();
		for (int i = 1; i < this.generationRoundId; i++) {
			ChesspairingRound round = getRound(i);
			List<ChesspairingGame> games = round.getGames();
			for (ChesspairingGame game : games) {
				ChesspairingResult result = game.getResult();
				if (result != ChesspairingResult.BYE) {
					// wee have the partners
					String whiteKey = game.getWhitePlayer().getPlayerKey();
					String blackKey = game.getBlackPlayer().getPlayerKey();
					addToOpponentsHistory(whiteKey, blackKey);
				}
			}
		}
	}

	/**
	 * it updates the players opponents history of these 2 players
	 * 
	 * @param whiteKey
	 * @param blackKey
	 */
	private void addToOpponentsHistory(String whiteKey, String blackKey) {
		List<String> whiteHistory = getOponentsHistory(whiteKey);
		if (whiteHistory.contains(blackKey)) {
			throw new IllegalStateException("Wee have 2 players that have played against more then one time");
		}
		whiteHistory.add(blackKey);

		List<String> blackHistory = getOponentsHistory(blackKey);
		if (blackHistory.contains(whiteKey)) {
			throw new IllegalStateException("Wee have 2 players that have played against more then one time");
		}
		blackHistory.add(whiteKey);
	}

	private List<String> getOponentsHistory(String palyerKey) {
		List<String> adversers = this.opponentsHistory.get(palyerKey);
		if (adversers == null) {
			adversers = new ArrayList<>();
			this.opponentsHistory.put(palyerKey, adversers);
		}
		return adversers;
	}

	/**
	 * A.7 rules from
	 * https://www.fide.com/fide/handbook.html?id=167&view=article
	 * 
	 * The color difference of a player is the number of games played with white
	 * minus the number of games played with black
	 */
	private void computeColourHistory() {
		this.colourHistory = new HashMap<>();
		for (int i = 1; i < this.generationRoundId; i++) {
			ChesspairingRound round = getRound(i);
			List<ChesspairingGame> games = round.getGames();

			for (ChesspairingGame game : games) {
				ChesspairingResult result = game.getResult();
				if (result == ChesspairingResult.NOT_DECIDED) {
					throw new IllegalStateException("Found game with no result");
				}

				// wee are only interested if the color history if no buy
				if (result != ChesspairingResult.BYE) {
					ChesspairingPlayer whitePlayer = game.getWhitePlayer();
					List<Integer> whitePlayerHistory = getColorHistory(whitePlayer.getPlayerKey());
					whitePlayerHistory.add(1);

					ChesspairingPlayer blackPlayer = game.getBlackPlayer();
					List<Integer> blackPlayerHistory = getColorHistory(blackPlayer.getPlayerKey());
					blackPlayerHistory.add(-1);
				}
			}
		}
	}

	private List<Integer> getColorHistory(String playerKey) {
		List<Integer> history = this.colourHistory.get(playerKey);
		if (history == null) {
			history = new ArrayList<>();
			this.colourHistory.put(playerKey, history);
		}
		return history;
	}

	private void computePresentPlayers() {
		ChesspairingRound round = getRound(this.generationRoundId);
		List<ChesspairingPlayer> players = round.getPresentPlayers();
		this.presentPlayers = new HashSet<>();
		for (ChesspairingPlayer player : players) {
			if (!this.allPlayersMap.containsKey(player.getPlayerKey())) {
				throw new IllegalStateException(
						"playersMap does not conatin " + player.getPlayerKey() + "/" + player.getName());
			}
			presentPlayers.add(player.getPlayerKey());
		}
	}

	private ChesspairingRound getRound(Integer roundNumber) {
		ChesspairingRound round = this.roundsMap.get(roundNumber);
		if (round == null) {
			throw new IllegalStateException("Not able to find round nr: " + roundNumber);
		}
		return round;
	}

	private void computeAllPlayersMap() {
		this.allPlayersMap = new HashMap<>();
		List<ChesspairingPlayer> list = this.tournament.getPlayers();
		for (ChesspairingPlayer player : list) {
			this.allPlayersMap.put(player.getPlayerKey(), player);
		}
	}

	private void computeRoundsMap() {
		this.roundsMap = new HashMap<>();
		List<ChesspairingRound> list = this.tournament.getRounds();
		for (ChesspairingRound round : list) {
			this.roundsMap.put(round.getRoundNumber(), round);
		}
	}

	/**
	 * wee decide that the next round that wee will generate to be the first
	 * round that has no games.
	 */
	private void computeGenerationRoundId() {
		List<ChesspairingRound> list = this.tournament.getRounds();
		for (ChesspairingRound round : list) {
			if (round.getGames() == null || round.getGames().size() == 0) {
				this.generationRoundId = round.getRoundNumber();
				break;
			}
		}

		if (this.generationRoundId == null) {
			throw new IllegalStateException("The tournament does not conatain a round with no games");
		}

		// just make sure that all the rounds exist
		int finalRound = this.generationRoundId;
		for (int i = 1; i <= finalRound; i++) {
			ChesspairingRound round = this.roundsMap.get(i);
			if (round == null) {
				throw new IllegalStateException(
						"Wee are paring round " + finalRound + " but round " + i + " is missing");
			}
		}
	}

	// getters--------------------------
	public ChesspairingTournament getTournament() {
		return tournament;
	}

	public Map<String, ChesspairingPlayer> getPlayersMap() {
		return allPlayersMap;
	}

	public Set<String> getPresentPlayers() {
		return presentPlayers;
	}

	public Map<Integer, ChesspairingRound> getRoundsMap() {
		return roundsMap;
	}

	public Integer getGenerationRoundId() {
		return generationRoundId;
	}

}
