package eu.chessdata.chessparing.model;

import java.util.ArrayList;
import java.util.List;

public class ChessparingTournament {
	private String name;
	private String description;
	/**
	 * this is the maximum allowed number of rounds in a tournament. 
	 * If you try to pare over this number some algorithms will just crash. 
	 */
	private int totalRounds;
	private List<ChessparingPlayer> players = new ArrayList<ChessparingPlayer>();
	private List<ChessparingRound> rounds = new ArrayList<ChessparingRound>();
	private ParringSummary parringSummary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<ChessparingRound> getRounds() {
		return rounds;
	}

	public void setRounds(List<ChessparingRound> rounds) {
		this.rounds = rounds;
	}

	public List<ChessparingPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<ChessparingPlayer> players) {
		this.players = players;
	}

	public ParringSummary getParringSummary() {
		return parringSummary;
	}

	public void setParringSummary(ParringSummary parringSummary) {
		this.parringSummary = parringSummary;
	}
	
	
}
