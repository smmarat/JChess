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

public class King extends Piece {
    public static int value = 0;
    public static int SIZE = Board.CELL_SIZE*2;

    public String imgurl(boolean white) {
        if (white) {
            return "images/wking.png";
        } else {
            return "images/bking.png";
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
                    if (i + 1 < 8) {
                        if (grid[i + 1][j].getPiece() == SIZE) {
                            res[i + 1][j] = true;
                        } else if (grid[i + 1][j].isOwner() != grid[i][j].isOwner()) {
                            res[i + 1][j] = true;
                        }
                    }
                    if (i - 1 > -1) {
                        if (grid[i - 1][j].getPiece() == SIZE) {
                            res[i - 1][j] = true;
                        } else if (grid[i - 1][j].isOwner() != grid[i][j].isOwner()) {
                            res[i - 1][j] = true;
                        }
                    }
                    if (j + 1 < 8) {
                        if (grid[i][j + 1].getPiece() == SIZE) {
                            res[i][j + 1] = true;
                        } else if (grid[i][j + 1].isOwner() != grid[i][j].isOwner()) {
                            res[i][j + 1] = true;
                        }
                    }
                    if (j - 1 > -1) {
                        if (grid[i][j - 1].getPiece() == SIZE) {
                            res[i][j - 1] = true;
                        } else if (grid[i][j - 1].isOwner() != grid[i][j].isOwner()) {
                            res[i][j - 1] = true;
                        }
                    }
                    if (i + 1 < 8 && j + 1 < 8) {
                        if (grid[i + 1][j + 1].getPiece() == SIZE) {
                            res[i + 1][j + 1] = true;
                        } else if (grid[i + 1][j + 1].isOwner() != grid[i][j].isOwner()) {
                            res[i + 1][j + 1] = true;
                        }
                    }
                    if (i + 1 < 8 && j - 1 > -1) {
                        if (grid[i + 1][j - 1].getPiece() == SIZE) {
                            res[i + 1][j - 1] = true;
                        } else if (grid[i + 1][j - 1].isOwner() != grid[i][j].isOwner()) {
                            res[i + 1][j - 1] = true;
                        }
                    }
                    if (i - 1 > -1 && j - 1 > -1) {
                        if (grid[i - 1][j - 1].getPiece() == SIZE) {
                            res[i - 1][j - 1] = true;
                        } else if (grid[i - 1][j - 1].isOwner() != grid[i][j].isOwner()) {
                            res[i - 1][j - 1] = true;
                        }
                    }
                    if (i - 1 > -1 && j + 1 < 8) {
                        if (grid[i - 1][j + 1].getPiece() == SIZE) {
                            res[i - 1][j + 1] = true;
                        } else if (grid[i - 1][j + 1].isOwner() != grid[i][j].isOwner()) {
                            res[i - 1][j + 1] = true;
                        }
                    }
                }
            }
        }
        return res;
    }
}