
package videogamestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Store class - holds catalog and handles file I/O.
 */
public class Store {

    public static String STORE_NAME = "Pixel Vault Games";
    public static int idCounter = 1000;
    public static final double RENTAL_WEEK_PRICE = 4.99;
    public static final double RENTAL_TWO_WEEKS_PRICE = 7.99;

    private List<Game> catalog = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();

    public class Cart {
        private List<CartItem> items = new ArrayList<>();

        public class CartItem {
            private Game game;
            private boolean forRent;
            private int days;

            public CartItem(Game game) {
                this.game = game;
                this.forRent = false;
                this.days = 0;
            }
            public Game getGame() { return game; }
            public void setRent(int days) { this.forRent = true; this.days = days; }
            public boolean isForRent() { return forRent; }
            public int getDays() { return days; }
            @Override
            public String toString() {
                if (forRent) return game.getName() + " (Rent " + days + "d)";
                return game.getName() + " (Buy)";
            }
        }

        public void add(Game g) { items.add(new CartItem(g)); }
        public List<CartItem> getItems() { return items; }
    }

    public Store() {}

    public void addGame(Game g) { catalog.add(g); }
    public List<Game> getCatalog() { return catalog; }
    public List<Customer> getCustomers() { return customers; }

    public void loadGamesFromCSV(String path) throws FileNotFoundException {
        File f = new File(path);
        if (!f.exists()) {
            System.err.println("CSV not found: " + path);
            return;
        }
        try (Scanner sc = new Scanner(f)) {
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                String platform = parts[3];
                String type = parts[4];
                String extra = parts.length>5 ? parts[5] : "";
                if (type.equalsIgnoreCase("DIGITAL")) {
                    double sizeMb = extra.isEmpty() ? 500 : Double.parseDouble(extra);
                    addGame(new DigitalGame(id, name, price, platform, sizeMb));
                } else {
                    double weight = extra.isEmpty() ? 150 : Double.parseDouble(extra);
                    addGame(new PhysicalGame(id, name, price, platform, weight));
                }
                if (id >= idCounter) idCounter = id + 1;
            }
        }
    }

    public void loadCustomersFromTXT(String path) throws FileNotFoundException {
        File f = new File(path);
        if (!f.exists()) {
            System.err.println("TXT not found: " + path);
            return;
        }
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(";");
                if (p.length>=2) customers.add(new Customer(p[0], p[1]));
            }
        }
    }
    public Customer findCustomerByEmail(String email) {
        for (Customer c : customers) {
            if (c.getEmail().equalsIgnoreCase(email)) return c;
        }
        return null;
    }


    public synchronized void recordTransaction(String record) {
        try (PrintWriter pw = new PrintWriter(new java.io.FileWriter("resources/transactions.txt", true))) {
            pw.println("[" + java.time.LocalDateTime.now() + "] " + record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int nextId() { return idCounter++; }
    
    
}


