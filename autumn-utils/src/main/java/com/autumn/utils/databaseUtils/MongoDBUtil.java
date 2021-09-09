package com.autumn.utils.databaseUtils;

/*-
 * #%L
 * autumn-utils
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.autumn.reporting.assertions.CustomAssert;
import com.autumn.reporting.extentReport.Logger;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.InsertOneOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MongoDBUtil {

    private static MongoDBUtil mongoDBUtil;
    private static ConcurrentMap<String, MongoClient> connectionMap = new ConcurrentHashMap<>();


    private MongoDBUtil() {
    }

    /**
     * Class constructor creates a new object of MongoDBUtil if
     * that object already doesn't exist, otherwise returns the existing object.
     *
     * @return The synchronized object of MongoDBUtil class.
     */
    public static synchronized MongoDBUtil getInstance() {
        if (mongoDBUtil == null) {
            synchronized (MongoDBUtil.class) {
                if (mongoDBUtil == null) {
                    mongoDBUtil = new MongoDBUtil();
                }
            }
        }
        return mongoDBUtil;
    }

    /**
     * Creates the mongo database connection with the database specified in
     * the connection URL.
     *
     * @param mongoConnectionURL The string URL specifying the database details.
     * @return The object of database connection.
     */
    public synchronized MongoClient getConnection(String mongoConnectionURL) {
        if (!connectionMap.containsKey(mongoConnectionURL)) {
            synchronized (MongoDBUtil.class) {
                if (!connectionMap.containsKey(mongoConnectionURL)) {
                    MongoClientURI mongoClientURI = new MongoClientURI(mongoConnectionURL);
                    MongoClient mongoClient = new MongoClient(mongoClientURI);
                    connectionMap.put(mongoConnectionURL, mongoClient);
                }

            }
        }
        return connectionMap.get(mongoConnectionURL);
    }

    /**
     * Closes the existing database connection from the database specified in
     * the mongo database connection URL.
     *
     * @param mongoConnectionURL The string URL specifying the database details.
     */
    public synchronized void closeConnection(String mongoConnectionURL) {
        if (connectionMap.containsKey(mongoConnectionURL)) {
            connectionMap.get(mongoConnectionURL).close();
            connectionMap.remove(mongoConnectionURL);
        }
    }


    /**
     * Closes all the currently opened mongo database connections.
     */
    public synchronized void closeAllConnections() {
        Set<String> keys = connectionMap.keySet();
        for (String key : keys) {
            MongoClient mongoClient = connectionMap.get(key);
            mongoClient.close();
            connectionMap.remove(key);
        }
    }

    /**
     * Insert a document in specified collection
     *
     * @param mongoConnectionURL The string URL specifying the database details
     * @param doc                document which is to be inserted
     * @param collection         name of the collection where document is to be inserted
     */
    public synchronized String insertDocument(String mongoConnectionURL, Document doc, String collection) {
        MongoClient mongoClient = this.getConnection(mongoConnectionURL);

        String dbName = mongoConnectionURL.split("=")[1].split("&")[0];
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
        InsertOneOptions insertOneOptions = new InsertOneOptions().bypassDocumentValidation(false);
        mongoCollection.insertOne(doc,insertOneOptions);
        Logger.logInfoInLogger("Document Created: "+ doc.toString());
        if(!insertOneOptions.getBypassDocumentValidation()){
            return "Document Inserted Successfully";
        }else {
            return "Some error Occurred";
        }
    }

    /**
     * Insert multiple document in specified collection
     *
     * @param mongoConnectionURL The string URL specifying the database details
     * @param docs                document which is to be inserted
     * @param collection         name of the collection where document is to be inserted
     */
    public synchronized String insertManyDocuments(String mongoConnectionURL, List<Document> docs, String collection) {
        MongoClient mongoClient = this.getConnection(mongoConnectionURL);

        String dbName = mongoConnectionURL.split("=")[1].split("&")[0];
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
        InsertManyOptions insertManyOptions = new InsertManyOptions().bypassDocumentValidation(false);
        mongoCollection.insertMany(docs,insertManyOptions);
        Logger.logInfoInLogger("Documents Created: "+ docs.toString());
        if(!insertManyOptions.getBypassDocumentValidation()){
            return "Document Inserted Successfully";
        }else {
            return "Some error Occurred";
        }
    }


    public synchronized String deleteManyDocuments(String mongoConnectionURL, Bson filters, String collection) {
        try {
            MongoClient mongoClient = this.getConnection(mongoConnectionURL);
            String dbName = mongoConnectionURL.split("=")[1].split("&")[0];
            MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
            mongoCollection.deleteMany(filters);
            Logger.logInfoInLogger("Documents Deleted with filter: "+ filters.toString());
            return "Document Deleted Successfully";
        }catch(Exception e){
            return "Some error Occurred";
        }
    }

    public synchronized String deleteOneDocument(String mongoConnectionURL, Bson filters, String collection) {
        try {
            MongoClient mongoClient = this.getConnection(mongoConnectionURL);
            String dbName = mongoConnectionURL.split("=")[1].split("&")[0];
            MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
            mongoCollection.deleteOne(filters);
            Logger.logInfoInLogger("Document Deleted with filter: "+ filters.toString());
            return "Document Deleted Successfully";
        }catch(Exception e){
            return "Some error Occurred";
        }
    }


    /**
     * Find documents using the filter
     *
     * @param mongoConnectionURL
     * @param collection
     * @param filters
     * @return
     */
    public synchronized List<String> findDocumentWithFilter(String mongoConnectionURL,  String collection, Bson filters) {
        List<String> documentsList = new LinkedList<>();
        MongoClient mongoClient = getConnection(mongoConnectionURL);
        String dbName = mongoConnectionURL.split("=")[1].split("&")[0];
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
        FindIterable<Document> fi = mongoCollection.find(filters);
        MongoCursor<Document> cursor = fi.iterator();
        Logger.logInfoInLogger("Query Executed With Filter: " + filters.toString());
        try {
            while (cursor.hasNext()) {
                documentsList.add(cursor.next().toJson());
            }
        }  finally {
            cursor.close();
        }
        Logger.logInfoInLogger("Documents Returned is: " + documentsList.size());
        if(documentsList.size()>0)
            return documentsList;
        else
            return null;
    }

    /**
     * Find documents using the filter and sort
     *
     * @param mongoConnectionURL
     * @param collection
     * @param filters
     * @param sort
     * @return
     */
    public synchronized List<String> findDocumentWithFilterAndSort(String mongoConnectionURL,  String collection, Bson filters ,Bson sort) {
        List<String> documentsList = new LinkedList<>();
        MongoClient mongoClient = getConnection(mongoConnectionURL);
        String dbName = mongoConnectionURL.split("=")[1].split("&")[0];
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
        FindIterable<Document> fi = mongoCollection.find(filters).sort(sort);
        MongoCursor<Document> cursor = fi.iterator();
        Logger.logInfoInLogger("Query Executed With Filter And Sort " + filters.toString());
        try {
            while (cursor.hasNext()) {
                documentsList.add(cursor.next().toJson());
            }
        }  finally {
            cursor.close();
        }

        Logger.logInfoInLogger("Documents Returned is: " + documentsList.size());
        if(documentsList.size()>0)
            return documentsList;
        else
            return null;
    }


    /**
     * Find all the documents without filter
     *
     * @param mongoConnectionURL
     * @param collection
     * @return
     */
    public synchronized  List<String> findAllDocuments(String mongoConnectionURL, String collection){
        List<String> documentsList = new LinkedList<>();
        MongoClient mongoClient = getConnection(mongoConnectionURL);
        String dbName = mongoConnectionURL.split("=")[1].split("&")[0];
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
        FindIterable<Document> fi = mongoCollection.find();
        MongoCursor<Document> cursor = fi.iterator();
        Logger.logInfoInLogger("Query Executed To Get All Document of Collection: "+ collection);
        try {
            while (cursor.hasNext()) {
                documentsList.add(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        Logger.logInfoInLogger("Documents Returned is: " + documentsList.size());
        if(documentsList.size()>0)
            return documentsList;
        else
            return null;
    }

    /**
     * Update Single document fetched based on the filters
     *
     * @param mongoConnectionURL
     * @param collection
     * @param filters
     * @param updateOperation
     */
    public synchronized void updateOneDocumentWithFilter(String mongoConnectionURL, String collection, Bson filters, Bson updateOperation){
        MongoClient mongoClient = getConnection(mongoConnectionURL);
        String dbName = mongoConnectionURL.split("=")[1].split("&")[0];
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
        mongoCollection.updateOne(filters,updateOperation);
        Logger.logInfoInLogger("Document Updated with filter: "+filters.toString());
    }


    /**
     * Update multiple documents fetched based on the filters
     *
     * @param mongoConnectionURL
     * @param collection
     * @param filters
     * @param updateOperation
     */
    public synchronized void updateManyDocumentWithFilter(String mongoConnectionURL, String collection, Bson filters, Bson updateOperation){
        MongoClient mongoClient = getConnection(mongoConnectionURL);
        String dbName = mongoConnectionURL.split("=")[1].split("&")[0];
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
        mongoCollection.updateMany(filters,updateOperation);
        Logger.logInfoInLogger("Documents Updated with filter: "+filters.toString());
    }

    /**
     * Check if Given Entry is Present In MongoDB and If Present Then Return it
     * @param mongoConnectionURL
     * @param collection
     * @param filters
     * @return
     */
    public List<String> isEntryPresent(String mongoConnectionURL,  String collection, Bson filters){
        List<String> documentList = findDocumentWithFilter(mongoConnectionURL,collection,filters);
        CustomAssert.assertEquals((documentList.size()>=1),true,"Validating If Entry Present In MongoDB");
        return documentList;
    }

    /**
     * Check If Given Entry is Present In MongoDB  and If Present then Return it In Given Sorted Order
     * @param mongoConnectionURL
     * @param collection
     * @param filters
     * @param sort
     * @return
     */
    public List<String> isEntryPresent(String mongoConnectionURL,  String collection, Bson filters ,Bson sort){
        List<String> documentList = findDocumentWithFilterAndSort(mongoConnectionURL,collection,filters,sort);
        CustomAssert.assertEquals((documentList.size()>=1),true,"Validating If Entry Present In MongoDB");
        return documentList;
    }

    /**
     * Check if Entry is Absent In MongoDB
     * @param mongoConnectionURL
     * @param collection
     * @param filters
     */
    public void isEntryAbsent(String mongoConnectionURL,  String collection, Bson filters){
        List<String> documentList = findDocumentWithFilter(mongoConnectionURL,collection,filters);
        CustomAssert.assertEquals((documentList.size()==0),true,"Validating If Entry Absent In MongoDB");
    }



}
