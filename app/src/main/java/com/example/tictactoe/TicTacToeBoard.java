package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TicTacToeBoard extends View {

    private final int board_color;
    private final int x_color;
    private final int o_color;
    private final int winning_line_color;

    private boolean winningLine = false;

    private final Paint paint = new Paint();

    private final GameLogic game;

    private int cellSize = getWidth() / 3;

    public TicTacToeBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        game = new GameLogic();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TicTacToeBoard, 0, 0);

        try{
            board_color = a.getInteger(R.styleable.TicTacToeBoard_board_color, 0);
            x_color = a.getInteger(R.styleable.TicTacToeBoard_x_color, 0);
            o_color = a.getInteger(R.styleable.TicTacToeBoard_o_color, 0);
            winning_line_color = a.getInteger(R.styleable.TicTacToeBoard_winning_line_color, 0);
        }finally{
            a.recycle();
        }

    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);

        //verifies which one is smaller, screen width or screen height.
        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());

        cellSize = dimension/3;

        //sets the smaller value as the Tic tac toe width and height.
        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        drawGameBoard(canvas);

        drawMarkers(canvas);

        if(winningLine) {
            paint.setColor(winning_line_color);
            drawWinningLine(canvas);
        }
    }

    //function called on screen touch
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //gets the touch position
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN) {
            int row = (int) Math.ceil(y/cellSize);
            int col = (int) Math.ceil(x/cellSize);

            if(!winningLine) {
                if(game.updateGameBoard(row, col)) {
                    invalidate();

                    if(game.winnerCheck()){
                        winningLine = true;
                        invalidate();
                    }

                    //alter the player turn
                    if(game.getPlayer() % 2 == 0) {
                        game.setPlayer(game.getPlayer() - 1);
                    } else {
                        game.setPlayer(game.getPlayer() + 1);
                    }
                }
            }


            return true;
        }

        return false;
    }

    private void drawGameBoard(Canvas canvas) {
        paint.setColor(board_color);
        paint.setStrokeWidth(9);
        //draw columns
        for(int col=1; col<3; col++) {
            canvas.drawLine(
                    cellSize*col,
                    0,
                    cellSize*col,
                    canvas.getHeight(),
                    paint
            );
        }
        //draw rows
        for(int row=1; row<3; row++) {
            canvas.drawLine(
                    0,
                    cellSize*row,
                    canvas.getWidth(),
                    cellSize*row,
                    paint
            );
        }
    }

    private void drawMarkers(Canvas canvas) {
        for(int row=0; row<3;row++) {
            for(int col=0; col<3;col++) {
                if(game.getGameBoard()[row][col] != 0) {
                    if(game.getGameBoard()[row][col] == 1) {
                        drawX(canvas, row, col);
                    } else {
                        drawO(canvas, row, col);
                    }
                }
            }
        }
    }

    private void drawX(Canvas canvas, int row, int col) {
        paint.setColor(x_color);
        paint.setStrokeWidth(18);

        canvas.drawLine(
                (col+1)*cellSize - cellSize*0.2f,
                row*cellSize + cellSize*0.2f,
                col*cellSize + cellSize*0.2f,
                (row+1)*cellSize - cellSize*0.2f,
                paint
        );

        canvas.drawLine(
                col*cellSize + cellSize*0.2f,
                row*cellSize + cellSize*0.2f,
                (col+1)*cellSize - cellSize*0.2f,
                (row+1)*cellSize - cellSize*0.2f,
                paint
        );
    }

    private void drawO(Canvas canvas, int row, int col) {
        paint.setColor(o_color);
        paint.setStrokeWidth(18);

        canvas.drawOval(
                col*cellSize + cellSize*0.2f,
                row*cellSize + cellSize*0.2f,
                (col*cellSize + cellSize) - cellSize*0.2f,
                (row*cellSize + cellSize) - cellSize*0.2f,
                paint
        );
    }

    private void drawHorizontalLine(Canvas canvas, int row, int col) {
        paint.setStrokeWidth(9);
        canvas.drawLine(col, row*cellSize+(float)cellSize/2,
                cellSize*3, row*cellSize+(float)cellSize/2, paint);
    }

    private void drawVerticalLine(Canvas canvas, int row, int col) {
        paint.setStrokeWidth(9);
        canvas.drawLine(col*cellSize+(float)cellSize/2, row,
                col*cellSize+(float)cellSize/2, cellSize*3, paint);
    }

    private void drawDiagonalLinePositive(Canvas canvas) {
        paint.setStrokeWidth(9);
        canvas.drawLine(0, cellSize*3, cellSize*3, 0, paint);
    }

    private void drawDiagonalLineNegative(Canvas canvas) {
        paint.setStrokeWidth(9);
        canvas.drawLine(0, 0, cellSize*3, cellSize*3, paint);
    }

    private void drawWinningLine(Canvas canvas) {
        int row = game.getWinType()[0];
        int col = game.getWinType()[1];

        switch(game.getWinType()[2]) {
            case 1:
                drawHorizontalLine(canvas, row, col);
                break;
            case 2:
                drawVerticalLine(canvas, row, col);
                break;
            case 3:
                drawDiagonalLineNegative(canvas);
                break;
            case 4:
                drawDiagonalLinePositive(canvas);
                break;
        }
    }

    public void setUpGame(Button playAgain, Button home, TextView playerDisplay, String[] names) {
        game.setPlayAgainBtn(playAgain);
        game.setHomeBtn(home);
        game.setPlayerTurn(playerDisplay);
        game.setPlayerNames(names);
    }

}
