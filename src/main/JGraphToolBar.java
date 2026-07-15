package src.main;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

public class JGraphToolBar extends JToolBar {
    public JGraphToolBar(JRootFrame rootFrame) {
        setFloatable(false);
        JButton menu = new JButton("Menu");
        menu.addActionListener(e->{
            JPopupMenu m = new JGraphPopupMenu(rootFrame);
            m.show(menu, 0, menu.getHeight());
        });
        add(menu);
        
        Method[] mMethods = { Method.QUEUE, Method.STACK, Method.NONE };
        JComboBox<Method> comboBox = new JComboBox<>(mMethods);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rootFrame.setMethod((Method) comboBox.getSelectedItem());
            }
        });
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                switch (((Method) value)) {
                    case QUEUE:
                        setText("Queue method");
                        break;
                    case STACK:
                        setText("Stack method");
                        break;
                    case NONE:
                        setText("Choose method!");
                        break;

                    default:
                        break;
                }
                return this;
            }
        });
        add(comboBox);
        // comboBox.setSelectedItem(Method.NONE);
    }
}
