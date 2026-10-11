package ui;

import dao.ComplaintDAO;
import model.User;
import service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame {
    private JPanel contentArea;
    private final User user = AuthService.getLoggedInUser();
    private final ComplaintDAO dao = new ComplaintDAO();
    private JLabel pageTitle;

    public StudentDashboard() {
        setTitle("Hostel CMS — Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 580));
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
        sidebar.setBackground(UITheme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Logo
        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 18));
        logo.setBackground(new Color(7, 14, 30));
        logo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        JLabel logoText = new JLabel("🏨  Hostel CMS");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logoText.setForeground(Color.WHITE);
        logo.add(logoText);
        sidebar.add(logo);

        // User info
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        userInfo.setBackground(new Color(30, 41, 59));
        userInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        JLabel nameL = new JLabel(user.getFullName());
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameL.setForeground(Color.WHITE);
        JLabel roleL = new JLabel("Room: " + (user.getRoomNumber() != null
                                  ? user.getRoomNumber() : "N/A"));
        roleL.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleL.setForeground(new Color(148, 163, 184));
        namePanel.add(nameL);
        namePanel.add(roleL);
        userInfo.add(avatar);
        userInfo.add(namePanel);
        sidebar.add(userInfo);

        sidebar.add(Box.createVerticalStrut(12));

        String[][] navItems = {
            {"home", "Dashboard"},
            {"new", "New Complaint"},
            {"complaint", "My Complaints"},
        };

        String[] actions = {"home", "new", "list"};

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
        btn.setBackground(UITheme.BG_SIDEBAR);
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
                btn.setBackground(UITheme.BG_SIDEBAR);
                btn.setForeground(new Color(203, 213, 225));
            }
        });
        return btn;
    }

    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BG_MAIN);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 60));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
        pageTitle = new JLabel("  Dashboard");
        pageTitle.setFont(UITheme.FONT_H2);
        pageTitle.setForeground(UITheme.TEXT_PRIMARY);
        JLabel bell = new JLabel(UITheme.icon("bell") + "   ");
        bell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        topBar.add(pageTitle, BorderLayout.WEST);
        topBar.add(bell, BorderLayout.EAST);
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
                pageTitle.setText("  Dashboard");
                contentArea.add(buildHomePanel(), BorderLayout.CENTER);
            }
            case "new" -> {
                pageTitle.setText("  Submit New Complaint");
                contentArea.add(new ComplaintFormPanel(this), BorderLayout.CENTER);
            }
            case "list" -> {
                pageTitle.setText("  My Complaints");
                contentArea.add(new ViewComplaintsPanel(user.getId(), false),
                                BorderLayout.CENTER);
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

        JLabel welcome = new JLabel("Good day, " + user.getFullName() + "! 👋");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcome.setForeground(UITheme.TEXT_PRIMARY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Here's an overview of your complaints");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(welcome);
        panel.add(Box.createVerticalStrut(4));
        panel.add(sub);
        panel.add(Box.createVerticalStrut(24));

        int total    = dao.getComplaintsByStudent(user.getId()).size();
        int pending  = (int) dao.getComplaintsByStudent(user.getId()).stream()
                            .filter(c -> "PENDING".equals(c.getStatus())).count();
        int resolved = (int) dao.getComplaintsByStudent(user.getId()).stream()
                            .filter(c -> "RESOLVED".equals(c.getStatus())).count();
        int inProg   = (int) dao.getComplaintsByStudent(user.getId()).stream()
                            .filter(c -> "IN_PROGRESS".equals(c.getStatus())).count();

        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.add(statCard("📋", "Total", String.valueOf(total),
                              UITheme.PRIMARY, UITheme.PRIMARY_LIGHT));
        statsRow.add(statCard("⏳", "Pending", String.valueOf(pending),
                              UITheme.WARNING, UITheme.WARNING_BG));
        statsRow.add(statCard("🔄", "In Progress", String.valueOf(inProg),
                              UITheme.INFO, UITheme.INFO_BG));
        statsRow.add(statCard("✅", "Resolved", String.valueOf(resolved),
                              UITheme.SUCCESS, UITheme.SUCCESS_BG));
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

        JButton newBtn = UITheme.primaryButton("➕  Submit Complaint");
        newBtn.addActionListener(e -> navigate("new"));
        JButton listBtn = UITheme.outlineButton("📋  View All My Complaints");
        listBtn.addActionListener(e -> navigate("list"));

        actions.add(newBtn);
        actions.add(listBtn);
        panel.add(actions);
        return panel;
    }

    private JPanel statCard(String icon, String label, String value,
                             Color accent, Color bg) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        top.add(ico);
        top.add(lbl);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 28));
        val.setForeground(accent);
        card.add(top, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        return card;
    }

    public void refreshContent() {
        navigate("list");
    }
}