package eu.chessdata.chesspairing.model;

import eu.chessdata.chesspairing.model.ChesspairingResult;

public class ChesspairingGame {

	/**
	 * it allays starts from 1
	 */
	private int tableNumber;

	private ChesspairingPlayer whitePlayer;
	private ChesspairingPlayer blackPlayer;

	/**
	 * 0 still not decided, 1 white player wins, 2 black player wins, 3 draw
	 * game, 4 no partner
	 */
	private ChesspairingResult result = ChesspairingResult.NOT_DECIDED;

	public ChesspairingGame(int tableNumber, ChesspairingPlayer whitePlayer, ChesspairingPlayer blackPlayer) {
		this.tableNumber = tableNumber;
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.result = ChesspairingResult.NOT_DECIDED;
	}

	public ChesspairingGame() {
	}

	@Override
	public String toString() {
		String whitePlayerKey = whitePlayer.getPlayerKey();
		String blackPlayerKey = "buy";
		if (this.result!= ChesspairingResult.BYE){
			blackPlayerKey = blackPlayer.getPlayerKey();
		}
		return (String.valueOf(tableNumber) + "(" + whitePlayerKey + "-" + blackPlayerKey
				+ ")");
	}

	// getters and setters
	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public ChesspairingPlayer getWhitePlayer() {
		return whitePlayer;
	}

	public void setWhitePlayer(ChesspairingPlayer whitePlayer) {
		this.whitePlayer = whitePlayer;
	}

	public ChesspairingPlayer getBlackPlayer() {
		return blackPlayer;
	}

	public void setBlackPlayer(ChesspairingPlayer blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	public ChesspairingResult getResult() {
		return result;
	}

	public void setResult(ChesspairingResult result) {
		this.result = result;
	}

	/**
	 * Static method used to build a buy game
	 * @param tableNumber
	 * @param indexWhite
	 * @return
	 */
	public static ChesspairingGame buildBuyGame(int tableNumber, ChesspairingPlayer whitePlayer) {
		ChesspairingGame buyGame = new ChesspairingGame();
		buyGame.tableNumber = tableNumber;
		buyGame.whitePlayer = whitePlayer;
		
	}
}
