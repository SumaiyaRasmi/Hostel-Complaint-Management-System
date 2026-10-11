package ui;

import service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterFrame extends JFrame {
    private JTextField nameField, emailField, usernameField, roomField, phoneField;
    private JPasswordField passField, confirmPassField;
    private JLabel statusLabel;
    private final AuthService authService = new AuthService();

    public RegisterFrame() {
        setTitle("Hostel CMS — Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(540, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(buildUI());
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                    0, 0, UITheme.BG_SIDEBAR,
                    getWidth(), 0, UITheme.PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(540, 80));
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 24, 20));
        JLabel hTitle = new JLabel("🏨  Create Student Account");
        hTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        hTitle.setForeground(Color.WHITE);
        header.add(hTitle);
        root.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(24, 36, 20, 36));

        nameField      = addField(form, "Full Name");
        emailField     = addField(form, "Email Address");
        usernameField  = addField(form, "Username");
        passField      = addPasswordField(form, "Password");
        confirmPassField = addPasswordField(form, "Confirm Password");
        roomField      = addField(form, "Room Number");
        phoneField     = addField(form, "Phone Number");

        statusLabel = new JLabel(" ");
        statusLabel.setFont(UITheme.FONT_SMALL);
        statusLabel.setForeground(UITheme.DANGER);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(statusLabel);
        form.add(Box.createVerticalStrut(8));

        JButton regBtn = UITheme.primaryButton("Create Account");
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        regBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        regBtn.addActionListener(e -> handleRegister());
        form.add(regBtn);

        form.add(Box.createVerticalStrut(12));
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setBackground(Color.WHITE);
        JLabel backLink = new JLabel("← Back to Login");
        backLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backLink.setForeground(UITheme.PRIMARY);
        backLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        backPanel.add(backLink);
        form.add(backPanel);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        root.add(scroll, BorderLayout.CENTER);
        return root;
    }

    private JTextField addField(JPanel form, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lbl);
        form.add(Box.createVerticalStrut(4));
        JTextField field = UITheme.styledTextField("");
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(field);
        form.add(Box.createVerticalStrut(12));
        return field;
    }

    private JPasswordField addPasswordField(JPanel form, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lbl);
        form.add(Box.createVerticalStrut(4));
        JPasswordField field = UITheme.styledPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(field);
        form.add(Box.createVerticalStrut(12));
        return field;
    }

    private void handleRegister() {
        String name    = nameField.getText().trim();
        String email   = emailField.getText().trim();
        String uname   = usernameField.getText().trim();
        String pass    = new String(passField.getPassword());
        String confirm = new String(confirmPassField.getPassword());
        String room    = roomField.getText().trim();
        String phone   = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || uname.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("⚠  Please fill in all required fields.");
            return;
        }
        if (!pass.equals(confirm)) {
            statusLabel.setText("✗  Passwords do not match.");
            return;
        }
        if (pass.length() < 6) {
            statusLabel.setText("✗  Password must be at least 6 characters.");
            return;
        }
        if (!email.contains("@")) {
            statusLabel.setText("✗  Enter a valid email address.");
            return;
        }

        boolean success = authService.register(name, email, uname, pass, room, phone);
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Account created successfully!\nYou can now log in.",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            statusLabel.setText("✗  Username or email already exists.");
        }
    }
}