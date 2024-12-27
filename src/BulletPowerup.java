import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BulletPowerup {
    private int x, y, width, height;
    private ImageIcon pgun;

    public BulletPowerup(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        pgun = new ImageIcon("assets/pgun.png");
    }

    public void draw(Graphics g) {
        //        g.setColor(Color.GREEN);
        g.drawImage(pgun.getImage(), x, y, width + 20, height + 20, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
