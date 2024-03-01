package com.application.media.controller;

import com.application.media.model.Image;
import com.application.media.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "${ALLOWED_ORIGINS}")
@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Retrieves a list of all images.
     *
     * @return A list of Image objects.
     */
    @GetMapping("/all")
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @GetMapping("/getAll")
    public List<Image> getAllImage() {
        return imageRepository.findAll();
    }

    /**
     * Retrieves an image by its ID.
     *
     * @param id The ID of the image to retrieve.
     * @return The Image object with the specified ID.
     * @throws ResponseStatusException If the image with the given ID is not found.
     */
    @GetMapping("/{id}")
    public Image getImageById(@PathVariable int id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
    }

    /**
     * Creates a new image.
     *
     * @param image The Image object to create.
     * @return The created Image object.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Image createImage(@RequestBody Image image) {
        return imageRepository.save(image);
    }

    /**
     * Updates an existing image with the specified ID.
     *
     * @param id    The ID of the image to update.
     * @param image The updated Image object.
     * @return The updated Image object.
     * @throws ResponseStatusException If the image with the given ID is not found.
     */
    @PutMapping("/{id}")
    public Image updateImage(@PathVariable int id, @RequestBody Image image) {
        return imageRepository.findById(id).map(existingImage -> {
            existingImage.setDatetime(image.getDatetime());
            existingImage.setLaptop(image.getLaptop());
            existingImage.setStatus(image.getStatus());
            existingImage.setNotes(image.getNotes());
            return imageRepository.save(existingImage);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
    }

    /**
     * Deletes an image by its ID.
     *
     * @param id The ID of the image to delete.
     * @throws ResponseStatusException If the image with the given ID is not found.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable int id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
        imageRepository.delete(image);
    }
}