package com.kylehodgetts.sunka;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kylehodgetts.sunka.controller.GameManager;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/** @author: Phileas and future Chaz **/

public class BoardActivity extends AppCompatActivity {
    private static int gameMode;
    GameManager gameManager;
    GameState gameState;
    Player turn;
    Board board = new Board();
    Button bscoreB;Button bscoreA;
    Button buttons[][] = new Button[2][7];
    TextView tvPlayerA;TextView tvPlayerB;TextView tvDebugging;
    Player playerA ; Player playerB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        gameManager = new GameManager();
        playerA = new Player();playerB = new Player();
        gameState = new GameState(board,playerA,playerB,1);

        tvDebugging = (TextView)findViewById(R.id.tvDebugging);
        tvPlayerA = (TextView) findViewById(R.id.tvPlayerA);tvPlayerB=(TextView)findViewById(R.id.tvPlayerB);
        bscoreA = (Button) findViewById(R.id.buttonas);bscoreB = (Button) findViewById(R.id.buttonbs);

        makeXMLButton();
        bscoreA.setText("0");bscoreB.setText("0");
        if(gameMode == 2){
            turn = gameState.getCurrentPlayer();
        }
        wrapPlayer();
    }

    public void makeXMLButton(){
        GridLayout gridlayout = (GridLayout)findViewById(R.id.gridLayout);

        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 8; ++j) {

                if( !( i ==0 && j ==0 ) && !(i ==1 && j ==0) ) {
                    View view = (View) getLayoutInflater().inflate(R.layout.buttonlayouta, gridlayout, false);
                    if (i == 0) {
                        view = (View) getLayoutInflater().inflate(R.layout.buttonlayoutb, gridlayout, false);
                    }

                    Button vi = (Button) view;
                    if( i == 0){ j--;}
                    vi.setId(Integer.parseInt(i + "" + j));
                    if(i == 0){ j++;}
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                    param.columnSpec = GridLayout.spec(j);
                    param.rowSpec = GridLayout.spec(i);
                    vi.setLayoutParams(param);
                    gridlayout.addView(vi);
                    --j;

                    buttons[i][j] = vi;

                    buttons[i][j].setText("" + board.getTray(i, j));

                    ++j;
                }
            }
        }

    }

    int startCyclei; int startCyclej;
    public void buttonPlayableClick(View view){
        Button buttonSelected = (Button) view;
        int vx=  view.getId();
        int positionSelectedi;
        int positionSelectedj;
        if( vx-(vx%10) <=0){
            positionSelectedi = 0;
            positionSelectedj = vx;
        }else{
            positionSelectedi = 1;
            positionSelectedj = (vx%10)-1;
        }
        int tray = board.getTray(positionSelectedi,positionSelectedj);
        if (gameMode == 2 && !gameState.isInitialising()) {

            if(isvalidPlayerMove(buttonSelected,positionSelectedi,positionSelectedj)){
                startCyclei = positionSelectedi;
                startCyclej = positionSelectedj;
                buttonSelected.setText("" + 0);
                gameManager.move(positionSelectedj,positionSelectedi,tray,/*missing method in game state*/,gameState.getCurrentPlayer());

                wrapPlayer();
                gameState.setPlayer1(playerA);
                gameState.setPlayer2(playerB);


                if(this.isEmptySetOfTrays(playerA)&& this.isEmptySetOfTrays(playerB)){
                  //  gameState.setGameState(0);
                    /** missing method or misunderstood concept **/

                    Log.e("END OF GAME","END OF GAME");
                }
                int numbOfShells =(getShellInPlayerSet(playerA)+getShellInPlayerSet(playerB)+playerA.getStonesInPot()+playerB.getStonesInPot());
                tvDebugging.setText(""+numbOfShells);
            }

        } }

    /** The main actions that occurs when a player's button is pressed **/
//    private void moving(int i, int j,int heldShells) {
//        board.emptyTray(i, j);
//        if (heldShells > 0) {
//            Player previousTurn = turn;
//            turn = switchTurns();
//            boolean passA = false;
//            boolean passB = false;
//            for (int x = heldShells; x > -1; x--) {
//
//                if (i == 1 && ((j + 1) == 7)) {
//                    if (passA == false) {
//                        if (x != heldShells) {
//
//                            if (startCyclei == 1 && board.isEmptyTray(i, j) && x == 0 && !board.isEmptyTray(0, j)) {
//                                buttons[i][j].setText("" + 1);
//                                playerA.addToPot(board.emptyTray(0, j) + 1);
//                                bscoreA.setText("" + playerA.getStonesInPot());
//                                buttons[0][j].setText("" + board.getTray(0, j));
//                            } else {
//                                board.incrementTray(i, j);
//                            }
//                        }
//
//                        buttons[i][j].setText("" + board.getTray(i, j));
//                        passA = true;
//
//                    } else if (passA == true) {
//
//                        if (startCyclei == 1) {
//                            playerA.addToPot(1);
//                            bscoreA.setText("" + playerA.getStonesInPot());
//                            if (x == 0) {
//                                turn = playerA;
//                            }
//                        }else{
//                            i--;
//                            board.incrementTray(i, j);
//                            buttons[i][j].setText(""+board.getTray(i,j));
//                            j--;
//                        }
//                        i = 0;
//                        passA = false;
//                    }
//                } else if (i == 0 && ((j - 1) == -1)) {
//                    if (passB == false) {
//                        if (x != heldShells) {
//                            if (startCyclei == 0 && board.isEmptyTray(i, j) && x == 0 && !board.isEmptyTray(1, j)) {
//                                buttons[i][j].setText("" + 1);
//                                playerB.addToPot(board.emptyTray(1, j) + 1);
//                                bscoreB.setText("" + playerB.getStonesInPot());
//                                buttons[1][j].setText("" + board.getTray(1, j));
//
//                            } else {
//                                board.incrementTray(i, j);
//                                buttons[i][j].setText(""+board.getTray(i,j));
//                            }
//
//                        }
//                        passB = true;
//                    } else if (passB == true) {
//                        if (startCyclei == 0) {
//                            playerB.addToPot(1);
//                            bscoreB.setText("" + playerB.getStonesInPot());
//                            if (x == 0) {
//                                turn = playerB;
//                            }
//                        }else{
//                            i++;
//                            board.incrementTray(i, j);
//                            buttons[i][j].setText("" + board.getTray(i, j));
//                            j++;
//                        }
//                        i = 1;
//                        passB = false;
//                    }
//
//                } else {
//                    if (x != heldShells) {
//                        if (i == 1 && !board.isEmptyTray(0, j) && previousTurn.getSide() == i
//                                && startCyclei == i && board.isEmptyTray(i, j) && x == 0) {
//                            playerA.addToPot(board.emptyTray(0, j) + 1);
//                            bscoreA.setText("" + playerA.getStonesInPot());
//                            buttons[0][j].setText("" + board.getTray(0, j));
//                        } else if (i == 0 && !board.isEmptyTray(1, j) && previousTurn.getSide() == i
//                                && startCyclei == i && board.isEmptyTray(i, j) && x == 0) {
//                            playerB.addToPot(board.emptyTray(1, j) + 1);
//                            bscoreB.setText("" + playerB.getStonesInPot());
//                            buttons[1][j].setText("" + board.getTray(1, j));
//
//                        } else {
//                            board.incrementTray(i, j);
//                        }
//                    }
//                        buttons[i][j].setText("" + board.getTray(i, j));
//                    if (i == 0) {
//                        --j;
//                    } else {
//                        ++j;
//                    }
//                }
//
//            }
//        }
//        //** skip turn if opponent has no more shells to play
//        if (this.isEmptySetOfTrays(playerB) && turn == playerB) {
//            turn = playerA;
//        }else if (this.isEmptySetOfTrays(playerA) && turn == playerA){
//            turn = playerB;
//        }
//    }


    public static void setGameMode(int gameM){
        gameMode = gameM;
    }

    private Player switchTurns(){
        if(playerA.getSide() == turn.getSide()){
            return playerB;
        } else{
            return playerA;
        }

    }

    private void wrapPlayer(){
        if(turn.getSide() == 0) {
            tvPlayerA.setText("PlayerA");
            tvPlayerB.setBackgroundResource(R.color.red);
            tvPlayerA.setTextColor(getResources().getColor(R.color.gray));
            tvPlayerB.setTextColor(getResources().getColor(R.color.white));
            tvPlayerA.setBackgroundResource(R.color.white);
        }
        else{
            tvPlayerB.setText("PlayerB");
            tvPlayerA.setBackgroundResource(R.color.blue);
            tvPlayerB.setTextColor(getResources().getColor(R.color.gray));
            tvPlayerA.setTextColor(getResources().getColor(R.color.white));
            tvPlayerB.setBackgroundResource(R.color.white);
        }

    }

    private boolean isvalidPlayerMove(Button button,int selectedI,int selectedJ){
        // row 0 is player B and row 1 is player A
        if(selectedI == turn.getSide()){
            //Checks if empty
            return !board.isEmptyTray(selectedI,selectedJ);
        }else {
            return false;
        }
    }

    public boolean isEmptySetOfTrays(Player x){
        int side = x.getSide();
        boolean statement = true;
        for(int j = 0 ; j<7; j++){
            if(!board.isEmptyTray(side,j)){
                statement = false;
                break;
            }
        }
        return statement;
    }

    //Debugging method
    public  int getShellInPlayerSet(Player x){
        int addition =0;
        for(int j = 0; j<7; j++){
            addition += board.getTray(x.getSide(),j);
        }
        return addition;
    }



}
