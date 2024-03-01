package com.application.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface ExtendedRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {


    public List<T> findByAttributeContainsText(@RequestParam String attributeName, @RequestParam String text);


}
