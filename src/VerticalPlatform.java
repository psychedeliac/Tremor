import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class VerticalPlatform {
    public int x, y, width, height;
    public ImageIcon vPlatformImage;

    public VerticalPlatform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // Load the platform image
        vPlatformImage = new ImageIcon("assets/vplatform.png");
        // Resize the platform image to match the specified dimensions
        vPlatformImage = new ImageIcon(vPlatformImage.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
    }

    public void draw(Graphics g) {
     //   g.setColor(Color.DARK_GRAY);
        g.drawImage(vPlatformImage.getImage(), x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 5, y, width - 30, height);
    }
}
