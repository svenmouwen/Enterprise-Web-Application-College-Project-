// Package
package com.application.media.security;

// Imports

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Public class of JWToken with some properties
 *
 * @author MilanBouwmeester
 */
public class JWToken {
    private static final String JWT_ISSUER_CLAIM = "iss";
    private static final String JWT_CALLNAME_CLAIM = "sub";
    private static final String JWT_ACCOUNTID_CLAIM = "id";
    private final String callName;
    private final Long accountID;

    /**
     * @param callName  - CallName
     * @param accountID - ID of the user account
     */
    public JWToken(String callName, Long accountID) {
        this.callName = callName;
        this.accountID = accountID;
    }

    /**
     * Generates a JWT token with the specified issuer, secret key, and expiration time.
     *
     * @param issuer     The issuer of the token.
     * @param passphrase The secret key used for signing the token.
     * @param expiration The expiration time of the token in seconds.
     * @return The generated JWT token.
     */
    public String generateToken(String issuer, String passphrase, int expiration) {
        Key key = getKey(passphrase);

        return Jwts.builder().claim(JWT_CALLNAME_CLAIM, this.callName).claim(JWT_ACCOUNTID_CLAIM, this.accountID).setIssuer(issuer).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L)).signWith(key, SignatureAlgorithm.HS512).compact();
    }

    /**
     * @param passphrase - A passphrase of a key
     * @return - Returns a new secretKeySpec
     */
    private Key getKey(String passphrase) {
        byte[] hmacKey = passphrase.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(hmacKey, SignatureAlgorithm.HS512.getJcaName());
    }
    
    /**
     * @return - Returns an encode for a token
     */
    public String encode(String issuer, String passphrase, int expiration) {
        Key key = getKey(passphrase);

        return Jwts.builder().claim(JWT_CALLNAME_CLAIM, this.callName).claim(JWT_ACCOUNTID_CLAIM, this.accountID).setIssuer(issuer).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L)).signWith(key, SignatureAlgorithm.HS512).compact();
    }

    public String getJwtIssuerClaim() {
        return JWT_ISSUER_CLAIM;
    }
}
