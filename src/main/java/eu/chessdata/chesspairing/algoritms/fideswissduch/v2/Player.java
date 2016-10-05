package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.List;

import eu.chessdata.chesspairing.model.ChesspairingPlayer;

public class Player {
	String playerKey;
	String name;
	Integer initialRanking;
	Integer elo;
	List<Integer> colourHistory;
	List<String> playersHistory;
	Double pairingPoints;

	public Player(ChesspairingPlayer player, int initialRanking) {
		this.playerKey = player.getPlayerKey();
		this.name = player.getName();
		this.initialRanking = initialRanking;
		this.elo = player.getElo();
		this.playersHistory = new ArrayList<>();
		this.colourHistory = new ArrayList<>();
		this.pairingPoints = 0.0;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Player: " + this.playerKey + " " + this.name
				+ "\n\t initialRanking:\t"+initialRanking
				+ "\n\t elo:\t"+elo
				+ "\n\t points:\t"+pairingPoints
				+ "\n\t colourHistory:\t"+colourHistory
				+ "\n\t playersHistory:\t");
		for (String key: playersHistory){
			sb.append(key+", ");
		}
		return sb.toString();
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



	public List<Integer> getColourHistory() {
		return colourHistory;
	}



	public void setColourHistory(List<Integer> colourHistory) {
		this.colourHistory = colourHistory;
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
