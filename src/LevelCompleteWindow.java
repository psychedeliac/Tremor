import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LevelCompleteWindow extends JFrame {
    private JButton nextLevelButton;
    private ImageIcon backgroundimg;

    private Clip gameOverClip;
    public LevelCompleteWindow() {
        setTitle("Level Completed");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        backgroundimg = new ImageIcon("assets/background.png");

        playGameOverSound("assets/sounds/gameover.wav");

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                g.drawImage(backgroundimg.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false); // Set transparent background

        nextLevelButton = createButton("Next Level", "assets/next_level.png", "assets/next_level_hover.png", 600, 210);
        nextLevelButton.addActionListener(e -> {
            // Start LevelTwo
            JFrame levelTwoFrame = new JFrame("Level Two");
            LevelTwo levelTwo = new LevelTwo();
            levelTwoFrame.add(levelTwo);
            levelTwoFrame.setSize(800, 600);
            levelTwoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            levelTwoFrame.setLocationRelativeTo(null); // Center the frame
            levelTwoFrame.setVisible(true);
            dispose();
        });

        buttonPanel.add(nextLevelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(panel);
        setVisible(true);
    }

    private JButton createButton(String text, String defaultImagePath, String hoverImagePath, int width, int height) {
        ImageIcon defaultIcon = new ImageIcon(new ImageIcon(defaultImagePath).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        ImageIcon hoverIcon = new ImageIcon(new ImageIcon(hoverImagePath).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));

        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    g.drawImage(hoverIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.drawImage(defaultIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
                super.paintComponent(g);
            }
        };
        // Set the foreground color to transparent
        button.setForeground(new Color(0, 0, 0, 0));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));
        return button;
    }
    private void playGameOverSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            gameOverClip = AudioSystem.getClip();
            gameOverClip.open(audioInputStream);
            setVolume(gameOverClip, 6.0f); // Adjust volume if needed
            gameOverClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setVolume(Clip clip, float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LevelCompleteWindow::new);
    }
}
