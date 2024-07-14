package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.client.ClientController;

public class SettingsWindow extends JFrame {
    private GUI mainGui;

    public SettingsWindow(GUI mainGui) {
        this.mainGui = mainGui;
        initComponents();
    }

    private void initComponents() {
        setTitle("Settings");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Set background image
        setContentPane(new JLabel(new ImageIcon(getClass().getResource("/1screen4.gif"))));
        setLayout(new GridBagLayout());

        // Sound setting
        JLabel soundLabel = new JLabel("Sound:");
        soundLabel.setFont(mainGui.dozerFont);
        soundLabel.setForeground(Color.GREEN);
        JToggleButton soundToggle = new JToggleButton("On");
        soundToggle.setSelected(mainGui.getClientController().isSoundEnabled());
        soundToggle.setFont(mainGui.dozerFont);
        soundToggle.addActionListener(e -> {
            mainGui.getClientController().setSoundEnabled(soundToggle.isSelected());
            soundToggle.setText(soundToggle.isSelected() ? "On" : "Off");
        });
        add(soundLabel, gbc);
        add(soundToggle, gbc);

        // Text type setting
        JLabel textTypeLabel = new JLabel("Choose Text Type:");
        textTypeLabel.setFont(mainGui.dozerFont);
        textTypeLabel.setForeground(Color.GREEN);
        add(textTypeLabel, gbc);

        String[] textTypes = {"Random", "English", "German", "Dad Jokes", "Facts"};
        JComboBox<String> textTypeComboBox = new JComboBox<>(textTypes);
        textTypeComboBox.setFont(mainGui.dozerFont);
        if(mainGui.getClientController().isHost(mainGui.getClientController().getCurrentPlayerName())) {
            textTypeComboBox.setSelectedItem(mainGui.getClientController().getTextType());
            textTypeComboBox.addActionListener(e -> {
                mainGui.getClientController().setTextType((String) textTypeComboBox.getSelectedItem());
            });
        } else {
            textTypeComboBox.setBackground(Color.BLACK);
            textTypeComboBox.setForeground(Color.DARK_GRAY);
        }
        add(textTypeComboBox, gbc);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.setFont(mainGui.dozerFont);
        saveButton.addActionListener(e -> {
            mainGui.getClientController().setSoundEnabled(soundToggle.isSelected());
            mainGui.getClientController().setTextType((String) textTypeComboBox.getSelectedItem());
            this.dispose();
        });
        add(saveButton, gbc);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
