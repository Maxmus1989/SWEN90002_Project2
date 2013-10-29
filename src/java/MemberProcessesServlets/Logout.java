/********************************************************
* Student Name: Choong Teik Tan                         *
* Student Number: 568701                                *
* Student Email: choongt@student.unimelb.edu.au         *
* File: Logout.java <servlet> (SWEN90002 Project 2)     *
********************************************************/

package MemberProcessesServlets;

import Entity.BindingsUserDB;
import Entity.User;
import Util.EmailValidator;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

public class Logout extends HttpServlet {

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
        boolean logout = false;
        String status = "Error";
        String email = request.getParameter("email").toLowerCase();
        String token = request.getParameter("token");

        // Since Enforce redirect checking by using cookie in jsp so some checking will not be necessary,
        // Just In Case and Requirement Asked, So I include in..
        
        // validate email if pass, check token with database and process logout.
        EmailValidator emailValidator = new EmailValidator();
        if (emailValidator.isValidate(email)) {
            if(userDB.find(email)) {
                User toLogout = userDB.getUser(email);
                // check token match
                if(toLogout.getToken().equals(token)){
                    if(userDB.logout(email)) {
                        status = "logout";
                        logout = true;
                    }
                }
                else {
                    status = "incorrectToken";
                }
            }
            else { // Not possible to come here (Just In Case)
                status = "notAMember";
            }
        }
        else { // Not possible to come here (Just In Case)
            status = "invalidEmail";
        }

        JSOB.put("logout", logout);
        JSOB.put("status", status);

        System.out.println("\n\n"+status);

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
