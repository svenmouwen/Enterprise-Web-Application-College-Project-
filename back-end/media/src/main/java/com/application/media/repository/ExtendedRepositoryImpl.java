package com.application.media.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

public class ExtendedRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ExtendedRepository<T, ID> {

    private final EntityManager entityManager;

    @Autowired
    public ExtendedRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    public List<T> findByAttributeContainsText(@RequestParam String attributeName, @RequestParam String text) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(getDomainClass());
        Root<T> root = criteriaQuery.from(getDomainClass());
        criteriaQuery.select(root).where(builder.like(root.get(attributeName).as(String.class), "%" + text + "%"));
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }



}
