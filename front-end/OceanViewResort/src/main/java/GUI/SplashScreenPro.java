package GUI;

import Api.AuthContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;

public class SplashScreenPro extends JWindow {

    private float alpha = 0f; // transparency for fade-in effect
    private Timer timer;

    public SplashScreenPro() {
        // ===== Gradient background panel =====
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 102, 204),
                        getWidth(), getHeight(), new Color(102, 178, 255)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Overlay fade-in effect
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                super.paintChildren(g2);
            }
        };
        content.setLayout(new GridBagLayout());
        setContentPane(content);

        // ===== Title & Tagline =====
        JLabel title = new JLabel("Welcome to Ocean View Resort");
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 36));
        title.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("Hotel Management System");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tagline.setForeground(new Color(200, 230, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        content.add(title, gbc);

        gbc.gridy = 1;
        content.add(tagline, gbc);

        // ===== Window settings =====
        setSize(600, 400);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);

        // ===== Animation Timer (fade-in) =====
        timer = new Timer(40, e -> {
            alpha += 0.02f;
            if (alpha >= 1f) {
                alpha = 1f;
                timer.stop();
                // After splash, check session/login
                SwingUtilities.invokeLater(this::checkSessionAndProceed);
            }
            repaint();
        });
        timer.start();
    }

    // ===== Session/Login Logic from old SplashScreen =====
    private void checkSessionAndProceed() {
        if (AuthContext.getInstance().hasValidToken()) {
            if (isTokenExpired(AuthContext.getInstance().getJwtToken())) {
                AuthContext.getInstance().clear();
                new newlogin().setVisible(true); // open login if expired
            } else {
                new ProDashboardFlat(AuthContext.getInstance().getRole()); // open dashboard if valid
            }
        } else {
            new newlogin().setVisible(true); // no token -> login
        }
        dispose(); // close splash
    }

    private boolean isTokenExpired(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            JsonNode node = new ObjectMapper().readTree(payload);
            long exp = node.get("exp").asLong();
            return Instant.now().getEpochSecond() > exp;
        } catch (Exception e) {
            return true;
        }
    }

  
}