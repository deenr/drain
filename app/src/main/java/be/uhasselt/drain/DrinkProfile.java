package be.uhasselt.drain;

import java.util.ArrayList;

public class DrinkProfile {
    private int weight;
    private int amountPerDay;
    private ArrayList<Drink> drinkList;
    private int day;

    public DrinkProfile() {
    }

    public DrinkProfile(int weight) {
        this.weight = weight;
        this.amountPerDay = 30 * weight;
        drinkList = new ArrayList<>();
        this.day = 0;
    }

    public DrinkProfile(int weight, ArrayList<Drink> drinkList) {
        this.weight = weight;
        this.amountPerDay = 30 * weight;
        this.drinkList = drinkList;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAmountPerDay() {
        return amountPerDay;
    }

    public void setAmountPerDay(int amountPerDay) {
        this.amountPerDay = amountPerDay;
    }

    public ArrayList<Drink> getDrinkList() {
        return drinkList;
    }

    public void setDrinkList(ArrayList<Drink> drinkList) {
        this.drinkList = drinkList;
    }

    public void addBottle() {
        Drink bottle = new Drink("Bottle",500,"https://i.ibb.co/VCsnFf3/ic-bottle.png", getDay());
        drinkList.add(bottle);
    }

    public void addGlass() {
        Drink glass = new Drink("Glass",200,"https://i.ibb.co/TgZ4QRy/ic-glass.png", getDay());
        drinkList.add(glass);
    }

    public void addCan() {
        Drink can = new Drink("Can",330,"https://i.ibb.co/GCdk937/ic-can.png", getDay());
        drinkList.add(can);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void increaseDay() {
        this.day =+ 1;
    }

    @Override
    public String toString() {
        return "DrinkProfile{" +
                "weight=" + weight +
                ", amountPerDay=" + amountPerDay +
                ", drinkList=" + drinkList +
                '}';
    }
}
