package com.dtna.vehicle.repository;

import com.dtna.vehicle.domain.Vehicle;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class VehicleRepository implements CommonRepository<Vehicle> {

    private static final String SQL_INSERT = "INSERT INTO vehicle" +
            "(id, vin, created, modified, completed) " +
            "VALUES (:id, :vin, :created, :modified, :completed)";

    private static final String SQL_QUERY_FIND_ALL = "SELECT " +
            "id, vin, created, modified, completed " +
            "FROM vehicle";

    private static final String SQL_QUERY_FIND_BY_ID = SQL_QUERY_FIND_ALL + " WHERE id = :id";

    private static final String SQL_UPDATE = "UPDATE vehicle SET " +
            "vin = :vin," +
            "created = :created," +
            "modified = :modified," +
            "completed = :completed " +
            "WHERE id = :id";

    private static final String SQL_DELETE = "DELETE FROM vehicle WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public VehicleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Vehicle> vehicleRowMapper = (ResultSet rs, int rowNumber) -> {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getString("id"));
        vehicle.setVin(rs.getString("vin"));
        vehicle.setCreated(rs.getTimestamp("created").toLocalDateTime());
        vehicle.setModified(rs.getTimestamp("modified").toLocalDateTime());
        vehicle.setCompleted(rs.getBoolean("completed"));
        return vehicle;
    };

    @Override
    public Vehicle save(Vehicle domain) {
        Vehicle result = findById(domain.getId());
        if (null != result) {
            result.setModified(LocalDateTime.now());
            result.setVin(domain.getVin());
            result.setCompleted(domain.isCompleted());
            return upsert(domain, SQL_UPDATE);
        }
        return upsert(domain, SQL_INSERT);
    }

    private Vehicle upsert(final Vehicle vehicle, final String sql) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", vehicle.getId());
        namedParameters.put("vin", vehicle.getVin());
        namedParameters.put("created", vehicle.getCreated());
        namedParameters.put("modified", vehicle.getModified());
        namedParameters.put("completed", vehicle.isCompleted());
        this.jdbcTemplate.update(sql, namedParameters);
        return findById(vehicle.getId());
    }

    @Override
    public Iterable<Vehicle> save(Collection<Vehicle> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(Vehicle domain) {
        Map<String, String> namedParameters = Collections.singletonMap("id", domain.getId());
        this.jdbcTemplate.update(SQL_DELETE, namedParameters);
    }

    @Override
    public Vehicle findById(String id) {
        try {
            Map<String, String> namedParameters = Collections.singletonMap("id", id);
            return this.jdbcTemplate.queryForObject(SQL_QUERY_FIND_BY_ID, namedParameters, vehicleRowMapper);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public Iterable<Vehicle> findAll() {
        return this.jdbcTemplate.query(SQL_QUERY_FIND_ALL, vehicleRowMapper);
    }
}
