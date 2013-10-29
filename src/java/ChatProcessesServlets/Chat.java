/****************************************************
* Student Name: Choong Teik Tan                     *
* Student Number: 568701                            *
* Student Email: choongt@student.unimelb.edu.au     *
* File: Chat.java <servlet> (SWEN90002 Project 2)   *
****************************************************/

package ChatProcessesServlets;

import Entity.BindingsChatDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Entity.ChatMessage;
import java.util.LinkedList;
import javax.servlet.ServletConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Chat extends HttpServlet {

    private static BindingsChatDB chatDB = null;
    private static List<ChatMessage> receivedMessage = new LinkedList<ChatMessage>();

    public static boolean delete(String groupid) {
        boolean success = true;
        success = success && chatDB.delete(groupid);

        List<ChatMessage> toDelete = new LinkedList<ChatMessage>();
        for(ChatMessage cm : receivedMessage) {
            if(cm.getGroupID().equals(groupid)) {
                toDelete.add(cm);
            }
        }

        for(ChatMessage tdm : toDelete) {
            success = success && receivedMessage.remove(tdm);
        }


        return success;
    }

     // This Happens Once and is Reused
    @Override
    public void init(ServletConfig config) throws ServletException
    {
               super.init(config);
                    
               try {
                   // create new database binding class and start load data.
                   chatDB = new BindingsChatDB();
                   receivedMessage = chatDB.load();
               }
               catch(Exception e) {
                    System.out.println(e.getMessage());
               }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        JSONObject JSOB = new JSONObject();

        String type = request.getParameter("type");
        String user = request.getParameter("user");
        String groupID = request.getParameter("groupid");
        
        String status = "success";

        if(groupID == null) {
            status = "NULL_GROUP_ID";
        }
        else if(type.equals("pull")) {
            String message = request.getParameter("message");
            int msghead = Integer.parseInt(request.getParameter("msghead"));
            String groupid = request.getParameter("groupid");

            // Increment counter and store new message to list when receive a message.
            if(!message.equals("")) {
                synchronized(this) {
                    // (set new message's index, user, message) time stamp will automatic created
                    ChatMessage newMessage = new ChatMessage(receivedMessage.size()+1, user, message, groupid);
                    // save new message to database.
                    chatDB.save(newMessage);
                    // save new message to local stored message list.
                    receivedMessage.add(newMessage);
                }
            }

            //System.out.println(groupID);
            
            List<ChatMessage> groupMessage = new LinkedList<ChatMessage>();
            for(ChatMessage x : receivedMessage) {
                if(x.getGroupID().equals(groupID))
                    groupMessage.add(x);
            }

            JSOB.put("totalmsg", groupMessage.size());//groupMessage.size()-msghead);
            JSONArray JSLS = new JSONArray();
            for(int i=msghead;i<groupMessage.size();i++) {
                JSONObject temp = new JSONObject();
                temp.put("user", groupMessage.get(i).getUser());
                temp.put("message", groupMessage.get(i).getMessage());
                temp.put("timestamp", groupMessage.get(i).getTimestamp().toString());
                JSLS.add(temp);
            }
            JSOB.put("newmessage", JSLS);
        }

        JSOB.put("status", status);
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
