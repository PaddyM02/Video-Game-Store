
package videogamestore;

import java.time.LocalDate;

/**
 * Game class - extends Product and implements Purchasable and Rentable.
 */
public class Game extends Product implements Purchasable, Rentable {

    private String platform;
    private boolean physical;
 
    

    public Game(int id, String name, double basePrice, String platform, boolean physical) {
        super(id, name, basePrice);
        this.platform = platform;
        this.physical = physical;
    }

    public String getPlatform() { return platform; }
    public boolean isPhysical() { return physical; }

    @Override
    public String getDisplayString() {
        return String.format("%s - %s - %.2f €", name, platform, basePrice);
    }

    @Override
    public void purchase(Customer customer, Store store) {
        String record = String.format("PURCHASE,%d,%s,%s,%.2f,%s", id, name, customer.getName(), basePrice, LocalDate.now());
        store.recordTransaction(record);
    }

    @Override
    public void rent(Customer customer, int days, Store store) {
        double price = days <= 7 ? Store.RENTAL_WEEK_PRICE : Store.RENTAL_TWO_WEEKS_PRICE;
        String record = String.format("RENT,%d,%s,%s,%d,%.2f,%s", id, name, customer.getName(), days, price, LocalDate.now());
        store.recordTransaction(record);
    }
}
