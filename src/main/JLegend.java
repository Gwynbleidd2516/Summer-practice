package src.main;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JLegend extends JPanel {
    public JLegend() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Legend")); // Border titled "Legend"

        // Add legend items
        setPreferredSize(new Dimension(150, 0));
        add(createLegendItem(Color.BLACK, "Обработано"));
        add(createLegendItem(Color.GRAY, "В обработке"));
        add(createLegendItem(Color.WHITE, "Ещё не тронутые"));
        setVisible(true);
    }

    private static JPanel createLegendItem(Color color, String text) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create a 16x16 colored square icon
        Icon colorIcon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillRect(x, y, getIconWidth(), getIconHeight());
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };

        // Combine icon and text into a JLabel
        JLabel label = new JLabel(text, colorIcon, JLabel.LEFT);
        itemPanel.add(label);
        return itemPanel;
    }

    public static void main(String[] args) {
        new JLegend();
    }
}
