
package videogamestore;

/**
 * Abstract class Product - base for items in store.
 */
public abstract class Product {
    protected int id;
    protected String name;
    protected double basePrice;

    public Product(int id, String name, double basePrice) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }

    public abstract String getDisplayString();

    public String brief() {
        return String.format("#%d: %s (%.2f €)", id, name, basePrice);
    }
}
