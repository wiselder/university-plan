package ru.wiselder.plan.business.lesson;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import ru.wiselder.plan.model.Auditorium;
import ru.wiselder.plan.model.Bell;
import ru.wiselder.plan.model.Discipline;
import ru.wiselder.plan.model.Lesson;
import ru.wiselder.plan.model.Teacher;
import ru.wiselder.plan.model.LessonInfo;
import ru.wiselder.plan.utils.SqlUtils;

@Repository
@RequiredArgsConstructor
public class LessonDao {
    private static final String REMOVE_LESSON = "DELETE FROM LESSONS WHERE LESSON_ID = :id";

    private static final String SELECT_BASE = """
            SELECT l.*, t.*, a.*, d.*, b.*
            FROM LESSONS l
            JOIN TEACHERS t ON l.TEACHER_ID = t.TEACHER_ID
            JOIN AUDITORIUMS a ON l.AUDITORIUM_ID = a.AUDITORIUM_ID
            JOIN DISCIPLINES d ON l.DISCIPLINE_ID = d.DISCIPLINE_ID
            JOIN BELLS b ON l.BELL_ID = b.ORDINAL_ID
            """;

    private static final String SELECT_BY_ID = SELECT_BASE + " WHERE l.LESSON_ID = :id";

    private static final String LESSON_TIME_PREDICATE = """
            WHERE l.BELL_ID = :bell AND l.WEEK_DAY = :day
            """;
    private static final String SELECT_LESSON_BY_TIME = SELECT_BASE + LESSON_TIME_PREDICATE;
    private static final String SELECT_LESSON_INTERSECTIONS_A = SELECT_LESSON_BY_TIME + """
              AND l.AUDITORIUM_ID = :auditoriumId
            """;
    private static final String SELECT_LESSON_INTERSECTIONS_T = SELECT_LESSON_BY_TIME + """
              AND l.TEACHER_ID = :teacherId
            """;
    private static final String SELECT_LESSON_INTERSECTIONS_G = SELECT_BASE + """
            JOIN GROUPLESSONS gl ON gl.LESSON_ID = l.LESSON_ID AND gl.GROUP_ID IN (:groupIds)
            """ + LESSON_TIME_PREDICATE;
    private static final String INSERT_LESSON = """
            INSERT INTO LESSONS (DISCIPLINE_ID, AUDITORIUM_ID, TEACHER_ID, WEEK_DAY, BELL_ID)
            VALUES (:disciplineId, :auditoriumId, :teacherId, :day, :bell)
            """;
    private static final String UPDATE_LESSON = """
            UPDATE LESSONS SET
                DISCIPLINE_ID = :disciplineId,
                AUDITORIUM_ID = :auditoriumId,
                TEACHER_ID = :teacherId,
                WEEK_DAY = :day,
                BELL_ID = :bell
            WHERE LESSON_ID = :id
            """;
    private static final String DELETE_GROUPS_FROM_LESSON = "DELETE FROM GROUPLESSONS WHERE LESSON_ID = :lessonId";
    private static final String INSERT_GROUPS_TO_LESSON = """
            INSERT INTO GROUPLESSONS (LESSON_ID, GROUP_ID) VALUES (:lessonId, :groupId)
            """;
    public static final RowMapper<Lesson> MAPPER = (rs, rowNum) -> new Lesson(
            rs.getInt("LESSON_ID"),
            new Discipline(
                    rs.getInt("DISCIPLINES.DISCIPLINE_ID"),
                    rs.getString("DISCIPLINES.NAME")
            ),
            new Auditorium(
                    rs.getInt("AUDITORIUMS.AUDITORIUM_ID"),
                    rs.getString("AUDITORIUMS.NAME"),
                    rs.getString("ADDRESS")
            ),
            new Teacher(rs.getInt("TEACHERS.TEACHER_ID"), rs.getString("NAME")),
            DayOfWeek.of(rs.getInt("WEEK_DAY")),
            new Bell(
                    rs.getInt("ORDINAL_ID"),
                    rs.getTime("START").toLocalTime(),
                    rs.getTime("FINISH").toLocalTime()
            )
    );
    private final NamedParameterJdbcTemplate jdbcTemplate;


    public Optional<Lesson> getLessonByAuditorium(int bell, DayOfWeek day,  int auditoriumId) {
        return jdbcTemplate.query(SELECT_LESSON_INTERSECTIONS_A, Map.of(
                "bell", bell,
                "day", day.getValue(),
                "auditoriumId", auditoriumId
        ), SqlUtils.singleExtractor(MAPPER));
    }

    public Optional<Lesson> getLessonByTeacher(int bell, DayOfWeek day,  int teacherId) {
        return jdbcTemplate.query(SELECT_LESSON_INTERSECTIONS_T, Map.of(
                "bell", bell,
                "day", day.getValue(),
                "teacherId", teacherId
        ), SqlUtils.singleExtractor(MAPPER));
    }

    public List<Lesson> getLessonByGroups(int bell, DayOfWeek day, Set<Integer> groupIds) {
        return jdbcTemplate.query(SELECT_LESSON_INTERSECTIONS_G, Map.of(
                "bell", bell,
                "day", day.getValue(),
                "groupIds", groupIds
        ), MAPPER);
    }

    public void createLesson(LessonInfo lesson) {
        jdbcTemplate.update(INSERT_LESSON, Map.of(
                "disciplineId", lesson.disciplineId(),
                "auditoriumId", lesson.auditoriumId(),
                "teacherId", lesson.teacherId(),
                "bell", lesson.bellOrdinal(),
                "day", lesson.day().getValue()
        ));
    }

    public void updateLesson(int lessonId, LessonInfo lesson) {
        jdbcTemplate.update(UPDATE_LESSON, Map.of(
                "id", lessonId,
                "disciplineId", lesson.disciplineId(),
                "auditoriumId", lesson.auditoriumId(),
                "teacherId", lesson.teacherId(),
                "bell", lesson.bellOrdinal(),
                "day", lesson.day().getValue()
        ));
    }
    public void resetGroups(int lessonId, Set<Integer> groupIds) {
        jdbcTemplate.update(DELETE_GROUPS_FROM_LESSON, Map.of("lessonId", lessonId));

        var batch = SqlParameterSourceUtils.createBatch(
                groupIds.stream()
                        .map(groupId -> Map.of("lessonId", lessonId, "groupId", groupId))
                        .toList()
        );
        jdbcTemplate.batchUpdate(INSERT_GROUPS_TO_LESSON, batch);
    }

    public int removeLesson(int lessonId) {
        jdbcTemplate.update(DELETE_GROUPS_FROM_LESSON, Map.of("lessonId", lessonId));
        return jdbcTemplate.update(REMOVE_LESSON, Map.of("id", lessonId));
    }

    public Optional<Lesson> getLessonById(int id) {
        return jdbcTemplate.query(SELECT_BY_ID, Map.of("id", id), SqlUtils.singleExtractor(MAPPER));
    }
}
