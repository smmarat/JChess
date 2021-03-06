package org.jchess;

import org.jchess.model.Board;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Marat
 * Date: 22.09.13
 * Time: 10:34
 */
public class CommandListener extends Thread {

    private BufferedReader in;
    private Board board;

    public CommandListener(InputStream in, Board board) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.board = board;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String command = in.readLine();
                System.err.println("receive: "+command);
                if (command!=null) {
                    if (command.contains("~")) {
                        try {
                            String[] point = command.trim().split("~");
                            board.touch(Integer.parseInt(point[0]), Integer.parseInt(point[1]));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connection lost!");
        }
    }
}
