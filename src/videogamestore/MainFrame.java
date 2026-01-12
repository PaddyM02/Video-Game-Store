
package videogamestore;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame - tabbed professional UI
 */
public class MainFrame extends JFrame implements CustomerSelectionListener {

    private static final long serialVersionUID = 1L;
	private Store store;
    private Store.Cart cart;
    private Customer activeCustomer;

    private GameListPanel gamesPanel;
    private CartPanel cartPanel;
    private CustomerPanel customerPanel;

    public MainFrame(Store store) {
        this.store = store;
        this.cart = store.new Cart();
        setTitle(Store.STORE_NAME);
        setSize(1000,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {

        JTabbedPane tabs = new JTabbedPane();

        cartPanel = new CartPanel(store, cart);

        // IMPORTANT: pass cartPanel into GameListPanel
        gamesPanel = new GameListPanel(store, cart, cartPanel);

        customerPanel = new CustomerPanel(store, this);

        tabs.addTab("Games", gamesPanel);
        tabs.addTab("Cart", cartPanel);
        tabs.addTab("Customers", customerPanel);

        add(tabs, BorderLayout.CENTER);

        // Default customer
        this.activeCustomer = new Customer("Guest", "guest@local");

        customerPanel.setSelectedCustomer(this.activeCustomer);

        customerPanel.setListener(this);

        // NEW: inform GameListPanel of current customer
        gamesPanel.setActiveCustomer(this.activeCustomer);
    }
    public void setLoggedInCustomer(Customer c) {
        try {
            customerPanel.setSelectedCustomer(c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void customerSelected(Customer c) {
        this.activeCustomer = c != null ? c : new Customer("Guest", "guest@local");
        setTitle(Store.STORE_NAME + " — " + this.activeCustomer.getName());
        cartPanel.setActiveCustomer(this.activeCustomer);
        cartPanel.reload();
        
        
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("List.font", new Font("Segoe UI", Font.PLAIN, 13));

    }
    

}
