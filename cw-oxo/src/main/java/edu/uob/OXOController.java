package edu.uob;

public class OXOController {
    OXOModel gameModel;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {
        command = command.toLowerCase();
        int col = 0;
        int row = 0;
        if (command.matches("[a-c][1-3]")) {
            //convert the command to int row and col
            row = command.charAt(0) - 'a';
            col = Integer.parseInt(command.substring(1)) - 1;
            if (gameModel.getCellOwner(row, col) != null) {
                throw new OXOMoveException.CellAlreadyTakenException(row, col);
            }
            int currentPlayerNumber = gameModel.getCurrentPlayerNumber();
            OXOPlayer currentPlayer = (OXOPlayer) gameModel.getPlayerByNumber(currentPlayerNumber);
            //add o or x on the board.
            gameModel.setCellOwner(row, col, currentPlayer);
            //switch player
            if(currentPlayerNumber==1){
                gameModel.setCurrentPlayerNumber(0);
            }else{
                gameModel.setCurrentPlayerNumber(1);
            }
        }else {
            throw new OXOMoveException.InvalidIdentifierLengthException(command.length());
        }


    }
    public void addRow() {}
    public void removeRow() {}
    public void addColumn() {}
    public void removeColumn() {}
    public void increaseWinThreshold() {}
    public void decreaseWinThreshold() {}
    public void reset() {
        //detect key pressed ESC???

        //clear board
        for(int row=0;row<gameModel.getNumberOfRows();row++){
            for(int col=0;col<gameModel.getNumberOfColumns();col++){
                gameModel.setCellOwner(row,col,null);
            }
        }
        // Reset other game state variables
        gameModel.setCurrentPlayerNumber(0);
        gameModel.setWinner(null);
        gameModel.setGameDrawn(false);
    }
}
