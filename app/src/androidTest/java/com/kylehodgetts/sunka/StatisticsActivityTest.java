package com.kylehodgetts.sunka;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import android.widget.TextView;

import com.kylehodgetts.sunka.controller.statistics.ScoreListAdapter;
import com.kylehodgetts.sunka.model.PlayerScores;
import com.kylehodgetts.sunka.view.StatisticsActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.kylehodgetts.sunka.view.MainActivity.AVG_TIME;
import static com.kylehodgetts.sunka.view.MainActivity.GAMES_LOST;
import static com.kylehodgetts.sunka.view.MainActivity.GAMES_WON;
import static com.kylehodgetts.sunka.view.MainActivity.MAX_SCORE;
import static com.kylehodgetts.sunka.view.MainActivity.STATS_LOCAL;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class StatisticsActivityTest extends ActivityInstrumentationTestCase2<StatisticsActivity> {
    public StatisticsActivityTest() {
        super(StatisticsActivity.class);
    }

    private Activity activity;
    private TextView displayName;
    private TextView gamesWon;
    private TextView gamesLost;
    private TextView maxScore;
    private TextView averageTime;
    private ListView listView;

    /**
     * Sets up the environment for testing the Statistics activity, writes all required data to local statistics
     * and then simulates a receive of highscores from server
     * @throws Exception
     */
    @BeforeClass
    @Override
    public void setUp() throws Exception {
        super.setUp();

        File file = new File("/data/data/com.kylehodgetts.sunka/files",STATS_LOCAL);
        file.setWritable(true);
        try {
            FileOutputStream out = new FileOutputStream(file);
            JSONObject json = new JSONObject();
            json.put(GAMES_LOST,2);
            json.put(GAMES_WON,3);
            json.put(MAX_SCORE,30);
            json.put(AVG_TIME,2500);
            out.write(json.toString().getBytes());
            out.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        System.out.println(getActivity().getFilesDir());
        activity = getActivity();
        displayName = ((TextView)activity.findViewById(R.id.display_name));
        gamesWon =((TextView)activity.findViewById(R.id.games_won));
        gamesLost = ((TextView)activity.findViewById(R.id.games_lost));
        maxScore = ((TextView)activity.findViewById(R.id.max_score));
        averageTime = ((TextView)activity.findViewById(R.id.avg_time));

        listView = (ListView)activity.findViewById(R.id.scores_list);
        final List<PlayerScores> scores = new ArrayList<>();
        scores.add(new PlayerScores("TEST", 30, 40, 10));
        Thread.sleep(1000);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(new ScoreListAdapter(activity, scores));
            }
        });
    }

    /**
     * Assert that view components are not null post onCreate execution
     * @throws Exception e
     */
    @Test
    public void testPreConditions() throws Exception {
        assertNotNull(displayName);
        assertNotNull(gamesWon);
        assertNotNull(gamesLost);
        assertNotNull(maxScore);
        assertNotNull(averageTime);
        assertNotNull(listView);
    }

    /**
     * Tests whether all of the view displays have the expected value after the setup
     * @throws Exception
     */
    @Test
    public void testHasValues() throws Exception {
        PlayerScores sc = (PlayerScores)listView.getItemAtPosition(0);

        assertEquals("N/A", displayName.getText());
        assertEquals(3, Integer.parseInt(gamesWon.getText().toString()));
        assertEquals(2,Integer.parseInt(gamesLost.getText().toString()));
        assertEquals(30,Integer.parseInt(maxScore.getText().toString()));
        assertEquals(2.5,Double.parseDouble(averageTime.getText().toString()));
        assertEquals(1, listView.getCount());
        assertEquals("TEST",sc.getName());
        assertEquals(30,sc.getMaxScore());
        assertEquals(40,sc.getGamesWon());
        assertEquals(10,sc.getGamesLost());

    }


}
