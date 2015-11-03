package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.TrayOnClick;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.EndGame;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.NextTurn;
import com.kylehodgetts.sunka.event.PlayerMove;
import com.kylehodgetts.sunka.event.TickDistribution;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.Tuple2;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *
 * The controller for the main game logic
 */
public class GameManager extends EventHandler<GameState> {

    private Timer timer;
    private Timer timer2;
    private EventBus<GameState> bus;

    /**
     * Default constructor for event handler, assigns its id that should be unique
     */
    public GameManager(EventBus<GameState> bus) {
        super("GameManager");
        timer = new Timer("GameManager");
        timer2 = new Timer("GameManagerPlayer2");
        this.bus = bus;
    }

    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state, EventBus<GameState> bus) {

        if (event instanceof PlayerMove) return new Tuple2<>(new BusContext(state,bus).event((PlayerMove) event),true);
        if (event instanceof TickDistribution) return new Tuple2<>(new BusContext(state,bus).event((TickDistribution) event),true);
        if (event instanceof NextTurn) {
            if(((NextTurn) event).finishInit())
                state.finishInit();
            state.setDoingMove(false);
            return new Tuple2<>(state, true);}
        if (event instanceof NewGame) return new Tuple2<>(state, true); //TODO Should set the board to initial state


        return new Tuple2<>(state,false);
    }

    /**
     * Renders any changes made to the model and the game state on to the GUI on the UI Thread.
     *
     * @param state     The current state of the event bus
     * @param activity  The active activity
     */
    @Override
    public void render(final GameState state, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button playerOneStore = (Button) activity.findViewById(R.id.buttonbs);
                playerOneStore.setText(Integer.toString(state.getPlayer1().getStonesInPot()));

                Button playerTwoStore = (Button) activity.findViewById(R.id.buttonas);
                playerTwoStore.setText(Integer.toString(state.getPlayer2().getStonesInPot()));

                Board currentBoard = state.getBoard();
                for (int row = 0; row < 2; ++row) {
                    for (int column = 0; column < 7; ++column) {
                        LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(row + "" + column));
                        TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
                        textView.setText(Integer.toString(currentBoard.getTray(row, column)));
                        ImageButton button = (ImageButton) linearLayout.findViewById(R.id.button);
                        button.setPadding(35,35,35,35); // In order to make the image scale
                        int currentTrayShellCount = currentBoard.getTray(row, column);
                        if(currentTrayShellCount < 10) {
                            button.setImageResource(activity.getResources().getIdentifier("s"+currentTrayShellCount, "drawable", activity.getPackageName()));
                        }
                        else {
                            button.setImageResource(activity.getResources().getIdentifier("s9", "drawable", activity.getPackageName()));
                        }
                        if (
                                !state.getBoard().isEmptyTray(row, column)
                                        && (
                                        (state.getPlayerOneTurn() == row && !state.isDoingMove() && !state.isInitialising())
                                                || state.playerInitialising(row)
                                )
                                ) {
                            button.setOnClickListener(new TrayOnClick(column, row, row, bus));
                        } else {
                            button.setOnClickListener(null);
                        }
                    }
                }

                TextView tvPlayerA = (TextView) activity.findViewById(R.id.tvPlayerA);
                TextView tvPlayerB = (TextView) activity.findViewById(R.id.tvPlayerB);

                // TODO: Look at situations when both Players are highlighted after initialisation and both players sometimes are not highlighted?
                if (state.isInitialising()) {
                    tvPlayerB.setBackgroundColor(Color.parseColor("#2D8BA8"));
                    tvPlayerA.setBackgroundColor(Color.parseColor("#A84136"));
                    tvPlayerB.setTextColor(Color.WHITE);
                    tvPlayerA.setTextColor(Color.WHITE);
                } else if (state.getPlayerOneTurn() == 0) {
                    tvPlayerB.setBackgroundColor(Color.TRANSPARENT);
                    tvPlayerB.setTextColor(Color.BLACK);
                    tvPlayerA.setBackgroundColor(Color.parseColor("#A84136"));
                    tvPlayerA.setTextColor(Color.WHITE);
                } else {
                    tvPlayerB.setBackgroundColor(Color.parseColor("#2D8BA8"));
                    tvPlayerA.setBackgroundColor(Color.TRANSPARENT);
                    tvPlayerB.setTextColor(Color.WHITE);
                    tvPlayerA.setTextColor(Color.BLACK);
                }
            }
        });


    }



    /**
     * Convenience class to wrap the current state and bus together
     */
    private class BusContext{
        private GameState state;
        private EventBus<GameState> bus;

        public BusContext(GameState state, EventBus<GameState> bus) {
            this.state = state;
            this.bus = bus;
        }

        /**
         * Processes the player move, and starts the moving of the pebbles
         * @param selected the location of selected tray
         * @return
         */
        public GameState event(PlayerMove selected){
            int column = selected.getX();
            int row = selected.getY();

            schedule(new TickDistribution(column == 6 ? 0 : column + 1, column == 6 ? (row + 1) % 2 : row, state.getBoard().emptyTray(row,column), true, selected.getPlayer()), 300, selected.getPlayer());
            if(state.getPlayerOneTurn() == -1) {
                state.setPlayerOneTurn(selected.getPlayer());
            }
            System.out.println("PLayer turn"+ state.getPlayerOneTurn());
            if (state.isInitialising())
                state.nextInitPhase(selected.getPlayer());
            state.getBoard().emptyTray(row,column);
            state.setDoingMove(true);
            return state;
        }

        /**
         * Processes one of the move tick of the players move
         * @param tick the current sate of ticking
         * @return
         */
        public GameState event(TickDistribution tick){
            return move(tick.getX(),tick.getY(),tick.getLeft(), tick.isFirst(), tick.getPlayer());
        }

        /**
         * Moves the beads by one tray/store
         * @param x         the current column where we add the bead
         * @param y         the current row where we add the bead
         * @param amt       the amount of beads to move
         * @param first     whether this move is the first one
         * @return
         */
        private GameState move(int x, int y, int amt, boolean first, int player){
            boolean repeatTurn = false;

            if (first && x == 0){ //TODO disgusting hack for fixing of the moving straight into the store
                state.getPlayerFor(player).addToPot(1);
                amt--;
                repeatTurn = true;
                if (amt>0){
                    state.getBoard().incrementTray(y, x);
                    amt--;
                    repeatTurn = false;
                }
            }else {
                state.getBoard().incrementTray(y, x);
                amt--;
                if (x == 6  && player == y && amt > 0) {
                    state.getPlayerFor(player).addToPot(1);
                    amt--;
                    repeatTurn = true;
                }
            }

            if(amt == 0){
                return endTurn(x, y, repeatTurn, player);
            } else {
                schedule(new TickDistribution(x == 6 ? 0 : x + 1, x == 6 ? (y + 1) % 2 : y, amt, false, player), 300, player);
                return state;
            }
        }

        /**
         * Processes the end of the turn for current player
         * @param x         the column of the last tray we putted beads into
         * @param y         the row of the last tray we putted beads into
         * @param repeat    whether the last bead ended in the store
         * @return
         */
        private GameState endTurn(int x, int y, boolean repeat, int player){
            Board b = state.getBoard();
            if(!repeat){
                if (b.getTray(y,x)==1 && y == player){
                    int oppositeTray = b.emptyTray((y+1)%2,6-x);
                    state.getPlayerFor(player).addToPot(oppositeTray + 1);
                    b.emptyTray(y,x);
                }
                if(!state.isInitialising()) {
                    state.setPlayerOneTurn((state.getPlayerOneTurn()  + 1)%2);
                }
            }
            if (b.isEmptyRow(0) && b.isEmptyRow(1)){
                bus.feedEvent(new EndGame());
                return state;
            }

            if(!state.isInitialising() && b.isEmptyRow(state.currentPlayerRow()))
                state.setPlayerOneTurn((state.getPlayerOneTurn() + 1)%2);

            schedule(new NextTurn(state.isInitialising() && state.getPlayerOneTurn() !=player), 300, player);
            return state;
        }


        /**
         * Schedules an event to the timer for later dispatching
         * @param event     the event to be dispatched to the bus
         * @param millis    the amount in millis by which the execution is delayed
         */
        private void schedule(final Event event, long millis, int player){
            (player == 0? timer : timer2).schedule(new TimerTask() {
                @Override
                public void run() {
                bus.feedEvent(event);
                }
            }, millis);
        }

    }

}
