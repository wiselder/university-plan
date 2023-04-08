package ru.wiselder.plan.business.teacher;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wiselder.plan.model.Teacher;
import ru.wiselder.plan.request.CreateTeacherRequest;
import ru.wiselder.plan.utils.SqlUtils;

@Repository
@RequiredArgsConstructor
public class TeacherDao {
    private final String SELECT_BY_NAME = "SELECT * FROM TEACHERS WHERE FULL_NAME LIKE :name";
    private final String SELECT_BY_ID = "SELECT * FROM TEACHERS WHERE TEACHER_ID = :id";
    private final String INSERT_QUERY = "INSERT INTO TEACHERS (FULL_NAME) VALUES (:name)";
    private final RowMapper<Teacher> MAPPER = (rs, rowNum) -> new Teacher(
            rs.getInt("TEACHER_ID"),
            rs.getString("FULL_NAME")
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Teacher> findLikeName(String namePattern) {
        return jdbcTemplate.query(SELECT_BY_NAME, Map.of("name", namePattern + '%'), MAPPER);
    }

    public Optional<Teacher> findById(int id) {
        return jdbcTemplate.query(SELECT_BY_ID, Map.of("id", id), SqlUtils.singleExtractor(MAPPER));
    }

    public void create(CreateTeacherRequest request) {
        jdbcTemplate.update(INSERT_QUERY, Map.of("name", request.fullName()));
    }
}
