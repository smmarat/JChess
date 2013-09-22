package org.jchess;/*
Copyright (C) 2012 Arvind Kumar

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation,either version 3 of the License,or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not,see <http://www.gnu.org/licenses/>

Please note that in the event that any source file or other resource in this project does not include the above header,it should be assumed to be under the same license.
*/

import org.jchess.model.Board;
import org.jchess.model.GameSelector;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class JChess extends JFrame {

    public static final int SERVER_PORT = 8989;
    private Socket socket;
    private Board board;

    public JChess() {
        super("org.jchess.JChess - version 0.3 Beta");
        setResizable(false);
        setContentPane(new GameSelector(this));
        pack();
        setCenterPosition(this);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String args[]) {
        new JChess();
    }

    public void begin() {
        setPreferredSize(new Dimension(8*Board.CELL_SIZE+6, 8*Board.CELL_SIZE+26));
        if (board==null) board = new Board(false, this);
        setContentPane(board);
        pack();
        setCenterPosition(this);
        setVisible(true);
    }

    public void startServer() {
        final JLabel label = new JLabel("Waiting for connection...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket ss = new ServerSocket(SERVER_PORT);
                    socket = ss.accept();
                    board = new Board(true, JChess.this);
                    new CommandListener(socket.getInputStream(), board).start();
                    System.err.println("incoming new connection...");
                    SwingUtilities.getWindowAncestor(label).dispose();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        JOptionPane.showMessageDialog(null, label);
    }

    public void connect(String ip) throws IOException {
        socket = new Socket(ip, SERVER_PORT);
        board = new Board(false, this);
        new CommandListener(socket.getInputStream(), board).start();
    }

    public void send(String msg) {
        if (socket!=null) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write(msg);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCenterPosition(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getPreferredSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
}