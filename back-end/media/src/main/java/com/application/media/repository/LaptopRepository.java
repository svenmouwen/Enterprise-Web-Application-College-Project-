package com.application.media.repository;

import com.application.media.model.Laptop;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface LaptopRepository extends ExtendedRepository<Laptop, BigInteger> {

    // Search methods
    @Query("SELECT l FROM Laptop l WHERE " +
            "CAST(l.ean AS string) LIKE %:search% OR " +
            "CAST(l.articleNr AS string) LIKE %:search% OR " +
            "l.model LIKE %:search% OR " +
            "l.brand LIKE %:search%")
    List<Laptop> searchByAny(String search);

//    @Query("SELECT l FROM Laptop l JOIN l.images img ORDER BY img.creationDate DESC")
//    Page<Laptop> findAllSortedByImageDate(Pageable pageable);
}
