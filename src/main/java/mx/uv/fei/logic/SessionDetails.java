package mx.uv.fei.logic;

public class SessionDetails {
    private static SessionDetails instance;
    private String username;
    private String userType;
    private String id;

    public static SessionDetails getInstance(String username, String userType, String id) {
        if (instance == null) {
            instance = new SessionDetails(username, userType, id);
        }
        return instance;
    }

    private SessionDetails(String username, String userType, String id) {
        this.username = username;
        this.userType = userType;
        this.id = id;
    }

    public void cleanSessionDetails() {
        instance = null;
    }

    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }

    public String getId() {
        return id;
    }
}
