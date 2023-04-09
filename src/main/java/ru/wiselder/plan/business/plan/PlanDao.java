package ru.wiselder.plan.business.plan;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wiselder.plan.business.faculty.FacultyDao;
import ru.wiselder.plan.business.lesson.LessonDao;
import ru.wiselder.plan.model.Bell;
import ru.wiselder.plan.model.Group;
import ru.wiselder.plan.model.Lesson;
import ru.wiselder.plan.request.GroupDayPlanRequest;
import ru.wiselder.plan.request.GroupWeekPlanRequest;
import ru.wiselder.plan.request.TeacherDayPlanRequest;
import ru.wiselder.plan.request.TeacherWeekPlanRequest;

@Repository
@RequiredArgsConstructor
public class PlanDao {
    private static final String SELECT_BY_GROUP = """
            SELECT l.*, t.*, a.*, d.*, b.*
            FROM GROUPS g
            JOIN GROUPLESSONS gl ON g.GROUP_ID = gl.GROUP_ID
            JOIN LESSONS l ON l.LESSON_ID = gl.LESSON_ID
            JOIN TEACHERS t ON l.TEACHER_ID = t.TEACHER_ID
            JOIN AUDITORIUMS a ON l.AUDITORIUM_ID = a.AUDITORIUM_ID
            JOIN DISCIPLINES d ON l.DISCIPLINE_ID = d.DISCIPLINE_ID
            JOIN BELLS b ON l.BELL_ID = b.ORDINAL_ID
            WHERE g.FACULTY_ID = :faculty
              AND g.COURSE = :course
              AND g.NUMBER = :number
              AND g.SUB_NUMBER = :subNumber
            """;
    private static final String SELECT_BY_TEACHER = "SELECT * FROM LESSONS l WHERE l.TEACHER_ID = :teacherId";
    private static final String PREDICATE_BY_DAY = " AND l.WEEK_DAY = :day";
    private static final String SELECT_BY_GROUP_AND_DAY = SELECT_BY_GROUP + PREDICATE_BY_DAY;
    private static final String SELECT_BY_TEACHER_AND_DAY = SELECT_BY_TEACHER + PREDICATE_BY_DAY;
    private static final String SELECT_GROUPS_BY_LESSON = """
            SELECT g.* FROM GROUPS g
            JOIN GROUPLESSONS gl ON gl.GROUP_ID = g.GROUP_ID
            WHERE gl.LESSON_ID = :lessonId
            """;
    private static final RowMapper<Bell> BELL_MAPPER = (rs, rn) -> new Bell(
            rs.getInt("ORDINAL_ID"),
            rs.getTime("START").toLocalTime(),
            rs.getTime("FINISH").toLocalTime()
    );
    private static final String SELECT_BELLS = "SELECT * FROM BELLS ORDER BY START";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Lesson> findLessons(GroupWeekPlanRequest request) {
        return jdbcTemplate.query(SELECT_BY_GROUP, Map.of(
                "faculty", request.faculty(),
                "course", request.course(),
                "number", request.number(),
                "subNumber", request.subNumber()
        ), LessonDao.MAPPER);
    }

    public List<Lesson> findLessons(GroupDayPlanRequest request) {
        return jdbcTemplate.query(SELECT_BY_GROUP_AND_DAY, Map.of(
                "faculty", request.group().faculty(),
                "course", request.group().course(),
                "number", request.group().number(),
                "subNumber", request.group().subNumber(),
                "day", request.day().getValue()
        ), LessonDao.MAPPER);
    }

    public List<Lesson> findLessons(TeacherWeekPlanRequest request) {
        return jdbcTemplate.query(SELECT_BY_TEACHER, Map.of("teacherId", request.teacherId()), LessonDao.MAPPER);
    }

    public List<Lesson> findLessons(TeacherDayPlanRequest request) {
        return jdbcTemplate.query(SELECT_BY_TEACHER_AND_DAY, Map.of(
                "teacherId", request.teacherId(),
                "day", request.day().getValue()
        ), LessonDao.MAPPER);
    }

    public List<Group> getGroupsByLesson(int id) {
        return jdbcTemplate.query(SELECT_GROUPS_BY_LESSON, Map.of("lessonId", id), FacultyDao.GROUP_MAPPER);
    }

    public List<Bell> findAllBells() {
        return jdbcTemplate.query(SELECT_BELLS, BELL_MAPPER);
    }
}
