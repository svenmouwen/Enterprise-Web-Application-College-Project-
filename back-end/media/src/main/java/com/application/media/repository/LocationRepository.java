package com.application.media.repository;

import com.application.media.model.Location;
import com.application.media.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, String> {
}