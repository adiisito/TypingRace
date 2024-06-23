package view;

import javax.swing.*;
import java.awt.*;
//import javafx.application.Application;

public class GUI extends JFrame {

    public GUI() {
        LoginWindow();
    }


    // Login Window GUI
    private JTextField playerName;
    private JButton joinButton;

    public void LoginWindow() {
        setTitle("Type Racer Game");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel LoginPanel = new JPanel(new GridBagLayout());
        LoginPanel.setBackground(new Color(173, 216, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        LoginPanel.add(new JLabel("Welcome To Type Race"), gbc);
        LoginPanel.add(new JLabel("Please Enter Your Name"), gbc);

        playerName = new JTextField(20);
        LoginPanel.add(playerName, gbc);

        joinButton = new JButton("Join new Game");
        joinButton.setBackground(Color.green);
        LoginPanel.add(joinButton, gbc);

        add(LoginPanel);


    }

    public void GameWindow(){

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }


}
