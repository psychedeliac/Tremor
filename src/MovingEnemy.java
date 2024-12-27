import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.io.File;

public class MovingEnemy extends Enemy {
    private int speed;
    private int leftBound, rightBound;
    private boolean movingRight;
    private ImageIcon mEnemyRight, mEnemyLeft, activeMEnemy;

    public MovingEnemy(int x, int y, int width, int height, int speed, int leftBound, int rightBound) {
        super(x, y, width, height);
        this.speed = speed;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.movingRight = true;
        mEnemyLeft = new ImageIcon("assets/mEnemyLeft.png");
        mEnemyRight = new ImageIcon("assets/mEnemyRight.png");
    }

    @Override
    public void update() {
        if (movingRight) {
            x += speed;
            if (x + width > rightBound) {
                movingRight = false;
            }
        } else {
            x -= speed;
            if (x < leftBound) {
                movingRight = true;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (movingRight) {
            g.drawImage(mEnemyRight.getImage(), x, y - 20, width + 30, height + 30, null);
        } else {
            g.drawImage(mEnemyLeft.getImage(), x, y - 20, width + 30, height + 30, null);
        }
    }

    @Override
    public void checkBulletCollision(PlayerBullet bullet) {
        super.checkBulletCollision(bullet);
        try {
            File soundFile = new File("assets/sounds/snowballimpact.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
