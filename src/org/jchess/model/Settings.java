package org.jchess.model;

import org.jchess.JChess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Marat
 * Date: 21.09.13
 * Time: 20:37
 */
public class Settings extends JPanel implements ActionListener {

    private JChess main;

    public Settings(JChess main) {
        super(new BorderLayout());
        this.main = main;
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        this.add(nextButton,BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Next".equals(((JButton) e.getSource()).getText())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    main.begin();
                }
            }).start();
        }
    }
}
