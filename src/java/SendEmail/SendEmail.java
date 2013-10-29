/****************************************************
* Student Name: Choong Teik Tan                     *
* Student Number: 568701                            *
* Student Email: choongt@student.unimelb.edu.au     *
* File: SendEmail.java (SWEN90002 Project 2)        *
****************************************************/

package SendEmail;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {
    
    private Properties properties = null;
    final private String user = "SWEN90002-03 Project 2";
    final private String host = "smtp.gmail.com";
    final private String port = "587";
    final private String userName = "choongt.unimelb@gmail.com";
    final private String password = "swen90002";

    public SendEmail() {
        // Sets SMTP server properties
        properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
    }

    public Boolean sendEmail(String subject, String message, String toAddress) {
        
        // Creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // Creates a new e-mail message
        Message msg = new MimeMessage(session);
        
        try {
            // Re-validate
            InternetAddress emailAddr = new InternetAddress(toAddress);
            emailAddr.validate();

            msg.setFrom(new InternetAddress(userName,user));
            InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
            
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // Creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/html");

            // Creates multi-part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Sets the multi-part as e-mail's content
            msg.setContent(multipart);

            // Sends the e-mail
            Transport.send(msg);
        } catch (Exception ex) {
            System.out.println("Error: (Unable to reach: "+toAddress+")\n"+ex);
            return false;
        }
        System.out.println("Sent! "+toAddress+" :)");
        return true;
    }
}
