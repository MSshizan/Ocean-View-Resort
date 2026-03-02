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

/**
 * Modern Embedded Update Room Panel
 */
public class EmbeddedUpdateRoomProAdvanced extends JPanel {

    private JTextField searchRoomField, roomNumField, roomTypeField;
    private JComboBox<String> checkStatusCombo;
    private JTextArea descriptionArea;
    private JButton searchButton, updateButton, clearButton;

    private Integer currentRoomId = null;

    public EmbeddedUpdateRoomProAdvanced() {
        // ===== Apply FlatLightLaf =====
        try { UIManager.setLookAndFeel(new FlatLightLaf()); } 
        catch (Exception e) { e.printStackTrace(); }

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 650));

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
        header.setPreferredSize(new Dimension(700, 100));
        header.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Update Room");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== CONTENT PANEL =====
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        addLabel("Enter Room Number", row, gbc, content);
        searchRoomField = addTextField(row++, gbc, content);
        searchButton = new JButton("Search");
        gbc.gridx = 2;
        gbc.gridy = row-1;
        content.add(searchButton, gbc);

        addLabel("Room Number", row, gbc, content);
        roomNumField = addTextField(row++, gbc, content);
        roomNumField.setEditable(false);
        roomNumField.setBackground(Color.LIGHT_GRAY);

        addLabel("Room Type", row, gbc, content);
        roomTypeField = addTextField(row++, gbc, content);
        roomTypeField.setEditable(false);
        roomTypeField.setBackground(Color.LIGHT_GRAY);

        addLabel("Room Status", row, gbc, content);
        checkStatusCombo = addComboBox(new String[]{"CLEAN", "DIRTY"}, row++, gbc, content);

        addLabel("Description", row, gbc, content);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        gbc.gridx = 1;
        gbc.gridy = row++;
        content.add(descriptionArea, gbc);

        // ===== BUTTONS =====
        updateButton = new JButton("Update");
        updateButton.setBackground(new Color(0, 120, 255));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { updateButton.setBackground(new Color(0, 150, 255)); }
            @Override
            public void mouseExited(MouseEvent e) { updateButton.setBackground(new Color(0, 120, 255)); }
        });

        clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(180, 180, 180));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clearButton.setFocusPainted(false);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(updateButton);
        btnPanel.add(clearButton);

        gbc.gridx = 1;
        gbc.gridy = row++;
        content.add(btnPanel, gbc);

        add(content, BorderLayout.CENTER);

        // ===== ACTIONS =====
        searchButton.addActionListener(e -> searchRoom());
        updateButton.addActionListener(e -> updateRoom());
        clearButton.addActionListener(e -> clearFields());
    }

    private void searchRoom() {
        try {
            String roomText = searchRoomField.getText().trim();
            if (roomText.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter Room Number"); return; }

            int roomNumber = Integer.parseInt(roomText);
            ApiResponse<RoomDTO> response = RoomService.getInstance().getRoomById(roomNumber);

            if (response.getStatus() == 200 && response.getData() != null) {
                RoomDTO room = response.getData();
                currentRoomId = room.getRoomNumber();
                roomNumField.setText(room.getRoomNumber() + "");
                roomTypeField.setText(room.getBedType());
                checkStatusCombo.setSelectedItem(room.getCheckStatus());
                descriptionArea.setText(room.getDescription());
            } else {
                JOptionPane.showMessageDialog(this, "Room not found");
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching room");
            ex.printStackTrace();
        }
    }

    private void updateRoom() {
        if (currentRoomId == null) {
            JOptionPane.showMessageDialog(this, "Please search a room first");
            return;
        }
        RoomDTO dto = new RoomDTO();
        dto.setCheckStatus((String) checkStatusCombo.getSelectedItem());
        dto.setDescription(descriptionArea.getText());

        try {
            ApiResponse<RoomDTO> response = RoomService.getInstance()
                    .updateRoomStatusAndDescription(currentRoomId, dto);

            if (response.getStatus() == 200) {
                JOptionPane.showMessageDialog(this, "Room updated successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed: " + response.getMessage());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating room");
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        searchRoomField.setText("");
        roomNumField.setText("");
        roomTypeField.setText("");
        descriptionArea.setText("");
        checkStatusCombo.setSelectedIndex(0);
        currentRoomId = null;
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