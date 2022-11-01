package com.learn.tankgame;

import javax.swing.*;

/**
 * @author zhoulei
 * @date 2022/10/31
 * main class
 */
public class TankGame extends JFrame {
    private GameArea gameArea = null;

    public static void main(String[] args) {
        TankGame tankGame = new TankGame();
    }

    public TankGame() {
        gameArea = new GameArea();
        this.add(gameArea);
        this.addKeyListener(gameArea);
        this.setSize(1000, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}

