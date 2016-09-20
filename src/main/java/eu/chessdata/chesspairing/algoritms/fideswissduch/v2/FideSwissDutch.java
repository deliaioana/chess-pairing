package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.chessdata.chesspairing.algoritms.fideswissduch.Algorithm;
import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingRound;
import eu.chessdata.chesspairing.model.ChesspairingTournament;

public class FideSwissDutch implements Algorithm {
	private ChesspairingTournament tournament;
	private Map<String, ChesspairingPlayer> playersMap;
	private Set<String> presentPlayers;
	private Map<Integer, ChesspairingRound> roundsMap;

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
		computePlayersMap();
		computeRoundsMap();
		computeGenerationRoundId();
		computePresentPlayers();
	}

	private void computePresentPlayers() {
		ChesspairingRound round = getRound(this.generationRoundId);
		List<ChesspairingPlayer> players = round.getPresentPlayers();
		this.presentPlayers = new HashSet<>();
		for (ChesspairingPlayer player : players) {
			if (!this.playersMap.containsKey(player.getPlayerKey())) {
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

	private void computePlayersMap() {
		this.playersMap = new HashMap<>();
		List<ChesspairingPlayer> list = this.tournament.getPlayers();
		for (ChesspairingPlayer player : list) {
			this.playersMap.put(player.getPlayerKey(), player);
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
		return playersMap;
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
