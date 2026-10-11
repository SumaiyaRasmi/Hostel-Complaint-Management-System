package ui;

import service.AuthService;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private final AuthService authService = new AuthService();

    public LoginFrame() {
        UITheme.setGlobalLook();
        setTitle("Hostel CMS — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(buildUI());
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new GridLayout(1, 2));

        // Left Panel
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(15, 23, 42),
                    getWidth(), getHeight(), new Color(37, 99, 235)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(-60, -60, 260, 260);
                g2.fillOval(getWidth() - 120, getHeight() - 120, 220, 220);
            }
        };
        left.setLayout(new GridBagLayout());

        JPanel brandContent = new JPanel();
        brandContent.setOpaque(false);
        brandContent.setLayout(new BoxLayout(brandContent, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("🏨");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Hostel CMS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Complaint Management System");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(148, 163, 184));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel(
            "<html><center>Manage, track and resolve<br>" +
            "hostel complaints efficiently</center></html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setForeground(new Color(148, 163, 184));
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandContent.add(icon);
        brandContent.add(Box.createVerticalStrut(12));
        brandContent.add(title);
        brandContent.add(Box.createVerticalStrut(6));
        brandContent.add(sub);
        brandContent.add(Box.createVerticalStrut(16));
        brandContent.add(desc);
        left.add(brandContent);

        // Right Panel
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Color.WHITE);

        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel loginTitle = new JLabel("Welcome back");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        loginTitle.setForeground(UITheme.TEXT_PRIMARY);
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel loginSub = new JLabel("Sign in to your account");
        loginSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loginSub.setForeground(UITheme.TEXT_SECONDARY);
        loginSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(UITheme.FONT_LABEL);
        userLabel.setForeground(UITheme.TEXT_SECONDARY);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = UITheme.styledTextField("");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UITheme.FONT_LABEL);
        passLabel.setForeground(UITheme.TEXT_SECONDARY);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = UITheme.styledPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(UITheme.FONT_SMALL);
        statusLabel.setForeground(UITheme.DANGER);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginBtn = UITheme.primaryButton("Sign In →");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> handleLogin());

        JPanel regPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        regPanel.setBackground(Color.WHITE);
        regPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel regText = new JLabel("Don't have an account?");
        regText.setFont(UITheme.FONT_BODY);
        regText.setForeground(UITheme.TEXT_SECONDARY);
        JLabel regLink = new JLabel("Register here");
        regLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        regLink.setForeground(UITheme.PRIMARY);
        regLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        regLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new RegisterFrame().setVisible(true);
                dispose();
            }
        });
        regPanel.add(regText);
        regPanel.add(regLink);

        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin();
            }
        });

        form.add(loginTitle);
        form.add(Box.createVerticalStrut(4));
        form.add(loginSub);
        form.add(Box.createVerticalStrut(28));
        form.add(userLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(usernameField);
        form.add(Box.createVerticalStrut(16));
        form.add(passLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(passwordField);
        form.add(Box.createVerticalStrut(8));
        form.add(statusLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(loginBtn);
        form.add(Box.createVerticalStrut(16));
        form.add(regPanel);

        right.add(form);
        root.add(left);
        root.add(right);
        return root;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠  Please fill in all fields.");
            return;
        }

        User user = authService.login(username, password);
        if (user != null) {
            statusLabel.setText("");
            if ("ADMIN".equals(user.getRole())) {
                new AdminDashboard().setVisible(true);
            } else {
                new StudentDashboard().setVisible(true);
            }
            dispose();
        } else {
            statusLabel.setText("✗  Invalid username or password.");
            passwordField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}