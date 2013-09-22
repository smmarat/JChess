package org.jchess.model;

import org.jchess.JChess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Marat
 * Date: 21.09.13
 * Time: 20:37
 */
public class GameSelector extends JPanel implements ActionListener {

    private JChess main;

    public GameSelector(JChess main) {
        super(new BorderLayout());
        this.main = main;
        JLabel label = new JLabel("Select type of game:");
        JButton localButton = new JButton("Local");
        JButton clientButton = new JButton("Client");
        JButton serverButton = new JButton("Server");
        localButton.addActionListener(this);
        clientButton.addActionListener(this);
        serverButton.addActionListener(this);
        this.add(label, BorderLayout.PAGE_START);
        this.add(localButton, BorderLayout.EAST);
        this.add(clientButton, BorderLayout.CENTER);
        this.add(serverButton, BorderLayout.WEST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String where = ((JButton) e.getSource()).getText();
        if ("Local".equals(where)) {
            main.begin();
        }
        if ("Server".equals(where)) {
            main.startServer();
            main.begin();
        }
        if ("Client".equals(where)) {
            String ip = JOptionPane.showInputDialog(main, "Server ip address?");
            try {
                main.connect(ip);
                main.begin();
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(main, "Can't connect to "+ip);
            }
        }
    }
}
