package ru.wiselder.plan.business.discipline;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wiselder.plan.model.Discipline;
import ru.wiselder.plan.request.CreateDisciplineRequest;
import ru.wiselder.plan.utils.SqlUtils;

import static ru.wiselder.plan.utils.SqlUtils.nameLike;

@Repository
@RequiredArgsConstructor
public class DisciplineDao {
    private final String SELECT_BASE = "SELECT * FROM DISCIPLINES";
    private final String SELECT_BY_NAME = SELECT_BASE + " WHERE NAME = :name";
    private final String SELECT_LIKE_NAME = SELECT_BASE + " WHERE NAME LIKE :name";
    private final String SELECT_BY_ID = SELECT_BASE + " WHERE DISCIPLINE_ID = :id";
    private final String INSERT_QUERY = "INSERT INTO DISCIPLINES (NAME) VALUES (:name)";
    private final String ORDER_BY_NAME = " ORDER BY NAME";

    private final RowMapper<Discipline> MAPPER = (rs, rowNum) -> new Discipline(
            rs.getInt("DISCIPLINE_ID"),
            rs.getString("NAME")
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<Discipline> findByName(String name) {
        return jdbcTemplate.query(SELECT_BY_NAME, Map.of("name", name), SqlUtils.singleExtractor(MAPPER));
    }

    public List<Discipline> findLikeName(String name) {
        return StringUtils.isBlank(name) ?
                jdbcTemplate.query(SELECT_BASE + ORDER_BY_NAME, MAPPER) :
                jdbcTemplate.query(SELECT_LIKE_NAME + ORDER_BY_NAME, nameLike(name), MAPPER);
    }


    public Optional<Discipline> findById(int id) {
        return jdbcTemplate.query(SELECT_BY_ID, Map.of("id", id), SqlUtils.singleExtractor(MAPPER));
    }

    public int create(CreateDisciplineRequest request) {
        return jdbcTemplate.update(INSERT_QUERY, Map.of("name", request.name()));
    }
}
