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

    private char[] grid = new char[3];
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

            while(true){
                int player1Int = fromPlayer1.readInt();


                if(isWon(player1Int)){
                    toPlayer1.writeInt(PLAYER1_WON);
                    toPlayer2.writeInt(PLAYER1_WON);
                    sendMove(toPlayer2, player1Int );
                    break;
                }
                //need to write an else if here for a draw

                else{

                    toPlayer2.writeInt(CONTINUE);

                    sendMove(toPlayer2, player1Int);
                }

                int player2Int = fromPlayer2.readInt();



                if(isWon(player2Int)){
                    toPlayer1.writeInt(PLAYER2_WON);
                    toPlayer2.writeInt(PLAYER2_WON);
                    sendMove(toPlayer1, player2Int);
                    break;
                }
                else{
                    toPlayer1.writeInt(CONTINUE);

                    sendMove(toPlayer1, player2Int);
                }
            }
        }
        catch(IOException ex) {
            System.err.println(ex);
        }

    }

    private void sendMove(DataOutputStream out, int row) throws IOException{
        out.writeInt(row);
    }

    private boolean isDraw(){

        return false;
    }

    private boolean isWon(int token){
        for(int i =0; i<3;i++){

        }

        return false;
    }


}
