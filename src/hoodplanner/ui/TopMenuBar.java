package hoodplanner.ui;

import hoodplanner.models.FloorObject;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TopMenuBar extends JMenuBar {

    private final List<JMenuItem> menuItems = new ArrayList<>();
    private final RightPanel<? extends FloorObject, ? extends ObjectLabel<? extends FloorObject>> rightPanel;
    private final JMenu fileMenu;
    private final JMenu addMenu;

    public TopMenuBar(RightPanel<? extends FloorObject, ? extends ObjectLabel<? extends FloorObject>> rightPanel) {
        this.rightPanel = rightPanel;

        fileMenu = new JMenu("File");
        addMenu = new JMenu("Add");

        add(fileMenu);
        add(addMenu);

        JMenuItem addRoomItem = new JMenuItem("Add Room");
        JMenuItem addItemItem = new JMenuItem("Add Fixtures/Furniture");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");
        JMenuItem loadItem = new JMenuItem("Load");

        addMenu.add(addRoomItem);
        addMenu.add(addItemItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(loadItem);

        menuItems.add(addRoomItem);
        menuItems.add(addItemItem);
        menuItems.add(saveItem);
        menuItems.add(saveAsItem);
        menuItems.add(loadItem);

        // Action Listeners
        addRoomItem.addActionListener(e -> {
            System.out.println("Add Room clicked");
            rightPanel.showAddObjectView();
        });

        addItemItem.addActionListener(e -> {
            System.out.println("Add Item clicked");
            rightPanel.showAddItemView();
        });
    }

    public JMenuItem getItem(String itemName) {
        return menuItems.stream()
                .filter(item -> item.getText().equals(itemName))
                .findFirst()
                .orElse(null);
    }
}
