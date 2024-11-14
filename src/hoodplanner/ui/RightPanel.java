package hoodplanner.ui;

import hoodplanner.controllers.RoomController;
import hoodplanner.models.FloorObject;
import hoodplanner.models.Room;
import hoodplanner.models.RoomType;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RightPanel<T extends FloorObject, L extends ObjectLabel<T>> extends JPanel {

    private L selectedObjectLabel;
    private final LeftPanel leftPanel;

    private final JLabel objectName;
    private final JLabel positionLabel;
    private final JLabel dimensionLabel;
    private final JComboBox<RoomType> roomTypeDropdown;
    private final JPanel addObjectPanel;
    private final JPanel addItemPanel;
    private final CardLayout cardLayout;
    private final JButton removeObjectButton;
    private final RoomController roomController;

    public RightPanel(LeftPanel leftPanel, RoomController roomController) {
        this.leftPanel = leftPanel;
        this.roomController = roomController;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        addObjectPanel = new JPanel();
        addObjectPanel.setLayout(null);

        objectName = new JLabel("Details Panel");
        objectName.setBounds(10, 10, 300, 25);
        objectName.setFont(objectName.getFont().deriveFont(16.0f));

        positionLabel = new JLabel("Position: ");
        positionLabel.setBounds(10, 40, 300, 25);
        dimensionLabel = new JLabel("Dimensions: ");
        dimensionLabel.setBounds(10, 70, 300, 25);

        addObjectPanel.add(objectName);
        addObjectPanel.add(positionLabel);
        addObjectPanel.add(dimensionLabel);

        roomTypeDropdown = new JComboBox<>(RoomType.values());
        JLabel typeLabel = new JLabel("Select Room Type:");
        typeLabel.setBounds(10, 95, 300, 25);
        roomTypeDropdown.setBounds(10, 120, 200, 25);

        addObjectPanel.add(typeLabel);
        addObjectPanel.add(roomTypeDropdown);

        roomTypeDropdown.addActionListener(e -> {
            if (selectedObjectLabel != null) {
                if (selectedObjectLabel.getObject() instanceof Room room) {
                    RoomType selectedType = (RoomType) roomTypeDropdown.getSelectedItem();
                    room.setType(selectedType);
                    selectedObjectLabel.setColor(selectedType.getColor());
                    this.leftPanel.repaint();
                }
            }
        });

        removeObjectButton = new JButton("Remove Room");
        removeObjectButton.setBounds(10, 160, 200, 25);
        addObjectPanel.add(removeObjectButton);

        removeObjectButton.addActionListener(e -> {
            if (selectedObjectLabel != null && selectedObjectLabel.getObject() instanceof Room) {
                Room room = (Room) selectedObjectLabel.getObject();
                roomController.deleteRoom(room, leftPanel); // Call deleteRoom on roomController
                selectedObjectLabel = null; // Clear selection
                positionLabel.setText("Position: ");
                dimensionLabel.setText("Dimensions: ");
            }
        });

        addItemPanel = new JPanel();
        addItemPanel.setLayout(null);
        JLabel addItemLabel = new JLabel("Add Items Panel");
        addItemLabel.setBounds(10, 10, 300, 25);
        addItemPanel.add(addItemLabel);

        add(addObjectPanel, "AddObject");
        add(addItemPanel, "AddItem");
    }

    public void showAddObjectView() {
        cardLayout.show(this, "AddObject");
    }

    public void showAddItemView() {
        cardLayout.show(this, "AddItem");
    }

    public void setSelectedObjectLabel(L objectLabel) {
        selectedObjectLabel = objectLabel;
        update(objectLabel);
    }

    private void update(L objectLabel) {
        if (objectLabel == null) {
            return;
        }
        T object = objectLabel.getObject();
        double x = object.getX();
        double y = object.getY();
        double width = object.getWidth();
        double length = object.getLength();

        // Update the labels with the object's position and size
        positionLabel.setText("Position: X = " + x + ", Y = " + y);
        dimensionLabel.setText("Dimensions: Width = " + width + ", Length = " + length);

        if (object instanceof Room room) {
            objectName.setText("Details Panel: " + room.getName());
            Color bgColor = objectLabel.getBackground();
            for (RoomType type : RoomType.values()) {
                if (bgColor.equals(type.getColor())) {
                    roomTypeDropdown.setSelectedItem(type);
                    break;
                }
            }
        } else {
            roomTypeDropdown.setEnabled(false);
        }
    }
}
