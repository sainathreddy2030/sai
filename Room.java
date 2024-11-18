
import java.awt.*;

public class Room {

    private int x, y, width, height;
    private Color color;
    private boolean dragging;
    private int offsetX, offsetY;
    private int initialX, initialY; // Stores the original position when dragging starts

    public Room(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;

        // Store the initial position for snapping back if needed
        this.initialX = x;
        this.initialY = y;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public boolean contains(Point p) {
        return (p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
        if (dragging) {
            // Store the initial position at the start of dragging
            initialX = x;
            initialY = y;
        }
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void resetPosition() {
        // Snap back to initial position if overlap is detected
        this.x = initialX;
        this.y = initialY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
