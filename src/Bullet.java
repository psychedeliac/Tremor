import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet {
    protected int x, y, width, height, speedX, speedY;
    protected Color color;

    public Bullet(int x, int y, int width, int height, int speedX, int speedY, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speedX = speedX;
        this.speedY = speedY;
        this.color = color;
    }

    public void update() {
        x += speedX;
        y += speedY;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
