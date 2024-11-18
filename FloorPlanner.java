
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FloorPlanner {

    private JFrame mainFrame;
    private CanvasPanel canvasPanel;
    private ControlPanel controlPanel;

    public FloorPlanner() {
        // Initialize the main frame
        mainFrame = new JFrame("2D Floor Planner");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        mainFrame.setLayout(new BorderLayout());

        // Initialize canvas and control panels
        canvasPanel = new CanvasPanel();
        controlPanel = new ControlPanel(canvasPanel);

        // Add panels to the frame
        mainFrame.add(canvasPanel, BorderLayout.CENTER); // Canvas on the main area
        mainFrame.add(controlPanel, BorderLayout.EAST);   // Control panel on the right side

        // Show the frame
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(FloorPlanner::new);
    }
}

class ControlPanel extends JPanel {

    private CanvasPanel canvasPanel;
    private JComboBox<String> roomTypeCombo;
    private JTextField widthField, heightField;
    private JButton addRoomButton;

    public ControlPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;
        setLayout(new GridLayout(5, 1, 10, 10));
        setPreferredSize(new Dimension(200, 0));

        // Room Type Selection
        roomTypeCombo = new JComboBox<>(new String[]{"Bedroom", "Bathroom", "Kitchen", "Living Room"});
        add(new JLabel("Room Type:"));
        add(roomTypeCombo);

        // Room Dimensions
        widthField = new JTextField();
        heightField = new JTextField();
        add(new JLabel("Width:"));
        add(widthField);
        add(new JLabel("Height:"));
        add(heightField);

        // Add Room Button
        addRoomButton = new JButton("Add Room");
        addRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRoom();
            }
        });
        add(addRoomButton);
    }

    private void addRoom() {
        try {
            String roomType = (String) roomTypeCombo.getSelectedItem();
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());

            // Set room color based on type
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
                case "Living Room":
                    color = Color.ORANGE;
                    break;
                default:
                    color = Color.GRAY;
            }

            // Create and add room to the canvas
            Room room = new Room(50, 50, width, height, color); // Initial position set arbitrarily
            canvasPanel.addRoom(room, false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid dimensions.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
