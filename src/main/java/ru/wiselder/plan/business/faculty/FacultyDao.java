package ru.wiselder.plan.business.faculty;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wiselder.plan.model.Faculty;
import ru.wiselder.plan.model.Group;
import ru.wiselder.plan.request.CreateFacultyRequest;
import ru.wiselder.plan.request.GroupRequest;
import ru.wiselder.plan.utils.SqlUtils;

@Repository
@RequiredArgsConstructor
public class FacultyDao {
    private final String SELECT_ALL = "SELECT * FROM FACULTIES";
    private final String ORDER_BY_NAME = " ORDER BY NAME";
    private final String SELECT_FACULTY_BY_NAME = SELECT_ALL + " WHERE NAME = :name";
    private final String SELECT_FACULTY_BY_ID = SELECT_ALL + " WHERE FACULTY_ID = :id";
    private final String INSERT_FACULTY_QUERY = "INSERT INTO FACULTIES (NAME) VALUES (:name)";
    private final String INSERT_GROUP_QUERY = """
            INSERT INTO GROUPS (FACULTY_ID, COURSE, NUMBER, SUB_NUMBER)
            VALUES (:facultyId, :course, :number, :subNumber)
            """;

    private final String SELECT_GROUP_QUERY = """
            SELECT * FROM GROUPS
            WHERE FACULTY_ID = :facultyId
              AND COURSE = :course
              AND NUMBER = :number
              AND SUB_NUMBER = :subNumber
            """;
    private final String SELECT_GROUP_BY_FACULTY = """
            SELECT * FROM GROUPS WHERE FACULTY_ID = :facultyId
            ORDER BY COURSE, NUMBER, SUB_NUMBER
            """;
    private final String SELECT_GROUP_BY_ID = "SELECT * FROM GROUPS WHERE GROUP_ID = :id";

    public static final RowMapper<Faculty> FACULTY_MAPPER = (rs, rowNum) -> new Faculty(
            rs.getInt("FACULTY_ID"),
            rs.getString("NAME")
    );
    public static final RowMapper<Group> GROUP_MAPPER = (rs, rowNum) -> new Group(
            rs.getInt("GROUP_ID"),
            rs.getInt("FACULTY_ID"),
            rs.getInt("COURSE"),
            rs.getInt("NUMBER"),
            rs.getInt("SUB_NUMBER")
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Faculty> findAll() {
        return jdbcTemplate.query(SELECT_ALL + ORDER_BY_NAME, FACULTY_MAPPER);
    }

    public void create(CreateFacultyRequest faculty) {
        jdbcTemplate.update(INSERT_FACULTY_QUERY, Map.of("name", faculty.name()));

    }
    public List<Group> findGroupsByFaculty(int facultyId) {
        return jdbcTemplate.query(SELECT_GROUP_BY_FACULTY, Map.of("facultyId", facultyId), GROUP_MAPPER);
    }

    public void createGroup(int facultyId, GroupRequest request) {
        jdbcTemplate.update(INSERT_GROUP_QUERY, mapOf(facultyId, request));
    }

    public Optional<Group> findGroup(int facultyId, GroupRequest request) {
        return jdbcTemplate.query(SELECT_GROUP_QUERY, mapOf(facultyId, request), SqlUtils.singleExtractor(GROUP_MAPPER));
    }

    public Optional<Faculty> findByName(String name) {
        return jdbcTemplate.query(SELECT_FACULTY_BY_NAME, Map.of("name", name), SqlUtils.singleExtractor(FACULTY_MAPPER));
    }

    public Optional<Faculty> findById(int id) {
        return jdbcTemplate.query(SELECT_FACULTY_BY_ID, Map.of("id", id), SqlUtils.singleExtractor(FACULTY_MAPPER));
    }

    private Map<String, Object> mapOf(int facultyId, GroupRequest request) {
        return Map.of(
                "facultyId", facultyId,
                "course", request.course(),
                "number", request.number(),
                "subNumber", request.subNumber()
        );
    }

    public Optional<Group> findGroupById(int id) {
        return jdbcTemplate.query(SELECT_GROUP_BY_ID, Map.of("id", id), SqlUtils.singleExtractor(GROUP_MAPPER));
    }
}
