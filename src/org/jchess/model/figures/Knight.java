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

public class Knight extends Piece {
    public static int value = 30;
    public static int SIZE = Board.CELL_SIZE*2;

    public String imgurl(boolean white) {
        if (white) {
            return "images/wknight.png";
        } else {
            return "images/bknight.png";
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
                    if (i + 2 < 8 && j + 1 < 8) {
                        if (grid[i + 2][j + 1].getPiece() == SIZE) {
                            res[i + 2][j + 1] = true;
                        } else if (grid[i + 2][j + 1].isOwner() != grid[i][j].isOwner()) {
                            res[i + 2][j + 1] = true;
                        }
                    }
                    if (i + 2 < 8 && j - 1 > -1) {
                        if (grid[i + 2][j - 1].getPiece() == SIZE) {
                            res[i + 2][j - 1] = true;
                        } else if (grid[i + 2][j - 1].isOwner() != grid[i][j].isOwner()) {
                            res[i + 2][j - 1] = true;
                        }
                    }
                    if (i - 2 > -1 && j + 1 < 8) {
                        if (grid[i - 2][j + 1].getPiece() == SIZE) {
                            res[i - 2][j + 1] = true;
                        } else if (grid[i - 2][j + 1].isOwner() != grid[i][j].isOwner()) {
                            res[i - 2][j + 1] = true;
                        }
                    }
                    if (i - 2 > -1 && j - 1 > -1) {
                        if (grid[i - 2][j - 1].getPiece() == SIZE) {
                            res[i - 2][j - 1] = true;
                        } else if (grid[i - 2][j - 1].isOwner() != grid[i][j].isOwner()) {
                            res[i - 2][j - 1] = true;
                        }
                    }
                    if (i + 1 < 8 && j + 2 < 8) {
                        if (grid[i + 1][j + 2].getPiece() == SIZE) {
                            res[i + 1][j + 2] = true;
                        } else if (grid[i + 1][j + 2].isOwner() != grid[i][j].isOwner()) {
                            res[i + 1][j + 2] = true;
                        }
                    }
                    if (i + 1 < 8 && j - 2 > -1) {
                        if (grid[i + 1][j - 2].getPiece() == SIZE) {
                            res[i + 1][j - 2] = true;
                        } else if (grid[i + 1][j - 2].isOwner() != grid[i][j].isOwner()) {
                            res[i + 1][j - 2] = true;
                        }
                    }
                    if (i - 1 > -1 && j + 2 < 8) {
                        if (grid[i - 1][j + 2].getPiece() == SIZE) {
                            res[i - 1][j + 2] = true;
                        } else if (grid[i - 1][j + 2].isOwner() != grid[i][j].isOwner()) {
                            res[i - 1][j + 2] = true;
                        }
                    }
                    if (i - 1 > -1 && j - 2 > -1) {
                        if (grid[i - 1][j - 2].getPiece() == SIZE) {
                            res[i - 1][j - 2] = true;
                        } else if (grid[i - 1][j - 2].isOwner() != grid[i][j].isOwner()) {
                            res[i - 1][j - 2] = true;
                        }
                    }
                }
            }
        }
        return res;
    }
}