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

public class EmbeddedDeleteReservationProAdvanced extends JPanel {

    private Long currentReservationId = null;

    private JTextField txtReservationId;
    private JTextField txtGuestName;
    private JTextField txtRoomNumber;
    private JTextField txtCheckIn;
    private JTextField txtCheckOut;

    private JButton btnSearch;
    private JButton btnDelete;
    private JButton btnClear;

    public EmbeddedDeleteReservationProAdvanced() {

        // ===== FlatLaf =====
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 600));

        // ===== HEADER (Light Blue Gradient) =====
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

        JLabel title = new JLabel("Delete Reservation");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
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
        content.setBorder(new EmptyBorder(30, 80, 30, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // ===== Reservation ID =====
        addLabel("Reservation ID", row, gbc, content);
        txtReservationId = addTextField(row++, gbc, content);

        btnSearch = new JButton("Search");
        stylePrimaryButton(btnSearch);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        content.add(btnSearch, gbc);

        // ===== Guest Name =====
        addLabel("Guest Name", row, gbc, content);
        txtGuestName = addTextField(row++, gbc, content);
        txtGuestName.setEditable(false);

        // ===== Room Number =====
        addLabel("Room Number", row, gbc, content);
        txtRoomNumber = addTextField(row++, gbc, content);
        txtRoomNumber.setEditable(false);

        // ===== Check In =====
        addLabel("Check In Date", row, gbc, content);
        txtCheckIn = addTextField(row++, gbc, content);
        txtCheckIn.setEditable(false);

        // ===== Check Out =====
        addLabel("Check Out Date", row, gbc, content);
        txtCheckOut = addTextField(row++, gbc, content);
        txtCheckOut.setEditable(false);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setOpaque(false);

        btnClear = new JButton("Clear");
        styleSecondaryButton(btnClear);

        btnDelete = new JButton("Delete");
        styleDangerButton(btnDelete);

        buttonPanel.add(btnClear);
        buttonPanel.add(btnDelete);

        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        content.add(buttonPanel, gbc);

        add(content, BorderLayout.CENTER);

        // ===== ACTIONS =====
        btnSearch.addActionListener(e -> searchReservation());
        btnDelete.addActionListener(e -> deleteReservation());
        btnClear.addActionListener(e -> clearFields());
    }

    // ================= STYLES =================

    private void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(0, 120, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(new Color(0, 150, 255)); }
            public void mouseExited(MouseEvent e) { button.setBackground(new Color(0, 120, 255)); }
        });
    }

    private void styleDangerButton(JButton button) {
        button.setBackground(new Color(220, 53, 69));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(new Color(240, 70, 90)); }
            public void mouseExited(MouseEvent e) { button.setBackground(new Color(220, 53, 69)); }
        });
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(120, 120, 120));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
    }

    // ================= LOGIC =================

    private void searchReservation() {
        try {
            String idText = txtReservationId.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Reservation ID");
                return;
            }

            Long id = Long.parseLong(idText);

            ApiResponse<ReservationDTO> response =
                    ReservationService.getInstance().getReservationById(id);

            if (response.getStatus() == 200 && response.getData() != null) {

                ReservationDTO r = response.getData();
                currentReservationId = r.getId();

                txtGuestName.setText(r.getCustomerName());
                txtRoomNumber.setText(String.valueOf(r.getRoomNumber()));
                txtCheckIn.setText(r.getCheckIn().toString());
                txtCheckOut.setText(r.getCheckOut().toString());

            } else {
                JOptionPane.showMessageDialog(this, "Reservation not found!");
                clearFields();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Reservation ID");
        }
    }

    private void deleteReservation() {

        if (currentReservationId == null) {
            JOptionPane.showMessageDialog(this, "Search reservation first");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this reservation?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            ApiResponse<String> response =
                    ReservationService.getInstance()
                            .deleteReservation(currentReservationId);

            if (response.getStatus() == 200) {
                JOptionPane.showMessageDialog(this, "Reservation deleted successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Delete failed: " + response.getMessage());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting reservation");
        }
    }

    private void clearFields() {
        txtReservationId.setText("");
        txtGuestName.setText("");
        txtRoomNumber.setText("");
        txtCheckIn.setText("");
        txtCheckOut.setText("");
        currentReservationId = null;
    }

    // ================= UTIL =================

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
        JTextField tf = new JTextField(18);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(160, 160, 160), 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(tf, gbc);
        return tf;
    }
}