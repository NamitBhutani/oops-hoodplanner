package hoodplanner.controllers;

import hoodplanner.models.FloorObject;
import hoodplanner.ui.LeftPanel;
import hoodplanner.ui.ObjectLabel;
import hoodplanner.ui.RightPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class FloorObjectController<T extends FloorObject, L extends ObjectLabel<T>> {
    private final List<L> objectLabels = new ArrayList<>();

    public void createObjectLabel(T object, L label, LeftPanel leftPanel, RightPanel<T, L> rightPanel) {
        objectLabels.add(label);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rightPanel.setSelectedObjectLabel(label);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                rightPanel.setSelectedObjectLabel(label);
            }
        });
        
        leftPanel.add(label);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    public void deleteObjectLabel(L label, LeftPanel leftPanel) {
        objectLabels.remove(label);
        leftPanel.remove(label);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    public boolean isOverlappingAny(L label) {
        for (L otherLabel : objectLabels) {
            if (label != otherLabel && label.getObject().checkOverlap(otherLabel.getObject())) {
                return true;
            }
        }
        return false;
    }

    public List<L> getObjectLabels() {
        return objectLabels;
    }

    public L getObjectLabel(T object) {
        for (L label : objectLabels) {
            if (label.getObject().equals(object)) {
                return label;
            }
        }
        return null;
    }

    public void clearObjectLabels() {
        objectLabels.clear();
    }
}
