/********************************************************
* Student Name: Choong Teik Tan                         *
* Student Number: 568701                                *
* Student Email: choongt@student.unimelb.edu.au         *
* File: Reset.java <servlet> (SWEN90002 Project 2)      *
********************************************************/

package MemberProcessesServlets;

import Entity.BindingsUserDB;
import Entity.Registration;
import Entity.User;
import SendEmail.SendEmail;
import Util.EmailValidator;
import Util.MessageCreator;
import Util.RandomGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

public class Reset extends HttpServlet {

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
        boolean reset = false;
        String status = "Error";
        String email = request.getParameter("email").toLowerCase();
        String token = request.getParameter("token");

        // validate email if pass, check token with database and process reset.
        EmailValidator emailValidator = new EmailValidator();
        if (emailValidator.isValidate(email)) {
            if(userDB.find(email)) {
                User toReset = userDB.getUser(email);
                // check token match
                if(toReset.getToken().equals(token)){
                    String oldPassword = toReset.getPassword();
                    String newPassword = RandomGenerator.generateSessionKey(6);
                    // if success update database then Email new password to user
                    if(userDB.setPassword(email, token, newPassword)) {
                        String subject = MessageCreator.getResetPasswordSubjectTitle();
                        // Use _HTML for SendMail, Use _TEXT for SendInternalMail
                        //String message = MessageCreator.createSuccessRegistrationMessage_TEXT(newPassword, email);
                        String message = MessageCreator.createSuccessRegistrationMessage_HTML(newPassword, email);

                        // Send Internal Mail (SendMail working in VM via my account)
                        //if (new SentInternalMail().sendEmail(subject, message, email)) {
                        if ((new SendEmail().sendEmail(subject, message, email))) {
                            reset = true;
                            status = "reset";

                            // since password already reset so allow user to request with 5 minutes
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
                        else { // unable to complete sending email (rollback)
                            status = "emailError";
                            userDB.setPassword(email, token, oldPassword);
                        }
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

        JSOB.put("reset", reset);
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
