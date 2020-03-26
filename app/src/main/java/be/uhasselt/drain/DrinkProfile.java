package be.uhasselt.drain;

import java.util.ArrayList;

public class DrinkProfile {
    private int weight;
    private int amountPerDay;
    private ArrayList<Drink> drinkList;

    public DrinkProfile() {
    }

    public DrinkProfile(int weight) {
        this.weight = weight;
        this.amountPerDay = 30 * weight;
        drinkList = new ArrayList<>();
        addBottle();
        addCan();
        addGlass();
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
        Drink bottle = new Drink("Bottle",500,"bottle");
        drinkList.add(bottle);
    }

    public void addGlass() {
        Drink glass = new Drink("Glass",200,"glass");
        drinkList.add(glass);
    }

    public void addCan() {
        Drink can = new Drink("Can",330,"can");
        drinkList.add(can);
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
