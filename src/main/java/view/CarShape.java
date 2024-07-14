package view;

import game.Car;
import game.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/** The type Car shape. */
public class CarShape {
  private final int vertical;
  private final int width;
  private final int height;
  private final String name;
  private final Car car;
  private int horizontal;
  private Image carImage;

  private Player player;
  private int wpm;
  private int progress;
  private Font font;

  /**
   * Instantiates a new Car shape.
   *
   * @param car the car
   * @param player the player
   * @param horizontal the x
   * @param vertical the y
   * @param width the width
   * @param height the height
   * @param font the font
   */
  public CarShape(
      Car car, Player player, int horizontal, int vertical, int width, int height, Font font) {
    this.horizontal = horizontal;
    this.vertical = vertical;
    this.width = width;
    this.height = height;
    this.name = car.getName();
    this.car = car;
    this.player = player;
    this.wpm = 0;
    this.progress = 0;
    this.font = font;

    try {
      carImage = ImageIO.read(new File("src/main/resources/test-ufo-removebg-preview.png"));
    } catch (IOException e) {
      e.printStackTrace();
      carImage = null;
    }
  }

  /**
   * Draws a car for the specified player.
   *
   * @param g the Graphics object
   * @param roadLength the road length
   */
  public void draw(Graphics g, int roadLength) {
    if (carImage != null) {
      g.drawImage(carImage, horizontal, vertical, width, height, null);
    } else {
      g.setColor(Color.RED);
      g.fillOval(horizontal, vertical, width, height);
    }

    if (name != null) {
      g.setFont(font.deriveFont(Font.BOLD, 15));
      g.setColor(Color.LIGHT_GRAY);
      g.drawString(name, horizontal, vertical + height + 1);
    }
    if (player != null) {
      g.setFont(font.deriveFont(Font.BOLD, 15));
      g.setColor(Color.LIGHT_GRAY);
      g.drawString(
          "WPM: " + wpm,
          horizontal + width + 5,
          vertical + height / 2); // Display WPM to the right of the car
    }

    // Draw progress bar below the car shape
    int progressBarY = vertical + height + 7; // Position it right below the car
    g.setColor(Color.GREEN);
    int progressBarWidth = (int) ((progress / 100.0) * roadLength);
    g.fillRect(0, progressBarY, progressBarWidth, 10); // Start at fixed x position

    // Draw progress percentage
    g.setColor(Color.WHITE);
    g.setFont(font.deriveFont(Font.PLAIN, 12));
    g.drawString(progress + "%", progressBarWidth + 5, progressBarY + 10);
  }

  /**
   * Gets wpm.
   *
   * @return the wpm
   */
  public int getWpm() {
    return wpm;
  }

  /**
   * Sets wpm.
   *
   * @param wpm the wpm
   */
  public void setWpm(int wpm) {
    this.wpm = wpm;
  }

  /**
   * Gets player.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Sets player.
   *
   * @param player the player
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Sets the current progress of the car's player.
   *
   * @param progress the new progress status
   */
  public void setProgress(int progress) {
    this.progress = progress;
  }

  /**
   * Sets font.
   *
   * @param font the font
   */
  public void setFont(Font font) {
    this.font = font;
  }

  /**
   * Gets the horizontal position.
   *
   * @return the car's horizontal position
   */
  public int getHorizontal() {
    return horizontal;
  }

  /**
   * Sets the horizontal position.
   *
   * @param horizontal the new horizontal position
   */
  public void setHorizontal(int horizontal) {
    this.horizontal = horizontal;
  }

  /**
   * Gets the vertical position.
   *
   * @return the car's vertical position
   */
  public int getVertical() {
    return vertical;
  }

  /**
   * Gets height.
   *
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Gets the width.
   *
   * @return the car's width
   */
  public int getWidth() {
    return width;
  }
}
