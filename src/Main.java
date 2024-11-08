import hoodplanner.ui.MainFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import hoodplanner.controllers.RoomController;
import hoodplanner.models.FloorPlan;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        FloorPlan floorPlan = FloorPlan.load(); // Load or initialize FloorPlan
        RoomController roomController = new RoomController(floorPlan);

        // Pass both controllers to MainFrame
        MainFrame mainFrame = new MainFrame(Arrays.asList(roomController), floorPlan);
        mainFrame.setVisible(true);
    }
}
