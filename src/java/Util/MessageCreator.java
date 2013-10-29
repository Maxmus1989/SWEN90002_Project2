/**********************************************************
* Student Name: Choong Teik Tan                           *
* Student Number: 568701                                  *
* Student Email: choongt@student.unimelb.edu.au           *
* File: MessageCreator.java <Util> (SWEN90002 Project 2)  *
**********************************************************/

package Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageCreator {

    final static private String confirmationSubjectTitle = "SWEN90002-03 Project 2 (Registration Confirmation)";
    final static private String confirmation_msg_first = "Hi There!<br><br>"
            + "Somebody, probably you, has registered your email address.<br>"
            + "Click the following link to confirm registration:<br><br>";
    final  static private String confirmation_msg_last ="<br><br>"
                    + "Best!<br>"
                    + "Choong Teik Tan";

    final static private String confirmation_msg_first_text = "Hi There!\n\n"
            + "Somebody, probably you, has registered your email address.\n"
            + "Click the following link to confirm registration:\n\n";
    final  static private String confirmation_msg_last_text ="\n\n"
                    + "Best!\n"
                    + "Choong Teik Tan";

    final static private String successRegistrationSubjectTitle = "SWEN90002-03 Project 2 (Success Registration)";
    final static private String successReg_msg_first = "Hi There!<br><br>Your login ID for proj2 account is: ";
    final static private String successReg_msg_second = "<br>The password for your proj2 account is: ";
    final static private String successReg_msg_last = "<br>Keep it secret, keep it safe.<br><br>"
                    //+ "http://swen90002-03.cis.unimelb.edu.au:8080/proj2/<br><br>"
                    + "http://192.168.2.6:8082/proj2/<br><br>"
                    + "Best!<br>"
                    + "Choong Teik Tan";

    final static private String successReg_msg_first_text = "Hi There!\n\nYour login ID for proj1 account is: ";
    final static private String successReg_msg_second_text = "\nThe password for your proj1 account is: ";
    final static private String successReg_msg_last_text = "\nKeep it secret, keep it safe.\n\n"
                    //+ "http://swen90002-03.cis.unimelb.edu.au:8080/proj2/\n\n"
                    + "http://192.168.2.6:8082/proj2/\n\n"
                    + "Best!\n"
                    + "Choong Teik Tan";

    final static private String reminderSubjectTitle = "SWEN90002-03 Project 2 (Password Reminder)";
    final static private String resetPasswordSubjectTitle = "SWEN90002-03 Project 2 (New Password)";

    final static private String groupInvitationSubjectTitle = "SWEN90002-03 Project 2 (Group Invitation)";
    final static private String groupInvitation_msg_first = "Hi There!<br><br>"
            + "Your friend has invite your to join his group chat.<br>"
            + "Click the following link to join group:<br><br>";
    final  static private String groupInvitation_msg_last ="<br><br>"
                    + "Best!<br>"
                    + "Choong Teik Tan";

    final static private String groupInvitation_msg_first_text = "Hi There!\n\n"
            + "Your friend has invite your to join his group chat.\n"
            + "Click the following link to join group:\n\n";
    final  static private String groupInvitation_msg_last_text ="\n\n"
                    + "Best!\n"
                    + "Choong Teik Tan";

    public MessageCreator() {}

    // Get title for Confirmation Email
    public static String getConfirmationSubjectTitle() {
        return confirmationSubjectTitle;
    }

    // Create conformation message (HTML)
    public static String createConfirmationMessage_HTML(String token, String email) {

        String URL = "http://192.168.2.6:8082/proj2/confirm?token="+token+"&email=";
        //String URL = "http://swen90002-03.cis.unimelb.edu.au:8082/proj2/confirm?token="+token+"&email=";
        
        try {
            URL += URLEncoder.encode(email, "ISO-8859-1"); // Or "UTF-8".
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MessageCreator.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

        return (confirmation_msg_first + URL + confirmation_msg_last);
    }

    // Create conformation message (TEXT)
    public static String createConfirmationMessage_TEXT(String token, String email) {

        String URL = "http://192.168.2.6:8082/proj2/confirm?token="+token+"&email=";
        //String URL = "http://swen90002-03.cis.unimelb.edu.au:8080/proj2/confirm?token="+token+"&email=";

        try {
            URL += URLEncoder.encode(email, "ISO-8859-1"); // Or "UTF-8".
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MessageCreator.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

        return (confirmation_msg_first_text + URL + confirmation_msg_last_text);
    }

    // Create Group Invitation message (HTML)
    public static String createGroupInvitationMessage_HTML(String groupid, String email, String owner) {

        String URL = "http://192.168.2.6:8082/proj2/GroupProcesses?gid="+groupid+"&e="+email+"&o="+owner;
        //String URL = "http://swen90002-03.cis.unimelb.edu.au:8082/GroupProcesses?gid="+groupid+"&e="+email+"&o="+owner;
        return (groupInvitation_msg_first + URL + groupInvitation_msg_last);
    }

    // Create Group Invitation message (TEXT)
    public static String createGroupInvitationMessage_TEXT(String groupid, String email, String owner) {

        String URL = "http://192.168.2.6:8082/proj2/GroupProcesses?gid="+groupid+"&e="+email+"&o="+owner;
        //String URL = "http://swen90002-03.cis.unimelb.edu.au:8082/GroupProcesses?gid="+groupid+"&e="+email+"&o="+owner;
        return (groupInvitation_msg_first_text + URL + groupInvitation_msg_last_text);
    }

    // Get title for Confirmation Email
    public static String getSuccessRegistrationSubjectTitle() {
        return successRegistrationSubjectTitle;
    }

    // Create conformation message (HTML)
    public static String createSuccessRegistrationMessage_HTML(String password, String email) {
        return (successReg_msg_first + email + successReg_msg_second + password + successReg_msg_last);
    }

    // Create conformation message (TEXT)
    public static String createSuccessRegistrationMessage_TEXT(String password, String email) {
        return (successReg_msg_first_text + email + successReg_msg_second_text + password + successReg_msg_last_text);
    }

    // Get title for Password Reminder Email
    public static String getReminderSubjectTitle() {
        return reminderSubjectTitle;
    }

    // Get title for Password Reminder Email
    public static String getResetPasswordSubjectTitle() {
        return resetPasswordSubjectTitle;
    }

    // Get title for Group Invitation Email
    public static String getGroupInvitationSubjectTitle() {
        return groupInvitationSubjectTitle;
    }
}
