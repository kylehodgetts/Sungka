package com.kylehodgetts.sunka;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public void testbuttonInitiallyExist() {
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
        assertEquals("1",tvPlayerBStoreCount.getText());
        for (int j =0; j<3; j++) {
            Thread.sleep(1000);
            LinearLayout linearLayout1 = (LinearLayout) activity.findViewById(Integer.parseInt(1+"" + j));
            ImageButton button1 = (ImageButton) linearLayout1.findViewById(R.id.button);
            TextView textView1 = (TextView) linearLayout1.findViewById(R.id.tv);
             assertEquals("s8",button1.getTag());
            assertEquals("8", textView1.getText().toString());
        }
      }





    }








