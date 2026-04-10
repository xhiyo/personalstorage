public class Item {
    private String name;
    private String photo;
    private String price;
    private String description;

    public Item(String name, String photo, String price, String description) {
        this.name = name;
        this.photo = photo;
        this.price = price;
        this.description = description;
    }

    public String getName() { return name; }
    public String getPhoto() { return photo; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
}
