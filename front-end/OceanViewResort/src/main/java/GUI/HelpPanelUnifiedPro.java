

package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class HelpPanelUnifiedPro extends JPanel {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public HelpPanelUnifiedPro() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 120, 255));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Help & Support Center");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Step-by-Step Instructions");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(Color.WHITE);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ===== SPLIT PANEL =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(240);
        splitPane.setDividerSize(2);
        splitPane.setBorder(null);
        splitPane.setBackground(new Color(245, 247, 250));

        // ===== LEFT MENU =====
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(245, 247, 250));

        String[] modules = {
            "Customer Registration",
            "Add Room", "View Rooms", "Update Room Status", "View Staff",
            "Reservation", "Check Out",
            "Add Staff", "Approve Users", "Delete Reservation", "Sign Up", "Forget Password"
        };

        for (String module : modules) {
            JLabel lbl = new JLabel(module);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lbl.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
            lbl.setOpaque(true);
            lbl.setBackground(new Color(245, 247, 250));
            lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));

            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cardLayout.show(contentPanel, module);
                    highlightSelected(lbl, menuPanel);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!lbl.getBackground().equals(new Color(0, 120, 255))) 
                        lbl.setBackground(new Color(220, 235, 255));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!lbl.getBackground().equals(new Color(0, 120, 255)))
                        lbl.setBackground(new Color(245, 247, 250));
                }
            });

            menuPanel.add(lbl);
        }

        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.setBorder(null);
        menuScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        menuScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        splitPane.setLeftComponent(menuScroll);

        // ===== RIGHT CONTENT =====
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(new Color(245, 247, 250));

        for (String module : modules) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(245, 247, 250));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));

            // Add description card
            panel.add(createCard(module, getHelpText(module)));

            // Add screenshot if available
            panel.add(createImage("/Images/" + module.replace(" ", "") + ".png"));

            // Add panel to scrollable container
            JScrollPane scroll = new JScrollPane(panel);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            contentPanel.add(scroll, module);
        }

        splitPane.setRightComponent(contentPanel);
        add(splitPane, BorderLayout.CENTER);

        // ===== FOOTER =====
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(230, 230, 230));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel footerLabel = new JLabel(
                "<html>Support: support@hotelapp.com | +94 7123456 | Developed by: Shidan</html>");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        // ===== Default selected module =====
        cardLayout.show(contentPanel, modules[0]);
        highlightSelected((JLabel) menuPanel.getComponent(0), menuPanel);
    }

    private JPanel createCard(String title, String description) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblDesc = new JLabel("<html>" + description + "</html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cardPanel.add(lblTitle, BorderLayout.NORTH);
        cardPanel.add(lblDesc, BorderLayout.CENTER);

        return cardPanel;
    }

    private JPanel createImage(String path) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(750, -1, Image.SCALE_SMOOTH);
                JLabel lblImg = new JLabel(new ImageIcon(img));
                lblImg.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2, true));
                panel.add(lblImg, BorderLayout.CENTER);
            } else {
                panel.add(new JLabel("Screenshot not found: " + path), BorderLayout.CENTER);
            }
        } catch (Exception e) {
            panel.add(new JLabel("Error loading image: " + path), BorderLayout.CENTER);
        }

        return panel;
    }

    private void highlightSelected(JLabel selected, JPanel menuPanel) {
        for (Component comp : menuPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                lbl.setBackground(new Color(245, 247, 250));
                lbl.setForeground(Color.BLACK);
            }
        }
        selected.setBackground(new Color(0, 120, 255));
        selected.setForeground(Color.WHITE);
    }

    private String getHelpText(String module) {
        switch (module) {
            case "Customer Registration":
                return "<b>Step-by-Step Instructions:</b><br>"
                        + "1. Select ID Type from the drop-down list.<br>"
                        + "2. Enter the exact ID Number.<br>"
                        + "3. Enter full Customer Name.<br>"
                        + "4. Select Gender.<br>"
                        + "5. Enter Country.<br>"
                        + "6. Assign an available Room Number.<br>"
                        + "7. Enter a valid Phone Number.<br>"
                        + "8. Set Check-In and Check-Out dates.<br>"
                        + "9. Click Save to register the customer.<br>"
                        + "10. Click Clear All to reset fields.<br>"
                        + "<i>Do not leave mandatory fields empty or assign occupied rooms.</i>";

            case "Add Room":
                return "<b>Step-by-Step Instructions to Add a Room:</b><br>"
                        + "1. Navigate to the 'Add Room' module from the admin panel.<br>"
                        + "2. Fill in the following fields in the form:<br>"
                        + "&nbsp;&nbsp;- <b>Room Number:</b> Enter a unique number for the room.<br>"
                        + "&nbsp;&nbsp;- <b>Available:</b> Select 'Available' or 'Not Available'.<br>"
                        + "&nbsp;&nbsp;- <b>Check Status:</b> Select 'Clean' or 'Dirty'.<br>"
                        + "&nbsp;&nbsp;- <b>Bed Type:</b> Choose the type of bed from the drop-down menu (Twin, Single, Double, Queen, King, California King).<br>"
                        + "&nbsp;&nbsp;- <b>Description:</b> Enter a short description of the room.<br>"
                        + "&nbsp;&nbsp;- <b>Price:</b> Enter the price per night for the room.<br>"
                        + "3. After filling all fields, click the <b>ADD</b> button to save the room.<br>"
                        + "4. If the room is successfully added, a confirmation message will appear.<br>"
                        + "5. To add another room, clear the fields and repeat the process.<br>"
                        + "<i>Note: Only users with Admin privileges can add new rooms.</i><br>"
                        + "<i>Do not leave any mandatory fields empty, and ensure the room number is unique.</i>";

            case "View Rooms":
                return "<b>Step-by-Step Instructions to View Rooms:</b><br>"
                        + "1. Navigate to the 'Rooms' module from the left menu.<br>"
                        + "2. The table will display all rooms in the system with the following columns:<br>"
                        + "&nbsp;&nbsp;- <b>Room Number:</b> Unique identifier for each room.<br>"
                        + "&nbsp;&nbsp;- <b>Available:</b> Shows whether the room is available or not.<br>"
                        + "&nbsp;&nbsp;- <b>Status:</b> Current room status (Clean or Dirty).<br>"
                        + "&nbsp;&nbsp;- <b>Bed Type:</b> Type of bed in the room (Twin, Single, Double, etc.).<br>"
                        + "&nbsp;&nbsp;- <b>Description:</b> Short description of the room.<br>"
                        + "&nbsp;&nbsp;- <b>Price:</b> Room price per night.<br>"
                        + "3. To filter rooms by bed type, select a type from the 'Room Bed Type' drop-down menu.<br>"
                        + "4. To view only available rooms, check the 'Only Available Rooms' checkbox.<br>"
                        + "5. The table will automatically update based on the selected filters.<br>"
                        + "6. Click on any column header to sort rooms based on that column (e.g., Room Number or Price).<br>"
                        + "7. Use this view to compare room availability, type, and price before making reservations.<br>"
                        + "<i>Do not modify room data directly from this view; use the 'Add Room' or 'Update Room Status' modules for changes.</i>";

            case "Update Room Status":
                return "<b>Step-by-Step Instructions for Update Room:</b><br>"
                        + "1. Navigate to the 'Update Room' module from the left menu.<br>"
                        + "2. Enter the <b>Room Number</b> you want to update in the search field.<br>"
                        + "3. Click the <b>Search</b> button to load room details.<br>"
                        + "4. Verify that the displayed <b>Room Number</b> and <b>Room Type</b> match the room you want to update.<br>"
                        + "5. Modify the following fields if needed:<br>"
                        + "&nbsp;&nbsp;- Room Status (Clean / Dirty)<br>"
                        + "&nbsp;&nbsp;- Description<br>"
                        + "6. Click the <b>Update</b> button to save changes.<br>"
                        + "7. A confirmation message will appear if the update is successful.<br>"
                        + "8. Click the <b>Clear</b> button to reset the form for the next update.<br>"
                        + "<i>Do not update a room without verifying its Room Number and current status.</i>";

            case "Reservation":
                return "<b>Step-by-Step Instructions for Check Reservations:</b><br>"
                        + "1. Navigate to the 'Check Reservations' module from the left panel.<br>"
                        + "2. Review the table displaying all current reservations.<br>"
                        + "3. Use the <b>Search by Date</b> field to filter reservations for a specific date.<br>"
                        + "4. Click the <b>Search</b> button to display reservations matching the selected date.<br>"
                        + "5. If no reservations are found, a message will appear and the table will be cleared.<br>"
                        + "6. To view the bill for a reservation, click the <b>View</b> button in the corresponding row.<br>"
                        + "7. The bill will open in your default PDF viewer or browser if available.<br>"
                        + "8. Click the <b>Clear</b> button to reset the date filter and reload all reservations.<br>";

            case "Check Out":
                return "<b>Step-by-Step Instructions for Check Out:</b><br>"
                        + "1. Navigate to the 'Check Out' module from the left menu.<br>"
                        + "2. Enter the <b>Reservation ID</b> of the guest who is checking out.<br>"
                        + "3. Click the <b>Search</b> button to retrieve reservation details.<br>"
                        + "4. Verify the following information displayed:<br>"
                        + "&nbsp;&nbsp;- Guest Name<br>"
                        + "&nbsp;&nbsp;- Room Number<br>"
                        + "&nbsp;&nbsp;- Check-In Date<br>"
                        + "&nbsp;&nbsp;- Check-Out Date<br>"
                        + "5. After verifying, click the <b>Check Out</b> button.<br>"
                        + "6. Confirm the check-out in the popup dialog.<br>"
                        + "7. Upon successful check-out, a confirmation message will appear.<br>"
                        + "8. Use the <b>Clear</b> button to reset the fields for the next check-out.<br>"
                        + "<i>Do not skip verification of guest details before checking out.</i>";

            case "Add Staff":
                return "<b>Step-by-Step Instructions to Add Staff:</b><br>"
                        + "1. Navigate to the 'Add Staff' module from the admin panel.<br>"
                        + "2. Fill in the following fields in the form:<br>"
                        + "&nbsp;&nbsp;- <b>Name:</b> Enter the full name of the staff member.<br>"
                        + "&nbsp;&nbsp;- <b>Age:</b> Enter the age in numbers.<br>"
                        + "&nbsp;&nbsp;- <b>Gender:</b> Select either 'Male' or 'Female'.<br>"
                        + "&nbsp;&nbsp;- <b>Department:</b> Choose the department from the drop-down menu (Front Office, Housekeeping, F&B, etc.).<br>"
                        + "&nbsp;&nbsp;- <b>Job Role:</b> Enter the staff member's role (e.g., Receptionist, Chef, Cleaner).<br>"
                        + "&nbsp;&nbsp;- <b>Contact:</b> Enter a valid phone number.<br>"
                        + "&nbsp;&nbsp;- <b>Email:</b> Enter a valid email address.<br>"
                        + "3. After filling all fields, click the <b>ADD</b> button to register the staff member.<br>"
                        + "4. If the staff member is successfully added, a confirmation message will appear.<br>"
                        + "5. To add another staff member, clear the fields and repeat the process.<br>"
                        + "<i>Note: Only users with Admin privileges can add staff members.</i><br>"
                        + "<i>Do not leave any mandatory fields empty and ensure the data entered is valid.</i>";

            case "Approve Users":
                return "<b>Step-by-Step Instructions for User Approval:</b><br>"
                        + "1. Navigate to the 'User Approval' module from the admin panel.<br>"
                        + "2. Review the table displaying all registered users.<br>"
                        + "3. Use the <b>Search by Email</b> field to find a specific user if needed.<br>"
                        + "4. Click the <b>Search</b> button to filter the results.<br>"
                        + "5. Select a user by clicking on their row in the table.<br>"
                        + "6. A pop-up dialog will appear asking to <b>Approve</b> or <b>Disapprove</b> the user based on their current status.<br>"
                        + "7. Click <b>Yes</b> in the dialog to confirm the action.<br>"
                        + "8. The table will refresh automatically to show the updated status.<br>"
                        + "9. Use the <b>Clear</b> button to reset the search field and reload all users.<br>"
                        + "<i>Only admin users can approve or disapprove users. Ensure you loged as the correct user.</i>";

            case "View Staff":
                return "<b>Step-by-Step Instructions for Viewing Employees:</b><br>"
                        + "1. Navigate to the 'All Employees' module from the left panel.<br>"
                        + "2. The table will display all employees with columns: <b>Name, Age, Gender, Department, Role, Contact Number, Email</b>.<br>"
                        + "3. To filter employees by department, select the desired department from the <b>Department</b> dropdown at the bottom.<br>"
                        + "4. The table will automatically update to show only employees from the selected department.<br>"
                        + "5. To view all employees again, select <b>All Departments</b> from the dropdown.<br>"
                        + "6. Scroll through the table to review employee details.<br>"
                        + "7. You cannot edit employee details from this view; this module is for viewing and filtering only.<br>"
                        + "<i>Only admin users can access this module to view staff details.</i>";

            case "Delete Reservation":
                return "<b>Step-by-Step Instructions for Delete Reservation:</b><br>"
                        + "1. Navigate to the 'Delete Reservation' module from the admin panel.<br>"
                        + "2. Enter the <b>Reservation ID</b> of the booking you want to delete.<br>"
                        + "3. Click the <b>Search</b> button to load the reservation details.<br>"
                        + "4. Verify that the <b>Guest Name</b>, <b>Room Number</b>, and dates match the reservation you want to delete.<br>"
                        + "5. Click the <b>Delete</b> button to remove the reservation.<br>"
                        + "6. Confirm the deletion in the pop-up dialog.<br>"
                        + "7. A confirmation message will appear if deletion is successful.<br>"
                        + "8. Click the <b>Clear</b> button to reset the form for the next operation.<br>"
                        + "<i>Only admin users can delete reservations. Double-check the reservation details before deleting.</i>";

            case "Sign Up":
                return "<b>Step-by-Step Instructions for User Sign Up:</b><br>"
                        + "1. Open the application and navigate to the 'Create Account' or 'Sign Up' screen.<br>"
                        + "2. Fill in your <b>Full Name</b> in the corresponding field.<br>"
                        + "3. Enter a valid <b>Email Address</b> that will be used for login.<br>"
                        + "4. Create a secure <b>Password</b> and enter it in the password field.<br>"
                        + "5. Select a <b>Security Question</b> from the dropdown list.<br>"
                        + "6. Provide the <b>Answer</b> to your chosen security question for account recovery.<br>"
                        + "7. Optionally, fill in your <b>Address</b> and <b>Phone Number</b>.<br>"
                        + "8. Click the <b>Create Account</b> button to submit your information.<br>"
                        + "9. A confirmation message will appear if your account is successfully created.<br>"
                        + "10. Use the <b>Back to Login</b> button to return to the login screen.<br>"
                        + "<i>Ensure that all required fields are correctly filled. Passwords should be strong and email should be valid for account verification.</i>";

            case "Forget Password":
                return "<b>Step-by-Step Instructions for Reset / Forget Password:</b><br>"
                        + "1. Open the application and navigate to the 'Forget Password'.<br>"
                        + "2. Enter your registered <b>Email Address</b> in the email field.<br>"
                        + "3. Click the <b>Search</b> button to load your security question.<br>"
                        + "4. Verify that your security question appears correctly in the <b>Security Question</b> field.<br>"
                        + "5. Enter the correct <b>Answer</b> to your security question.<br>"
                        + "6. Enter your desired new password in the <b>New Password</b> field.<br>"
                        + "7. Click the <b>Save New Password</b> button to update your account password.<br>"
                        + "8. A confirmation message will appear if your password is successfully updated.<br>"
                        + "9. Use the <b>Back to Login</b> button to return to the login screen and log in with your new password.<br>"
                        + "<i>Ensure that all fields are filled correctly. The email must be registered, and the security answer must match for the password to reset successfully.</i>";

            default:
                return "Help content not available.";
        }
    }

    
}