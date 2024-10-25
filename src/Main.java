import hoodplanner.ui.MainFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import hoodplanner.controllers.RoomController;
import hoodplanner.models.FloorPlan;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        FloorPlan floorPlan = FloorPlan.load(); // new FloorPlan("Test Floor Plan");
        RoomController roomController = new RoomController(floorPlan);
        MainFrame mainFrame = new MainFrame(roomController, floorPlan);
        mainFrame.setVisible(true);
    }
}