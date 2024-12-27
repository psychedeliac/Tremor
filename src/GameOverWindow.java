import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GameOverWindow extends JFrame {
    private Clip gameOverClip;

    public GameOverWindow() {
        setTitle("Game Over");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        playGameOverSound("assets/sounds/gameover.wav");

        ImageIcon backgroundImage = new ImageIcon("assets/gameover.png");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        setContentPane(backgroundLabel);

        ImageIcon normalIcon = new ImageIcon("assets/mm.png");
        ImageIcon hoveredIcon = new ImageIcon("assets/mmh.png");

        JButton restartButton = new JButton(normalIcon);
        restartButton.setOpaque(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setBorderPainted(false);
        restartButton.setFocusPainted(false);

        // button position
        int buttonWidth = normalIcon.getIconWidth();
        int buttonHeight = normalIcon.getIconHeight();
        int x = (getWidth() - buttonWidth) / 2;
        int y = (getHeight() - buttonHeight) / 2 + 200;

        restartButton.setBounds(x, y, buttonWidth, buttonHeight);

        restartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                restartButton.setIcon(hoveredIcon);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                restartButton.setIcon(normalIcon);
            }
        });

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close the GameOverWindow
                dispose();

                // Restart the game
                Main.main(null);
            }
        });

        // Add the restart button to the content pane
        getContentPane().setLayout(null);
        getContentPane().add(restartButton);

        setVisible(true);
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
        SwingUtilities.invokeLater(() -> new GameOverWindow());
    }
}
