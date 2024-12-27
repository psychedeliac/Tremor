import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class PlayerBullet {
    int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private ImageIcon pbullet;

    public PlayerBullet(int x, int y, int width, int height, int speed, boolean isFacingRight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;

        // Load the appropriate image based on the direction
        if (isFacingRight) {
            pbullet = new ImageIcon("assets/pbullet.png");
        } else {
            pbullet = new ImageIcon("assets/pbullet_left.png");
        }
    }

    public void update() {
        x += speed;
    }

    public void draw(Graphics g) {
        //g.setColor(Color.BLUE);
        g.drawImage(pbullet.getImage(), x, y - 20, width + 20, height + 40, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
