package videogamestore;

import javax.swing.*;
import java.awt.*;

public class CheckoutSummaryDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private boolean confirmed = false;

    public CheckoutSummaryDialog(JFrame parent, Customer customer, Store.Cart cart, double total) {

        super(parent, "Checkout Summary", true);
        setLayout(new BorderLayout(10, 10));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblCust = new JLabel("Customer: " + customer.getName() + " (" + customer.getEmail() + ")");
        lblCust.setFont(new Font("Arial", Font.BOLD, 14));
        main.add(lblCust);
        main.add(new JSeparator());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));

        StringBuilder sb = new StringBuilder();
        sb.append("Items in Cart:\n\n");

        for (Store.Cart.CartItem it : cart.getItems()) {
            if (it.isForRent()) {
                double price = it.getDays() == 14 ? Store.RENTAL_TWO_WEEKS_PRICE : Store.RENTAL_WEEK_PRICE;
                sb.append(String.format("RENT  %-25s  %2d days  €%.2f\n",
                        it.getGame().getName(), it.getDays(), price));
            } else {
                sb.append(String.format("BUY   %-25s         €%.2f\n",
                        it.getGame().getName(), it.getGame().getBasePrice()));
            }
        }

        sb.append("\n----------------------------------------\n");
        sb.append(String.format("TOTAL:                       €%.2f\n", total));

        area.setText(sb.toString());

        main.add(new JScrollPane(area));

        add(main, BorderLayout.CENTER);

        JPanel south = new JPanel();
        JButton btnConfirm = new JButton("Confirm Checkout");
        JButton btnCancel = new JButton("Cancel");
        south.add(btnConfirm);
        south.add(btnCancel);
        add(south, BorderLayout.SOUTH);

        btnConfirm.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        setSize(500, 450);
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
