/****************************************************
 * Student Name: Choong Teik Tan                     *
 * Student Number: 568701                            *
 * Student Email: choongt@student.unimelb.edu.au     *
 * File: GroupProcesses.java (SWEN90002 Project 2)   *
 ****************************************************/

package GroupProcessesServlets;

import Entity.BindingsGroupDB;
import Entity.BindingsUserDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Entity.Group;
import SendEmail.*;
import Util.EmailValidator;
import Util.MessageCreator;
import java.util.LinkedList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GroupProcesses extends HttpServlet {

    private static final String jspReporter = "confirmation.jsp";
    
    private static BindingsGroupDB groupDB = null;
    private static List<Group> groupList = new LinkedList<Group>();

     // This Happens Once and is Reused
    @Override
    public void init(ServletConfig config) throws ServletException
    {
               super.init(config);

               try {
                   // create new database binding class and start load data.
                   groupDB = new BindingsGroupDB();
                   groupList = groupDB.load();
               }
               catch(Exception e) {
                    System.out.println(e.getMessage());
               }
    }

    // Been Called only when user click on Invitation Link
    // http://localhost:8082/proj2/GroupProcesses?gid=AB8CJtOYyqb0CQy2kaVuNiX2wT&e=choongt@student.unimelb.edu.au&o=maxmus.teik89@gmail.com
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String status = new String();

        status = "false";

        String email = "";
        String groupid = "";
        String groupowner = "";
        String groupname = "";

        try {
            email = request.getParameter("e").toLowerCase();
            groupid = request.getParameter("gid");
            groupowner = request.getParameter("o").toLowerCase();
        } catch(Exception e) {}
        
        if((new BindingsUserDB()).find(email)) {

            if(!email.equals("") && !groupid.equals("") && !groupowner.equals("")) {
                Group inviteGroup = groupDB.getGroup(groupid);

                if(inviteGroup != null) {
                    groupname = inviteGroup.getName();

                    if(inviteGroup.isPendingMember(email) && !inviteGroup.isMember(email)) {
                        boolean success = true;

                        success = success && groupDB.removePendingMember(groupid, email);
                        success = success && groupDB.addMember(groupid, groupowner, email);

                        int i = 0;
                        while (!groupList.get(i).getID().equals(groupid)) {
                            i++;
                        }
                        success = success && groupList.get(i).removePendingMember(email);
                        groupList.get(i).addMember(email);

                        if(success)
                            status = "success";
                    }
                    else {
                        status = "isMember";
                    }
                }
            }
        }
        else {
            status = "NOT_YET_REGISTER";
        }

        request.setAttribute("groupname", groupname);
        request.setAttribute("status", status);
        request.setAttribute("email", email);

        RequestDispatcher dispatch = request.getRequestDispatcher(jspReporter);
        dispatch.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        JSONObject JSOB = new JSONObject();
        String status = "Error";
        
        boolean isowner = false;

        String type = request.getParameter("type").toLowerCase();
        String email = request.getParameter("email").toLowerCase();
        
        // if User request create group..
        if (type.equalsIgnoreCase("create")) {
            String groupname = request.getParameter("groupname").toLowerCase();
            // validate email if pass, generate token then create email.
            EmailValidator emailValidator = new EmailValidator();
            if (emailValidator.isValidate(email)) {
                boolean exist = false;
                
                for(Group x : groupList) {
                    // Check is group already exist.
                    if(x.getName().equals(groupname) && x.getOwner().equals(email)){
                        exist = true;
                    }
                }

                if(exist) {
                    status = "exist";
                }
                else {
                    Group newGroup = new Group(groupname, email);
                    // save new group to database.
                    groupDB.save(newGroup);
                    // save new group to local stored group list.
                    groupList.add(newGroup);
                    status = "success";
                    isowner = true;
                }

            }
            else {
                status = "invalidEmail";
            }

            JSOB.put("groupname", groupname);
        }
        else if (type.equalsIgnoreCase("getlist")) {
            
            List<Group> ownedGroup = new LinkedList<Group>();
            List<Group> memberGroup = new LinkedList<Group>();

            try {
                ownedGroup = groupDB.getOwnedGroup(email);
                
                JSOB.put("totalowned", ownedGroup.size());
                JSONArray JSLS = new JSONArray();
                for(Group x : ownedGroup) {
                    JSONObject temp = new JSONObject();
                    temp.put("id", x.getID());
                    temp.put("name", x.getName());
                    JSLS.add(temp);
                }
                JSOB.put("owned", JSLS);
            } catch (NullPointerException e) {
                JSOB.put("totalowned", 0);
            }

            try {
                memberGroup = groupDB.getJoinedGroup(email);

                JSOB.put("totaljoined", memberGroup.size());
                JSONArray JSLS2 = new JSONArray();
                for(Group x : memberGroup) {
                    JSONObject temp = new JSONObject();
                    temp.put("id", x.getID());
                    temp.put("name", x.getName());
                    JSLS2.add(temp);
                }
                JSOB.put("joined", JSLS2);
            } catch (NullPointerException e) {
                JSOB.put("totaljoined", 0);
            }
        }
        else if (type.equalsIgnoreCase("ismember")) {
            status = "false";
            String groupid = request.getParameter("groupid");


            if(groupid != null) {
                Group a = groupDB.getGroup(groupid);
                if(a != null) {
                    if(a.getOwner().equalsIgnoreCase(email) || a.isMember(email)) {
                        status = "true";
                    }
                }
            }
        }
        else if (type.equalsIgnoreCase("invite")) {
            status = "false";
            String groupid = request.getParameter("groupid");
            String newmember = request.getParameter("newmember");

            String url = "http://localhost:8082/proj2/GroupProcesses?";
            String gid = "gid=";
            String pendingEmail = "&e=";
            String ownerEmail = "&o=";

            if(groupid != null) {
                Group toInvite = groupDB.getGroup(groupid);
                if(toInvite.isMember(newmember)){
                    status = "isMember";
                }
                else if(toInvite.getOwner().equalsIgnoreCase(newmember)){
                    status = "isOwner";
                }
                else {
                    gid += toInvite.getID();
                    pendingEmail += newmember;
                    ownerEmail += email;

                    if(!toInvite.isPendingMember(newmember)) {
                        groupDB.addPendingMember(groupid, email, newmember);
                        Group x = null;
                        int i = 0;
                        while (!groupList.get(i).getID().equals(groupid)) {
                            i++;
                        }
                        groupList.get(i).addPendingMember(newmember);
                    }
                    
                    // Send Group Invitation Email
                    String subject = MessageCreator.getGroupInvitationSubjectTitle();
                    // Use _HTML for SendMail, Use _TEXT for SendInternalMail
                    //String message = MessageCreator.createGroupInvitationMessage_TEXT(groupid, newmember, email);
                    String message = MessageCreator.createGroupInvitationMessage_HTML(groupid, newmember, email);

                    // Send Internal Mail (SendMail working in VM via my account)
                    //if (new SentInternalMail().sendEmail(subject, message, newmember)) {
                    if ((new SendEmail().sendEmail(subject, message, newmember))) {
                        status = "success";
                    }
                    else {
                        status = "sendMailFail";
                    }
                }
            }
        }
        else if (type.equalsIgnoreCase("getGroupInfo")) {
            status = "fail";
            String groupid = request.getParameter("groupid");
            Group x = groupDB.getGroup(groupid);
            
            if(x != null) {
                JSOB.put("groupname", x.getName());
                JSOB.put("owned", x.getOwner());

                JSOB.put("totalmember", x.getMembers().size());
                JSONArray JSLS = new JSONArray();
                for(String s : x.getMembers()) {
                    JSONObject temp = new JSONObject();
                    temp.put("name", s);
                    JSLS.add(temp);
                }
                JSOB.put("member", JSLS);

                if(x.getOwner().equalsIgnoreCase(email))
                    isowner = true;

                status = "success";
            }
        }
        else if (type.equalsIgnoreCase("quitGroup")) {
            status = "fail";
            String groupid = request.getParameter("groupid");
            Group x = groupDB.getGroup(groupid);

            if(x != null) {
                if(groupDB.removeMember(groupid, email)) {

                    int i = 0;
                    while (!groupList.get(i).getID().equals(groupid)) {
                        i++;
                    }
                    groupList.get(i).removeMember(email);
                    status = "success";
                }


                for(Group a : groupList) {
                    if(a.getName().equals(x.getName())) {
                        if(groupDB.removeMember(groupid, email)) {
                            a.removeMember(email);
                            status = "success";
                        }
                    }
                }
            }

        }
        else if (type.equalsIgnoreCase("removeMember")) {
            boolean success = true;
            String groupid = request.getParameter("groupid");
            String toremove = request.getParameter("toremove");
            
            Group x = null;
            int i = 0;
            while (!groupList.get(i).getID().equals(groupid)) {
                i++;
            }

            String[] splited = toremove.split("<_>");
            
            for(String a: splited) {
                success = success && groupDB.removeMember(groupid, a.toLowerCase());
                success = success && groupList.get(i).removeMember(a.toLowerCase());
            }


            if(success)
                status = "success";
            else
                status = "fail";

        }
        else if (type.equalsIgnoreCase("deleteGroup")) {
            status = "fail";
            String groupid = request.getParameter("groupid");
            Group x = groupDB.getGroup(groupid);

            if(x != null && x.getOwner().equalsIgnoreCase(email)) {
                if(groupDB.delete(groupid)) {
                    groupList.remove(x);
                    status = "success";
                }
            }
        }


        JSOB.put("status", status);
        JSOB.put("email", email);
        //JSOB.put("groupname", groupname);
        JSOB.put("isowner", isowner);

        // Respond to client
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            out.println(JSOB);
        } finally {
            out.close();
        }
    }

    public static boolean deleteFromRelatedGroup(String email) {
        boolean success = true;

        List<Group> ownedGroup = groupDB.getOwnedGroup(email);
        List<Group> joinedGroup = groupDB.getJoinedGroup(email);

        if(ownedGroup != null && ownedGroup.size() > 0) {
            for(Group x: ownedGroup) {
                success = success && groupDB.delete(x.getID());
                success = success && groupList.remove(x);
            }
        }
        if(joinedGroup != null && joinedGroup.size() > 0) {
            for(Group x: joinedGroup) {
                success = success && groupDB.removeMember(x.getID(), email);
                Group tmp = x;
                tmp.removeMember(email);
                success = success && groupList.remove(x);
                success = success && groupList.add(tmp);
            }
        }

        return success;
    }

}
