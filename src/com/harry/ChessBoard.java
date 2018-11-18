package com.harry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * @program: five
 * @description: 棋盘类
 * @author: Harry
 **/



public class ChessBoard extends JPanel implements MouseListener, MouseMotionListener {
    public static final int MARGIN=30;//边距
    public static final int GRID_SPAN=35;//网格间距
    public static final int ROWS=15;//棋盘行数
    public static final int COLS=15;//棋盘列数


    Point[][] chessList=new Point[ROWS+1][COLS+1];//初始每个数组元素为null

    boolean isBlack=true;//默认开始是黑棋先
    boolean gameOver=false;//游戏是否结束
    int chessCount;//当前棋盘棋子的个数
    int xIndex,yIndex;//当前刚下棋子的索引

    Color colortemp;
    public ChessBoard(){
        addMouseListener(this);
        addMouseMotionListener(this);
        for (int j = 0; j < ROWS + 1; j++) {
            for (int i = 0; i < COLS + 1; i++) {
                chessList[i][j] = new Point(i , j);
            }
        }
    }



    @Override
    public void paintComponent(Graphics g){


        for(int i=0;i<=ROWS;i++){//画横线
            g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+COLS*GRID_SPAN, MARGIN+i*GRID_SPAN);
        }
        for(int i=0;i<=COLS;i++){//画竖线
            g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, MARGIN+ROWS*GRID_SPAN);

        }

        //画棋子
        for(Point[] points : chessList){

            for (Point point : points) {
                if (point.getColor() == null) {
                    continue;
                }
                int xPos=point.getX()*GRID_SPAN+MARGIN;
                int yPos=point.getY()*GRID_SPAN+MARGIN;
                g.setColor(point.getColor());//设置颜色
                colortemp=point.getColor();
                if(colortemp==Color.black){
                    RadialGradientPaint paint = new RadialGradientPaint(xPos-Point.DIAMETER/2+25, yPos-Point.DIAMETER/2+10, 20, new float[]{0f, 1f}
                            , new Color[]{Color.WHITE, Color.BLACK});
                    ((Graphics2D) g).setPaint(paint);
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

                }
                else if(colortemp==Color.white){
                    RadialGradientPaint paint = new RadialGradientPaint(xPos-Point.DIAMETER/2+25, yPos-Point.DIAMETER/2+10, 70, new float[]{0f, 1f}
                            , new Color[]{Color.WHITE, Color.BLACK});
                    ((Graphics2D) g).setPaint(paint);
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

                }

                Ellipse2D e = new Ellipse2D.Float(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, 34, 35);
                ((Graphics2D) g).fill(e);
                //标记最后一个棋子的红矩形框

                if(point.getChessNumber() == chessCount){//如果是最后一个棋子
                    g.setColor(Color.red);
                    g.drawRect(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2,
                            34, 35);
                }
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e){

        //游戏结束时，不再能下
        if(gameOver) return;

        String colorName=isBlack?"黑棋":"白棋";

        //将鼠标点击的坐标位置转换成网格索引
        xIndex=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
        yIndex=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;

        //落在棋盘外不能下
        if(xIndex<0||xIndex>ROWS||yIndex<0||yIndex>COLS)
            return;

        //如果x，y位置已经有棋子存在，不能下
        if(findChess(xIndex,yIndex))return;

        Point point = chessList[xIndex][yIndex];

        //可以进行时的处理
        point.setColor(isBlack?Color.black:Color.white);
        point.setChessNumber(++chessCount);
        setNext(point);

        System.out.println(point.getChessNumber());

        repaint();//通知系统重新绘制

        //如果胜出则给出提示信息，不能继续下棋

        if(isWin(point)){
            String msg=String.format("恭喜，%s赢了！", colorName);
            JOptionPane.showMessageDialog(this, msg);
            gameOver=true;
        }
        isBlack=!isBlack;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x1=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
        //将鼠标点击的坐标位置转成网格索引
        int y1=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
        //游戏已经结束不能下
        //落在棋盘外不能下
        //x，y位置已经有棋子存在，不能下
        if(x1<0||x1>ROWS||y1<0||y1>COLS||gameOver||findChess(x1,y1))
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            //设置成默认状态
        else setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseClicked(MouseEvent e){
        //鼠标按键在组件上单击时调用
    }

    @Override
    public void mouseEntered(MouseEvent e){
        //鼠标进入到组件上时调用
    }

    @Override
    public void mouseExited(MouseEvent e){
        //鼠标离开组件时调用
    }

    @Override
    public void mouseReleased(MouseEvent e){
        //鼠标按钮在组件上释放时调用
    }

    private void setNext(Point point) {
        Color[] colorNext = null;
        int x = point.getX();
        int y = point.getY();

        //top
        if (y - 1 >= 0) {
            colorNext = point.getColorNext();
            colorNext[Direction.TOP.getIndex()] = chessList[x][y -1].getColor();
            point.setColorNext(colorNext);
            colorNext = chessList[x][y-1].getColorNext();
            colorNext[Direction.DOWN.getIndex()] = point.getColor();
            chessList[x][y-1].setColorNext(colorNext);
        }
        //topright
        if (y - 1 >= 0 && x + 1 <= ROWS) {
            colorNext = point.getColorNext();
            colorNext[Direction.TOPRIGHT.getIndex()] = chessList[x+1][y-1].getColor();
            point.setColorNext(colorNext);
            colorNext = chessList[x+1][y-1].getColorNext();
            colorNext[Direction.DOWNLEFT.getIndex()] = point.getColor();
            chessList[x+1][y-1].setColorNext(colorNext);
        }
        //right
        if (x + 1 <= ROWS) {
            colorNext = point.getColorNext();
            colorNext[Direction.RIGHT.getIndex()] = chessList[x+1][y].getColor();
            point.setColorNext(colorNext);
            colorNext = chessList[x+1][y].getColorNext();
            colorNext[Direction.LEFT.getIndex()] = point.getColor();
            chessList[x+1][y].setColorNext(colorNext);
        }
        // downright
        if (y + 1 <= COLS && x + 1 <= ROWS) {
            colorNext = point.getColorNext();
            colorNext[Direction.DOWNRIGHT.getIndex()] = chessList[x+1][y+1].getColor();
            point.setColorNext(colorNext);
            colorNext = chessList[x+1][y+1].getColorNext();
            colorNext[Direction.TOPLEFT.getIndex()] = point.getColor();
            chessList[x+1][y+1].setColorNext(colorNext);
        }
        //down
        if (y + 1 <= COLS) {
            colorNext = point.getColorNext();
            colorNext[Direction.DOWN.getIndex()] = chessList[x][y+1].getColor();
            point.setColorNext(colorNext);
            colorNext = chessList[x][y+1].getColorNext();
            colorNext[Direction.TOP.getIndex()] = point.getColor();
            chessList[x][y+1].setColorNext(colorNext);
        }
        //downleft
        if (y + 1 <= COLS && x - 1 >= 0) {
            colorNext = point.getColorNext();
            colorNext[Direction.DOWNLEFT.getIndex()] = chessList[x-1][y+1].getColor();
            point.setColorNext(colorNext);
            colorNext = chessList[x-1][y+1].getColorNext();
            colorNext[Direction.TOPRIGHT.getIndex()] = point.getColor();
            chessList[x-1][y+1].setColorNext(colorNext);
        }
        //left
        if (x - 1 >= 0) {
            colorNext = point.getColorNext();
            colorNext[Direction.LEFT.getIndex()] = chessList[x-1][y].getColor();
            point.setColorNext(colorNext);
            colorNext = chessList[x-1][y].getColorNext();
            colorNext[Direction.RIGHT.getIndex()] = point.getColor();
            chessList[x-1][y].setColorNext(colorNext);
        }
        //topleft
        if (y - 1 >= COLS && x - 1 >= ROWS) {
            colorNext = point.getColorNext();
            colorNext[Direction.TOPLEFT.getIndex()] = chessList[x-1][y-1].getColor();
            point.setColorNext(colorNext);
            colorNext = chessList[x-1][y-1].getColorNext();
            colorNext[Direction.DOWNRIGHT.getIndex()] = point.getColor();
            chessList[x-1][y-1].setColorNext(colorNext);
        }

    }

    //在棋子数组中查找是否有索引为x，y的棋子存在
    private boolean findChess(int x,int y){
        if (chessList[x][y].getColor() == null) {
            return false;
        }
        return true;
    }


    private boolean isWin(Point point){

        for (Direction direction : Direction.values()) {
            int check = check(0, direction, point);
            if (check == 4) {
                return true;
            }
        }
        return false;
    }

    private int check(int number, Direction direction, Point point) {


        if (point.getColorNext()[direction.getIndex()] != null && point.getColorNext()[direction.getIndex()].equals(point.getColor())) {
            number++;
            switch (direction) {
                case TOP:
                    number = check(number, direction, chessList[point.getX()][point.getY()-1]);
                    break;
                case TOPRIGHT:
                    number = check(number, direction, chessList[point.getX()+1][point.getY()-1]);
                    break;
                case RIGHT:
                    number = check(number, direction, chessList[point.getX()+1][point.getY()]);
                    break;
                case DOWNRIGHT:
                    number = check(number, direction, chessList[point.getX()+1][point.getY()+1]);
                    break;
                case DOWN:
                    number = check(number, direction, chessList[point.getX()][point.getY()+1]);
                    break;
                case DOWNLEFT:
                    number = check(number, direction, chessList[point.getX()-1][point.getY()+1]);
                    break;
                case LEFT:
                    number = check(number, direction, chessList[point.getX()-1][point.getY()]);
                    break;
                case TOPLEFT:
                    number = check(number, direction, chessList[point.getX()-1][point.getY()-1]);
                    break;
            }
        }
        return number;


    }


    public void restartGame(){
        //清除棋子
        for (int j = 0; j < ROWS + 1; j++) {
            for (int i = 0; i < COLS + 1; i++) {
                chessList[i][j] = new Point(i , j);
            }
        }
        //恢复游戏相关的变量值
        isBlack=true;
        gameOver=false; //游戏是否结束
        chessCount =0; //当前棋盘棋子个数
        repaint();
    }

    //悔棋
    public void goback(){
        if(chessCount==0)
            return ;
        label:
        for (int j = 0; j < ROWS + 1; j++) {
            for (int i = 0; i < COLS + 1; i++) {
                if (chessList[i][j].getChessNumber() == chessCount) {
                    chessList[i][j] = new Point(i, j);
                    setNext(chessList[i][j]);
                    chessList[i][j] = new Point(i, j);
                    chessCount--;
                    if (chessCount>1) {
                        isBlack=!isBlack;
                    }
                    if (gameOver = true) {
                        gameOver=!true;
                    }
                    break label;
                }
            }
        }
        repaint();

}

    //矩形Dimension

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(MARGIN*2+GRID_SPAN*COLS,MARGIN*2
                +GRID_SPAN*ROWS);
    }



}

