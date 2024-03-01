package com.application.media.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Table(name = "locations")
@Entity
public class Location implements Serializable {

    @Id
    @Column(name = "location", unique = true)
    private String name;

    public Location() {
    }

    public Location(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
