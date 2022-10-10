package gameServer.engine;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerCallbacks extends Remote, Serializable {
    public void startGame(GameBoard gameBoard, String nickname) throws RemoteException;
    public void gameBoardChange(GameBoard gameBoard) throws RemoteException;
    public void setBlock(boolean block) throws RemoteException;
}

