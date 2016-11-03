package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.comparators.ComparatorChain;

import eu.chessdata.chesspairing.Tools;

public class ScoreBracket {
	private final FideSwissDutch fideSwissDutch;
	private final Double bracketScore;
	private final List<Player> bracketPlayers;
	private PairingResult bracketResult;
	private Boolean lastBracket;
	private ScoreBracket nextBracket;
	
	

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
			this.lastBracket = true;
			pareLastBracket();
		}
		this.nextBracket = nextBraket;
		
		int playersCount = this.bracketPlayers.size();
		
		this.sortPlayers();
		
		//set the initial pairs
		int size = playersCount / 2;
		Integer groupA[] = new Integer[size];
		Integer groupB[] = new Integer[size];
		for (int i=0;i<size;i++){
			groupA[i]=i;
			groupB[i]=i+size;
		}
		PairingResult pairingResult = new PairingResult(bracketPlayers, groupA, groupB);
		if(pairingResult.isOk()){
			processResult(pairingResult);
			return true;
		}
		
		
		Set<Integer[]>permutations = Tools.getPermutations(groupB);
		for(Integer[] permutation:permutations){
			PairingResult permResult = new PairingResult(bracketPlayers, groupA, permutation);
			if (permResult.isOk()){
				processResult(permResult);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * set the result
	 * downfloat not paired
	 * if last round set as buy not paired.
	 * make sure that all the players have bean paired
	 */
	private void processResult(PairingResult result){
		this.bracketResult = result;
		
		//make sure that all players have bean paired
		Set<Player> set = new HashSet<>();
		set.addAll(this.bracketPlayers);
		if (set.size()!= bracketPlayers.size()){
			throw new IllegalStateException("duplicate in bracketPlayers");
		}
		
		//identify not paired player;
		List<Game> games = result.getGames();
		for (Game game:games){
			Player white = game.getWhite();
			Player black = game.getBlack();
			if(!set.remove(white)){
				throw new IllegalStateException("The set did not contained white player");
			}
			if (!set.remove(black)){
				throw new IllegalStateException("The set did not contained black player");
			}
		}
		if(set.size()>1){
			throw new IllegalStateException("Not all the players have bean pared");
		}
		if (set.size()==1){
			Player notPared = set.iterator().next();
			/**
			 * if not last bracket downfloat else set as buy
			 */
			if (!this.lastBracket){
				//not last bracket: time to downfloat
				
			}
		}
	}
	
	/**
	 * downfloats the player and makes sure that this player initially belonged to this bracket
	 * @param player
	 */
	private void downfloat(Player player){
		if (!this.bracketPlayers.contains(player)){
			throw new IllegalStateException("Player does not belong to this bracket");
		}
		
		this.nextBracket.addPlayer(player);
		player.setFloatingState(FloatingState.DOWNFLOATER);
		boolean ok = this.bracketPlayers.remove(player);
		if (!ok){
			throw new IllegalStateException("For some reason I was not able to remove player");
		}
	}


	public boolean pareLastBracket() {
		throw new IllegalStateException("Please implement pareLastRound");
	}
}
