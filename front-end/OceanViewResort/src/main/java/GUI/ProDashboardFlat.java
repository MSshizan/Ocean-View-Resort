/*package GUI;

import Api.AuthContext;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProDashboardFlat extends JFrame {

    private JPanel mainContent;
    private String userRole;

    public ProDashboardFlat(String userRole) {
        this.userRole = userRole;

        // ===== FlatLaf Look & Feel =====
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===== WINDOW SETUP =====
        setTitle("Ocean View Resort - Dashboard");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // handle close manually
        setLayout(new BorderLayout());
        setResizable(true);

        // ===== WINDOW CLOSE CONFIRMATION =====
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(32, 44, 66));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, getHeight()));

        JLabel appLabel = new JLabel("  OCEAN VIEW");
        appLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appLabel.setForeground(Color.WHITE);
        appLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        appLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(appLabel);

        Font btnFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color btnColor = new Color(40, 60, 90);
        Color accentColor = new Color(0, 150, 255);

        // ===== SIDEBAR BUTTONS =====
        JButton btnDashboard = createSidebarButton("Dashboard", btnFont, btnColor, accentColor);
        JButton btnCustomerReg = createSidebarButton("Customer Registration", btnFont, btnColor, accentColor);
        JButton btnRooms = createSidebarButton("Rooms", btnFont, btnColor, accentColor);
        JButton btnEmployees = createSidebarButton("All Employees", btnFont, btnColor, accentColor);
        JButton btnReservation = createSidebarButton("Reservation", btnFont, btnColor, accentColor);
        JButton btnUpdateStatus = createSidebarButton("Update Room Status", btnFont, btnColor, accentColor);
        JButton btnCheckOut = createSidebarButton("Check Out", btnFont, btnColor, accentColor);

        JButton btnAddRoom = createSidebarButton("Add Room", btnFont, btnColor, accentColor);
        JButton btnAddStaff = createSidebarButton("Add Staff", btnFont, btnColor, accentColor);
        JButton btnUserApproval = createSidebarButton("User Approval", btnFont, btnColor, accentColor);
        JButton btnDeleteReservation = createSidebarButton("Delete Reservation", btnFont, btnColor, accentColor);

        JButton btnHelp = createSidebarButton("Help", btnFont, btnColor, accentColor);
        JButton btnLogout = createSidebarButton("Logout", btnFont, btnColor, accentColor);

        // ---- add buttons conditionally by role ----
        sidebar.add(btnDashboard);
        sidebar.add(btnCustomerReg);
        sidebar.add(btnRooms);
        sidebar.add(btnEmployees);
        sidebar.add(btnReservation);
        sidebar.add(btnUpdateStatus);
        sidebar.add(btnCheckOut);
        

        if ("ROLE_ADMIN".equalsIgnoreCase(userRole)) {
            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(btnAddRoom);
            sidebar.add(btnAddStaff);
            sidebar.add(btnUserApproval);
            sidebar.add(btnDeleteReservation);
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnHelp);
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(20));

        add(sidebar, BorderLayout.WEST);

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(20, 30, 50));
        topBar.setPreferredSize(new Dimension(getWidth(), 60));
        JLabel titleLabel = new JLabel("  Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topBar.add(titleLabel, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // ===== MAIN CONTENT =====
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(245, 245, 245));
        showDashboard();
        add(mainContent, BorderLayout.CENTER);

        // ===== BUTTON ACTIONS =====
        btnDashboard.addActionListener(e -> showDashboard());
      
        
        btnDeleteReservation.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new EmbeddedDeleteReservationProAdvanced(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });
        

        btnCheckOut.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new EmbeddedCheckOutProAdvanced(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        btnUpdateStatus.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new EmbeddedUpdateRoomProAdvanced(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        btnReservation.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new EmbeddedReservationsProAdvanced(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        btnEmployees.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new EmbeddedEmployeesProAdvanced(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        btnRooms.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new EmbeddedRoomsProAdvanced(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        btnCustomerReg.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new EmbeddedCustomerRegProAdvanced(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        btnAddRoom.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new EmbeddedAddRoomProAdvanced(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        btnAddStaff.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new EmbeddedAddStaffProAdvanced(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        btnUserApproval.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new EmbeddedUserApprovalProAdvanced(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        btnHelp.addActionListener(e -> showPage("Help Page / Contact Support"));

        btnLogout.addActionListener(e -> logout());

        setVisible(true);
    }

    private JButton createSidebarButton(String text, Font font, Color bg, Color accent) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setFont(font);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return btn;
    }

    private void showDashboard() {
        mainContent.removeAll();
        JLabel welcome = new JLabel("Welcome to Ocean View Resort Dashboard", SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcome.setForeground(new Color(0, 150, 255));
        mainContent.add(welcome, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showPage(String pageName) {
        mainContent.removeAll();
        JLabel pageLabel = new JLabel(pageName, SwingConstants.CENTER);
        pageLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        pageLabel.setForeground(new Color(50, 50, 50));
        mainContent.add(pageLabel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            AuthContext.getInstance().clear();
            for (Frame frame : Frame.getFrames()) {
                frame.dispose();
            }
            new newlogin().setVisible(true);
        }
    }

    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
*/


package GUI;

import Api.AuthContext;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProDashboardFlat extends JFrame {

    private JPanel mainContent;
    private String userRole;
    private Color accentColor = new Color(0, 150, 255); // professional blue

    public ProDashboardFlat(String userRole) {
        this.userRole = userRole;

        // ===== FlatLaf Look & Feel =====
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===== WINDOW SETUP =====
        setTitle("Ocean View Resort - Dashboard");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        // ===== SIDEBAR =====
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(20, 30, 50));
        topBar.setPreferredSize(new Dimension(getWidth(), 60));
        JLabel titleLabel = new JLabel("  Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topBar.add(titleLabel, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // ===== MAIN CONTENT =====
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(245, 245, 245));
        showDashboard();
        add(mainContent, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(32, 44, 66));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, getHeight()));

        JLabel appLabel = new JLabel("  OCEAN VIEW");
        appLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appLabel.setForeground(Color.WHITE);
        appLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        appLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(appLabel);

        Font btnFont = new Font("Segoe UI", Font.PLAIN, 16);

        // Sidebar buttons
        JButton btnDashboard = createSidebarButton("Dashboard", btnFont);
        JButton btnCustomerReg = createSidebarButton("Customer Registration", btnFont);
        JButton btnRooms = createSidebarButton("Rooms", btnFont);
        JButton btnEmployees = createSidebarButton("All Employees", btnFont);
        JButton btnReservation = createSidebarButton("Reservation", btnFont);
        JButton btnUpdateStatus = createSidebarButton("Update Room Status", btnFont);
        JButton btnCheckOut = createSidebarButton("Check Out", btnFont);

        JButton btnAddRoom = createSidebarButton("Add Room", btnFont);
        JButton btnAddStaff = createSidebarButton("Add Staff", btnFont);
        JButton btnUserApproval = createSidebarButton("User Approval", btnFont);
        JButton btnDeleteReservation = createSidebarButton("Delete Reservation", btnFont);

        JButton btnHelp = createSidebarButton("Help", btnFont);
        JButton btnLogout = createSidebarButton("Logout", btnFont);

        // ---- add buttons conditionally by role ----
        sidebar.add(btnDashboard);
        sidebar.add(btnCustomerReg);
        sidebar.add(btnRooms);
        sidebar.add(btnEmployees);
        sidebar.add(btnReservation);
        sidebar.add(btnUpdateStatus);
        sidebar.add(btnCheckOut);

        if ("ROLE_ADMIN".equalsIgnoreCase(userRole)) {
            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(btnAddRoom);
            sidebar.add(btnAddStaff);
            sidebar.add(btnUserApproval);
            sidebar.add(btnDeleteReservation);
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnHelp);
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(20));

        // ===== Button Actions =====
        btnDashboard.addActionListener(e -> showDashboard());
        btnCustomerReg.addActionListener(e -> loadPage(new EmbeddedCustomerRegProAdvanced()));
        btnRooms.addActionListener(e -> loadPage(new EmbeddedRoomsProAdvanced()));
        btnEmployees.addActionListener(e -> loadPage(new EmbeddedEmployeesProAdvanced()));
        btnReservation.addActionListener(e -> loadPage(new EmbeddedReservationsProAdvanced()));
        btnUpdateStatus.addActionListener(e -> loadPage(new EmbeddedUpdateRoomProAdvanced()));
        btnCheckOut.addActionListener(e -> loadPage(new EmbeddedCheckOutProAdvanced()));
        btnAddRoom.addActionListener(e -> loadPage(new EmbeddedAddRoomProAdvanced()));
        btnAddStaff.addActionListener(e -> loadPage(new EmbeddedAddStaffProAdvanced()));
        btnUserApproval.addActionListener(e -> loadPage(new EmbeddedUserApprovalProAdvanced()));
        btnDeleteReservation.addActionListener(e -> loadPage(new EmbeddedDeleteReservationProAdvanced()));
        btnHelp.addActionListener(e -> loadPage(new HelpPanelUnifiedPro()));
        btnLogout.addActionListener(e -> logout());

        return sidebar;
    }

    private JButton createSidebarButton(String text, Font font) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setFont(font);
        btn.setBackground(new Color(40, 60, 90));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // ===== Hover effect =====
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(accentColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(40, 60, 90));
            }
        });

        return btn;
    }

    private void loadPage(JPanel panel) {
        mainContent.removeAll();
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }
    
    
    
private void showDashboard() {
    mainContent.removeAll();
    mainContent.setLayout(new BorderLayout());
    mainContent.setBackground(new Color(10, 20, 35));

    // ===== Hero Panel =====
    JPanel heroPanel = new JPanel() {
        private float hue = 0f;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Animated gradient
            int w = getWidth();
            int h = getHeight();
            Color c1 = new Color(0, 180, 255);
            Color c2 = new Color(0, 100, 200);
            GradientPaint gp = new GradientPaint(0, 0, c1, w, h, c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
        }
    };
    heroPanel.setLayout(null);
    heroPanel.setPreferredSize(new Dimension(mainContent.getWidth(), 400));

    // ===== Welcome Label =====
    JLabel welcome = new JLabel("Welcome to Ocean View Resort", SwingConstants.CENTER);
    welcome.setFont(new Font("Segoe UI Semibold", Font.BOLD, 56));
    welcome.setForeground(new Color(255, 255, 255, 0));
    welcome.setBounds(0, 120, 800, 60); // center roughly
    heroPanel.add(welcome);

    // ===== Fade-in + Glow =====
    Timer fadeGlow = new Timer(40, e -> {
        Color c = welcome.getForeground();
        int alpha = Math.min(c.getAlpha() + 5, 255);
        int glow = 200 + (int)(55 * Math.sin(System.currentTimeMillis() * 0.005));
        welcome.setForeground(new Color(255, 255, 255, glow));
    });
    fadeGlow.start();

    // ===== Floating Waves =====
    JPanel waves = new JPanel() {
        private int offset = 0;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 25));

            int w = getWidth();
            int h = getHeight();
            int amp = 15;

            for (int x = 0; x < w; x += 2) {
                int y = (int)(amp * Math.sin((x + offset) * 0.05) + h/2 + 30);
                g2.drawLine(x, y, x, y);
            }
            offset += 2;
        }
    };
    waves.setOpaque(false);
    waves.setBounds(0, 0, 800, 400);
    heroPanel.add(waves);
    new Timer(50, e -> waves.repaint()).start();

    // ===== Sparkling Particles =====
    JPanel particles = new JPanel() {
        private java.util.List<Point> points = new java.util.ArrayList<>();
        {
            for (int i = 0; i < 100; i++) points.add(new Point((int)(Math.random()*800), (int)(Math.random()*400)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(255, 255, 255, 120));
            for (Point p : points) {
                g2.fillOval(p.x, p.y, 3, 3);
                p.y += 1;
                if (p.y > getHeight()) p.y = 0;
            }
        }
    };
    particles.setOpaque(false);
    particles.setBounds(0, 0, 800, 400);
    heroPanel.add(particles);
    new Timer(40, e -> particles.repaint()).start();

    // ===== Floating Bubbles (parallax) =====
    JPanel bubbles = new JPanel() {
        private java.util.List<Point> points = new java.util.ArrayList<>();
        {
            for (int i = 0; i < 30; i++) points.add(new Point((int)(Math.random()*800), (int)(Math.random()*400)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(200, 255, 255, 80));
            for (Point p : points) {
                g2.fillOval(p.x, p.y, 8, 8);
                p.y -= 1; // bubbles go up
                if (p.y < 0) p.y = getHeight();
            }
        }
    };
    bubbles.setOpaque(false);
    bubbles.setBounds(0, 0, 800, 400);
    heroPanel.add(bubbles);
    new Timer(60, e -> bubbles.repaint()).start();

    mainContent.add(heroPanel, BorderLayout.CENTER);
    mainContent.revalidate();
    mainContent.repaint();
}



    private void showPage(String pageName) {
        mainContent.removeAll();
        JLabel pageLabel = new JLabel(pageName, SwingConstants.CENTER);
        pageLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        pageLabel.setForeground(new Color(50, 50, 50));
        mainContent.add(pageLabel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            AuthContext.getInstance().clear();
            for (Frame frame : Frame.getFrames()) {
                frame.dispose();
            }
            new newlogin().setVisible(true);
        }
    }

    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}

