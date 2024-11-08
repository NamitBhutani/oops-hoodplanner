package hoodplanner.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import hoodplanner.models.FloorObject;

public class TopMenuBar extends JMenuBar {

    private final List<JMenuItem> menuItems = new ArrayList<>();
    private final RightPanel<? extends FloorObject, ? extends ObjectLabel<? extends FloorObject>> rightPanel;

    public TopMenuBar(RightPanel<? extends FloorObject, ? extends ObjectLabel<? extends FloorObject>> rightPanel) {
        this.rightPanel = rightPanel;

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

        addRoomItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add Room clicked");
                rightPanel.showAddObjectView();
            }
        });

        addItemItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add Item clicked");
                // For now, we just show the Add Items view
                rightPanel.showAddItemView();
            }
        });
    }

    public JMenuItem getItem(String itemName) {
        return menuItems.stream()
                .filter(item -> item.getText().equals(itemName))
                .findFirst()
                .orElse(null);
    }
}
