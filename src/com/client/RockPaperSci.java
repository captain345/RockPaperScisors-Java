package com.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.*;
import java.net.*;
/**
 * Created by Thomas on 17/01/2015.
 * This code is the client side of the system and works with a variety of getter and setter methods,
 * as all variables are set to private they can only be altered using these methods
 *
 * 2 data streams are used to send data to the server
 *
 * action listeners are used in the class constructor, the class itself inherits from JFrame and the listeners are used to invoke actions when certain buttons are pressed
 *
 * send move takes the information that's input by the user and sends it to the server for processing
 *
 * receive move does much the same thing as send however it receives the move of the other player rather than sending it
 *
 *
 * receive information from server will let the system check if the server has assigned a winner for the game
 *
 * getters and setters do as the name implys
 */
public class RockPaperSci extends JFrame implements Runnable, RockPaperSciConstant {
    private boolean myTurn = false;

    private int mySelection = 0;
    private int otherSelection  = 0;

    private JLabel jlblTitle = new JLabel();

    private JLabel jlblStatus = new JLabel();

    private DataInputStream fromServer;
    private DataOutputStream toServer;

    private boolean continueToPlay = true;

    private boolean waiting = true;
    private String host = "localhost";

    public RockPaperSci(String title) {
        super(title);

        JPanel p = new JPanel();

        p.setLayout(new GridLayout(3, 0, 0, 0));
        p.setBorder(new LineBorder(Color.black, 1));

        JButton rock = new JButton("Rock");
        rock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStatusMessage("Waiting for the other player to move");
                setWaiting(false);         // Just completed a successful move
                setMySelection(ROCK);

            }
        });

        JButton paper = new JButton("Paper");
        paper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStatusMessage("Waiting for the other player to move");
                setWaiting(false);         // Just completed a successful move
                setMySelection(PAPER);
            }
        });

        JButton scissors = new JButton("Scissors");
        scissors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStatusMessage("Waiting for the other player to move");
                setWaiting(false);         // Just completed a successful move
                setMySelection(SCISSORS);
            }
        });

        p.add(rock);
        p.add(paper);
        p.add(scissors);

        jlblTitle.setHorizontalAlignment(JLabel.CENTER);
        jlblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        jlblTitle.setBorder(new LineBorder(Color.black, 1));
        jlblStatus.setBorder(new LineBorder(Color.black, 1));

        setLayout(new BorderLayout()); // implicit anyway

        add(jlblTitle, BorderLayout.NORTH);
        add(p, BorderLayout.CENTER);
        add(jlblStatus, BorderLayout.SOUTH);

        // Connect to the server
        connectToServer();
    }
        private void connectToServer() {

            try {
                // Create a socket to connect to the server

                Socket socket;

                socket = new Socket(host, 8000);

                // Create an input stream to receive data from the server
                fromServer = new DataInputStream(socket.getInputStream());

                // Create an output stream to send data to the server
                toServer = new DataOutputStream(socket.getOutputStream());
            }
            catch (Exception ex) {
                System.err.println(ex);
            }

            // Control the game on a separate thread
            Thread thread = new Thread(this);
            thread.start();
        }

    public void run() {

        try {

            // Get notification from the server

            int player = fromServer.readInt();

            // Am I player 1 or 2?

            if (player == PLAYER1) {

                setTitleMessage("You're Player 1");
                setStatusMessage("Waiting for player 2 to join");

                // Receive startup notification from the server

                fromServer.readInt(); // Whatever read is ignored here, but this is the first CONTNUE message
                // this is to tell the client to effectively start or continue now a 2nd player has joined

                // The other player has joined
                setStatusMessage("Player 2 has joined. I start first");

                // It is my turn
                setMyTurn(true);
            }

            else if (player == PLAYER2) {

                 setTitleMessage("You're Player 2");
                setStatusMessage("Waiting for player 1 to move");
            }

            // Continue to play

            while (continueToPlay) {

                if (player == PLAYER1) {
                    waitForPlayerAction(); // Wait for player 1 to move
                    sendMove(); // Send the move to the server
                    recieveMove(); // Receive info from the server
                    receiveInfoFromServer();

                }
                else if (player == PLAYER2) {
                    recieveMove(); // Receive info from the server
                    waitForPlayerAction(); // Wait for player 2 to move
                    sendMove(); // Send player 2's move to the server
                    receiveInfoFromServer();
                }
            }
        }
        catch (Exception ex) {
        }
    }

    private void waitForPlayerAction() throws InterruptedException {

        while (isWaiting()) { // we are effectively "polling" on a wait flag

            Thread.sleep(100);
        }
        // when we reach this point, the local player must have selected a board position, so proceed
        // and send on the move to the server side
        setWaiting(true);
    }


    private void sendMove() throws IOException {
        toServer.writeInt(getMySelection()); // Send the selected int
    }

    private void recieveMove() throws IOException, InterruptedException {

        // Receive game status - this will either be a win message, a draw message or a continue message

        int i = fromServer.readInt();

        setOtherSelection(i);
        setStatusMessage("My turn");
        setMyTurn(true); // It is my turn
    }
    private void receiveInfoFromServer() throws IOException {
        int status = fromServer.readInt();

        if (status == PLAYER1_WON) {
            // Player 1 won, stop playing
            continueToPlay = false;
            setStatusMessage("Player 1 WON!!!");
            System.out.println("1 Won");

        }
        else if (status == PLAYER2_WON) {
            // Player 2 won, stop playing
            continueToPlay = false;
            setStatusMessage("Player 2 WON");
            System.out.println("2 Won");


        }
        else if (status == DRAW) {
            // No winner, game is over
            continueToPlay = false;
            setStatusMessage("Game is over, no winner!");
            System.out.println("Draw");


        }


    }

    public static void main(String[] args) {

        // Create a frame
        RockPaperSci frame = new RockPaperSci("Rock Paper Scissors");

        // Display the frame
        frame.setSize(320, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }



    // accessors/mutators

    public void setMyTurn(boolean b) {
        myTurn = b;
    }

    public void setOtherSelection(int i){
        otherSelection = i;
    }

    public void setMySelection(int i){
        mySelection = i;
    }

    public int getMySelection(){
        return mySelection;
    }

    public void setWaiting(boolean b) {
        waiting = b;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setStatusMessage(String msg) {
        jlblStatus.setText(msg);
    }

    public void setTitleMessage(String msg) {
        jlblTitle.setText(msg);
    }


}




