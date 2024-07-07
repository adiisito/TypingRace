package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Custom TableCellRenderer for highlighting rows based on a player's name.
 * This renderer sets the background color to gray for rows where the player's
 * name matches the specified name; otherwise, it sets the background color to white.
 * The foreground color is set to black in all cases.
 */
public class HighlightRenderer extends DefaultTableCellRenderer {

    private String playerName;

    /**
     * Constructs a HighlightRenderer with the specified player name.
     *
     * @param playerName The name of the player to highlight in the table.
     */
    public HighlightRenderer(String playerName) {
        this.playerName = playerName;
        setOpaque(true);
    }

    /**
     * Returns the component used for drawing the cell. This method sets the
     * background color to gray if the player name matches the specified name;
     * otherwise, it sets the background color to white. The foreground color is
     * always set to black.
     *
     * @param table      the JTable that is asking the renderer to draw; can be null
     * @param value      the value of the cell to be rendered.
     * @param isSelected true if the cell is to be rendered with the selection highlighted;
     *                   otherwise false
     * @param hasFocus   if true, render cell appropriately.
     * @param row        the row index of the cell being drawn.
     * @param column     the column index of the cell being drawn
     * @return the component used for drawing the cell
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (table.getModel().getValueAt(row, 1).equals(playerName)) {
            setForeground(Color.YELLOW);
        } else {
            setForeground(Color.WHITE);
        }
            setBackground(Color.BLACK);
            setFont(new Font("Consolas", Font.BOLD, 14));
        return this;
    }


}
