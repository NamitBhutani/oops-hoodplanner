package hoodplanner.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class TopMenuBar extends JMenuBar {

    private final List<JMenuItem> menuItems = new ArrayList<>();

    public TopMenuBar() {
        JMenu fileMenu = new JMenu("File");
        JMenu addMenu = new JMenu("Add");

        add(fileMenu);
        add(addMenu);

        JMenuItem addRoomItem = new JMenuItem("Add Room");
        JMenuItem addItemItem = new JMenuItem("Add Items");

        addMenu.add(addRoomItem);
        addMenu.add(addItemItem);

        menuItems.add(addRoomItem);
        menuItems.add(addItemItem);
    }

    public JMenuItem getItem(String itemName) {
        
        JMenuItem reqItem = menuItems.stream()
                .filter(item -> item.getText().equals(itemName))
                .findFirst()
                .orElse(null);

        return reqItem;
    }
    
}
