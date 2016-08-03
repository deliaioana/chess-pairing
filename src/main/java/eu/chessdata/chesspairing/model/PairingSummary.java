package eu.chessdata.chesspairing.model;
/**
 * This is a simple class that can be used to represent different results
 * of a peering algorithm or just to package useful information to the
 * coller inside the {@link ChesspairingTournament} class. 
 * @author bogdan
 *
 */
public class PairingSummary {
	public static final String PARRING_OK = "pairingOK";
	public static final String PARRING_NOT_OK = "pairingNotOK";
	public static final String PARRING_NOT_PERFORMED = "pairingNotPerformed";

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
