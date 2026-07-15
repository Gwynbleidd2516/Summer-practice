package src.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.Timer;

public class JPlayer extends JToolBar {
    private JButton stepB;
    private JButton stepS;
    private JButton stepF;
    private Timer mPlayertimer;
    private JLabel mAnswear;

    public JPlayer(JRootFrame rootFrame) {
        setFloatable(false);

        stepB = new JButton("<");
        stepB.addActionListener(e -> {
            if (!rootFrame.mTopologicalSort.isBegin()) {
                rootFrame.mTopologicalSort.stepBack();
            }
            rootFrame.mGraph.repaint();
        });
        add(stepB);

        stepS = new JButton("Stop");
        mPlayertimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!rootFrame.mTopologicalSort.isEnd())
                    rootFrame.mTopologicalSort.stepForward();

                rootFrame.repaint();
            }
        });
        stepS.addActionListener(e -> {
            if (mPlayertimer.isRunning()) {
                stepS.setText("Stop");
                mPlayertimer.stop();
            } else {
                stepS.setText("Resume");
                mPlayertimer.start();
            }
        });
        add(stepS);
        stepF = new JButton(">");
        stepF.addActionListener(e -> {
            if (!rootFrame.mTopologicalSort.isEnd()) {
                rootFrame.mTopologicalSort.stepForward();
            }
            rootFrame.repaint();
        });
        add(stepF);

        mAnswear = new JLabel("Result: ");
        add(mAnswear);
    }

    public void enable() {
        stepB.setEnabled(true);
        stepF.setEnabled(true);
        stepS.setEnabled(true);
    }

    public void disable() {
        stepB.setEnabled(false);
        stepF.setEnabled(false);
        stepS.setEnabled(false);
    }

    public void setResultText(String text) {
        mAnswear.setText(text);
    }
}
