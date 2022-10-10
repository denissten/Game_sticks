package gameServer.server;
import gameServer.engine.GameBoard;
import gameServer.engine.ServerCallbacks;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface GameServer extends Remote {
    public int addPlayer(String nickname) throws RemoteException;
    public void addServerCallBacks(int id, ServerCallbacks callbacks) throws RemoteException;
    public GameBoard getGameBoard() throws RemoteException;
    public void test() throws RemoteException;
    public void startGame() throws RemoteException;
    public void lineClick(int id, int i, int j) throws RemoteException;
    public void updateGameBoard() throws RemoteException;
}
