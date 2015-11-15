package com.lynx.sungka.server;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * A mock up of server context, as such does not have to be connected to database to test
 * functions with database
 */
public class MockServerContext extends ServerContext {

    private MockDB db;

    /**
     * Constructs a default mock state, with a mockUp database
     */
    public MockServerContext() {
        super();
        db = new MockDB("MockDB");
    }

    /**
     * Getter for database, returning the mockup database as such we can do operations over
     * database even when offline
     * @return
     */
    @Override
    public DB getDatabase() {
        return db;
    }

    /**
     * A mock up database for testing
     */
    private class MockDB extends DB {

        private MockCollection userNames;
        private MockCollection statistics;

        public MockDB(String name) {
            super(name);
            userNames = new MockCollection(this,"username");
            statistics = new MockCollection(this,"statistics");
        }

        /** Returns the collection represented by the string &lt;dbName&gt;.&lt;collectionName&gt;.
         * @param fullNameSpace the string
         * @return the collection
         */
        @Override
        public DBCollection getCollectionFromFull(String fullNameSpace) {
            return null; //NOT IMPLEMENTED
        }

        /**
         * Does the returning of mockup collection
         * @param name the name
         * @return
         */
        @Override
        protected DBCollection doGetCollection(String name) {
            if (name.equals("username")){
                return userNames;
            }else{
                return statistics;
            }
        }

        /**
         * NOt used ignored function
         */
        @Override
        public DB getSisterDB(String dbName) {
            return null; //NOT IMPLEMENTED
        }
    }

    /**
     * A mock up of database collection, for offline local testing
     */
    private class MockCollection extends DBCollection {

        private List<DBObject> contents;

        /**
         * Initializes a new collection.
         *
         * @param base database in which to create the collection
         * @param name the name of the collection
         */
        protected MockCollection(MockDB base, String name) {
            super(base, name);
            contents = new ArrayList<>();
        }

        /**
         * Saves an document to the database.
         * @param doc object to save
         * @dochub insert
         */
        @Override
        public void insert(DBObject doc) throws MongoException {
            contents.add(doc);
        }

        /**
         * Saves an array of documents to the database.
         *
         * @param arr array of documents to save
         * @dochub insert
         */
        @Override
        public void insert(DBObject[] arr) throws MongoException {
            contents.addAll(Arrays.asList(arr));
        }

        /**
         * Saves an array of documents to the database.
         *
         * @param list list of documents to save
         * @dochub insert
         */
        @Override
        public void insert(List<DBObject> list) throws MongoException {
            contents.addAll(list);
        }


        /**
         * Performs an update operation.
         * @param q search query for old object to update
         * @param o object with which to update <tt>q</tt>
         * @param upsert if the database should create the element if it does not exist
         * @param multi if the update should be applied to all objects matching (db version 1.1.3 and above)
         * See http://www.mongodb.org/display/DOCS/Atomic+Operations
         * @dochub update
         */
        @Override
        public void update(DBObject q, DBObject o, boolean upsert, boolean multi) throws MongoException {
            boolean found;
            int index = -1;
            for (DBObject obj : contents) {
                found = true;
                for (String s : q.keySet()) {
                   if (!obj.get(s).equals(q.get(s))){
                       found = false;
                       break;
                   }
                }
                if (found){
                    index = contents.indexOf(obj);
                    break;
                }
            }
            if (index !=-1){
                contents.set(index,o);
            }else if (upsert){
                insert(o);
            }

        }

        @Override
        protected void doapply(DBObject o) {
            //NOT IMPLEMENTED
        }

        @Override
        public void remove(DBObject o) throws MongoException {
            //NOT IMPLEMENTED
        }

        /** Finds an object.
         * @param ref query used to search
         * @param fields the fields of matching objects to return
         * @param numToSkip will not return the first <tt>numToSkip</tt> matches
         * @param batchSize if positive, is the # of objects per batch sent back from the db.  all objects that match will be returned.  if batchSize < 0, its a hard limit, and only 1 batch will either batchSize or the # that fit in a batch
         * @param options - see Bytes QUERYOPTION_*
         * @return the objects, if found
         * @dochub find
         */
        @Override
        public Iterator<DBObject> find(DBObject ref, DBObject fields, int numToSkip, int batchSize, int options) throws MongoException {
            boolean skipSort = ref.containsField("count");
            String keyMaybe = "";
            if(ref.keySet().isEmpty())
                skipSort = true;
            else
                keyMaybe = ref.keySet().iterator().next();
            final String key = keyMaybe;
            Object sort = ref.get(key);
            int ord = 1;
            if((sort instanceof Number)) {
                ord = ((Number) sort).intValue();
            }
            final int sortOrd = ord;
            List<DBObject> copy = new ArrayList<>();
            for (DBObject obj : contents) {
                copy.add(obj);
            }

            if (skipSort)
                copy.sort(new Comparator<DBObject>() {
                    @Override
                    public int compare(DBObject o1, DBObject o2) {
                        Object o1Obj = o1.get(key);
                        Object o2Obj = o2.get(key);
                        if (o1Obj instanceof Number && o2Obj instanceof Number){
                            return Double.compare(((Number) o1Obj).doubleValue(), ((Number) o2Obj).doubleValue()) * sortOrd;
                        }

                        if (o1Obj instanceof String && o2Obj instanceof String){
                            return ((String) o1Obj).compareTo((String) o2Obj) * sortOrd;
                        }

                        return 1;
                    }
                });

            Iterator<DBObject> iterator = copy.iterator();
            if (ref.containsField("count")){
                ArrayList<DBObject> ret = new ArrayList<>();
                BasicDBObject obj = new BasicDBObject("n",(long)contents.size());
                obj.put("ok",1);
                ret.add(obj);
                return ret.iterator();
            }


            for (int i = 0; i < numToSkip; i++) {
                if (iterator.hasNext())
                    iterator.next();
            }

            return iterator;
        }

        @Override
        protected void createIndex(DBObject keys, DBObject options) throws MongoException {
            //NOT IMPLEMENTED
        }
    }
}
