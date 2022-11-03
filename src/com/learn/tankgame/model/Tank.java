package com.learn.tankgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author zhoulei
 * @date 2022/10/31
 * all tanks father
 */
public class Tank {
    private int x;
    private int y;
    private int direction;
    private int speed = 3;

    private boolean isAlive = true;

    protected Vector<Bullet> bullets = new Vector<>();   //for thread safe

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Tank(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveUp() {
        if (y > 30) {
            y -= speed;
        }
    }

    public void moveDown() {
        if (y < 680) {
            y += speed;
        }
    }

    public void moveLeft() {
        if (x > 30) {
            x -= speed;
        }
    }

    public void moveRight() {
        if (x < 950) {
            x += speed;
        }
    }

    public void fire() {
        Bullet bullet = null;
        switch (this.getDirection()) {
            case 0:
                bullet = new Bullet(this.getX(), this.getY() - 30, this.getDirection());
                break;
            case 1:
                bullet = new Bullet(this.getX(), this.getY() + 30, this.getDirection());
                break;
            case 2:
                bullet = new Bullet(this.getX() - 30, this.getY(), this.getDirection());
                break;
            case 3:
                bullet = new Bullet(this.getX() + 30, this.getY(), this.getDirection());
                break;
        }
        new Thread(bullet).start();
        bullets.add(bullet);
    }

}
