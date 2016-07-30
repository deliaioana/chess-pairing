package eu.chessdata.chessparing.model;

public class ChessparingGame {
	/**
	 * it allays starts from 1
	 */
	private int tableNumber;

	private ChessparingPlayer whitePlayer;
	private ChessparingPlayer blackPlayer;

	/**
	 * 0 still not decided, 1 white player wins, 2 black player wins, 3 draw
	 * game, 4 no partner
	 */
	private int result = 0;

	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public ChessparingPlayer getWhitePlayer() {
		return whitePlayer;
	}

	public void setWhitePlayer(ChessparingPlayer whitePlayer) {
		this.whitePlayer = whitePlayer;
	}

	public ChessparingPlayer getBlackPlayer() {
		return blackPlayer;
	}

	public void setBlackPlayer(ChessparingPlayer blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
	
}
