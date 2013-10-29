<%-- 
    Document   : memberpage
    Created on : Oct 11, 2013, 12:37:54 AM
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

    if (email.equals("") || token.equals("")) {
        response.sendRedirect("./index.jsp");
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
        <!-- Member Page [START] -->
        <div data-role="page" id="home" class="memberpage" data-title="SWEN90002 Project 2 (Member Page)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
                <!--a href="memberpage.jsp" data-role="button" data-icon="alert" data-iconpos="left" class="ui-btn-left" data-mini="true">Feedback</a-->
                <a class="logout" data-role="button" data-icon="delete" data-iconpos="left" class="ui-btn-right" data-mini="true">Log Out</a>
            </div><!-- /header -->
            <div data-role="content">
                <!--h2 class="text-color-white" id="message-title"></h2-->
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <p id="message-body"><% out.print(email); %><br><% out.print(token); %></p>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="navbar">
                    <ul>
			<li><a href="#home" data-role="button" data-icon="home" data-iconpos="top" class="ui-disabled">Home</a></li>
			<li><div class="groups-button" data-role="button" data-icon="add" data-iconpos="top">Groups</div></li>
			<li><a href="#profile" data-role="button" data-icon="star" data-iconpos="top">Profile</a></li>
                    </ul>
                </div><!-- /navbar -->
            </div><!-- /footer -->
        </div>
        <!-- Member Page [END] -->

        <!-- Group Page [START] -->
        <div data-role="page" id="groups" class="memberpage" data-title="SWEN90002 Project 2 (Member Page)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
                <!--a href="memberpage.jsp" data-role="button" data-icon="alert" data-iconpos="left" class="ui-btn-left" data-mini="true">Feedback</a-->
                <a class="logout" data-role="button" data-icon="delete" data-iconpos="left" class="ui-btn-right" data-mini="true">Log Out</a>
            </div><!-- /header -->
            <div data-role="content">
                <div id="group-list-container" data-role = "fieldcontain" class="text-color-white no-field-separator" >
                    <ul id="grouplist" data-icon="false" data-role="listview" data-inset="true" data-filter="true" data-divider-theme="b" data-theme="c" data-filter-theme="e" data-filter-placeholder="Filter">
                        <li id="group-created" data-role="list-divider" >Created Group</li>
                        <li id="group-jointed" data-role="list-divider">Jointed Group</li>
                    </ul>
                </div>
            </div><!-- /content -->

            <div data-role="footer" data-position="fixed">
                <div data-role="controlgroup" data-type="horizontal" class="center">
                    <a id ="create-group-button" href="#create-group-dialog" data-role="button" data-icon="plus" data-iconpos="left" data-mini="true" data-theme="a">Create</a>
                    <a id="refresh-grouplist" data-role="button" data-icon="refresh" data-iconpos="left" data-mini="true" data-theme="a">Refresh</a>
                </div>
                <div data-role="navbar">
                    <ul>
			<li><a href="#home" data-role="button" data-icon="home" data-iconpos="top">Home</a></li>
			<li><div class="groups-button ui-disabled" data-role="button" data-icon="add" data-iconpos="top" >Groups</div></li>
			<li><a href="#profile" data-role="button" data-icon="star" data-iconpos="top">Profile</a></li>
                    </ul>
                </div><!-- /navbar -->
            </div><!-- /footer -->
        </div>
        <!-- Group Page [END] -->

        <!-- Profile Page [START] -->
        <div data-role="page" id="profile" class="memberpage" data-title="SWEN90002 Project 2 (Member Page)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
                <!--a href="memberpage.jsp" data-role="button" data-icon="alert" data-iconpos="left" class="ui-btn-left" data-mini="true">Feedback</a-->
                <a class="logout" data-role="button" data-icon="delete" data-iconpos="left" class="ui-btn-right" data-mini="true">Log Out</a>
            </div><!-- /header -->
            <div data-role="content">
                <p class="text-color-white center" id="message-title"><b><% out.print(email); %></b></p>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <br>
                    <a id="reset" data-role="button" data-icon="gear" data-iconpos="top">Reset Password</a>
                    <a id="delete" href="#confirm-delete-dialog" id="confirmdelete" data-role="button" data-icon="delete" data-iconpos="top">Delete Account</a>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="navbar">
                    <ul>
			<li><a href="#home" data-role="button" data-icon="home" data-iconpos="top">Home</a></li>
                        <li><div class="groups-button" data-role="button" data-icon="add" data-iconpos="top">Groups</div></li>
			<li><a href="#profile" data-role="button" data-icon="star" data-iconpos="top" class="ui-disabled">Profile</a></li>
                    </ul>
                </div><!-- /navbar -->
            </div><!-- /footer -->
        </div>
        <!-- Profile Page [END] -->

        <!-- Create Group Dialog [START] -->
        <div data-role="dialog" id="create-group-dialog" class="memberpage" data-title="SWEN90002 Project 2 (Create Group)">
            <div data-role="header" data-position="fixed">
                <h2>CHOONGT'S P2</h2>
            </div><!-- /header -->
            <div data-role="content">
                <h3 class="text-color-white">Create Group</h3>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                        <label for="group-name"><abbr title="[Group Name]<br><b>Any String That You Like..<br>But not Empty :)</b>" rel="tooltip">Group Name:</abbr></label>
                        <input type="text" name="group-name" id="new-group-name" class="text-color-white" value=""  />
                    </div>
                    <div data-role = "fieldcontain" class="tips-to-display-group text-color-red" style="display:none;"><p class="validateTips-group"></p></div>
                    <div id="confirm-create-group-yes" data-role="button" data-icon="check" data-iconpos="top">Yes</div>
                    <a id="confirm-create-group-no" href="#groups" data-role="button" data-icon="delete" data-iconpos="top">No</a>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <h2>Create New Group</h2>
            </div><!-- /footer -->
        </div>
        <!-- Create Group Dialog [END] -->

        <!-- Confirm Delete Account Dialog [START] -->
        <div data-role="dialog" id="confirm-delete-dialog" class="memberpage" data-title="SWEN90002 Project 2 (Confirm Delete Account)">
            <div data-role="header" data-position="fixed">
                <h2>CHOONGT'S P2</h2>
            </div><!-- /header -->
            <div data-role="content">
                <h3 class="text-color-white">Delete Account?</h3>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <p class="center">Are You Sure You Wan To Delete Registration?</p>
                    <a id="confirm-delete-yes" data-role="button" data-icon="alert" data-iconpos="top">Yes</a>
                    <a id="confirm-delete-no" href="#profile" data-role="button" data-icon="check" data-iconpos="top">No</a>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <h2>We Will Miss You!!</h2>
            </div><!-- /footer -->
        </div>
        <!-- Confirm Delete Account Dialog [END] -->

        <!-- Display Message Page [START] -->
        <div data-role="page" id="message-member" class="memberpage" data-title="SWEN90002 Project 2 (Member Request Result)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
                <a href="memberpage.jsp" data-role="button" data-icon="alert" data-iconpos="left" class="ui-btn-left" data-mini="true">Feedback</a>
                <a class="logout" data-role="button" data-icon="delete" data-iconpos="left" class="ui-btn-right" data-mini="true">Log Out</a>
            </div><!-- /header -->
            <div data-role="content">
                <h2 class="text-color-white" id="message-title-member"></h2>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <p id="message-body-member"></p>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="navbar">
                    <ul>
			<li><a href="#home" data-role="button" data-icon="home" data-iconpos="top">Home</a></li>
                        <li><div class="groups-button" data-role="button" data-icon="add" data-iconpos="top">Groups</div></li>
			<li><a href="#profile" data-role="button" data-icon="star" data-iconpos="top">Profile</a></li>
                    </ul>
                </div><!-- /navbar -->
            </div><!-- /footer -->
        </div>
        <!-- Display Message Page [END] -->
        
        <div id="block-ui"></div>
    </body>
</html>
