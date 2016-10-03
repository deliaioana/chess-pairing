package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.List;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;

public class Player {
	String playerKey;
	String name;
	Integer initialRanking;
	Integer elo;
	Integer colorPreference;
	List<String> playersHistory;
	Double pairingPoints;

	public Player(ChesspairingPlayer player, int initialRanking) {
		this.playerKey = player.getPlayerKey();
		this.name = player.getName();
		this.initialRanking = initialRanking;
		this.elo = player.getElo();
		this.colorPreference = 0;
		this.playersHistory = new ArrayList<>();
		this.pairingPoints = 0.0;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getPlayerKey() {
		return playerKey;
	}



	public void setPlayerKey(String playerKey) {
		this.playerKey = playerKey;
	}



	public Integer getInitialRanking() {
		return initialRanking;
	}

	public void setInitialRanking(Integer initialRanking) {
		this.initialRanking = initialRanking;
	}

	public Integer getElo() {
		return elo;
	}

	public void setElo(Integer elo) {
		this.elo = elo;
	}

	public Integer getColorPreference() {
		return colorPreference;
	}

	public void setColorPreference(Integer colorPreference) {
		this.colorPreference = colorPreference;
	}

	public List<String> getPlayersHistory() {
		return playersHistory;
	}

	public void setPlayersHistory(List<String> playersHistory) {
		this.playersHistory = playersHistory;
	}

	public Double getPairingPoints() {
		return pairingPoints;
	}

	public void setPairingPoints(Double pairingPoints) {
		this.pairingPoints = pairingPoints;
	}
}
