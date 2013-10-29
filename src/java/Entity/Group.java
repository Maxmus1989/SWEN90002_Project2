/****************************************************
* Student Name: Choong Teik Tan                     *
* Student Number: 568701                            *
* Student Email: choongt@student.unimelb.edu.au     *
* File: Group.java (SWEN90002 Project 2)             *
****************************************************/

package Entity;

import Util.RandomGenerator;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import org.lightcouch.Document;

public class Group extends Document{

    private String id = "";
    private String name = "";
    private String owner = "";
    private Timestamp since = null;
    private List<String> member = null;
    private List<String> pendingMember = null;


    public Group (String name, String owner) {

        this.id = RandomGenerator.generateSessionKey(26);
        this.name = name;
        this.owner = owner;
        this.member = new LinkedList<String>();
        this.pendingMember = new LinkedList<String>();
        // auto timestap when created.
        java.util.Date date = new java.util.Date();
        this.since = new Timestamp(date.getTime());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() { return id;}
    public String getOwner() { return owner;}
    public String getName() { return name;}
    public Timestamp getSince() { return since;}

    public void addMember(String email) {
        member.add(new String(email));
    }
    
    public boolean removeMember(String email) {
        return member.remove(email);
    }
    
    public boolean isMember(String email) {
        boolean contain = false;
        for(String x : member) {
            if(x.equalsIgnoreCase(email))
                contain = true;
        }

        return contain;
    }

    public List<String> getMembers() {return member;}

    public void addPendingMember(String email) {
        pendingMember.add(new String(email));
    }

    public boolean removePendingMember(String email) {
        return pendingMember.remove(email);
    }

    public boolean isPendingMember(String email) {
        boolean contain = false;
        for(String x : pendingMember) {
            if(x.equalsIgnoreCase(email))
                contain = true;
        }

        return contain;
    }

    public List<String> getPendingMembers() {return pendingMember;}

}
