package eu.chessdata.chessparing;

import com.google.gson.Gson;

import eu.chessdata.chessparing.model.ParringSummary;

public class Tools {
	public static final String GENERATED_FILES = "generatedFiles";
	private static Gson gson = buildGson();

	public static Gson getGson() {
		return Tools.gson;
	}

	private static Gson buildGson() {
		return new Gson();
	}

	public static ParringSummary buildParringStarted() {
		ParringSummary parringSummary = new ParringSummary();
		parringSummary.setShortMessage(ParringSummary.PARRING_NOT_PERFORMED);
		parringSummary.setLongMessage("Parring was not performed");
		return parringSummary;
	}
}
