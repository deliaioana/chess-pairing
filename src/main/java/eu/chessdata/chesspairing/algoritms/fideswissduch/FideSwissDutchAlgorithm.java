package eu.chessdata.chesspairing.algoritms.fideswissduch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.chessdata.chesspairing.Tools;
import eu.chessdata.chesspairing.algoritms.comparators.ByElo;
import eu.chessdata.chesspairing.model.ChesspairingResult;
import eu.chessdata.chesspairing.model.ChesspairingGame;
import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingRound;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.model.PairingSummary;

public class FideSwissDutchAlgorithm implements Algorithm {
	private ChesspairingTournament mTournament;

	/**
	 * groups of players The key
	 */
	private Map<Long, Map<String, ChesspairingPlayer>> playersByResult;

	public ChesspairingTournament generateNextRound(ChesspairingTournament tournament) {
		this.mTournament = tournament;
		this.mTournament.setParringSummary(Tools.buildParringStarted());
		// more tan 1 players
		if (mTournament.getPlayers().size() < 2) {
			throw new IllegalStateException("Please ad at least 2 players or more");
		}

		// more rounds than totalRounds? For the moment I do not want to deal
		// with this use case
		if (mTournament.getTotalRounds() <= mTournament.getRounds().size()) {
			throw new IllegalStateException("You are trying to generate more rounds than totalRounds");
		}

		boolean validationOk = validateOrder();
		if (!validationOk) {
			return mTournament;
		}

		List<ChesspairingRound> rounds = this.mTournament.getRounds();
		if (rounds.size() <= 0) {
			generateFirstRound();
			return this.mTournament;
		}

		// make sure that the next round can be generated
		if (!canIGenerateNextRound()) {
			mTournament.getParringSummary().setShortMessage(PairingSummary.PARRING_NOT_OK);
			mTournament.getParringSummary().setLongMessage("You can not generate the next round!");
			return mTournament;
		}
		prepareNextRound();
		int roundNumber = mTournament.getRounds().size();
		computeCurrentResults(roundNumber);

		throw new UnsupportedOperationException("Please implement this");
	}

	/**
	 * compute all players results from the previous rounds
	 * 
	 * @param roundNumber
	 */
	private void computeCurrentResults(int roundNumber) {
		// get the current round
		// reset playersByResult;
		this.playersByResult = new HashMap<>();
		
		// players key
		Map<String, Double> currentPoints = new HashMap<>();

		for (int i = 1; i < roundNumber; i++) {
			ChesspairingRound round = getRound(i);
			List<ChesspairingGame> games = round.getGames();
			for (ChesspairingGame game : games) {

				String whiteKey = game.getWhitePlayer().getPlayerKey();
				Double whitePoints = currentPoints.get(whiteKey);
				if (whitePoints == null) {
					whitePoints = 0.0;
					currentPoints.put(whiteKey, whitePoints);
				}
				if (game.getResult() == ChesspairingResult.WHITE_WINS) {
					whitePoints = whitePoints + 1;
				}
				if (game.getResult() == ChesspairingResult.BYE) {
					whitePoints = whitePoints + 0.5;
					// go to the next game
					continue;
				}

				String blackKey = game.getBlackPlayer().getPlayerKey();
				Double blackPoints = currentPoints.get(blackKey);
				if (blackPoints == null) {
					blackPoints = 0.0;
					currentPoints.put(blackKey, blackPoints);
				}
				if (game.getResult() == ChesspairingResult.BLACK_WINS) {
					blackPoints = blackPoints + 1;
				}
				if (game.getResult() == ChesspairingResult.DRAW_GAME) {
					whitePoints = whitePoints + 0.5;
					blackPoints = blackPoints + 0.5;
				}
			}
		}
		
		//collect only the points from the present players;
		throw new IllegalStateException("Please implement this");
	}

	/**
	 * get a reference to the round by round number;
	 * 
	 * @param roundNumber
	 * @return
	 */
	private ChesspairingRound getRound(int roundNumber) {
		List<ChesspairingRound> rounds = mTournament.getRounds();
		for (ChesspairingRound round : rounds) {
			if (round.getRoundNumber() == roundNumber) {
				return round;
			}
		}
		throw new IllegalStateException(
				"Tournament in inconsistent state! Not able to find roundNumber = " + roundNumber);
	}

	/**
	 * create the next round and copy the players presence
	 */
	private void prepareNextRound() {
		ChesspairingRound round = new ChesspairingRound();
		int roundNumber = mTournament.getRounds().size() + 1;
		round.setRoundNumber(roundNumber);
		List<ChesspairingPlayer> players = new ArrayList<>();
		// round.setRoundNumber(roundNumber);
		for (ChesspairingPlayer player : mTournament.getPlayers()) {
			if (player.isPresent()) {
				players.add(player);
			}
		}
		round.setPresentPlayers(players);
		mTournament.getRounds().add(round);
	}

	/**
	 * it tests for the proper rounds identification (not to have 2 rounds with
	 * the same id) and if there is place to create one more round
	 * 
	 * @return true if a new round can be generated;
	 */
	private boolean canIGenerateNextRound() {
		int totalRounds = mTournament.getTotalRounds();
		int currentRouns = mTournament.getRounds().size();

		List<Integer> roundNumbers = new ArrayList<>();
		for (ChesspairingRound round : mTournament.getRounds()) {
			if (roundNumbers.contains(round.getRoundNumber())) {
				// you have 2 rounds with the same id
				return false;
			}
			roundNumbers.add(round.getRoundNumber());
		}

		if (currentRouns < totalRounds) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * For the moment I will sort the players in the descending order by elo and
	 * group them 2 by 2 I'm still wondering if I should group the first with
	 * the last and so on.
	 */
	private void generateFirstRound() {
		if (this.mTournament.getRounds().size() > 0) {
			throw new IllegalStateException("Tournament allready contains round 1");
		}

		Collections.sort(mTournament.getPlayers(), new ByElo());
		List<ChesspairingGame> games = new ArrayList<>();
		List<ChesspairingPlayer> players = mTournament.getPlayers();
		int count = 0;
		ChesspairingGame game = new ChesspairingGame();
		game.setTableNumber(0);
		for (ChesspairingPlayer player : players) {
			count++;
			if (count % 2 == 1) {
				int tableNumber = game.getTableNumber() + 1;
				game = new ChesspairingGame();
				game.setTableNumber(tableNumber);
				game.setWhitePlayer(player);
				if (count == players.size()) {
					game.setResult(ChesspairingResult.BYE);
					games.add(game);
				}
			} else {
				game.setBlackPlayer(player);
				game.setResult(ChesspairingResult.NOT_DECIDED);
				games.add(game);
			}
		}
		ChesspairingRound round = new ChesspairingRound();
		round.setRoundNumber(1);
		round.setGames(games);

		// is the first round so wee can create all new data
		List<ChesspairingRound> rounds = new ArrayList<>();

		// add the round
		rounds.add(round);

		// and wee set the rounds
		mTournament.setRounds(rounds);

		PairingSummary firstRoundOk = new PairingSummary();
		firstRoundOk.setShortMessage(PairingSummary.PARRING_OK);
		firstRoundOk.setLongMessage("First round was generated");
		mTournament.setParringSummary(firstRoundOk);
	}

	/**
	 * if players with no initialOrderId or the same initialOrderId then do not
	 * pare and set the paring message accordingly. No initial order is
	 * considered smaller or equal to 0
	 * 
	 * @return true if validation is OK and false otherwise
	 */
	private boolean validateOrder() {
		// players with no id
		List<String> playersNoId = new ArrayList<>();
		List<ChesspairingPlayer> players = mTournament.getPlayers();
		for (ChesspairingPlayer player : players) {
			if (player.getInitialOrderId() <= 0) {
				playersNoId.add(player.getName());
			}
		}
		if (playersNoId.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("You have players with no initialOrderId: ");
			for (String name : playersNoId) {
				sb.append(name + ", ");//
			}
			int id = sb.lastIndexOf(", ");
			sb.replace(id, sb.length(), "");
			PairingSummary parringSummary = new PairingSummary();
			parringSummary.setShortMessage(PairingSummary.PARRING_NOT_OK);
			parringSummary.setLongMessage(sb.toString());
			mTournament.setParringSummary(parringSummary);
			return false;
		}

		// players with the same id
		Map<Integer, ChesspairingPlayer> map = new HashMap<>();
		for (ChesspairingPlayer player : players) {
			if (map.containsKey(player.getInitialOrderId())) {
				StringBuffer sb = new StringBuffer();
				sb.append("You have players with the same initialOrderId: ");
				sb.append(map.get(player.getInitialOrderId()).getName() + " and ");
				sb.append(player.getName());
				PairingSummary parringSummary = new PairingSummary();
				parringSummary.setShortMessage(PairingSummary.PARRING_NOT_OK);
				parringSummary.setLongMessage(sb.toString());
				mTournament.setParringSummary(parringSummary);
				return false;
			} else {
				map.put(player.getInitialOrderId(), player);
			}
		}
		return true;
	}
}
