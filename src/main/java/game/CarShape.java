package game;

import java.awt.*;

public class CarShape {
    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    private Car car;



    public CarShape(Car car,int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = car.getName();
        this.car = car;
    }



    public void setX(int x) {
        this.x = x;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, width, height);
        g.setColor(Color.BLACK);
        if (name != null) {
            g.drawString(name, x, y + height + 15);
        }
    }


}

