package hoodplanner.ui;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import hoodplanner.models.FloorObject;
import hoodplanner.models.RoomType;
import hoodplanner.models.Room;

public class RightPanel<T extends FloorObject, L extends ObjectLabel<T>> extends JPanel {

    private L selectedObjectLabel;
    private final LeftPanel leftPanel;
    private final JLabel positionLabel;
    private final JLabel dimensionLabel;
    private final JComboBox<RoomType> roomTypeDropdown;
    private final JPanel addObjectPanel;
    private final JPanel addItemPanel;
    private final CardLayout cardLayout;
    private final JButton removeObjectButton;

    public RightPanel(LeftPanel leftPanel) {
        this.leftPanel = leftPanel;

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        addObjectPanel = new JPanel();
        addObjectPanel.setLayout(null);
        positionLabel = new JLabel("Position: ");
        positionLabel.setBounds(10, 10, 300, 25);
        dimensionLabel = new JLabel("Dimensions: ");
        dimensionLabel.setBounds(10, 40, 300, 25);

        addObjectPanel.add(positionLabel);
        addObjectPanel.add(dimensionLabel);

        roomTypeDropdown = new JComboBox<>(RoomType.values());
        JLabel typeLabel = new JLabel("Select Room Type:");
        typeLabel.setBounds(10, 70, 300, 25);
        roomTypeDropdown.setBounds(10, 100, 200, 25);

        addObjectPanel.add(typeLabel);
        addObjectPanel.add(roomTypeDropdown);

        roomTypeDropdown.addActionListener(e -> {
            if (selectedObjectLabel != null) {
                if (selectedObjectLabel.getObject() instanceof Room) {
                    RoomType selectedType = (RoomType) roomTypeDropdown.getSelectedItem();
                    selectedObjectLabel.setColor(selectedType.getColor());
                    this.leftPanel.repaint();
                }
            }
        });

        removeObjectButton = new JButton("Remove Object");
        removeObjectButton.setBounds(10, 140, 200, 25);
        addObjectPanel.add(removeObjectButton);

        removeObjectButton.addActionListener(e -> {
            if (selectedObjectLabel != null) {
                leftPanel.remove(selectedObjectLabel);
                leftPanel.revalidate();
                leftPanel.repaint();
                selectedObjectLabel = null;
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
        double height = object.getHeight();

        // Update the labels with the object's position and size
        positionLabel.setText("Position: X = " + x + ", Y = " + y);
        dimensionLabel.setText("Dimensions: Width = " + width + ", Height = " + height);

        if (object instanceof Room) {
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
