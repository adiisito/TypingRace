package view;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {


    // Login Window GUI
    private JTextField playerName;
    private JButton joinBUtton;

    public void LoginWindow() {
        setTitle("Type Racer Game");
        setSize(400,400);

        JPanel LoginPanel = new JPanel(new GridLayout(3,1));
        LoginPanel.setBackground(new Color(173, 216, 230));

        playerName = new JTextField();
        playerName.setHorizontalAlignment(SwingConstants.CENTER);
        LoginPanel.add(playerName);

        joinBUtton = new JButton("Join new Game");
        joinBUtton.setBackground(Color.green);
        LoginPanel.add(joinBUtton);

    }


}
