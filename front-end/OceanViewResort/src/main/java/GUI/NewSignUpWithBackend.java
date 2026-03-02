
package GUI;

import DTO.UserDTO;
import Service.AuthManager;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class NewSignUpWithBackend extends JFrame {

    private JTextField nameField, emailField, answerField, addressField, phoneField;
    private JPasswordField passwordField;
    private JComboBox<String> securityCombo;
    private final AuthManager authManager;

    public NewSignUpWithBackend() {
        authManager = new AuthManager();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initUI();
    }

    private void initUI() {
        setTitle("Create Account");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // ===== Gradient Background =====
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(58, 123, 213),
                        0, getHeight(), new Color(0, 210, 255)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new GridBagLayout());

        // ===== Card Panel =====
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(550, 580));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(35, 45, 35, 45));

        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Please fill the details below");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        nameField = createField("Full Name");
        emailField = createField("Email Address");
        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        securityCombo = new JComboBox<>(new String[]{
                "What is the name of your first pet?",
                "What was your first car?",
                "What elementary school did you attend?",
                "What is the name of the town where you were born?"
        });
        securityCombo.setBorder(BorderFactory.createTitledBorder("Security Question"));
        securityCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        answerField = createField("Security Answer");
        addressField = createField("Address");
        phoneField = createField("Phone Number");

        JButton signUpBtn = new JButton("Create Account");
        signUpBtn.setBackground(new Color(58, 123, 213));
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setFocusPainted(false);
        signUpBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton loginBtn = new JButton("Back to Login");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Layout spacing
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));
        card.add(nameField);
        card.add(Box.createVerticalStrut(12));
        card.add(emailField);
        card.add(Box.createVerticalStrut(12));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(12));
        card.add(securityCombo);
        card.add(Box.createVerticalStrut(12));
        card.add(answerField);
        card.add(Box.createVerticalStrut(12));
        card.add(addressField);
        card.add(Box.createVerticalStrut(12));
        card.add(phoneField);
        card.add(Box.createVerticalStrut(25));
        card.add(signUpBtn);
        card.add(Box.createVerticalStrut(15));
        card.add(loginBtn);

        background.add(card);
        add(background);

        // ===== Action Listeners =====
        signUpBtn.addActionListener(e -> handleSignUp());
        loginBtn.addActionListener(e -> {
            dispose();
            new newlogin();
        });

        setVisible(true);
    }

    private JTextField createField(String title) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return field;
    }

    private void handleSignUp() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String question = (String) securityCombo.getSelectedItem();
        String answer = answerField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Email, and Password cannot be empty!");
            return;
        }

        try {
            UserDTO user = new UserDTO(name, email, password, question, answer, address, phone);
            String response = authManager.signup(user);
            JOptionPane.showMessageDialog(this, response);

            if (response.toLowerCase().contains("success")) {
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        answerField.setText("");
        addressField.setText("");
        phoneField.setText("");
        securityCombo.setSelectedIndex(0);
    }

  
}