package videogamestore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernUI {

    public static JButton createModernButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(230, 230, 230));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(245,245,245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(230,230,230));
            }
        });

        return btn;
        
        
    }
    
}


