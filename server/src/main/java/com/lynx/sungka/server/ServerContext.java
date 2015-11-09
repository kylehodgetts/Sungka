package com.lynx.sungka.server;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * The server context, ie access to resources of the server
 */
public class ServerContext {

    private Mongo mongo;

    /**
     * Constructor for the context, accesses the mongo api
     * @param address
     */
    public ServerContext(DBAddress address) {
        this.mongo = new Mongo(address);
    }

    /**
     * Getter for the mongo API resource
     * @return
     */
    public Mongo getMongo() {
        return mongo;
    }

    public DB getDatabase(){
        return mongo.getDB("lynx");
    }

    public DBCollection getNameCollection(){
        return getDatabase().getCollection("username");
    }

    public DBCollection getStatisticsCollection(){
        return getDatabase().getCollection("stats");
    }
}
