package com.learn.tankgame.model;

import java.util.List;

/**
 * @author zhoulei
 * @date 2022/10/31
 */
public class EnemyTank extends Tank implements Runnable {
    public EnemyTank(int x, int y, int direction) {
        super(x, y, direction);
    }

    public List<Bullet> getBullets() {
        return bullets;
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
            switch (getDirection()){
                case 0:
                    if(ran1<3){
                        moveUp();
                    }else {
                        setDirection(ran2);
                    }
                case 1:
                    if(ran1<3){
                        moveDown();
                    }else {
                        setDirection(ran2);
                    }
                case 2:
                    if(ran1<3){
                        moveLeft();
                    }else {
                        setDirection(ran2);
                    }
                case 3:
                    if(ran1<3){
                        moveRight();
                    }else {
                        setDirection(ran2);
                    }
            }
        }
    }
}
