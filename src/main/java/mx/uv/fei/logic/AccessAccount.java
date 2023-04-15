package mx.uv.fei.logic;

import javafx.beans.property.SimpleStringProperty;

public class AccessAccount {
    private int userId;
    private String username;
    private String userPassword;
    private String userType;
    private SimpleStringProperty usernameProperty;
    private SimpleStringProperty userTypeProperty;

    public String getUsernameProperty() {
        return usernameProperty.get();
    }

    public SimpleStringProperty usernamePropertyProperty() {
        return usernameProperty;
    }

    public void setUsernameProperty(String usernameProperty) {
        this.usernameProperty.set(usernameProperty);
    }

    public String getUserTypeProperty() {
        return userTypeProperty.get();
    }

    public SimpleStringProperty userTypePropertyProperty() {
        return userTypeProperty;
    }

    public void setUserTypeProperty(String userTypeProperty) {
        this.userTypeProperty.set(userTypeProperty);
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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
