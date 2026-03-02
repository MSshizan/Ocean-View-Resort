package GUI;

import DTO.UserDTO;
import Service.AuthManager;
import Service.UserService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class NewForgetPasswordWithBackend extends JFrame {

    private JTextField emailField, questionField, answerField;
    private JPasswordField newPasswordField;
    private final AuthManager authManager;

    public NewForgetPasswordWithBackend() {
        authManager = new AuthManager();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initUI();
    }

    private void initUI() {
        setTitle("Reset Password");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Gradient Background
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

        // Card Panel
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(450, 420));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(35, 40, 35, 40));

        JLabel title = new JLabel("Reset Your Password");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Verify your identity to continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        emailField = createField("Email Address");
        questionField = createField("Security Question");
        questionField.setEditable(false);
        answerField = createField("Security Answer");

        newPasswordField = new JPasswordField();
        newPasswordField.setBorder(BorderFactory.createTitledBorder("New Password"));
        newPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton searchBtn = new JButton("Search");
        searchBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton saveBtn = new JButton("Save New Password");
        saveBtn.setBackground(new Color(58, 123, 213));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton loginBtn = new JButton("Back to Login");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Layout
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));
        card.add(emailField);
        card.add(Box.createVerticalStrut(10));
        card.add(searchBtn);
        card.add(Box.createVerticalStrut(15));
        card.add(questionField);
        card.add(Box.createVerticalStrut(10));
        card.add(answerField);
        card.add(Box.createVerticalStrut(10));
        card.add(newPasswordField);
        card.add(Box.createVerticalStrut(20));
        card.add(saveBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(loginBtn);

        background.add(card);
        add(background);

        // ===== Action Listeners =====
        searchBtn.addActionListener(e -> searchSecurityQuestion());
        saveBtn.addActionListener(e -> saveNewPassword());
        loginBtn.addActionListener(e -> {
            dispose();
            new newlogin().setVisible(true);
        });

        setVisible(true);
    }

    private JTextField createField(String title) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return field;
    }

    private void searchSecurityQuestion() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an email");
            return;
        }

        String question = UserService.getInstance().getSecurityQuestion(email);
        if (question == null) {
            JOptionPane.showMessageDialog(this, "Email not found");
            return;
        }

        questionField.setText(question);
        emailField.setEditable(false);
        questionField.setEditable(false);
    }

    private void saveNewPassword() {
        String email = emailField.getText().trim();
        String question = questionField.getText().trim();
        String answer = answerField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());

        if (email.isEmpty() || question.isEmpty() || answer.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields");
            return;
        }

        String message = authManager.forgotPassword(email, question, answer, newPassword);
        JOptionPane.showMessageDialog(this, message);

        if ("Password updated successfully".equalsIgnoreCase(message)) {
            dispose();
            new newlogin().setVisible(true);
        }
    }

   
}