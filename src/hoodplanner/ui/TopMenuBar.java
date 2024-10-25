package hoodplanner.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopMenuBar extends JMenuBar {

    private final List<JMenuItem> menuItems = new ArrayList<>();
    private final RightPanel rightPanel;

    public TopMenuBar(RightPanel rightPanel) {
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
                rightPanel.showAddRoomView();
            }
        });

        addItemItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add Item clicked");
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
