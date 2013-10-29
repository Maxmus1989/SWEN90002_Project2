<%-- 
    Document   : chatpage
    Created on : Oct 12, 2013, 2:33:35 PM
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
    <body onload="noBack(); init();" onpageshow="if (event.persisted) noBack();" onunload="" id="ctr">
        <!-- Member Page [START] -->
        <div data-role="page" id="chatroom" class="memberpage" data-title="SWEN90002 Project 2 (Chat Room)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
                <a href="memberpage.jsp#groups" data-role="button" data-icon="back" data-iconpos="left" class="ui-btn-left" data-mini="true" rel="external">Back</a>
                <a href="#group-info" data-role="button" data-icon="info" data-iconpos="left" class="ui-btn-right" data-mini="true" >Info</a>
            </div><!-- /header -->
            <div id="content" data-role="content">
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <div id="message-display"class="container">
                    </div>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="fieldcontain">
                    <table style='width:100%'>
                        <tr>
                            <td>
                                <textarea id="message-contet" rows="1" cols="3000" style="height:1em;"></textarea>
                            </td>
                            <td><!--td style='font-size:80%; width:7em'-->
                                <a id="send-message-button" data-role="button" data-theme="a" data-mini="true" class="ui-right">Send</a>
                            </td>
                        </tr>
                    </table>
                </div>
            </div><!-- /footer -->
        </div>
        <!-- Member Page [END] -->

        <!-- Group Chat Info Page [START] -->
        <div data-role="page" id="group-info" class="memberpage" data-title="SWEN90002 Project 2 (Chat Room Info)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
                <a href="#chatroom" data-role="button" data-icon="back" data-iconpos="left" class="ui-btn-left" data-mini="true">Back</a>
            </div><!-- /header -->
            <div id="content" data-role="content" class="text-color-white no-field-separator">
                <h2 id="group-name"></h2>
                <ul id="group-member-list" data-icon="false" data-role="listview" data-inset="true" data-filter="true" data-divider-theme="b" data-theme="c" data-filter-theme="e" data-filter-placeholder="Filter Group">
                    <li id="group-owner" data-role="list-divider" >Group Owner</li>
                    <li id="group-member" data-role="list-divider">Group Member</li>
                </ul>
                <a id="invite-user-button" href="#group-invitation" data-role="button" data-icon="star" data-iconpos="top" data-mini="true" data-theme="e" >Invite</a>
                <a id="delete-user-button" href="#remove-member-dialog" data-role="button" data-icon="delete" data-iconpos="top" data-mini="true" data-theme="e">Remove Member</a>
                <a id="quit-group-button" data-role="button" data-icon="delete" data-iconpos="top" data-mini="true" data-theme="e">Quit Group</a>
                <a id="delete-group-button" href="#delete-group-dialog" data-role="button" data-icon="alert" data-iconpos="top" data-mini="true" data-theme="e" >Delete Group</a>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="fieldcontain">
                    <p class="center">Group Info</p>
                </div>
            </div><!-- /footer -->
        </div>
        <!-- Group Chat Info Page [END] -->

        <!-- Group Invitation Dialog [START] -->
        <div data-role="dialog" id="group-invitation" data-title="SWEN90002 Project 2 (Chat Room Invitation)">
            <div data-role="header" data-position="fixed">
                <h1>CHOONGT'S P2</h1>
            </div><!-- /header -->
            <div id="content" data-role="content" class="text-color-white no-field-separator">
                <h2 id="group-invitation-name"></h2>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <label for="invite-email"><abbr title="[Example of Valid Email Address]<br><b>choongt@student.unimelb.edu.au</b>" rel="tooltip">Email:</abbr></label>
                    <input type="email" name="invite-email" id="invite-email" class="text-color-white" value=""  />
                </div>
                <div data-role = "fieldcontain" class="tips-to-display-invite text-color-red" style="display:none;"><p class="validateTips-invite"></p></div>
                <div id="invite-button" data-role="button" data-icon="plus" data-iconpos="top" data-mini="true">Send</div>
                <a href="#group-info" data-role="button" data-icon="delete" data-iconpos="top" data-mini="true">Cancel</a>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <div data-role="fieldcontain">
                    <p class="center">Group Invitation</p>
                </div>
            </div><!-- /footer -->
        </div>
        <!-- Group Invitation Dialog [END] -->

        <!-- Confirm Quit Group Dialog [START] -->
        <div data-role="dialog" id="confirm-quit-group-dialog" class="memberpage" data-title="SWEN90002 Project 2 (Confirm Delete Account)">
            <div data-role="header" data-position="fixed">
                <h2>CHOONGT'S P2</h2>
            </div><!-- /header -->
            <div data-role="content">
                <h3 class="text-color-white">Quit Group?</h3>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <p id="group-to-quit" class="center"></p>
                    <p class="center">Are You Sure You Wan To Quit This Group?</p>
                    <a id="confirm-quit-yes" data-role="button" data-icon="alert" data-iconpos="top">Yes</a>
                    <a id="confirm-quit-no" href="#group-info" data-role="button" data-icon="check" data-iconpos="top">No</a>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <h2>They Will Miss You!!</h2>
            </div><!-- /footer -->
        </div>
        <!-- Confirm Quit Group Dialog [END] -->

        <!-- Remove Member Dialog [START] -->
        <div data-role="dialog" id="remove-member-dialog" class="memberpage" data-title="SWEN90002 Project 2 (Remove Member)">
            <div data-role="header" data-position="fixed">
                <h2>CHOONGT'S P2</h2>
            </div><!-- /header -->
            <div data-role="content">
                <h3 class="text-color-white">Remove Member</h3>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <div data-role="fieldcontain" id="cg">
                    </div>
                    <br>
                    <a id="confirm-remove-yes" data-role="button" data-icon="alert" data-iconpos="top">Yes</a>
                    <a id="confirm-remove-no" href="#group-info" data-role="button" data-icon="check" data-iconpos="top">No</a>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <h2>They Will Miss You!!</h2>
            </div><!-- /footer -->
        </div>
        <!-- Remove Member Dialog [END] -->

        <!-- Confirm Delete Account Dialog [START] -->
        <div data-role="dialog" id="delete-group-dialog" class="memberpage" data-title="SWEN90002 Project 2 (Confirm Delete Group)">
            <div data-role="header" data-position="fixed">
                <h2>CHOONGT'S P2</h2>
            </div><!-- /header -->
            <div data-role="content">
                <h3 class="text-color-white">Delete Group?</h3>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <p class="center">Are You Sure You Wan To Delete This Group?</p>
                    <a id="confirm-delete-group-yes" data-role="button" data-icon="alert" data-iconpos="top">Yes</a>
                    <a id="confirm-delete-group-no" href="#group-info" data-role="button" data-icon="check" data-iconpos="top">No</a>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <h2>We Will Miss You!!</h2>
            </div><!-- /footer -->
        </div>
        <!-- Confirm Delete Account Dialog [END] -->

        <!-- Message Dialog [START] -->
        <div data-role="dialog" id="chatpage-message-dialog" class="memberpage" data-title="SWEN90002 Project 2">
            <div data-role="header" data-position="fixed">
                <h2>CHOONGT'S P2</h2>
            </div><!-- /header -->
            <div data-role="content">
                <h3 class="text-color-white" id="inform-message-title"></h3>
                <div data-role = "fieldcontain" class="text-color-white no-field-separator">
                    <p class="center" id="inform-message-content"></p>
                    <a id="informed-button" href="#group-info" data-role="button" data-icon="check" data-iconpos="top">OK</a>
                </div>
            </div><!-- /content -->
            <div data-role="footer" data-position="fixed">
                <h2>Server Reply</h2>
            </div><!-- /footer -->
        </div>
        <!-- Message Dialog [END] -->

        <div id="block-ui"></div>
    </body>
</html>
