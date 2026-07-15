package src.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileNameExtensionFilter;

import src.main.Algorithms.TopologicalSortQueue;
import src.main.Algorithms.TopologicalSortStack;

public class JGraphPopupMenu extends JPopupMenu {
    JRootFrame mRootFrame;

    public JGraphPopupMenu(JRootFrame rootFrame) {
        mRootFrame = rootFrame;
        JMenuItem open = new JMenuItem("Load graph");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                new JLoadGraph(rootFrame);
            }
        });

        JMenuItem save = new JMenuItem("Save state");
        save.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Statement Files (*.stm)", "stm"));
            int responce = fileChooser.showSaveDialog(null);
            if (responce == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                // Ensure the file path ends with the proper extension if missing
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".stm")) {
                    fileToSave = new File(filePath + ".stm");
                }
                saveState(fileToSave);
            }
        });

        JMenuItem loadState = new JMenuItem("Load state");
        loadState.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Statement Files (*.stm)", "stm"));
            int responce = fileChooser.showOpenDialog(null);
            if (responce == JFileChooser.APPROVE_OPTION) {
                readState(fileChooser.getSelectedFile());
                repaint();
            }
        });
        add(open);
        add(new JSeparator());
        add(save);
        add(loadState);
        add(new JSeparator());
        add(new JMenuItem("Exit"));
    }
    
    
    public void saveState(File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream)) {
            oos.writeObject(mRootFrame.mNodes);
            oos.writeObject(mRootFrame.mEdges);
            oos.writeObject(((JComboBox) mRootFrame.mToolBar.getComponent(1)).getSelectedItem());
            if (mRootFrame.mTopologicalSort != null) {
                oos.writeObject(mRootFrame.mTopologicalSort);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void readState(File file) {
        ArrayList<Node> buffNodes = (ArrayList<Node>) mRootFrame.mNodes.clone();
        ArrayList<Edge> buffEdges = (ArrayList<Edge>) mRootFrame.mEdges.clone();
        try (FileInputStream fileOutputStream = new FileInputStream(file);
                ObjectInputStream oos = new ObjectInputStream(fileOutputStream)) {
            mRootFrame.mNodes.clear();
            mRootFrame.mEdges.clear();
            mRootFrame.mNodes.addAll((ArrayList<Node>) oos.readObject());
            mRootFrame.mEdges.addAll(((ArrayList<Edge>) oos.readObject()));
            Method method = (Method) oos.readObject();
            ((JComboBox) mRootFrame.mToolBar.getComponent(1)).setSelectedItem(method);
            switch (method) {
                case QUEUE:
                    mRootFrame.mTopologicalSort = new TopologicalSortQueue(mRootFrame.mNodes, mRootFrame.mEdges);
                    mRootFrame.mTopologicalSort = ((TopologicalSortQueue) oos.readObject());
                    break;
                case STACK:
                    mRootFrame.mTopologicalSort = new TopologicalSortStack(mRootFrame.mNodes, mRootFrame.mEdges);
                    mRootFrame.mTopologicalSort = ((TopologicalSortStack) oos.readObject());
                    break;
                case NONE:
                    mRootFrame.mTopologicalSort = null;
                    break;
                default:
                    throw new RuntimeException("Unknown method type!!!");

            }

        } catch (Exception e) {
            System.out.println(e);
            mRootFrame.mNodes.clear();
            mRootFrame.mNodes.addAll(buffNodes);
            mRootFrame.mEdges.clear();
            mRootFrame.mEdges.addAll(buffEdges);
            JOptionPane.showMessageDialog(this, "State is loaded unsuccessfully!", "State load",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "State is loaded successfully!", "State load",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
