package com.learn.tankgame;

import com.learn.tankgame.model.EnemyTank;
import com.learn.tankgame.model.FriendTank;

import javax.xml.soap.Node;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author zhoulei
 * @date 2022/11/10
 */
public class Record {
    private static int score = 0;
    private static ObjectOutputStream oop = null;
    private static ObjectInputStream oip = null;

    public static int getScore() {
        return score;
    }

    public static String getPath() {
        return "src\\record.dat";
    }

    public static void addScore() {
        Record.score++;
    }

    public static void saveGame(FriendTank friendTank, Vector<EnemyTank> enemyTanks) throws IOException {
        oop = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(getPath())));
        oop.write(score);
        oop.writeObject(friendTank);
        oop.writeObject(enemyTanks);
        oop.close();
    }

    public static List loadGame() throws IOException, ClassNotFoundException {
        ArrayList<Object> list = new ArrayList<>();
        oip = new ObjectInputStream(new BufferedInputStream(new FileInputStream(getPath())));
        score = oip.read();
        list.add(oip.readObject());
        list.add(oip.readObject());
        oip.close();
        return list;
    }
}
