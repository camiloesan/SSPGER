package mx.uv.fei.logic;

public class AccessAccount {
    private int userId;
    private String username;
    private String userPassword;

    public AccessAccount() {}

    public AccessAccount(String username, String userPassword) {
        this.username = username;
        this.userPassword = userPassword;
    }

    public void areCredentialsValid() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
