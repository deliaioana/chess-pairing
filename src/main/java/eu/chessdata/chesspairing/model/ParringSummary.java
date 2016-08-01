package eu.chessdata.chesspairing.model;
/**
 * This is a simple class that can be used to represent different results
 * of a peering algorithm or just to package useful information to the
 * coller inside the {@link ChessparingTournament} class. 
 * @author bogdan
 *
 */
public class ParringSummary {
	public static final String PARRING_OK = "parringOK";
	public static final String PARRING_NOT_OK = "parringNotOK";
	public static final String PARRING_NOT_PERFORMED = "parringNotPerformed";

	private String shortMessage;
	private String longMessage;
	
	public String getShortMessage() {
		return shortMessage;
	}
	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}
	public String getLongMessage() {
		return longMessage;
	}
	public void setLongMessage(String longMessage) {
		this.longMessage = longMessage;
	}
}
