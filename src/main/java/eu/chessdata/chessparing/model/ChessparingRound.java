package eu.chessdata.chessparing.model;

import java.util.ArrayList;
import java.util.List;

public class ChessparingRound {
	private int roundNumber;
	private List<ChessparingPlayer> upfloaters = new ArrayList<ChessparingPlayer>();
	private List<ChessparingGame> games = new ArrayList<ChessparingGame>();
	
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
	public List<ChessparingPlayer> getUpfloaters() {
		return upfloaters;
	}
	public void setUpfloaters(List<ChessparingPlayer> upfloaters) {
		this.upfloaters = upfloaters;
	}
}
