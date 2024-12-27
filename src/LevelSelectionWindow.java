import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LevelSelectionWindow extends JPanel implements ActionListener {
    private JButton[] levelButtons;
    private JButton backButton;
    private ImageIcon backgroundImg;

    public LevelSelectionWindow() {
        setLayout(new GridBagLayout());
        setOpaque(false); // Set the panel to be transparent
        backgroundImg = new ImageIcon("assets/start_background.png");

        // Create a panel for the level buttons
        JPanel levelPanel = new JPanel(new GridLayout(2, 1, 0, 30)); // Added vertical gap of 30 pixels
        levelPanel.setOpaque(false); // Set the panel to be transparent

        // Create level buttons with images
        levelButtons = new JButton[2]; // Assuming there are two levels
        for (int i = 0; i < levelButtons.length; i++) {
            JButton levelButton = createButton("assets/level" + (i + 1) + ".gif", "assets/level" + (i + 1) + ".gif", 300, 150);
            levelButtons[i] = levelButton;
            levelButtons[i].addActionListener(this);
            levelPanel.add(levelButton);
        }

        // Create back button with image
        backButton = createButton("assets/back_button.png", "assets/back_button_hover.png", 300, 150);
        backButton.addActionListener(this);

        // Add components to the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(levelPanel, gbc);

        gbc.gridy = 1;
        add(backButton, gbc);
    }

    private JButton createButton(String defaultImagePath, String hoverImagePath, int width, int height) {
        ImageIcon defaultIcon = new ImageIcon(new ImageIcon(defaultImagePath).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        ImageIcon hoverIcon = new ImageIcon(new ImageIcon(hoverImagePath).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));

        JButton button = new JButton(defaultIcon);
        // Set button properties
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setIcon(hoverIcon);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setIcon(defaultIcon);
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImg.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            // Go back to the start menu
            JFrame startMenuFrame = new JFrame("Start Menu");
            StartMenu startMenu = new StartMenu();
            startMenuFrame.add(startMenu);
            startMenuFrame.setSize(800, 600);
            startMenuFrame.setLocationRelativeTo(null); // Center the window
            startMenuFrame.setVisible(true);
            ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose(); // Close the current window
        } else {
            if (e.getSource() == levelButtons[0]) {
                // Start level one
                JFrame levelOneFrame = new JFrame("Level One");
                LevelOne levelOne = new LevelOne();
                levelOneFrame.add(levelOne);
                levelOneFrame.setSize(800, 600);
                levelOneFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                levelOneFrame.setLocationRelativeTo(null); // Center the frame
                levelOneFrame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();

            } else if (e.getSource() == levelButtons[1]) {
                // Start level two
                JFrame levelTwoFrame = new JFrame("Level Two");
                LevelTwo levelTwo = new LevelTwo();
                levelTwoFrame.add(levelTwo);
                levelTwoFrame.setSize(800, 600);
                levelTwoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                levelTwoFrame.setLocationRelativeTo(null); // Center the frame
                levelTwoFrame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose(); // Close the current window
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Level Selection");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().add(new LevelSelectionWindow());
            frame.setVisible(true);
        });
    }
}
