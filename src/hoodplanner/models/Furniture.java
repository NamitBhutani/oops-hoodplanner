package hoodplanner.models;

import java.awt.*;
import javax.swing.*;

public class Furniture extends FloorObject {
    private String name;
    private String imagePath;
    private Image image;
    private Image scaledImage;

    public Furniture(double length, double width, double x, double y, String name, String imagePath) {
        super(length, width, x, y);
        this.name = name;
        this.imagePath = imagePath;
        this.image = loadImage(imagePath);
        this.scaledImage = new ImageIcon(this.image.getScaledInstance(100, 100, Image.SCALE_SMOOTH)).getImage();
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
        this.scaledImage = new ImageIcon(this.image.getScaledInstance(100, 100, Image.SCALE_SMOOTH)).getImage();
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

    public void draw(Graphics g, int x, int y, JLabel label) {
        if (image != null) {
            // Draw the furniture at the specified (x, y) location
            System.out.println("Drawing furniture: " + name + " at x: " + x + " y: " + y); // Debug message
            // Image img = this.image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);'
            System.out.println("Image: " + scaledImage + " Loss " + image); // Debug message
            System.out.println(g.drawImage(scaledImage, x, y, label));
        } else {
            System.out.println("Image not available for: " + name); // Debug message
        }
    }
}
