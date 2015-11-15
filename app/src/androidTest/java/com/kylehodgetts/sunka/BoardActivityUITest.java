package com.kylehodgetts.sunka;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kylehodgetts.sunka.event.EndGame;

import org.junit.Test;
import org.w3c.dom.Text;

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
    @Test
    public void testbuttonInitiallyExist() {
        TextView tvPlayerA = (TextView) activity.findViewById(R.id.tvPlayerA);
        TextView tvPlayerB = (TextView) activity.findViewById(R.id.tvPlayerB);
                assertNotNull(tvPlayerA);assertNotNull(tvPlayerB);
        for(int i = 0 ; i<2; i++) {
            for (int j = 0; j < 7; j++) {
                LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(i + "" + j));
                ImageButton button = (ImageButton) linearLayout.findViewById(R.id.button);
                TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
                assertNotNull(linearLayout);
                assertNotNull(button);
                assertNotNull(textView);
            }
        }
        }
      @Test
    public void testOneClickedButton() throws Exception {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(1+""+6));
        ImageButton button = (ImageButton) linearLayout.findViewById(R.id.button);
        TouchUtils.clickView(this, button);
        TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
        assertEquals(textView.getText().toString(), "0");
        Thread.sleep(1000);
        TextView tvPlayerAStoreCount = (TextView) activity.findViewById(R.id.tvPlayerBStoreCount);
        assertEquals(tvPlayerAStoreCount.getText(), "1");

    }


  @Test
    public void testClickButtonCircularCycle1() throws InterruptedException {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(1 + "" + 3));
        ImageButton button = (ImageButton) linearLayout.findViewById(R.id.button);
        TouchUtils.clickView(this, button);
        TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
        assertEquals("0",textView.getText().toString());
        assertEquals("s0", button.getTag());

        TextView tvPlayerAStoreCount = (TextView) activity.findViewById(R.id.tvPlayerBStoreCount);
        assertEquals(tvPlayerAStoreCount.getText(),"1");
        for (int j = 0; j<3; j++) {
            Thread.sleep(1000);
            LinearLayout linearLayout0 = (LinearLayout) activity.findViewById(Integer.parseInt("" + j));
            ImageButton button0 = (ImageButton) linearLayout0.findViewById(R.id.button);
            TextView textView0 = (TextView) linearLayout0.findViewById(R.id.tv);
            Log.e("Cycle is at: ", "" + j);
            assertEquals("s8",button0.getTag());
            assertEquals("8", textView0.getText().toString());
        }
    }

      @Test
    public void testClickButtonCircularCycle0() throws InterruptedException {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" +3));
        ImageButton button = (ImageButton) linearLayout.findViewById(R.id.button);
        TouchUtils.clickView(this, button);

        TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
        assertEquals("0",textView.getText().toString());
        assertEquals("s0",button.getTag());

        TextView tvPlayerBStoreCount = (TextView) activity.findViewById(R.id.tvPlayerAStoreCount);
        assertEquals("1", tvPlayerBStoreCount.getText());
        for (int j =0; j<3; j++) {
            Thread.sleep(1000);
            LinearLayout linearLayout1 = (LinearLayout) activity.findViewById(Integer.parseInt(1+"" + j));
            ImageButton button1 = (ImageButton) linearLayout1.findViewById(R.id.button);
            TextView textView1 = (TextView) linearLayout1.findViewById(R.id.tv);
             assertEquals("s8",button1.getTag());
            assertEquals("8", textView1.getText().toString());
        }
      }

    @Test
        public void testStealforBothPlayers() throws InterruptedException {
        //Simultaneous Round
        LinearLayout linearLayout0a = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" + 3));
        ImageButton button0a = (ImageButton) linearLayout0a.findViewById(R.id.button);
        LinearLayout linearLayout1a = (LinearLayout) activity.findViewById(Integer.parseInt(1 + "" + 3));
        ImageButton button1a = (ImageButton) linearLayout1a.findViewById(R.id.button);
        TouchUtils.clickView(this, button0a);TouchUtils.clickView(this, button1a);Thread.sleep(2000);
        //One after another
         LinearLayout linearLayout0b = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" + 4));
        ImageButton button0b = (ImageButton) linearLayout0b.findViewById(R.id.button);
        TouchUtils.clickView(this, button0b);Thread.sleep(2000);
        LinearLayout linearLayout1b = (LinearLayout) activity.findViewById(Integer.parseInt(1 + "" + 4));
        ImageButton button1b = (ImageButton) linearLayout1b.findViewById(R.id.button);
        TouchUtils.clickView(this, button1b);Thread.sleep(2000);
        LinearLayout linearLayout0c = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" + 4));
        ImageButton button0c = (ImageButton) linearLayout0c.findViewById(R.id.button);
        TouchUtils.clickView(this, button0c);Thread.sleep(2000);

        //Steal Shells located in tray 02 by moving shell from 13 to 14
        LinearLayout linearLayout1f = (LinearLayout) activity.findViewById(Integer.parseInt(1 + "" + 3));
        ImageButton button1f= (ImageButton) linearLayout1f.findViewById(R.id.button);
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

        //Steal Shells located in tray 12 by moving shell from 03 to 04
        LinearLayout linearLayout0g = (LinearLayout) activity.findViewById(Integer.parseInt(0 + "" + 3));
        ImageButton button0g= (ImageButton) linearLayout0g.findViewById(R.id.button);
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

    /**    For when the game is over **/
    @Test
    public void testGameOverMenuButtonsExist(){
            activity.getGameState().getPlayer1().addToPot(46);
            activity.getGameState().getPlayer2().addToPot(46);
            activity.getEventBus().feedEvent(new EndGame());
        Button bMenu =(Button) activity.findViewById(R.id.bMenu);
        Button bAgain = (Button) activity.findViewById(R.id.bAgain);
        TextView gameOverLabel = (TextView) activity.findViewById(R.id.game_over_label);
        TextView player1Label = (TextView) activity.findViewById(R.id.your_score_label);
        TextView player0Label = (TextView) activity.findViewById(R.id.opponent_score_label);
        TextView player1Score = (TextView) activity.findViewById(R.id.opponent_score);
        TextView player0Score = (TextView) activity.findViewById(R.id.your_score);
        assertNotNull(bMenu);assertNotNull(bAgain);assertNotNull(gameOverLabel);
        assertNotNull(player1Label);assertNotNull(player1Score);
        assertNotNull(player0Label);assertNotNull(player0Score);
    }

    @Test
    public void testGameToGameOverPlayer0Win() throws InterruptedException {
        //Thats messed up
       activity.getGameState().getPlayer1().addWonGames();
        Thread.sleep(1000);
        activity.getEventBus().feedEvent(new EndGame());
        Thread.sleep(1000);
        TextView player0Score = (TextView) activity.findViewById(R.id.your_score);
        System.out.println(player0Score.getText());
        assertEquals("1",player0Score.getText());

    }

    @Test
    public void testGameToGameOverPlayer1Win() throws InterruptedException {
        //Thats messed up
        activity.getGameState().getPlayer2().addWonGames();
        Thread.sleep(1000);
        activity.getEventBus().feedEvent(new EndGame());
        Thread.sleep(1000);
        TextView player1Score = (TextView) activity.findViewById(R.id.opponent_score);
        System.out.println(player1Score.getText());
        assertEquals("1",player1Score.getText());
    }

//    @Todo: Need to comment and create 2 testcases for when user reaches the Play again button or Menu button.



    }








