import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class LevelTwo extends JPanel implements ActionListener {
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

    public LevelTwo() {
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        player = new Player(50, 470);
        addKeyListener(player);

        platforms = new ArrayList<>();
        platforms.add(new Platform(0, 510, 800, 50, new ImageIcon("assets/bottom.png")));
        platforms.add(new Platform(400, 360, 200, 40, new ImageIcon("assets/check.png")));
        platforms.add(new Platform(100, 360, 180, 40, new ImageIcon("assets/check.png")));
        platforms.add(new Platform(700, 360, 80, 40, new ImageIcon("assets/check.png")));
        platforms.add(new Platform(50, 200, 90, 40, new ImageIcon("assets/check.png")));
        platforms.add(new Platform(250, 200, 250, 40, new ImageIcon("assets/check.png")));
        platforms.add(new Platform(545, 160, 80, 40, new ImageIcon("assets/check.png")));
        platforms.add(new Platform(380, 80, 140, 40, new ImageIcon("assets/check.png")));


        verticalPlatforms = new ArrayList<>();
        verticalPlatforms.add(new VerticalPlatform(0, 160, 40, 160));
        verticalPlatforms.add(new VerticalPlatform(550, 360, 54, 175));

        enemies = new ArrayList<>();
        enemies.add(new MovingEnemy(730, 340, 30, 30, 5, 700, 770)); //second platform
        enemies.add(new MovingEnemy(560, 140, 30, 30, 5, 540, 610));
        enemies.add(new ShootingEnemy(380, 170, 30, 30, 100)); //second platform
        enemies.add(new ShootingEnemy(460, 50, 30, 30, 25)); //top platform
        enemies.add(new ImperviousEnemy(480, 330, 20, 30)); // second platform
        enemies.add(new ImperviousEnemy(200, 330, 20, 30)); // second platform
        enemies.add(new ImperviousEnemy(290, 170, 20, 30)); // third platform middle

        powerups = new ArrayList<>();
        powerups.add(new BulletPowerup(100, 330, 20, 20));

        key = new Key(730, 340, 20, 20);
        door = new Door(400, -20, 50, 100);
        //key = new Key(300, 500, 20, 20);
        //door = new Door(400, 450, 50, 100);


        backgroundImage = new ImageIcon("assets/l2back.gif");

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
            if (player.getBounds().intersects(platform.getBounds())) {
                if (player.getBounds().y + player.getBounds().height <= platform.getBounds().y + 5) {
                    onAnyPlatform = true;
                    player.setOnPlatform(true);
                    player.y = platform.getBounds().y - player.getBounds().height;
                    break;
                }
            }
        }
        if (!onAnyPlatform) {
            player.setOnPlatform(false);
        }

        for (VerticalPlatform verticalPlatform : verticalPlatforms) {
            if (player.getBounds().intersects(verticalPlatform.getBounds())) {
                onAnyVerticalPlatform = true;
                player.setOnVerticalPlatform(true);
                break;
            }
        }
        if (!onAnyVerticalPlatform) {
            player.setOnVerticalPlatform(false);
        }

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
            new GameCompleteWindow();
            SwingUtilities.getWindowAncestor(this).dispose(); // Close the Level Two window
        }

        enemies.removeIf(Enemy::isDead);  // Remove dead enemies
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
        if (lives <= 0) {
            timer.stop();
            new GameOverWindow();
            SwingUtilities.getWindowAncestor(this).dispose(); // Close the level window
        } else {
            player.resetPosition2();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Level Two");
        LevelTwo levelTwo = new LevelTwo();
        frame.add(levelTwo);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}