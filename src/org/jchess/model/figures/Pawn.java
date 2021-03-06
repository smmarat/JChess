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

public class Pawn extends Piece {
    public static int value = 10;
    public static int SIZE = Board.CELL_SIZE*2;
    private boolean reverse = false;

    public Pawn(boolean reverse) {
        this.reverse = reverse;
    }

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
                    int nextI = reverse ? (grid[i][j].isOwner() ? i+1 : i-1) : (grid[i][j].isOwner() ? i-1 : i+1);
                    if (grid[i][j].isOwner() && nextI > -1) {
                        if (grid[nextI][j].getPiece() == SIZE) {
                            res[nextI][j] = true;
                        }
                        if (j + 1 < 8) {
                            if (grid[nextI][j + 1].getPiece() != SIZE) {
                                if (grid[nextI][j + 1].isOwner() != grid[i][j].isOwner()) {
                                    res[nextI][j + 1] = true;
                                }
                            }
                        }
                        if (j - 1 > -1) {
                            if (grid[nextI][j - 1].getPiece() != SIZE) {
                                if (grid[nextI][j - 1].isOwner() != grid[i][j].isOwner()) {
                                    res[nextI][j - 1] = true;
                                }
                            }
                        }
                    } else if ((!grid[i][j].isOwner()) && nextI < 8) {
                        if (grid[nextI][j].getPiece() == SIZE) {
                            res[nextI][j] = true;
                        }
                        if (j + 1 < 8) {
                            if (grid[nextI][j + 1].getPiece() != SIZE) {
                                if (grid[nextI][j + 1].isOwner() != grid[i][j].isOwner()) {
                                    res[nextI][j + 1] = true;
                                }
                            }
                        }
                        if (j - 1 > -1) {
                            if (grid[nextI][j - 1].getPiece() != SIZE) {
                                if (grid[nextI][j - 1].isOwner() != grid[i][j].isOwner()) {
                                    res[nextI][j - 1] = true;
                                }
                            }
                        }
                    }
                    nextI = reverse ? (grid[i][j].isOwner() ? i+2 : i-2) : (grid[i][j].isOwner() ? i-2 : i+2);
                    boolean ownerLine = reverse ? !grid[i][j].isOwner() : grid[i][j].isOwner();
                    if (ownerLine && i == 6) {
                        if (grid[nextI][j].getPiece() == 100) {
                            res[nextI][j] = true;
                        }
                    }
                    if (!ownerLine && i == 1) {
                        if (grid[nextI][j].getPiece() == 100) {
                            res[nextI][j] = true;
                        }
                    }
                }
            }
        }
        return res;
    }
}