package com.lynx.sungka.server.action;

import com.lynx.sungka.server.MockServerContext;
import com.lynx.sungka.server.Server;
import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.actions.GetId;
import com.lynx.sungka.server.actions.GetPages;
import com.lynx.sungka.server.actions.GetScores;
import com.lynx.sungka.server.actions.SaveScore;
import com.lynx.sungka.server.http.RequestResponse;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Test for all the end points of this server
 */
public class ActionsTest extends TestCase {

    private ServerContext context;
    private DBCollection names;
    private DBCollection stats;
    private DBObject newUser;

    /**
     * Sets up the server with mock context and populates the database with moc data
     * @throws Exception
     */
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new MockServerContext();
        names = context.getNameCollection();
        stats = context.getStatisticsCollection();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            DBObject obj = new BasicDBObject(Server.USER_NAME,"TEST_NAME"+i);
            obj.put(Server.SERVER_ID, "ID-" + i);
            names.insert(obj);

            obj.removeField(Server.USER_NAME);
            obj.put(Server.MAX_SCORE, i);
            obj.put(Server.GAMES_LOST, i);
            obj.put(Server.GAMES_WON, i);
            stats.insert(obj);
        }
        setUpNewUser();
    }

    /**
     * Sets up the user to create to its required form
     */
    private void setUpNewUser(){
        newUser = new BasicDBObject();
        newUser.put(Server.SERVER_ID, "ID-5");
        newUser.put(Server.MAX_SCORE, 9);
        newUser.put(Server.GAMES_LOST, 10);
        newUser.put(Server.GAMES_WON, 11);
    }

    /**
     * Test the generation of id and then consecutively its sending to client. Also verifies that if
     * the request does not contain the user name it responses with 400
     */
    @Test
    public void testGetId(){
        RequestResponse response = new GetId().run(context,new BasicDBObject(Server.USER_NAME,"TEST_PERSON"),new ArrayList<String>());
        String foundId = "";
        for (DBObject object : names.find()) {
           String gotten = (String)object.get(Server.SERVER_ID);
            if (gotten.endsWith("-ID")){
                foundId = gotten;
                break;
            }
        }

        assertEquals(200, response.getCode().getValue());
        assertEquals(foundId, ((DBObject) JSON.parse(new String(response.getContent()))).get(Server.SERVER_ID));

        response = new GetId().run(context,new BasicDBObject(),new ArrayList<String>());
        assertEquals(400, response.getCode().getValue());

    }

    /**
     * Tests whether this returns the required number of pages
     */
    @Test
    public void testGetPages(){
        RequestResponse response = new GetPages().run(context,new BasicDBObject(),new ArrayList<String>());

        assertEquals(200, response.getCode().getValue());
        assertEquals("1", new String(response.getContent()));
    }

    /**
     * Test whether returns the correct amount of players after the request queries for given page with given order
     * also checks that returns code 400 in case the parameters are not in path (should not
     * ever happen in production since path matching will not allow it)
     */
    @Test
    public void testGetScores(){
        List<String> args = new ArrayList<>();
        args.add("0");
        args.add("0");
        RequestResponse response = new GetScores().run(context,new BasicDBObject(),args);

        List<DBObject> ret = (List<DBObject>)JSON.parse(new String(response.getContent()));

        assertEquals(200, response.getCode().getValue());
        assertEquals(5, ret.size());
        for (int i = 0; i < ret.size(); i++) {
            assertTrue(ret.get(i).get(Server.SERVER_ID).equals("ID-"+i));
        }

        args = new ArrayList<>();
        args.add("0");
        response = new GetScores().run(context,new BasicDBObject(),args);
        assertEquals(400, response.getCode().getValue());

    }

    /**
     * Tests that the new statistics are properly save to server and that it replies with 400 in case
     * there are some data missing from the json
     */
    @Test
    public void testSaveScore(){
        RequestResponse response = new SaveScore().run(context,newUser,new ArrayList<String>());

        assertEquals(204, response.getCode().getValue());
        assertEquals(6, stats.getCount());

        setUpNewUser();
        newUser.removeField(Server.SERVER_ID);
        response = new SaveScore().run(context,newUser,new ArrayList<String>());
        assertEquals(400, response.getCode().getValue());

        setUpNewUser();
        newUser.removeField(Server.MAX_SCORE);
        response = new SaveScore().run(context,newUser,new ArrayList<String>());
        assertEquals(400, response.getCode().getValue());

        setUpNewUser();
        newUser.removeField(Server.GAMES_LOST);
        response = new SaveScore().run(context,newUser,new ArrayList<String>());
        assertEquals(400, response.getCode().getValue());

        setUpNewUser();
        newUser.removeField(Server.GAMES_WON);
        response = new SaveScore().run(context,newUser,new ArrayList<String>());
        assertEquals(400, response.getCode().getValue());
    }

}
