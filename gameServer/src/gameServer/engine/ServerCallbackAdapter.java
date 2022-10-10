package gameServer.engine;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class ServerCallbackAdapter extends UnicastRemoteObject implements ServerCallbacks {
    protected ServerCallbackAdapter() throws RemoteException {
        super();
    }
    public abstract void startGame(GameBoard gameBoard, String nickname) throws RemoteException;
    public abstract void gameBoardChange(GameBoard gameBoard) throws RemoteException;
    public abstract void setBlock(boolean block) throws RemoteException;
}
