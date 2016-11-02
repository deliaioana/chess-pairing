package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.swing.plaf.synth.SynthSeparatorUI;

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
	 * main algorithm for pairing bracket
	 * if nextBraket is null then this is the last bracket
	 * 
	 * @param lastRound
	 * @param nextBraket
	 */
	public boolean pareBraket(boolean lastRound, ScoreBracket nextBraket) {
		System.out.println("Pairing bracket: "+ this.bracketScore+" ("+lastRound+")" );
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
		
		//set the initial pairs
		/**
		 * TODO: create private PairingResult pare(Integer[] groupA, Integer[]groupB);
		 * PairingResut
		 * boolean isOk
		 * getGames returns List<Games>
		 */
		int size = playersCount / 2;
		Integer groupA[] = new Integer[size];
		Integer groupB[] = new Integer[size];
		for (int i=0;i<size;i++){
			groupA[i]=i;
			groupB[i]=i+size;
		}
		
		Set<Integer[]>permutations = Tools.getPermutations(groupA);
		for (Integer[] array:permutations){
			StringBuffer sb = new StringBuffer();
			for (Integer index:array){
				sb.append(String.valueOf(index)+", ");
			}
			System.out.println("permutation: " + sb.toString());
		}
		
		throw new IllegalStateException("Please implement pareBracket");
	}
	
	
	




	public boolean pareLastBracket() {
		throw new IllegalStateException("Please implement pareLastRound");
	}
}
