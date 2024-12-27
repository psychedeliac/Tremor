import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy {
    protected int x, y, width, height;
    protected boolean isDead;

    public Enemy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isDead = false;
    }

    public void draw(Graphics g) {
        if (!isDead) {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void update() {
        // Default update behavior
    }

    public void checkBulletCollision(PlayerBullet bullet) {
        if (getBounds().intersects(bullet.getBounds())) {
            isDead = true;
        }
    }

    public boolean isDead() {
        return isDead;
    }
}
