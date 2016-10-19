package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.comparators.ComparatorChain;

import eu.chessdata.chesspairing.Tools;

public class ScoreBracket {
	private final FideSwissDutch fideSwissDutch;
	private final Double bracketScore;
	private final List<Player> bracketPlayers;

	public ScoreBracket(FideSwissDutch fideSwissDutch, Double bracketScore) {
		this.fideSwissDutch = fideSwissDutch;
		this.bracketPlayers = new ArrayList<>();

		this.bracketScore = bracketScore;
	}

	public FideSwissDutch getFideSwissDutch() {
		return fideSwissDutch;
	}

	public Double getBracketScore() {
		return bracketScore;
	}

	public List<Player> getBracketPlayers() {
		return bracketPlayers;
	}

	public void addPlayer(Player player) {
		if (this.bracketPlayers.contains(player)) {
			throw new IllegalStateException("Trining to add same player 2 times");
		}

		player.setFloatingState(getFloatingState(player));
		this.bracketPlayers.add(player);
	}

	private FloatingState getFloatingState(Player player) {
		FloatingState floatingState = FloatingState.STANDARD;

		Double points = player.getPairingPoints();
		if (points < this.bracketScore) {
			floatingState = FloatingState.UPFLOATER;

		} else if (points > this.bracketScore) {
			floatingState = FloatingState.DOWNFLOATER;

		}
		return floatingState;
	}

	protected void sortPlayers() {
		ComparatorChain<Player> comparatorChain = new ComparatorChain<>();
		comparatorChain.addComparator(Player.byPoints);
		comparatorChain.addComparator(Player.byElo);
		comparatorChain.addComparator(Player.byInitialRanking);

		Collections.sort(this.bracketPlayers, comparatorChain);
	}

	/**
	 * if nextBraket is null then this is the last bracket
	 * 
	 * @param lastRound
	 * @param nextBraket
	 */
	public boolean pareBraket(boolean lastRound, ScoreBracket nextBraket) {
		// compute initial state
		if (nextBraket == null) {
			pareLastBracket();
		}
		int playersCount = this.bracketPlayers.size();
		boolean even = true;
		if ((playersCount % 2) == 1) {
			even = false;
		}
		this.sortPlayers();
		// get all the permutations
		Integer[] intArray = new Integer[this.bracketPlayers.size()];
		for (int i = 0; i < this.bracketPlayers.size(); i++) {
			intArray[i] = i;
		}
		Set<Integer[]>permutations = Tools.getPermutations(intArray);
		List<GameList> gameLists = new ArrayList<>();
		for (Integer[] permutation: permutations){
			GameList gameList = computeGameList(permutation);
		}
		//
		if (!lastRound) {

		} else {

		}
		throw new IllegalStateException("Please implement pareBracket");
	}

	/**
	 * it takes the players 2 by 2 and computes the games
	 * @param permutation
	 * @return
	 */
	private GameList computeGameList(Integer[] permutation) {
		Player playerA = null;
		Player playerB = null;
		GameList gameList = new GameList();
		for(int i=0;i<permutation.length;i++){
			if ((i%2)==0){
				playerA = this.bracketPlayers.get(permutation[i]);
				if (i==permutation.length){
					gameList.setNoPartner(playerA);
				}
			}else{
				playerB =this.bracketPlayers.get(permutation[i]);
				gameList.addGame(playerA,playerB);
				//reset the players
				playerA = null;
				playerB = null;
			}
		}
		throw new IllegalStateException("Please implement computeGameList");
	}

	public boolean pareLastBracket() {
		throw new IllegalStateException("Please implement pareLastRound");
	}
}
