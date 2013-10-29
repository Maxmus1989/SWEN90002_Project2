/****************************************************
* Student Name: Choong Teik Tan                     *
* Student Number: 568701                            *
* Student Email: choongt@student.unimelb.edu.au     *
* File: User.java (SWEN90002 Project 2)             *
****************************************************/

package Entity;

import java.sql.Timestamp;
import org.lightcouch.Document;

public class User extends Document{

    private String user = "";
    private String password = "";
    private String token = "";
    private boolean login = false;
    private Timestamp memberSince = null;

    public User (String user, String password) {
        this.user = user;
        this.password = password;
        // auto timestap when created.
        java.util.Date date = new java.util.Date();
        this.memberSince = new Timestamp(date.getTime());
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void login() {login = true;}
    public void logout() {login = false;}
    
    public String getUser() { return user;}
    public String getPassword() { return password;}
    public String getToken() { return token;}
    public boolean isLogin() { return login;}
    public Timestamp memberSince() { return memberSince;}
}
