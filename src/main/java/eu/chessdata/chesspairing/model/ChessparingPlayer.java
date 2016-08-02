package eu.chessdata.chesspairing.model;

import eu.chessdata.chesspairing.algoritms.fideswissduch.FideSwissDutchAlgorithm;

public class ChessparingPlayer {
	public static final String PRESENT = "present";
	public static final String ABSENT = "absent";
	
	private String name;
	private int elo;
	/**
	 * this is the the natural order that you wish to be considered for a tournament. 
	 *  {@link FideSwissDutchAlgorithm} also uses this number in making paring decisions. 
	 */
	private int initialOrderId;
	
	private String playerKey;
	private String presence;
	
	
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
	public String getPresence() {
		return presence;
	}
	public void setPresence(String presence) {
		this.presence = presence;
	}
	
	
}
