package com.application.media.repository;

import com.application.media.model.Image;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends ExtendedRepository<Image, Integer> {
}