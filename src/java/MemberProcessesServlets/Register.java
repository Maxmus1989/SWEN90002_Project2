/********************************************************
* Student Name: Choong Teik Tan                         *
* Student Number: 568701                                *
* Student Email: choongt@student.unimelb.edu.au         *
* File: Register.java <servlet> (SWEN90002 Project 2)   *
********************************************************/

package MemberProcessesServlets;

import Entity.BindingsUserDB;
import Entity.Registration;
import SendEmail.SendEmail;
import SendEmail.SentInternalMail;
import Util.EmailValidator;
import Util.MessageCreator;
import Util.RandomGenerator;
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

public class Register extends HttpServlet {

    private static BindingsUserDB userDB = null;
    public static List<Registration> registrationQueue = new LinkedList<Registration>();
    public static final long FIVE_MINUTES = 1000*60*5;

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
        // Unsafe user can call to serverdirectly via url to register
        // e.g:
        // http://swen90002-03.cis.unimelb.edu.au:8080/proj1/Register?email=maxmus.teik89%40gmail.com
    } 
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        JSONObject JSOB = new JSONObject();
        String email = request.getParameter("email").toLowerCase();
        String title = " Fail Sending Confirmation";
        String serverreply = email;
        String status = "error";
        
        // validate email if pass, generate token then create email.
        EmailValidator emailValidator = new EmailValidator();
        if (emailValidator.isValidate(email)) {
            // Check the email address is already REGISTERED!
            if(userDB.find(email)) {
                status = "AlreadyMEMBER";
                serverreply = "<b><i><u>"+email+"</u></i></b><br>Server detected email address already registered as a member..<br>If you had forget your password,<br>Please Request from <b><font color=\"blue\"><a href=\"#reminder\">Reminder</a></font></b> Tab.";
            }
            else {
                // Check the email address has already been sent a confirmation Email. but not yet been clicked (<5 minutes)
                Registration previousRecord = null;
                if (!registrationQueue.isEmpty()) {
                    Iterator x = registrationQueue.listIterator();
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
                        registrationQueue.remove(previousRecord);

                    String token = RandomGenerator.generateSessionKey(26);
                    String subject = MessageCreator.getConfirmationSubjectTitle();
                    // Use _HTML for SendMail, Use _TEXT for SendInternalMail
                    //String message = MessageCreator.createConfirmationMessage_TEXT(token, email);
                    String message = MessageCreator.createConfirmationMessage_HTML(token, email);

                    // Send Internal Mail (SendMail working in VM via my account)
                    //if (new SentInternalMail().sendEmail(subject, message, email)) {
                    if ((new SendEmail().sendEmail(subject, message, email))) {
                        // Add to queue.
                        registrationQueue.add(new Registration(email, token));
                        status = "sent";
                        title = "Registration Email Sent";
                        serverreply = "<b><i><u>"+email+"</u></i></b><br>Please check ur email to complete registration.";//+" ("+registrationQueue.size()+")";
                    }
                    else {
                        status = "reregister";
                        serverreply += "Unable to Reach!!<br>Please try again..";
                    }
                }
                else{
                    status = "waiting";
                    String cooldownTime = String.valueOf((FIVE_MINUTES-previousRecord.compareTime())/60000);
                    String sentTime = String.valueOf((5-Integer.parseInt(cooldownTime)));
                    serverreply = "<b><i><u>"+email+"</u></i></b><br><br>Confirmation Email had sent in less then <font color=\"red\">"+sentTime+"</font> minutes."
                            + "<br>Server restrict of 5 minutes cooldown period per email address."
                            + "<br>Please recheck your email to complete registration."
                            + "<br><br>Or you may try to register again after <font color=\"red\">"+cooldownTime+"</font> minutes.";
                }
            }

        }
        else {
            serverreply = "<b><i><u>"+email+"</u></i></b><br>Invalid Email!<br><br><A HREF=\"proj2/#register\"><b>Try Again</b></A>";
        }
        
        JSOB.put("serverreply", serverreply); 
        JSOB.put("status", status);
        JSOB.put("email", email);
        JSOB.put("title", title);

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

