<%-- 
    Document   : index
    Created on : Oct 6, 2013, 7:35:32 PM
    Author     : Choong Teik Tan
--%>

<%@page import="java.net.URLDecoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%
    String email = "";
    String token = "";

    Cookie cookie = null;
    Cookie[] cookies = null;
    cookies = request.getCookies();
    if( cookies != null ){
        for (int i = 0; i < cookies.length; i++){
            cookie = cookies[i];
            if(cookie.getName().equals("email"))
                email = URLDecoder.decode(cookie.getValue(), "UTF-8");
            else if(cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }
    }

    if (!email.equals("") && !token.equals("")) {
        response.sendRedirect("./memberpage.jsp");
    }
%>

<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>SWEN90002 Project 2</title>

        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css" />
        <script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>

        <link rel="stylesheet" type="text/css" href="./css/proj2-style.css">
        <link rel="stylesheet" type="text/css" href="./css/tooltip.css">
        <script type="text/javascript"  src="./js/proj2-jscript.js"></script>
        <script type="text/javascript"  src="./js/tooltip.js"></script>
        <script type="text/javascript" src="./jquery/jquery.cookie.js"></script>
        <script type="text/javascript" src="./js/autosize.js"></script>
    </head>
    <body onload="noBack();" onpageshow="if (event.persisted) noBack();" onunload="">
        <!-- Start of first page [LOGIN] -->
        <div data-role="page" id="login" data-title="SWEN90002 Project 2 (Member Login)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
            </div><!-- /header -->
            <div data-role="content">
                <h2 class="text-color-white">Member Login</h2>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <label for="login-email"><abbr title="[Example of Valid Email Address]<br><b>choongt@student.unimelb.edu.au</b>" rel="tooltip">Email:</abbr></label>
                    <input type="email" name="login-email" id="login-email" class="text-color-white" value=""  />
                </div>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <label for="login-password"><abbr title="Enter a valid password to Log in.<br>If you had forget your password,<br>Please click on <b>Reminder</b> Tab." rel="tooltip">Password:</abbr></label>
                    <input type="password" name="login-password" id="login-password" value="" class="text-color-white" />
                </div>
                <div data-role = "fieldcontain" class="tips-to-display-login text-color-red" style="display:none;"><p class="validateTips-login"></p></div>
                <input id="login-button" type="submit" value="Login" />
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="navbar">
                    <ul>
			<li><a href="#register" data-role="button" data-icon="gear" data-iconpos="top">Register</a></li>
			<li><a href="#reminder" data-role="button" data-icon="info" data-iconpos="top">Reminder</a></li>
			<li><a href="#login" data-role="button" data-icon="check" data-iconpos="top" class="ui-disabled">Log In</a></li>
                    </ul>
                </div><!-- /navbar -->
            </div><!-- /footer -->
        </div>
        <!-- /page [LOGIN] -->

        <!-- Start of page [Register] -->
        <div data-role="page" id="register" data-title="SWEN90002 Project 2 (Member Registration)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
            </div><!-- /header -->
            <div data-role="content">
                <h2 class="text-color-white">Registration</h2>

                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <label for="register-email"><abbr title="[Example of Valid Email Address]<br><b>choongt@student.unimelb.edu.au</b>" rel="tooltip">Email:</abbr></label>
                    <input type="email" name="register-email" id="register-email" class="text-color-white" value=""  />
                </div>
                <div data-role = "fieldcontain" class="tips-to-display-register text-color-red" style="display:none;"><p class="validateTips-register"></p></div>
                <input id="register-button" type="submit" value="Register" />
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="navbar">
                    <ul>
			<li><a href="#register" data-role="button" data-icon="gear" data-iconpos="top" class="ui-disabled">Register</a></li>
			<li><a href="#reminder" data-role="button" data-icon="info" data-iconpos="top">Reminder</a></li>
			<li><a href="#login" data-role="button" data-icon="check" data-iconpos="top">Log In</a></li>
                    </ul>
                </div><!-- /navbar -->
            </div><!-- /footer -->
        </div>
        <!-- /page [Register] -->

        <!-- Start of page [Reminder] -->
        <div data-role="page" id="reminder" data-title="SWEN90002 Project 2 (Password Reminder)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
            </div><!-- /header -->
            <div data-role="content">
                <h2 class="text-color-white">Password Reminder</h2>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <label for="reminder-email"><abbr title="[Example of Valid Email Address]<br><b>choongt@student.unimelb.edu.au</b>" rel="tooltip">Email:</abbr></label>
                    <input type="email" name="reminder-email" id="reminder-email" class="text-color-white" value=""  />
                </div>
                <div data-role = "fieldcontain" class="tips-to-display-reminder text-color-red" style="display:none;"><p class="validateTips-reminder"></p></div>
                <input id="reminder-button" type="submit" value="Request Reminder" />
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="navbar">
                    <ul>
			<li><a href="#register" data-role="button" data-icon="gear" data-iconpos="top">Register</a></li>
			<li><a href="#reminder" data-role="button" data-icon="info" data-iconpos="top" class="ui-disabled">Reminder</a></li>
			<li><a href="#login" data-role="button" data-icon="check" data-iconpos="top">Log In</a></li>
                    </ul>
                </div><!-- /navbar -->
            </div><!-- /footer -->
        </div>
        <!-- /page [Reminder] -->
        
        <!-- Display Message Page [START] -->
        <div data-role="page" id="message" data-title="SWEN90002 Project 2 (Registration Result)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
            </div><!-- /header -->
            <div data-role="content">
                <h2 class="text-color-white" id="message-title"></h2>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <p id="message-body"></p>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="navbar">
                    <ul>
			<li><a href="#register" data-role="button" data-icon="gear" data-iconpos="top">Register</a></li>
			<li><a href="#reminder" data-role="button" data-icon="info" data-iconpos="top">Reminder</a></li>
			<li><a href="#login" data-role="button" data-icon="check" data-iconpos="top">Log In</a></li>
                    </ul>
                </div><!-- /navbar -->
            </div><!-- /footer -->
        </div>
        <!-- Display Message Page [END] -->
        
        <div id="block-ui"></div>
    </body>
</html>
