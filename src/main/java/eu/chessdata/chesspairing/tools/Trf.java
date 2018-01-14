package eu.chessdata.chesspairing.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;

import eu.chessdata.chesspairing.model.ChesspairingByeValue;
import eu.chessdata.chesspairing.model.ChesspairingGame;
import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingResult;
import eu.chessdata.chesspairing.model.ChesspairingRound;
import eu.chessdata.chesspairing.model.ChesspairingTitle;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
//import sun.org.mozilla.javascript.internal.regexp.SubString;

/**
 * Class with public static methods that are used for exporting a tournament as
 * a trf
 * 
 * @author bogda
 *
 */
public class Trf {

	/**
	 * I have never saw a model of trf using this Position 1 - 3
	 * Data-Identification-number (??2 for tournament data)
	 */
	@SuppressWarnings("unused")
	private String dataIdentificationNumber;
	private String tournamentName;
	private String city;
	private String federation;
	private String dateOfStart;
	private String dateOfEnd;
	private int numberOfPlayers;
	private int numberOfRatedPlayers;
	private int numberOfTeams;
	private String typeOfTournament;
	private String chifArbiter;
	private String deputyChifArbiters;
	private String datesOfTheRounds;
	private String timesPerMovesGame;

	private final ChesspairingTournament trfTournament;

	public static String getTrf(ChesspairingTournament tournament) {

		Trf trf = new Trf(tournament);
		return trf.buildTrf();
	}

	public Trf(ChesspairingTournament chesspTournament) {
		this.trfTournament = chesspTournament;

		this.dataIdentificationNumber = "no-dataIdentificationNumber-Implemented";
		this.tournamentName = chesspTournament.getName();
		this.city = chesspTournament.getCity();
		if (chesspTournament.getFederation() != null) {
			this.federation = chesspTournament.getFederation();
		} else {
			this.federation = "";
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		this.dateOfStart = dateFormat.format(chesspTournament.getDateOfStart());
		this.dateOfEnd = dateFormat.format(chesspTournament.getDateOfEnd());

		this.numberOfPlayers = chesspTournament.getPlayers().size();

		// number of rated players
		this.numberOfPlayers = 0;
		for (ChesspairingPlayer player : chesspTournament.getPlayers()) {
			if (player.getElo() != 0) {
				this.numberOfPlayers++;
			}
		}

		this.numberOfTeams = 0;
		if (chesspTournament.getTypeOfTournament() != null) {
			this.typeOfTournament = chesspTournament.getTypeOfTournament();
		} else {
			this.typeOfTournament = "";
		}
		this.chifArbiter = chesspTournament.getChifArbiter();
		this.deputyChifArbiters = chesspTournament.getDeputyChifArbiters();

		this.timesPerMovesGame = "moves/time, increment";
	}

	private String buildTrf() {
		StringBuilder sb = new StringBuilder();
		sb.append("012 " + this.tournamentName + "\n");
		sb.append("022 " + this.city + "\n");
		sb.append("032 " + this.federation + "\n");
		sb.append("042 " + this.dateOfStart + "\n");
		sb.append("052 " + this.dateOfEnd + "\n");
		sb.append("062 " + this.numberOfPlayers + "\n");
		sb.append("072 " + this.numberOfRatedPlayers + "\n");
		sb.append("082 " + this.numberOfTeams + "\n");
		sb.append("092 " + this.typeOfTournament + "\n");
		sb.append("102 " + this.chifArbiter + "\n");
		sb.append("112 " + this.deputyChifArbiters + "\n");
		sb.append("122 " + this.timesPerMovesGame + "\n");

		// XXR 9 ? to be clarified what it means
		sb.append("XXR 9" + "\n");

		for (ChesspairingPlayer player : this.trfTournament.getPlayers()) {
			PlayerSection playerSection = new PlayerSection(player);
			sb.append(playerSection.getString());
			sb.append("\n");
		}

		return sb.toString();
	}

	private class PlayerSection {
		private ChesspairingPlayer player;

		private String dataIdentificationNumber;
		private String startingRankNumber;
		private String sex;
		private String title;
		private String name;
		private String fideRating;
		private String fideFederation;
		private String fideNumber;
		private String birthDate;
		private String points;
		private String rank;

		private PlayerSection(ChesspairingPlayer player) {
			this.player = player;
			this.dataIdentificationNumber = "001";
			this.startingRankNumber = String.valueOf(this.player.getInitialOrderId());

			this.sex = "";
			switch (this.player.getSex()) {
			case MAN:
				this.sex = "m";
				break;
			case WOMAN:
				this.sex = "w";
			default:
				this.sex = "s";// "s" comes from secret
				break;
			}

			this.title = "";
			ChesspairingTitle chessTitle = player.getTitle();
			if (chessTitle != null) {
				switch (chessTitle) {
				case GRANDMASTER:
					this.title = "g";
					break;
				case INTERNATIONAL_MASTER:
					this.title = "m";
					break;
				case FIDE_MASTER:
					this.title = "f";
					break;
				case CANDIDATE_MASTER:
					this.title = "c";
					break;
				}
			}
			this.name = player.getName();

			this.fideRating = "";
			if (player.getElo() > 0) {
				this.fideRating = String.valueOf(player.getElo());
			}

			this.fideFederation = "";
			if (player.getFederation() != null) {
				this.fideFederation = player.getFederation();
			}

			this.fideNumber = "";
			if (player.getFideNumber() != null) {
				this.fideNumber = player.getFideNumber();
			}

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			this.birthDate = "";
			if (player.getBirthDate() != null) {
				this.birthDate = dateFormat.format(player.getBirthDate());
			}

			Double pointsValue = Trf.computePoints(player, trfTournament);
		}

		private String getString() {
			StringBuilder sb = new StringBuilder();
			sb.append(Trf.formatStringIndentLeft(1, 3, dataIdentificationNumber));
			sb.append(" ");
			sb.append(Trf.formatStringIndentRight(5, 8, this.startingRankNumber));
			sb.append(" ");
			sb.append(Trf.formatStringIndentRight(10, 10, this.sex));
			sb.append(Trf.formatStringIndentRight(11, 13, this.title));
			sb.append(" ");
			sb.append(Trf.formatStringIndentLeft(15, 47, this.name));
			sb.append(" ");
			sb.append(Trf.formatStringIndentRight(49, 52, this.fideRating));
			sb.append(" ");
			sb.append(Trf.formatStringIndentRight(54, 56, this.fideFederation));
			sb.append(" ");
			sb.append(Trf.formatStringIndentRight(58, 68, this.fideNumber));
			sb.append(" ");
			sb.append(Trf.formatStringIndentRight(70, 79, this.birthDate));
			sb.append(" ");// 80
			sb.append(Trf.formatStringIndentRight(81, 84, this.getPoints()));
			sb.append(" ");// 85
			sb.append(Trf.formatStringIndentRight(86, 89, this.getRank()));
			return sb.toString();
		}

		/**
		 * It computes the rank of the player overall
		 * @return
		 */
		private String getRank() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * computes the number of point of the current tournament and returns
		 * them in a string format( 11.5 format)
		 * 
		 * @return
		 */
		// computes the number of points of the current player from a specified
		// tournament
		private String getPoints( ) {
			List<ChesspairingRound> rounds = trfTournament.getRounds();
			float points = 0;
			for (ChesspairingRound round: rounds){
				List<ChesspairingGame> games = round.getGames();
				for (ChesspairingGame game: games){
					ChesspairingResult result = game.getResult();
					
					//this player is the white player
					if (this.player.equals(game.getWhitePlayer())){
						if(result.equals(ChesspairingResult.WHITE_WINS)){
							points += 1;
						}else if(result.equals(ChesspairingResult.DRAW_GAME)){
							points += 0.5;
						}else if (result.equals(ChesspairingResult.BYE)){
							points += trfTournament.getChesspairingByeValue().getValue();
						}
					}else if(this.player.equals(game.getBlackPlayer())){
						if (result.equals(ChesspairingResult.BLACK_WINS)){
							points += 1;
						}else if (result.equals(ChesspairingResult.DRAW_GAME)){
							points += 0.5;
						}
					}
				}
			}
			String pointsString = String.format("%.1f", points);
			return pointsString;
		}
	}

		/**
		 * format a "something" string into "something___" string
		 * 
		 * @param start
		 * @param end
		 * @param content
		 * @return
		 */
		private static String formatStringIndentLeft(int start, int end, String content) {
			int size = end - start + 1;
			StringBuilder sb = new StringBuilder();
			sb.append(content);

			if (sb.length() > size) {
				String result = sb.substring(0, size);
				return result;
			} else {
				while (sb.length() < size) {
					sb.append(" ");
				}
				String result = sb.toString();
				return result;
			}
		}

		/**
		 * format a "something" sting into "___something" string
		 * 
		 * @param start
		 * @param end
		 * @param content
		 * @return
		 */
		private static String formatStringIndentRight(int start, int end, String content) {
			int size = end - start + 1;

			if (content.length() > size) {
				String result = content.substring(0, size);
				return result;
			} else {
				StringBuilder sb = new StringBuilder();
				while (sb.length() + content.length() < size) {
					sb.append(" ");
				}
				String result = sb.append(content).toString();
				return result;
			}
		}

		@SuppressWarnings("incomplete-switch")
		public static double computePoints(ChesspairingPlayer player, ChesspairingTournament tournament) {
			String key = player.getPlayerKey();
			Double totalPoints = 0.0;

			Double whinPoints = 1.0;
			Double lostPoints = 0.0;
			Double drawPoints = 0.5;
			Double buyPoints = 0.0;
			ChesspairingByeValue buy = tournament.getChesspairingByeValue();

			switch (buy) {
			case HALF_A_POINT:
				buyPoints = 0.5;
				break;
			case ONE_POINT:
				buyPoints = 1.0;
			default:
				throw new IllegalStateException("Case not implemented fro buy " + buy);
			}

			for (ChesspairingRound round : tournament.getRounds()) {
				for (ChesspairingGame game : round.getGames()) {
					ChesspairingResult result = game.getResult();

					String whiteKey = game.getWhitePlayer().getPlayerKey();
					if (whiteKey.equals(key)) {
						switch (result) {
						case BYE:
							totalPoints += buyPoints;
							break;
						case WHITE_WINS:
							totalPoints += whinPoints;
							break;
						case WHITE_WINS_OPONENT_ABSENT:
							totalPoints += whinPoints;
						}
						continue;
					}

					if (game.getBlackPlayer() == null) {
						continue;
					}

					String blackKey = game.getBlackPlayer().getPlayerKey();
					if (blackKey.equals(key)) {
						switch (result) {
						case BYE:
							totalPoints += buyPoints;
							break;
						case BLACK_WINS:
							totalPoints += whinPoints;
							break;
						case BLACK_WINS_OPONENT_ABSENT:
							totalPoints += whinPoints;
						}
					}

				}
			}
			return totalPoints;
		}

}
