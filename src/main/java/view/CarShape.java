package view;

import game.Car;
import game.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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
    private int progress;
    private Font font;

    public CarShape(Car car, Player player, int x, int y, int width, int height, Font font) {
        this.x = x;
        this.y = y;
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

    public void setProgress(int progress) {
        this.progress = progress;
    }

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
            g.drawString(name, x, y + height + 1);
        }
        if (player != null) {
            g.setFont(font.deriveFont(Font.BOLD, 15));
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("WPM: " + wpm, x + width + 5, y + height / 2);  // Display WPM to the right of the car
        }

        // Draw progress bar below the car shape
        int progressBarY = y + height + 7; // Position it right below the car
        g.setColor(Color.GREEN);
        int progressBarWidth = (int) ((progress / 100.0) * roadLength);
        g.fillRect(0, progressBarY, progressBarWidth, 10); // Start at fixed x position

        // Draw progress percentage
        g.setColor(Color.WHITE);
        g.setFont(font.deriveFont(Font.PLAIN, 12));
        g.drawString(progress + "%", progressBarWidth + 5, progressBarY + 10);
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getWpm() {
        return wpm;
    }

    public void setWpm(int wpm) {
        this.wpm = wpm;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getWidth() {
        return width;
    }
}
