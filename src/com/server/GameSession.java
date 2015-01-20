package com.server;

import com.client.RockPaperSciConstant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Thomas on 18/01/2015.
 */
public class GameSession implements Runnable,RockPaperSciConstant {
    private Socket player1;
    private Socket player2;

    private DataInputStream fromPlayer1;
    private DataOutputStream toPlayer1;
    private DataInputStream fromPlayer2;
    private DataOutputStream toPlayer2;

    private boolean continueToPlay = true;


    public GameSession(Socket player1, Socket player2){
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        try{
            DataInputStream fromPlayer1 = new DataInputStream(player1.getInputStream());
            DataOutputStream toPlayer1 = new DataOutputStream(player1.getOutputStream());
            DataInputStream fromPlayer2 = new DataInputStream(player2.getInputStream());
            DataOutputStream toPlayer2 = new DataOutputStream(player2.getOutputStream());

            toPlayer1.writeInt(CONTINUE);

            while(true) {
                int selection1 = fromPlayer1.readInt();
                System.out.println(selection1);
                break;
            }
                toPlayer2.writeInt(CONTINUE);
                int selection2 = fromPlayer2.readInt();
                System.out.println(selection2);
        }
        catch(IOException ex) {
            System.err.println(ex);
        }

    }






//
//    private void sendMove(DataOutputStream out, int selection) throws IOException{
//        out.writeInt(selection);
//    }
//



}
