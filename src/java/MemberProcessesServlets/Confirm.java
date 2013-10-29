/********************************************************
* Student Name: Choong Teik Tan                         *
* Student Number: 568701                                *
* Student Email: choongt@student.unimelb.edu.au         *
* File: Confirm.java <servlet> (SWEN90002 Project 2)    *
********************************************************/

package MemberProcessesServlets;

import java.io.PrintWriter;
import Entity.BindingsUserDB;
import Entity.Registration;
import Entity.User;
import SendEmail.SendEmail;
import Util.EmailValidator;
import Util.MessageCreator;
import Util.RandomGenerator;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Confirm extends HttpServlet {

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
        String email = request.getParameter("email").toLowerCase();
        String token = request.getParameter("token");
        String status = new String();

        // validate email if pass, generate token then create email.
        EmailValidator emailValidator = new EmailValidator();
        if (emailValidator.isValidate(email)) {
            // check if already register!! (database check)
            if(userDB.find(email)) {
                status = "AlreadyMEMBER";
            }
            else {
                Registration previousRecord = null;
                if (!Register.registrationQueue.isEmpty()) {
                    Iterator x = Register.registrationQueue.listIterator();
                    while(x.hasNext() && previousRecord == null) {
                        Registration a = (Registration)(x.next());
                        if(a.getEmail().equals(email)) {
                            previousRecord = a;
                        }
                    }
                }

                if(previousRecord != null) {
                    // it take more then 5 minutes to click on the link.
                    if(previousRecord.compareTime() > Register.FIVE_MINUTES) {
                        status = "expired";
                        Register.registrationQueue.remove(previousRecord);
                    }
                    // wrong token case
                    else if (!previousRecord.getToken().equals(token)) {
                        status = "incorrectToken";
                    }
                    // else then create (add to database & remove from queue)
                    else {
                        String password = RandomGenerator.generateSessionKey(6);
                        User newUser = new User(email, password);
                        if(userDB.save(newUser)) { // success added to database
                            String subject = MessageCreator.getSuccessRegistrationSubjectTitle();
                            // Use _HTML for SendMail, Use _TEXT for SendInternalMail
                            //String message = MessageCreator.createSuccessRegistrationMessage_TEXT(password, email);
                            String message = MessageCreator.createSuccessRegistrationMessage_HTML(password, email);
                            // Send Internal Mail (SendMail working in VM via my account)
                            //if (new SentInternalMail().sendEmail(subject, message, email)) {
                            if (new SendEmail().sendEmail(subject, message, email)) {
                                // success send email then remove from queue.
                                status = "success";
                                Register.registrationQueue.remove(previousRecord);
                            }
                            else {
                                // delete from database (rollback)
                                userDB.delete(email);
                                status = "emailFail";
                            }
                        }
                    }
                }
                else { // no email record in queue. (modified link)
                    status = "notInQueue";
                }
            }
        }
        else {
            status = "notInQueue";
        }

        /* Response to show registration status */
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        out.println("<title>SWEN90002 Project 2</title>");
        out.println("<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css\" />");
        out.println("<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>");
        out.println("<script src=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js\"></script>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/proj2-style.css\">");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/tooltip.css\">");
        out.println("<script type=\"text/javascript\"  src=\"./js/proj2-jscript.js\"></script>");
        out.println("<script type=\"text/javascript\"  src=\"./js/tooltip.js\"></script>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div data-role=\"page\" id=\"message\" data-title=\"SWEN90002 Project 2 (Registration Result)\">");
        out.println("<div data-role=\"header\" data-position=\"fixed\">");
        out.println("<h1>CHOONGT'S P2</h1>");
        out.println("</div><!-- /header -->");
        out.println("<div data-role=\"content\">");
        out.println("<h2 class=\"text-color-white\">Registration Confirmation</h2>");

        out.println("<div data-role = \"fieldcontain\" class=\"text-color-white no-field-separator\">");
        out.print("<center>");
        if(status.equals("AlreadyMEMBER")) {
            out.print("<h3>Server detected email already registered.</h3>");
            out.print("<p>"+email+"</p>");
            out.print("<h2>");
            out.print("If you forgot your password,<br><br>Please click ");
            out.print("<a href=\"/proj2/#reminder\" rel=external>HERE</a>");
            out.print("</h2>");
        }
        else if(status.equals("expired")) {
            out.print("<h3>Sorry session expired for</h3>");
            out.print("<h2>"+email+"</h2>");
            out.print("<p>(Server enforced to confirm within 5 minutes)</p><br>");
            out.print("<h2>");
            out.print("Please try to register again,<br><br> ");
            out.print("<a href=\"/proj2/#register\" rel=external>To Register</a>");
            out.print("</h2>");
        }
        else if(status.equals("success")) {
            out.print("<h3>An Email with Password had just sent to</h3>");
            out.print("<h2>"+email+"</h2>");
            out.print("<br>Thanks For Joining Us! :)");
            out.print("<h2>");
            out.print("Login to your account via ..<br>");
            out.print("<a href=\"/proj2/\" rel=external>Login Page</a>");
            out.print("</h2>");
        }
        else if(status.equals("incorrectToken")) {
            out.print("<h3>Incorrect token detected for</h3>");
            out.print("<h2>"+email+"</h2>");
            out.print("<h2>");
            out.print("Please recheck your email and try again.<br><br> ");
            out.print("</h2>");
            out.print("<p>(Note: Server enforced to confirm within 5 minutes)</p>");
        }
        else if(status.equals("notInQueue")) {
            out.print("<h3>Incorrect Link Detected</h3>");
            out.print("<p>Server did not receive registration request from</p>");
            out.print("<h2>"+email+"</h2><br>");
            out.print("<h2>");
            out.print("Please recheck your email!<br>");
            out.print("Or try to register again.<br><br>");
            out.print("<a href=\"/proj2/#register\" rel=external>To Register</a>");
            out.print("</h2>");
        }
        else if(status.equals("emailFail")) {
            out.print("<h3>Server encounter error sending Email to</h3>");
            out.print("<h2>"+email+"</h2>");
            out.print("<br><h2>");
            out.print("Please try again..");
            out.print("</h2>");
            out.print("<p>(Note: Server enforced to confirm within 5 minutes)</p>");
            out.print("<p>If you unable to confirm within 5 minutes,<br>please try to register again.</p>");
        }
        out.print("</center>");
        out.println("</div>");

        out.println("</div><!-- /content -->");
        out.println("<div data-role=\"footer\" data-position=\"fixed\">");
        out.println("<div data-role=\"navbar\">");
        out.println("<ul>");
        out.println("<li><a href=\"/proj2/#register\" data-role=\"button\" data-icon=\"gear\" data-iconpos=\"top\" rel=external>Register</a></li>");
        out.println("<li><a href=\"/proj2/#reminder\" data-role=\"button\" data-icon=\"info\" data-iconpos=\"top\" rel=external>Reminder</a></li>");
        out.println("<li><a href=\"/proj2/\" data-role=\"button\" data-icon=\"check\" data-iconpos=\"top\" rel=external>Log In</a></li>");
        out.println("</ul>");
        out.println("</div><!-- /navbar -->");
        out.println("</div><!-- /footer -->");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }

}
