package mx.uv.fei.logic;

public class SessionDetails {
    private static SessionDetails instance;
    private String username;
    private String userType;
    private String id;
    public static SessionDetails getInstance() {
        if (instance == null) {
            instance = new SessionDetails();
        }
        return instance;
    }
    public SessionDetails(){}
    public void cleanSessionDetails() {
        id = "";
        username = "";
        userType = "";
    }

    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
