/*********************************************************
* Student Name: Choong Teik Tan                          *
* Student Number: 568701                                 *
* Student Email: choongt@student.unimelb.edu.au          *
* File: Registration.java <Entity> (SWEN90002 Project 2) *
*********************************************************/

package Entity;

import java.util.Date;

public class Registration {
    String email = "";
    String token = "";
    long time = 0;

    public Registration(String email, String token) {
        this.email = email;
        this.token = token;
        this.time = new Date().getTime();
    }

    public void setEmail(String email) {this.email = email;}
    public void setToken(String token) {this.token = token;}
    public void setTime() {this.time = new Date().getTime();}

    public String getEmail() {return email;}
    public String getToken() {return token;}
    public long getTime() {return time;}

    public long compareTime() {return (new Date().getTime())-time;}

}
