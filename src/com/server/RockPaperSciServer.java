package com.server;

import com.client.RockPaperSciConstant;

import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Created by Thomas on 20/01/2015.
 */
public class RockPaperSciServer extends JFrame implements RockPaperSciConstant{
    private ServerSocket serverSocket;
    private Socket player1;
    private Socket player2;
    private GameSession game;

    public RockPaperSciServer(){
        JTextArea serverLog = new JTextArea();

        JScrollPane scrollPane = new JScrollPane(serverLog);

        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,300);
        setTitle("THE SERVER!!!!");
        setVisible(true);

        try{
            serverSocket = new ServerSocket(8000);

            serverLog.append(new Date() + ": Server has started on socket 8000 YEAH!\n");

            int sessionNo = 1;

            while(true){
                serverLog.append(new Date() + ": wait for players to join the session " + sessionNo +"\n" );

                player1 = serverSocket.accept();

                serverLog.append(new Date() + ": Player 1 joined session " + sessionNo + '\n');

                serverLog.append("Player 1's IP address" + player1.getInetAddress().getHostAddress() + '\n');

                new DataOutputStream(player1.getOutputStream()).writeInt(PLAYER1);

                player2 = serverSocket.accept();

                serverLog.append(new Date() + ": Player 2 joined session " + sessionNo + '\n');

                serverLog.append("Player 2's IP address" + player2.getInetAddress().getHostAddress() + '\n');

                new DataOutputStream(player2.getOutputStream()).writeInt(PLAYER2);

                serverLog.append(new Date() + ": Start a thread for session " + sessionNo++ + '\n');

                game = new GameSession(player1, player2);

                new Thread(game).start();


            }
        }
        catch(IOException ex) {
            System.err.println(ex);
        }
    }
    public static void main(String[] args) {

        RockPaperSciServer frame = new RockPaperSciServer();
    }

}

