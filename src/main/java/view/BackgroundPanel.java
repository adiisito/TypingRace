package view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * A custom JPanel that displays a background image stretched to fill the entire panel. This panel
 * is typically used to add an aesthetic background to a frame or another panel. The image is scaled
 * to fit the size of the panel, maintaining its aspect ratio.
 */
public class BackgroundPanel extends JPanel {
  /** The image used as the background for the panel. */
  private final Image backgroundImage;

  /**
   * Constructs a new BackgroundPanel with a specific background image.
   *
   * @param image the Image to set as background.
   */
  public BackgroundPanel(Image image) {
    this.backgroundImage = image;
    setLayout(new BorderLayout());
  }

  /**
   * Paints the background image to fit the entire panel. This method is called automatically by the
   * Swing framework whenever the panel needs redrawing, for example, when it is resized or made
   * visible.
   *
   * @param g the Graphics object to protect.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (backgroundImage != null) {
      // Draw the image scaled to fill the entire dimension of the panel.
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
  }
}
