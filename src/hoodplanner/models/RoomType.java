package hoodplanner.models;

import java.awt.Color;

public enum RoomType {
    LIVING_ROOM("Living Room", new Color(215, 110, 0)), // Orange
    BATHROOM("Bathroom", new Color(40, 40, 155)),
    BEDROOM("Bedroom", new Color(40, 155, 40)),
    KITCHEN("Kitchen", new Color(130, 131, 40));

    private final String name;
    private final Color color;

    RoomType(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }
}