import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Door {
    private int x, y, width, height;
    private ImageIcon door;

    public Door(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        door = new ImageIcon("assets/door.png");
    }

    public void draw(Graphics g) {
       // g.setColor(Color.cyan);
        g.drawImage(door.getImage(), x - 5, y - 25, width + 50, height + 50, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
