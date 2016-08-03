package eu.chessdata.chesspairing;

import com.google.gson.Gson;

import eu.chessdata.chesspairing.model.PairingSummary;

public class Tools {
	public static final String GENERATED_FILES = "generatedFiles";
	private static Gson gson = buildGson();

	public static Gson getGson() {
		return Tools.gson;
	}

	private static Gson buildGson() {
		return new Gson();
	}

	public static PairingSummary buildParringStarted() {
		PairingSummary parringSummary = new PairingSummary();
		parringSummary.setShortMessage(PairingSummary.PARRING_NOT_PERFORMED);
		parringSummary.setLongMessage("Parring was not performed");
		return parringSummary;
	}
}
