package be.uhasselt.drain;

import java.util.ArrayList;

public class UserProfile {
    public String userName;
    public String userEmail;
    public String userAge;
    public String userWeight;

    public UserProfile() {
    }

    public UserProfile(String userName, String userEmail, String userAge, String userWeight) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userAge = userAge;
        this.userWeight = userWeight;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }
}
