package com.application.media.controller;

import com.application.media.model.Laptop;
import com.application.media.repository.LaptopRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.*;

@CrossOrigin(origins = "${ALLOWED_ORIGINS}")
@RestController
@RequestMapping("/api/laptops")
public class LaptopController {

    private final LaptopRepository laptopRepository;

    @Autowired
    public LaptopController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    /**
     * Retrieves a collection of all laptops.
     *
     * @return A collection of Laptop objects.
     */
    @GetMapping("/all")
    public Collection<Laptop> getAllLaptops() {
        return laptopRepository.findAll();
    }

    /**
     * Retrieves a laptop by its EAN (European Article Number).
     *
     * @param ean The EAN of the laptop to retrieve.
     * @return The Laptop object with the specified EAN.
     * @throws ResponseStatusException If the laptop with the given EAN is not found.
     */
    @GetMapping("/{ean}")
    public Laptop getLaptopByEan(@PathVariable BigInteger ean) {
        return laptopRepository.findById(ean)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes a laptop by its EAN (European Article Number).
     *
     * @param ean The EAN of the laptop to delete.
     * @throws ResponseStatusException If the laptop with the given EAN is not found.
     */
    @DeleteMapping("/{ean}")
    public void deleteLaptop(@PathVariable BigInteger ean) {
        if (!laptopRepository.existsById(ean)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        laptopRepository.deleteById(ean);
    }

    /**
     * Updates an existing laptop with the specified EAN.
     *
     * @param ean           The EAN of the laptop to update.
     * @param laptopDetails The updated Laptop object.
     * @return The updated Laptop object.
     * @throws ResponseStatusException If the laptop with the given EAN is not found.
     */
    @PutMapping(value = "/{ean}", consumes = "application/json")
    public ResponseEntity<Laptop> updateLaptop(@PathVariable BigInteger ean, @RequestBody @Valid Laptop laptopDetails) {
        Laptop laptop = laptopRepository.findById(ean)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Laptop not found with EAN: " + ean));

        // Updated properties of the laptop
        laptop.setArticleNr(laptopDetails.getArticleNr());
        laptop.setBrand(laptopDetails.getBrand());
        laptop.setModel(laptopDetails.getModel());

        // Save the updated laptop details
        Laptop updatedLaptop = laptopRepository.save(laptop);

        // Return the updated laptop
        return ResponseEntity.ok(updatedLaptop);
    }

    /**
     * Creates a new laptop.
     *
     * @param laptop The Laptop object to create.
     * @return The created Laptop object.
     */
    @PostMapping("/new")
    public Laptop createLaptop(@RequestBody @Valid Laptop laptop) {
        return laptopRepository.save(laptop);
    }

    /**
     * Loads a batch of laptops with pagination.
     *
     * @param offset The starting offset for loading laptops.
     * @param limit  The maximum number of laptops to load.
     * @return A ResponseEntity containing a list of loaded laptops or a no-content response if no laptops are found.
     */
    @GetMapping("/load")
    public ResponseEntity<List<Laptop>> loadLaptops(@RequestParam int offset, @RequestParam int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Laptop> page = laptopRepository.findAll(pageable);
        if (page.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * Searches for laptops based on a search query.
     *
     * @param search The search query.
     * @return A list of Laptop objects matching the search criteria.
     */
    @GetMapping("/search/{search}")
    public List<Laptop> searchLaptops(@PathVariable String search) {
        return laptopRepository.searchByAny(search);
    }
}
