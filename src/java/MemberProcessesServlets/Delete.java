/********************************************************
* Student Name: Choong Teik Tan                         *
* Student Number: 568701                                *
* Student Email: choongt@student.unimelb.edu.au         *
* File: Delete.java <servlet> (SWEN90002 Project 2)     *
********************************************************/

package MemberProcessesServlets;

import Entity.BindingsUserDB;
import Entity.Registration;
import Entity.User;
import GroupProcessesServlets.GroupProcesses;
import Util.EmailValidator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

public class Delete extends HttpServlet {

    private static BindingsUserDB userDB = null;

    // This Happens Once and is Reused
    @Override
    public void init(ServletConfig config) throws ServletException
    {
               super.init(config);

               try {
                   // create new database binding class
                   userDB = new BindingsUserDB();
               }
               catch(Exception e) {
                    System.out.println(e.getMessage());
               }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        JSONObject JSOB = new JSONObject();
        boolean deleted = false;
        String status = "Error";
        String email = request.getParameter("email").toLowerCase();
        String token = request.getParameter("token");

        // validate email if pass, check token with database and process delete.
        EmailValidator emailValidator = new EmailValidator();
        if (emailValidator.isValidate(email)) {
            if(userDB.find(email)) {
                User toDelete = userDB.getUser(email);
                // check token match
                if(toDelete.getToken().equals(token)){
                    // try delete user
                    if(userDB.delete(email)) {
                        // Delete user owned group && Leave Joined Group
                        GroupProcessesServlets.GroupProcesses.deleteFromRelatedGroup(email);
                        
                        deleted = true;
                        status = "delete";
                        
                        // Check and delete from queue (REMINDER)
                        Registration previousRecord = null;
                        if (!Reminder.reminderQueue.isEmpty()) {
                            Iterator x = Reminder.reminderQueue.listIterator();
                            while(x.hasNext() && previousRecord == null) {
                                Registration a = (Registration)(x.next());
                                if(a.getEmail().equals(email))
                                    previousRecord = a;
                            }
                        }
                        if(previousRecord != null) {
                            Reminder.reminderQueue.remove(previousRecord);
                        }
                    }
                    else {
                        status = "dbError";
                    }
                }
                else {
                    status = "incorrectToken";
                }
            }
            else { // Not possible to come here (Just In Case) <Only if allow to have multiple login AND one use delete registration>
                status = "notAMember";
            }
        }
        else { // Not possible to come here (Just In Case)
            status = "invalidEmail";
        }

        JSOB.put("deleted", deleted);
        JSOB.put("status", status);

        // Respond to client
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            out.println(JSOB);
        } finally {
            out.close();
        }
    }

}
