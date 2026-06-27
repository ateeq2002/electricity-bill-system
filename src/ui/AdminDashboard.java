package ui;

import model.Admin;
import service.*;
import dao.AdminDAO;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final Admin admin;
    private final ConsumerService consumerService = new ConsumerService();
    private final BillService     billService     = new BillService();
    private final PaymentService  paymentService  = new PaymentService();

    private JPanel contentArea;
    private CardLayout cardLayout;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        setTitle("Admin Dashboard — " + admin.getUsername());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);
        buildUI();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // LAYOUT
    // ═══════════════════════════════════════════════════════════════════════════
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());

        root.add(buildSidebar(), BorderLayout.WEST);

        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(AppTheme.BG);
        contentArea.add(buildHomePanel(),     "home");
        contentArea.add(buildConsumersPanel(),"consumers");
        contentArea.add(buildBillsPanel(),    "bills");
        contentArea.add(buildPaymentsPanel(), "payments");

        root.add(contentArea, BorderLayout.CENTER);
        cardLayout.show(contentArea, "home");
        add(root);
    }

    private JPanel buildSidebar() {
        JPanel sb = new JPanel();
        sb.setBackground(AppTheme.PRIMARY_DARK);
        sb.setPreferredSize(new Dimension(200, 0));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel logo = new JLabel("⚡ EBMS", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        sb.add(logo);

        addSidebarBtn(sb, "🏠  Dashboard",  "home");
        addSidebarBtn(sb, "👥  Consumers",  "consumers");
        addSidebarBtn(sb, "📄  Bills",      "bills");
        addSidebarBtn(sb, "💳  Payments",   "payments");

        sb.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("🚪  Logout");
        logoutBtn.setFont(AppTheme.FONT_BODY);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(AppTheme.DANGER);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(180, 36));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        sb.add(logoutBtn);
        sb.add(Box.createVerticalStrut(10));
        return sb;
    }

    private void addSidebarBtn(JPanel sb, String label, String card) {
        JButton btn = new JButton(label);
        btn.setFont(AppTheme.FONT_BODY);
        btn.setForeground(Color.WHITE);
        btn.setBackground(AppTheme.PRIMARY_DARK);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 10));
        btn.addActionListener(e -> cardLayout.show(contentArea, card));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(AppTheme.PRIMARY); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(AppTheme.PRIMARY_DARK); }
        });
        sb.add(btn);
        sb.add(Box.createVerticalStrut(2));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // HOME / STATS PANEL
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildHomePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        p.add(AppTheme.sectionTitle("Dashboard Overview"), BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setBackground(AppTheme.BG);
        cards.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        int totalConsumers  = consumerService.getAllConsumers().size();
        int pendingBills    = billService.countPendingBills();
        double revenue      = paymentService.getTotalRevenue();

        cards.add(statCard("👥 Total Consumers", String.valueOf(totalConsumers), AppTheme.PRIMARY));
        cards.add(statCard("📄 Pending Bills",    String.valueOf(pendingBills),   AppTheme.WARNING));
        cards.add(statCard("💰 Total Revenue",    "Rs " + String.format("%,.2f", revenue), AppTheme.SUCCESS));

        p.add(cards, BorderLayout.CENTER);

        JLabel welcome = new JLabel("Welcome back, " + admin.getUsername() + "! Use the sidebar to manage the system.", SwingConstants.CENTER);
        welcome.setFont(AppTheme.FONT_BODY);
        welcome.setForeground(AppTheme.TEXT_MUTED);
        p.add(welcome, BorderLayout.SOUTH);

        return p;
    }

    private JPanel statCard(String label, String value, Color accent) {
        JPanel card = AppTheme.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        JLabel lbl = new JLabel(label);
        lbl.setFont(AppTheme.FONT_BODY);
        lbl.setForeground(AppTheme.TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 26));
        val.setForeground(accent);
        val.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lbl);
        card.add(Box.createVerticalStrut(8));
        card.add(val);
        return card;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CONSUMERS PANEL
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildConsumersPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(AppTheme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header row
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(AppTheme.BG);
        headerRow.add(AppTheme.sectionTitle("Consumer Management"), BorderLayout.WEST);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(AppTheme.BG);
        JTextField searchField = AppTheme.styledField();
        searchField.setPreferredSize(new Dimension(180, 30));
        searchField.setToolTipText("Search consumers...");
        JButton searchBtn = AppTheme.primaryButton("Search");
        JButton addBtn    = AppTheme.successButton("+ Add");
        JButton editBtn   = AppTheme.warningButton("Edit");
        JButton deleteBtn = AppTheme.dangerButton("Delete");
        btnRow.add(searchField); btnRow.add(searchBtn);
        btnRow.add(addBtn); btnRow.add(editBtn); btnRow.add(deleteBtn);
        headerRow.add(btnRow, BorderLayout.EAST);
        p.add(headerRow, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Name", "Address", "Meter No.", "Type", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        AppTheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshConsumers(model, null);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDEE2E6)));
        p.add(scroll, BorderLayout.CENTER);

        // Button actions
        searchBtn.addActionListener(e -> refreshConsumers(model, searchField.getText().trim()));
        searchField.addActionListener(e -> refreshConsumers(model, searchField.getText().trim()));

        addBtn.addActionListener(e -> {
            showAddConsumerDialog();
            refreshConsumers(model, null);
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a consumer to edit."); return; }
            int id = (int) model.getValueAt(row, 0);
            showEditConsumerDialog(consumerService.getConsumerById(id));
            refreshConsumers(model, null);
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a consumer to delete."); return; }
            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete consumer ID " + id + "? This will also delete their bills and payments.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                consumerService.deleteConsumer(id);
                refreshConsumers(model, null);
            }
        });

        return p;
    }

    private void refreshConsumers(DefaultTableModel model, String keyword) {
        model.setRowCount(0);
        List<model.Consumer> list = (keyword == null || keyword.isEmpty())
            ? consumerService.getAllConsumers()
            : consumerService.searchConsumers(keyword);
        for (model.Consumer c : list) {
            model.addRow(new Object[]{
                c.getConsumerId(), c.getName(), c.getAddress(),
                c.getMeterNumber(), c.getConnectionType(), c.getStatus()
            });
        }
    }

    private void showAddConsumerDialog() {
        JTextField name     = AppTheme.styledField();
        JTextField address  = AppTheme.styledField();
        JTextField meter    = AppTheme.styledField();
        JComboBox<String> type = new JComboBox<>(new String[]{"Residential","Commercial","Industrial"});
        JPasswordField pass = AppTheme.styledPasswordField();

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.add(AppTheme.formLabel("Name:"));       form.add(name);
        form.add(AppTheme.formLabel("Address:"));    form.add(address);
        form.add(AppTheme.formLabel("Meter No.:"));  form.add(meter);
        form.add(AppTheme.formLabel("Type:"));       form.add(type);
        form.add(AppTheme.formLabel("Password:"));   form.add(pass);

        int result = JOptionPane.showConfirmDialog(this, form, "Add Consumer",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                boolean ok = consumerService.addConsumer(
                    name.getText().trim(), address.getText().trim(),
                    meter.getText().trim(), (String) type.getSelectedItem(),
                    new String(pass.getPassword())
                );
                JOptionPane.showMessageDialog(this,
                    ok ? "Consumer added successfully." : "Failed to add consumer.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditConsumerDialog(model.Consumer c) {
        if (c == null) return;
        JTextField name    = AppTheme.styledField(); name.setText(c.getName());
        JTextField address = AppTheme.styledField(); address.setText(c.getAddress());
        JTextField meter   = AppTheme.styledField(); meter.setText(c.getMeterNumber());
        JComboBox<String> type = new JComboBox<>(new String[]{"Residential","Commercial","Industrial"});
        type.setSelectedItem(c.getConnectionType());
        JComboBox<String> status = new JComboBox<>(new String[]{"Active","Inactive"});
        status.setSelectedItem(c.getStatus());

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.add(AppTheme.formLabel("Name:"));       form.add(name);
        form.add(AppTheme.formLabel("Address:"));    form.add(address);
        form.add(AppTheme.formLabel("Meter No.:"));  form.add(meter);
        form.add(AppTheme.formLabel("Type:"));       form.add(type);
        form.add(AppTheme.formLabel("Status:"));     form.add(status);

        int result = JOptionPane.showConfirmDialog(this, form, "Edit Consumer",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            c.setName(name.getText().trim());
            c.setAddress(address.getText().trim());
            c.setMeterNumber(meter.getText().trim());
            c.setConnectionType((String) type.getSelectedItem());
            c.setStatus((String) status.getSelectedItem());
            boolean ok = consumerService.updateConsumer(c);
            JOptionPane.showMessageDialog(this,
                ok ? "Consumer updated." : "Update failed.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // BILLS PANEL
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildBillsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(AppTheme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(AppTheme.BG);
        headerRow.add(AppTheme.sectionTitle("Bill Management"), BorderLayout.WEST);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(AppTheme.BG);
        JButton genBtn  = AppTheme.successButton("+ Generate");
        JButton payBtn  = AppTheme.primaryButton("Mark Paid");
        JButton delBtn  = AppTheme.dangerButton("Delete");
        btnRow.add(genBtn); btnRow.add(payBtn); btnRow.add(delBtn);
        headerRow.add(btnRow, BorderLayout.EAST);
        p.add(headerRow, BorderLayout.NORTH);

        String[] cols = {"Bill ID","Consumer ID","Month","Units","Rate","Total (Rs)","Due Date","Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        AppTheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshBills(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDEE2E6)));
        p.add(scroll, BorderLayout.CENTER);

        genBtn.addActionListener(e -> { showGenerateBillDialog(); refreshBills(model); });
        payBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a bill."); return; }
            int billId = (int) model.getValueAt(row, 0);
            showPayBillDialog(billId);
            refreshBills(model);
        });
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a bill."); return; }
            int billId = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete bill ID " + billId + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                billService.deleteBill(billId);
                refreshBills(model);
            }
        });

        return p;
    }

    private void refreshBills(DefaultTableModel model) {
        model.setRowCount(0);
        for (model.Bill b : billService.getAllBills()) {
            model.addRow(new Object[]{
                b.getBillId(), b.getConsumerId(), b.getMonth(),
                b.getUnitsConsumed(), b.getRatePerUnit(), b.getTotalAmount(),
                b.getDueDate(), b.getStatus()
            });
        }
    }

    private void showGenerateBillDialog() {
        List<model.Consumer> consumers = consumerService.getAllConsumers();
        if (consumers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No consumers found. Add a consumer first.");
            return;
        }
        JComboBox<model.Consumer> consumerBox = new JComboBox<>(consumers.toArray(new model.Consumer[0]));
        JTextField monthField = AppTheme.styledField(); monthField.setText(LocalDate.now().getMonth().toString() + " " + LocalDate.now().getYear());
        JTextField unitsField = AppTheme.styledField();
        JTextField rateField  = AppTheme.styledField();

        consumerBox.addActionListener(e -> {
            model.Consumer sel = (model.Consumer) consumerBox.getSelectedItem();
            if (sel != null) rateField.setText(BillService.getDefaultRate(sel.getConnectionType()).toPlainString());
        });
        // Trigger initial auto-fill
        model.Consumer first = (model.Consumer) consumerBox.getSelectedItem();
        if (first != null) rateField.setText(BillService.getDefaultRate(first.getConnectionType()).toPlainString());

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(AppTheme.formLabel("Consumer:"));       form.add(consumerBox);
        form.add(AppTheme.formLabel("Month:"));          form.add(monthField);
        form.add(AppTheme.formLabel("Units Consumed:")); form.add(unitsField);
        form.add(AppTheme.formLabel("Rate/Unit (Rs):")); form.add(rateField);

        int result = JOptionPane.showConfirmDialog(this, form, "Generate Bill",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                model.Consumer sel = (model.Consumer) consumerBox.getSelectedItem();
                BigDecimal units = new BigDecimal(unitsField.getText().trim());
                BigDecimal rate  = new BigDecimal(rateField.getText().trim());
                boolean ok = billService.generateBill(sel.getConsumerId(), monthField.getText().trim(), units, rate);
                JOptionPane.showMessageDialog(this, ok ? "Bill generated." : "Failed to generate bill.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showPayBillDialog(int billId) {
        model.Bill bill = billService.getBillById(billId);
        if (bill == null) return;
        if ("Paid".equals(bill.getStatus())) {
            JOptionPane.showMessageDialog(this, "This bill is already paid.");
            return;
        }
        JLabel amountLabel = new JLabel("Amount Due: Rs " + bill.getTotalAmount());
        amountLabel.setFont(AppTheme.FONT_HEADER);
        JComboBox<String> methodBox = new JComboBox<>(new String[]{"Cash","Bank Transfer","Online","Cheque"});

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(amountLabel); form.add(new JLabel());
        form.add(AppTheme.formLabel("Payment Method:")); form.add(methodBox);

        int result = JOptionPane.showConfirmDialog(this, form, "Process Payment",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                boolean ok = paymentService.processPayment(billId, bill.getTotalAmount(), (String) methodBox.getSelectedItem());
                JOptionPane.showMessageDialog(this, ok ? "Payment recorded. Bill marked as Paid." : "Payment failed.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PAYMENTS PANEL
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildPaymentsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(AppTheme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(AppTheme.BG);
        headerRow.add(AppTheme.sectionTitle("Payment History"), BorderLayout.WEST);
        JButton refreshBtn = AppTheme.primaryButton("Refresh");
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setBackground(AppTheme.BG);
        btnRow.add(refreshBtn);
        headerRow.add(btnRow, BorderLayout.EAST);
        p.add(headerRow, BorderLayout.NORTH);

        String[] cols = {"Payment ID","Bill ID","Date","Amount Paid","Method","Transaction ID"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        AppTheme.styleTable(table);
        refreshPayments(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDEE2E6)));
        p.add(scroll, BorderLayout.CENTER);

        // Revenue footer
        JLabel revenueLabel = new JLabel("Total Revenue: Rs " + String.format("%,.2f", paymentService.getTotalRevenue()),
            SwingConstants.RIGHT);
        revenueLabel.setFont(AppTheme.FONT_HEADER);
        revenueLabel.setForeground(AppTheme.SUCCESS);
        revenueLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        p.add(revenueLabel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> {
            refreshPayments(model);
            revenueLabel.setText("Total Revenue: Rs " + String.format("%,.2f", paymentService.getTotalRevenue()));
        });

        return p;
    }

    private void refreshPayments(DefaultTableModel model) {
        model.setRowCount(0);
        for (model.Payment pay : paymentService.getAllPayments()) {
            model.addRow(new Object[]{
                pay.getPaymentId(), pay.getBillId(), pay.getPaymentDate(),
                pay.getAmountPaid(), pay.getPaymentMethod(), pay.getTransactionId()
            });
        }
    }
}
