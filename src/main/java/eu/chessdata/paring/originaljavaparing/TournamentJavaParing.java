package eu.chessdata.paring.originaljavaparing;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.IllegalSelectorException;

import eu.chessdata.paring.originaljavaparing.stripedobjects.AbstractTableModel;

import eu.chessdata.paring.originaljavaparing.stripedobjects.TableModel;

/**
 * This class aims to abstract all tournament related data used by
 * {@link EnterFrame}
 * 
 * @author bogdan oloeriu
 *
 */
public class TournamentJavaParing {
	private final short MAXTEAMS = 500;
	private final short MAXPLAYERSPERTEAM = 10;
	private final short MAXBOARDS = 10;
	private final short MAXROUNDS = 18;
	private final short MAXPLAYERS = (short) (MAXTEAMS * MAXPLAYERSPERTEAM);
	private final short MAXPAIRS = (short) (MAXTEAMS / 2);
	private final short MINGAMESFIDE = 1;
	private final double MINSCOREFIDE = 0.5;
	private final short MINELOFIDE = 1000;
	private final float KELOFIDEBASE = 40f;
	private final short MAXROWSPERPAGE = 48;
	private final int MPAFC = 6; // max # of pairs that can be completely
									// explored by the engine avoiding factorial
									// complexity
	private final long MAXWAITTIME = 30 * 1000; // max wait time before to
												// signal factorial complexity
												// in millisec

	private boolean safeExitAllowed = true;
	private boolean abort = false;
	private short addedRows = 0;
	private short editingRow = 0;
	private String tournamentName = "";
	private String tournamentPlace = "";
	private String fed = "";
	private String tournamentDate1 = "";
	private String tournamentDate2 = "";
	private String tournamentArbiter = "";
	private int tournamentPairing = 0;
	private String rounds = "9";
	private File[] selectedDB = { new File(""), new File(""), new File(""), new File(""), new File("") };
	private String[] delimiterDB = { "", "", "", "", "" }, quotesDB = { "", "", "", "", "" };
	private String[][] indexesDB = { null, null, null, null, null };
	private int activeDB = 0; // index to active player database
	private short tournamentType = 0; // set default Dutch
	private short teamOrder = 0;
	private File currentDirectory = null;
	private File selectedFile = null;
	private File selectedSchema = null;
	private short currRound = 0;
	private short maxRound = 0;
	private short addedPairs = 0;
	private short prevRow = -1;
	private short[] sortIndex = new short[MAXTEAMS]; // of Teams
	private short[] tempIndex = new short[MAXPLAYERS]; // Players array
	private short[][] teamScores = new short[MAXTEAMS][3]; // (0-Team,
															// 1-Individual,
															// 2-Acceleration)
	private short[] playerScore = new short[MAXPLAYERS];
	private short[] tempScore = new short[MAXPLAYERS];
	private long[][] Z = new long[MAXPLAYERS][6]; // tie break values array
	private String[][][] roundsDetail = new String[MAXPAIRS][MAXBOARDS + 1][MAXROUNDS]; // max
																						// pairs
																						// x
																						// (0-Teams
																						// &
																						// 1...
																						// boards)
																						// x
																						// max
																						// rounds
	private short[][] pairFrom = new short[MAXPAIRS][2]; // temp buffer for
															// pairs
	private String[] upfloaters = new String[MAXROUNDS];
	private String[] downfloaters = new String[MAXROUNDS];
	private String[] possiblePlayerResults = { "0-0", "1-0", "\u00BD-\u00BD", "0-1", "0-0f", "1-0f", "\u00BD-\u00BDf",
			"0-1f", "\u00BD-0", "0-\u00BD", "1-1", "\u00BD-0f", "0-\u00BDf", "1-1f", "1-\u00BDf", "\u00BD-1f" };
	private String[] pairingSystems = { "swiss Dutch", "swiss Dubov", "Swiss Simple", "swiss Perfect Colours",
			"Amalfi Rating", "round robin", "by hand" };
	private String[] tieBreaks = { "", "Buchholz Cut1", "Buchholz Total", "Buchholz Median", "Sonneborn Berger",
			"Direct Encounter", "ARO", "TPR", "Won Games", "Games With Black", "Score %", "Weighted Boards" };
	private String flagsTieBreak;
	private String WhiteColor = "W"; // used for cross-tables and boards
	private String BlackColor = "B";
	private long doubleClickTime = 300;
	private long doubleClickSpace = 5;
	private boolean doSortOrder = false; // flag for 1.st round ordering

	private short maxBoards = 4; // dynamic N. boards to play
	private short maxPlayersPerTeam = 6; // dynamic max Players per Team
	private boolean rankingByAge = false;
	private boolean rankingByCategory = false;
	private boolean rankingByELO = false;
	private String[] group = new String[5];
	private String[] groupLimit = new String[5];
	private int nTeams;
	private String[] solution = null;
	private int[][] tI;
	private int visits;
	private String alreadyExplored = "";
	private int deltaColour;
	private boolean optimizationRequested = false;
	private short nonePlayedAlternate = 0;
	private int nGroupsAccelerated = 1;
	private short[] deltaPerformance = // table for 0 to 50% of score
			{ -800, -677, -589, -538, -501, -470, -444, -422, -401, -383, -366, -351, -336, -322, -309, -296, -284,
					-273, -262, -251, -240, -230, -220, -211, -202, -193, -184, -175, -166, -158, -149, -141, -133,
					-125, -117, -110, -102, -95, -87, -80, -72, -65, -57, -50, -43, -36, -29, -21, -14, -7, 0 };
	private String openedFile = "";
	private boolean engineRunning = false;
	private String allText = ""; // this and the following ones to convert HTML
									// tables to text
	private boolean tableOpened = false;
	private int rowTable = 0, colTable = 0, maxColTable = 0, colSpan = 0;
	private String[][] arrTable = new String[MAXPLAYERS * 2][MAXROUNDS + 4];
	private String colHeader = ""; // this and the following ones to sort
									// columns on Table1

	private boolean sortAscending = false;
	private int lastSortedCol = -1;
	private int returnValueFromshowOptionDialog = -1;
	private int availableTeams, lnb, group_lnb, lowestGroup, score50;
	private boolean lastRound;
	private String[] alreadySeen;
	private final int NORMAL_RETURN = 0;
	private final int RETURN_C13 = 1;
	private final int RETURN_DECREASE_P = 2;
	private final int RETURN_CONDITIONAL_DECREASE_P = 4;
	private final int CUTOFF = 1000;
	private final int NO_REMINDER = 1;
	private final int REMINDER_FOR_C9 = 2;
	private final int REMINDER_FOR_C12 = 4;
	private final int REMINDER_FOR_A7e = 8;
	private final int REMINDER_FOR_A7d = 16;
	private final int REMINDER_FOR_C13 = 32;
	private final int REMINDER_FOR_AVOIDING_C12 = 64;
	private final int REMINDER_KEEP_HETEROGENEOUS = 128;
	private final int REMINDER_FOR_maybeNeededAvoidB2 = 256;
	private final int REMINDER_FORCE_HOMOGENEOUS = 512;
	private String[] colours, gamesPlayed;
	private Thread EngineThread;
	private boolean missingResults = false;
	private short maxRowsPerPage = MAXROWSPERPAGE;
	private String[] colourPreference = { "no colour preference", "mild white but can change",
			"mild black but can change", "", "", "", "", "", "", "", "", "white", "black", "", "", "", "", "", "", "",
			"", "absolute white", "absolute black", "", "", "", "", "", "", "", "", "absolute white but can change",
			"absolute black but can change" };
	private boolean batchOrder = false; // to order pairs after read (import)
										// FIDE report
	private int statsPlayers, statsGames, statsGamesPlayed, statsBye, statsForfeit, statsDraw, statsWinWhite,
			statsWinStronger;
	private int simulationPlayers, simulationPercDraw, simulationPercWinWhite, simulationPercWinStronger,
			simulationForfeit, simulationRetired;
	private boolean simulationAcceleration;
	private String[] verbosities = { "Beginner", "Intermediate", "Expert" };
	private int verbosity_level = -1; // set 0=beginner, 1=intermediate,
										// 2=expert
	private boolean strong_verbose = (verbosity_level > 1);
	private boolean mild_verbose = (verbosity_level > 0);
	private String globalExplainText;
	private boolean explain, avoidBlocking, allowAcceleration;
	private int lastCompletedGroup, lastVisitedGroup, floatWeight;
	private long engineStartTime = 0;
	private boolean checkerRunning = false;
	public String FileToBeChecked = "";
	private boolean immediatelyExitAfterCalculation = false;
	private int firstWhite;

	class MyTableModel extends AbstractTableModel {
		public MyTableModel(int rows, int cols, boolean editable) {
			super();
			maxRows = rows;
			maxCols = cols;
			isEditable = editable;
			rowData = new String[maxRows][maxCols]; // allocate local memory for
													// the arrays
			colNames = new String[maxCols];
		}

		public MyTableModel(int rows, int cols) {
			super();
			maxRows = rows;
			maxCols = cols;
			rowData = new String[maxRows][maxCols]; // allocate local memory for
													// the arrays
			colNames = new String[maxCols];
		}

		// @Override
		public int getColumnCount() {
			return maxCols;
		}

		@Override
		public String getColumnName(int col) {
			return colNames[col];
		}

		public void setColumnName(int col, String s) {
			colNames[col] = s;
		}

		// @Override
		public int getRowCount() {
			return maxRows;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			rowData[row][col] = "" + value;
		}

		// @Override
		public Object getValueAt(int row, int col) {
			return rowData[row][col];
		}

		@Override
		public boolean isCellEditable(int x, int y) {
			return isEditable;
		}

		private String[][] rowData = null;
		private String[] colNames = null;
		private int maxRows = 0, maxCols = 0;
		private boolean isEditable = false;
	};

	private String calculate_mean_Elo(String Elom, int n) {
		int i, j, Elo = 0, Elo1, Elo2;
		String S[], temp;
		S = Elom.split(";");
		for (i = 0; i < n - 1; i++) // order decreasing Elo
			for (j = i + 1; j < n; j++) {
				try {
					Elo1 = Integer.valueOf("" + S[i]);
				} catch (NumberFormatException ex) {
					Elo1 = 0;
				}
				try {
					Elo2 = Integer.valueOf("" + S[j]);
				} catch (NumberFormatException ex) {
					Elo2 = 0;
				}
				if (Elo2 > Elo1) {
					temp = S[i];
					S[i] = S[j];
					S[j] = temp;
				}
			}
		if (n > maxBoards)
			n = maxBoards;
		for (i = 0; i < n; i++) {
			try {
				Elo += Integer.valueOf("" + S[i]);
			} catch (NumberFormatException ex) {
			}
		}
		return "" + Math.round(1f * Elo / n);
	}

	/**
	 * transform first 3 char of the string to number
	 * 
	 * @param string
	 * @return
	 */
	private short parseInt(String string) {

		int n = 3;
		if (string.length() < n)
			n = string.length();
		string = string.substring(0, n).trim();
		return Integer.valueOf(string).shortValue();
	}

	public void loadFromFile(BufferedReader bufferedReader) {
		int i1, i2, pair, i, j, k, r;
		String line, result, code, colour, sex, birth, ID, title;
		try {
			// read disc file
			BufferedReader in = bufferedReader;
			if (!in.ready())
				throw new IOException();
			boolean merge = false;

			
			for (r = 0; r < MAXROUNDS; r++) {
				upfloaters[r] = "upfloaters of round " + (r + 1) + " ;";
				downfloaters[r] = "downfloaters of round " + (r + 1) + " ;";
				for (i = 0; i < MAXPAIRS; i++)
					for (j = 0; j <= MAXBOARDS; j++)
						roundsDetail[i][j][r] = "0-0-0-0";
			}
			for (i = 0; i < MAXTEAMS; i++) {
				sortIndex[i] = 0;
				tempIndex[i] = (short) i;
				teamScores[i][2] = 0; // reset acceleration
				
			}
			sortAscending = false;
			lastSortedCol = -1;

			// read setup data
			tournamentName = in.readLine().trim();
			{
				tournamentPlace = in.readLine().trim();
				fed = in.readLine().trim();
				String[] tournamentDates = in.readLine().split(";");
				tournamentDate1 = "";
				tournamentDate2 = "";
				if (tournamentDates.length > 0)
					tournamentDate1 = tournamentDates[0].trim();
				if (tournamentDates.length > 1)
					tournamentDate2 = tournamentDates[1].trim();
				tournamentArbiter = in.readLine().trim();
				tournamentType = teamOrder = 0;
				try {
					tournamentType = parseInt(in.readLine());
					tournamentPairing = parseInt(in.readLine());
					rounds = in.readLine().substring(0, 4).trim();
					teamOrder = parseInt(in.readLine());
					maxBoards = parseInt(in.readLine());
					maxPlayersPerTeam = parseInt(in.readLine());
				} catch (NumberFormatException ex) {
				}
				if (tournamentType < 0 || tournamentType > 6 || teamOrder < 1 || teamOrder > 4 || maxBoards < 1
						|| maxBoards > MAXBOARDS || maxPlayersPerTeam < 1 || maxPlayersPerTeam > MAXPLAYERSPERTEAM) {
					throw new IllegalStateException("Not a good formated tournament. This does not make sence");
				}

				flagsTieBreak = (in.readLine() + "000000").substring(0, 6); // string
																			// with
																			// tie
																			// break
																			// criteria
				addedRows = parseInt(in.readLine()); // number of Teams
				maxRound = parseInt(in.readLine()); // number of rounds
				TableModel myTM = new MyTableModel(addedRows, maxRound, true);
				// read Teams & Players data

				addedPairs = 0;
				for (i = 0; i < addedRows; i++) {
					if ((line = in.readLine()) == null)
						return; // error !
					if (!(line.equals("null") || line.equals(""))) {
						if (maxRound > 0)
							sortIndex[parseInt(line) - 1] = (short) (i + 1); // set
																				// sort
																				// index
						else
							sortIndex[i] = (short) (i + 1);
						myTM.setValueAt(in.readLine(), i, 0); // read Team name
						int np = parseInt(in.readLine()); // number of Players
						String s, S[], Elom = "";
						int Elop = 0;
						int n = 0;
						for (j = 0; j < np; j++) {
							s = in.readLine();
							try {
								myTM.setValueAt(s, i, j + 2);
							} catch (ArrayIndexOutOfBoundsException ex) {
							} catch (IndexOutOfBoundsException ex) {
							}
							try {
								S = s.split(";");
								Elop = 0;
								try {
									Elop = Integer.valueOf(S[6]); // first
																	// consider
																	// FIDE
																	// rating
								} catch (NumberFormatException ex) {
								} catch (ArrayIndexOutOfBoundsException ex) {
								} catch (IndexOutOfBoundsException ex) {
								}
								try {
									if (Elop == 0)
										Elop = Integer.valueOf(S[8]); // second
																		// National
																		// rating
								} catch (NumberFormatException ex) {
								} catch (ArrayIndexOutOfBoundsException ex) {
								} catch (IndexOutOfBoundsException ex) {
								}
								if (Elop > 0) {
									Elom += Elop + ";";
									n++;
								}
							} catch (NumberFormatException ex) {
								//
							}
						}
						if (n > 0)
							myTM.setValueAt(calculate_mean_Elo(Elom, n), i, 1); // mean
																				// Elo
					}
				}
				missingResults = false;
				safeExitAllowed = true;
				if (maxRound > 0) {
					// read rounds data
					addedPairs = (short) ((addedRows + 1) / 2);
					for (r = 0; r < maxRound; r++) { // for each round
						if ((line = in.readLine()) == null)
							return; // header row. if not error !
						for (i = 0; i < addedPairs; i++) // for each pair
							for (j = 0; j <= maxBoards; j++) {
								if ((line = in.readLine()) == null)
									return; // error !
								if (!(line.equals("null") || line.equals(""))) {
									line = line.replace("0.5", "\u00bd"); // conversion
																			// of
																			// half
																			// point
									roundsDetail[i][j][r] = line;
								}
							}
					}
					if (tournamentType != 5) {
						// read floaters, except for 'round robin'
						line = in.readLine(); // explain line
						for (r = 0; r < maxRound; r++) { // for each round
							if ((line = in.readLine()) == null)
								break;
							if (!line.trim().equals("")) {
								String lines[] = line.split("\\|");
								String line1 = "", line2 = "";
								if (lines.length > 0)
									line1 = lines[0];
								if (lines.length > 1)
									line2 = lines[1];
								upfloaters[r] = line1;
								downfloaters[r] = line2;
							}
						}
					}
				}

				if (tournamentName.equals("")) {
					throw new IllegalStateException("You should allways have a tournament name");
				}

				in.close();
				currRound = maxRound;
			}
		} catch (IOException ex) {
		}

		System.out.println("End of load");
	}
}
