import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class EnemyBullet {
    private int x, y, width, height, speed;
    private ImageIcon ebullet;

    public EnemyBullet(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        ebullet = new ImageIcon("assets/enemybullet.png");
    }

    public void update() {
        x += speed;
    }

    public void draw(Graphics g) {
   //     g.setColor(Color.BLACK);
        g.drawImage(ebullet.getImage(), x, y - 20, width + 20, height + 40, null);
    }

    public boolean isOffScreen() {
        return x < 0 || x > 800;  // Assuming 800 is the width of the game screen
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
