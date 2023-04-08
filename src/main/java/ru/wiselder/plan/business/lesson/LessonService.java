package ru.wiselder.plan.business.lesson;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.wiselder.plan.business.auditorium.AuditoriumDao;
import ru.wiselder.plan.business.faculty.FacultyDao;
import ru.wiselder.plan.business.teacher.TeacherDao;
import ru.wiselder.plan.exception.LessonIntersectionException;
import ru.wiselder.plan.exception.ObjectNotFoundException;
import ru.wiselder.plan.model.Lesson;
import ru.wiselder.plan.model.Week;
import ru.wiselder.plan.model.LessonInfo;
import ru.wiselder.plan.request.LessonRequest;
import ru.wiselder.plan.request.RequestWeek;

import static ru.wiselder.plan.exception.LessonIntersectionException.Type.AUDITORIUM;
import static ru.wiselder.plan.exception.LessonIntersectionException.Type.GROUP;
import static ru.wiselder.plan.exception.LessonIntersectionException.Type.TEACHER;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonDao storage;
    private final FacultyDao facultyDao;
    private final TeacherDao teacherDao;
    private final AuditoriumDao auditoriumDao;

    @Transactional
    public void addLesson(LessonRequest lesson) {
        checkIntersections(lesson);
        checkExisting(lesson);
        for (var week : toWeeks(lesson.week())) {
            storage.createLesson(new LessonInfo(
                    lesson.disciplineId(),
                    lesson.auditoriumId(),
                    lesson.teacherId(),
                    week,
                    lesson.day(),
                    lesson.bellOrdinal()
            ));
            var created = storage.getLessonByAuditorium(lesson.bellOrdinal(), lesson.day(), week, lesson.auditoriumId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
            storage.editGroups(created.id(), lesson.groupIds());
        }
    }

    @Transactional
    public void editLesson(int lessonId, LessonRequest lesson) {
        storage.getLessonById(lessonId)
                .orElseThrow(() -> new ObjectNotFoundException("lesson", lessonId));
        checkIntersections(lesson);
        checkExisting(lesson);
        for (var week : toWeeks(lesson.week())) {
            storage.editLesson(lessonId, new LessonInfo(
                    lesson.disciplineId(),
                    lesson.auditoriumId(),
                    lesson.teacherId(),
                    week,
                    lesson.day(),
                    lesson.bellOrdinal()
            ));
            storage.editGroups(lessonId, lesson.groupIds());
        }
    }

    public void removeLesson(int lessonId) {
        storage.removeLesson(lessonId);
    }

    public void checkIntersections(LessonRequest lesson) {
        for (var week : toWeeks(lesson.week())) {
            storage.getLessonByAuditorium(lesson.bellOrdinal(), lesson.day(), week, lesson.auditoriumId())
                    .ifPresent(l -> {
                        throw new LessonIntersectionException(AUDITORIUM, l);
                    });
            storage.getLessonByTeacher(lesson.bellOrdinal(), lesson.day(), week, lesson.teacherId())
                    .ifPresent(l -> {
                        throw new LessonIntersectionException(TEACHER, l);
                    });
            storage.getLessonByGroups(lesson.bellOrdinal(), lesson.day(), week, lesson.groupIds())
                    .ifPresent(l -> {
                        throw new LessonIntersectionException(GROUP, l);
                    });
        }
    }

    private void checkExisting(LessonRequest lesson) {
        teacherDao.findById(lesson.teacherId())
                .orElseThrow(() -> new ObjectNotFoundException("teacher", lesson.teacherId()));
        auditoriumDao.findById(lesson.auditoriumId())
                .orElseThrow(() -> new ObjectNotFoundException("auditorium", lesson.auditoriumId()));
        for (Integer groupId : lesson.groupIds()) {
            facultyDao.findGroupById(groupId)
                    .orElseThrow(() -> new ObjectNotFoundException("group", groupId));
        }
    }

    private Week[] toWeeks(RequestWeek week) {
        return switch (week) {
            case FIRST -> new Week[]{Week.FIRST};
            case SECOND -> new Week[]{Week.SECOND};
            case BOTH -> new Week[]{Week.FIRST, Week.SECOND};
        };
    }

    public Optional<Lesson> getLesson(int id) {
        return storage.getLessonById(id);
    }
}
