package videogamestore;

import javax.swing.*;
import java.awt.*;

/**
 * CartPanel - shows items in cart, displays total, and handles checkout.
 */
public class CartPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Store store;
    private Store.Cart cart;
    private DefaultListModel<String> model;
    private JList<String> list;

    private Customer activeCustomer;
    private JLabel totalLabel;

    public CartPanel(Store store, Store.Cart cart) {
        this.store = store;
        this.cart = cart;
        setLayout(new BorderLayout(6, 6));
        initComponents();
    }

    private void initComponents() {

        totalLabel = new JLabel("Total: €0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(totalLabel, BorderLayout.NORTH);

        model = new DefaultListModel<>();
        list = new JList<>(model);
        list.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel south = new JPanel();
        JButton btnRemove = new JButton("Remove Selected");
        JButton btnCheckout = new JButton("Checkout");
        south.add(btnRemove);
        south.add(btnCheckout);
        add(south, BorderLayout.SOUTH);

        btnRemove.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx >= 0) {
                cart.getItems().remove(idx);
                reload();
            }
        });

        btnCheckout.addActionListener(e -> handleCheckout());

        reload();
    }

    private void handleCheckout() {

        if (activeCustomer == null) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Please select a customer first in the Customers tab.");
            return;
        }

        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Your cart is empty. Add some items first!");
            return;
        }

        double total = calculateTotal();

        CheckoutSummaryDialog dialog =
                new CheckoutSummaryDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                        activeCustomer, cart, total);

        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            return; 
        }

        for (Store.Cart.CartItem it : cart.getItems()) {
            if (it.isForRent())
                it.getGame().rent(activeCustomer, it.getDays(), store);
            else
                it.getGame().purchase(activeCustomer, store);
        }

        JOptionPane.showMessageDialog(this,
                "Checkout complete for: " + activeCustomer.getName() +
                "\nTransactions saved to resources/transactions.txt");

        cart.getItems().clear();
        reload();
    }

    private double calculateTotal() {
        double total = 0.0;

        for (Store.Cart.CartItem it : cart.getItems()) {
            if (it.isForRent()) {
                if (it.getDays() == 7)
                    total += Store.RENTAL_WEEK_PRICE;
                else if (it.getDays() == 14)
                    total += Store.RENTAL_TWO_WEEKS_PRICE;
            } else {
                total += it.getGame().getBasePrice();
            }
        }
        return total;
    }

    /**
     * Called when user selects a customer in CustomerPanel.
     */
    public void setActiveCustomer(Customer c) {
        this.activeCustomer = c;
    }

    /**
     * Refresh list + total price.
     */
    public void reload() {
        model.clear();
        double total = 0.0;

        for (Store.Cart.CartItem it : cart.getItems()) {
            model.addElement(it.toString());

            if (it.isForRent()) {
                if (it.getDays() == 14)
                    total += Store.RENTAL_TWO_WEEKS_PRICE;
                else
                    total += Store.RENTAL_WEEK_PRICE;
            } else {
                total += it.getGame().getBasePrice();
            }
        }

        totalLabel.setText(String.format("Total: €%.2f", total));
    }
}
