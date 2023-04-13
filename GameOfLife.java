/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

public class GameOfLife extends JFrame implements Runnable {

    static final int numRows = 193;
    static final int numColumns = 373;
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 30;
    static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + numColumns*5;
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + numRows*5;

    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;

    final int DEAD = 0;
    final int ALIVE = 1;
    int board[][];
    int board2[][];

    boolean inGame;

    boolean showMenu;
    boolean clear;
    boolean glider;
    boolean smallExploder;
    boolean exploder;
    boolean lightweightSpaceship;
    boolean tumbler;
    boolean gliderGun;
    boolean vertLine;
    boolean horizLine;
    boolean exit;
    boolean menu;

    int timeCount;
    double frameRate = 40.0;

    static GameOfLife frame;
    public static void main(String[] args) {
        frame = new GameOfLife();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public GameOfLife() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button

// location of the cursor.
                    int xpos = e.getX() - getX(5);
                    int ypos = e.getY() - getY(5);

                    for (int zrow = 0;zrow < numRows;zrow++)
                    {
                        for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
                        {
                            if (xpos < zcolumn * 5 && xpos > (zcolumn - 1) * 5 &&
                                ypos < zrow * 5&& ypos > (zrow - 1) * 5)
                            {
                                if (board[zrow][zcolumn] == DEAD)
                                    board2[zrow][zcolumn] = ALIVE;
                                else if (board[zrow][zcolumn] == ALIVE)
                                    board2[zrow][zcolumn] = DEAD;
                            }
                        }
                    }
                    if (showMenu)
                    {
                        if (xpos > getWidth2()-170 && xpos < getWidth2()-95 &&
                            ypos > getHeight2()-170 && ypos < getHeight2()-143)
                            clear();
                        if (xpos > getWidth2()-85 && xpos < getWidth2()-10 &&
                            ypos > getHeight2()-170 && ypos < getHeight2()-143)
                            Glider();
                        if (xpos > getWidth2()-180 && xpos < getWidth2()-70 &&
                            ypos > getHeight2()-140 && ypos < getHeight2()-113)
                            SmallExploder();
                        if (xpos > getWidth2()-70 && xpos < getWidth2()-10 &&
                            ypos > getHeight2()-140 && ypos < getHeight2()-113)
                            Exploder();
                        if (xpos > getWidth2()-180 && xpos < getWidth2()-113 &&
                            ypos > getHeight2()-110 && ypos < getHeight2()-83)
                            Tumbler();
                        if (xpos > getWidth2()-85 && xpos < getWidth2()-7 &&
                            ypos > getHeight2()-110 && ypos < getHeight2()-83)
                            GliderGun();
                        if (xpos > getWidth2()-170 && xpos < getWidth2()-13 &&
                            ypos > getHeight2()-80 && ypos < getHeight2()-53)
                            LightweightSpaceship();
                        if (xpos > getWidth2()-170 && xpos < getWidth2()-13 &&
                            ypos > getHeight2()-52 && ypos < getHeight2()-25)
                            HorizLine();
                        if (xpos > getWidth2()-170 && xpos < getWidth2()-13 &&
                            ypos > getHeight2()-21 && ypos < getHeight2()-2)
                            VertLine();
                        if (xpos > getWidth2()-180 && xpos < getWidth2()-150 &&
                            ypos > getHeight2()-200 && ypos < getHeight2()-183)
                            showMenu = false;
                    }
                    else
                    {
                        if (xpos > getWidth2()-185 && xpos < getWidth2()-5 &&
                            ypos > getHeight2()-38 && ypos < getHeight2()-5)
                            showMenu = true;
                    }

                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {

        int xpos = e.getX() - getX(5);
        int ypos = e.getY() - getY(5);

        for (int zrow = 0;zrow < numRows;zrow++)
        {
            for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
            {
                if (xpos < zcolumn * 5 && xpos > (zcolumn - 1) * 5 &&
                    ypos < zrow * 5&& ypos > (zrow - 1) * 5)
                {
                    if (board[zrow][zcolumn] == DEAD)
                        board2[zrow][zcolumn] = ALIVE;
                    else if (board[zrow][zcolumn] == ALIVE)
                        board2[zrow][zcolumn] = DEAD;
                }
            }
        }
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
            int xpos = e.getX() - getX(5);
            int ypos = e.getY() - getY(5);
            if (showMenu)
            {
                if (xpos > getWidth2()-170 && xpos < getWidth2()-95 &&
                    ypos > getHeight2()-170 && ypos < getHeight2()-143)
                    clear = true;
                else
                    clear = false;

                if (xpos > getWidth2()-85 && xpos < getWidth2()-10 &&
                    ypos > getHeight2()-170 && ypos < getHeight2()-143)
                    glider = true;
                else
                    glider = false;

                if (xpos > getWidth2()-180 && xpos < getWidth2()-73 &&
                    ypos > getHeight2()-140 && ypos < getHeight2()-113)
                    smallExploder = true;
                else
                    smallExploder = false;

                if (xpos > getWidth2()-70 && xpos < getWidth2()-10 &&
                    ypos > getHeight2()-140 && ypos < getHeight2()-113)
                    exploder = true;
                else
                    exploder = false;

                if (xpos > getWidth2()-180 && xpos < getWidth2()-113 &&
                    ypos > getHeight2()-110 && ypos < getHeight2()-83)
                    tumbler = true;
                else
                    tumbler = false;

                if (xpos > getWidth2()-85 && xpos < getWidth2()-7 &&
                    ypos > getHeight2()-110 && ypos < getHeight2()-83)
                    gliderGun = true;
                else
                    gliderGun = false;

                if (xpos > getWidth2()-170 && xpos < getWidth2()-13 &&
                    ypos > getHeight2()-80 && ypos < getHeight2()-53)
                    lightweightSpaceship = true;
                else
                    lightweightSpaceship = false;

                if (xpos > getWidth2()-170 && xpos < getWidth2()-13 &&
                    ypos > getHeight2()-52 && ypos < getHeight2()-25)
                    horizLine = true;
                else
                    horizLine = false;

                if (xpos > getWidth2()-170 && xpos < getWidth2()-13 &&
                    ypos > getHeight2()-21 && ypos < getHeight2()-2)
                    vertLine = true;
                else
                    vertLine = false;

                if (xpos > getWidth2()-180 && xpos < getWidth2()-150 &&
                    ypos > getHeight2()-200 && ypos < getHeight2()-183)
                    exit = true;
                else
                    exit = false;
            }
            else
            {
                if (xpos > getWidth2()-185 && xpos < getWidth2()-5 &&
                    ypos > getHeight2()-38 && ypos < getHeight2()-5)
                    menu = true;
                else
                    menu = false;

                //g.fillRoundRect(getWidth2()-150, getHeight2() + 20, getX(151), getY(-19), 10, 10);
            }

        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_UP == e.getKeyCode()) {

                } else if (e.VK_DOWN == e.getKeyCode()) {

                } else if (e.VK_LEFT == e.getKeyCode()) {

                } else if (e.VK_RIGHT == e.getKeyCode()) {

                }

                if (e.VK_SPACE == e.getKeyCode()) {
                    if(inGame)
                        inGame = false;
                    else
                        inGame = true;
                }

                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }



////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

//fill background
        g.setColor(Color.black);
        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
//        g.setColor(Color.red);
//        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }

//Display the objects of the board
        for (int zrow=0;zrow<numRows;zrow++)
        {
            for (int zcolumn=0;zcolumn<numColumns;zcolumn++)
            {
                if (board[zrow][zcolumn] == ALIVE)
                {
                    g.setColor(Color.gray);
                    g.fillRect(getX(0)+zcolumn*getWidth2()/numColumns,
                    getY(0)+zrow*getHeight2()/numRows,
                    getWidth2()/numColumns,
                    getHeight2()/numRows);
                }
            }
        }

        if (showMenu)
        {
            g.setColor(Color.lightGray);
            g.fillRoundRect(getWidth2()-150, getHeight2()-150, getX(151), getY(151), 10, 10);


            g.setColor(Color.gray);
            if (clear)
                g.fillRoundRect(getWidth2()-135, getHeight2()-110, 70, 20, 10, 10);
            if (glider)
                g.fillRoundRect(getWidth2()-50, getHeight2()-110, 70, 20, 10, 10);
            if (smallExploder)
                g.fillRoundRect(getWidth2()-140, getHeight2()-80, 100, 20, 10, 10);
            if (exploder)
                g.fillRoundRect(getWidth2()-37, getHeight2()-80, 60, 20, 10, 10);
            if (tumbler)
                g.fillRoundRect(getWidth2()-140, getHeight2()-50, 60, 20, 10, 10);
            if (gliderGun)
                g.fillRoundRect(getWidth2()-50, getHeight2()-50, 75, 20, 10, 10);
            if (lightweightSpaceship)
                g.fillRoundRect(getWidth2()-135, getHeight2()-20, 155, 20, 10, 10);
            if (vertLine)
                g.fillRoundRect(getWidth2()-135, getHeight2()+35, 155, 15, 10, 10);
            if (horizLine)
                g.fillRoundRect(getWidth2()-135, getHeight2()+8, 155, 20, 10, 10);
            if (exit)
                g.fillRoundRect(getWidth2()-147, getHeight2()-142, 30, 13, 10, 10);


            g.setColor(Color.black);
            g.setFont(new Font("Times New Roman", Font.PLAIN,28));
            g.drawString("Menu",getWidth2() - 90,getHeight2() - 120);

            g.setFont(new Font("Times New Roman", Font.PLAIN,13));
            g.drawString("Exit",getWidth2() - 142,getHeight2() -132);

            g.setFont(new Font("Times New Roman", Font.PLAIN,16));
            g.drawString("Clear",getWidth2() - 115,getHeight2() - 95);
            g.drawString("Glider",getWidth2() - 35,getHeight2() - 95);
            g.drawString("Small Exploder",getWidth2() - 135,getHeight2() - 65);
            g.drawString("Exploder",getWidth2() - 35,getHeight2() - 65);
            g.drawString("Tumbler",getWidth2() - 135,getHeight2() - 35);
            g.drawString("Glider Gun",getWidth2() - 45,getHeight2() - 35);
            g.drawString("Lightweight Spaceship",getWidth2() - 125,getHeight2() - 5);
            g.drawString("Horizontal Line",getWidth2() - 105,getHeight2() + 23);
            g.drawString("Vertical Line",getWidth2() - 95,getHeight2() + 48);
        }
        else
        {
            if(menu)
                g.setColor(Color.gray);
            else
                g.setColor(Color.lightGray);
            g.fillRoundRect(getWidth2()-150, getHeight2() + 20, getX(151), getY(-19), 10, 10);

            g.setColor(Color.black);
            g.setFont(new Font("Times New Roman", Font.PLAIN,28));
            g.drawString("Menu",getWidth2() - 90,getHeight2() + 43);
        }

// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        gOld.drawImage(image, 0, 0, null);
    }
    public void clear()
    {
        board = new int[numRows][numColumns];
        board2 = new int[numRows][numColumns];

        for (int zrow = 0;zrow < numRows;zrow++)
        {
            for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
                board[zrow][zcolumn] = DEAD;
        }
        for (int zrow = 0;zrow < numRows;zrow++)
        {
            for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
                board2[zrow][zcolumn] = board[zrow][zcolumn];
        }
    }
    public void HorizLine()
    {
        for (int column = 0;column < numColumns;column++)
            board2[numRows/2][column] = ALIVE;
    }
    public void VertLine()
    {
        for (int row = 0;row < numRows;row++)
            board2[row][numColumns/2] = ALIVE;
    }
    public void Glider()
    {
        board2[numRows/2][numColumns/2] = ALIVE;
        board2[numRows/2][numColumns/2+1] = ALIVE;
        board2[numRows/2][numColumns/2+2] = ALIVE;
        board2[numRows/2-1][numColumns/2+2] = ALIVE;
        board2[numRows/2-2][numColumns/2+1] = ALIVE;
    }
    public void SmallExploder()
    {
        board2[numRows/2][numColumns/2] = ALIVE;
        board2[numRows/2-1][numColumns/2] = ALIVE;
        board2[numRows/2][numColumns/2-1] = ALIVE;
        board2[numRows/2][numColumns/2+1] = ALIVE;
        board2[numRows/2+1][numColumns/2-1] = ALIVE;
        board2[numRows/2+1][numColumns/2+1] = ALIVE;
        board2[numRows/2+2][numColumns/2] = ALIVE;
    }
    public void Exploder()
    {
        board2[numRows/2-2][numColumns/2] = ALIVE;
        board2[numRows/2+2][numColumns/2] = ALIVE;
        board2[numRows/2-2][numColumns/2-2] = ALIVE;
        board2[numRows/2-1][numColumns/2-2] = ALIVE;
        board2[numRows/2][numColumns/2-2] = ALIVE;
        board2[numRows/2+1][numColumns/2-2] = ALIVE;
        board2[numRows/2+2][numColumns/2-2] = ALIVE;
        board2[numRows/2-2][numColumns/2+2] = ALIVE;
        board2[numRows/2-1][numColumns/2+2] = ALIVE;
        board2[numRows/2][numColumns/2+2] = ALIVE;
        board2[numRows/2+1][numColumns/2+2] = ALIVE;
        board2[numRows/2+2][numColumns/2+2] = ALIVE;
    }
    public void LightweightSpaceship()
    {
        board2[numRows/2][numColumns/2] = ALIVE;
        board2[numRows/2-2][numColumns/2] = ALIVE;
        board2[numRows/2][numColumns/2+3] = ALIVE;
        board2[numRows/2-1][numColumns/2+4] = ALIVE;
        board2[numRows/2-2][numColumns/2+4] = ALIVE;
        board2[numRows/2-3][numColumns/2+4] = ALIVE;
        board2[numRows/2-3][numColumns/2+1] = ALIVE;
        board2[numRows/2-3][numColumns/2+2] = ALIVE;
        board2[numRows/2-3][numColumns/2+3] = ALIVE;
        board2[numRows/2-3][numColumns/2+4] = ALIVE;
    }
    public void Tumbler()
    {
        board2[numRows/2-2][numColumns/2+1] = ALIVE;
        board2[numRows/2-1][numColumns/2+1] = ALIVE;
        board2[numRows/2+0][numColumns/2+1] = ALIVE;
        board2[numRows/2+1][numColumns/2+1] = ALIVE;
        board2[numRows/2+2][numColumns/2+1] = ALIVE;
        board2[numRows/2-2][numColumns/2-1] = ALIVE;
        board2[numRows/2-1][numColumns/2-1] = ALIVE;
        board2[numRows/2+0][numColumns/2-1] = ALIVE;
        board2[numRows/2+1][numColumns/2-1] = ALIVE;
        board2[numRows/2+2][numColumns/2-1] = ALIVE;
        board2[numRows/2-2][numColumns/2+2] = ALIVE;
        board2[numRows/2-1][numColumns/2+2] = ALIVE;
        board2[numRows/2-2][numColumns/2-2] = ALIVE;
        board2[numRows/2-1][numColumns/2-2] = ALIVE;
        board2[numRows/2+3][numColumns/2-2] = ALIVE;
        board2[numRows/2+3][numColumns/2-3] = ALIVE;
        board2[numRows/2+2][numColumns/2-3] = ALIVE;
        board2[numRows/2+1][numColumns/2-3] = ALIVE;
        board2[numRows/2+3][numColumns/2+2] = ALIVE;
        board2[numRows/2+3][numColumns/2+3] = ALIVE;
        board2[numRows/2+2][numColumns/2+3] = ALIVE;
        board2[numRows/2+1][numColumns/2+3] = ALIVE;
    }
    public void GliderGun()
    {
        board2[numRows/2][numColumns/2] = ALIVE;
        board2[numRows/2-1][numColumns/2-1] = ALIVE;
        board2[numRows/2-1][numColumns/2-2] = ALIVE;
        board2[numRows/2][numColumns/2-2] = ALIVE;
        board2[numRows/2+1][numColumns/2-2] = ALIVE;

        board2[numRows/2-1][numColumns/2-9] = ALIVE;
        board2[numRows/2-1][numColumns/2-10] = ALIVE;
        board2[numRows/2-2][numColumns/2-10] = ALIVE;
        board2[numRows/2-2][numColumns/2-8] = ALIVE;
        board2[numRows/2-3][numColumns/2-8] = ALIVE;
        board2[numRows/2-3][numColumns/2-9] = ALIVE;

        board2[numRows/2-3][numColumns/2-17] = ALIVE;
        board2[numRows/2-3][numColumns/2-18] = ALIVE;
        board2[numRows/2-2][numColumns/2-17] = ALIVE;
        board2[numRows/2-2][numColumns/2-18] = ALIVE;

        board2[numRows/2-3][numColumns/2+4] = ALIVE;
        board2[numRows/2-3][numColumns/2+5] = ALIVE;
        board2[numRows/2-4][numColumns/2+4] = ALIVE;
        board2[numRows/2-4][numColumns/2+6] = ALIVE;
        board2[numRows/2-5][numColumns/2+6] = ALIVE;
        board2[numRows/2-5][numColumns/2+5] = ALIVE;

        board2[numRows/2+7][numColumns/2+6] = ALIVE;
        board2[numRows/2+7][numColumns/2+7] = ALIVE;
        board2[numRows/2+7][numColumns/2+8] = ALIVE;
        board2[numRows/2+8][numColumns/2+6] = ALIVE;
        board2[numRows/2+9][numColumns/2+7] = ALIVE;

        board2[numRows/2-5][numColumns/2+16] = ALIVE;
        board2[numRows/2-5][numColumns/2+17] = ALIVE;
        board2[numRows/2-4][numColumns/2+16] = ALIVE;
        board2[numRows/2-4][numColumns/2+17] = ALIVE;

        board2[numRows/2+2][numColumns/2+17] = ALIVE;
        board2[numRows/2+3][numColumns/2+17] = ALIVE;
        board2[numRows/2+4][numColumns/2+17] = ALIVE;
        board2[numRows/2+2][numColumns/2+18] = ALIVE;
        board2[numRows/2+3][numColumns/2+19] = ALIVE;
    }
////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 1/frameRate;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {
//Allocate memory for the 2D array that represents the board.
        board = new int[numRows][numColumns];
        board2 = new int[numRows][numColumns];
//Initialize the board to be empty.
        for (int zrow = 0;zrow < numRows;zrow++)
        {
            for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
                board[zrow][zcolumn] = DEAD;
        }

        timeCount = 0;
        inGame = false;

        for (int zrow = 0;zrow < numRows;zrow++)
        {
            for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
                board2[zrow][zcolumn] = board[zrow][zcolumn];
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {
        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }
            reset();

        }
        if (inGame)
        {
        //if(timeCount % (int)(frameRate/1) == (int)(frameRate/1)-1)
        {
            for (int zrow = 0;zrow < numRows;zrow++)
            {
                for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
                {
                    int neighbors = 0;

                    if (board[zrow][zcolumn] == ALIVE)
                    {
                        for(int rowDir = -1; rowDir <= 1; rowDir++)
                        {
                            for(int columnDir = -1; columnDir <= 1; columnDir++)
                            {
                                if (zrow + rowDir != numRows && zrow + rowDir != -1 &&
                                    zcolumn + columnDir != numColumns && zcolumn + columnDir != -1)
                                    {
                                        if (rowDir == 0 && columnDir == 0){}
                                        else if (board[zrow+rowDir][zcolumn+columnDir] == ALIVE)
                                            neighbors++;
                                    }
                            }
                        }

                        if(neighbors < 2 || neighbors > 3)
                            board2[zrow][zcolumn] = DEAD;
                    }

                    if (board[zrow][zcolumn] == DEAD)
                    {

                        for(int rowDir = -1; rowDir <= 1; rowDir++)
                        {
                            for(int columnDir = -1; columnDir <= 1; columnDir++)
                            {
                                if (zrow + rowDir != numRows && zrow + rowDir != -1 &&
                                    zcolumn + columnDir != numColumns && zcolumn + columnDir != -1)
                                    {
                                        if (rowDir == 0 && columnDir == 0){}
                                        else if (board[zrow+rowDir][zcolumn+columnDir] == ALIVE)
                                            neighbors++;
                                    }
                            }
                        }

                        if(neighbors == 3)
                            board2[zrow][zcolumn] = ALIVE;
                    }
                }
            }

        }

        timeCount++;
        }
        for (int zrow = 0;zrow < numRows;zrow++)
        {
            for (int zcolumn = 0;zcolumn < numColumns;zcolumn++)
                board[zrow][zcolumn] = board2[zrow][zcolumn];
        }
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }


/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER + WINDOW_BORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE );
    }

    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }

    public int getWidth2() {
        return (xsize - 2 * (XBORDER + WINDOW_BORDER));
    }

    public int getHeight2() {
        return (ysize - 2 * YBORDER - WINDOW_BORDER - YTITLE);
    }
}
