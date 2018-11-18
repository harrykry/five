package com.harry;

import java.awt.*;

/**
 * @program: five
 * @description: 棋子类
 * @author: Harry
 **/

public class Point {
    private int x;//棋盘中的x索引
    private int y;//棋盘中的y索引
    private Color color;//颜色
    private Color[] colorNext = new Color[8];
    private int chessNumber;
    public static final int DIAMETER=30;//直径

    public Point(int x,int y){
        this.x=x;
        this.y=y;
    }

    public int getChessNumber() {
        return chessNumber;
    }

    public void setChessNumber(int chessNumber) {
        this.chessNumber = chessNumber;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color[] getColorNext() {
        return colorNext;
    }

    public void setColorNext(Color[] colorNext) {
        this.colorNext = colorNext;
    }

    public int getX(){//拿到棋盘中x的索引
        return x;
    }
    public int getY(){
        return y;
    }
    public Color getColor(){//获得棋子的颜色
        return color;
    }
}

