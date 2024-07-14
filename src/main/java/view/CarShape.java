package view;

import game.Car;
import game.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * The type Car shape.
 */
public class CarShape {
    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    private Car car;
    private Image carImage;

    private Player player;
    private int wpm;
    private Font font;


    /**
     * Instantiates a new Car shape.
     *
     * @param car    the car
     * @param player the player
     * @param x      the x
     * @param y      the y
     * @param width  the width
     * @param height the height
     * @param font   the font
     */
    public CarShape(Car car, Player player, int x, int y, int width, int height, Font font) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = car.getName();
        this.car = car;
        this.player = player;
        this.wpm = 0;
        this.font = font;

        try {
            carImage = ImageIO.read(new File("src/main/resources/test-ufo-removebg-preview.png"));
        } catch (
                IOException e) {
            e.printStackTrace();
            carImage = null;
        }
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
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
        return y;
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
     * Sets x.
     *
     * @param x the x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Draw.
     *
     * @param g          the g
     * @param roadLength the road length
     */
    public void draw(Graphics g, int roadLength) {
        if (carImage != null) {
            g.drawImage(carImage, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height);
        }

        if (name != null) {
            g.setFont(font.deriveFont(Font.BOLD, 15));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(name, x, y + height + 15);
        }
        if (player != null) {
            g.setFont(font.deriveFont(Font.BOLD, 15));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("WPM: " + wpm, x + width + 10, y + height / 2);  // Display WPM to the right of the car
        }

        //the road (dotted line)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        float[] dash = {5f, 5f};
        BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);
        g2d.setStroke(bs);

        int startX = 0; // Starting from the left side of the panel
        int endX = roadLength; // 70% of the screen width

        g2d.drawLine(startX, y + height, endX, y + height);
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
}

