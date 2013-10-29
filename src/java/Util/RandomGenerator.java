/**********************************************************
* Student Name: Choong Teik Tan                           *
* Student Number: 568701                                  *
* Student Email: choongt@student.unimelb.edu.au           *
* File: RandomGenerator.java <Util> (SWEN90002 Project 2) *
**********************************************************/

package Util;

import java.util.Random;
import java.util.UUID;

public class RandomGenerator {

    public RandomGenerator () {}

    public static String generateSessionKey(int length){
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; 
        int n = alphabet.length(); 

        String result = new String();
        Random r = new Random(); 

        for (int i=0; i<length; i++) 
            result = result + alphabet.charAt(r.nextInt(n)); 

        return result;
    }

    public static String generateSessionKey1() {
        return UUID.randomUUID().toString();
    }

    public static String generateSessionKey2() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

}
