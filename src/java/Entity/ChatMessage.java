/****************************************************
 * Student Name: Choong Teik Tan                     *
 * Student Number: 568701                            *
 * Student Email: choongt@student.unimelb.edu.au     *
 * File: ChatMessage.java (SWEN90002 Project 2)      *
 ****************************************************/
package Entity;

import java.sql.Timestamp;
import org.lightcouch.Document;

public class ChatMessage extends Document{

    String GroupID;
    String User;
    String Message;
    Timestamp Time;

    public ChatMessage(int index, String User, String Message, String GroupID) {
        // send index as _id in super class for CouchDB
        //super.setId(String.valueOf(index));

        // auto timestap when created.
        java.util.Date date = new java.util.Date();

        this.GroupID = GroupID;
        this.User = User;
        this.Message = Message;
        this.Time = new Timestamp(date.getTime());
    }

    public int getIndex() {
        return Integer.parseInt(super.getId());
    }

    public String getUser() {
        return User;
    }

    public String getMessage() {
        return Message;
    }

    public Timestamp getTimestamp() {
        return Time;
    }

    public String getGroupID() {
        return GroupID;
    }

}
