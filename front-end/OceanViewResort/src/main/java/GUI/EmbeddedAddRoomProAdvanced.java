package GUI;

import Api.ApiResponse;
import DTO.RoomDTO;
import Service.RoomService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmbeddedAddRoomProAdvanced extends JPanel {

    private final RoomService roomService;

    JTextField roomNumField;
    JComboBox<String> availableCombo;
    JComboBox<String> checkStatusCombo;
    JComboBox<String> bedTypeCombo;
    JTextArea descriptionArea;
    JTextField priceField;
    JButton addButton;

    // ===== Constructor with DI =====
    public EmbeddedAddRoomProAdvanced(RoomService roomService) {
        this.roomService = roomService;
        initComponents();
    }

    // Default constructor uses singleton
    public EmbeddedAddRoomProAdvanced() {
        this(RoomService.getInstance());
    }

    private void initComponents() {
        try { UIManager.setLookAndFeel(new FlatLightLaf()); } catch (Exception ignored) {}

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(650, 650));

        // ===== HEADER =====
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
        header.setPreferredSize(new Dimension(650, 100));
        header.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Add Room");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        header.add(title);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        // ===== CONTENT =====
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // ===== Room Number =====
        addLabel("Room Number", row, gbc, content);
        roomNumField = addTextField(row++, gbc, content);
        roomNumField.setName("roomNumField");

        // ===== Available =====
        addLabel("Available", row, gbc, content);
        availableCombo = addComboBox(new String[]{"Available", "Not Available"}, row++, gbc, content);
        availableCombo.setName("availableCombo");

        // ===== Check Status =====
        addLabel("Check Status", row, gbc, content);
        checkStatusCombo = addComboBox(new String[]{"Clean", "Dirty"}, row++, gbc, content);
        checkStatusCombo.setName("checkStatusCombo");

        // ===== Bed Type =====
        addLabel("Bed Type", row, gbc, content);
        bedTypeCombo = addComboBox(
                new String[]{"Twin bed", "Single bed", "Double bed", "Queen bed", "King bed", "California king bed"},
                row++, gbc, content);
        bedTypeCombo.setName("bedTypeCombo");

        // ===== Description =====
        addLabel("Description", row, gbc, content);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        descriptionArea.setName("descriptionArea"); // <<<<<< Name set
        gbc.gridx = 1;
        gbc.gridy = row++;
        content.add(descriptionArea, gbc);

        // ===== Price =====
        addLabel("Price", row, gbc, content);
        priceField = addTextField(row++, gbc, content);
        priceField.setName("priceField");

        // ===== ADD BUTTON =====
        addButton = new JButton("ADD");
        addButton.setBackground(new Color(0, 120, 255));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        addButton.setName("addButton"); // <<<<<< Name set

        addButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { addButton.setBackground(new Color(0, 150, 255)); }
            @Override public void mouseExited(MouseEvent e) { addButton.setBackground(new Color(0, 120, 255)); }
        });

        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.anchor = GridBagConstraints.LINE_START;
        content.add(addButton, gbc);

        add(content, BorderLayout.CENTER);

        // ===== ACTION =====
        addButton.addActionListener(e -> addRoomAction());
    }

    private void addRoomAction() {
        try {
            RoomDTO dto = new RoomDTO();
            dto.setRoomNumber(Integer.parseInt(roomNumField.getText().trim()));
            dto.setAvailable((String) availableCombo.getSelectedItem());
            dto.setCheckStatus((String) checkStatusCombo.getSelectedItem());
            dto.setBedType((String) bedTypeCombo.getSelectedItem());
            dto.setDescription(descriptionArea.getText());
            dto.setPrice(Double.parseDouble(priceField.getText()));

            ApiResponse<RoomDTO> response = roomService.addRoom(dto);

            if (response.getStatus() == 200) {
                JOptionPane.showMessageDialog(this, "Room added successfully!");
                roomNumField.setText("");
                descriptionArea.setText("");
                priceField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add room: " + response.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
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

    private JComboBox<String> addComboBox(String[] items, int row, GridBagConstraints gbc, JPanel panel) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(combo, gbc);
        return combo;
    }
}