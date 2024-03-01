package com.application.media.controller;

import com.application.media.model.Location;
import com.application.media.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "${ALLOWED_ORIGINS}")
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    // Create a new location
    @PostMapping("/create")
    public Location createLocation(@RequestBody Location location) {
        return locationRepository.save(location);
    }

    // Read all locations
    @GetMapping("/getAll")
    public List<Location> getAllLocations() {
        System.out.println("Pulled all locations");
        return locationRepository.findAll();
    }

    // Read a specific location by name
    @GetMapping("/{locationName}")
    public Optional<Location> getLocationByName(@PathVariable String locationName) {
        return locationRepository.findById(locationName);
    }

    // Update a location by name
    @PutMapping("/update/{locationName}")
    public Location updateLocation(@PathVariable String locationName, @RequestBody Location updatedLocation) {
        Optional<Location> existingLocation = locationRepository.findById(locationName);
        if (existingLocation.isPresent()) {
            Location location = existingLocation.get();
            location.setName(updatedLocation.getName());
            return locationRepository.save(location);
        } else {
            return null;
        }
    }

    // Delete a location by name
    @DeleteMapping("/delete/{locationName}")
    public void deleteLocation(@PathVariable String locationName) {
        locationRepository.deleteById(locationName);
    }
}
