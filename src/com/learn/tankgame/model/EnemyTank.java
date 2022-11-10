package com.learn.tankgame.model;

import java.util.List;

/**
 * @author zhoulei
 * @date 2022/10/31
 */
public class EnemyTank extends Tank implements Runnable {
    private boolean isMove = true;
    public EnemyTank(int x, int y, int direction) {
        super(x, y, direction);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public boolean isMove() {
        return isMove;
    }

    public void setMove(boolean move) {
        isMove = move;
    }

    @Override
    public void run() {
        while (isAlive()) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            double ran1 = Math.random() * 4;
            int ran2 = (int)(Math.random() * 4);
            if(ran1<2){
                fire();
            }
            if(ran1>=2){
                setDirection(ran2);
            }
            if(isMove) {
                switch (getDirection()) {
                    case 0:
                        if (ran1 < 2) {
                            moveUp();
                        }
                    case 1:
                        if (ran1 < 2) {
                            moveDown();
                        }
                    case 2:
                        if (ran1 < 2) {
                            moveLeft();
                        }
                    case 3:
                        if (ran1 < 2) {
                            moveRight();
                        }
                }
            }
        }
    }
}
