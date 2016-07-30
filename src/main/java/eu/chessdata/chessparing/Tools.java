package eu.chessdata.chessparing;

import com.google.gson.Gson;

public class Tools {
	public static final String GENERATED_FILES = "generatedFiles";
	private static Gson gson = buildGson();
	public static Gson getGson(){
		return Tools.gson;
	}
	
	private static Gson buildGson(){
		return new Gson();
	}
}
