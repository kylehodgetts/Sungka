package com.lynx.sungka.server.actions;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Bind;
import com.lynx.sungka.server.util.IDGen;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import static com.lynx.sungka.server.Server.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * A testing end point, adds mock data to the database
 */
public class PopulateMockData extends Bind {

    private static final String[] mockNames = new String[]{"Adam","Kyle","Charlie","Jonathan","Phileas","George"};

    /**
     * An end point to a call to the server, the response is build from the resources on the server and
     * the arguments given in the path.
     *
     * Adds mock data to the server database
     *
     * @param context   The server context, access to server resources
     * @param body      The body of the request
     * @param args      Arguments passed in path
     * @return          Returns a response with no content
     */
    @Override
    public RequestResponse run(ServerContext context, DBObject body, List<String> args) {

        DBCollection names = context.getNameCollection();
        DBCollection scores = context.getStatisticsCollection();
        Random random = new Random();
        for (String mockName : mockNames) {
            DBObject object = new BasicDBObject(SERVER_ID, IDGen.genId());
            object.put(USER_NAME,mockName);
            names.insert(object);
            object.removeField(USER_NAME);
            object.put(MAX_SCORE, random.nextInt(40));
            object.put(GAMES_WON, random.nextInt(100));
            object.put(GAMES_LOST, random.nextInt(100));
            scores.insert(object);
        }

        return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.NO_CONTENT);
    }
}
