package eu.chessdata.chessparing.model;

import java.util.List;

public class ChessparingRound {
	private int roundNumber;
	private List<ChessparingGame> games;
	public int getRoundNumber() {
		return roundNumber;
	}
	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}
	public List<ChessparingGame> getGames() {
		return games;
	}
	public void setGames(List<ChessparingGame> games) {
		this.games = games;
	}
	
	
}
