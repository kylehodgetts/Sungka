package com.lynx.sungka.server.actions;

import com.lynx.sungka.server.Server;
import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.header.ContentLength;
import com.lynx.sungka.server.http.header.ContentType;
import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Bind;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class GetScores extends Bind {
    @Override
    public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
        try{
            int page = Integer.parseInt(args.get(0));
            int type = Integer.parseInt(args.get(1));
            DBCursor cursor;
            switch (type){
                case 0:
                    cursor = context.getStatisticsCollection().find().sort(new BasicDBObject(Server.GAMES_WON, -1));
                    break;
                case 1:
                    cursor = context.getStatisticsCollection().find().sort(new BasicDBObject(Server.GAMES_WON, 1));
                    break;
                case 2:
                    cursor = context.getStatisticsCollection().find().sort(new BasicDBObject(Server.MAX_SCORE, -1));
                    break;
                case 3:
                    cursor = context.getStatisticsCollection().find().sort(new BasicDBObject(Server.MAX_SCORE, 1));
                    break;
                default:
                    return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
            }

            List<DBObject> objects = new ArrayList<>();
            for (DBObject object:cursor.skip(page*Server.BATCH_SIZE).batchSize(Server.BATCH_SIZE)){
                String id = (String)object.get(Server.SERVER_ID);
                DBObject name = context.getNameCollection().findOne(new BasicDBObject(Server.SERVER_ID,id));
                object.put(Server.USER_NAME,name.get(Server.USER_NAME));
                objects.add(object);
            }

            byte[] resp = JSON.serialize(objects).getBytes();
            List<Header> headers = new ArrayList<>();
            headers.add(new ContentLength(resp.length));
            headers.add(ContentType.json());
            return new RequestResponse(headers, resp, RequestResponse.ResponseCode.OK);
        }catch (Exception ex){
            return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
        }
    }
}
