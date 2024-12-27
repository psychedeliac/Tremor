import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartMenu extends JPanel implements ActionListener, KeyListener {
    private JButton startButton, selectLevelButton, instructionsButton;
    private JButton[] buttons;
    private ImageIcon startBackground;
    private int selectedButtonIndex;

    public StartMenu() {
        setLayout(new GridBagLayout());
        setOpaque(false); // Set the panel to be transparent
        startBackground = new ImageIcon("assets/start_background.png");
        selectedButtonIndex = 0; // Initially select the startButton

        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Set the panel to be transparent
        // Make it occupy the top half of the frame
        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.gridx = 0;
        gbcTop.gridy = 0;
        gbcTop.weighty = 0.5; // Half of the vertical space
        gbcTop.fill = GridBagConstraints.BOTH; // Occupy both horizontal and vertical space
        add(topPanel, gbcTop);

        // Create bottom panel with FlowLayout
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false); // Set the panel to be transparent
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(200, 0, 0, 0)); // Add an empty border to shift the buttons down
        // Make it occupy the bottom half of the frame
        GridBagConstraints gbcBottom = new GridBagConstraints();
        gbcBottom.gridx = 0;
        gbcBottom.gridy = 1;
        gbcBottom.weighty = 0.5; // Half of the vertical space
        gbcBottom.fill = GridBagConstraints.BOTH; // Occupy both horizontal and vertical space
        add(bottomPanel, gbcBottom);

        // Buttons creation
        startButton = createButton("Start Game", "assets/start_game.png", "assets/start_game_hover.png");
        selectLevelButton = createButton("Select Level", "assets/select_level.png", "assets/select_level_hover.png");
        instructionsButton = createButton("Instructions", "assets/instructions.png", "assets/instructions_hover.png");

        // Add buttons to the bottom panel
        bottomPanel.add(startButton);
        bottomPanel.add(selectLevelButton);
        bottomPanel.add(instructionsButton);

        // Create an array to hold all buttons
        buttons = new JButton[]{startButton, selectLevelButton, instructionsButton};

        // Add action listeners and key listeners to buttons
        for (JButton button : buttons) {
            button.addActionListener(this);
            button.addKeyListener(this);
        }

        // Set focus to the start button initially
        startButton.requestFocusInWindow();
    }

    private JButton createButton(String text, String originalImagePath, String hoverImagePath) {
        JButton button = new JButton(text);
        button.setForeground(new Color(0, 0, 0, 0)); // Transparent text color
        button.setIcon(new ImageIcon(originalImagePath));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);

        // Add mouse listener to change icon on hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(new ImageIcon(hoverImagePath));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(new ImageIcon(originalImagePath));
            }
        });

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            JFrame levelOneFrame = new JFrame("Level One");
            LevelOne levelOne = new LevelOne();
            levelOneFrame.add(levelOne);
            levelOneFrame.setSize(800, 600);
            levelOneFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            levelOneFrame.setLocationRelativeTo(null); // Center the frame
            levelOneFrame.setVisible(true);
            ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();
        }  else if (e.getSource() == selectLevelButton) {
            // Show level selection window
            JFrame levelSelectionFrame = new JFrame("Level Selection");
            LevelSelectionWindow levelSelectionPanel = new LevelSelectionWindow();
            levelSelectionFrame.add(levelSelectionPanel);
            levelSelectionFrame.setSize(800, 600);
            levelSelectionFrame.setLocationRelativeTo(null); // Center the window
            levelSelectionFrame.setVisible(true);
            ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose(); // Close the current window
        } else if (e.getSource() == instructionsButton) {
            // Show instructions window
            JFrame instructionsFrame = new JFrame("Instructions");
            InstructionsWindow instructionsPanel = new InstructionsWindow();
            instructionsFrame.add(instructionsPanel);
            instructionsFrame.setSize(800, 600);
            instructionsFrame.setLocationRelativeTo(null); // Center the window
            instructionsFrame.setVisible(true);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(startBackground.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        JButton currentButton = buttons[selectedButtonIndex];

        if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT) {
            selectedButtonIndex = (selectedButtonIndex + 1) % buttons.length;
        } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_LEFT) {
            selectedButtonIndex = (selectedButtonIndex - 1 + buttons.length) % buttons.length;
        } else if (keyCode == KeyEvent.VK_ENTER) {
            currentButton.doClick();
            return; // Stop further processing if Enter is pressed
        }

        // Simulate mouse hover effect for the selected button
        for (int i = 0; i < buttons.length; i++) {
            if (i == selectedButtonIndex) {
                simulateMouseEnter(buttons[i]);
            } else {
                simulateMouseExit(buttons[i]);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    // Method to simulate mouse enter event
    private void simulateMouseEnter(JButton button) {
        ImageIcon hoverIcon = new ImageIcon(getHoverImagePath(button));
        button.setIcon(hoverIcon);
    }

    // Method to simulate mouse exit event
    private void simulateMouseExit(JButton button) {
        ImageIcon originalIcon = new ImageIcon(getOriginalImagePath(button));
        button.setIcon(originalIcon);
    }

    // Helper method to get the original image path for a button
    private String getOriginalImagePath(JButton button) {
        String buttonText = button.getText().toLowerCase().replace(" ", "_");
        return "assets/" + buttonText + ".png";
    }

    // Helper method to get the hover image path for a button
    private String getHoverImagePath(JButton button) {
        String buttonText = button.getText().toLowerCase().replace(" ", "_");
        return "assets/" + buttonText + "_hover.png";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Start Menu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().add(new StartMenu());
            frame.setVisible(true);
        });
    }
}
