package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.comparators.ComparatorChain;

import eu.chessdata.chesspairing.Tools;

public class ScoreBracket {
	private final FideSwissDutch fideSwissDutch;
	private final PairingTool pairingTool;
	private final Double bracketScore;
	protected final List<Player> bracketPlayers;
	protected PairingResult bracketResult;
	private ScoreBracket nextBracket;

	/**
	 * private constructor that should never be used. It will throw an error
	 */
	@SuppressWarnings("unused")
	private ScoreBracket() {
		throw new IllegalStateException("This constructor should never be used");
	}

	public ScoreBracket(FideSwissDutch fideSwissDutch, Double bracketScore, PairingTool pairingTool) {
		this.fideSwissDutch = fideSwissDutch;
		this.bracketPlayers = new ArrayList<>();

		this.bracketScore = bracketScore;
		this.pairingTool = pairingTool;
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
	 * main algorithm for pairing bracket if nextBraket is null then this is
	 * the. It will try to identify if there is a next bracket if no next
	 * bracket then it will call pareLastBraket last bracket
	 * 
	 * @param lastRound
	 * @param nextBraket
	 */
	public boolean pareBraket() {
		System.out.println("Pairing bracket: " + this.bracketScore);
		// compute initial state
		if (!pairingTool.hasNextBraket(this.bracketScore)) {
			return pareLastBracket();
		}
		ScoreBracket nextBraket = pairingTool.getNextBraket(this.bracketScore);
		if (nextBraket == null) {
			throw new IllegalStateException(
					"Please check that you have a next braket before you try to pare. If not pare last braket instead");
		}
		this.nextBracket = nextBraket;

		this.sortPlayers();

		if (eavenPlayers()) {
			if (pareEavenPlayers()) {
				return true;
			}
		}else{
			if(pareOddPlayers()){
				return true;
			}
		}

		throw new IllegalStateException("Please finish this");

		// set the initial pairs
		/*
		 * int size = playersCount / 2; Integer groupA[] = new Integer[size];
		 * Integer groupB[] = new Integer[size]; for (int i = 0; i < size; i++)
		 * { groupA[i] = i; groupB[i] = i + size; }
		 * 
		 * PairingResult pairingResult = new PairingResult(bracketPlayers,
		 * groupA, groupB); if (pairingResult.isOk()) {
		 *//**
			 * wee still need to see if the buy is OK. that is handled by the
			 * next function in a really bad spaghetify way (needs to be changed
			 * in the future)
			 *//*
			 * boolean resultOk = processResultPosibleBuy(pairingResult); if
			 * (resultOk) { return (validateResult()); // return true; } }
			 */

		/*
		 * Set<Integer[]> permutations = Tools.getPermutations(groupB); for
		 * (Integer[] permutation : permutations) { PairingResult permResult =
		 * new PairingResult(bracketPlayers, groupA, permutation); if
		 * (permResult.isOk()) { processResult(permResult); return true; } }
		 * 
		 * if (this.bracketResult == null) { throw new IllegalStateException(
		 * "For the moment this error is only for degugging please fix the above aloritm for bracket 1.0. It should have some results"
		 * ); } for (Game game : this.bracketResult.getGames()) { if
		 * (game.getBlack() == null) { System.out.println("buy:" +
		 * game.getWhite().getPlayerKey()); } else {
		 * System.out.println(game.getWhite().getPlayerKey() + " vs " +
		 * game.getBlack().getPlayerKey()); } } return false;
		 */
	}

	/**
	 * use the pairingTool to decide what players can be downfloated
	 * if this is the last bracket then call pareOddLastBraket
	 * if will make a list with the players that can be downfloated if no players can be downfloated the throw an error
	 * for each downfloated player it will make sure that the remaining players can be pared. it will downfloat the player and make sure that the next bracket 
	 * can be pared
	 * @return
	 */
	private boolean pareOddPlayers() {
		if (lastBracket()){
			return pareLastBracketOddPlayers();
		}
		
		List<Player> downfloatCandidates = new ArrayList<>();
		for (Player candidate: bracketPlayers){
			if (candidate.floatingState == FloatingState.STANDARD){
				downfloatCandidates.add(candidate);
			}
		}
		if (downfloatCandidates.size()==0)
		throw new IllegalStateException("Please implement this");
	}

	/**
	 * TODO select the player that can be buy. if no buy players then return false
	 * @return
	 */
	private boolean pareLastBracketOddPlayers() {
		throw new IllegalStateException("Please implement this");
	}

	/**
	 * 
	 * @return true if this score bracket is the last bracket
	 */
	private boolean lastBracket() {
		return this.pairingTool.isLastBracket(this.bracketScore);
	}

	/**
	 * it returns true if the current bracket can be pared. It will not try to downfloat any players. 
	 * @return
	 */
	private boolean pareEavenPlayers() {
		throw new IllegalStateException("Please finish this");
	}

	/**
	 * if the bracket contains an even number of players it return true
	 * 
	 * @return
	 */
	private boolean eavenPlayers() {
		if ((bracketPlayers.size() % 2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * it performs a set of tests just to make sure that all the bracket players
	 * have bean pared
	 * 
	 * @return
	 */
	protected boolean validateResult() {
		Set<Player> allPlayers = new HashSet<>();
		allPlayers.addAll(bracketPlayers);

		if (null == bracketResult) {
			throw new IllegalStateException("result is null");
		}

		if (!bracketResult.isOk()) {
			throw new IllegalStateException("Why are you validating the result?");
		}
		List<Game> games = bracketResult.getGames();
		for (Game game : games) {
			if (null == game) {
				throw new IllegalStateException("Null game. Please debug this braket");
			}
			if (!game.isValid()) {
				throw new IllegalStateException("Game not valid: Please debug this");
			}
			List<Player> players = game.getPlayers();
			allPlayers.removeAll(players);

		}
		if (allPlayers.size() != 0) {
			StringBuffer sb = new StringBuffer();
			for (Player player : allPlayers) {
				sb.append(player + ", ");
			}
			throw new IllegalStateException(
					"Not all the players have bean pared for bracket : " + this.bracketScore + " " + sb.toString());
		} else {
			// the only place where i return true
			return true;
		}

	}

	/**
	 * * it checks the state of the result and if there is none player only left
	 * it checks if it can be buy and it creates a buy game for him
	 * 
	 * @param pairingResult
	 * @return true if the result was OK and false otherwise
	 */
	private boolean processResultPosibleBuy(PairingResult result) {
		// make sure that all players have bean paired
		Set<Player> set = new HashSet<>();
		set.addAll(this.bracketPlayers);
		if (set.size() != bracketPlayers.size()) {
			throw new IllegalStateException("duplicate in bracketPlayers");
		}

		// identify not paired player;
		List<Game> games = result.getGames();
		for (Game game : games) {
			Player white = game.getWhite();
			Player black = game.getBlack();
			if (!set.remove(white)) {
				throw new IllegalStateException("The set did not contained white player");
			}
			if (!set.remove(black)) {
				throw new IllegalStateException("The set did not contained black player");
			}
		}
		if (set.size() > 1) {
			throw new IllegalStateException("Not all the players have bean pared");
		}
		if (set.size() == 1) {
			Player notPared = set.iterator().next();

			/**
			 * if not last bracket downfloat else set as buy
			 */
			if (this.nextBracket != null) {
				// not last bracket: time to downfloat
				downfloat(notPared);
				// return true;
			} else {
				// time to create buy
				if (notPared.wasBuy) {
					return false;
				} else {
					Game game = Game.createBuyGame(notPared);
					result.addGame(game);
				}
			}
		}

		// all in order
		this.bracketResult = result;
		// validateResult();
		return true;
	}

	/**
	 * set the result downfloat not paired if last round set as buy not paired.
	 * make sure that all the players have bean paired
	 */
	private void processResult(PairingResult result) {

		// make sure that all players have bean paired
		Set<Player> set = new HashSet<>();
		set.addAll(this.bracketPlayers);
		if (set.size() != bracketPlayers.size()) {
			throw new IllegalStateException("duplicate in bracketPlayers");
		}

		// identify not paired player;
		List<Game> games = result.getGames();
		confirmListDoesNotContainNullGames(games);
		for (Game game : games) {
			Player white = game.getWhite();
			Player black = game.getBlack();
			if (!set.remove(white)) {
				throw new IllegalStateException("The set did not contained white player");
			}
			if (!set.remove(black)) {
				throw new IllegalStateException("The set did not contained black player");
			}
		}
		if (set.size() > 1) {
			throw new IllegalStateException("Not all the players have bean pared");
		}
		if (set.size() == 1) {
			Player notPared = set.iterator().next();

			/**
			 * if not last bracket downfloat else set as buy
			 */
			if (this.nextBracket != null) {
				// not last bracket: time to downfloat
				downfloat(notPared);
			} else {
				// time to create buy
				throw new IllegalStateException("You forgot to pare one player");
			}
		}

		this.bracketResult = result;
		System.out.println("End debug point process result!");
	}

	/**
	 * it confirms that the list does not contain null games
	 * 
	 * @param games
	 */
	private void confirmListDoesNotContainNullGames(List<Game> games) {
		for (Game game : games) {
			if (null == game) {
				throw new IllegalStateException("games list contains null elements");
			}
		}
	}

	/**
	 * downfloats the player and makes sure that this player initially belonged
	 * to this bracket
	 * 
	 * @param player
	 */
	private void downfloat(Player player) {
		if (!this.bracketPlayers.contains(player)) {
			throw new IllegalStateException("Player does not belong to this bracket");
		}
		if (null == this.nextBracket) {
			throw new IllegalStateException(
					"This is the last bracket. Thre is no nextBracket that you are looking fore");
		}
		this.nextBracket.addPlayer(player);
		player.setFloatingState(FloatingState.DOWNFLOATER);
		boolean ok = this.bracketPlayers.remove(player);
		if (!ok) {
			throw new IllegalStateException("For some reason I was not able to remove player");
		}
	}

	public boolean pareLastBracket() {

		this.sortPlayers();
		Integer size = this.bracketPlayers.size();
		Integer group[] = new Integer[size];
		for (int i = 0; i < size; i++) {
			group[i] = i;
		}

		throw new IllegalStateException("Please finish this");
	}

	public PairingResult getBracketResult() {
		return bracketResult;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(bracketScore) + ": ");
		for (Player player : bracketPlayers) {
			sb.append(player + ", ");
		}
		return sb.toString();
	}

}
