/********************************************************
* Student Name: Choong Teik Tan                         *
* Student Number: 568701                                *
* Student Email: choongt@student.unimelb.edu.au         *
* File: Login.java <servlet> (SWEN90002 Project 2)      *
********************************************************/

package MemberProcessesServlets;

import Entity.BindingsUserDB;
import Entity.User;
import Util.EmailValidator;
import Util.RandomGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

public class Login extends HttpServlet {

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
        String status = "Error";
        String token = "";
        boolean success = false;

        String email = request.getParameter("email").toLowerCase();
        String password = request.getParameter("password");

        System.out.println("Email: "+email+"\nPassword: "+password);

        // validate email if pass, generate token then create email.
        EmailValidator emailValidator = new EmailValidator();
        if (emailValidator.isValidate(email)) {
            if(userDB.find(email)) {
                User toLogin = userDB.getUser(email);
                // check password match
                if(toLogin.getPassword().equals(password)){
                    // crete token
                    token = RandomGenerator.generateSessionKey(26);
                    if(userDB.login(email, token)) {
                        status = "login";
                        success = true;
                    }
                }
                else {
                    status = "incorrectPassword";
                }
            }
            else {
                status = "notAMember";
            }
        }
        else {
            status = "invalidEmail";
        }

        JSOB.put("success", success);
        JSOB.put("status", status);
        JSOB.put("token", token);
        JSOB.put("email", email);

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
