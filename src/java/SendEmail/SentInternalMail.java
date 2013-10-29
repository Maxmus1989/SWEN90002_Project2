/******************************************************
* Student Name: Choong Teik Tan                       *
* Student Number: 568701                              *
* Student Email: choongt@student.unimelb.edu.au       *
* File: SendInternalEmail.java (SWEN90002 Project 2)  *
******************************************************/

package SendEmail;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SentInternalMail {

    public SentInternalMail() {}

    public Boolean sendEmail(String subject, String message, String toAddress) {

        String from = "choongt@student.unimelb.edu.au";//"SWEN90002-03 Lab5";
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

    try{
        // Create a default MimeMessage object.
        MimeMessage mimeMessage = new MimeMessage(session);

        // Set From: header field of the header.
        mimeMessage.setFrom(new InternetAddress(from, "SWEN90002-03 Project 2"));

        // Set To: header field of the header.
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

        // Set Subject: header field
        mimeMessage.setSubject(subject);

        // Now set the actual message
        mimeMessage.setText(message);

        // Send message
        Transport.send(mimeMessage);
         
      } catch (Exception mex) {
        System.out.println("Sent message Failed....");
        return false;
      }

      System.out.println("Sent message successfully....");
      return true;
    }
}
