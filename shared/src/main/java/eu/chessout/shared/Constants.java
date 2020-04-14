package eu.chessout.shared;


public class Constants {


    /**
     * LOCATIONS
     */
    public static final String USERS = "users";
    public static final String USER_KEY = "$userKey";
    public static final String CLUBS = "clubs";
    public static final String CLUB_KEY = "$clubKey";
    public static final String CLUB_NAME = "clubName";
    public static final String CLUB_MANAGERS = "clubManagers";
    public static final String MANAGER_KEY = "$managerKey";
    public static final String USER_SETTINGS = "userSettings";
    public static final String MY_CLUBS = "myClubs";
    public static final String MY_CLUB = "myClub";
    public static final String MY_DEVICES = "myDevices";
    public static final String DEVICE_KEY = "_deviceKey";

    public static final String DEFAULT_CLUB = "defaultClub";
    public static final String TOURNAMENTS = "tournaments";
    public static final String TOURNAMENT_KEY = "$tournamentKey";

    public static final String CLUB_PLAYERS = "clubPlayers";
    public static final String PLAYER_KEY = "$playerKey";

    public static final String TOURNAMENT_PLAYERS = "tournamentPlayers";

    public static final String TOURNAMENT_ROUNDS = "tournamentRounds";
    public static final String STANDINGS = "standings";
    public static final String TOURNAMENT_INITIAL_ORDER = "tournamentInitialOrder";
    public static final String STANDING_NUMBER = "$standingNumber";
    public static final String TOTAL_ROUNDS = "totalRounds";
    public static final String ROUND_NUMBER = "$roundNumber";
    //public static final String ROUND_PLAYERS = "roundPlayers";
    public static final String ROUND_ABSENT_PLAYERS = "absentPlayers";
    public static final String DATA_PLACEHOLDER = "dataPlaceHolder";
    public static final String TABLE_NUMBER = "$tableNumber";
    public static final String GAMES = "games";
    public static final String RESULT = "result";
    public static final String WHITE_PLAYER_NAME = "whitePlayerName";
    public static final String BLACK_PLAYER_NAME = "blackPlayerName";
    public static final String NO_PARTNER = "noPartner";
    public static final String CURRENT_RESULT = "currentResult";
    public static final String GLOBAL_FOLLOWERS = "globalFollowers";
    public static final String BY_PLAYER = "byPlayer";
    public static final String FOLLOWED_PLAYERS = "followedPlayers";
    public static final String MEDIA = "media";
    public static final String PLAYER = "player";
    public static final String CLUB = "club";
    public static final String PROFILE_PICTURE = "profilePicture";


    //clubManagers/$clubKey/$managerKey
    public static final String LOCATION_CLUB_MANAGERS = CLUB_MANAGERS + "/" + CLUB_KEY + "/" + MANAGER_KEY;

    //userSettings/$userKey/myClubs
    //public static final String LOCATION_MY_CLUBS = USER_SETTINGS + "/" + USER_KEY + "/" + MY_CLUBS;
    //userSettings/$userKey/myClubs
    public static final String LOCATION_MY_CLUBS = USER_SETTINGS + "/" + USER_KEY + "/" + MY_CLUBS + "/" + CLUB_KEY;

    //userSettings/$userKey/myClub
    public static final String LOCATION_L_MY_CLUB = USER_SETTINGS + "/" + USER_KEY + "/" + MY_CLUB;

    //userSettings/$userKey/defaultClub
    public static final String LOCATION_DEFAULT_CLUB = USER_SETTINGS + "/" + USER_KEY + "/" + DEFAULT_CLUB;

    //userSettings/$userKey/myDevices
    public static final String LOCATION_MY_DEVICES = USER_SETTINGS + "/" + USER_KEY + "/" + MY_DEVICES;
    //userSettings/$userKey/myDevices/$deviceKey
    public static final String LOCATION_MY_DEVICE = LOCATION_MY_DEVICES + "/" + DEVICE_KEY;

    //userSettings/$userKey/followedPlayers/
    public static final String LOCATION_MY_FOLLOWED_PLAYERS = USER_SETTINGS + "/" + USER_KEY + "/" + FOLLOWED_PLAYERS;
    //userSettings/$userKey/followedPlayers/byPlayer/$playerKey
    public static final String LOCATION_MY_FOLLOWED_PLAYERS_BY_PLAYER = LOCATION_MY_FOLLOWED_PLAYERS + "/" + BY_PLAYER + "/" + PLAYER_KEY;


    //tournaments/$clubKey
    public static final String LOCATION_TOURNAMENTS = TOURNAMENTS + "/" + CLUB_KEY;

    //tournaments/$clubKey/$tournamentKey
    public static final String LOCATION_TOURNAMENT = LOCATION_TOURNAMENTS + "/" + TOURNAMENT_KEY;

    //clubPlayers/$clubKey/playerKey
    public static final String LOCATION_CLUB_PLAYERS = CLUB_PLAYERS + "/" + CLUB_KEY;

    //tournamentPlayers/$tournamentKey/
    public static final String LOCATION_TOURNAMENT_PLAYERS = TOURNAMENT_PLAYERS + "/" + TOURNAMENT_KEY;

    //tournamentRounds/$tournamentKey/$roundNumber/games
    public static final String LOCATION_ROUND_GAMES = TOURNAMENT_ROUNDS + "/" + TOURNAMENT_KEY + "/" + ROUND_NUMBER + "/" + GAMES;
    //tournamentRounds/$tournamentKey/$roundNumber/games/$tableNumber
    public static final String LOCATION_GAME = LOCATION_ROUND_GAMES + "/" + TABLE_NUMBER;

    //tournamentRounds/$tournamentKey/$roundNumber/games/$tableNumber/result
    public static final String LOCATION_GAME_RESULT = LOCATION_ROUND_GAMES + "/" + TABLE_NUMBER + "/" + RESULT;

    //tournamentRounds/$tournamentKey/$roundNumber/roundPlayers
    //public static final String LOCATION_ROUND_PLAYERS = TOURNAMENT_ROUNDS + "/" + TOURNAMENT_KEY + "/" + ROUND_NUMBER + "/" + ROUND_PLAYERS;
    //tournamentRounds/$tournamentKey/$roundNumber/absentPlayers
    public static final String LOCATION_ROUND_ABSENT_PLAYERS = TOURNAMENT_ROUNDS + "/" + TOURNAMENT_KEY + "/" + ROUND_NUMBER + "/" + ROUND_ABSENT_PLAYERS;

    /**
     * For the moment wee only deal with the absolute category soo wee just hard code it
     * CategoryName = absoluteCategory
     * CategoryNumber = 0
     */
    public static final String CATEGORY_ABSOLUTE_NAME = "Standings";
    public static final String CATEGORY_ABSOLUTE_NUMBER = "0";

    public static final String CATEGORY_NUMBER = "$categoryNumber";

    //standings/$tournamentKey/$roundNumber/$categoryNumber/$standingNumber
    public static final String LOCATION_STANDINGS = STANDINGS + "/" + TOURNAMENT_KEY + "/" + ROUND_NUMBER + "/" + CATEGORY_NUMBER + "/" + STANDING_NUMBER;

    //tournamentInitialOrder/$tournamentKey/$playerKey
    public static final String LOCATION_TOURNAMENT_PLAYER_INITIAL_ORDER = TOURNAMENT_INITIAL_ORDER + "/"
            + TOURNAMENT_KEY + "/"
            + PLAYER_KEY;

    //tournamentInitialOrder/$tournamentKey
    public static final String LOCATION_TOURNAMENT_INITIAL_ORDER = TOURNAMENT_INITIAL_ORDER + "/"
            + TOURNAMENT_KEY;


    //globalFollowers/byPlayer/$playerKey
    public static final String LOCATION_GLOBAL_FOLLOWERS_BY_PLAYER = GLOBAL_FOLLOWERS + "/" + BY_PLAYER + "/" + PLAYER_KEY;
    //globalFollowers/byPlayer/$playerKey/$userKey
    public static final String LOCATION_GLOBAL_FOLLOWER_BY_PLAYER = LOCATION_GLOBAL_FOLLOWERS_BY_PLAYER + "/" + USER_KEY;

    /**
     * Constants for Firebase URL
     */
    public static final String FIREBASE_URL = "https://chess-out-v2.firebaseio.com/";

    /**
     * Constants for Firebase object properties
     */
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";

    /**
     * Some other constants
     */
    public static final String LOG_TAG = "my-debug";

    public static final java.lang.String IS_ADMIN = "isAdmin";

    //crowd tournaments section
    public static final String LOCATION_CROWD_TOURNAMENTS = "crowd-tournaments";

    public static final String LOCATION_USER_SETTINGS_CROWD_TOURNAMENTS = USER_SETTINGS + "/" + USER_KEY + "/" + LOCATION_CROWD_TOURNAMENTS + "/" + TOURNAMENT_KEY;

    //"crowd-who-follows-tournament/TOURNAMENT_KEY/USER_KEY
    public static final String LOCATION_CROWD_WHO_FOLLOWS_TOURNAMENT = "crowd-who-follows-tournament" + "/" + TOURNAMENT_KEY;

    //crowd-tournaments/$tournamentKey
    //public static final String LOCATION_CROWD_TOURNAMENT = LOCATION_CROWD_TOURNAMENTS +"/"+TOURNAMENT_KEY;

    // media/club/$clubKey/player/$playerKey
    public static final String LOCATION_PLAYER_MEDIA = MEDIA + "/" + CLUB + "/"
            + CLUB_KEY + "/" + PLAYER + "/" + PLAYER_KEY;

    // media/club/$clubKey/player/$playerKey/profilePicture
    public static final String LOCATION_PLAYER_MEDIA_PROFILE_PICTURE = LOCATION_PLAYER_MEDIA +
            "/" + PROFILE_PICTURE;

}
