package videogamestore;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GameListPanel with search, platform filter and sort.
 * - Compatible constructors:
 *    GameListPanel(Store store, Store.Cart cart, CartPanel cartPanel)
 *    GameListPanel(Store store, CartPanel cartPanel)
 *
 * If cart/cartPanel are provided it will add items to the given cart and call cartPanel.reload().
 */
public class GameListPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Store store;
    private Store.Cart cart;            // optional
    private CartPanel cartPanel;        // optional

    private DefaultListModel<String> model;
    private JList<String> list;

    private JTextField searchField;
    private JComboBox<String> platformCombo;
    private JComboBox<String> sortCombo;

    private java.util.List<Game> working;

    public GameListPanel(Store store, Store.Cart cart, CartPanel cartPanel) {
        this.store = store;
        this.cart = cart;
        this.cartPanel = cartPanel;
        init();
    }

    public GameListPanel(Store store, CartPanel cartPanel) {
        this(store, null, cartPanel);
    }

    private void init() {
        setLayout(new BorderLayout(8,8));
        working = new ArrayList<>(store.getCatalog());

        JPanel top = new JPanel(new BorderLayout(6,6));
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));

        searchField = new JTextField(24);
        searchField.setToolTipText("Search by name...");
        controls.add(new JLabel("Search:"));
        controls.add(searchField);

        platformCombo = new JComboBox<>();
        platformCombo.addItem("All Platforms");
        Set<String> platforms = new TreeSet<>();
        for (Game g : store.getCatalog()) platforms.add(g.getPlatform());
        for (String p : platforms) platformCombo.addItem(p);
        controls.add(new JLabel("Platform:"));
        controls.add(platformCombo);

        sortCombo = new JComboBox<>(new String[] {"Sort: Name", "Sort: Price"});
        controls.add(sortCombo);

        top.add(controls, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        model = new DefaultListModel<>();
        list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(list);
        add(sp, BorderLayout.CENTER);

        JTextArea details = new JTextArea();
        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setPreferredSize(new Dimension(320, 120));
        add(new JScrollPane(details), BorderLayout.EAST);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton btnAddBuy = ModernUI.createModernButton("Add to Cart — Buy");
        JButton btnAddRent7 = ModernUI.createModernButton("Add to Cart — Rent 1 week");
        JButton btnAddRent14 = ModernUI.createModernButton("Add to Cart — Rent 2 weeks");
        JButton btnRefresh = ModernUI.createModernButton("Refresh");
        bottom.add(btnAddBuy);
        bottom.add(btnAddRent7);
        bottom.add(btnAddRent14);
        bottom.add(btnRefresh);
        add(bottom, BorderLayout.SOUTH);

        refreshWorkingAndModel();

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshWorkingAndModel(); }
            public void removeUpdate(DocumentEvent e) { refreshWorkingAndModel(); }
            public void changedUpdate(DocumentEvent e) { refreshWorkingAndModel(); }
        });

        platformCombo.addActionListener(e -> refreshWorkingAndModel());
        sortCombo.addActionListener(e -> refreshWorkingAndModel());
        btnRefresh.addActionListener(e -> {
            working = new ArrayList<>(store.getCatalog());
            refreshWorkingAndModel();
            if (cartPanel != null) cartPanel.reload();
        });

        list.addListSelectionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx >= 0) {
                Game g = working.get(idx);
                StringBuilder sb = new StringBuilder();
                sb.append(g.getDisplayString()).append("\n");
                sb.append("Platform: ").append(g.getPlatform()).append("\n");
                sb.append(g.isPhysical() ? "Physical copy" : "Digital copy").append("\n");

                if (g instanceof DigitalGame) {
                    sb.append("Size: ").append(((DigitalGame) g).getDownloadSizeMb()).append(" MB\n");
                } else if (g instanceof PhysicalGame) {

                	try {
                        sb.append("Weight: ").append(((PhysicalGame) g).getWeightGrams()).append(" g\n");
                    } catch (Exception ignored) {}
                    try {
                        sb.append("Stock: ").append(((PhysicalGame) g).getStock()).append("\n");
                    } catch (Exception ignored) {}
                }
                details.setText(sb.toString());
            } else {
                details.setText("");
            }
        });

        btnAddBuy.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx < 0) { JOptionPane.showMessageDialog(this, "Select a game first."); return; }
            Game g = working.get(idx);
            if (g.isPhysical()) {

            	try {
                    PhysicalGame pg = (PhysicalGame) g;
                    if (!pg.isInStock()) {
                        JOptionPane.showMessageDialog(this, "Out of stock!");
                        return;
                    }
                } catch (Throwable ignored) {}
            }
            if (cart == null) {
                JOptionPane.showMessageDialog(this, "Cart not available. Contact dev.");
                return;
            }
            cart.add(g);
            JOptionPane.showMessageDialog(this, "Added to cart (Buy): " + g.getName());
            if (cartPanel != null) cartPanel.reload();
        });

        btnAddRent7.addActionListener(e -> addRentAction(7));
        btnAddRent14.addActionListener(e -> addRentAction(14));
    }

    private void addRentAction(int days) {
        int idx = list.getSelectedIndex();
        if (idx < 0) { JOptionPane.showMessageDialog(this, "Select a game first."); return; }
        Game g = working.get(idx);
        if (g.isPhysical()) {
            try {
                PhysicalGame pg = (PhysicalGame) g;
                if (!pg.isInStock()) {
                    JOptionPane.showMessageDialog(this, "Out of stock!");
                    return;
                }
            } catch (Throwable ignored) {}
        }
        if (cart == null) {
            JOptionPane.showMessageDialog(this, "Cart not available. Contact dev.");
            return;
        }
        Store.Cart.CartItem it = cart.new CartItem(g);
        it.setRent(days);
        cart.getItems().add(it);
        JOptionPane.showMessageDialog(this, "Added to cart (Rent " + days + " days): " + g.getName());
        if (cartPanel != null) cartPanel.reload();
    }

    /** rebuild the working list from store, apply filters and update the model */
    private void refreshWorkingAndModel() {
        String q = searchField.getText().trim().toLowerCase();
        String platformFilter = (String) platformCombo.getSelectedItem();
        boolean allPlatforms = platformFilter == null || platformFilter.equals("All Platforms");

        working = new ArrayList<>(store.getCatalog());

        if (!allPlatforms) {
            working = working.stream()
                    .filter(g -> g.getPlatform().equalsIgnoreCase(platformFilter))
                    .collect(Collectors.toList());
        }

        if (!q.isEmpty()) {
            working = working.stream()
                    .filter(g -> g.getName().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        String sort = (String) sortCombo.getSelectedItem();
        if ("Sort: Price".equals(sort)) {
            working.sort(Comparator.comparingDouble(Game::getBasePrice));
        } else {
            working.sort(Comparator.comparing(Game::getName, String.CASE_INSENSITIVE_ORDER));
        }

        model.clear();
        for (Game g : working) model.addElement(g.getDisplayString());
    }

    /** expose a reload method so MainFrame or others can refresh the list */
    public void reloadList() {
        refreshWorkingAndModel();
    }

    /** allow main frame to set active customer if you want to use it later */
    public void setActiveCustomer(Customer c) {
    }
}
