/**********************************************************
* Student Name: Choong Teik Tan                           *
* Student Number: 568701                                  *
* Student Email: choongt@student.unimelb.edu.au           *
* File: EmailValidator.java <Util> (SWEN90002 Project 2)  *
**********************************************************/

package Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }


    // Validate hex (email address) by regular expression
    public boolean isValidate(final String hex) {
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    // Validate by InternetAddress
    public boolean isValidEmailAddress(String email) {
        boolean result = true;

        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }

        return result;
    }
}
