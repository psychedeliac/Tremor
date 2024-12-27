import javax.swing.*;
import java.awt.*;

public class InstructionsWindow extends JFrame {
    private JButton backButton;
    private ImageIcon backgroundImg;

    public InstructionsWindow() {
        setTitle("Instructions");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        backgroundImg = new ImageIcon("assets/gameinstructions.png");

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false); // Set transparent background

        backButton = createButton("Back", "assets/back_button.png", "assets/back_button_hover.png", 300, 150);
        backButton.addActionListener(e -> {
            StartMenu main = new StartMenu();
            main.setVisible(true);
            dispose();
        });

        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(panel);
        setVisible(true);
    }

    private JButton createButton(String text, String defaultImagePath, String hoverImagePath, int width, int height) {
        ImageIcon defaultIcon = new ImageIcon(new ImageIcon(defaultImagePath).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        ImageIcon hoverIcon = new ImageIcon(new ImageIcon(hoverImagePath).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));

        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    g.drawImage(hoverIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.drawImage(defaultIcon.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
                super.paintComponent(g);
            }
        };
        // Set the foreground color to transparent
        button.setForeground(new Color(0, 0, 0, 0));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InstructionsWindow::new);
    }
}
