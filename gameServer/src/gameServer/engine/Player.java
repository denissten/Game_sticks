package gameServer.engine;

import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {
    static int playerCount = 0;
    String nickname;
    int id;
    private int score = 0;
    private Color color;

    public Player(String nickname, Color color){
        playerCount++;
        this.nickname = nickname+"#"+playerCount;
        this.id = playerCount;
        this.color = color;
    }
    public int getId() {
        return id;
    }
    public String getNickname() {
        return nickname;
    }
    public int getScore() {
        return score;
    }
    public void addToScore(int i){
        score+=i;
    }

    public Color getColor() {
        return color;
    }
}
