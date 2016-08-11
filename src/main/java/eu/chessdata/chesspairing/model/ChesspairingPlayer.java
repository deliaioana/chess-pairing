package eu.chessdata.chesspairing.model;

import eu.chessdata.chesspairing.algoritms.fideswissduch.FideSwissDutchAlgorithm;

public class ChesspairingPlayer {
	private String name;
	private int elo;
	/**
	 * this is the the natural order that you wish to be considered for a tournament. 
	 *  {@link FideSwissDutchAlgorithm} also uses this number in making paring decisions. 
	 */
	private int initialOrderId;
	
	private String playerKey;
	private boolean isPresent;
	private int acceleratedPoints;
	private int tournamentPoints;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.playerKey;
	}
	
	//getters and setters
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
	
	public int getAcceleratedPoints() {
		return acceleratedPoints;
	}
	public void setAcceleratedPoints(int acceleratedPoints) {
		this.acceleratedPoints = acceleratedPoints;
	}
	public boolean isPresent() {
		return isPresent;
	}
	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}
	public int getTournamentPoints() {
		return tournamentPoints;
	}
	public void setTournamentPoints(int tournamentPoints) {
		this.tournamentPoints = tournamentPoints;
	}
	
}
