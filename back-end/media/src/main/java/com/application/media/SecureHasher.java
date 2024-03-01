// package
package com.application.media;

// Imports

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Public SecureHasher with some properties
 *
 * @author MilanBouwmeester
 */
public class SecureHasher {
    private static final MessageDigest saltedMessageDigest = getMessageDigest("SHA-512");
    private static final String secretSalt = "HvA-HBO-ICT-2022";

    /**
     * @param algorithm - Algorithm
     * @return - Returns a message of the algorithm
     */
    private static MessageDigest getMessageDigest(String algorithm) {
        //System.out.println("GetMD-" + algorithm);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
            // apply a secret salt to the hasher
            md.update(String.format("%-16s", secretSalt).getBytes(StandardCharsets.UTF_8));
            // for more security, add more salt, maybe different salt...

            // reset the md before its first use
            md.reset();
            return md;
        } catch (Exception ex) {
            // try to fallback on SHA-256
            if (!algorithm.equals("SHA-256")) return getMessageDigest("SHA-256");
        }
        // nothing found
        return null;
    }

    /**
     * Calculate a secure hash from a source for the purpose of password hashing.
     *
     * @param source - The source of the password
     * @return - Returns the result of this method
     */
    public static String secureHash(String source) {
        if (saltedMessageDigest == null || source == null) return null;
        // hash the source using the salted SHA hasher
        byte[] hashedResult = saltedMessageDigest.digest(source.getBytes(StandardCharsets.UTF_8));
        // for more security, waste some time, maybe sleep a bit...
        // convert the hashed result to a string
        return Base64.getEncoder().encodeToString(hashedResult);
    }
}