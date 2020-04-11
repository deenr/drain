package be.uhasselt.drain.Profiles;

import java.io.Serializable;

public class Drink implements Serializable {
    private String name;
    private int amount;
    private String image;
    private int day;

    public Drink() {
    }

    public Drink(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public Drink(String name, int amount, String image, int day) {
        this.name = name;
        this.amount = amount;
        this.image = image;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
