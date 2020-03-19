package be.uhasselt.drain;

import java.util.ArrayList;

public class UserProfile {
    public String userName;
    public String userEmail;
    public String userAge;
    public String userWeight;
    public ArrayList<DrinkProfile> drinkProfiles;

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

    public void addBottle() {
        DrinkProfile bottle = new DrinkProfile("Bottle", "500");
        drinkProfiles.add(bottle);
    }

    public void addGlass() {
        DrinkProfile glass = new DrinkProfile("Glass", "175");
        drinkProfiles.add(glass);
    }

    public void addCan() {
        DrinkProfile can = new DrinkProfile("Can", "330");
        drinkProfiles.add(can);
    }
}
