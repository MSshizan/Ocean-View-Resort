package GUI;

import Service.ReservationService;
import DTO.ReservationDTO;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/**
 * Professional Reservation Panel with FlatLaf design
 */

public class EmbeddedReservationsProAdvanced extends JPanel {

    private final JTable reservationTable;
    private final DefaultTableModel tableModel;
    private final DatePicker datePicker;
    private final JButton searchButton;
    private final JButton clearButton;

    // ✅ Make service non-final so we can inject mock
    private ReservationService reservationService = ReservationService.getInstance();
    private List<ReservationDTO> reservationList;

    public EmbeddedReservationsProAdvanced() {
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
        header.setPreferredSize(new Dimension(1400, 80));
        header.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Check Reservations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"Room Number", "Reservation ID", "Customer Name", "Country", "Personal ID", 
                            "ID Type", "Gender", "Check In", "Check Out", "Phone Number", "Status", "View Bill"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        reservationTable = new JTable(tableModel);
        reservationTable.setRowHeight(28);
        reservationTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reservationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < reservationTable.getColumnCount(); i++) {
            reservationTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(reservationTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(5,5,5,5)
        ));
        add(scrollPane, BorderLayout.CENTER);

        // ===== FILTER PANEL =====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(new Color(245, 245, 245));
        filterPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel dateLabel = new JLabel("Search by Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dateSettings.setAllowEmptyDates(false);
        datePicker = new DatePicker(dateSettings);
        datePicker.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        filterPanel.add(dateLabel);
        filterPanel.add(datePicker);
        filterPanel.add(searchButton);
        filterPanel.add(clearButton);

        add(filterPanel, BorderLayout.SOUTH);

        // ===== LOAD DATA =====
        loadAllReservations();

        // ===== BUTTON ACTIONS =====
        searchButton.addActionListener(e -> searchReservations());
        clearButton.addActionListener(e -> loadAllReservations());

        // ===== TABLE CLICK EVENT =====
        reservationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = reservationTable.rowAtPoint(evt.getPoint());
                int col = reservationTable.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == reservationTable.getColumnCount() - 1) { // "View Bill"
                    ReservationDTO dto = reservationList.get(row);
                    openBill(dto.getBillPdfUrl());
                }
            }
        });
    }

    // ✅ Made package-private for testing
   public void loadAllReservations() {
        var response = reservationService.getAllReservations();
        if (response.getData() != null) {
            reservationList = response.getData();
            populateTable(reservationList);
        }
    }

    private void searchReservations() {
        LocalDate selectedDate = datePicker.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date!");
            return;
        }

        var response = reservationService.searchByDate(selectedDate.toString());
        if (response.getData() != null) {
            reservationList = response.getData();
            populateTable(reservationList);
        } else {
            reservationList.clear();
            populateTable(reservationList);
            JOptionPane.showMessageDialog(this, "No reservations found for selected date");
        }
    }

    private void populateTable(List<ReservationDTO> list) {
        tableModel.setRowCount(0);
        for (ReservationDTO dto : list) {
            tableModel.addRow(new Object[]{
                    dto.getRoomNumber(),
                    dto.getId(),
                    dto.getCustomerName() != null ? dto.getCustomerName() : "N/A",
                    dto.getCountry() != null ? dto.getCountry() : "N/A",
                    dto.getIdNumber(),
                    dto.getIdType(),
                    dto.getGender(),
                    dto.getCheckIn(),
                    dto.getCheckOut(),
                    dto.getPhoneNumber() != null ? dto.getPhoneNumber() : "N/A",
                    dto.getStatus(),
                    "View"
            });
        }
    }

    private void openBill(String billUrl) {
        if (billUrl == null || billUrl.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No bill available for this reservation.");
            return;
        }
        try {
            if (billUrl.startsWith("http")) Desktop.getDesktop().browse(new URI(billUrl));
            else {
                File pdfFile = new File(billUrl);
                if (pdfFile.exists()) Desktop.getDesktop().open(pdfFile);
                else JOptionPane.showMessageDialog(this, "Bill file not found.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open bill.");
        }
    }

    // =========================
    // ✅ Public getters for testing
    // =========================
    public JTable getReservationTable() { return reservationTable; }
    public DatePicker getDatePicker() { return datePicker; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getClearButton() { return clearButton; }

    // ✅ Setter for injecting mock service in tests
    public void setReservationService(ReservationService service) {
        this.reservationService = service;
    }
}