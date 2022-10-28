package com.andrdc.springpersistence.dataaccessobjects;

import com.andrdc.springpersistence.entities.Officer;
import com.andrdc.springpersistence.entities.Rank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JdbcOfficerDataAccessObjectTest {
    @Autowired
    private JdbcOfficerDataAccessObject dataAccessObject;

    @Test
    public void save() {
        Officer officer = new Officer(Rank.ENSIGN, "Wesley", "Crusher");
        officer = dataAccessObject.save(officer);
        assertNotNull(officer.getId());
    }

    @Test
    public void findByIdThatExists() {
        Optional<Officer> officer = dataAccessObject.findById(1);
        assertTrue(officer.isPresent());
        assertEquals(1, officer.get().getId().intValue());
    }

    @Test
    public void findByIdThatDoesNotExist() {
        Optional<Officer> officer = dataAccessObject.findById(999);
        assertFalse(officer.isPresent());
    }

    @Test
    public void count() { assertEquals(5, dataAccessObject.count()); }

    @Test
    public void findAll() {
        List<String> dataBaseNames = dataAccessObject.findAll().stream().map(Officer::getLastName).collect(Collectors.toList());
        assertThat(dataBaseNames, containsInAnyOrder("Archer", "Janeway", "Kirk", "Picard", "Sisko"));
    }

    @Test
    public void delete() {
        IntStream.rangeClosed(1, 5).forEach(id -> {
            Optional<Officer> officer = dataAccessObject.findById(id);
            assertTrue(officer.isPresent());
            dataAccessObject.delete(officer.get());
        });
        assertEquals(0, dataAccessObject.count());
    }

    @Test
    public void existsById() {
        IntStream.rangeClosed(1, 5).forEach(id -> { assertTrue(dataAccessObject.existsById(id)); });
    }
}
