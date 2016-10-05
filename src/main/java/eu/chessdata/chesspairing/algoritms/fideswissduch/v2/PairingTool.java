package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;

public class PairingTool {
	private final FideSwissDutch fideSwissDutch;
	protected final Map<String, Player> players;
	private final int generationRoundId;
	protected final Map<Double,List<ScoreBracket>> scoreBrackets;

	public PairingTool(FideSwissDutch fideSwissDutch) {
		this.fideSwissDutch = fideSwissDutch;
		this.players = new HashMap<>();
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
		
	}

	/**
	 * it initializes only the present players;
	 */
	protected void initializePlayers() {
		// set the present players
		List<ChesspairingPlayer> chesspairingPlayers = this.fideSwissDutch.getPresentPlayersList();
		for (ChesspairingPlayer chesspairingPlayer : chesspairingPlayers) {
			if (!this.players.containsKey(chesspairingPlayer.getPlayerKey())) {
				int initialRanking = this.fideSwissDutch.getInitialRanking(chesspairingPlayer.getPlayerKey());
				Player player = new Player(chesspairingPlayer, initialRanking);
				this.players.put(player.getPlayerKey(), player);
			}
		}

		// set the pairing points
		if (this.fideSwissDutch.getGenerationRoundId() > 1) {
			for (Entry<String, Player> set : players.entrySet()) {
				int roundNumber = this.fideSwissDutch.getGenerationRoundId() - 1;
				set.getValue().setPairingPoints(this.fideSwissDutch.getPairingPoints(roundNumber, set.getKey()));
			}
		}

		// set the paring history
		if (this.fideSwissDutch.getGenerationRoundId() > 1) {
			for (Entry<String, Player> set : players.entrySet()) {
				String key = set.getKey();
				Player player = set.getValue();
				List<String> list = this.fideSwissDutch.getOponents(key);
				List<String> history = player.getPlayersHistory();
				history.addAll(list);
			}
		}

		// set the points
		if (this.fideSwissDutch.getGenerationRoundId() > 1) {
			for (Entry<String, Player> set: players.entrySet()){
				String key = set.getKey();
				Player player = set.getValue();
				
				int roundNr = this.generationRoundId -1;
				Double pairingPoints = this.fideSwissDutch.getPairingPoints(roundNr, key);
				player.setPairingPoints(pairingPoints);
			}
		}
		
		//compute the color preference
		
		if (this.generationRoundId > 1){
			for (Entry<String,Player> set:players.entrySet()){
				String key = set.getKey();
				Player player = set.getValue();
				List<Integer> list = this.fideSwissDutch.getColorHistory(key);
				List<Integer> history = player.getColourHistory();
				history.addAll(list);
			}
		}				
	}
}
