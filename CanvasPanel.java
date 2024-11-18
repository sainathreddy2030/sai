
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CanvasPanel extends JPanel {

    private List<Room> rooms;
    private static final int ROOM_SPACING = 10;
    private int currentX = ROOM_SPACING;
    private int currentY = ROOM_SPACING;
    private int panelWidth;
    private Room draggedRoom = null;  // To track the currently dragged room

    public CanvasPanel() {
        this.rooms = new ArrayList<>();
        setBackground(Color.LIGHT_GRAY);
        panelWidth = getPreferredSize().width;

        // Add mouse listeners for right-click and drag-and-drop functionality
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Right-click: show context menu
                    Room selectedRoom = getRoomAt(e.getPoint());
                    if (selectedRoom != null) {
                        showContextMenu(e, selectedRoom);
                    }
                } else {
                    // Left-click: initiate drag if clicking on a room
                    draggedRoom = getRoomAt(e.getPoint());
                    if (draggedRoom != null) {
                        draggedRoom.setDragging(true);
                        draggedRoom.setOffset(e.getX() - draggedRoom.getX(), e.getY() - draggedRoom.getY());
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Release dragged room and reset dragging state
                if (draggedRoom != null && draggedRoom.isDragging()) {
                    draggedRoom.setDragging(false);
                    if (checkOverlap(draggedRoom)) {
                        JOptionPane.showMessageDialog(null, "Overlap detected! Resetting position.");
                        draggedRoom.resetPosition(); // Snap back if overlap detected
                    }
                    draggedRoom = null; // Clear reference to the dragged room
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Update the position of the dragged room
                if (draggedRoom != null && draggedRoom.isDragging()) {
                    draggedRoom.setPosition(e.getX() - draggedRoom.getOffsetX(), e.getY() - draggedRoom.getOffsetY());
                    repaint();
                }
            }
        });
    }

    // Add a room in row-major order
    public void addRoom(Room room) {
        if (currentX + room.getWidth() + ROOM_SPACING > panelWidth) {
            currentX = ROOM_SPACING;  // Start a new row
            currentY += room.getHeight() + ROOM_SPACING; // Move down to next row
        }

        room.setPosition(currentX, currentY);
        currentX += room.getWidth() + ROOM_SPACING; // Update X position for the next room in row-major order

        rooms.add(room);
        repaint();
    }

    // Overloaded addRoom method for relative positioning
    public void addRoom(Room room, boolean isRelative) {
        if (isRelative) {
            // Add room with specific position set by dialog
            if (!checkOverlap(room)) {
                rooms.add(room);
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Cannot add room: Overlaps with an existing room.");
            }
        } else {
            addRoom(room); // Use row-major order placement
        }
    }

    // Check if the given room overlaps with any other room
    public boolean checkOverlap(Room newRoom) {
        for (Room room : rooms) {
            if (room != newRoom && room.getBounds().intersects(newRoom.getBounds())) {
                return true;
            }
        }
        return false;
    }

    // Show context menu to delete room or add a room relatively
    private void showContextMenu(MouseEvent e, Room selectedRoom) {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem deleteRoomItem = new JMenuItem("Delete Room");
        deleteRoomItem.addActionListener(event -> {
            rooms.remove(selectedRoom);
            repaint();
        });

        JMenuItem addRoomRelativelyItem = new JMenuItem("Add Room Relatively");
        addRoomRelativelyItem.addActionListener(event -> openAddRoomDialog(selectedRoom));

        contextMenu.add(deleteRoomItem);
        contextMenu.add(addRoomRelativelyItem);
        contextMenu.show(this, e.getX(), e.getY());
    }

    // Open AddRoomDialog for adding a new room relative to the selected room
    private void openAddRoomDialog(Room selectedRoom) {
        AddRoomDialog dialog = new AddRoomDialog(selectedRoom, this);
        dialog.setVisible(true);
    }

    // Helper method to find the room at a specific point (for dragging)
    private Room getRoomAt(Point point) {
        for (Room room : rooms) {
            if (room.contains(point)) {
                return room;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Room room : rooms) {
            room.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 600); // Set preferred panel size
    }
}
