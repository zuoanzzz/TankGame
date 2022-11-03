package com.learn.tankgame.model;

/**
 * @author zhoulei
 * @date 2022/11/3
 */
public class Bomb{
    private int x;
    private int y;
    private boolean isAlive = true;
    private int lifeTime = 9;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void lifeMinus(){
        if(lifeTime>0)
            lifeTime--;
        else
            isAlive = false;
    }

}
