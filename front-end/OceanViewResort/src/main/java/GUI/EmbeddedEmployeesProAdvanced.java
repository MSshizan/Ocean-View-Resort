package GUI;

import Api.ApiResponse;
import DTO.StaffDTO;
import Service.StaffService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Professional Employees Panel with FlatLaf design
 */
public class EmbeddedEmployeesProAdvanced extends JPanel {

    private final JTable employeeTable;
    private final DefaultTableModel tableModel;
    private final JComboBox<String> departmentCombo;

    private List<StaffDTO> allStaff; // store data once
    private final StaffService staffService = StaffService.getInstance();

    public EmbeddedEmployeesProAdvanced() {
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
        JLabel title = new JLabel("All Employees");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"Name", "Age", "Gender", "Department", "Role", "Contact Number", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(28);
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Center table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            employeeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(5,5,5,5)
        ));
        add(scrollPane, BorderLayout.CENTER);

        // ===== FILTER PANEL =====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(new Color(245, 245, 245));
        filterPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel departmentLabel = new JLabel("Department:");
        departmentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        String[] departments = {
                "All Departments",
                "Front Office",
                "Housekeeping",
                "Food & Beverage (F&B)",
                "Driver/Travels",
                "Kitchen / Culinary",
                "Maintenance / Engineering",
                "Security",
                "Spa & Recreation"
        };
        departmentCombo = new JComboBox<>(departments);
        departmentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        departmentCombo.addActionListener(e -> applyFilters());

        filterPanel.add(departmentLabel);
        filterPanel.add(departmentCombo);

        add(filterPanel, BorderLayout.SOUTH);

        // ===== LOAD DATA =====
        loadAllEmployees();
    }

    private void loadAllEmployees() {
        ApiResponse<List<StaffDTO>> response = staffService.getAllStaff();
        if (response != null && response.getData() != null) {
            allStaff = response.getData();
            updateTable(allStaff);
        }
    }

    private void applyFilters() {
        if (allStaff == null) return;

        String selectedDept = departmentCombo.getSelectedItem().toString();

        if ("All Departments".equalsIgnoreCase(selectedDept)) {
            updateTable(allStaff);
            return;
        }

        List<StaffDTO> filtered = staffService.filterByDepartment(allStaff, selectedDept);
        updateTable(filtered);
    }

    private void updateTable(List<StaffDTO> staffList) {
        tableModel.setRowCount(0);
        for (StaffDTO staff : staffList) {
            tableModel.addRow(new Object[]{
                    staff.getName(),
                    staff.getAge(),
                    staff.getGender(),
                    staff.getDepartment(),
                    staff.getJobRole(),
                    staff.getContact(),
                    staff.getEmail()
            });
        }
    }

    // ===== TEST STANDALONE =====
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new FlatLightLaf()); }
        catch (Exception e) { e.printStackTrace(); }

        JFrame frame = new JFrame("All Employees");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.add(new EmbeddedEmployeesProAdvanced());
        frame.setVisible(true);
    }
}