package view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
  private final Image backgroundImage;

  public BackgroundPanel(Image image) {
    this.backgroundImage = image;
    setLayout(new BorderLayout());
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
  }
}
