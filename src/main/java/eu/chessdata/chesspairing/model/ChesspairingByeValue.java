package eu.chessdata.chesspairing.model;

public enum ChesspairingByeValue {
	ONE_POINT(1.0f), HALF_A_POINT(0.5f);

	private ChesspairingByeValue(float value) {
		this.value = value;
	}

	private float value;

	public float getValue() {
		return this.value;
	}
}
