package eu.chessdata.chesspairing.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChesspairingTournament {
	private String name;
	private String description;
	private String city;
	private String federation;
	private Date dateOfStart;
	private Date dateOfEnd;
	private String typeOfTournament;
	private String ChifArbiter;
	private String deputyChifArbiters;
	/**
	 * this is the maximum allowed number of rounds in a tournament. If you try to
	 * pare over this number some algorithms will just crash.
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFederation() {
		return federation;
	}

	public void setFederation(String federation) {
		this.federation = federation;
	}

	public Date getDateOfStart() {
		return dateOfStart;
	}

	public void setDateOfStart(Date dateOfStart) {
		this.dateOfStart = dateOfStart;
	}

	public Date getDateOfEnd() {
		return dateOfEnd;
	}

	public void setDateOfEnd(Date dateOfEnd) {
		this.dateOfEnd = dateOfEnd;
	}

	public String getTypeOfTournament() {
		return typeOfTournament;
	}

	public void setTypeOfTournament(String typeOfTournament) {
		this.typeOfTournament = typeOfTournament;
	}

	public String getChifArbiter() {
		return ChifArbiter;
	}

	public void setChifArbiter(String chifArbiter) {
		ChifArbiter = chifArbiter;
	}

	public String getDeputyChifArbiters() {
		return deputyChifArbiters;
	}

	public void setDeputyChifArbiters(String deputyChifArbiters) {
		this.deputyChifArbiters = deputyChifArbiters;
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

	/**
	 * It returns the player in {@link #players} by player key
	 * 
	 * @param playerKey
	 *            is the key of a specific player
	 * @return a player and throws exception if player does not exist
	 */
	public ChesspairingPlayer getPlayer(String playerKey) {
		for (ChesspairingPlayer player : players) {
			String key = player.getPlayerKey();
			if (key.equals(playerKey)) {
				return player;
			}
		}
		throw new IllegalStateException("Player does not exist in the players list");
	}

	
	/**
	 * TODO implement this
	 * @param indexWhite
	 * @return
	 */
	public ChesspairingPlayer getPlayerByInitialRank(int indexWhite) {
		// TODO Auto-generated method stub
		return null;
	}

}
