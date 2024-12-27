import java.awt.*;
import javax.swing.*;

public class Platform {
    public int x, y, width, height;
    public ImageIcon platformImage;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.platformImage = new ImageIcon("assets/platform.jpg");
    }

    public Platform(int x, int y, int width, int height, ImageIcon image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.platformImage = image;
    }

    public void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        //g.fillRect(x, y, width, height);
        g.drawImage(platformImage.getImage(), x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 5, y, width - 30, height);
    }
}
