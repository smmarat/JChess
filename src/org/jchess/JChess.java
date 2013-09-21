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
import org.jchess.model.Settings;

import javax.swing.*;

public class JChess extends JFrame {



    public JChess() {
        super("org.jchess.JChess - version 0.3 Beta");
        setSize(415, 435);
        setContentPane(new Settings(this));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String args[]) {
        new JChess();
    }

    public void begin() {
        Board board = new Board();
        setContentPane(board);
        setVisible(true);
    }

}