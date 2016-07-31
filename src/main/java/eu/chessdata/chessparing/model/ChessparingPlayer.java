package eu.chessdata.chessparing.model;

import eu.chessdata.chessparing.algoritms.fideswissduch.FideSwissDutchAlgorithm;

public class ChessparingPlayer {
	private String name;
	private int elo;
	/**
	 * this is the the natural order that you wish to be considered for a tournament. 
	 *  {@link FideSwissDutchAlgorithm} also uses this number in making paring decisions. 
	 */
	private int initialOrderId;
	
	private String playerKey;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getElo() {
		return elo;
	}
	public void setElo(int elo) {
		this.elo = elo;
	}
	public String getPlayerKey() {
		return playerKey;
	}
	public void setPlayerKey(String playerKey) {
		this.playerKey = playerKey;
	}
	public int getInitialOrderId() {
		return initialOrderId;
	}
	public void setInitialOrderId(int initialOrderId) {
		this.initialOrderId = initialOrderId;
	}
	
	
}
