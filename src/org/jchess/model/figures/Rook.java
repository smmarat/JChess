package org.jchess.model.figures;/*
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
import org.jchess.model.Grid;
import org.jchess.model.Piece;

public class Rook extends Piece {
    public static int value = 40;
    public static int SIZE = Board.CELL_SIZE*2;

    public String imgurl(boolean white) {
        if (white) {
            return "images/wrook.png";
        } else {
            return "images/brook.png";
        }
    }

    public boolean[][] placeMoves(Grid[][] grid) {
        boolean[][] res = new boolean[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                res[i][j] = false;
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j].isSelected()) {
                    for (int ii = i + 1; ii < 8; ii++) {
                        if (grid[ii][j].getPiece() == SIZE) {
                            res[ii][j] = true;
                        }
                        if (grid[ii][j].getPiece() != SIZE && (grid[ii][j].isOwner() == grid[i][j].isOwner())) {
                            break;
                        }
                        res[ii][j] = true;
                        if (grid[ii][j].getPiece() != SIZE && (grid[ii][j].isOwner() != grid[i][j].isOwner())) {
                            break;
                        }
                    }
                    for (int ii = i - 1; ii > -1; ii--) {
                        if (grid[ii][j].getPiece() == SIZE) {
                            res[ii][j] = true;
                        }
                        if (grid[ii][j].getPiece() != SIZE && (grid[ii][j].isOwner() == grid[i][j].isOwner())) {
                            break;
                        }
                        res[ii][j] = true;
                        if (grid[ii][j].getPiece() != SIZE && (grid[ii][j].isOwner() != grid[i][j].isOwner())) {
                            break;
                        }
                    }
                    for (int jj = j + 1; jj < 8; jj++) {
                        if (grid[i][jj].getPiece() == SIZE) {
                            res[i][jj] = true;
                        }
                        if (grid[i][jj].getPiece() != SIZE && (grid[i][jj].isOwner() == grid[i][j].isOwner())) {
                            break;
                        }
                        res[i][jj] = true;
                        if (grid[i][jj].getPiece() != SIZE && (grid[i][jj].isOwner() != grid[i][j].isOwner())) {
                            break;
                        }
                    }
                    for (int jj = j - 1; jj > -1; jj--) {
                        if (grid[i][jj].getPiece() == SIZE) {
                            res[i][jj] = true;
                        }
                        if (grid[i][jj].getPiece() != SIZE && (grid[i][jj].isOwner() == grid[i][j].isOwner())) {
                            break;
                        }
                        res[i][jj] = true;
                        if (grid[i][jj].getPiece() != SIZE && (grid[i][jj].isOwner() != grid[i][j].isOwner())) {
                            break;
                        }
                    }
                }
            }
        }
        return res;
    }
}