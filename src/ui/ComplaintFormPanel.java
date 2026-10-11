package ui;

import dao.ComplaintDAO;
import model.Complaint;
import service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ComplaintFormPanel extends JPanel {
    private JTextField titleField;
    private JTextArea descField;
    private JComboBox<String> categoryBox, priorityBox;
    private final ComplaintDAO dao = new ComplaintDAO();
    private final JFrame parent;
    private String[] categoryIds;

    public ComplaintFormPanel(JFrame parent) {
        this.parent = parent;
        setBackground(UITheme.BG_MAIN);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        buildUI();
    }

    private void buildUI() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(28, 32, 28, 32)
        ));

        JLabel head = new JLabel("Submit a New Complaint");
        head.setFont(UITheme.FONT_TITLE);
        head.setForeground(UITheme.TEXT_PRIMARY);
        head.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(
            "Describe your issue clearly so it can be resolved quickly");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(head);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(2));

        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));

        // Category
        List<String[]> cats = dao.getCategories();
        categoryIds = new String[cats.size()];
        String[] categoryNames = new String[cats.size()];
        for (int i = 0; i < cats.size(); i++) {
            categoryIds[i] = cats.get(i)[0];
            categoryNames[i] = cats.get(i)[1];
        }
        categoryBox = new JComboBox<>(categoryNames);
        categoryBox.setFont(UITheme.FONT_INPUT);
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(formRow("Category", categoryBox));
        card.add(Box.createVerticalStrut(14));

        // Title
        titleField = UITheme.styledTextField("");
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(formRow("Complaint Title", titleField));
        card.add(Box.createVerticalStrut(14));

        // Priority
        priorityBox = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH"});
        priorityBox.setSelectedIndex(1);
        priorityBox.setFont(UITheme.FONT_INPUT);
        priorityBox.setBackground(Color.WHITE);
        priorityBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(formRow("Priority Level", priorityBox));
        card.add(Box.createVerticalStrut(14));

        // Description
        descField = UITheme.styledTextArea(5, 30);
        JScrollPane descScroll = new JScrollPane(descField);
        descScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1, true));
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLbl = new JLabel("Description *");
        descLbl.setFont(UITheme.FONT_LABEL);
        descLbl.setForeground(UITheme.TEXT_SECONDARY);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(descScroll);
        card.add(Box.createVerticalStrut(22));

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton submitBtn = UITheme.primaryButton("Submit Complaint");
        submitBtn.addActionListener(e -> handleSubmit());

        JButton clearBtn = UITheme.outlineButton("Clear Form");
        clearBtn.addActionListener(e -> {
            titleField.setText("");
            descField.setText("");
            categoryBox.setSelectedIndex(0);
            priorityBox.setSelectedIndex(1);
        });

        btnRow.add(submitBtn);
        btnRow.add(clearBtn);
        card.add(btnRow);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(card, gbc);
    }

    private JPanel formRow(String label, JComponent comp) {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label + " *");
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(lbl);
        row.add(Box.createVerticalStrut(5));
        row.add(comp);
        return row;
    }

    private void handleSubmit() {
        String title = titleField.getText().trim();
        String desc  = descField.getText().trim();

        if (title.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                "Please fill in all required fields.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int catId = Integer.parseInt(categoryIds[categoryBox.getSelectedIndex()]);
        String priority = (String) priorityBox.getSelectedItem();
        int studentId = AuthService.getLoggedInUser().getId();

        Complaint c = new Complaint(studentId, catId, title, desc, priority);
        boolean success = dao.addComplaint(c);

        if (success) {
            JOptionPane.showMessageDialog(parent,
                "Your complaint has been submitted successfully!\n" +
                "Our team will review it shortly.",
                "Success ✓", JOptionPane.INFORMATION_MESSAGE);
            titleField.setText("");
            descField.setText("");
        } else {
            JOptionPane.showMessageDialog(parent,
                "Failed to submit complaint. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}