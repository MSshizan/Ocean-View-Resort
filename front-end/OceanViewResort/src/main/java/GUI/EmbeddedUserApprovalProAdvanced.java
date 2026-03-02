package GUI;

import DTO.UserDTO;
import Service.UserService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Professional User Approval Panel with full backend logic + light blue header
 */
public class EmbeddedUserApprovalProAdvanced extends JPanel {

    private final JTable userTable;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;

    private final UserService userService = UserService.getInstance();

    public EmbeddedUserApprovalProAdvanced() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ===== HEADER =====
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 170, 255),
                        getWidth(), getHeight(), new Color(180, 220, 255)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(900, 80));
        header.setLayout(new GridBagLayout());
        JLabel title = new JLabel("User Approval");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"Name", "Email", "Address", "Phone", "Status", "Created At"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(28);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Center all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < userTable.getColumnCount(); i++) {
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(5,5,5,5)
        ));
        add(scrollPane, BorderLayout.CENTER);

        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel searchLabel = new JLabel("Search by Email:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        add(searchPanel, BorderLayout.SOUTH);

        // ===== LOAD USERS FROM SERVICE =====
        loadUsers();

        // ===== TABLE CLICK ACTION =====
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = userTable.getSelectedRow();
                if (row >= 0) {
                    String email = (String) tableModel.getValueAt(row, 1);
                    String status = (String) tableModel.getValueAt(row, 4);

                    int option = JOptionPane.showConfirmDialog(
                            null,
                            status.equalsIgnoreCase("Inactive") ?
                                    "Do you want to approve this user?" :
                                    "Do you want to disapprove this user?",
                            "User Approval",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (option == JOptionPane.YES_OPTION) {
                        boolean success = status.equalsIgnoreCase("Inactive") ?
                                userService.approveUser(email) :
                                userService.disapproveUser(email);

                        if (success) {
                            JOptionPane.showMessageDialog(null, "Operation successful!");
                            loadUsers(); // refresh table
                        } else {
                            JOptionPane.showMessageDialog(null, "Operation failed!");
                        }
                    }
                }
            }
        });

        // ===== SEARCH BUTTONS =====
        searchButton.addActionListener(e -> searchUser());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            loadUsers();
        });
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<UserDTO> users = userService.getAllUsers();
        for (UserDTO u : users) {
            tableModel.addRow(new Object[]{
                    u.getName(),
                    u.getEmail(),
                    u.getAddress(),
                    u.getPhoneNumber(),
                    u.getStatus(),
                    u.getCreatedAt()
            });
        }
    }

    private void searchUser() {
        String email = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        if (email.isEmpty()) {
            loadUsers();
            return;
        }

        UserDTO user = userService.findUserByEmail(email);
        if (user != null) {
            tableModel.addRow(new Object[]{
                    user.getName(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getPhoneNumber(),
                    user.getStatus(),
                    user.getCreatedAt()
            });
        } else {
            JOptionPane.showMessageDialog(null, "User not found!");
        }
    }

    // ===== TEST STANDALONE =====
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new FlatLightLaf()); }
        catch (Exception e) { e.printStackTrace(); }

        JFrame frame = new JFrame("User Approval Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);
        frame.add(new EmbeddedUserApprovalProAdvanced());
        frame.setVisible(true);
    }
}