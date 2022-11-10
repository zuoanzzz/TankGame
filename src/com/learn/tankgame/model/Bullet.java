package com.learn.tankgame.model;

import java.io.Serializable;

/**
 * @author zhoulei
 * @date 2022/11/2
 */
public class Bullet implements Runnable, Serializable {
    private int x;
    private int y;
    private int direction;
    private int speed = 2;
    private boolean isAlive = true;

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

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

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

    public Bullet(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    @Override
    public void run() {
        while (isAlive) {
            switch (direction) {
                case 0:
                    y -= speed;
                    break;
                case 1:
                    y += speed;
                    break;
                case 2:
                    x -= speed;
                    break;
                case 3:
                    x += speed;
                    break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!(x >= 0 && x <= 1000 && y >= 0 && y <= 750)) {
                isAlive = false;
                break;
            }
        }
    }
}
