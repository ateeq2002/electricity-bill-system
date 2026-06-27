package ui;

import model.Admin;
import model.Consumer;
import service.ConsumerService;
import dao.AdminDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTabbedPane tabs;

    // Admin fields
    private JTextField     adminUsernameField;
    private JPasswordField adminPasswordField;

    // Consumer fields
    private JTextField     meterField;
    private JPasswordField consumerPasswordField;

    public LoginFrame() {
        AppTheme.applyLookAndFeel();
        setTitle("Electricity Bill Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(460, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(AppTheme.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("⚡ Electricity Bill System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
        root.add(header, BorderLayout.NORTH);

        // ── Tabs ──────────────────────────────────────────────────────────────
        tabs = new JTabbedPane();
        tabs.setFont(AppTheme.FONT_HEADER);
        tabs.setBackground(AppTheme.BG);
        tabs.addTab("Admin Login",    buildAdminPanel());
        tabs.addTab("Consumer Login", buildConsumerPanel());
        root.add(tabs, BorderLayout.CENTER);

        add(root);
    }

    // ── Admin Login Panel ─────────────────────────────────────────────────────
    private JPanel buildAdminPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(AppTheme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        adminUsernameField = AppTheme.styledField();
        adminPasswordField = AppTheme.styledPasswordField();
        JButton loginBtn   = AppTheme.primaryButton("Login");

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        p.add(AppTheme.formLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        p.add(adminUsernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        p.add(AppTheme.formLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        p.add(adminPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        p.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> adminLogin());
        adminPasswordField.addActionListener(e -> adminLogin());

        return p;
    }

    // ── Consumer Login Panel ──────────────────────────────────────────────────
    private JPanel buildConsumerPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(AppTheme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        meterField               = AppTheme.styledField();
        consumerPasswordField    = AppTheme.styledPasswordField();
        JButton loginBtn         = AppTheme.primaryButton("Login");

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        p.add(AppTheme.formLabel("Meter Number:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        p.add(meterField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        p.add(AppTheme.formLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        p.add(consumerPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        p.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> consumerLogin());
        consumerPasswordField.addActionListener(e -> consumerLogin());

        return p;
    }

    // ── Login Logic ───────────────────────────────────────────────────────────
    private void adminLogin() {
        String user = adminUsernameField.getText().trim();
        String pass = new String(adminPasswordField.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Admin admin = new AdminDAO().login(user, pass);
        if (admin != null) {
            dispose();
            new AdminDashboard(admin).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            adminPasswordField.setText("");
        }
    }

    private void consumerLogin() {
        String meter = meterField.getText().trim();
        String pass  = new String(consumerPasswordField.getPassword());
        if (meter.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Consumer consumer = new ConsumerService().login(meter, pass);
        if (consumer != null) {
            dispose();
            new ConsumerDashboard(consumer).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid meter number or password, or account inactive.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            consumerPasswordField.setText("");
        }
    }
}
