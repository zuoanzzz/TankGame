package com.learn.tankgame;

import com.learn.tankgame.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

/**
 * @author zhoulei
 * @date 2022/10/31
 * game area
 */
public class GameArea extends JPanel implements KeyListener, Runnable {
    private FriendTank friendTank = null;
    private Vector<EnemyTank> enemyTanks;          //for threads safe
    private int enemyNum = 3;
    private Vector<Bomb> bombs = new Vector<>();
    private Image[] images = new Image[3];

    public FriendTank getFriendTank() {
        return friendTank;
    }

    public Vector<EnemyTank> getEnemyTanks() {
        return enemyTanks;
    }

    public GameArea() throws IOException, ClassNotFoundException {
        System.out.println("1:新游戏;2:上局游戏");
        String choice = new Scanner(System.in).next();
        if (choice.equals("2")) {
            List list = Record.loadGame();
            friendTank = (FriendTank) list.get(0);
            enemyTanks = (Vector<EnemyTank>) list.get(1);
        }else {
            friendTank = new FriendTank(100, 100, 3);
            enemyTanks = new Vector<>();
            for (int i = 0; i < enemyNum; i++) {
                enemyTanks.add(new EnemyTank((i + 2) * 100, (i + 2) * 100, 0));
            }
        }
        friendTank.getBullets().clear();
        for (EnemyTank enemyTank : enemyTanks) {
            enemyTank.getBullets().clear();
            new Thread(enemyTank).start();
        }
        images[0] = Toolkit.getDefaultToolkit().getImage(GameArea.class.getResource("/bomb1.png"));  //获取图像
        images[1] = Toolkit.getDefaultToolkit().getImage(GameArea.class.getResource("/bomb2.png"));
        images[2] = Toolkit.getDefaultToolkit().getImage(GameArea.class.getResource("/bomb3.png"));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);

        if (friendTank.isAlive()) {
            drawTank(friendTank.getX(), friendTank.getY(), g, friendTank.getDirection(), 0);
        }

        Iterator<Bullet> bulletIterator = friendTank.getBullets().iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (bullet.isAlive()) {
                g.draw3DRect(bullet.getX(), bullet.getY(), 1, 1, false);
                for (int i = 0; i < enemyTanks.size(); i++) {
                    if (hitTank(enemyTanks.get(i), bullet))
                        break;
                }
            } else {
                bulletIterator.remove();
            }
        }

        Iterator<Bomb> bombIterator = bombs.iterator();
        while (bombIterator.hasNext()) {
            Bomb bomb = bombIterator.next();
            if (bomb.isAlive()) {
                if (bomb.getLifeTime() > 6) {
                    g.drawImage(images[0], bomb.getX() - 30, bomb.getY() - 30, 60, 60, this);
                } else if (bomb.getLifeTime() > 3) {
                    g.drawImage(images[1], bomb.getX() - 30, bomb.getY() - 30, 60, 60, this);
                } else if (bomb.getLifeTime() > 0) {
                    g.drawImage(images[2], bomb.getX() - 30, bomb.getY() - 30, 60, 60, this);
                }
            } else {
                bombIterator.remove();
            }
        }

        for (EnemyTank enemyTank : enemyTanks) {
            if (enemyTank.isAlive()) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirection(), 1);
                if (enemyTank.isMove())
                    move(enemyTank.getDirection(), enemyTank);
                for (Bullet bullet : enemyTank.getBullets()) {
                    if (bullet.isAlive())
                        g.draw3DRect(bullet.getX(), bullet.getY(), 1, 1, false);
                    hitPlayer(bullet);
                }
            }
        }

        showInfo(g);

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

    public void hitPlayer(Bullet bullet) {
        if (friendTank.isAlive()) {
            int x = bullet.getX();
            int y = bullet.getY();
            switch (friendTank.getDirection()) {
                case 0:
                case 1:
                    if ((x > (friendTank.getX() - 20)) && (x < (friendTank.getX() + 20))
                            && (y > (friendTank.getY() - 30)) && (y < (friendTank.getY() + 30))) {
                        bullet.setAlive(false);
                        friendTank.setAlive(false);
                        Bomb bomb = new Bomb(friendTank.getX(), friendTank.getY());
                        bombs.add(bomb);
                    }
                    break;
                case 2:
                case 3:
                    if ((x > (friendTank.getX() - 30)) && (x < (friendTank.getX() + 30))
                            && (y > (friendTank.getY() - 20)) && (y < (friendTank.getY() + 20))) {
                        bullet.setAlive(false);
                        friendTank.setAlive(false);
                        Bomb bomb = new Bomb(friendTank.getX(), friendTank.getY());
                        bombs.add(bomb);
                    }
                    break;
            }
        }
    }

    public boolean hitTank(EnemyTank enemyTank, Bullet bullet) {
        int x = bullet.getX();
        int y = bullet.getY();
        switch (enemyTank.getDirection()) {
            case 0:
            case 1:
                if ((x > (enemyTank.getX() - 20)) && (x < (enemyTank.getX() + 20))
                        && (y > (enemyTank.getY() - 30)) && (y < (enemyTank.getY() + 30))) {
                    bullet.setAlive(false);
                    enemyTank.setAlive(false);
                    enemyTanks.remove(enemyTank);
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                    Record.addScore();
                    return true;
                }
                return false;
            case 2:
            case 3:
                if ((x > (enemyTank.getX() - 30)) && (x < (enemyTank.getX() + 30))
                        && (y > (enemyTank.getY() - 20)) && (y < (enemyTank.getY() + 20))) {
                    bullet.setAlive(false);
                    enemyTank.setAlive(false);
                    enemyTanks.remove(enemyTank);
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                    Record.addScore();
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (friendTank.isAlive()) {
            boolean move = true;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    friendTank.setDirection(0);
                    for (EnemyTank enemyTank : enemyTanks) {
                        if (collide(friendTank, enemyTank)) {
                            move = false;
                            break;
                        }
                    }
                    if (move)
                        move(friendTank.getDirection(), friendTank);
                    break;
                case KeyEvent.VK_S:
                    friendTank.setDirection(1);
                    for (EnemyTank enemyTank : enemyTanks) {
                        if (collide(friendTank, enemyTank)) {
                            move = false;
                            break;
                        }
                    }
                    if (move)
                        move(friendTank.getDirection(), friendTank);
                    break;
                case KeyEvent.VK_A:
                    friendTank.setDirection(2);
                    for (EnemyTank enemyTank : enemyTanks) {
                        if (collide(friendTank, enemyTank)) {
                            move = false;
                            break;
                        }
                    }
                    if (move)
                        move(friendTank.getDirection(), friendTank);
                    break;
                case KeyEvent.VK_D:
                    friendTank.setDirection(3);
                    for (EnemyTank enemyTank : enemyTanks) {
                        if (collide(friendTank, enemyTank)) {
                            move = false;
                            break;
                        }
                    }
                    if (move)
                        move(friendTank.getDirection(), friendTank);
                    break;
                case KeyEvent.VK_J:
                    friendTank.fire();
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void move(int direction, Tank tank) {
        switch (direction) {
            case 0:
                tank.moveUp();
                break;
            case 1:
                tank.moveDown();
                break;
            case 2:
                tank.moveLeft();
                break;
            case 3:
                tank.moveRight();
                break;
        }
    }


    //以点为参考
    public boolean collide(Tank tank0, Tank tank1) {
        int x0 = tank0.getX();
        int y0 = tank0.getY();
        int x1 = tank1.getX();
        int y1 = tank1.getY();
        switch (tank0.getDirection()) {
            case 0:
                switch (tank1.getDirection()) {
                    case 0:
                    case 1:
                        return (((x0 + 20) > (x1 - 20)) && ((x0 + 20) < (x1 + 20)) && ((y0 - 30) > (y1 - 30)) && ((y0 - 30) < (y1 + 30)))
                                || (((x0 - 20) > (x1 - 20)) && ((x0 - 20) < (x1 + 20)) && ((y0 - 30) > (y1 - 30)) && ((y0 - 30) < (y1 + 30)));
                    case 2:
                    case 3:
                        return (((x0 + 20) > (x1 - 30)) && ((x0 + 20) < (x1 + 30)) && ((y0 - 30) > (y1 - 20)) && ((y0 - 30) < (y1 + 20)))
                                || (((x0 - 20) > (x1 - 30)) && ((x0 - 20) < (x1 + 30)) && ((y0 - 30) > (y1 - 20)) && ((y0 - 30) < (y1 + 20)));
                }
                break;
            case 1:
                switch (tank1.getDirection()) {
                    case 0:
                    case 1:
                        return (((x0 + 20) > (x1 - 20)) && ((x0 + 20) < (x1 + 20)) && ((y0 + 30) > (y1 - 30)) && ((y0 + 30) < (y1 + 30)))
                                || (((x0 - 20) > (x1 - 20)) && ((x0 - 20) < (x1 + 20)) && ((y0 + 30) > (y1 - 30)) && ((y0 + 30) < (y1 + 30)));
                    case 2:
                    case 3:
                        return (((x0 + 20) > (x1 - 30)) && ((x0 + 20) < (x1 + 30)) && ((y0 + 30) > (y1 - 20)) && ((y0 + 30) < (y1 + 20)))
                                || (((x0 - 20) > (x1 - 30)) && ((x0 - 20) < (x1 + 30)) && ((y0 + 30) > (y1 - 20)) && ((y0 + 30) < (y1 + 20)));
                }
                break;
            case 2:
                switch (tank1.getDirection()) {
                    case 0:
                    case 1:
                        return (((y0 - 20) > (y1 - 30)) && ((y0 - 20) < (y1 + 30)) && ((x0 - 30) > (x1 - 20)) && ((x0 - 30) < (x1 + 20)))
                                || (((y0 + 20) > (y1 - 30)) && ((y0 + 20) < (y1 + 30)) && ((x0 - 30) > (x1 - 20)) && ((x0 - 30) < (x1 + 20)));
                    case 2:
                    case 3:
                        return (((y0 - 20) > (y1 - 20)) && ((y0 - 20) < (y1 + 20)) && ((x0 - 30) > (x1 - 30)) && ((x0 - 30) < (x1 + 30)))
                                || (((y0 + 20) > (y1 - 20)) && ((y0 + 20) < (y1 + 20)) && ((x0 - 30) > (x1 - 30)) && ((x0 - 30) < (x1 + 30)));
                }
                break;
            case 3:
                switch (tank1.getDirection()) {
                    case 0:
                    case 1:
                        return (((y0 - 20) > (y1 - 30)) && ((y0 - 20) < (y1 + 30)) && ((x0 + 30) > (x1 - 20)) && ((x0 + 30) < (x1 + 20)))
                                || (((y0 + 20) > (y1 - 30)) && ((y0 + 20) < (y1 + 30)) && ((x0 + 30) > (x1 - 20)) && ((x0 + 30) < (x1 + 20)));
                    case 2:
                    case 3:
                        return (((y0 - 20) > (y1 - 20)) && ((y0 - 20) < (y1 + 20)) && ((x0 + 30) > (x1 - 30)) && ((x0 + 30) < (x1 + 30)))
                                || (((y0 + 20) > (y1 - 20)) && ((y0 + 20) < (y1 + 20)) && ((x0 + 30) > (x1 - 30)) && ((x0 + 30) < (x1 + 30)));
                }
                break;
        }
        return false;
    }

    public void showInfo(Graphics g) {
        g.setColor(Color.black);
        g.setFont(new Font("宋体", Font.BOLD, 25));
        g.drawString("击毁敌方坦克：", 1000, 30);
        drawTank(1040, 80, g, 0, 1);
        g.setColor(Color.black);
        g.drawString(Integer.toString(Record.getScore()), 1100, 90);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Iterator<Bomb> bombIterator = bombs.iterator();
            while (bombIterator.hasNext()) {
                Bomb bomb = bombIterator.next();
                if (bomb.isAlive()) {
                    bomb.lifeMinus();
                } else {
                    bombIterator.remove();
                }
            }

            for (int i = 0; i < enemyTanks.size(); i++) {
                for (int j = 0; j < enemyTanks.size(); j++) {
                    if (j != i) {
                        if (collide(enemyTanks.get(i), enemyTanks.get(j))) {
                            enemyTanks.get(i).setMove(false);
                            break;
                        } else
                            enemyTanks.get(i).setMove(true);
                    }
                }
                if (enemyTanks.get(i).isMove()) {
                    if (collide(enemyTanks.get(i), friendTank)) {
                        enemyTanks.get(i).setMove(false);
                    }
                }
            }

            repaint();
        }
    }
}
