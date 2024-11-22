package hoodplanner.models;

import javax.swing.*;
import java.awt.*;

public class Furniture extends FloorObject {
    private String name;
    private String imagePath;
    private Image image;

    public Furniture(double length, double width, double x, double y, String name, String imagePath) {
        super(length, width, x, y);
        this.name = name;
        this.imagePath = imagePath;
        this.image = loadImage(imagePath);
    }

    public Furniture() {
        super();
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        this.image = loadImage(imagePath);
    }

    private Image loadImage(String path) {
        // Load the image from the path and handle any issues
        ImageIcon icon = new ImageIcon(path);
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            System.out.println("Image loaded: " + path); // Debug message
        } else {
            System.out.println("Failed to load image: " + path); // Debug message
        }
        return icon.getImage();
    }

    public void draw(Graphics g, int x, int y) {
        if (image != null) {
            // Draw the furniture at the specified (x, y) location
            g.drawImage(image, x, y, (int) getWidth(), (int) getLength(), null);
        } else {
            System.out.println("Image not available for: " + name); // Debug message
        }
    }
}
