/****************************************************
* Student Name: Choong Teik Tan                     *
* Student Number: 568701                            *
* Student Email: choongt@student.unimelb.edu.au     *
* File: BindingsUserDB.java (SWEN90002 Project 2)   *
****************************************************/

package Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lightcouch.CouchDbClient;
import org.lightcouch.DesignDocument;
import org.lightcouch.Response;

public class BindingsUserDB {

    CouchDbClient dbClient = null;

//    public BindingsUserDB() {}

    public List<User> load() {
        dbClient = new CouchDbClient("couchdb.properties");
        List<User> registeredUser = new ArrayList<User>();

        DesignDocument designDoc = dbClient.design().getFromDesk("User");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        registeredUser = dbClient.view("User/by_all").includeDocs(true).query(User.class);

        dbClient.shutdown();
        return registeredUser;
    }

    public boolean save(User newUser) {
        dbClient = new CouchDbClient("couchdb.properties");

        try {
            dbClient.save(newUser);
        } catch(Exception e) {
            dbClient.shutdown();
            return false;
        }

        dbClient.shutdown();
        return true;
    }

    public boolean find(String email) {
        dbClient = new CouchDbClient("couchdb.properties");
        
        DesignDocument designDoc = dbClient.design().getFromDesk("User");
        Response response = dbClient.design().synchronizeWithDb(designDoc);

        int dupCount = 0;
        try {
            dupCount = dbClient.view("User/by_all").key(email).query(User.class).size();
        } catch(Exception e) {
            dbClient.shutdown();
            return false;
        }

        dbClient.shutdown();
        if (dupCount > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean delete(String email) {
        if(!find(email)) {
            return false;
        }
        
        dbClient = new CouchDbClient("couchdb.properties");
        DesignDocument designDoc = dbClient.design().getFromDesk("User");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        List<User> registeredUser = dbClient.view("User/by_all").includeDocs(true).query(User.class);
        User toDelete = null;

        Iterator x = registeredUser.listIterator();
        while(x.hasNext() && toDelete == null) {
            User a = (User)(x.next());
            if(a.getUser().equals(email))
                toDelete = a;
        }

        try {
            dbClient.remove(toDelete);
        } catch(Exception e) {
            dbClient.shutdown();
            return false;
        }

        dbClient.shutdown();
        return true;
    }

    public User getUser(String email) {

        dbClient = new CouchDbClient("couchdb.properties");
        DesignDocument designDoc = dbClient.design().getFromDesk("User");
        Response response = dbClient.design().synchronizeWithDb(designDoc);

        List<User> registeredUser = dbClient.view("User/by_all").includeDocs(true).query(User.class);
        User user = null;

        Iterator x = registeredUser.listIterator();
        while(x.hasNext() && user == null) {
            User a = (User)(x.next());
            if(a.getUser().equals(email))
                user = a;
        }


        dbClient.shutdown();
        return user;
    }

    public boolean login(String email, String token) {
        if(!find(email)) {
            return false;
        }

        dbClient = new CouchDbClient("couchdb.properties");
        DesignDocument designDoc = dbClient.design().getFromDesk("User");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        List<User> registeredUser = dbClient.view("User/by_all").includeDocs(true).query(User.class);
        User toLogin = null;

        Iterator x = registeredUser.listIterator();
        while(x.hasNext() && toLogin == null) {
            User a = (User)(x.next());
            if(a.getUser().equals(email))
                toLogin = a;
        }

        toLogin.setToken(token);
        toLogin.login();

        try {
            dbClient.update(toLogin);
        } catch(Exception e) {
            dbClient.shutdown();
            return false;
        }

        dbClient.shutdown();
        return true;
    }

    public String getToken(String email) {
        dbClient = new CouchDbClient("couchdb.properties");
        DesignDocument designDoc = dbClient.design().getFromDesk("User");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        List<User> registeredUser = dbClient.view("User/by_all").includeDocs(true).query(User.class);
        User user = null;

        Iterator x = registeredUser.listIterator();
        while(x.hasNext() && user == null) {
            User a = (User)(x.next());
            if(a.getUser().equals(email))
                user = a;
        }

        dbClient.shutdown();

        return user.getToken();
    }

    public boolean  isLogin(String email) {
        dbClient = new CouchDbClient("couchdb.properties");
        DesignDocument designDoc = dbClient.design().getFromDesk("User");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        List<User> registeredUser = dbClient.view("User/by_all").includeDocs(true).query(User.class);
        User user = null;

        Iterator x = registeredUser.listIterator();
        while(x.hasNext() && user == null) {
            User a = (User)(x.next());
            if(a.getUser().equals(email))
                user = a;
        }

        dbClient.shutdown();

        return user.isLogin();
    }

    public boolean logout(String email) {
        if(!find(email)) {
            return false;
        }

        dbClient = new CouchDbClient("couchdb.properties");
        DesignDocument designDoc = dbClient.design().getFromDesk("User");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        List<User> registeredUser = dbClient.view("User/by_all").includeDocs(true).query(User.class);
        User toLogout = null;

        Iterator x = registeredUser.listIterator();
        while(x.hasNext() && toLogout == null) {
            User a = (User)(x.next());
            if(a.getUser().equals(email))
                toLogout = a;
        }

        toLogout.setToken("");
        toLogout.logout();

        try {
            dbClient.update(toLogout);
        } catch(Exception e) {
            dbClient.shutdown();
            return false;
        }

        dbClient.shutdown();
        return true;
    }

    public boolean setPassword(String email, String token, String newPassword) {
        if(!find(email)) {
            return false;
        }

        dbClient = new CouchDbClient("couchdb.properties");
        DesignDocument designDoc = dbClient.design().getFromDesk("User");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        List<User> registeredUser = dbClient.view("User/by_all").includeDocs(true).query(User.class);
        User toReset = null;

        Iterator x = registeredUser.listIterator();
        while(x.hasNext() && toReset == null) {
            User a = (User)(x.next());
            if(a.getUser().equals(email))
                toReset = a;
        }

        // double check token match
        if(!toReset.getToken().equals(token))
            return false;

        toReset.setPassword(newPassword);

        try {
            dbClient.update(toReset);
        } catch(Exception e) {
            dbClient.shutdown();
            return false;
        }

        dbClient.shutdown();
        return true;
    }
}
