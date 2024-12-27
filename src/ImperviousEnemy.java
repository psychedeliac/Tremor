import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Timer;

public class ImperviousEnemy extends Enemy {
    private ArrayList<BufferedImage> frames;
    private int currentFrameIndex;
    private Timer animationTimer;

    public ImperviousEnemy(int x, int y, int width, int height) {
        super(x, y, width, height);
        frames = new ArrayList<>();
        loadGif("assets/spike.gif");
        currentFrameIndex = 0;

        int animationSpeed = 100; //animation speed
        animationTimer = new Timer(animationSpeed, e -> updateFrame());
        animationTimer.start();
    }

    private void loadGif(String filePath) {
        try {
            ImageInputStream input = ImageIO.createImageInputStream(new File(filePath));
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(input);
                int numFrames = reader.getNumImages(true);
                for (int i = 0; i < numFrames; i++) {
                    frames.add(reader.read(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFrame() {
        currentFrameIndex = (currentFrameIndex + 1) % frames.size();
    }

    @Override
    public void draw(Graphics g) {
        if (!isDead && !frames.isEmpty()) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(frames.get(currentFrameIndex), x, y - 35, width + 40, height + 60, null);
            g2d.dispose();
        }
    }


    @Override
    public void checkBulletCollision(PlayerBullet bullet) {
    }

    public static void main(String[] args) {
        // Example JFrame to test the drawing
        JFrame frame = new JFrame("Test ImperviousEnemy");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            ImperviousEnemy enemy = new ImperviousEnemy(100, 100, 50, 50);

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                enemy.draw(g);
            }
        };

        frame.add(panel);
        frame.setVisible(true);

        // animation loop
        Timer timer = new Timer(1000 / 60, e -> panel.repaint());
        timer.start();
    }
}
