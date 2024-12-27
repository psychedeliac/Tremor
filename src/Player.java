import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

public class Player implements KeyListener {
    public int x, y, width, height;
    protected int dx, dy;
    private Image playerLeft, playerRight; // Separate images for left and right
    private Image currentPlayer; // Current player image
    private boolean left, right, up, down, shooting;
    private boolean onPlatform;
    private boolean onVerticalPlatform;
    private boolean jumping;
    private boolean hasBulletPowerup;
    private ArrayList<PlayerBullet> bullets;
    private final int GAME_WIDTH = 800;
    private final int GAME_HEIGHT = 600;
    private final int START_X = 50;
    private final int START_Y = 500;
    private final int GRAVITY = 2;
    private final int TERMINAL_VELOCITY = 5;
    private final int JUMP_VELOCITY = -20;
    private long jumpCooldownTime;
    private final long JUMP_COOLDOWN = 20; // in milliseconds
    private boolean jumpRequested; // Add this boolean flag

    private Clip walkClip;
    private Clip shootClip;
    private Clip jumpClip;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 30;
        this.dx = 5;
        this.dy = 0;
        this.jumping = false;
        this.hasBulletPowerup = false;
        this.bullets = new ArrayList<>();
        this.jumpCooldownTime = 0;
        this.jumpRequested = false; // Initialize jumpRequested to false

        playerLeft = new ImageIcon("assets/player_left.png").getImage();
        playerRight = new ImageIcon("assets/player.png").getImage();
        // Initially set the player image to face right
        currentPlayer = playerRight;

        // Load sound effects
        walkClip = loadSound("assets/sounds/footsteps.wav");
        shootClip = loadSound("assets/sounds/playershooting.wav");
        jumpClip = loadSound("assets/sounds/jump.wav");

        // Set sound volume
        setVolume(walkClip, 6.0f);
        setVolume(shootClip, 6.0f);
        setVolume(jumpClip, 6.0f);
    }

    private Clip loadSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private void setVolume(Clip clip, float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume); // volume is in decibels
        }
    }

    private void jump() {
        long currentTime = System.currentTimeMillis();
        if (onPlatform && !jumping && (currentTime - jumpCooldownTime >= JUMP_COOLDOWN)) {
            dy = JUMP_VELOCITY;  // Initial jump velocity
            jumping = true;
            onPlatform = false;
            jumpCooldownTime = currentTime;
            playSound(jumpClip);
        }
    }

    public void update() {
        if (left) {
            x -= dx;
            currentPlayer = playerLeft;
            playSound(walkClip);
        }
        if (right) {
            x += dx;
            currentPlayer = playerRight;
            playSound(walkClip);
        }

        // Handle the jump request
        if (jumpRequested) {
            jump();
            jumpRequested = false; // Reset the jump request
        }

        // Apply gravity only if not on any platform
        if (!onPlatform && !onVerticalPlatform) {
            dy += GRAVITY;  // Gravity pull
            if (dy > TERMINAL_VELOCITY) dy = TERMINAL_VELOCITY;  // Terminal velocity
        }

        if (up && onVerticalPlatform) y -= dx;
        if (down && onVerticalPlatform) y += dx;

        y += dy;

        // Stop the jump when landing
        if (onPlatform || onVerticalPlatform) {
            dy = 0;
            jumping = false;
        }

        // Ensure player stays within bounds
        if (x < 0) x = 0;
        if (x + width > GAME_WIDTH) x = GAME_WIDTH - width;
        if (y < 0) y = 0;
        if (y + height > GAME_HEIGHT) {
            y = GAME_HEIGHT - height;
            onPlatform = true;  // Treat bottom edge as a platform
            dy = 0;
        }

        // Update bullets
        for (PlayerBullet bullet : bullets) {
            bullet.update();
        }

        // Remove bullets that go out of bounds
        bullets.removeIf(b -> b.x < 0 || b.x > GAME_WIDTH);
    }

    public void draw(Graphics g) {
        // Draw the current player image
        g.drawImage(currentPlayer, x, y - 30, width + 40, height + 40, null);

        // Draw bullets
        for (PlayerBullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) left = true;
        if (key == KeyEvent.VK_RIGHT) right = true;
        if (key == KeyEvent.VK_UP) up = true;
        if (key == KeyEvent.VK_DOWN) down = true;
        if (key == KeyEvent.VK_S) {
            jumpRequested = true; // Request a jump
        }
        if (key == KeyEvent.VK_A && hasBulletPowerup) {
            shoot();
            playSound(shootClip); // Play shooting sound
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) left = false;
        if (key == KeyEvent.VK_RIGHT) right = false;
        if (key == KeyEvent.VK_UP) up = false;
        if (key == KeyEvent.VK_DOWN) down = false;
        if (key == KeyEvent.VK_A) shooting = false;
    }

    private void shoot() {
        boolean isFacingRight = (currentPlayer == playerRight);  // Check current player direction
        int bulletSpeed = isFacingRight ? 10 : -10;  // Determine bullet speed based on direction
        bullets.add(new PlayerBullet(x + width / 2, y + height / 2, 10, 5, bulletSpeed, isFacingRight));
    }

    public void setOnPlatform(boolean onPlatform) {
        this.onPlatform = onPlatform;
    }

    public void setOnVerticalPlatform(boolean onVerticalPlatform) {
        this.onVerticalPlatform = onVerticalPlatform;
    }

    public void resetPosition() {
        x = START_X;
        y = START_Y;
        dx = 5;
        dy = 0;
        jumping = false;
        onPlatform = false;
        onVerticalPlatform = false;
    }

    public void resetPosition2() {
        x = START_X;
        y = 470;
        dx = 5;
        dy = 30;
        jumping = false;
        onPlatform = false;
        onVerticalPlatform = false;
    }

    public void collectBulletPowerup() {
        hasBulletPowerup = true;
    }

    public ArrayList<PlayerBullet> getBullets() {
        return bullets;
    }
}