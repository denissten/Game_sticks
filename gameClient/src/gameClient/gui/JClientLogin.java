package gameClient.gui;

import gameClient.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JClientLogin extends JPanel {
    Client client;
    JTextField hostField;
    JTextField portField;
    JTextField nicknameField;
    JButton connectButton;
    JLabel statusLabel;

    public JClientLogin(Client client){
        this.client = client;
        this.setSize(300, 300);
        this.setBackground(Color.darkGray);
        hostField = new JTextField("127.0.0.1",16);
        portField = new JTextField("1099", 5);
        nicknameField = new JTextField(22);
        connectButton = new JButton("Connect");
        connectButton.addActionListener( new ConnectButtonActionListener());
        statusLabel = new JLabel("Fill in all fields.");
        statusLabel.setForeground(Color.red);
        statusLabel.setFont(new Font("Sans", Font.BOLD, 20));
        connectButton.setSize(new Dimension(200, 20));
        this.setLayout(new GridBagLayout());
        this.add(hostField, createGridBagConstrains(0, 0, 0,0, 2, 1,0 ,0));
        this.add(portField, createGridBagConstrains(0, 0, 2,0, 1, 1, 7,0));
        this.add(nicknameField, createGridBagConstrains(0, 0, 0,1, 3, 1,0,0));
        this.add(connectButton, createGridBagConstrains(0, 0, 0,2, 3, 1, 165, 0));
        this.add(statusLabel, createGridBagConstrains(0, 0, 0,3, 3, 1, 0, 0));
    }
    public void setStatusLabel(String text, Color color){
        statusLabel.setText(text);
        this.statusLabel.setForeground(color);
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
    public String getNickname(){
        return nicknameField.getText();
    }
    public Client getClient(){
        return client;
    }
    public void setEnableConnectButton(boolean disable){
        connectButton.setEnabled(disable);
    }

    class ConnectButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (hostField.getText().length() == 0 || portField.getText().length() == 0 || nicknameField.getText().length() == 0){
                statusLabel.setForeground(Color.red);
                statusLabel.setText("Fill in all fields.");
                return;
            }
            statusLabel.setForeground(Color.yellow);
            statusLabel.setText("Connecting....");
            client.connectToServer();
        }
    }
}