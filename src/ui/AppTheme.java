package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class AppTheme {
    // ── Palette ───────────────────────────────────────────────────────────────
    public static final Color PRIMARY       = new Color(0x1565C0); // deep blue
    public static final Color PRIMARY_DARK  = new Color(0x0D47A1);
    public static final Color ACCENT        = new Color(0x00ACC1); // teal
    public static final Color SUCCESS       = new Color(0x2E7D32);
    public static final Color DANGER        = new Color(0xC62828);
    public static final Color WARNING       = new Color(0xF57F17);
    public static final Color BG            = new Color(0xF5F7FA);
    public static final Color CARD_BG       = Color.WHITE;
    public static final Color TEXT          = new Color(0x212121);
    public static final Color TEXT_MUTED    = new Color(0x757575);
    public static final Color TABLE_HEADER  = new Color(0x1565C0);
    public static final Color TABLE_ALT     = new Color(0xE8EAF6);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);

    // ── Helpers ───────────────────────────────────────────────────────────────
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_HEADER);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 36));
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(DANGER);
        return btn;
    }

    public static JButton successButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(SUCCESS);
        return btn;
    }

    public static JButton warningButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(WARNING);
        btn.setForeground(TEXT);
        return btn;
    }

    public static JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(PRIMARY);
        return lbl;
    }

    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xDEE2E6), 1, true),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(28);
        table.setGridColor(new Color(0xE0E0E0));
        table.setSelectionBackground(new Color(0xBBDEFB));
        table.setSelectionForeground(TEXT);
        table.getTableHeader().setFont(FONT_HEADER);
        table.getTableHeader().setBackground(TABLE_HEADER);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
    }

    public static JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xBDBDBD)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return tf;
    }

    public static JPasswordField styledPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_BODY);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xBDBDBD)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return pf;
    }

    public static JLabel formLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BODY);
        lbl.setForeground(TEXT);
        return lbl;
    }

    public static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        UIManager.put("OptionPane.messageFont",  FONT_BODY);
        UIManager.put("OptionPane.buttonFont",   FONT_BODY);
    }
}
