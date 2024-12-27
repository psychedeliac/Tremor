import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Main {
    private static JFrame frame;
    private static Clip backgroundMusic;

    public static void main(String[] args) {
        frame = new JFrame("Tremor");
        StartMenu startMenu = new StartMenu();
        frame.add(startMenu);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // This line centers the window on the screen
        frame.setVisible(true);
        playBackgroundMusic("assets/sounds/music1.wav"); // Path to your background music file
    }

    public static void startLevelOne() {
        frame.getContentPane().removeAll();
        LevelOne level = new LevelOne();
        frame.add(level);
        frame.revalidate();
        level.requestFocusInWindow();
    }

    private static void playBackgroundMusic(String filePath) {
        try {
            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioInput);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusic.start();
            } else {
                System.out.println("Can't find file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
