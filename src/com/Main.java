import hoodplanner.ui.MainFrame;
import com.formdev.flatlaf.FlatDarkLaf;


public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}   