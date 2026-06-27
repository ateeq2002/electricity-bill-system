import ui.AppTheme;
import ui.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        AppTheme.applyLookAndFeel();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
