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

import org.jchess.model.Grid;
import org.jchess.model.Piece;

public class Pawn extends Piece {
    public static int value = 10;

    public String imgurl(boolean white) {
        if (white) {
            return "images/wpawn.png";
        } else {
            return "images/bpawn.png";
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
                    if (grid[i][j].isOwner() && i - 1 > -1) {
                        if (grid[i - 1][j].getPiece() == 100) {
                            res[i - 1][j] = true;
                        }
                        if (j + 1 < 8) {
                            if (grid[i - 1][j + 1].getPiece() != 100) {
                                if (grid[i - 1][j + 1].isOwner() != grid[i][j].isOwner()) {
                                    res[i - 1][j + 1] = true;
                                }
                            }
                        }
                        if (j - 1 > -1) {
                            if (grid[i - 1][j - 1].getPiece() != 100) {
                                if (grid[i - 1][j - 1].isOwner() != grid[i][j].isOwner()) {
                                    res[i - 1][j - 1] = true;
                                }
                            }
                        }
                    } else if ((!grid[i][j].isOwner()) && i + 1 < 8) {
                        if (grid[i + 1][j].getPiece() == 100) {
                            res[i + 1][j] = true;
                        }
                        if (j + 1 < 8) {
                            if (grid[i + 1][j + 1].getPiece() != 100) {
                                if (grid[i + 1][j + 1].isOwner() != grid[i][j].isOwner()) {
                                    res[i + 1][j + 1] = true;
                                }
                            }
                        }
                        if (j - 1 > -1) {
                            if (grid[i + 1][j - 1].getPiece() != 100) {
                                if (grid[i + 1][j - 1].isOwner() != grid[i][j].isOwner()) {
                                    res[i + 1][j - 1] = true;
                                }
                            }
                        }
                    }
                    if (grid[i][j].isOwner() && i == 6) {
                        if (grid[i - 2][j].getPiece() == 100) {
                            res[i - 2][j] = true;
                        }
                    }
                    if ((!grid[i][j].isOwner()) && i == 1) {
                        if (grid[i + 2][j].getPiece() == 100) {
                            res[i + 2][j] = true;
                        }
                    }
                }
            }
        }
        return res;
    }
}