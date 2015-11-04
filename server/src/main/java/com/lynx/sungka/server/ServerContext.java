package com.lynx.sungka.server;

import com.mongodb.DBAddress;
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
}
