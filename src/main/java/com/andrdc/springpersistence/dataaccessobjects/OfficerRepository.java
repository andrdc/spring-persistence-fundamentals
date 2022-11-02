package com.andrdc.springpersistence.dataaccessobjects;

import com.andrdc.springpersistence.entities.Officer;
import com.andrdc.springpersistence.entities.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficerRepository extends JpaRepository<Officer, Integer> {
    List<Officer> findAllByRankAndLastNameContaining(Rank rank,String string);
}