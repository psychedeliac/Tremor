import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

public class ShootingEnemy extends Enemy {
    private ArrayList<EnemyBullet> bullets;
    private int shootingInterval;
    private int timer;
    private boolean shootLeft;
    private ImageIcon sEnemy;


    public ShootingEnemy(int x, int y, int width, int height, int shootingInterval) {
        super(x, y, width, height);
        this.shootingInterval = shootingInterval;
        this.bullets = new ArrayList<>();
        this.timer = 0;
        this.shootLeft = true; // Start by shooting to the left
        sEnemy = new ImageIcon("assets/shootingenemy.png");
    }

    @Override
    public void update() {
        if (!isDead) {
            timer++;
            if (timer >= shootingInterval) {
                shoot();
                timer = 0;
            }

            for (EnemyBullet bullet : bullets) {
                bullet.update();
            }

            bullets.removeIf(EnemyBullet::isOffScreen);
        }
    }

    private void shoot() {
        int bulletX = shootLeft ? x - 10 : x + width;
        int bulletSpeed = shootLeft ? -5 : 5;
        bullets.add(new EnemyBullet(bulletX, y + height / 2, 10, 5, bulletSpeed));
        shootLeft = !shootLeft; // Alternate shooting direction
    }

    @Override
    public void draw(Graphics g) {
        if (!isDead) {
            g.drawImage(sEnemy.getImage(), x - 5, y - 25, width + 50, height + 50, null);
            g.setColor(Color.YELLOW);
            for (EnemyBullet bullet : bullets) {
                bullet.draw(g);
            }
        }
    }

    public ArrayList<EnemyBullet> getBullets() {
        return bullets;
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
