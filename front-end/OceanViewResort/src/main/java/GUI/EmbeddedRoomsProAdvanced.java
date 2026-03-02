package GUI;

import Api.ApiResponse;
import DTO.RoomDTO;
import Service.RoomService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Professional Rooms Panel with FlatLaf design
 */
public class EmbeddedRoomsProAdvanced extends JPanel {

    private final JTable roomTable;
    private final DefaultTableModel tableModel;
    private final JComboBox<String> bedTypeCombo;
    private final JCheckBox onlyAvailableCheck;
    
    private final RoomService roomService = RoomService.getInstance();

    public EmbeddedRoomsProAdvanced() {
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
        header.setPreferredSize(new Dimension(1200, 80));
        header.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Rooms Deatils");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"Room Number", "Available", "Status", "Bed Type", "Description", "Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        roomTable = new JTable(tableModel);
        roomTable.setRowHeight(28);
        roomTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roomTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Center table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < roomTable.getColumnCount(); i++) {
            roomTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(5,5,5,5)
        ));
        add(scrollPane, BorderLayout.CENTER);

        // ===== FILTER PANEL =====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(new Color(245, 245, 245));
        filterPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel bedTypeLabel = new JLabel("Room Bed Type:");
        bedTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bedTypeCombo = new JComboBox<>(new String[]{
                "Twin bed", "Single bed", "Double bed", "Queen bed", "King bed", "California king bed"
        });
        bedTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bedTypeCombo.addActionListener(e -> applyFilters());

        onlyAvailableCheck = new JCheckBox("Only Available Rooms");
        onlyAvailableCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        onlyAvailableCheck.setBackground(new Color(245, 245, 245));
        onlyAvailableCheck.addActionListener(e -> applyFilters());

        filterPanel.add(bedTypeLabel);
        filterPanel.add(bedTypeCombo);
        filterPanel.add(onlyAvailableCheck);

        add(filterPanel, BorderLayout.SOUTH);

        // ===== LOAD DATA =====
        loadAllRooms();
    }

    private void loadAllRooms() {
        tableModel.setRowCount(0);
        ApiResponse<List<RoomDTO>> response = roomService.getAllRooms();
        List<RoomDTO> rooms = response.getData();
        if (rooms != null) {
            updateTable(rooms);
        }
    }

    private void applyFilters() {
        ApiResponse<List<RoomDTO>> response = roomService.getAllRooms();
        List<RoomDTO> rooms = response.getData();
        if (rooms == null) return;

        String selectedBedType = bedTypeCombo.getSelectedItem().toString();
        boolean onlyAvailable = onlyAvailableCheck.isSelected();

        List<RoomDTO> filtered = roomService.filterRooms(rooms,
                "All".equals(selectedBedType) ? null : selectedBedType, onlyAvailable);
        updateTable(filtered);
    }

    private void updateTable(List<RoomDTO> rooms) {
        tableModel.setRowCount(0);
        for (RoomDTO r : rooms) {
            tableModel.addRow(new Object[]{
                    r.getRoomNumber(),
                    r.getAvailable(),
                    r.getCheckStatus(),
                    r.getBedType(),
                    r.getDescription(),
                    r.getPrice()
            });
        }
    }

  
}