package com.learn.tankgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * @author zhoulei
 * @date 2022/10/31
 * main class
 */
public class TankGame extends JFrame {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TankGame tankGame = new TankGame();
    }

    public TankGame() throws IOException, ClassNotFoundException {
        GameArea gameArea = new GameArea();
        new Thread(gameArea).start();
        this.add(gameArea);
        this.addKeyListener(gameArea);
        this.setSize(1200, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    Record.saveGame(gameArea.getFriendTank(),gameArea.getEnemyTanks());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        this.setVisible(true);
    }
}

