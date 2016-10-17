package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;

public class PairingTool {
	private final FideSwissDutch fideSwissDutch;
	protected final Set<Player> players;
	private final int generationRoundId;
	protected final Map<Double, ScoreBracket> scoreBrackets;

	public PairingTool(FideSwissDutch fideSwissDutch) {
		this.fideSwissDutch = fideSwissDutch;
		this.players = new HashSet<>();
		this.generationRoundId = fideSwissDutch.getGenerationRoundId();
		this.scoreBrackets = new HashMap<>();
	}

	public void computeGames() {
		// TODO Auto-generated method stub
		initializePlayers();
		initializeScoreBrackets();
	}

	/**
	 * for each player it puts him in his default bracket
	 */
	protected void initializeScoreBrackets() {
		int lastRound = this.fideSwissDutch.getGenerationRoundId() - 1;
		for (Player player : this.players) {
			String key = player.getPlayerKey();
			Double score = this.fideSwissDutch.getPairingPoints(lastRound, key);
			if (!this.scoreBrackets.containsKey(score)) {
				ScoreBracket braket = new ScoreBracket(fideSwissDutch, score);
				this.scoreBrackets.put(score, braket);
			}
			ScoreBracket braket = this.scoreBrackets.get(score);
			// ad the player to the braket

			braket.addPlayer(player);
		}
		
		//order the brackets by linking them
		List<Double> keys = new ArrayList<>();
		for (Entry<Double,ScoreBracket> entry: scoreBrackets.entrySet()){
			keys.add(entry.getKey());
		}
		
		//TODO start the pairing algorithm
	}

	/**
	 * it initializes only the present players;
	 */
	protected void initializePlayers() {
		// set the present players
		List<ChesspairingPlayer> chesspairingPlayers = this.fideSwissDutch.getPresentPlayersList();
		for (ChesspairingPlayer chesspairingPlayer : chesspairingPlayers) {
			int initialRanking = this.fideSwissDutch.getInitialRanking(chesspairingPlayer.getPlayerKey());
			Player player = new Player(chesspairingPlayer, initialRanking);
			if (!this.players.contains(player)) {

				this.players.add(player);
			}
		}

		// set the pairing points
		if (this.fideSwissDutch.getGenerationRoundId() > 1) {
			for (Player player : players) {
				int roundNumber = this.fideSwissDutch.getGenerationRoundId() - 1;
				player.setPairingPoints(this.fideSwissDutch.getPairingPoints(roundNumber, player.getPlayerKey()));
			}
		}

		// set the paring history
		if (this.fideSwissDutch.getGenerationRoundId() > 1) {
			for (Player player : players) {
				String key = player.getPlayerKey();
				List<String> list = this.fideSwissDutch.getOponents(key);
				List<String> history = player.getPlayersHistory();
				history.addAll(list);
			}
		}

		// set the points
		if (this.fideSwissDutch.getGenerationRoundId() > 1) {
			for (Player player : players) {
				String key = player.getPlayerKey();

				int roundNr = this.generationRoundId - 1;
				Double pairingPoints = this.fideSwissDutch.getPairingPoints(roundNr, key);
				player.setPairingPoints(pairingPoints);
			}
		}

		// compute the color preference

		if (this.generationRoundId > 1) {
			for (Player player : players) {
				String key = player.getPlayerKey();
				List<Integer> list = this.fideSwissDutch.getColorHistory(key);
				List<Integer> history = player.getColourHistory();
				history.addAll(list);
			}
		}
	}

	/**
	 * gets the player by player key
	 * @param playerKey
	 * @return
	 */
	public Player get(String key) {
		for (Player player:players){
			String playerKey = player.getPlayerKey();
			if (key.equals(playerKey)){
				return player;
			}
		}
		throw new IllegalStateException("Player not in the players set");
	}
}
