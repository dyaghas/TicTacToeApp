package com.example.tictactoe;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameLogic {

    private int[][] gameBoard;
    private int player = 1;

    private String[] playerNames = {"player1", "player2"};

    //1º element = row | 2º element = col | 3º element = line type
    private int[] winType = {-1, -1, -1};

    private Button playAgainBtn;
    private Button homeBtn;
    private TextView playerTurn;

    GameLogic() {
        gameBoard = new int[3][3];
        for(int row=0; row<3;row++) {
            for(int col=0; col<3;col++) {
                gameBoard[row][col] = 0;
            }
        }
    }

    public boolean updateGameBoard(int row, int col) {
        if(gameBoard[row-1][col-1] == 0) {
            gameBoard[row-1][col-1] = player;

            if(player == 1) {
                playerTurn.setText((playerNames[1] + "'s turn"));
            } else {
                playerTurn.setText((playerNames[0] + "'s turn"));
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean winnerCheck() {
        boolean isWinner = false;

        //check rows | winType == 1
        for(int row=0; row<3; row++) {
            if(gameBoard[row][0] == gameBoard[row][1] && gameBoard[row][1] == gameBoard[row][2] &&
            gameBoard[row][0] != 0) {
                winType = new int[] {row, 0, 1};
                isWinner = true;
            }
        }

        //check cols | winType == 2
        for(int col=0; col<3; col++) {
            if(gameBoard[0][col] == gameBoard[1][col] && gameBoard[1][col] == gameBoard[2][col] &&
            gameBoard[0][col] != 0) {
                winType = new int[] {0, col, 2};
                isWinner = true;
            }
        }

        //check negative diagonal | winType == 3
        if(gameBoard[0][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][2] &&
        gameBoard[0][0] != 0) {
            winType = new int[] {0, 2, 3};
            isWinner = true;
        }

        //check positive diagonal | winType == 4
        if(gameBoard[0][2] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][0] &&
                gameBoard[0][2] != 0) {
            winType = new int[] {2, 2, 4};
            isWinner = true;
        }

        int boardFilled = 0;

        for(int row=0; row<3;row++) {
            for(int col=0; col<3;col++) {
                if(gameBoard[row][col] != 0) {
                    boardFilled+=1;
                }
            }
        }

        if(isWinner) {
            playAgainBtn.setVisibility(View.VISIBLE);
            homeBtn.setVisibility(View.VISIBLE);
            playerTurn.setText(playerNames[player-1] + " won!");
            return true;
        }
        else if(boardFilled == 9) {
            playAgainBtn.setVisibility(View.VISIBLE);
            homeBtn.setVisibility(View.VISIBLE);
            playerTurn.setText("Tie!");
            return true;
        } else {
            return false;
        }
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayAgainBtn(Button playAgainBtn) {
        this.playAgainBtn = playAgainBtn;
    }

    public void setHomeBtn(Button homeBtn) {
        this.homeBtn = homeBtn;
    }

    public void setPlayerTurn(TextView playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public int[] getWinType() {
        return winType;
    }
}
