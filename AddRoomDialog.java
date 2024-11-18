
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AddRoomDialog extends JDialog {

    private static final int ROOM_SPACING = 10;

    private JComboBox<String> positionCombo;
    private JRadioButton leftAlign, centerAlign, rightAlign;
    private JTextField widthField, heightField;
    private JComboBox<String> roomTypeCombo;
    private Room referenceRoom;
    private CanvasPanel canvasPanel;

    public AddRoomDialog(Room referenceRoom, CanvasPanel canvasPanel) {
        this.referenceRoom = referenceRoom;
        this.canvasPanel = canvasPanel;

        setTitle("Add Room Relatively");
        setSize(400, 300);  // Adjusted size to ensure all elements are visible
        setLayout(new GridLayout(8, 1, 5, 5));
        setLocationRelativeTo(null);

        // Position selection dropdown
        add(new JLabel("Position:"));
        positionCombo = new JComboBox<>(new String[]{"North", "South", "East", "West"});
        add(positionCombo);

        // Alignment options with radio buttons
        add(new JLabel("Alignment:"));
        leftAlign = new JRadioButton("Left");
        centerAlign = new JRadioButton("Center");
        rightAlign = new JRadioButton("Right");
        leftAlign.setSelected(true);

        // Group the alignment radio buttons
        ButtonGroup alignmentGroup = new ButtonGroup();
        alignmentGroup.add(leftAlign);
        alignmentGroup.add(centerAlign);
        alignmentGroup.add(rightAlign);

        // Add the alignment radio buttons to a panel with enough width
        JPanel alignmentPanel = new JPanel(new FlowLayout());
        alignmentPanel.add(leftAlign);
        alignmentPanel.add(centerAlign);
        alignmentPanel.add(rightAlign);
        add(alignmentPanel);

        // Room dimensions input fields
        add(new JLabel("Width:"));
        widthField = new JTextField(5);
        add(widthField);

        add(new JLabel("Height:"));
        heightField = new JTextField(5);
        add(heightField);

        // Room type dropdown
        add(new JLabel("Room Type:"));
        roomTypeCombo = new JComboBox<>(new String[]{"Bedroom", "Bathroom", "Kitchen", "Living Room"});
        add(roomTypeCombo);

        // Add Room button
        JButton addButton = new JButton("Add Room");
        addButton.addActionListener(new AddRoomAction());
        add(addButton);
    }

    private class AddRoomAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String position = (String) positionCombo.getSelectedItem();
            String alignment = leftAlign.isSelected() ? "Left" : centerAlign.isSelected() ? "Center" : "Right";
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            String roomType = (String) roomTypeCombo.getSelectedItem();
            Color color;

            switch (roomType) {
                case "Bedroom":
                    color = Color.GREEN;
                    break;
                case "Bathroom":
                    color = Color.BLUE;
                    break;
                case "Kitchen":
                    color = Color.RED;
                    break;
                default:
                    color = Color.ORANGE;
                    break;
            }

            Room newRoom = new Room(0, 0, width, height, color);
            setRelativePosition(newRoom, position, alignment);

            if (!canvasPanel.checkOverlap(newRoom)) {
                canvasPanel.addRoom(newRoom, true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(AddRoomDialog.this, "Room placement results in overlap.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setRelativePosition(Room newRoom, String position, String alignment) {
        int refX = referenceRoom.getX();
        int refY = referenceRoom.getY();
        int refWidth = referenceRoom.getWidth();
        int refHeight = referenceRoom.getHeight();

        int newX = refX;
        int newY = refY;

        switch (position) {
            case "North":
                newY = refY - newRoom.getHeight() - ROOM_SPACING;
                newX = alignment.equals("Left") ? refX : alignment.equals("Center") ? refX + (refWidth - newRoom.getWidth()) / 2 : refX + refWidth - newRoom.getWidth();
                break;

            case "South":
                newY = refY + refHeight + ROOM_SPACING;
                newX = alignment.equals("Left") ? refX : alignment.equals("Center") ? refX + (refWidth - newRoom.getWidth()) / 2 : refX + refWidth - newRoom.getWidth();
                break;

            case "East":
                newX = refX + refWidth + ROOM_SPACING;
                newY = alignment.equals("Left") ? refY : alignment.equals("Center") ? refY + (refHeight - newRoom.getHeight()) / 2 : refY + refHeight - newRoom.getHeight();
                break;

            case "West":
                newX = refX - newRoom.getWidth() - ROOM_SPACING;
                newY = alignment.equals("Left") ? refY : alignment.equals("Center") ? refY + (refHeight - newRoom.getHeight()) / 2 : refY + refHeight - newRoom.getHeight();
                break;
        }

        newRoom.setPosition(newX, newY);
    }
}
