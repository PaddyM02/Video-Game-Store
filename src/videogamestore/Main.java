
package videogamestore;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Store store = new Store();
            try {
                store.loadGamesFromCSV("resources/games.csv");
                store.loadCustomersFromTXT("resources/customers.txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
            MainFrame frame = new MainFrame(store);
            frame.setVisible(true);
            
            
        });
        UIManager.put("ScrollBar.thumbHighlight", new Color(200,200,200));
        UIManager.put("ScrollBar.thumbDarkShadow", new Color(150,150,150));
        UIManager.put("ScrollBar.track", new Color(240,240,240));

        
    }
}
