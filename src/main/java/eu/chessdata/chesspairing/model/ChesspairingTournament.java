package eu.chessdata.chesspairing.model;

import java.util.ArrayList;
import java.util.List;

public class ChesspairingTournament {
	private String name;
	private String description;
	/**
	 * this is the maximum allowed number of rounds in a tournament. 
	 * If you try to pare over this number some algorithms will just crash. 
	 */
	private int totalRounds;
	private ChesspairingByeValue chesspairingByeValue;
	private List<ChesspairingPlayer> players = new ArrayList<ChesspairingPlayer>();
	private List<ChesspairingRound> rounds = new ArrayList<ChesspairingRound>();
	private PairingSummary parringSummary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public ChesspairingByeValue getChesspairingByeValue() {
		return chesspairingByeValue;
	}

	public void setChesspairingByeValue(ChesspairingByeValue chesspairingByeValue) {
		this.chesspairingByeValue = chesspairingByeValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTotalRounds() {
		return totalRounds;
	}

	public void setTotalRounds(int totalRounds) {
		this.totalRounds = totalRounds;
	}

	public List<ChesspairingRound> getRounds() {
		return rounds;
	}

	public void setRounds(List<ChesspairingRound> rounds) {
		this.rounds = rounds;
	}

	public List<ChesspairingPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<ChesspairingPlayer> players) {
		this.players = players;
	}

	public PairingSummary getParringSummary() {
		return parringSummary;
	}

	public void setParringSummary(PairingSummary parringSummary) {
		this.parringSummary = parringSummary;
	}
	
	
}
