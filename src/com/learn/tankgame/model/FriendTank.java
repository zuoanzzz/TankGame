package com.learn.tankgame.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoulei
 * @date 2022/10/31
 * friendly tanks
 */
public class FriendTank extends Tank{
    public FriendTank(int x, int y, int direction) {
        super(x, y, direction);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

}
