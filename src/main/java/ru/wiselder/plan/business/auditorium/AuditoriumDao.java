package ru.wiselder.plan.business.auditorium;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wiselder.plan.model.Auditorium;
import ru.wiselder.plan.request.CreateAuditoriumRequest;
import ru.wiselder.plan.utils.SqlUtils;

import static ru.wiselder.plan.utils.SqlUtils.nameLike;

@Repository
@RequiredArgsConstructor
public class AuditoriumDao {
    private final String BASE_SELECT = "SELECT * FROM AUDITORIUMS";
    private final String ORDER_BY_NAME_ADDRESS = " ORDER BY NAME, ADDRESS";
    private final String SELECT_LIKE_NAME = BASE_SELECT + " WHERE NAME LIKE :name";
    private final String SELECT_BY_ID = BASE_SELECT + " WHERE AUDITORIUM_ID = :id";
    private final String SELECT_BY_NAME_ADDRESS = BASE_SELECT + " WHERE NAME = :name AND ADDRESS = :address";
    private final String INSERT_QUERY = "INSERT INTO AUDITORIUMS (NAME, ADDRESS) VALUES (:name, :address)";
    private final RowMapper<Auditorium> MAPPER = (rs, rowNum) -> new Auditorium(
            rs.getInt("AUDITORIUM_ID"),
            rs.getString("ADDRESS"),
            rs.getString("NAME")
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Auditorium> findLikeName(String name) {
        return StringUtils.isBlank(name) ?
                jdbcTemplate.query(BASE_SELECT + ORDER_BY_NAME_ADDRESS, MAPPER) :
                jdbcTemplate.query(SELECT_LIKE_NAME + ORDER_BY_NAME_ADDRESS, nameLike(name), MAPPER);
    }

    public Optional<Auditorium> findById(int id) {
        return jdbcTemplate.query(SELECT_BY_ID, Map.of("id", id), SqlUtils.singleExtractor(MAPPER));
    }

    public int create(CreateAuditoriumRequest request) {
        return jdbcTemplate.update(INSERT_QUERY, toMap(request));
    }

    public Optional<Auditorium> find(CreateAuditoriumRequest request) {
        return jdbcTemplate.query(SELECT_BY_NAME_ADDRESS, toMap(request), SqlUtils.singleExtractor(MAPPER));
    }

    private Map<String, Object> toMap(CreateAuditoriumRequest request) {
        return Map.of("name", request.name(), "address", request.address());
    }
}
