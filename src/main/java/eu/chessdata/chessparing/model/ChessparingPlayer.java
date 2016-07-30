package eu.chessdata.chessparing.model;

public class ChessparingPlayer {
	private String name;
	private int elo;
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
	
	
}
