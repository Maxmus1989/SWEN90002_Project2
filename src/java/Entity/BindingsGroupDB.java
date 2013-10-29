/****************************************************
* Student Name: Choong Teik Tan                     *
* Student Number: 568701                            *
* Student Email: choongt@student.unimelb.edu.au     *
* File: BindingsChatDB.java (SWEN90002 Lab 5)       *
****************************************************/

package Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lightcouch.CouchDbClient;
import org.lightcouch.DesignDocument;
import org.lightcouch.Response;

public class BindingsGroupDB {

    CouchDbClient dbClient = null;

    public BindingsGroupDB() {}

    public List<Group> load() {
        dbClient = new CouchDbClient("couchdbGroup.properties");
        List<Group> groupList = new ArrayList<Group>();

        DesignDocument designDoc = dbClient.design().getFromDesk("Group");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        groupList = dbClient.view("Group/by_all").includeDocs(true).query(Group.class);

        dbClient.shutdown();
        return groupList;
    }

    public boolean save(Group newGroup) {
        dbClient = new CouchDbClient("couchdbGroup.properties");

        try {
            dbClient.save(newGroup);
        } catch(Exception e) {
            dbClient.shutdown();
            return false;
        }

        dbClient.shutdown();
        return true;
    }

    public List<Group> getOwnedGroup(String email) {
        dbClient = new CouchDbClient("couchdbGroup.properties");
        List<Group> groupList = new ArrayList<Group>();
        List<Group> ownedGroupList = new ArrayList<Group>();

        DesignDocument designDoc = dbClient.design().getFromDesk("Group");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        groupList = dbClient.view("Group/by_all").includeDocs(true).query(Group.class);

        //Group group = null;
        Iterator x = groupList.listIterator();
        while(x.hasNext()) {
            Group a = (Group)(x.next());
            if(a.getOwner().equals(email))
                ownedGroupList.add((Group)a);
        }

        dbClient.shutdown();
        return ownedGroupList;
    }

    public List<Group> getJoinedGroup(String email) {
        dbClient = new CouchDbClient("couchdbGroup.properties");
        List<Group> groupList = new ArrayList<Group>();
        List<Group> joinedGroupList = new ArrayList<Group>();

        DesignDocument designDoc = dbClient.design().getFromDesk("Group");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        groupList = dbClient.view("Group/by_all").includeDocs(true).query(Group.class);

        //Group group = null;
        Iterator x = groupList.listIterator();
        while(x.hasNext()) {
            Group a = (Group)(x.next());
            if(a.isMember(email))
                joinedGroupList.add((Group)a);
        }

        dbClient.shutdown();
        return joinedGroupList;
    }

    public boolean addMember(String groupID, String groupOwner, String memberID) {
        boolean success = false;
        dbClient = new CouchDbClient("couchdbGroup.properties");
        List<Group> groupList = new ArrayList<Group>();

        DesignDocument designDoc = dbClient.design().getFromDesk("Group");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        groupList = dbClient.view("Group/by_all").includeDocs(true).query(Group.class);

        Group toAddMember = null;

        Iterator x = groupList.listIterator();
        while(x.hasNext() && toAddMember == null) {
            Group a = (Group)(x.next());
            if(a.getOwner().equalsIgnoreCase(groupOwner) && a.getID().equals(groupID))
                toAddMember = a;
        }

        if(toAddMember != null) {
            toAddMember.addMember(memberID);

            try {
                dbClient.update(toAddMember);
                success = true;
            } catch(Exception e) {
                //dbClient.shutdown();
                //return success;
            }
        }

        dbClient.shutdown();
        return success;
    }

    public Group getGroup(String groupID) {
        dbClient = new CouchDbClient("couchdbGroup.properties");
        List<Group> groupList = new ArrayList<Group>();

        DesignDocument designDoc = dbClient.design().getFromDesk("Group");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        groupList = dbClient.view("Group/by_all").includeDocs(true).query(Group.class);

        Group group = null;
        Iterator x = groupList.listIterator();
        while(x.hasNext() && group == null) {
            Group a = (Group)(x.next());
            if(a.getID().equals(groupID)) {
                //group = new Group();
                group = a;
            }
        }

        dbClient.shutdown();
        return group;
    }

    public boolean removeMember(String groupID, String memberID) {
        boolean success = false;
        dbClient = new CouchDbClient("couchdbGroup.properties");
        List<Group> groupList = new ArrayList<Group>();

        DesignDocument designDoc = dbClient.design().getFromDesk("Group");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        groupList = dbClient.view("Group/by_all").includeDocs(true).query(Group.class);

        Group toRemoveMember = null;

        Iterator x = groupList.listIterator();
        while(x.hasNext() && toRemoveMember == null) {
            Group a = (Group)(x.next());
            if(a.getID().equals(groupID))
                toRemoveMember = a;
        }

        if(toRemoveMember != null) {
            toRemoveMember.removeMember(memberID);

            try {
                dbClient.update(toRemoveMember);
                success = true;
            } catch(Exception e) {
                //dbClient.shutdown();
                //return success;
            }
        }

        dbClient.shutdown();
        return success;
    }

    public boolean delete(String groupid) {

        boolean success = true;
        dbClient = new CouchDbClient("couchdbGroup.properties");
        DesignDocument designDoc = dbClient.design().getFromDesk("Group");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        List<Group> groupList = dbClient.view("Group/by_all").includeDocs(true).query(Group.class);
        Group toDelete = null;

        Iterator x = groupList.listIterator();
        while(x.hasNext() && toDelete == null) {
            Group a = (Group)(x.next());
            if(a.getID().equals(groupid))
                toDelete = a;
        }

        try {
            dbClient.remove(toDelete);
            success = ChatProcessesServlets.Chat.delete(groupid);
        } catch(Exception e) {
            dbClient.shutdown();
            return false;
        }

        dbClient.shutdown();
        return success;
    }

    public boolean addPendingMember(String groupID, String groupOwner, String memberID) {
        boolean success = false;
        dbClient = new CouchDbClient("couchdbGroup.properties");
        List<Group> groupList = new ArrayList<Group>();

        DesignDocument designDoc = dbClient.design().getFromDesk("Group");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        groupList = dbClient.view("Group/by_all").includeDocs(true).query(Group.class);

        Group toAddMember = null;

        Iterator x = groupList.listIterator();
        while(x.hasNext() && toAddMember == null) {
            Group a = (Group)(x.next());
            if(a.getOwner().equalsIgnoreCase(groupOwner) && a.getID().equals(groupID))
                toAddMember = a;
        }

        if(toAddMember != null) {
            toAddMember.addPendingMember(memberID);

            try {
                dbClient.update(toAddMember);
                success = true;
            } catch(Exception e) {
                //dbClient.shutdown();
                //return success;
            }
        }

        dbClient.shutdown();
        return success;
    }

    public boolean removePendingMember(String groupID, String memberID) {
        boolean success = false;
        dbClient = new CouchDbClient("couchdbGroup.properties");
        List<Group> groupList = new ArrayList<Group>();

        DesignDocument designDoc = dbClient.design().getFromDesk("Group");
        Response response = dbClient.design().synchronizeWithDb(designDoc);
        groupList = dbClient.view("Group/by_all").includeDocs(true).query(Group.class);

        Group toRemoveMember = null;

        Iterator x = groupList.listIterator();
        while(x.hasNext() && toRemoveMember == null) {
            Group a = (Group)(x.next());
            if(a.getID().equals(groupID))
                toRemoveMember = a;
        }

        if(toRemoveMember != null) {
            toRemoveMember.removePendingMember(memberID);

            try {
                dbClient.update(toRemoveMember);
                success = true;
            } catch(Exception e) {
                //dbClient.shutdown();
                //return success;
            }
        }

        dbClient.shutdown();
        return success;
    }
}
