package ui;

import dao.ComplaintDAO;
import model.Complaint;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ViewComplaintsPanel extends JPanel {
    private final ComplaintDAO dao = new ComplaintDAO();
    private final int studentId;
    private final boolean isAdmin;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterBox;

    public ViewComplaintsPanel(int studentId, boolean isAdmin) {
        this.studentId = studentId;
        this.isAdmin = isAdmin;
        setBackground(UITheme.BG_MAIN);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buildUI();
        loadData("ALL");
    }

    private void buildUI() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout(12, 0));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JLabel title = new JLabel(isAdmin ? "All Complaints" : "My Complaints");
        title.setFont(UITheme.FONT_H2);
        title.setForeground(UITheme.TEXT_PRIMARY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JLabel filterLbl = new JLabel("Filter by status:");
        filterLbl.setFont(UITheme.FONT_BODY);
        filterLbl.setForeground(UITheme.TEXT_SECONDARY);
        filterBox = new JComboBox<>(
            new String[]{"ALL", "PENDING", "IN_PROGRESS", "RESOLVED", "REJECTED"});
        filterBox.setFont(UITheme.FONT_BODY);
        filterBox.addActionListener(e -> loadData((String) filterBox.getSelectedItem()));
        JButton refreshBtn = UITheme.outlineButton("↻ Refresh");
        refreshBtn.addActionListener(e -> loadData((String) filterBox.getSelectedItem()));
        right.add(filterLbl);
        right.add(filterBox);
        right.add(refreshBtn);

        topBar.add(title, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Table columns
        String[] cols = isAdmin
            ? new String[]{"#", "Student", "Room", "Category",
                           "Title", "Priority", "Status", "Date"}
            : new String[]{"#", "Category", "Title", "Priority",
                           "Status", "Date", "Admin Remarks"};

        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(UITheme.PRIMARY_LIGHT);
        table.setSelectionForeground(UITheme.TEXT_PRIMARY);
        table.getTableHeader().setFont(UITheme.FONT_LABEL);
        table.getTableHeader().setBackground(UITheme.BG_MAIN);
        table.getTableHeader().setForeground(UITheme.TEXT_SECONDARY);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

        // Custom renderer for status colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    t, val, sel, focus, row, col);
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                int statusColIndex = isAdmin ? 6 : 4;
                if (col == statusColIndex && val != null) {
                    String status = val.toString();
                    lbl.setBackground(sel ? UITheme.PRIMARY_LIGHT
                                          : UITheme.getStatusBg(status));
                    lbl.setForeground(UITheme.getStatusColor(status));
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                } else {
                    lbl.setBackground(sel ? UITheme.PRIMARY_LIGHT
                        : (row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252)));
                    lbl.setForeground(UITheme.TEXT_PRIMARY);
                    lbl.setFont(UITheme.FONT_BODY);
                }
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // Admin action buttons
        if (isAdmin) {
            JPanel actBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
            actBar.setOpaque(false);
            JButton updateBtn = UITheme.primaryButton("✏  Update Status");
            updateBtn.addActionListener(e -> handleUpdateStatus());
            JButton deleteBtn = UITheme.dangerButton("🗑  Delete");
            deleteBtn.addActionListener(e -> handleDelete());
            actBar.add(updateBtn);
            actBar.add(deleteBtn);
            add(actBar, BorderLayout.SOUTH);
        }
    }

    public void loadData(String filter) {
        tableModel.setRowCount(0);
        List<Complaint> list;

        if (isAdmin) {
            list = "ALL".equals(filter)
                ? dao.getAllComplaints()
                : dao.getComplaintsByStatus(filter);
        } else {
            list = dao.getComplaintsByStudent(studentId);
            if (!"ALL".equals(filter)) {
                list = list.stream()
                    .filter(c -> filter.equals(c.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
            }
        }

        for (Complaint c : list) {
            String dateStr = c.getCreatedAt() != null
                ? new java.text.SimpleDateFormat("dd MMM yyyy")
                    .format(c.getCreatedAt()) : "-";
            if (isAdmin) {
                tableModel.addRow(new Object[]{
                    "#" + c.getId(), c.getStudentName(),
                    c.getStudentRoom() != null ? c.getStudentRoom() : "-",
                    c.getCategoryName(), c.getTitle(),
                    c.getPriority(), c.getStatus(), dateStr
                });
            } else {
                tableModel.addRow(new Object[]{
                    "#" + c.getId(), c.getCategoryName(), c.getTitle(),
                    c.getPriority(), c.getStatus(), dateStr,
                    c.getAdminRemarks() != null ? c.getAdminRemarks() : "—"
                });
            }
        }
    }

    private void handleUpdateStatus() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a complaint first.", "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String idStr = tableModel.getValueAt(row, 0).toString().replace("#", "");
        int id = Integer.parseInt(idStr);

        String[] statusOpts = {"PENDING", "IN_PROGRESS", "RESOLVED", "REJECTED"};
        String newStatus = (String) JOptionPane.showInputDialog(this,
            "Select new status for complaint #" + id,
            "Update Status", JOptionPane.PLAIN_MESSAGE,
            null, statusOpts, statusOpts[0]);
        if (newStatus == null) return;

        String remarks = JOptionPane.showInputDialog(this,
            "Admin Remarks (optional):", "");
        boolean ok = dao.updateComplaintStatus(id, newStatus, remarks);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Status updated successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
            loadData((String) filterBox.getSelectedItem());
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a complaint first.", "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String idStr = tableModel.getValueAt(row, 0).toString().replace("#", "");
        int id = Integer.parseInt(idStr);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete complaint #" + id + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = dao.deleteComplaint(id);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                    "Complaint deleted.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadData((String) filterBox.getSelectedItem());
            }
        }
    }
}