package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.List;

public class PairingResult {
	private boolean ok;
	private List<Game> games;
	
	/**
	 * it attempts to create the games and if it does not succeed it invalidates the result
	 * @param players
	 * @param indexA
	 * @param indexB
	 */
	public PairingResult(final List<Player> players, Integer[] indexA, Integer[]indexB){
		this.ok = true;
		this.games = new ArrayList<>();
		
		for(int i=0;i<indexA.length;i++){
			Player a = players.get(indexA[i]);
			Player b = players.get(indexB[i]);
			Game game = Game.createGame(a, b);
			if (!game.isValid()){
				//invalidate the result and break the loop
				resultIsNoGood();
				break;
			}
			this.games.add(game);
		}
	}
	
	public boolean isOk() {
		return ok;
	}
	
	public List<Game> getGames() {
		return games;
	}
	public void addGame(Game game){
		this.games.add(game);
	}
	
	/**
	 * it makes sure too release the resources and to set the inner boolean value to false;
	 */
	private void resultIsNoGood(){
		this.games= null;
		this.ok = false;
	}
}
