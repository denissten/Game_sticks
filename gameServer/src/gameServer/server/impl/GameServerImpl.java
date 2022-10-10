package gameServer.server.impl;

import gameServer.engine.GameBoard;
import gameServer.engine.Player;
import gameServer.engine.ServerCallbacks;
import gameServer.server.GameServer;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {
    public static final String RMI_NAME = "GameServer";
    private static List<Player> playerList = new ArrayList<>();
    private static int playerCount = 0;
    private static HashMap<Integer, ServerCallbacks> callbacks = new HashMap<>();
    private static Scanner scanner;
    private static GameBoard gameBoard;
    private static final Color[] COLORS = new Color[]{Color.red, Color.blue, Color.YELLOW, Color.GREEN};


    public GameServerImpl() throws RemoteException{
        scanner = new Scanner(System.in);
        gameBoard = new GameBoard();
    }
    public static void main(String[] args) {

        System.out.println("> Start server");
        if (System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }
        try {
            GameServer gameServer = new GameServerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(RMI_NAME, gameServer);
            System.out.println("> " + RMI_NAME + " bound");
            System.out.println("> Waiting for connection");
            while (true){
                String cmd = scanner.nextLine();
                if (cmd.equals("start")){
                    if (playerCount <= 0){
                        System.out.println("> Not enough players");
                        continue;
                    }
                    gameServer.getGameBoard().fillLinesField();
                    gameServer.startGame();
                    gameBoard.startGame();
                    continue;
                }
                if (cmd.equals("end")){
                    break;
                }
            }
        } catch (Exception exception){
            System.out.println("> Error starting server: " + exception.getMessage());
            exception.printStackTrace();
        }


    }
    public static void setBlock(int id, boolean block){
        try {
            callbacks.get(id).setBlock(block);
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }
    @Override
    public int addPlayer(String nickname) throws RemoteException {
        Player player = new Player(nickname, COLORS[playerCount >= COLORS.length ? 0 : playerCount]);
        playerList.add(player);
        playerCount++;
        gameBoard.addNewPlayer(player);
        System.out.println("> Player connected. [ID " + playerCount + "]");
        return playerCount;
    }
    @Override
    public GameBoard getGameBoard() throws RemoteException{
        return gameBoard;
    }
    public static List<Player> getPlayerList() {
        return GameServerImpl.playerList;
    }
    @Override
    public void addServerCallBacks(int id, ServerCallbacks callbacks) throws RemoteException {
        GameServerImpl.callbacks.put(id, callbacks);
    }
    @Override
    public void startGame() throws RemoteException {
        for (Player player : playerList){
            try {
                callbacks.get(player.getId()).startGame(gameBoard, player.getNickname());
            } catch (Exception exception){
                callbacks.remove(player.getId());
                playerList.remove(player);
            }
        }
    }
    @Override
    public void lineClick(int id, int i, int j) throws RemoteException {
        gameBoard.changeLineStatus(id, i, j);
        updateGameBoard();
    }
    @Override
    public void updateGameBoard() throws RemoteException {
        for (Player player : playerList){
            try {
                callbacks.get(player.getId()).gameBoardChange(gameBoard);
            } catch (Exception exception){
                callbacks.remove(player.getId());
                playerList.remove(player);
            }
        }
    }

    @Override
    public void test(){
        for(int i = 0; i < this.gameBoard.getLines().length; i++) {
            for (int j = 0; j < this.gameBoard.getLines()[0].length; j++){
                if (this.gameBoard.getLines()[i][j] != null)
                System.out.print(gameBoard.getLines()[i][j].horizontal + " ");
                else System.out.print("+ ");
            }
            System.out.println();
        }
    }
}
