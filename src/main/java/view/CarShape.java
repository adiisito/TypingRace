package view;

import game.Car;
import game.Player;

import java.awt.*;

public class CarShape {
    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    private Car car;

    private Player player;



    public CarShape(Car car, Player player, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = car.getName();
        this.car = car;
        this.player = player;
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

    public void draw(Graphics g, int roadLength) {
        g.setColor(Color.RED);
        g.fillOval(x, y, width, height);
        g.setColor(Color.WHITE);
        if (name != null) {
            g.drawString(name, x, y + height + 15);
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

