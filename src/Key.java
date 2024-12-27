import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Key {
    private int x, y, width, height;
    private boolean collected;

    private ImageIcon keyImage;

    public Key(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.collected = false;
        keyImage = new ImageIcon("assets/key.png");
    }

    public void draw(Graphics g) {
        if (!collected) {
        //    g.setColor(Color.ORANGE);
            g.drawImage(keyImage.getImage(), x, y - 15 , width + 20, height + 20, null);
        }
    }

    public void collect() {
        this.collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
