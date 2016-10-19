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
	protected final List<Double> order;

	public PairingTool(FideSwissDutch fideSwissDutch) {
		this.fideSwissDutch = fideSwissDutch;
		this.players = new HashSet<>();
		this.generationRoundId = fideSwissDutch.getGenerationRoundId();
		this.scoreBrackets = new HashMap<>();
		this.order = new ArrayList<>();
	}

	public void computeGames() {
		// TODO Auto-generated method stub
		if (this.generationRoundId == 1) {
			throw new IllegalStateException("Please implement first round");
		}
		initializePlayers();
		initializeScoreBrackets();
		pairBrackets();
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

		// sort the brackets
		for (Entry<Double, ScoreBracket> entry : this.scoreBrackets.entrySet()) {
			Double key = entry.getKey();
			this.order.add(key);
		}
		
		//set the order
		Collections.sort(order,Collections.reverseOrder());
	}

	/**
	 * Cycles all the brackets and initializes pairing
	 */
	protected void pairBrackets() {
		for(Double key:this.order){
			pairBracketStandard(key);
		}
	}
	
	/**
	 * for the moment my intention is to try to pare this and if not possible downfloat all players to the next bracket
	 * @param key
	 * @return
	 */
	protected void pairBracketStandard(Double key){
		ScoreBracket bracket = this.scoreBrackets.get(key);
		//split the players and pair them in order
		boolean lastRound = false;
		if (this.order.indexOf(key)<(this.order.size()-1)){
			//not last round
			int indexOfKey = this.order.indexOf(key);
			Double nextKey = this.order.get(indexOfKey + 1);
			ScoreBracket nextBraket = this.scoreBrackets.get(nextKey);
			bracket.pareBraket(lastRound,nextBraket);
		}else{
			//last round
			throw new IllegalStateException("please implement last round");
		}
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
	 * 
	 * @param playerKey
	 * @return
	 */
	public Player get(String key) {
		for (Player player : players) {
			String playerKey = player.getPlayerKey();
			if (key.equals(playerKey)) {
				return player;
			}
		}
		throw new IllegalStateException("Player not in the players set");
	}
}
