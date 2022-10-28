package com.andrdc.springpersistence.dataaccessobjects;

import com.andrdc.springpersistence.entities.Officer;
import com.andrdc.springpersistence.entities.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("ConstantConditions")
@Repository
public class JdbcOfficerDataAccessObject implements OfficerDataAccessObject {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertOfficer;
    private final RowMapper<Officer> officerMapper =
            (resultSet, row) ->  new Officer(resultSet.getInt("id"),
            Rank.valueOf(resultSet.getString("rank")),
            resultSet.getString("first_name"),
            resultSet.getString("last_name"));

    @Autowired
    public JdbcOfficerDataAccessObject(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertOfficer = new SimpleJdbcInsert(jdbcTemplate).withTableName("officers").usingGeneratedKeyColumns("id");
    }

    @Override
    public Officer save(Officer officer) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("rank", officer.getRank());
        parameters.put("first_name", officer.getFirstName());
        parameters.put("last_name", officer.getLastName());
        Integer newId = (Integer) insertOfficer.executeAndReturnKey(parameters);
        officer.setId(newId);
        return officer;
    }

    @Override
    public Optional<Officer> findById(Integer id) {
        if (!existsById(id)) return Optional.empty();
        return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM officers WHERE id=?", officerMapper, id));
    }

    @Override
    public List<Officer> findAll() { return jdbcTemplate.query("SELECT * FROM officers", officerMapper); }

    @Override
    public long count() { return jdbcTemplate.queryForObject("select count(*) from officers", Long.class); }

    @Override
    public void delete(Officer officer) { jdbcTemplate.update("DELETE FROM officers WHERE id=?", officer.getId()); }

    @Override
    public boolean existsById(Integer id) { return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM officers where id=?)", Boolean.class, id); }
}