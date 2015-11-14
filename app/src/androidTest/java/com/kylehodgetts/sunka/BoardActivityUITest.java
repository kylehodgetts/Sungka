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

    public void testOneClickedButton() throws Exception {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(Integer.parseInt(1+""+6));
        ImageButton button = (ImageButton) linearLayout.findViewById(R.id.button);
        TouchUtils.clickView(this, button);
        TextView textView = (TextView) linearLayout.findViewById(R.id.tv);
        assertEquals(textView.getText().toString(), "0");
        Thread.sleep(1000);
        TextView tvPlayerAStoreCount = (TextView) activity.findViewById(R.id.tvPlayerBStoreCount);
        assertEquals(tvPlayerAStoreCount.getText(),"1");

    }



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






    }








