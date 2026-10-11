package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class UITheme {
    public static final Color PRIMARY        = new Color(37, 99, 235);
    public static final Color PRIMARY_DARK   = new Color(29, 78, 216);
    public static final Color PRIMARY_LIGHT  = new Color(219, 234, 254);
    public static final Color SECONDARY      = new Color(99, 102, 241);
    public static final Color BG_MAIN        = new Color(241, 245, 249);
    public static final Color BG_SIDEBAR     = new Color(15, 23, 42);
    public static final Color BG_CARD        = Color.WHITE;
    public static final Color TEXT_PRIMARY   = new Color(15, 23, 42);
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    public static final Color TEXT_LIGHT     = new Color(248, 250, 252);
    public static final Color SUCCESS        = new Color(34, 197, 94);
    public static final Color SUCCESS_BG     = new Color(220, 252, 231);
    public static final Color WARNING        = new Color(245, 158, 11);
    public static final Color WARNING_BG     = new Color(254, 243, 199);
    public static final Color DANGER         = new Color(239, 68, 68);
    public static final Color DANGER_BG      = new Color(254, 226, 226);
    public static final Color INFO           = new Color(59, 130, 246);
    public static final Color INFO_BG        = new Color(219, 234, 254);
    public static final Color BORDER         = new Color(226, 232, 240);

    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_H2     = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_H3     = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_LABEL  = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_INPUT  = new Font("Segoe UI", Font.PLAIN, 13);

    public static Color getStatusColor(String status) {
        return switch (status) {
            case "PENDING"     -> WARNING;
            case "IN_PROGRESS" -> INFO;
            case "RESOLVED"    -> SUCCESS;
            case "REJECTED"    -> DANGER;
            default            -> TEXT_SECONDARY;
        };
    }

    public static Color getStatusBg(String status) {
        return switch (status) {
            case "PENDING"     -> WARNING_BG;
            case "IN_PROGRESS" -> INFO_BG;
            case "RESOLVED"    -> SUCCESS_BG;
            case "REJECTED"    -> DANGER_BG;
            default            -> BG_MAIN;
        };
    }

    public static Color getPriorityColor(String priority) {
        return switch (priority) {
            case "HIGH"   -> DANGER;
            case "MEDIUM" -> WARNING;
            case "LOW"    -> SUCCESS;
            default       -> TEXT_SECONDARY;
        };
    }

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_H3);
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setOpaque(true);
        addHoverEffect(btn, PRIMARY, PRIMARY_DARK, Color.WHITE, Color.WHITE);
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_H3);
        btn.setBackground(DANGER);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setOpaque(true);
        return btn;
    }

    public static JButton successButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_H3);
        btn.setBackground(SUCCESS);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setOpaque(true);
        return btn;
    }

    public static JButton outlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY);
        btn.setBackground(BG_CARD);
        btn.setForeground(PRIMARY);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY, 1, true),
            BorderFactory.createEmptyBorder(7, 14, 7, 14)
        ));
        return btn;
    }

    public static JTextField styledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(FONT_INPUT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_PRIMARY);
        return field;
    }

    public static JPasswordField styledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(FONT_INPUT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }

    public static JTextArea styledTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setFont(FONT_INPUT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        area.setBackground(Color.WHITE);
        area.setForeground(TEXT_PRIMARY);
        return area;
    }

    public static JPanel card(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        panel.setLayout(new BorderLayout(0, 10));
        if (title != null && !title.isEmpty()) {
            JLabel lbl = new JLabel(title);
            lbl.setFont(FONT_H2);
            lbl.setForeground(TEXT_PRIMARY);
            panel.add(lbl, BorderLayout.NORTH);
        }
        return panel;
    }

    public static JLabel statusBadge(String status) {
        JLabel lbl = new JLabel(" " + status + " ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(getStatusColor(status));
        lbl.setBackground(getStatusBg(status));
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        return lbl;
    }

    public static String icon(String type) {
        return switch (type) {
            case "home"       -> "🏠";
            case "complaint"  -> "📋";
            case "new"        -> "➕";
            case "users"      -> "👥";
            case "logout"     -> "🚪";
            case "admin"      -> "🛡";
            case "pending"    -> "⏳";
            case "resolved"   -> "✅";
            case "rejected"   -> "❌";
            case "inprogress" -> "🔄";
            case "bell"       -> "🔔";
            default           -> "•";
        };
    }

    private static void addHoverEffect(JButton btn, Color normal, Color hover,
                                        Color fgNormal, Color fgHover) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
                btn.setForeground(fgHover);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(normal);
                btn.setForeground(fgNormal);
            }
        });
    }

    public static void setGlobalLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Panel.background",     BG_MAIN);
            UIManager.put("Label.font",           FONT_BODY);
            UIManager.put("Button.font",          FONT_BODY);
            UIManager.put("TextField.font",       FONT_INPUT);
            UIManager.put("ComboBox.font",        FONT_INPUT);
            UIManager.put("Table.font",           FONT_BODY);
            UIManager.put("TableHeader.font",     FONT_LABEL);
            UIManager.put("OptionPane.messageFont", FONT_BODY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}