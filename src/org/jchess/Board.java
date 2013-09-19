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

import org.jchess.figures.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Board extends JPanel implements MouseListener{

    Grid[][] grid;
    boolean white=true;
    
    public Board(){
        addMouseListener(this);
        grid=new Grid[8][8];
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                grid[i][j]=new Grid(j*50,i*50);
            }
        }
        for (int i=0;i<8;i++){
            (grid[1][i]).changePiece(Pawn.value);
            (grid[6][i]).changePiece(Pawn.value);
            (grid[6][i]).changeOwner(true);
        }
        grid[0][1].changePiece(Knight.value);
        grid[0][6].changePiece(Knight.value);
        grid[7][1].changePiece(Knight.value);
        grid[7][6].changePiece(Knight.value);
        grid[7][1].changeOwner(true);
        grid[7][6].changeOwner(true);
        grid[0][2].changePiece(Bishop.value);
        grid[0][5].changePiece(Bishop.value);
        grid[7][2].changePiece(Bishop.value);
        grid[7][5].changePiece(Bishop.value);
        grid[7][2].changeOwner(true);
        grid[7][5].changeOwner(true);
        grid[0][0].changePiece(Rook.value);
        grid[0][7].changePiece(Rook.value);
        grid[7][0].changePiece(Rook.value);
        grid[7][7].changePiece(Rook.value);
        grid[7][0].changeOwner(true);
        grid[7][7].changeOwner(true);
        grid[0][3].changePiece(Queen.value);
        grid[0][4].changePiece(King.value);
        grid[7][3].changePiece(Queen.value);
        grid[7][4].changePiece(King.value);
        grid[7][3].changeOwner(true);
        grid[7][4].changeOwner(true);
    }

    public void paintComponent(Graphics g){
        g.setColor(Color.DARK_GRAY);
        for (int i=0;i<8;i++){
            for (int j=0;j<=6;j+=2){
                if (i%2==0){
                    g.fillRect((j+1)*50,i*50,50,50);
                }
                else{
                    g.fillRect(j*50,i*50,50,50);
                }
            }
        }
        g.setColor(Color.WHITE);
        for (int i=0;i<8;i++){
            for (int j=0;j<=6;j+=2){
                if (i%2==0){
                    g.fillRect(j*50,i*50,50,50);
                }
                else{
                    g.fillRect((j+1)*50,i*50,50,50);
                }
            }
        }
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (grid[i][j].selected){
                    g.setColor(Color.BLUE);
                    g.fillRect(grid[i][j].x,grid[i][j].y,50,50);
                    g.setColor(Color.DARK_GRAY);
                }
            }
        }
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (grid[i][j].moveallowed){
                    g.setColor(Color.GREEN);
                    g.fillRect(grid[i][j].x,grid[i][j].y,50,50);
                    g.setColor(Color.DARK_GRAY);
                }
            }
        }
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (grid[i][j].moveallowed){
                    g.drawRect(grid[i][j].x+1,grid[i][j].y+1,47,47);
                }
            }
        }
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if ((grid[i][j]).piece!=100){
                    g.drawImage(Toolkit.getDefaultToolkit().getImage(Piece.parse((grid[i][j]).piece).imgurl(white&&grid[i][j].owner)),(grid[i][j]).x+10,(grid[i][j]).y+10,this);
                }
            }
        }
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                grid[i][j].moveunallow();
            }
        }
    }
    
    public boolean hasSelect(){
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (grid[i][j].selected){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void swapMoveArray(boolean[][] res){
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (res[i][j]){
                    grid[i][j].moveallow();
                }
            }
        }
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
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (grid[i][j].x==e.getX()-(e.getX()%50)&&grid[i][j].y==e.getY()-(e.getY()%50)&&grid[i][j].selected){
                    grid[i][j].deselect(); 
                }
                else if (grid[i][j].x==e.getX()-(e.getX()%50)&&grid[i][j].y==e.getY()-(e.getY()%50)&&(!grid[i][j].selected)&&(!hasSelect())){
                    grid[i][j].select(); 
                }
                else if (grid[i][j].x==e.getX()-(e.getX()%50)&&grid[i][j].y==e.getY()-(e.getY()%50)&&(!grid[i][j].selected)&&hasSelect()){
                    for (int ii=0;ii<8;ii++){
                        for (int jj=0;jj<8;jj++){
                            if (grid[ii][jj].selected&&grid[ii][jj].piece!=100){
                                if (Piece.parse(grid[ii][jj].piece).placeMoves(grid)[i][j]){
                                    grid[i][j].changePiece(grid[ii][jj].piece);
                                    grid[ii][jj].changePiece(100);
                                    grid[i][j].changeOwner(grid[ii][jj].owner);
                                    grid[ii][jj].changeOwner(false);
                                    grid[ii][jj].deselect();
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (grid[i][j].selected&&grid[i][j].piece!=100){
                    swapMoveArray(Piece.parse(grid[i][j].piece).placeMoves(grid));
                }
            }
        }
        repaint();
    }
}