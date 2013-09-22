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

import org.jchess.JChess;
import org.jchess.model.Grid;
import org.jchess.model.Piece;
import org.jchess.model.figures.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Board extends JPanel implements MouseListener {

    Grid[][] grid;
    private boolean currTurn = true;
    public static final int CELL_SIZE = 50;
    private boolean reverse;
    private JChess main;

    public Board(boolean isWhite, JChess main) {
        this.main = main;
        reverse = !isWhite;
        addMouseListener(this);
        grid = new Grid[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = new Grid(j * CELL_SIZE, i * CELL_SIZE);
            }
        }
        for (int i = 0; i < 8; i++) {
            (grid[1][i]).changePiece(Pawn.value);
            (grid[6][i]).changePiece(Pawn.value);
            (grid[isWhite ? 6 : 1][i]).changeOwner(true);
        }
        grid[0][1].changePiece(Knight.value);
        grid[0][6].changePiece(Knight.value);
        grid[7][1].changePiece(Knight.value);
        grid[7][6].changePiece(Knight.value);
        grid[isWhite ? 7 : 0][1].changeOwner(true);
        grid[isWhite ? 7 : 0][6].changeOwner(true);
        grid[0][2].changePiece(Bishop.value);
        grid[0][5].changePiece(Bishop.value);
        grid[7][2].changePiece(Bishop.value);
        grid[7][5].changePiece(Bishop.value);
        grid[isWhite ? 7 : 0][2].changeOwner(true);
        grid[isWhite ? 7 : 0][5].changeOwner(true);
        grid[0][0].changePiece(Rook.value);
        grid[0][7].changePiece(Rook.value);
        grid[7][0].changePiece(Rook.value);
        grid[7][7].changePiece(Rook.value);
        grid[isWhite ? 7 : 0][0].changeOwner(true);
        grid[isWhite ? 7 : 0][7].changeOwner(true);
        grid[0][3].changePiece(Queen.value);
        grid[0][4].changePiece(King.value);
        grid[7][3].changePiece(Queen.value);
        grid[7][4].changePiece(King.value);
        grid[isWhite ? 7 : 0][3].changeOwner(true);
        grid[isWhite ? 7 : 0][4].changeOwner(true);
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j <= 6; j += 2) {
                if (i % 2 == 0) {
                    g.fillRect((j + 1) * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else {
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        g.setColor(Color.WHITE);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j <= 6; j += 2) {
                if (i % 2 == 0) {
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else {
                    g.fillRect((j + 1) * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j].selected) {
                    g.setColor(Color.BLUE);
                    g.fillRect(grid[i][j].x, grid[i][j].y, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.DARK_GRAY);
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j].moveallowed) {
                    g.setColor(Color.GREEN);
                    g.fillRect(grid[i][j].x, grid[i][j].y, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.DARK_GRAY);
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j].moveallowed) {
                    g.drawRect(grid[i][j].x + 1, grid[i][j].y + 1, CELL_SIZE-3, CELL_SIZE-3);
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((grid[i][j]).piece != CELL_SIZE*2) {
                    g.drawImage(Toolkit.getDefaultToolkit().getImage(Piece.parse((grid[i][j]).piece, reverse).imgurl(grid[i][j].owner)), (grid[i][j]).x + CELL_SIZE*2/10, (grid[i][j]).y + CELL_SIZE*2/10, this);
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j].moveunallow();
            }
        }
    }

    public boolean hasSelect() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j].selected) {
                    return true;
                }
            }
        }
        return false;
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        main.send(e.getX()+"~"+(CELL_SIZE * 8 - e.getY()));
        touch(e.getX(), e.getY());
    }

    public void touch(int x, int y) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j].x == x - (x % CELL_SIZE) && grid[i][j].y == y - (y % CELL_SIZE) && grid[i][j].selected) {
                    grid[i][j].deselect();
                } else if (grid[i][j].x == x - (x % CELL_SIZE) && grid[i][j].y == y - (y % CELL_SIZE) && (!grid[i][j].selected) && (!hasSelect())) {
                    grid[i][j].select();
                } else if (grid[i][j].x == x - (x % CELL_SIZE) && grid[i][j].y == y - (y % CELL_SIZE) && (!grid[i][j].selected) && hasSelect()) {
                    for (int ii = 0; ii < 8; ii++) {
                        for (int jj = 0; jj < 8; jj++) {
                            if (grid[ii][jj].selected && grid[ii][jj].piece != CELL_SIZE*2) {
                                if (Piece.parse(grid[ii][jj].piece, reverse).placeMoves(grid)[i][j] && grid[ii][jj].owner==currTurn) {
                                    grid[i][j].changePiece(grid[ii][jj].piece);
                                    grid[ii][jj].changePiece(CELL_SIZE*2);
                                    grid[i][j].changeOwner(grid[ii][jj].owner);
                                    grid[ii][jj].changeOwner(false);
                                    grid[ii][jj].deselect();
                                    currTurn = !currTurn;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j].selected && grid[i][j].piece != CELL_SIZE*2) {
                    boolean[][] res = Piece.parse(grid[i][j].piece, reverse).placeMoves(grid);
                    for (int n = 0; n < 8; n++) {
                        for (int m = 0; m < 8; m++) {
                            if (res[n][m]) {
                                grid[n][m].moveallow();
                            }
                        }
                    }
                }
            }
        }
        repaint();
    }

}