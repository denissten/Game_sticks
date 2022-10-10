package gameServer.engine;
import gameServer.server.impl.GameServerImpl;

import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class GameBoard implements Serializable {
    private static final int WIDTH = 6;
    private static final int HEIGHT = 6;
    private static final int D = 21;
    private static final int STEP = 70;
    private static final int W = 5;
    private Line[][] lines = new Line[HEIGHT + 1][WIDTH * 2 + 1];
    private Letter[][] letters = new Letter[WIDTH][HEIGHT];
    private Point[][] circleCoords = new Point[WIDTH + 1][HEIGHT + 1];
    private Player curPlayer;
    private Queue<Player> playerQueue = new LinkedList<>();
    private int wait = 0;
    private String scoreBoard = "";
    private static int squareCount = 5;//WIDTH * HEIGHT;

    public Line[][] getLines() {
        return lines;
    }
    public Letter[][] getLetters() { return letters; }
    public Point[][] getCircleCoords() {
        return circleCoords;
    }
    public static int getW() {
        return W;
    }
    public static int getSTEP() { return STEP; }
    public static int getD() { return D; }

    public void fillLinesField(){
        for (int i = 0; i < WIDTH + 1; i++) {
            for (int j = 0; j < HEIGHT + 1; j++) {
                circleCoords[i][j] = new Point(50 + i * STEP, 50 + j * STEP);
                if (i != HEIGHT) {
                    int x1 = 50 + D - 10 + STEP * j, x2 = 50 + D - 10 + STEP * j,
                            y1 = 50 + D + STEP * i, y2 = 50 + D + 50 + STEP * i;
                    lines[i][j * 2] = new Line(x1, y1, x2, y2, W, false);
                }
                if (j != WIDTH ) {
                    int x1 = (50 + D) + STEP * j, x2 = 50 + D + 50 + STEP * j,
                    y1 = 50 + D - 10 + STEP * i, y2 = 50 + D - 10 + STEP * i;
                    lines[i][j * 2 + 1] = new Line(x1, y1, x2, y2, W, true);
                }
                if (j!= HEIGHT && i != WIDTH) {
                    letters[i][j] = new Letter(50 + j * STEP + 22, 50  + i * STEP + 57);
                }
            }
        }
        scoreBoard = "<html>Scoreboard:";
        for(Player player : GameServerImpl.getPlayerList()){
            scoreBoard += "<br>" + player.getNickname() + ": " + player.getScore();
        }
        scoreBoard += "</html>";
    }
    public void addNewPlayer(Player player){
        playerQueue.add(player);
    }
    public void startGame(){

        curPlayer = playerQueue.poll();
        GameServerImpl.setBlock(curPlayer.getId(), false);
    }
    public void nextPlayer(){
        GameServerImpl.setBlock(curPlayer.getId(), true);
        playerQueue.add(curPlayer);
        curPlayer = playerQueue.poll();
        GameServerImpl.setBlock(curPlayer.getId(), false);
    }
    private void updateScoreboard(){
        scoreBoard = "<html>Scoreboard:";
        for(Player player : GameServerImpl.getPlayerList()){
            scoreBoard += "<br>" + player.getNickname() + ": " + player.getScore();
        }
        scoreBoard += "</html>";
    }
    public String getScoreBoard() { return scoreBoard; }
    public void changeLineStatus(int id, int i, int j){
        if (id != curPlayer.getId()) return;
        if (lines[i][j].block) return;
        lines[i][j].block = true;
        lines[i][j].color = new Color(176, 0, 0);
        formedSquare(i, j);
        if (squareCount != 0){
            updateScoreboard();
            if (wait == 0) nextPlayer();
            else wait--;
        } else {
            endGame();
        }

    }
    private void endGame(){
        int maxS = 0; String nick = "";
        scoreBoard = "<html>Scoreboard:";
        for(Player player : GameServerImpl.getPlayerList()){
            GameServerImpl.setBlock(player.getId(), true);
            if (player.getScore() > maxS){
                maxS = player.getScore();
                nick = player.getNickname();
            }
        }
        for(Player player : GameServerImpl.getPlayerList()){
            if (player.getNickname().equals(nick))
                scoreBoard += "<br>" + player.getNickname() + ": " + player.getScore() + " - WINNER";
            else
                scoreBoard += "<br>" + player.getNickname() + ": " + player.getScore();
        }
        scoreBoard += "</html>";
        System.out.println("> Game ended");
    }
    private boolean lineIsBlock(int i, int j){
        if (i >= lines.length || i < 0 || j >= lines[i].length || j < 0) return false;
        if (lines[i][j] == null) return false;
        return lines[i][j].block;
    }
    public void formedSquare(int i, int j){
        if (lines[i][j].horizontal){
            if (lineIsBlock(i - 1,j - 1) && lineIsBlock(i - 1, j) && lineIsBlock(i - 1, j + 1) && lineIsBlock(i, j)){
                wait++;
                letters[i - 1][(j - 1)/2].letter = curPlayer.getNickname().substring(0, 1);
                letters[i - 1][(j - 1)/2].color = curPlayer.getColor();
                curPlayer.addToScore(1);
                squareCount--;
            }
            if (lineIsBlock(i,j - 1) && lineIsBlock(i + 1, j) && lineIsBlock(i, j + 1) && lineIsBlock(i, j)){
                wait++;
                letters[i][(j - 1)/2].letter = curPlayer.getNickname().substring(0, 1);
                letters[i][(j - 1)/2].color = curPlayer.getColor();
                curPlayer.addToScore(1);
                squareCount--;
            }
        } else {
            if (lineIsBlock(i,j - 2) && lineIsBlock(i, j - 1) && lineIsBlock(i + 1, j - 1) && lineIsBlock(i, j)){
                wait++;
                letters[i][(j - 1)/2].letter = curPlayer.getNickname().substring(0, 1);
                letters[i][(j - 1)/2].color = curPlayer.getColor();
                curPlayer.addToScore(1);
                squareCount--;
            }
            if (lineIsBlock(i,j + 2) && lineIsBlock(i, j + 1) && lineIsBlock(i + 1, j + 1) && lineIsBlock(i, j)){
                wait++;
                letters[i][(j + 1)/2].letter = curPlayer.getNickname().substring(0, 1);
                letters[i][(j + 1)/2].color = curPlayer.getColor();
                curPlayer.addToScore(1);
                squareCount--;
            }
        }
    }
}
