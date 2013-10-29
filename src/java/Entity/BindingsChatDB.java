/****************************************************
* Student Name: Choong Teik Tan                     *
* Student Number: 568701                            *
* Student Email: choongt@student.unimelb.edu.au     *
* File: BindingsChatDB.java (SWEN90002 Project 2)   *
****************************************************/

package Entity;

import java.util.ArrayList;
import java.util.List;
import org.lightcouch.CouchDbClient;
import org.lightcouch.DesignDocument;
import org.lightcouch.Response;

public class BindingsChatDB {

    CouchDbClient dbClient = null;
    
    public BindingsChatDB() {}

    public List<ChatMessage> load() {
        dbClient = new CouchDbClient("couchdbChat.properties");
        List<ChatMessage> receivedMessage = new ArrayList<ChatMessage>();

        DesignDocument designDoc = dbClient.design().getFromDesk("Chat");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        receivedMessage = dbClient.view("Chat/by_all").includeDocs(true).query(ChatMessage.class);

        dbClient.shutdown();
        return receivedMessage;
    }

    public boolean save(ChatMessage newMessage) {
        dbClient = new CouchDbClient("couchdbChat.properties");
        
        try {
            dbClient.save(newMessage);
        } catch(Exception e) {
            dbClient.shutdown();
            return false;
        }

        dbClient.shutdown();
        return true;
    }

    public boolean delete(String groupid) {

        dbClient = new CouchDbClient("couchdbChat.properties");
        DesignDocument designDoc = dbClient.design().getFromDesk("Chat");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        List<ChatMessage> message = dbClient.view("Chat/by_all").includeDocs(true).query(ChatMessage.class);

        List<ChatMessage> toDeleteMessage = new ArrayList<ChatMessage>();
        for(ChatMessage cm : message) {
            if(cm.getGroupID().equals(groupid)) {
                toDeleteMessage.add(cm);
            }
        }

        try {
            for(ChatMessage tdm : toDeleteMessage)
                dbClient.remove(tdm);
        } catch(Exception e) {
            dbClient.shutdown();
            return false;
        }

        dbClient.shutdown();
        return true;
    }

}
