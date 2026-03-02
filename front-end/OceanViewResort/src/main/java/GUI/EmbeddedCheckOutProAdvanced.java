package GUI;

import Api.ApiResponse;
import DTO.ReservationDTO;
import Service.ReservationService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern Embedded Check Out Panel
 */
public class EmbeddedCheckOutProAdvanced extends JPanel {

    private JTextField reservationIdField, guestNameField, roomNumberField, checkInField, checkOutField;
    private JButton searchButton, checkOutButton, clearButton;
    private Long currentReservationId = null;

    public EmbeddedCheckOutProAdvanced() {
        // ===== Apply FlatLaf =====
        try { UIManager.setLookAndFeel(new FlatLightLaf()); }
        catch (Exception e) { e.printStackTrace(); }

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(920, 550));

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
        header.setPreferredSize(new Dimension(920, 100));
        header.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Check Out");
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

        // Reservation ID & Search
        addLabel("Enter Reservation ID", row, gbc, content);
        reservationIdField = addTextField(row++, gbc, content);
        searchButton = new JButton("Search");
        gbc.gridx = 2;
        gbc.gridy = row - 1;
        content.add(searchButton, gbc);

        // Guest Name
        addLabel("Guest Name", row, gbc, content);
        guestNameField = addTextField(row++, gbc, content);
        guestNameField.setEditable(false);
        guestNameField.setBackground(Color.LIGHT_GRAY);

        // Room Number
        addLabel("Room Number", row, gbc, content);
        roomNumberField = addTextField(row++, gbc, content);
        roomNumberField.setEditable(false);
        roomNumberField.setBackground(Color.LIGHT_GRAY);

        // Check In
        addLabel("Check In Date", row, gbc, content);
        checkInField = addTextField(row++, gbc, content);
        checkInField.setEditable(false);
        checkInField.setBackground(Color.LIGHT_GRAY);

        // Check Out
        addLabel("Check Out Date", row, gbc, content);
        checkOutField = addTextField(row++, gbc, content);
        checkOutField.setEditable(false);
        checkOutField.setBackground(Color.LIGHT_GRAY);

        // Buttons
        checkOutButton = new JButton("Check Out");
        checkOutButton.setBackground(new Color(0, 120, 255));
        checkOutButton.setForeground(Color.WHITE);
        checkOutButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        checkOutButton.setFocusPainted(false);
        checkOutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        checkOutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { checkOutButton.setBackground(new Color(0, 150, 255)); }
            @Override
            public void mouseExited(MouseEvent e) { checkOutButton.setBackground(new Color(0, 120, 255)); }
        });

        clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(180, 180, 180));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clearButton.setFocusPainted(false);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(checkOutButton);
        btnPanel.add(clearButton);

        gbc.gridx = 1;
        gbc.gridy = row++;
        content.add(btnPanel, gbc);

        add(content, BorderLayout.CENTER);

        // ===== ACTIONS =====
        searchButton.addActionListener(e -> searchReservation());
        checkOutButton.addActionListener(e -> performCheckOut());
        clearButton.addActionListener(e -> clearFields());
    }

    // ================= SEARCH RESERVATION =================
    private void searchReservation() {
        try {
            String idText = reservationIdField.getText().trim();
            if (idText.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter Reservation ID"); return; }

            Long id = Long.parseLong(idText);
            ApiResponse<ReservationDTO> response = ReservationService.getInstance().getReservationById(id);

            if (response.getStatus() == 200 && response.getData() != null) {
                ReservationDTO reservation = response.getData();
                currentReservationId = reservation.getId();

                guestNameField.setText(reservation.getCustomerName());
                roomNumberField.setText(reservation.getRoomNumber().toString());
                checkInField.setText(reservation.getCheckIn().toString());
                checkOutField.setText(reservation.getCheckOut().toString());
            } else {
                JOptionPane.showMessageDialog(this, "Reservation not found!");
                clearFields();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Reservation ID");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching reservation");
        }
    }

    // ================= CHECK OUT =================
    private void performCheckOut() {
        if (currentReservationId == null) {
            JOptionPane.showMessageDialog(this, "Please search reservation first");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to check out?",
                "Confirm Check Out",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            ApiResponse<String> response = ReservationService.getInstance().checkOut(currentReservationId);

            if (response.getStatus() == 200) {
                JOptionPane.showMessageDialog(this, "Check Out Successful!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Check Out Failed: " + response.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during checkout");
        }
    }

    // ================= CLEAR =================
    private void clearFields() {
        reservationIdField.setText("");
        guestNameField.setText("");
        roomNumberField.setText("");
        checkInField.setText("");
        checkOutField.setText("");
        currentReservationId = null;
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