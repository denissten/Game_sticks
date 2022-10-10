package gameClient;
import gameClient.gui.JClientGame;
import gameClient.gui.JClientLogin;
import gameServer.engine.GameBoard;
import gameServer.engine.ServerCallbackAdapter;
import gameServer.server.GameServer;
import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    GameServer gameServer;
    JFrame jFrame;
    JClientLogin jClientLogin;
    JClientGame jClientGame;
    JLabel scoreBoard;
    private int id = -1;

    public static void main(String[] args) {
         Client client = new Client();
    }

    Client(){
        jFrame = new JFrame();
        jFrame.setTitle("Палочки");
        jFrame.setResizable(false);
        jClientLogin = new JClientLogin(this);
        jFrame.getContentPane().add(BorderLayout.CENTER, jClientLogin);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(300, 300);
        //jFrame.setSize(800, 800);
        jFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - jFrame.getWidth()/2,
                Toolkit.getDefaultToolkit().getScreenSize().height/2 - jFrame.getHeight()/2);
        jFrame.setVisible(true);
    }

    public int getId() {
        return id;
    }
    private GridBagConstraints createGridBagConstrains(double weightx, double weighty, int gridx, int gridy,
                                                       int gridwidth, int gridheight, int ipadx, int ipady){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = weightx;
        gridBagConstraints.weighty = weighty;
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.gridwidth = gridwidth;
        gridBagConstraints.gridheight = gridheight;
        gridBagConstraints.ipadx = ipadx;
        gridBagConstraints.ipady = ipady;
        return gridBagConstraints;
    }
    public void connectToServer(){
        if (id != -1) return;

        if (System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1");
            GameServer stub = (GameServer) registry.lookup("GameServer");
            jClientLogin.setStatusLabel("Waiting for the start.", Color.green);
            id = stub.addPlayer(jClientLogin.getNickname());
            jClientLogin.setEnableConnectButton(false);
            stub.addServerCallBacks(id, new ServerCallbackAdapter(){
                @Override
                public void startGame(GameBoard gameB, String nickname) throws RemoteException {
                    jClientLogin.setVisible(false);
                    jClientGame = new JClientGame(stub, jClientLogin.getClient());
                    scoreBoard = new JLabel(gameB.getScoreBoard());
                    scoreBoard.setFont(new Font("Sans", Font.BOLD, 20));
                    jFrame.setSize(559, 620);
                    jFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - jFrame.getWidth()/2,
                            Toolkit.getDefaultToolkit().getScreenSize().height/2 - jFrame.getHeight()/2);
                    jFrame.setTitle("Палочки | " + nickname);
                    jFrame.getContentPane().add(jClientGame, BorderLayout.CENTER);
                    jFrame.getContentPane().add(scoreBoard, BorderLayout.SOUTH);
                    jFrame.revalidate();
                    jFrame.repaint();
                }
                @Override
                public void gameBoardChange(GameBoard gameBoard) throws RemoteException {
                    jClientGame.updateGameBoard(gameBoard);
                    scoreBoard.setText(gameBoard.getScoreBoard());
                    jFrame.repaint();
                }
                @Override
                public void setBlock(boolean block) throws RemoteException {
                    jClientGame.setBlock(block);
                }
            });

        } catch (Exception exception){
            exception.printStackTrace();
            jClientLogin.setStatusLabel("Connection error.", Color.red);
        }

    }
}
