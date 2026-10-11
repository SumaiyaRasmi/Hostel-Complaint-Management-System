package ui;

import dao.ComplaintDAO;
import dao.UserDAO;
import model.User;
import service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    private JPanel contentArea;
    private JLabel pageTitle;
    private final ComplaintDAO dao = new ComplaintDAO();
    private final UserDAO userDAO = new UserDAO();

    public AdminDashboard() {
        setTitle("Hostel CMS — Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(960, 600));
        setContentPane(buildUI());
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMain(), BorderLayout.CENTER);
        return root;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(7, 14, 30));
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 16));
        logo.setBackground(new Color(4, 8, 20));
        logo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel logoText = new JLabel("🛡  Admin Panel");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoText.setForeground(Color.WHITE);
        logo.add(logoText);
        sidebar.add(logo);

        JPanel adminInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        adminInfo.setBackground(new Color(15, 23, 42));
        adminInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        JPanel nPanel = new JPanel();
        nPanel.setOpaque(false);
        nPanel.setLayout(new BoxLayout(nPanel, BoxLayout.Y_AXIS));
        User u = AuthService.getLoggedInUser();
        JLabel nameL = new JLabel(u != null ? u.getFullName() : "Admin");
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameL.setForeground(Color.WHITE);
        JLabel roleL = new JLabel("Administrator");
        roleL.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleL.setForeground(new Color(99, 102, 241));
        nPanel.add(nameL);
        nPanel.add(roleL);
        adminInfo.add(avatar);
        adminInfo.add(nPanel);
        sidebar.add(adminInfo);

        sidebar.add(Box.createVerticalStrut(8));

        String[][] navItems = {
            {"home", "Dashboard"},
            {"complaint", "All Complaints"},
            {"pending", "Pending"},
            {"inprogress", "In Progress"},
            {"resolved", "Resolved"},
            {"users", "Students"},
        };

        String[] actions = {"home", "all", "pending",
                            "inprogress", "resolved", "users"};

        for (int i = 0; i < navItems.length; i++) {
            JButton btn = sidebarBtn(navItems[i][0], navItems[i][1]);
            final String action = actions[i];
            btn.addActionListener(e -> navigate(action));
            sidebar.add(btn);
        }

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = sidebarBtn("logout", "Logout");
        logoutBtn.addActionListener(e -> {
            AuthService.logout();
            new LoginFrame().setVisible(true);
            dispose();
        });
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(16));
        return sidebar;
    }

    private JButton sidebarBtn(String iconType, String label) {
        JButton btn = new JButton(UITheme.icon(iconType) + "  " + label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(new Color(7, 14, 30));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(37, 99, 235));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(7, 14, 30));
                btn.setForeground(new Color(203, 213, 225));
            }
        });
        return btn;
    }

    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BG_MAIN);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 60));
        topBar.setBorder(BorderFactory.createMatteBorder(
            0, 0, 1, 0, UITheme.BORDER));
        pageTitle = new JLabel("  Admin Dashboard");
        pageTitle.setFont(UITheme.FONT_H2);
        pageTitle.setForeground(UITheme.TEXT_PRIMARY);
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 16));
        topRight.setBackground(Color.WHITE);
        JLabel dateL = new JLabel(
            new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy")
                .format(new java.util.Date()));
        dateL.setFont(UITheme.FONT_BODY);
        dateL.setForeground(UITheme.TEXT_SECONDARY);
        topRight.add(dateL);
        topBar.add(pageTitle, BorderLayout.WEST);
        topBar.add(topRight, BorderLayout.EAST);
        main.add(topBar, BorderLayout.NORTH);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UITheme.BG_MAIN);
        main.add(contentArea, BorderLayout.CENTER);

        navigate("home");
        return main;
    }

    private void navigate(String page) {
        contentArea.removeAll();
        switch (page) {
            case "home" -> {
                pageTitle.setText("  Admin Dashboard");
                contentArea.add(buildHomePanel(), BorderLayout.CENTER);
            }
            case "all" -> {
                pageTitle.setText("  All Complaints");
                contentArea.add(new ViewComplaintsPanel(0, true),
                                BorderLayout.CENTER);
            }
            case "pending" -> {
                pageTitle.setText("  Pending Complaints");
                ViewComplaintsPanel p = new ViewComplaintsPanel(0, true);
                p.loadData("PENDING");
                contentArea.add(p, BorderLayout.CENTER);
            }
            case "inprogress" -> {
                pageTitle.setText("  In-Progress Complaints");
                ViewComplaintsPanel p = new ViewComplaintsPanel(0, true);
                p.loadData("IN_PROGRESS");
                contentArea.add(p, BorderLayout.CENTER);
            }
            case "resolved" -> {
                pageTitle.setText("  Resolved Complaints");
                ViewComplaintsPanel p = new ViewComplaintsPanel(0, true);
                p.loadData("RESOLVED");
                contentArea.add(p, BorderLayout.CENTER);
            }
            case "users" -> {
                pageTitle.setText("  Students");
                contentArea.add(buildUsersPanel(), BorderLayout.CENTER);
            }
        }
        contentArea.revalidate();
        contentArea.repaint();
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UITheme.BG_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Admin Overview 🛡");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcome.setForeground(UITheme.TEXT_PRIMARY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("System-wide statistics and quick actions");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(welcome);
        panel.add(Box.createVerticalStrut(4));
        panel.add(sub);
        panel.add(Box.createVerticalStrut(24));

        int total    = dao.totalComplaints();
        int pending  = dao.countByStatus("PENDING");
        int inProg   = dao.countByStatus("IN_PROGRESS");
        int resolved = dao.countByStatus("RESOLVED");
        int students = userDAO.getAllStudents().size();

        JPanel statsRow = new JPanel(new GridLayout(1, 5, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.add(adminStatCard("📋", "Total",    String.valueOf(total),    UITheme.PRIMARY));
        statsRow.add(adminStatCard("⏳", "Pending",  String.valueOf(pending),  UITheme.WARNING));
        statsRow.add(adminStatCard("🔄", "In Progress", String.valueOf(inProg), UITheme.INFO));
        statsRow.add(adminStatCard("✅", "Resolved", String.valueOf(resolved), UITheme.SUCCESS));
        statsRow.add(adminStatCard("👥", "Students", String.valueOf(students), UITheme.SECONDARY));
        panel.add(statsRow);
        panel.add(Box.createVerticalStrut(28));

        JLabel actTitle = new JLabel("Quick Actions");
        actTitle.setFont(UITheme.FONT_H2);
        actTitle.setForeground(UITheme.TEXT_PRIMARY);
        actTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(actTitle);
        panel.add(Box.createVerticalStrut(12));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btn1 = UITheme.primaryButton("📋  All Complaints");
        btn1.addActionListener(e -> navigate("all"));
        JButton btn2 = UITheme.outlineButton("⏳  Pending");
        btn2.addActionListener(e -> navigate("pending"));
        JButton btn3 = UITheme.successButton("✅  Resolved");
        btn3.addActionListener(e -> navigate("resolved"));
        JButton btn4 = UITheme.outlineButton("👥  Students");
        btn4.addActionListener(e -> navigate("users"));

        actions.add(btn1);
        actions.add(btn2);
        actions.add(btn3);
        actions.add(btn4);
        panel.add(actions);
        return panel;
    }

    private JPanel adminStatCard(String ico, String label,
                                  String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        top.setOpaque(false);
        JLabel icon = new JLabel(ico);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        top.add(icon);
        top.add(lbl);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 28));
        val.setForeground(accent);
        card.add(top, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(UITheme.BG_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Registered Students");
        title.setFont(UITheme.FONT_H2);
        title.setForeground(UITheme.TEXT_PRIMARY);
        panel.add(title, BorderLayout.NORTH);

        Object[][] rows = userDAO.getAllStudents().stream().map(u -> new Object[]{
            "#" + u.getId(), u.getFullName(), u.getUsername(),
            u.getEmail(),
            u.getRoomNumber() != null ? u.getRoomNumber() : "-",
            u.getPhone() != null ? u.getPhone() : "-",
            u.getCreatedAt() != null
                ? new java.text.SimpleDateFormat("dd MMM yyyy")
                    .format(u.getCreatedAt()) : "-"
        }).toArray(Object[][]::new);

        String[] cols = {"#", "Full Name", "Username",
                         "Email", "Room", "Phone", "Joined"};

        JTable usersTable = new JTable(
            new javax.swing.table.DefaultTableModel(rows, cols) {
                public boolean isCellEditable(int r, int c) { return false; }
            });
        usersTable.setFont(UITheme.FONT_BODY);
        usersTable.setRowHeight(36);
        usersTable.setShowGrid(false);
        usersTable.setBackground(Color.WHITE);
        usersTable.setSelectionBackground(UITheme.PRIMARY_LIGHT);
        usersTable.getTableHeader().setFont(UITheme.FONT_LABEL);
        usersTable.getTableHeader().setBackground(UITheme.BG_MAIN);
        usersTable.getTableHeader().setForeground(UITheme.TEXT_SECONDARY);
        usersTable.getTableHeader().setPreferredSize(new Dimension(0, 36));

        JScrollPane scroll = new JScrollPane(usersTable);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
}