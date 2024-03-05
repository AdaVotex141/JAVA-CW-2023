package edu.uob;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OXOModel<OXOPlayer>{

    private ArrayList<ArrayList<OXOPlayer>> cells;
    private OXOPlayer[] players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        winThreshold = winThresh;
        cells = new ArrayList<>();
        for (int i = 0; i < numberOfRows; i++) {
            ArrayList<OXOPlayer> row = new ArrayList<>();
            // Add columns (OXOPlayer objects) to each row
            for (int j = 0; j < numberOfColumns; j++) {
                row.add(null);
            }
            cells.add(row);
        }
        //players =  new OXOPlayer[2];
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public void addPlayer(OXOPlayer player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                return;
            }
        }
    }
    public OXOPlayer getPlayerByNumber(int number) {
        return players[number];
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    public int getNumberOfRows() {
        return cells.size();
    }

    public int getNumberOfColumns() {
        return cells.get(0).size();
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        cells.get(rowNumber).set(colNumber, player);
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn(boolean isDrawn) {
        gameDrawn = isDrawn;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }

}
