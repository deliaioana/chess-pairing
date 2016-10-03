package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;

public class PairingTool {
	private final FideSwissDutch fideSwissDutch;
	private final Map<String, Player> players;

	public PairingTool(FideSwissDutch fideSwissDutch) {
		this.fideSwissDutch = fideSwissDutch;
		this.players = new HashMap<>();
	}

	public void computeGames() {
		// TODO Auto-generated method stub
		initializePlayers();
	}

	/**
	 * it initializes only the present players;
	 */
	private void initializePlayers() {
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
		for (Entry<String, Player> set : players.entrySet()) {
			if (this.fideSwissDutch.getGenerationRoundId() == 1) {
				break;
			}

			int roundNumber = this.fideSwissDutch.getGenerationRoundId() - 1;
			set.getValue().setPairingPoints(this.fideSwissDutch.getPairingPoints(roundNumber, set.getKey()));
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
		
		//set the points
		if (this.fideSwissDutch.getGenerationRoundId()>1){
			
		}
	}
}
