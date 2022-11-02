package com.learn.tankgame.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoulei
 * @date 2022/10/31
 * friendly tanks
 */
public class FriendTank extends Tank{
    private List<Bullet> bullets = new ArrayList<>();
    public FriendTank(int x, int y, int direction) {
        super(x, y, direction);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
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
