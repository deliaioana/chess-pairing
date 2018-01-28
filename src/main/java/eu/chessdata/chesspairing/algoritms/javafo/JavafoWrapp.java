package eu.chessdata.chesspairing.algoritms.javafo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import eu.chessdata.chesspairing.algoritms.fideswissduch.Algorithm;
import eu.chessdata.chesspairing.model.ChesspairingGame;
import eu.chessdata.chesspairing.model.ChesspairingPlayer;
import eu.chessdata.chesspairing.model.ChesspairingRound;
import eu.chessdata.chesspairing.model.ChesspairingTournament;
import eu.chessdata.chesspairing.tools.Trf;
import javafo.api.JaVaFoApi;

public class JavafoWrapp implements Algorithm {

	@Override
	public ChesspairingTournament generateNextRound(ChesspairingTournament tournament) {
		//compute next round number 
		int nextRoundNumber = tournament.getRounds().size() + 1;
		
		String trf = Trf.getTrf(tournament);
		InputStream inputStream = new ByteArrayInputStream(trf.getBytes());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JaVaFoApi.exec(1000, "Hello World", inputStream, outputStream);
		String javafoResult = outputStream.toString();
		String newline = System.getProperty("line.separator");
		String[] pares = javafoResult.split(newline);

		/**
		 * Decode the result: first item is number of pares and next lines are the
		 * number of pares
		 */
		List<ChesspairingGame> games = new ArrayList<>();

		int totalPares = Integer.valueOf(pares[0]);
		for (int i = 1; i <= totalPares; i++) {
			String pare[] = pares[i].split(" ");
			System.out.println(pare);
			int indexWhite = Integer.valueOf(pare[0]);
			int indexBlack = Integer.valueOf(pare[1]);
			int tableNumber = i;
			
			if (indexBlack == 0) {
				ChesspairingPlayer player = tournament.getPlayerByInitialRank(indexWhite);
				ChesspairingGame buyGame = ChesspairingGame.buildBuyGame(tableNumber, player);

				games.add(buyGame);
			} else {

				ChesspairingPlayer whitePlayer = tournament.getPlayerByInitialRank(indexWhite);
				ChesspairingPlayer blackPlayer = tournament.getPlayerByInitialRank(indexBlack);

				ChesspairingGame game = new ChesspairingGame(tableNumber, whitePlayer, blackPlayer);
				games.add(game);
			}
		}

		ChesspairingRound round = ChesspairingRound.buildRound(nextRoundNumber, games);
		

		tournament.addRound(round);
		return tournament;
	}

}
