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
}
