package view;

import java.awt.*;
import javax.swing.*;

public class BackgroundPanel extends JPanel {
  private Image backgroundImage;

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
