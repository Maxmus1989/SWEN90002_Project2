<%-- 
    Document   : confirmation
    Created on : Oct 22, 2013, 10:57:38 PM
    Author     : Choong Teik Tan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%
    String email = (String) request.getAttribute("email");

    String status = (String) request.getAttribute("status");
    String groupname = (String) request.getAttribute("groupname");
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
    <body onload="noBack(); init();" onpageshow="if (event.persisted) noBack();" onunload="" id="ctr">
        
        <!-- Group Joining Confirmation Page [START] -->
        <div data-role="page" id="confirm-join" class="memberpage" data-title="SWEN90002 Project 2 (Member Page)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
            </div><!-- /header -->
            <div data-role="content">
                <h2 class="text-color-white" id="message-title">
                    <%
                        if(status.equals("success"))
                            out.print("Success Joining Group");
                        else
                            out.print("Fail Joining Group");
                    %>
                </h2>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <p class="center">
                        <%
                            if(status.equals("success")) {
                                out.print("<h3>Congratulations!!</h3>");
                                out.print("<p class=\"center\">You Are Now A Member of Group "+groupname+"!</p>");
                            }
                            else if(status.equals("isMember")) {
                                out.print("<h3>Error Already A Member!!</h3>");
                                out.print("<p class=\"center\">Server Detectd: ["+email+"] is already a member of group ["+groupname+"]</p>");
                            }
                            else if(status.equals("NOT_YET_REGISTER")){
                                out.print("<h3>Please Register a Account</h3>");
                                out.print("<p class=\"center\">Server Detectd: ["+email+"] is NOT YEAT REGISTER AS A MEMBER!"
                                        + "<br>Inorder to Join this Group, You Must First Register As A Member!</p>");
                            }
                            else {
                                out.print("<h3>Error Link Detected!!</h3>");
                                out.print("<p class=\"center\">Please recheck the link which had already sent to "+email+"</p>");
                            }
                        %>
                    </p>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="navbar">
                    <ul>
			<li><a href="index.jsp#register" data-role="button" data-icon="gear" data-iconpos="top" rel="external" <% if(!status.equals("NOT_YET_REGISTER")) out.print("class=\"ui-disabled\""); %>>Register Account</a></li>
			<li><a href="memberpage.jsp#groups" data-role="button" data-icon="star" data-iconpos="top" rel="external" <% if(status.equals("NOT_YET_REGISTER")) out.print("class=\"ui-disabled\""); %>>Group Page</a></li>
                    </ul>
                </div><!-- /navbar -->
            </div><!-- /footer -->
        </div>
        <!-- Group Joining Confirmation Page [END] -->
        
        <div id="block-ui"></div>
    </body>
</html>
