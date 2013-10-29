/********************************************************
* Student Name: Choong Teik Tan                         *
* Student Number: 568701                                *
* Student Email: choongt@student.unimelb.edu.au         *
* File: Reminder.java <servlet> (SWEN90002 Project 2)   *
********************************************************/

package MemberProcessesServlets;

import Entity.BindingsUserDB;
import Entity.Registration;
import Entity.User;
import SendEmail.SendEmail;
import Util.EmailValidator;
import Util.MessageCreator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

public class Reminder extends HttpServlet {

    private static BindingsUserDB userDB = null;
    public static List<Registration> reminderQueue = new LinkedList<Registration>();
    private static final long FIVE_MINUTES = 1000*60*5;

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
        String email = request.getParameter("email").toLowerCase();

        String serverreply = "?";
        String status = "error";
        String title = "Error Detected!!";

        // validate email if pass, generate token then create email.
        EmailValidator emailValidator = new EmailValidator();
        if (emailValidator.isValidate(email)) { // valid case to continue.
            // Check the email address has already been sent a Reminder Email. (<5 minutes)
            Registration previousRecord = null;
            if (!reminderQueue.isEmpty()) {
                Iterator x = reminderQueue.listIterator();
                while(x.hasNext() && previousRecord == null) {
                    Registration a = (Registration)(x.next());
                    if(a.getEmail().equals(email))
                        previousRecord = a;
                }
            }

            // check is not in Queue record or already pass 5 minutes to send email.
            if (previousRecord == null || previousRecord.compareTime() > FIVE_MINUTES) {
                // remove previous record if more then 5 minutes.
                if(previousRecord != null)
                    reminderQueue.remove(previousRecord);

                // Check the email address is already REGISTERED! to proceed.
                if(userDB.find(email)) {
                    status = "found";
                    serverreply = "";
                    User user = userDB.getUser(email);
                    Registration r = new Registration(email, "");
                    String subject = MessageCreator.getReminderSubjectTitle();
                    // Use _HTML for SendMail, Use _TEXT for SendInternalMail
                    //String message = MessageCreator.createSuccessRegistrationMessage_TEXT(user.getPassword(), email);
                    String message = MessageCreator.createSuccessRegistrationMessage_HTML(user.getPassword(), email);

                    // Send Internal Mail (SendMail working in VM via my account)
                    //if (new SentInternalMail().sendEmail(subject, message, email)) {
                    if((new SendEmail().sendEmail(subject, message, email))) {
                        title = "Reminder Sent!";
                        status = "success";
                        reminderQueue.add(r);
                        serverreply = "<b><i><u>"+email+"</u></i></b><br>Please Check Your Email..";//<br>In Queue: "+reminderQueue.size();
                    }
                    else {
                        title = "Error Sending Reminder";
                        status = "emailfail";
                        serverreply = "Server Encounter Error Sending Email..<br>Please try again..<br><br><A HREF=\"proj2/#reminder\">Try Again</A>";
                    }
                }
                else {  // return status of not yet a member.
                    status = "notAMEMBER";
                    title = "No Such User!";
                    serverreply = "<b><i><u>"+email+"</u></i></b><br>No Record Found!<br><br>Please <a href=\"#register\"><b>Register An Account</b></a><br><br>Or recheck email address is correct!";
                }
            }
            else { // already sent request & < 5 minutes
                status = "waiting";
                title = "Server Enforced!";
                String cooldownTime = String.valueOf((FIVE_MINUTES-previousRecord.compareTime())/60000);
                String sentTime = String.valueOf((5-Integer.parseInt(cooldownTime)));
                serverreply = "<b><i><u>"+email+"</u></i></b><br>Reminder Email had sent in less then <font color=\"red\">"+sentTime+"</font> minutes."
                        + "<br>Server restrict of 5 minutes cooldown period per email address."
                        + "<br>Please recheck your email."
                        + "<br><br>Or you may try to request again after <font color=\"red\">"+cooldownTime+"</font> minutes.<p>";
            }
        }
        else {
            serverreply = "<b><i><u>"+email+"</u></i></b><br>Invalid Email!<br><br><A HREF=\"proj2/#reminder\">Try Again</A>";
        }

        //response.getWriter().write("{isSuccess: true}");
        JSONObject JSOB = new JSONObject();
        JSOB.put("status", status);
        JSOB.put("title", title);
        JSOB.put("email", email);
        JSOB.put("serverreply", serverreply);
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            out.println(JSOB);
        } finally {
            out.close();
        }
        
    }
    
}
