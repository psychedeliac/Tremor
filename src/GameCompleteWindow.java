import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class GameCompleteWindow extends JFrame {

    private Clip gameOverClip;
    public GameCompleteWindow() {
        setTitle("Game Complete");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon backgroundImage = new ImageIcon("assets/gamecompleted.jpeg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        setContentPane(backgroundLabel);

        playGameOverSound("assets/sounds/gameover.wav");


        ImageIcon normalIcon = new ImageIcon("assets/mm.png");
        ImageIcon hoveredIcon = new ImageIcon("assets/mmh.png");

        JButton mmButton = new JButton(normalIcon);
        mmButton.setOpaque(false);
        mmButton.setContentAreaFilled(false);
        mmButton.setBorderPainted(false);
        mmButton.setFocusPainted(false);

        int buttonWidth = normalIcon.getIconWidth();
        int buttonHeight = normalIcon.getIconHeight();
        int x = (getWidth() - buttonWidth) / 2;
        int y = (getHeight() - buttonHeight) / 2 + 200;

        mmButton.setBounds(x, y, buttonWidth, buttonHeight);

        mmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mmButton.setIcon(hoveredIcon);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                mmButton.setIcon(normalIcon);
            }
        });

        mmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Main.main(null);
            }
        });

        getContentPane().setLayout(null);
        getContentPane().add(mmButton);

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
