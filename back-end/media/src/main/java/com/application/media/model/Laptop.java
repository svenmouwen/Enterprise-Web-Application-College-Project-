package com.application.media.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import org.springframework.context.annotation.Primary;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Represents a Laptop entity in the database.
 */
@Entity
@Primary
@Table(name = "laptops")
public class Laptop implements Serializable {

    @Id
    @NotNull
    @Range
    @Column(name = "ean", unique = true)
    private BigInteger ean;

    @NotNull
    @Range
    @Column(name = "articleNr")
    private BigInteger articleNr;

    @NotEmpty
    @Column(name = "brand")
    private String brand;

    @NotEmpty
    @Column(name = "model")
    private String model;

    public Laptop() {
    }



    /**
     * Initializes a new Laptop instance with the specified attributes.
     *
     * @param ean       The EAN (European Article Number) of the laptop.
     * @param articleNr The article number of the laptop.
     * @param brand     The brand of the laptop.
     * @param model     The model of the laptop.
     */
    public Laptop(BigInteger ean, BigInteger articleNr, String brand, String model) {
        this.ean = ean;
        this.articleNr = articleNr;
        this.brand = brand;
        this.model = model;
    }

    // Getters and Setters for ean, articleNr, brand, model

    public BigInteger getEan() {
        return ean;
    }

    public void setEan(BigInteger ean) {
        this.ean = ean;
    }

    public BigInteger getArticleNr() {
        return articleNr;
    }

    public void setArticleNr(BigInteger articleNr) {
        this.articleNr = articleNr;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Returns a string representation of the Laptop object.
     *
     * @return A formatted string containing the laptop's attributes.
     */
    @Override
    public String toString() {
        return String.format(
                "Laptop[ean=%d, articleNr='%d', brand='%s', model='%s']",
                ean, articleNr, brand, model
        );
    }
}