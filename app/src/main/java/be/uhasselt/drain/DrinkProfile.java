package be.uhasselt.drain;

public class DrinkProfile {
    private String name;
    private String description;
    private String image;

    public DrinkProfile() {
    }

    public DrinkProfile(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public DrinkProfile(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
