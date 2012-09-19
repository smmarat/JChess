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

public abstract class Piece{
    
    public abstract String imgurl(boolean white);
    
    public abstract boolean[][] placeMoves(Grid[][] grid);

    public static Piece parse(int val){
        if (val==0){
            return new King();
        }
        else if (val==10){
            return new Pawn();
        }
        else if (val==20){
            return new Bishop();
        }
        else if (val==30){
            return new Knight();
        }
        else if (val==40){
            return new Rook();
        }
        else if (val==50){
            return new Queen();
        }
        else{
            return null;
        }
    }
}