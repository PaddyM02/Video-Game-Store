
package videogamestore;

import javax.swing.*;
import java.awt.*;

/**
 * CustomerPanel - select customers and notify listener
 */
public class CustomerPanel extends JPanel {

    private static final long serialVersionUID = 1L;
	private Store store;
    private CustomerSelectionListener listener;
    private JComboBox<String> combo;
    private Customer current;

    public CustomerPanel(Store store, CustomerSelectionListener listener) {
        this.store = store;
        this.listener = listener;
        setLayout(new BorderLayout(6,6));
        initComponents();
    }

    private void initComponents() {
        DefaultComboBoxModel<String> cbm = new DefaultComboBoxModel<>();
        for (Customer c : store.getCustomers()) cbm.addElement(c.toString());
        cbm.addElement("Guest: Quick checkout");
        combo = new JComboBox<>(cbm);
        combo.setSelectedIndex(cbm.getSize()-1);
        add(combo, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        JTextArea info = new JTextArea();
        info.setEditable(false);
        center.add(new JScrollPane(info), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JButton btnSelect = new JButton("Select Customer");
        add(btnSelect, BorderLayout.SOUTH);

        combo.addActionListener(e -> {
            int idx = combo.getSelectedIndex();
            if (idx >= 0 && idx < store.getCustomers().size()) {
                Customer c = store.getCustomers().get(idx);
                info.setText("Name: " + c.getName() + "\nEmail: " + c.getEmail());
            } else {
                info.setText("Guest: no email");
            }
        });

        btnSelect.addActionListener(e -> {
            int idx = combo.getSelectedIndex();
            if (idx >= 0 && idx < store.getCustomers().size()) {
                current = store.getCustomers().get(idx);
            } else current = new Customer("Guest", "guest@local");
            if (listener != null) {
                listener.customerSelected(current);
            }
            JOptionPane.showMessageDialog(this, "Selected: " + current.toString());
        });
    }

    public void setSelectedCustomer(Customer c) {
        if (c == null) return;
        for (int i=0;i<store.getCustomers().size();i++) {
            if (store.getCustomers().get(i).toString().equals(c.toString())) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.setSelectedIndex(combo.getItemCount()-1);
    }

    public void setListener(CustomerSelectionListener l) { this.listener = l; }
}
