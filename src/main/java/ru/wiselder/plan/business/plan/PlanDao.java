package ru.wiselder.plan.business.plan;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wiselder.plan.business.lesson.LessonDao;
import ru.wiselder.plan.model.Lesson;
import ru.wiselder.plan.request.GroupDayPlanRequest;
import ru.wiselder.plan.request.GroupRequest;
import ru.wiselder.plan.request.TeacherDayPlanRequest;
import ru.wiselder.plan.request.TeacherRequest;

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
    private static final String PREDICATE_BY_WEEK_AND_DAY = """
              AND l.WEEK_NUMBER = :week
              AND l.WEEK_DAY = :day
            """;
    private static final String SELECT_BY_GROUP_AND_DAY = SELECT_BY_GROUP + PREDICATE_BY_WEEK_AND_DAY;
    private static final String SELECT_BY_TEACHER_AND_DAY = SELECT_BY_TEACHER + PREDICATE_BY_WEEK_AND_DAY;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Lesson> findLessonsByGroup(GroupRequest request) {
        return jdbcTemplate.query(SELECT_BY_GROUP, Map.of(
                "faculty", request.faculty(),
                "course", request.course(),
                "number", request.number(),
                "subNumber", request.subNumber()
        ), LessonDao.MAPPER);
    }

    public List<Lesson> findLessonsGroupAndDay(GroupDayPlanRequest request) {
        return jdbcTemplate.query(SELECT_BY_GROUP_AND_DAY, Map.of(
                "faculty", request.group().faculty(),
                "course", request.group().course(),
                "number", request.group().number(),
                "subNumber", request.group().subNumber(),
                "week", request.week().getValue(),
                "day", request.day().getValue()
        ), LessonDao.MAPPER);
    }

    public List<Lesson> findLessonsByTeacher(TeacherRequest request) {
        return jdbcTemplate.query(SELECT_BY_TEACHER, Map.of("teacherId", request.id()), LessonDao.MAPPER);
    }

    public List<Lesson> findLessonsByTeacherAndDay(TeacherDayPlanRequest request) {
        return jdbcTemplate.query(SELECT_BY_TEACHER_AND_DAY, Map.of(
                "teacherId", request.teacherId(),
                "week", request.week().getValue(),
                "day", request.day().getValue()
        ), LessonDao.MAPPER);
    }
}