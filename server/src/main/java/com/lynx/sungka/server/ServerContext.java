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
     * @param address the DB address
     */
    public ServerContext(DBAddress address) {
        this.mongo = new Mongo(address);
    }

    /**
     * Used when you only need to match the type, should be only used with test, never with actual server
     */
    protected ServerContext(){}

    /**
     * Getter for the mongo API resource
     * @return the reference to mongo
     */
    public Mongo getMongo() {
        return mongo;
    }

    /**
     * Getter for our database
     * @return the reference to our database
     */
    public DB getDatabase(){
        return mongo.getDB("lynx");
    }

    /**
     * Getter for the collection with usernames
     * @return the reference for the collection of usernames
     */
    public DBCollection getNameCollection(){
        return getDatabase().getCollection("username");
    }

    /**
     * Getter for statistics collection
     * @return the reference for the collection of statistics collection
     */
    public DBCollection getStatisticsCollection(){
        return getDatabase().getCollection("stats");
    }
}
