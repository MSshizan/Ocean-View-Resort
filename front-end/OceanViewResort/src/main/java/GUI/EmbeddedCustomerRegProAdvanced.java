package GUI;

import Api.ApiResponse;
import DTO.ReservationDTO;
import Service.ReservationService;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;


public class EmbeddedCustomerRegProAdvanced extends JPanel {

    private JTextField idNumberField, nameField, countryField, roomNumberField, phoneField;
    private JComboBox<String> idTypeCombo;
    private JRadioButton maleRadio, femaleRadio;
    private ButtonGroup genderGroup;
    private JButton saveButton, clearButton;
    private DateTimePicker checkInPicker, checkOutPicker;

    private final ReservationService customerService = ReservationService.getInstance();

    public EmbeddedCustomerRegProAdvanced() {
        // ===== Apply FlatLightLaf Look and Feel =====
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        JLabel title = new JLabel("Customer Registration");
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

        addLabel("ID Type", row, gbc, content);
        idTypeCombo = addComboBox(new String[]{"National Identity Card", "Passport", "Driver’s License", "Social Security Number"}, row++, gbc, content);

        addLabel("ID Number", row, gbc, content);
        idNumberField = addTextField(row++, gbc, content);

        addLabel("Customer Name", row, gbc, content);
        nameField = addTextField(row++, gbc, content);

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

        addLabel("Country", row, gbc, content);
        countryField = addTextField(row++, gbc, content);

        addLabel("Room Number", row, gbc, content);
        roomNumberField = addTextField(row++, gbc, content);

        addLabel("Phone Number", row, gbc, content);
        phoneField = addTextField(row++, gbc, content);

        // ===== DATE PICKERS =====
        addLabel("Check In", row, gbc, content);
        checkInPicker = createDateTimePicker();
        gbc.gridx = 1;
        gbc.gridy = row++;
        content.add(checkInPicker, gbc);

        addLabel("Check Out", row, gbc, content);
        checkOutPicker = createDateTimePicker();
        gbc.gridx = 1;
        gbc.gridy = row++;
        content.add(checkOutPicker, gbc);

        // Link check-out min date to check-in
        checkInPicker.addDateTimeChangeListener(event -> {
            LocalDateTime checkInDateTime = checkInPicker.getDateTimePermissive();
            if (checkInDateTime != null) {
                if (checkOutPicker.getDateTimePermissive() != null &&
                        !checkOutPicker.getDateTimePermissive().isAfter(checkInDateTime)) {
                    checkOutPicker.setDateTimeStrict(checkInDateTime.plusHours(1));
                }
                checkOutPicker.getDatePicker().getSettings().setDateRangeLimits(
                        checkInDateTime.toLocalDate(), null
                );
            }
        });

        // ===== BUTTONS =====
        saveButton = new JButton("Save");
        saveButton.setBackground(new Color(0, 120, 255));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveButton.setFocusPainted(false);

        clearButton = new JButton("Clear All");
        clearButton.setBackground(new Color(180, 180, 180));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clearButton.setFocusPainted(false);

        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.anchor = GridBagConstraints.LINE_START;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(saveButton);
        btnPanel.add(clearButton);
        content.add(btnPanel, gbc);

        add(content, BorderLayout.CENTER);

        // ===== SET NAMES FOR ASSERTJ TESTING =====
        idTypeCombo.setName("idTypeCombo");
        idNumberField.setName("idNumberField");
        nameField.setName("nameField");
        maleRadio.setName("maleRadio");
        femaleRadio.setName("femaleRadio");
        countryField.setName("countryField");
        roomNumberField.setName("roomNumberField");
        phoneField.setName("phoneField");
        checkInPicker.setName("checkInPicker");
        checkOutPicker.setName("checkOutPicker");
        saveButton.setName("saveButton");
        clearButton.setName("clearButton");

        // ===== ACTIONS =====
        saveButton.addActionListener(e -> saveCustomer());
        clearButton.addActionListener(e -> clearAllFields());
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

    private DateTimePicker createDateTimePicker() {
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dateSettings.setAllowEmptyDates(false);

        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.use24HourClockFormat();

        DateTimePicker picker = new DateTimePicker(dateSettings, timeSettings);
        picker.setDateTimeStrict(LocalDateTime.now());
        return picker;
    }

    private void saveCustomer() {
        try {
            String idType = idTypeCombo.getSelectedItem().toString().trim();
            String idNumber = idNumberField.getText().trim();
            String name = nameField.getText().trim();
            String country = countryField.getText().trim();
            String phone = phoneField.getText().trim();

            int roomNumber;
            try {
                roomNumber = Integer.parseInt(roomNumberField.getText().trim());
                if (roomNumber <= 0) throw new Exception("Room Number must be > 0");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid Room Number");
                return;
            }

            String gender = maleRadio.isSelected() ? "Male" :
                    femaleRadio.isSelected() ? "Female" : "";

            LocalDateTime checkInDate = checkInPicker.getDateTimeStrict();
            LocalDateTime checkOutDate = checkOutPicker.getDateTimeStrict();

            if (idType.isEmpty() || idNumber.isEmpty() || name.isEmpty() || gender.isEmpty() ||
                    country.isEmpty() || phone.isEmpty() || checkInDate == null || checkOutDate == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                return;
            }
            if (!checkOutDate.isAfter(checkInDate)) {
                JOptionPane.showMessageDialog(this, "Check-Out must be after Check-In!");
                return;
            }

            ReservationDTO dto = new ReservationDTO(idType, idNumber, name, gender, country, roomNumber, checkInDate, checkOutDate, phone, "ACTIVE");
            ApiResponse<ReservationDTO> response = customerService.addCustomer(dto);

            if (response.getStatus() == 200 && response.getData() != null) {
                JOptionPane.showMessageDialog(this, "Reservation Saved Successfully!");

                // === AUTO OPEN BILL PDF (local or URL) ===
                String billPdfUrl = response.getData().getBillPdfUrl();
                if (billPdfUrl != null && !billPdfUrl.isEmpty()) {
                    try {
                        if (billPdfUrl.startsWith("http")) {
                            java.awt.Desktop.getDesktop().browse(new URI(billPdfUrl));
                        } else {
                            File file = new File(billPdfUrl);
                            if (file.exists()) {
                                java.awt.Desktop.getDesktop().open(file);
                            } else {
                                JOptionPane.showMessageDialog(this, "Bill file not found: " + billPdfUrl);
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "Bill PDF saved but could not be opened automatically.\n" + billPdfUrl);
                    }
                }

                clearAllFields();
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + response.getMessage());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage());
        }
    }

    private void clearAllFields() {
        idNumberField.setText("");
        nameField.setText("");
        countryField.setText("");
        roomNumberField.setText("");
        phoneField.setText("");
        genderGroup.clearSelection();
        checkInPicker.setDateTimeStrict(LocalDateTime.now());
        checkOutPicker.setDateTimeStrict(LocalDateTime.now().plusDays(1));
    }
}