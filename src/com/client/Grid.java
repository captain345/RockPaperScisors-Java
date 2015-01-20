package com.client;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Thomas on 18/01/2015.
 */
public class Grid extends JPanel{

    private int row;


    private char token = ' ';

    private RockPaperSci parent;

    public Grid(int row, RockPaperSci gui){
        this.row = row;
        this.parent = gui;

        setBorder(new LineBorder(Color.black, 1));
        addMouseListener(new ClickListener());

    }

    public char getToken() {
        return token;
    }

    public void setToken(char c) {
        token = c;
        repaint();
    }


    private class ClickListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {

            // If cell is not occupied and the player has the turn

            if ((token == ' ') && parent.getMyTurn()) {

                setToken(parent.getMyToken());   // Set the player's token in the cell
                parent.setMyTurn(false);
                parent.setStatusMessage("Waiting for the other player to move");
                parent.setWaiting(false);         // Just completed a successful move

            }
        }
    }
}
