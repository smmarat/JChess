package org.jchess.model;
/*
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

public class Grid {
    int piece = Board.CELL_SIZE*2;
    int x;
    int y;
    boolean selected = false;
    boolean owner = false;
    boolean moveallowed = false;

    public Grid(int gx, int gy) {
        x = gx;
        y = gy;
    }

    public void changePiece(int gp) {
        piece = gp;
    }

    public void changeOwner(boolean gb) {
        owner = gb;
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    public void moveallow() {
        moveallowed = true;
    }

    public void moveunallow() {
        moveallowed = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getPiece() {
        return piece;
    }

    public boolean isOwner() {
        return owner;
    }

    public boolean getOwner() {
        return owner;
    }
}