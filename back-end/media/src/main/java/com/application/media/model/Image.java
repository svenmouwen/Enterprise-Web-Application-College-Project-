package com.application.media.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Represents an Image entity in the database.
 */
@Table(name = "images")
@Entity
public class Image implements Serializable {

    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "laptop_ean", referencedColumnName = "ean")
    private Laptop laptop;

    @Column(name = "last_updated_on")
    private Date datetime;

    @ManyToOne
    @JoinColumn(name = "status_updated_by", referencedColumnName = "id")
    private User updated_by;

    @NotEmpty
    @Column(name = "status")
    private String status;

    @Column(name = "notes")
    private String notes;

    public Image() {

    }

    /**
     * Initializes a new Image instance with the specified attributes.
     *
     * @param laptop     The Laptop associated with the image.
     * @param updated_by The User who updated the image status.
     * @param status     The status of the image.
     * @param notes      Additional notes or comments about the image.
     */
    public Image(Laptop laptop, User updated_by, String status, String notes) {
        this.laptop = laptop;
        this.updated_by = updated_by;
        this.status = status;
        this.notes = notes;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(User updated_by) {
        this.updated_by = updated_by;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Compares this Image object with another object for equality.
     *
     * @param o The object to compare with this Image.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image image)) return false;
        return id == image.id && Objects.equals(laptop, image.laptop);
    }

    /**
     * Returns the hash code value for this Image object.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, laptop);
    }
}