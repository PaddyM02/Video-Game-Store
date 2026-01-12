package videogamestore;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class LoginWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private HashMap<String, String> credentials = new HashMap<>();

    private AnimatedBackgroundPanel backgroundPanel;
    private JPanel cardPanel;
    private JPanel loadingPanel;
    private Timer glowTimer;

    public LoginWindow() {
        super("Login - Pixel Vault Games");
        initCredentials();
        initUI();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(this::fadeIn);
    }

    private void initCredentials() {
        credentials.put("alice@gmail.com", "pass123");
        credentials.put("bob@gmail.com", "pass123");
        credentials.put("charlie@gmail.com", "pass123");
        credentials.put("paddy@gmail.com", "pass123");
        credentials.put("emma@gmail.com", "pass123");
    }

    private void initUI() {
        backgroundPanel = new AnimatedBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        setContentPane(backgroundPanel);

        cardPanel = createCardPanel();
        loadingPanel = createLoadingPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        backgroundPanel.add(cardPanel, gbc);

        loadingPanel.setVisible(false);
        backgroundPanel.add(loadingPanel, gbc);

        getRootPane().setDefaultButton(loginBtn);
    }

    private JPanel createCardPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(true);
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220), 1),
                BorderFactory.createEmptyBorder(18, 22, 18, 22)
        ));
        p.setPreferredSize(new Dimension(420, 260));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("PIXEL VAULT GAMES", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30,30,30));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        p.add(title, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 1; gbc.gridx = 0;
        p.add(new JLabel("Email:"), gbc);
        emailField = new JTextField();
        gbc.gridx = 1;
        p.add(emailField, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        p.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField();
        gbc.gridx = 1;
        p.add(passwordField, gbc);

        JCheckBox showPass = new JCheckBox("Show password");
        showPass.setOpaque(false);
        showPass.addActionListener(e -> passwordField.setEchoChar(showPass.isSelected() ? (char)0 : '•'));
        gbc.gridy = 3; gbc.gridx = 1;
        p.add(showPass, gbc);

        loginBtn = new JButton("Login");
        stylePrimaryButton(loginBtn);
        loginBtn.addActionListener(e -> onLoginClicked());
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        p.add(loginBtn, gbc);

        startGlowEffect();

        return p;
    }

    private JPanel createLoadingPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(420,260));
        JLabel loadingLabel = new JLabel("Signing you in...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loadingLabel.setForeground(Color.white);
        p.add(loadingLabel, BorderLayout.CENTER);
        return p;
    }

    private void stylePrimaryButton(JButton b) {
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(10, 120, 240));
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void startGlowEffect() {
        final int delay = 40;
        glowTimer = new Timer(delay, null);
        glowTimer.addActionListener(e -> {
            double t = (System.currentTimeMillis() % 1500) / 1500.0;
            double level = (Math.sin(t * Math.PI * 2) * 0.5 + 0.5);
            int base = 10;
            int green = (int)(120 + level * 80);
            int blue = (int)(240 + level * 10);
            loginBtn.setBackground(new Color(base, green, blue));
        });
        glowTimer.start();
    }

    private void fadeIn() {
        try {
            setOpacitySupportedAndFade();
        } catch (Throwable t) {
            scaleInFallback();
        }
    }

    private void setOpacitySupportedAndFade() throws Exception {
        setUndecorated(false);
        setVisible(true);

        try {
            for (float f = 0f; f <= 1f; f += 0.05f) {
                Thread.sleep(12);
            }
        } catch (Throwable t) {
            for (float f = 0f; f <= 1f; f += 0.05f) {
                setOpacity(f);
                try { Thread.sleep(12); } catch (InterruptedException ignored) {}
            }
        }
    }

    private void scaleInFallback() {
        Dimension pref = getPreferredSize();
        setSize((int)(pref.width*0.85), (int)(pref.height*0.85));
        setVisible(true);
        for (int i = 85; i <= 100; i += 3) {
            setSize(pref.width * i / 100, pref.height * i / 100);
            setLocationRelativeTo(null);
            try { Thread.sleep(8); } catch (InterruptedException ignored) {}
        }
    }

    private void onLoginClicked() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (!credentials.get(email).equals(password)) {
            doShake();
            JOptionPane.showMessageDialog(this, "Incorrect password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        slideToLoadingAndOpenMain(email);

        if (!credentials.get(email).equals(password)) {
            doShake();
            JOptionPane.showMessageDialog(this, "Incorrect password!", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doShake() {
        final Point p = getLocation();
        final int shakeDistance = 8;
        final int cycles = 8;
        new Thread(() -> {
            try {
                for (int i = 0; i < cycles; i++) {
                    setLocation(p.x + ((i % 2 == 0) ? -shakeDistance : shakeDistance), p.y);
                    Thread.sleep(30);
                }
            } catch (InterruptedException ignored) {}
            setLocation(p);
        }).start();
    }

    private void slideToLoadingAndOpenMain(String email) {
        if (glowTimer != null) glowTimer.stop();

        loadingPanel.setVisible(true);
        final Point center = cardPanel.getLocation();
        final int width = cardPanel.getWidth();

        final int steps = 20;
        final int delay = 12;
        Timer slideTimer = new Timer(delay, null);
        final int[] step = {0};

        slideTimer.addActionListener(e -> {
            step[0]++;
            float t = step[0] / (float)steps;
            int dx = (int)(t * (width + 40));
            cardPanel.setLocation(center.x - dx, center.y);
            loadingPanel.setLocation(center.x - dx + (width + 40), center.y);
            if (step[0] >= steps) {
                slideTimer.stop();
                new Timer(600, ev -> {
                    ((Timer)ev.getSource()).stop();
                    fadeOutAndOpenMain();
                }).start();
            }
        });
        slideTimer.start();
    }

    private void fadeOutAndOpenMain() {
        new Thread(() -> {
            try {
                try {
                    for (float f = 1f; f >= 0f; f -= 0.06f) {
                        setOpacity(Math.max(0f, f));
                        Thread.sleep(12);
                    }
                } catch (Throwable t) {
                    Point p = getLocation();
                    for (int i = 0; i < 30; i++) {
                        setLocation(p.x, p.y - 6);
                        Thread.sleep(8);
                    }
                }
            } catch (InterruptedException ignored) {}
            SwingUtilities.invokeLater(() -> {
                Store store = new Store();
                try {
                    store.loadGamesFromCSV("resources/games.csv");
                    store.loadCustomersFromTXT("resources/customers.txt");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                MainFrame mf = new MainFrame(store);
                String loggedEmail = emailField.getText().trim();
                Customer loggedIn = store.findCustomerByEmail(loggedEmail);
                if (loggedIn != null) {
                    mf.setLoggedInCustomer(loggedIn);
                }
                mf.setVisible(true);
                dispose();
            });
        }).start();
    }

    private static class AnimatedBackgroundPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private float pos = 0f;
        private Timer t;

        public AnimatedBackgroundPanel() {
            setPreferredSize(new Dimension(800, 520));
            t = new Timer(30, e -> {
                pos += 0.01f;
                if (pos > 1f) pos = 0f;
                repaint();
            });
            t.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth(), h = getHeight();

            Color c1 = new Color(255, 235, 205);
            Color c3 = new Color(220, 240, 255);

            float center = 0.2f + 0.6f * ((float)Math.sin(pos * Math.PI * 2) * 0.5f + 0.5f);

            Graphics2D g2 = (Graphics2D)g;
            GradientPaint gp = new GradientPaint(0, 0, c1, w, h, c3);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);

            int rx = (int)(w * center);
            int ry = h/3;
            int radius = Math.max(w, h)/2;
            RadialGradientPaint rg = new RadialGradientPaint(
                    new Point(rx, ry),
                    radius,
                    new float[] {0f, 0.6f, 1f},
                    new Color[] {new Color(255,255,255,120), new Color(255,255,255,40), new Color(255,255,255,0)}
            );
            g2.setPaint(rg);
            g2.fillRect(0,0,w,h);
        }
    }

    @Override
    public void setOpacity(float opacity) {
        try {
            super.setOpacity(opacity);
        } catch (Throwable ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginWindow w = new LoginWindow();
            w.setVisible(true);
        });
    }
}
