package eu.chessdata.chesspairing.algoritms.fideswissduch.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.comparators.ComparatorChain;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import com.google.common.collect.Lists;

import eu.chessdata.chesspairing.tools.Tools;

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
		if (null == bracketScore) {
			throw new IllegalStateException("You should never create a bracket with a null score");
		}
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

	/**
	 * it arranges the players in the invert order;
	 * 
	 * @param list
	 * @return
	 */
	protected List<Player> invertSort(List<Player> list) {
		Comparator<Player> comparator = getComparator();
		Collections.sort(list, comparator);
		List<Player> invers = Lists.reverse(list);
		return invers;
	}

	/**
	 * it sort the players in the correct using the default comparator
	 */
	protected void sortPlayers() {
		Comparator<Player> comparator = getComparator();

		Collections.sort(this.bracketPlayers, comparator);
	}

	/**
	 * this is the default comparator for the players
	 * 
	 * @return
	 */
	protected Comparator<Player> getComparator() {
		ComparatorChain<Player> comparatorChain = new ComparatorChain<>();
		comparatorChain.addComparator(Player.byPoints);
		comparatorChain.addComparator(Player.byElo);
		comparatorChain.addComparator(Player.byInitialRanking);

		return comparatorChain;
	}

	/**
	 * main algorithm for pairing bracket if nextBraket is null then this is the
	 * last bracket. It will try to identify if there is a next bracket if no
	 * next bracket then it will call pareLastBraket last bracket
	 * 
	 * @param lastRound
	 * @param nextBraket
	 */
	public boolean pareBraket() {
		// compute initial state
		if (!pairingTool.hasNextBraket(this.bracketScore)) {
			return pareLastBracket();
		}
		ScoreBracket nextBraketVal = pairingTool.getNextBraket(this.bracketScore);
		if (nextBraketVal == null) {
			throw new IllegalStateException(
					"Please check that you have a next braket before you try to pare. If not pare last braket instead");
		}
		this.nextBracket = nextBraketVal;

		this.sortPlayers();

		if (eavenPlayers()) {
			if (pareEavenPlayers()) {
				return true;
			}
		} else {
			if (pareOddPlayers()) {
				return true;
			}
		}

		if (this.bracketPlayers.size() <= 2) {
			// downfloat all the players and set the bracketResutl to an empty
			// PairingResult;
			List<Player> copy = new ArrayList<>();
			for (Player player : bracketPlayers) {
				copy.add(player);
			}
			for (Player player : copy) {
				downfloat(player);
			}
			// remove this bracket from the pairing tool
			this.pairingTool.removeBraket(bracketScore);
			// start again
			ScoreBracket first = this.pairingTool.getFirstBracket();
			return first.pareBraket();
		}

		// pare using pareInOrder for the moment is a replacement for swap
		// players
		if (eavenPlayers()) {
			PairingResult pairingResult = PairingResult.pareInOrder(this.bracketPlayers);
			if (pairingResult.isOk()) {
				if (nextBracket.pareBraket()) {
					this.bracketResult = pairingResult;
					return true;
				}
			}
		}else{
			/**
			 * mark downfloater in reverse order and pare
			 * if rairingResult.isOk downfloat the player marked and pare the next bracket
			 */
			
			List <Player> players = new ArrayList<>();
			players.addAll(this.bracketPlayers);
			Collections.sort(players, Player.comparator);
			for (int i=players.size()-1;i>=0;i--){
				List<Player> tempList = new ArrayList<>();
				tempList.addAll(players);
				Player downfloater = players.get(i);
				if (!tempList.remove(downfloater)){
					throw new IllegalStateException("not able to remove player");
				}
				PairingResult pairingResult = PairingResult.pareInOrder(tempList);
				if (pairingResult.isOk()){
					downfloat(downfloater);
					if (nextBracket.pareBraket()){
						this.bracketResult = pairingResult;
						return true;
					}
				}
			}
			return false;
		}

		// TODO probably good time to pare subgroups

		throw new IllegalStateException("Please finish this implementation. Time to downfloat everybody?");
	}

	/**
	 * use the pairingTool to decide what players can be downfloated if this is
	 * the last bracket then call pareOddLastBraket if will make a list with the
	 * players that can be downfloated if no players can be downfloated the
	 * throw an error for each downfloated player it will make sure that the
	 * remaining players can be pared. it will downfloat the player and make
	 * sure that the next bracket can be pared
	 * 
	 * @return
	 */
	private boolean pareOddPlayers() {

		// make sure it is eaven
		if ((this.bracketPlayers.size() % 2) != 1) {
			throw new IllegalStateException("Please debug: you do not have an even nr of players");
		}

		if (isLastBracket()) {
			return pareLastBracket();
		}

		// compute downfloat candidates
		List<Player> candidates = new ArrayList<>();
		for (Player candidate : bracketPlayers) {
			if (candidate.floatingState == FloatingState.STANDARD) {
				candidates.add(candidate);
			}
		}
		if (candidates.size() == 0) {
			return false;
		}
		// sort candidates
		List<Player> invertCandidates = invertSort(candidates);

		// for the remaining lists try to pare using even list pairing
		for (Player candidate : invertCandidates) {
			List<Player> evenList = new ArrayList<>();
			evenList.addAll(this.bracketPlayers);
			boolean ok = evenList.remove(candidate);
			if (!ok) {
				throw new IllegalStateException("I was not able to remove candidate from list");
			}
			PairingResult pairingResult = pareEvenList(evenList);
			if (pairingResult.isOk()) {
				/**
				 * downfloat the candidate and see if you can pare the next
				 * bracket
				 */
				downfloat(candidate);
				if (!nextBracket.pareBraket()) {
					cancelDownfloat(candidate);
				} else {
					// Everything is OK and wee set the result
					this.bracketResult = pairingResult;
					return true;
				}
			}

		}
		// no paring possible
		return false;
	}

	/**
	 * it resets the players to the original state. this one should be called
	 * when after a downfloat the following parings fail
	 * 
	 * @param candidate
	 */
	private void cancelDownfloat(Player candidate) {
		//candidate should be already downfloated
		if (this.bracketPlayers.contains(candidate)){
			throw new IllegalStateException("Player was not downfloated");
		}
		if (null == this.nextBracket){
			throw new IllegalStateException(
					"This is the last bracket. Thre is no nextBracket that you are looking fore");
		}
		boolean ok = this.nextBracket.removePlayer(candidate);
		if (!ok){
			throw new IllegalStateException("For some reason I was not able to remove player");
		}
		this.bracketPlayers.add(candidate);
	}

	/**
	 * it removes a player from the current bracketPlayers list
	 * @param candidate
	 * @return
	 */
	private boolean removePlayer(Player candidate) {
		if (this.bracketPlayers.contains(candidate)){
			this.bracketPlayers.remove(candidate);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * It creates a copy list. It sorts the copy and it tries to pare the list.
	 * It splits the group in half and for each possible combination try to pare
	 * Until you find a valid pare combination or return false;
	 * 
	 * @param evenList
	 * @return
	 */
	private PairingResult pareEvenList(List<Player> evenList) {
		List<Player> goodList = new ArrayList<>();
		goodList.addAll(evenList);
		Comparator<Player> comparator = Player.comparator;
		Collections.sort(goodList, comparator);

		Integer[] firstHalf = Tools.getFirstHalfIds(goodList.size());
		Integer[] seead = Tools.getSecondHalfIds(goodList.size());
		Generator<Integer> generator = Tools.getPermutations(seead);
		for (ICombinatoricsVector<Integer> vector : generator) {
			List<Integer> list = vector.getVector();
			Integer[] secondHalf = list.toArray(new Integer[list.size()]);
			// try to create a pairing result
			PairingResult result = new PairingResult(goodList, firstHalf, secondHalf);
			if (result.isOk()) {
				return result;
			}
		}
		// just return the invalid result
		return PairingResult.notValid();

	}

	/**
	 * 
	 * @return true if this score bracket is the last bracket
	 */
	private boolean isLastBracket() {
		if (null == this.bracketScore) {
			throw new IllegalStateException("Bracket score should never be null");
		}
		boolean result = this.pairingTool.isLastBracket(this.bracketScore);
		return result;
	}

	/**
	 * if it manages to pare the current bracket then it will continue to pare
	 * the next brackets returns true if it manages to pare the current bracket.
	 * It will not try to downfloat any players.
	 * 
	 * @return
	 */
	private boolean pareEavenPlayers() {

		PairingResult pairingResult = pareEvenList(this.bracketPlayers);
		if (pairingResult.isOk()) {
			if (this.isLastBracket()){
				return pareLastBracket();
			}
			if (this.nextBracket.pareBraket()) {
				this.bracketResult = pairingResult;
				return true;
			} else {
				return false;
			}
		} else {
			// upfloat one by one players from next bracket and try to pare
			// again
			List<Player> nextPlayers = new ArrayList<>();
			nextPlayers.addAll(nextBracket.getBracketPlayers());
			for (Player player : nextPlayers) {
				upfloat(player);
				if (pareBraket()) {
					return true;
				} else {
					downfloat(player);
				}
			}
			throw new IllegalStateException("Please finish this. What to do when even players can not be pared");
		}
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

	/**
	 * this player is removed from the next bracket and added to the current
	 * bracket
	 */
	private void upfloat(Player player) {
		if (this.bracketPlayers.contains(player)) {
			throw new IllegalStateException("Plyer allready in the current bracket");
		}
		if (null == this.nextBracket) {
			throw new IllegalStateException(
					"This is the last bracket. Thre is no nextBracket that you are looking fore");
		}
		this.bracketPlayers.add(player);
		boolean ok = nextBracket.bracketPlayers.remove(player);
		if (!ok) {
			throw new IllegalStateException("For some reason I was not able to remove player");
		}
	}

	/**
	 * It pares the last bracket. If odd players then it looks for downfloaters
	 * 
	 * @return
	 */
	private boolean pareLastBracket() {

		if (!isLastBracket()) {
			throw new IllegalStateException("This is not the last bracket");
		}
		sortPlayers();
		if (eavenPlayers()) {
			if (pareEavenPlayers()) {
				return true;
			}
		}
		// same logic as in odd players but wee look for the players that can be
		// buy

		// compute buy candidates
		List<Player> candidates = new ArrayList<>();
		for (Player candidate : bracketPlayers) {
			if (!candidate.wasBuy()) {
				candidates.add(candidate);
			}
		}
		if (candidates.size() == 0) {
			return false;
		}
		// sort candidates
		List<Player> invertCandidates = invertSort(candidates);

		for (Player candidate : invertCandidates) {
			// remove this candidate from the players list and see if you can
			// pare using even paring
			List<Player> evenList = new ArrayList<>();
			evenList.addAll(this.bracketPlayers);
			boolean ok = evenList.remove(candidate);
			if (!ok) {
				throw new IllegalStateException("Time to debug! I was not able to remove candidate from list");
			}
			PairingResult pairingResult = pareEvenList(evenList);
			if (pairingResult.isOk()) {
				Game buyGame = Game.createBuyGame(candidate);
				pairingResult.addGame(buyGame);
				// super dupper! Set the result and return
				this.bracketResult = pairingResult;
				return true;
			} else {
				return false;
			}
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
