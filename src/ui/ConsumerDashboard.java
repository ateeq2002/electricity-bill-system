package ui;

import model.Consumer;
import model.Bill;
import model.Payment;
import service.BillService;
import service.PaymentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ConsumerDashboard extends JFrame {

    private final Consumer       consumer;
    private final BillService    billService    = new BillService();
    private final PaymentService paymentService = new PaymentService();

    public ConsumerDashboard(Consumer consumer) {
        this.consumer = consumer;
        setTitle("My Bills — " + consumer.getName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        // ── Top Bar ───────────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.PRIMARY);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel title = new JLabel("⚡ My Electricity Account — " + consumer.getName());
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        topBar.add(title, BorderLayout.WEST);
        JButton logoutBtn = AppTheme.dangerButton("Logout");
        logoutBtn.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        topBar.add(logoutBtn, BorderLayout.EAST);
        root.add(topBar, BorderLayout.NORTH);

        // ── Info Card ─────────────────────────────────────────────────────────
        JPanel infoCard = AppTheme.cardPanel();
        infoCard.setLayout(new GridLayout(1, 4, 16, 0));
        infoCard.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        infoCard.add(infoField("Meter Number",     consumer.getMeterNumber()));
        infoCard.add(infoField("Connection Type",  consumer.getConnectionType()));
        infoCard.add(infoField("Address",          consumer.getAddress()));
        infoCard.add(infoField("Account Status",   consumer.getStatus()));
        root.add(infoCard, BorderLayout.CENTER);

        // ── Tabs ──────────────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(AppTheme.FONT_HEADER);
        tabs.addTab("📄 My Bills",    buildBillsTab());
        tabs.addTab("💳 My Payments", buildPaymentsTab());

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(AppTheme.BG);
        center.add(infoCard, BorderLayout.NORTH);
        center.add(tabs,     BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        add(root);
    }

    private JPanel infoField(String label, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(AppTheme.FONT_SMALL);
        lbl.setForeground(AppTheme.TEXT_MUTED);
        JLabel val = new JLabel(value);
        val.setFont(AppTheme.FONT_HEADER);
        val.setForeground(AppTheme.TEXT);
        p.add(lbl, BorderLayout.NORTH);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    // ── Bills Tab ──────────────────────────────────────────────────────────────
    private JPanel buildBillsTab() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(AppTheme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setBackground(AppTheme.BG);
        JButton payBtn     = AppTheme.primaryButton("Pay Bill");
        JButton refreshBtn = AppTheme.successButton("Refresh");
        btnRow.add(payBtn); btnRow.add(refreshBtn);
        p.add(btnRow, BorderLayout.NORTH);

        String[] cols = {"Bill ID","Month","Units","Rate (Rs)","Total (Rs)","Due Date","Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        AppTheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshMyBills(model);

        // Color-code rows by status
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    String status = (String) model.getValueAt(row, 6);
                    c.setBackground(switch (status) {
                        case "Paid"    -> new Color(0xE8F5E9);
                        case "Overdue" -> new Color(0xFFEBEE);
                        default        -> (row % 2 == 0) ? Color.WHITE : AppTheme.TABLE_ALT;
                    });
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        p.add(scroll, BorderLayout.CENTER);

        payBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a bill to pay."); return; }
            int billId = (int) model.getValueAt(row, 0);
            String status = (String) model.getValueAt(row, 6);
            if ("Paid".equals(status)) { JOptionPane.showMessageDialog(this, "This bill is already paid."); return; }
            showPayDialog(billId, model);
        });
        refreshBtn.addActionListener(e -> refreshMyBills(model));

        return p;
    }

    private void refreshMyBills(DefaultTableModel model) {
        model.setRowCount(0);
        for (Bill b : billService.getBillsByConsumer(consumer.getConsumerId())) {
            model.addRow(new Object[]{
                b.getBillId(), b.getMonth(), b.getUnitsConsumed(),
                b.getRatePerUnit(), b.getTotalAmount(), b.getDueDate(), b.getStatus()
            });
        }
    }

    private void showPayDialog(int billId, DefaultTableModel model) {
        Bill bill = billService.getBillById(billId);
        if (bill == null) return;

        JLabel amtLabel = new JLabel("Amount Due: Rs " + bill.getTotalAmount());
        amtLabel.setFont(AppTheme.FONT_HEADER);
        amtLabel.setForeground(AppTheme.PRIMARY);
        JComboBox<String> methodBox = new JComboBox<>(new String[]{"Cash","Bank Transfer","Online","Cheque"});

        JPanel form = new JPanel(new GridLayout(3, 1, 8, 8));
        form.add(amtLabel);
        form.add(AppTheme.formLabel("Select Payment Method:"));
        form.add(methodBox);

        int result = JOptionPane.showConfirmDialog(this, form, "Pay Bill",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                boolean ok = paymentService.processPayment(billId, bill.getTotalAmount(),
                    (String) methodBox.getSelectedItem());
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Payment successful! Bill marked as Paid.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshMyBills(model);
                } else {
                    JOptionPane.showMessageDialog(this, "Payment failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Payments Tab ───────────────────────────────────────────────────────────
    private JPanel buildPaymentsTab() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(AppTheme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        String[] cols = {"Payment ID","Bill ID","Date","Amount Paid (Rs)","Method","Transaction ID"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        AppTheme.styleTable(table);

        // Load payments for all bills of this consumer
        for (Bill b : billService.getBillsByConsumer(consumer.getConsumerId())) {
            for (Payment pay : paymentService.getPaymentsByBill(b.getBillId())) {
                model.addRow(new Object[]{
                    pay.getPaymentId(), pay.getBillId(), pay.getPaymentDate(),
                    pay.getAmountPaid(), pay.getPaymentMethod(), pay.getTransactionId()
                });
            }
        }

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }
}
