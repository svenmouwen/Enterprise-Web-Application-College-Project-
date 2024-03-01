// Package
package com.application.media.model;

// Imports

import com.application.media.SecureHasher;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Objects;


/**
 * User model with some properties
 *
 * @author MilanBouwester & TeunHilbers
 */
@Table(name = "users")
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Use GenerationType.IDENTITY for automatic generation
    @Column(name = "id", unique = true)
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;


    @Column(name = "location")
    private String location;

    @Column(name = "password")
    private String password;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * @param id       - ID of the user
     * @param email    - Email of the user
     * @param name     - Name of the user
     * @param location - Location of the workshop of the user
     * @param password - Password of the user
     */
    public User(long id, String email, String name, String location, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.location = location;
        this.password = password;
    }

    /**
     * @param password - Password of the user
     * @return - Returns a hashed password
     */
    public String hashPassword(String password) {
        return SecureHasher.secureHash("Id-" + this.getId() + ":" + password);
    }

    /**
     * @return - Returns a method with some information about the user
     */
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email='" + email + '\'' + ", name='" + name + '\'' + ", location='" + location + '\'' + ", password='" + password + '\'' + '}';
    }

    /**
     * @return - Get the ID of the user
     */
    public long getId() {
        return id;
    }

    /**
     * @param id - Change the ID of the user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return - Get the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email - Change the email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return - Get the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * @param name - Change the name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return - Get the workshop location of the user
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location - Change the workshop location of the user
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return - Get the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password - Change the password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param userPassword - Password of the user
     * @return - Returns the verified password of the user
     */
    public boolean verifyPassword(String userPassword) {
        return (Objects.equals(this.password, userPassword));
    }
}