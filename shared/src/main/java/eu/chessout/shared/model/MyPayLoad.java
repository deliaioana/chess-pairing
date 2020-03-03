package eu.chessout.shared.model;

/**
 * Created by Bogdan Oloeriu on 04/07/2016.
 */
public class MyPayLoad {

    private Event event;
    private String authToken;
    private String gameLocation;
    private String tournamentLocation;

    public String getGameLocation() {
        return gameLocation;
    }

    //getters and setters

    public void setGameLocation(String gameLocation) {
        this.gameLocation = gameLocation;
    }

    public String getTournamentLocation() {
        return tournamentLocation;
    }

    public void setTournamentLocation(String tournamentLocation) {
        this.tournamentLocation = tournamentLocation;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public enum Event {GAME_RESULT_UPDATED}
}
