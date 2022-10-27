package com.andrdc.springpersistence.dataaccessobjects;

import com.andrdc.springpersistence.entities.Officer;

import java.util.List;
import java.util.Optional;

public interface OfficerDataAccessObject {
    Officer save(Officer officer);
    Optional<Officer> findById(Integer id);
    List<Officer> findAll();
    long count();
    void delete(Officer officer);
    boolean existsById(Integer id);
}