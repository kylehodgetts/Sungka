package com.kylehodgetts.sunka;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kylehodgetts.sunka.event.EndGame;
import com.kylehodgetts.sunka.view.MainActivity;

/**
 * @Author: Phileas Hocquard
 */
public class BoardActivityUITest extends ActivityInstrumentationTestCase2<BoardActivity> {
    public BoardActivityUITest() {
        super(BoardActivity.class);
    }

private BoardActivity activity;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getInstrumentation().setInTouchMode(false);
        activity = getActivity();

    }

    /**
     * Test to verify that all components on the relative layout of the board exist.
     */
    public void testbuttonInitiallyExist() {
        TextView tvPlayerA = (TextView) activity.findViewById(R.id.tvPlayerA);
        TextView tvPlayerB = (TextView) activity.findViewById(R.id.tvPlayerB);
                assertNotNull(tvPlayerA);assertNotNull(tvPlayerB);
        for(int i = 0 ; i<2; i++) {
            for (int j = 0; j < 7; j++) {
                LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(i + "" + j));
                RelativeLayout button = (RelativeLayout) linearLayout.findViewById(R.id.button);
                TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
                assertNotNull(linearLayout);
                assertNotNull(button);
                assertNotNull(textView);
            }
        }
        }

    /**
     *Test to simply verify that after clicking a tray , that same tray has a value that becomes zero.
     *and its storage has a value of one.
     * @throws InterruptedException
     */
    public void testOneClickedButton() throws Exception {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(1+""+6));
        RelativeLayout button = (RelativeLayout) linearLayout.findViewById(R.id.button);
        TouchUtils.clickView(this, button);
        TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
        assertEquals(textView.getText().toString(), "0");
        getInstrumentation().waitForIdleSync();
        TextView tvPlayerAStoreCount = (TextView) activity.findViewById(R.id.tvPlayerBStoreCount);
        assertEquals(tvPlayerAStoreCount.getText(), "1");

    }








    /** Test Method to check that the UI has the appropiate values in each tray after all the shells have been deposited.
     * The shell that is being emptied in the first place is one on the top row.
     * @throws InterruptedException
     */
    public void testClickButtonCircularCycle1() throws InterruptedException {
        /**Tray in player1 row will be emptied and redistributed **/
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(1 + "" + 3));
        RelativeLayout button = (RelativeLayout) linearLayout.findViewById(R.id.button);
        TouchUtils.clickView(this, button);
        Thread.sleep(3000);
        TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
        assertEquals("0",textView.getText().toString());

        TextView tvPlayerAStoreCount = (TextView) activity.findViewById(R.id.tvPlayerBStoreCount);
        assertEquals(tvPlayerAStoreCount.getText(),"1");
        for (int j = 0; j<3; j++) {
            getInstrumentation().waitForIdleSync();
            LinearLayout linearLayout0 = (LinearLayout) activity.findViewById(Integer.parseInt("" + j));
            RelativeLayout button0 = (RelativeLayout) linearLayout0.findViewById(R.id.button);
            TextView textView0 = (TextView) linearLayout0.findViewById(R.id.tv);
            Log.e("Cycle is at: ", "" + j);
            assertEquals("8", textView0.getText().toString());
        }
    }
    /** Test Method to check that the UI has the appropiate values in each tray after all the shells have been deposited.
     * The shell that is being emptied in the first place is one in the bottom row.
     * @throws InterruptedException
     */
    public void testClickButtonCircularCycle0() throws InterruptedException {
        /**Tray in player0 row will be emptied and redistributed **/
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" +3));
        RelativeLayout button = (RelativeLayout) linearLayout.findViewById(R.id.button);
        TouchUtils.clickView(this, button);
        Thread.sleep(3000);
        TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
        assertEquals("0",textView.getText().toString());

        TextView tvPlayerBStoreCount = (TextView) activity.findViewById(R.id.tvPlayerAStoreCount);

        assertEquals("1",tvPlayerBStoreCount.getText());
        for (int j =0; j<3; j++) {
            getInstrumentation().waitForIdleSync();
            LinearLayout linearLayout1 = (LinearLayout) activity.findViewById(Integer.parseInt(1+"" + j));
            RelativeLayout button1 = (RelativeLayout) linearLayout1.findViewById(R.id.button);
            TextView textView1 = (TextView) linearLayout1.findViewById(R.id.tv);
            assertEquals("8", textView1.getText().toString());
        }
      }
        /** Method to verify that UI reacts as expected after a steal is provoked on each side.
         * @throws InterruptedException
         */
        public void testStealforBothPlayers() throws InterruptedException {
        //Simultaneous Round
        LinearLayout linearLayout0a = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" + 3));
        RelativeLayout button0a = (RelativeLayout) linearLayout0a.findViewById(R.id.button);
        LinearLayout linearLayout1a = (LinearLayout) activity.findViewById(Integer.parseInt(1 + "" + 3));
        RelativeLayout button1a = (RelativeLayout) linearLayout1a.findViewById(R.id.button);
        TouchUtils.clickView(this, button0a);TouchUtils.clickView(this, button1a);Thread.sleep(2000);
        //One after another
         LinearLayout linearLayout0b = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" + 4));
        RelativeLayout button0b = (RelativeLayout) linearLayout0b.findViewById(R.id.button);
        TouchUtils.clickView(this, button0b);Thread.sleep(2000);
        LinearLayout linearLayout1b = (LinearLayout) activity.findViewById(Integer.parseInt(1 + "" + 4));
        RelativeLayout button1b = (RelativeLayout) linearLayout1b.findViewById(R.id.button);
        TouchUtils.clickView(this, button1b);Thread.sleep(2000);
        LinearLayout linearLayout0c = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" + 4));
        RelativeLayout button0c = (RelativeLayout) linearLayout0c.findViewById(R.id.button);
        TouchUtils.clickView(this, button0c);Thread.sleep(2000);

        //Steal Shells located in tray 02 by moving a shell from 13 to 14
        LinearLayout linearLayout1f = (LinearLayout) activity.findViewById(Integer.parseInt(1 + "" + 3));
        RelativeLayout button1f= (RelativeLayout) linearLayout1f.findViewById(R.id.button);
        TextView textView1f = (TextView) linearLayout1f.findViewById(R.id.tv);
        assertEquals("1", textView1f.getText());
        LinearLayout linearLayout0f = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" + 2));
        TextView textView0f = (TextView) linearLayout0f.findViewById(R.id.tv);
        assertEquals("1",textView1f.getText());
        assertEquals("9", textView0f.getText());
        TextView tvPlayerAStoreCount = (TextView) activity.findViewById(R.id.tvPlayerBStoreCount);
         int currentNumberOfShellsInStore1 = Integer.parseInt("" + tvPlayerAStoreCount.getText());
        TouchUtils.clickView(this, button1f);Thread.sleep(2000);
        assertEquals("0", textView1f.getText());
        assertEquals("0", textView0f.getText());
        assertEquals(Integer.parseInt("" + tvPlayerAStoreCount.getText()),currentNumberOfShellsInStore1+10);

        //Steal Shells located in tray 12 by moving a shell from 03 to 04
        LinearLayout linearLayout0g = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" + 3));
        RelativeLayout button0g= (RelativeLayout) linearLayout0g.findViewById(R.id.button);
        TextView textView0g = (TextView) linearLayout0g.findViewById(R.id.tv);
        assertEquals("1", textView0g.getText());
        LinearLayout linearLayout1g = (LinearLayout) activity.findViewById(Integer.parseInt(1 + "" + 2));
        TextView textView1g = (TextView) linearLayout1g.findViewById(R.id.tv);
        assertEquals("1",textView0g.getText());
        assertEquals("9", textView1g.getText());
         TextView tvPlayerBStoreCount = (TextView) activity.findViewById(R.id.tvPlayerAStoreCount);
        int currentNumberOfShellsInStore0 = Integer.parseInt("" + tvPlayerBStoreCount.getText());
        TouchUtils.clickView(this, button0g);Thread.sleep(2000);
        assertEquals("0", textView0g.getText());
        assertEquals("0", textView1g.getText());
        assertEquals(Integer.parseInt(""+tvPlayerBStoreCount.getText()),currentNumberOfShellsInStore0+10);

    }

    /**    For when the game is over. Test that the buttons on that GameOver layout exist **/
    public void testGameOverMenuButtonsExist(){
            activity.getGameState().getPlayer1().addToPot(46);
            activity.getGameState().getPlayer2().addToPot(46);
            activity.getEventBus().feedEvent(new EndGame());
        Button bMenu =(Button) activity.findViewById(R.id.bMenu);
        TextView gameOverLabel = (TextView) activity.findViewById(R.id.game_over_label);
        TextView player1Label = (TextView) activity.findViewById(R.id.your_score_label);
        TextView player0Label = (TextView) activity.findViewById(R.id.opponent_score_label);
        TextView player1Score = (TextView) activity.findViewById(R.id.opponent_score);
        TextView player0Score = (TextView) activity.findViewById(R.id.your_score);
        assertNotNull(bMenu);assertNotNull(gameOverLabel);
        assertNotNull(player1Label);assertNotNull(player1Score);
        assertNotNull(player0Label);assertNotNull(player0Score);
    }

    /**
     *  Test to verify that the player0 is shown as the winner
     * @throws InterruptedException
     **/
    public void testGameToGameOverPlayer0Win() throws InterruptedException {

       activity.getGameState().getPlayer1().addWonGames();
        getInstrumentation().waitForIdleSync();
        activity.getEventBus().feedEvent(new EndGame());
        getInstrumentation().waitForIdleSync();
        TextView player0Score = (TextView) activity.findViewById(R.id.your_score);
        System.out.println(player0Score.getText());
        assertEquals("1", player0Score.getText());

    }

    /**
     *  Test to verify that the player1 is shown as the winner
     * @throws InterruptedException
     */
    public void testGameToGameOverPlayer1Win() throws InterruptedException {

        activity.getGameState().getPlayer2().addWonGames();
        getInstrumentation().waitForIdleSync();
        activity.getEventBus().feedEvent(new EndGame());
        getInstrumentation().waitForIdleSync();
        TextView player1Score = (TextView) activity.findViewById(R.id.opponent_score);
        System.out.println(player1Score.getText());
        assertEquals("1",player1Score.getText());
    }

    /** Testing the go back to menu button on the game over layout
     *@throws: InterruptedException
     */
    public void testBackToMainMenu() throws InterruptedException {
        Instrumentation.ActivityMonitor activityMonitor = new Instrumentation.ActivityMonitor(MainActivity.class.getName(), null, false);
        getInstrumentation().addMonitor(activityMonitor);
        activity.getEventBus().feedEvent(new EndGame());
        getInstrumentation().waitForIdleSync();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Button bMenu = (Button) activity.findViewById(R.id.bMenu);
                bMenu.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();
        MainActivity mainActivity = (MainActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(mainActivity);
        mainActivity.finish();
    }
}








