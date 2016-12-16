package client.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Small class used to render the messages in the message list
 *
 * @author Tristan Bourvon
 * @author Marc-Antoine FERNANDES
 * @version 1.0.0
 */
public class MyListCellRenderer extends JLabel implements ListCellRenderer<Object> {

    /**
     * Constructor initializing the look and feel of messages
     */
    public MyListCellRenderer() {
        setFont(new Font("Sans Serif", Font.PLAIN, 12));
    }

    /**
     * Called when a new message is added to the model, sets the text of the JLabel
     *
     * @param list List of messages
     * @param value Message to be added
     * @param index unused
     * @param isSelected unused
     * @param cellHasFocus unused
     * @return the cell renderer
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.toString());
        return this;
    }
}