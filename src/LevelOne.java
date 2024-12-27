import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class LevelOne extends JPanel implements ActionListener {
    private Player player;
    private ArrayList<Platform> platforms;
    private ArrayList<VerticalPlatform> verticalPlatforms;
    private ArrayList<Enemy> enemies;
    private ArrayList<BulletPowerup> powerups;
    private Key key;
    private Door door;
    private Timer timer;
    private boolean levelComplete;
    private int lives;
    private JLabel livesLabel;

    private ImageIcon backgroundImage;

    public LevelOne() {
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        player = new Player(50, 500);
        addKeyListener(player);

        platforms = new ArrayList<>();
        platforms.add(new Platform(0, 550, 800, 50));
        platforms.add(new Platform(0, 400, 730, 40));
        platforms.add(new Platform(50, 250, 755, 40));
        platforms.add(new Platform(0, 100, 730, 40));

        verticalPlatforms = new ArrayList<>();
        verticalPlatforms.add(new VerticalPlatform(0, 243, 50, 177));
        verticalPlatforms.add(new VerticalPlatform(730, 390, 54, 175));
        verticalPlatforms.add(new VerticalPlatform(730, 94, 54, 175));

        enemies = new ArrayList<>();
        enemies.add(new MovingEnemy(600, 520, 30, 30, 5, 500, 670)); //bottom platform
        enemies.add(new MovingEnemy(500, 370, 30, 30, 5, 500, 670)); //second platform
        enemies.add(new MovingEnemy(500, 70, 30, 30, 5, 500, 670)); //top platform
        enemies.add(new ShootingEnemy(200, 370, 30, 30, 100)); //second platform
        enemies.add(new ShootingEnemy(300, 70, 30, 30, 50)); //top platform
        enemies.add(new ImperviousEnemy(300, 510, 20, 30)); // bottom platform
        enemies.add(new ImperviousEnemy(350, 360, 20, 30)); // second platform
        enemies.add(new ImperviousEnemy(200, 210, 20, 30)); // third platform left
        enemies.add(new ImperviousEnemy(410, 210, 20, 30)); // third platform middle
        enemies.add(new ImperviousEnemy(620, 210, 20, 30)); // third platform right
        enemies.add(new ImperviousEnemy(100, 60, 20, 30));  // top platform

        powerups = new ArrayList<>();
        powerups.add(new BulletPowerup(670, 360, 20, 20));

        key = new Key(10, 75, 20, 20);
        door = new Door(660, 450, 50, 100);
      //  key = new Key(20, 540, 20, 20);

        backgroundImage = new ImageIcon("assets/background.gif");

        timer = new Timer(25, this);
        timer.start();
        levelComplete = false;
        lives = 3;

        // Add a label to show the number of lives
        livesLabel = new JLabel("Lives: " + lives);
        livesLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        livesLabel.setForeground(Color.WHITE);

        setLayout(new BorderLayout());
        add(livesLabel, BorderLayout.NORTH);

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setOpaque(false); // Make the panel transparent
        labelPanel.add(livesLabel);
        add(labelPanel, BorderLayout.NORTH);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);

        for (Platform platform : platforms) {
            platform.draw(g);
        }
        for (VerticalPlatform verticalPlatform : verticalPlatforms) {
            verticalPlatform.draw(g);
        }
        for (Enemy enemy : enemies) {
            enemy.update();  // Update enemy position
            enemy.draw(g);
        }
        for (BulletPowerup powerup : powerups) {
            powerup.draw(g);
        }
        key.draw(g);
        door.draw(g);
        player.draw(g);
    }

    public void actionPerformed(ActionEvent e) {
        if (!levelComplete) {
            player.update();
            checkCollisions();
            repaint();
        }
    }

    public void checkCollisions() {
        boolean onAnyPlatform = false;
        boolean onAnyVerticalPlatform = false;

        for (Platform platform : platforms) {
            Rectangle playerBounds = player.getBounds();
            Rectangle platformBounds = platform.getBounds();

            if (playerBounds.intersects(platformBounds)) {
                if (playerBounds.y + playerBounds.height <= platformBounds.y + 5 && player.dy >= 0) {
                    onAnyPlatform = true;
                    player.setOnPlatform(true);
                    player.y = platformBounds.y - playerBounds.height;
                    break;
                }
            }
        }
        if (!onAnyPlatform) {
            player.setOnPlatform(false);
        }

        for (VerticalPlatform verticalPlatform : verticalPlatforms) {
            Rectangle playerBounds = player.getBounds();
            Rectangle verticalPlatformBounds = verticalPlatform.getBounds();

            if (playerBounds.intersects(verticalPlatformBounds)) {
                onAnyVerticalPlatform = true;
                player.setOnVerticalPlatform(true);
                break;
            }
        }
        if (!onAnyVerticalPlatform) {
            player.setOnVerticalPlatform(false);
        }

        enemies.removeIf(Enemy::isDead);  // Remove dead enemies

        // Check for collisions with enemies
        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                loseLife();
                break;
            }
            if (enemy instanceof ShootingEnemy) {
                ShootingEnemy shootingEnemy = (ShootingEnemy) enemy;
                for (EnemyBullet bullet : shootingEnemy.getBullets()) {
                    if (player.getBounds().intersects(bullet.getBounds())) {
                        loseLife();
                        break;
                    }
                }
            }
        }

        // Handle player bullets collision with enemies
        ArrayList<PlayerBullet> bulletsToRemove = new ArrayList<>();
        for (PlayerBullet bullet : player.getBullets()) {
            boolean bulletRemoved = false;
            for (Enemy enemy : enemies) {
                if (!enemy.isDead() && enemy.getBounds().intersects(bullet.getBounds())) {
                    enemy.checkBulletCollision(bullet);
                    bulletsToRemove.add(bullet);
                    bulletRemoved = true;
                    break;
                }
            }
            if (!bulletRemoved) {
                for (Enemy enemy : enemies) {
                    if (enemy instanceof ImperviousEnemy && enemy.getBounds().intersects(bullet.getBounds())) {
                        bulletsToRemove.add(bullet);
                        break;
                    }
                }
            }
        }
        player.getBullets().removeAll(bulletsToRemove);

        // Check for bullet powerup collection
        for (BulletPowerup powerup : powerups) {
            if (player.getBounds().intersects(powerup.getBounds())) {
                player.collectBulletPowerup();
                powerups.remove(powerup);
                break;
            }
        }

        // Check for key collection
        if (player.getBounds().intersects(key.getBounds())) {
            key.collect();
        }

        // Check if player reaches door with the key
        if (key.isCollected() && player.getBounds().intersects(door.getBounds())) {
            levelComplete = true;
            SwingUtilities.getWindowAncestor(this).dispose(); // Close the current window
            new LevelCompleteWindow();
        }

        // Remove dead enemies
        enemies.removeIf(Enemy::isDead);
    }


    private void loseLife() {
        lives--;
        livesLabel.setText("Lives: " + lives);
        try {
            File soundFile = new File("assets/sounds/lifedecrement.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lives <= 0) {
            timer.stop();
            new GameOverWindow();
            SwingUtilities.getWindowAncestor(this).dispose(); // Close the level window
        } else {
            player.resetPosition();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Level One");
        LevelOne levelOne = new LevelOne();
        frame.add(levelOne);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
