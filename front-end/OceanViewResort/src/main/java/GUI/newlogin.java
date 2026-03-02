/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Api.AuthContext;
import DTO.LoginResponseDTO;
import Service.AuthManager;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class newlogin extends JFrame {

    private final AuthManager authManager;

    private JTextField emailField;
    private JPasswordField passwordField;

    public newlogin() {
        authManager = new AuthManager();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initUI();
    }

    private void initUI() {

        setTitle("Login");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Gradient Background Panel
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(58, 123, 213);
                Color color2 = new Color(0, 210, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };

        background.setLayout(new GridBagLayout());

        // Login Card Panel
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(400, 420));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Login to your account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(58, 123, 213));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton signUpBtn = new JButton("Create Account");
        signUpBtn.setFocusPainted(false);
        signUpBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setBorderPainted(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setForeground(new Color(58, 123, 213));
        forgotBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Spacing
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));
        card.add(emailField);
        card.add(Box.createVerticalStrut(15));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(25));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(15));
        card.add(signUpBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(forgotBtn);

        background.add(card);
        add(background);

        // Actions
        loginBtn.addActionListener(e -> login());
        signUpBtn.addActionListener(e -> {
            dispose();
            new NewSignUpWithBackend();
            
           
        });

        forgotBtn.addActionListener(e -> {
            dispose();
            new NewForgetPasswordWithBackend();
           
        });

        setVisible(true);
    }
  private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        System.out.println("[DEBUG] Login button clicked with email: '" + email + "' password: '" + password + "'");

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Email and Password cannot be empty!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LoginResponseDTO loggedInUser = authManager.login(email, password);

        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid credentials or inactive account.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("[DEBUG] Login failed for email: " + email);
            return;
        }

        AuthContext.getInstance().setJwtToken(loggedInUser.getToken());
        AuthContext.getInstance().setRole(loggedInUser.getRole());

        JOptionPane.showMessageDialog(this,
                "Welcome " + loggedInUser.getName(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
        new ProDashboardFlat(loggedInUser.getRole());
    }
}