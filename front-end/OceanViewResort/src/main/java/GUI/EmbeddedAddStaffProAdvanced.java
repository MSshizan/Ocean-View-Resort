package GUI;

import Api.ApiResponse;
import DTO.StaffDTO;
import Service.StaffService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern Add Staff Panel with full backend logic + light blue header
 */
public class EmbeddedAddStaffProAdvanced extends JPanel {

    private JTextField nameField, ageField, jobRoleField, contactField, emailField;
    private JComboBox<String> departmentCombo;
    private JRadioButton maleRadio, femaleRadio;
    private ButtonGroup genderGroup;
    private JButton addButton;

    private final StaffService staffService = StaffService.getInstance();

    public EmbeddedAddStaffProAdvanced() {
        // ===== Apply FlatLaf Look & Feel =====
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 600));

        // ===== HEADER PANEL (Light Blue Gradient) =====
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 170, 255),
                        0, getHeight(), new Color(180, 220, 255)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(700, 100));
        header.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Add Staff");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== CONTENT PANEL =====
        JPanel content = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(245, 245, 245),
                        0, getHeight(), new Color(230, 240, 255)
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // ===== Form Fields =====
        addLabel("Name", row, gbc, content);
        nameField = addTextField(row++, gbc, content);

        addLabel("Age", row, gbc, content);
        ageField = addTextField(row++, gbc, content);

        addLabel("Gender", row, gbc, content);
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        maleRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        femaleRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        genderPanel.setOpaque(false);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        gbc.gridx = 1;
        gbc.gridy = row++;
        content.add(genderPanel, gbc);

        addLabel("Department", row, gbc, content);
        departmentCombo = new JComboBox<>(new String[]{
                "Front Office", "Housekeeping", "F&B", "Driver/Travels",
                "Kitchen/Culinary", "Maintenance/Engineering", "Security", "Spa & Recreation"
        });
        departmentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = row++;
        content.add(departmentCombo, gbc);

        addLabel("Job Role", row, gbc, content);
        jobRoleField = addTextField(row++, gbc, content);

        addLabel("Contact", row, gbc, content);
        contactField = addTextField(row++, gbc, content);

        addLabel("Email", row, gbc, content);
        emailField = addTextField(row++, gbc, content);

        // ===== ADD BUTTON =====
        addButton = new JButton("ADD");
        addButton.setBackground(new Color(0, 120, 255));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { addButton.setBackground(new Color(0, 150, 255)); }
            @Override
            public void mouseExited(MouseEvent e) { addButton.setBackground(new Color(0, 120, 255)); }
        });

        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.anchor = GridBagConstraints.LINE_START;
        content.add(addButton, gbc);

        add(content, BorderLayout.CENTER);

        // ===== BUTTON ACTION: Full Backend Logic =====
        addButton.addActionListener(e -> addStaffAction());
    }

    private void addStaffAction() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) throw new Exception("Name is required");

            int age;
            try { age = Integer.parseInt(ageField.getText().trim()); }
            catch (NumberFormatException ex) { throw new Exception("Invalid age"); }

            String gender;
            if (maleRadio.isSelected()) gender = "Male";
            else if (femaleRadio.isSelected()) gender = "Female";
            else throw new Exception("Please select gender");

            String department = (String) departmentCombo.getSelectedItem();
            String jobRole = jobRoleField.getText().trim();
            String contact = contactField.getText().trim();
            String email = emailField.getText().trim();

            StaffDTO dto = new StaffDTO(name, age, gender, department, jobRole, contact, email);
            ApiResponse<StaffDTO> response = staffService.addStaff(dto);

            if (response.getStatus() == 200) {
                JOptionPane.showMessageDialog(this, "Staff added successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + response.getMessage());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        nameField.setText("");
        ageField.setText("");
        genderGroup.clearSelection();
        departmentCombo.setSelectedIndex(0);
        jobRoleField.setText("");
        contactField.setText("");
        emailField.setText("");
    }

    private void addLabel(String text, int row, GridBagConstraints gbc, JPanel panel) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(40, 40, 40));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(label, gbc);
    }

    private JTextField addTextField(int row, GridBagConstraints gbc, JPanel panel) {
        JTextField tf = new JTextField(20);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(tf, gbc);
        return tf;
    }
}