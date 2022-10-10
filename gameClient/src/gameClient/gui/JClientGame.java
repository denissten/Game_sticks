package gameClient.gui;

import gameClient.Client;
import gameServer.engine.GameBoard;
import gameServer.engine.Letter;
import gameServer.engine.Line;
import gameServer.server.GameServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;

public class JClientGame extends JPanel {
    GameServer gameServer;
    Client client;
    GameBoard gameBoard;
    private boolean block = true;

    public JClientGame(GameServer gameServer, Client client) throws RemoteException {
        this.gameServer = gameServer;
        this.client = client;
        this.setSize(559, 581);
        this.gameBoard = this.gameServer.getGameBoard();
        this.addMouseListener(new MouseEventsListener());
        this.setBackground(Color.BLUE);
    }

    public void updateGameBoard(GameBoard gameBoard){
        this.gameBoard = gameBoard;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Line[][] l = gameBoard.getLines();
        BasicStroke stroke = new BasicStroke(GameBoard.getW());
        for (Line[] lines : l) {
            for (Line line : lines) {
                if (line == null) continue;
                g2d.setStroke(stroke);
                g2d.setPaint(line.color);
                g2d.drawLine(line.startX, line.startY, line.endX, line.endY);
            }
        }
        g2d.setPaint(Color.black);
        Point[][] cord = gameBoard.getCircleCoords();
        for (Point[] points : cord) {
            for (Point point : points) {
                g2d.fillOval(point.x, point.y, 21, 21);
            }
        }

        Letter[][] letters = gameBoard.getLetters();
        g2d.setFont(Letter.FONT);
        for (Letter[] letters1 : letters) {
            for (Letter L : letters1) {
                g2d.setPaint(L.color);
                g2d.drawString(L.letter,L.x, L.y);
            }
        }
    }
    class MouseEventsListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            if (block) return;
            int x = e.getX(), y = e.getY();
            Line[][] l = gameBoard.getLines();
            for (int i = 0; i < l.length; i++) {
                for (int j = 0; j < l[i].length; j++) {
                    if (l[i][j] == null) continue;
                    if (l[i][j].block) continue;
                    if (l[i][j].isLine(x, y)) {
                        try {
                            gameServer.lineClick(client.getId(), i, j);
                            return;
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
