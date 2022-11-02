package com.learn.tankgame;

import com.learn.tankgame.model.Bullet;
import com.learn.tankgame.model.EnemyTank;
import com.learn.tankgame.model.FriendTank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * @author zhoulei
 * @date 2022/10/31
 * game area
 */
public class GameArea extends JPanel implements KeyListener ,Runnable{
    private FriendTank friendTank = null;
    private Vector<EnemyTank> enemyTanks;          //for threads safe

    public GameArea() {
        friendTank = new FriendTank(100, 100, 3);
        enemyTanks = new Vector<>();
        enemyTanks.add(new EnemyTank(200, 200, 0));
        enemyTanks.add(new EnemyTank(300, 300, 0));
        enemyTanks.add(new EnemyTank(400, 400, 0));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);
        drawTank(friendTank.getX(), friendTank.getY(), g, friendTank.getDirection(), 0);
        for (Bullet bullet : friendTank.getBullets()) {
            if(bullet.isAlive()){
                g.draw3DRect(bullet.getX(),bullet.getY(),1,1,false);
            }
        }
        for (EnemyTank enemyTank : enemyTanks) {
            drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirection(), 1);
        }
    }

    /**
     * @param x         x coordinate of tank
     * @param y         y coordinate of tank
     * @param g
     * @param direction direction of tank
     * @param type      type of tank
     */
    public void drawTank(int x, int y, Graphics g, int direction, int type) {

        switch (type) {
            case 0:
                g.setColor(Color.orange);
                break;
            case 1:
                g.setColor(Color.blue);
        }

        switch (direction) {
            case 0:
                g.fill3DRect(x - 20, y - 30, 10, 60, false);
                g.fill3DRect(x - 10, y - 20, 20, 40, false);
                g.fill3DRect(x + 10, y - 30, 10, 60, false);
                g.fillOval(x - 10, y - 10, 20, 20);
                g.drawLine(x, y, x, y - 30);
                break;
            case 1:
                g.fill3DRect(x - 20, y - 30, 10, 60, false);
                g.fill3DRect(x - 10, y - 20, 20, 40, false);
                g.fill3DRect(x + 10, y - 30, 10, 60, false);
                g.fillOval(x - 10, y - 10, 20, 20);
                g.drawLine(x, y, x, y + 30);
                break;
            case 2:
                g.fill3DRect(x - 30, y - 20, 60, 10, false);
                g.fill3DRect(x - 20, y - 10, 40, 20, false);
                g.fill3DRect(x - 30, y + 10, 60, 10, false);
                g.fillOval(x - 10, y - 10, 20, 20);
                g.drawLine(x, y, x - 30, y);
                break;
            case 3:
                g.fill3DRect(x - 30, y - 20, 60, 10, false);
                g.fill3DRect(x - 20, y - 10, 40, 20, false);
                g.fill3DRect(x - 30, y + 10, 60, 10, false);
                g.fillOval(x - 10, y - 10, 20, 20);
                g.drawLine(x, y, x + 30, y);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                friendTank.setDirection(0);
                friendTank.moveUp();
                break;
            case KeyEvent.VK_S:
                friendTank.setDirection(1);
                friendTank.moveDown();
                break;
            case KeyEvent.VK_A:
                friendTank.setDirection(2);
                friendTank.moveLeft();
                break;
            case KeyEvent.VK_D:
                friendTank.setDirection(3);
                friendTank.moveRight();
                break;
            case KeyEvent.VK_J:
                friendTank.fire();
                break;
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            repaint();
        }
    }
}
